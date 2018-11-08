package ssh;/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */

import lib.multi.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class UserAuthKI {
    boolean flag=false;

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
            session.connect();
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
                //*********************************************
                execCommand(command,channel);
                // execCommand("(" + command + "&); echo PID=$!", channel);

            }

            //********************************************
            if (channel.isConnected())
                flag = true;
            if (stopafterConnect){
                //channel.disconnect();
                /*
                channel.getOutputStream().close();
                channel.getInputStream().close();*/
                session.disconnect();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        finally {
            if (flag)
                JSch.getLogger().log(lib.multi.Logger.INFO,"SUCCESS");
        }
    }

    private void execCommand(String command, Channel channel) throws IOException, JSchException {
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
            }
            if (channel.isClosed()) {
                System.out.println("exit-status: " + channel.getExitStatus());
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception ee) {

            }
        }
    }


}
