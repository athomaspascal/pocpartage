package ssh;/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */

import lib.multi.Channel;
import lib.multi.JSch;
import lib.multi.Session;
import lib.multi.UserInfo;

import java.io.File;

public class UserAuthKI {
    boolean flag=false;

    public static void main(String[] args) {
        UserAuthKI userAuthKI = new UserAuthKI();
        userAuthKI.connect(args,true);

        System.out.println("Resultat:" + userAuthKI.flag);
    }

    public boolean testConnect(String[] args) {
        connect(args,true);
        return flag;
    }

    public void connect(String[] args,boolean stopAfterCOnnect) {

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
            Channel channel = session.openChannel("shell");

            if (stopAfterCOnnect) {
                channel.setInputStream(System.in, true);
                channel.setOutputStream(System.out, true);
            }
            else {
                channel.setInputStream(System.in);
                channel.setOutputStream(System.out);
            }

            channel.connect();
            if (channel.isConnected())
                flag = true;
            if (stopAfterCOnnect){
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


}
