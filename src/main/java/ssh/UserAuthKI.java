package ssh;/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */

import dap.entities.JPAService;
import dap.entities.actions.HistoryActions;
import dap.entities.actions.HistoryActionsRepository;
import lib.multi.*;
import org.apache.logging.log4j.LogManager;

import javax.persistence.EntityManager;
import java.io.*;
import java.sql.Date;
import java.util.Properties;

public class UserAuthKI {
    boolean flag=false;
    int exitStatus=-1;

    static org.apache.logging.log4j.Logger logger = LogManager.getLogger("elastic-generator");

    public static void main(String[] args) {
        UserAuthKI userAuthKI = new UserAuthKI();
        userAuthKI.connect(args,true,"ls -ldt *");
    }

    public boolean testConnect(String[] args) {
        connect(args,true,null);
        return flag;
    }

    public void connect(String[] args,boolean stopafterConnect,String command) {
        try {
            JSch.setLogger(new Logger.MyLogger());
            JSch jsch = new JSch();
            //jsch.setKnownHosts("192.168.1.98");
            jsch.setKnownHosts(new File("known_host").getAbsolutePath());
            String userName="pascal";
            if (args.length>0 && args[0] != null)
                userName = args[0];
            String hostName="192.168.91.128";
            if (args.length>1 && args[1] != null)
                hostName= args[1];

            String host = userName+ "@" + hostName;
            String user = host.substring(0, host.indexOf('@'));
            host = host.substring(host.indexOf('@') + 1);
            Session session = jsch.getSession(user, host, 22);

            UserInfo ui = new MyUserInfo();

            String password="pascal67";
            if (args.length>2 && args[2] != null)
                password=args[2];
            ((MyUserInfo) ui).passwd = password;
            session.setUserInfo(ui);
            HistoryActions hi = new HistoryActions();
            hi.setUserName(userName);
            int random= (int) (Math.random() * (Integer.MAX_VALUE - 1000000));
            java.util.Date d = new java.util.Date();
            Date newDate = new Date(d.getTime());
            hi.setPid(random);
            hi.setAction(command);
            hi.setServerName(host);;
            hi.setDateAction(newDate);
            HistoryActionsRepository.add(hi);
            EntityManager entityManager = JPAService.getFactory().createEntityManager();
            hi = HistoryActionsRepository.getByPid(random,entityManager);

            entityManager.close();


            logger.info("Id="+ hi.getId());
            session.connectWithId(hi.getId());

            Channel channel=null;
            if (command != null)
                channel = session.openChannel("exec");
            else
                channel = session.openChannel("shell");

            if (stopafterConnect) {
                channel.setInputStream(System.in, true);
                channel.setOutputStream(System.out, true);
            }
            else {
                channel.setInputStream(System.in);
                channel.setOutputStream(System.out);
            }

            if (command == null)
             channel.connect();
            else {
                execCommand(command,channel,hi.getId());
                hi.setDateEnd(new Date(new java.util.Date().getTime()));
                hi.setExitCode(this.exitStatus);
                HistoryActionsRepository.save(hi);

            }


            if (channel.isConnected())
                flag = true;
            if (stopafterConnect){
                session.disconnect();
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }
            IO io = new IO();

            String fileOut = IO.getLogFile(hi.getId());

            try {
                BufferedReader br = new BufferedReader(new FileReader(fileOut));
                String thisLine;
                while ((thisLine = br.readLine()) != null) {
                    if (thisLine.indexOf("PIDPROCESS") >=0)
                    {
                        hi.setPid(Integer.valueOf(thisLine.substring(11)));
                        HistoryActionsRepository.save(hi);
                        JPAService.getFactory().close();
                    }
                }
            }
            catch (IOException e) {
                logger.error("Error: " + e.getMessage());
            }

        } catch (Exception e) {
            logger.error("Error:" + e.getMessage());
            StackTraceElement[] stackTrace = e.getStackTrace();

            for (StackTraceElement element:e.getStackTrace())
            {
                logger.error(element.getClassName() +
                        "." + element.getMethodName()+
                        "(" + element.getLineNumber() + ")");
            }
        }
        finally {
            if (JPAService.getFactory().isOpen())
                JPAService.getFactory().close();
            if (flag)
                System.out.println("Command executed");
        }
    }

    private void execCommand(String command, Channel channel,int idOperation) throws IOException, JSchException {
        command=". /tmp/exec.sh \"" + command + "\";echo PIDPROCESS=$VAR";
        ((ChannelExec) channel).setCommand(command );
        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(System.err);
        InputStream in = channel.getInputStream();
        channel.connect();
        if (channel.isConnected())
            flag = true;
        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) break;
                System.out.print(new String(tmp, 0, i));
                if (idOperation !=0)
                {
                    String fileOut = IO.getLogFile(idOperation);
                    logger.info("Logging to " + fileOut);
                    FileOutputStream fos = new FileOutputStream(fileOut, true);
                    FileOutputStream outputStream = fos;
                    outputStream.write(new String(tmp, 0, i).getBytes());
                    outputStream.close();
                }
            }
            if (channel.isClosed()) {
                System.out.println("exit-status: " + channel.getExitStatus());
                exitStatus = channel.getExitStatus();
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception ee) {

            }
        }
    }

    static public String getLogFile(int idOperation) {
        Properties config = new Properties();
        String configFile = "config.properties";
        String directory = "/tmp";
        String file = "log";
        try {
            config.load(Thread.currentThread().
                    getContextClassLoader().
                    getResourceAsStream(configFile));
            if (config.getProperty("log.directory") != null)
                directory = config.getProperty("log.directory");
            if (config.getProperty("log.file") != null)
                file = config.getProperty("log.file");
        } catch (Exception e) {

        }
        return directory + "/" + file +"." + idOperation + ".out";
    }


}
