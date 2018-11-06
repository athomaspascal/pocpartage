/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2010 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright 
     notice, this list of conditions and the following disclaimer in 
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package lib.multi;

import java.io.InputStream;
import java.util.Vector;

public class JSch {
    static java.util.Hashtable config = new java.util.Hashtable();

    static {
//  config.put("kex", "diffie-hellman-group-exchange-sha1");
        config.put("kex", "diffie-hellman-group1-sha1,diffie-hellman-group-exchange-sha1");
        config.put("server_host_key", "ssh-rsa,ssh-dss");
//    config.put("server_host_key", "ssh-dss,ssh-rsa");

        config.put("cipher.s2c",
                "aes128-ctr,aes128-cbc,3des-ctr,3des-cbc,blowfish-cbc,aes192-cbc,aes256-cbc");
        config.put("cipher.c2s",
                "aes128-ctr,aes128-cbc,3des-ctr,3des-cbc,blowfish-cbc,aes192-cbc,aes256-cbc");

        config.put("mac.s2c", "hmac-md5,hmac-sha1,hmac-sha1-96,hmac-md5-96");
        config.put("mac.c2s", "hmac-md5,hmac-sha1,hmac-sha1-96,hmac-md5-96");
        config.put("compression.s2c", "none");
        // config.put("compression.s2c", "zlib@openssh.com,zlib,none");
        config.put("compression.c2s", "none");
        // config.put("compression.c2s", "zlib@openssh.com,zlib,none");

        config.put("lang.s2c", "");
        config.put("lang.c2s", "");

        config.put("compression_level", "6");

        config.put("diffie-hellman-group-exchange-sha1", "lib.multi.DHGEX");
        config.put("diffie-hellman-group1-sha1", "lib.multi.DHG1");

        config.put("dh", "lib.multi.jce.DH");
        config.put("3des-cbc", "lib.multi.jce.TripleDESCBC");
        config.put("blowfish-cbc", "lib.multi.jce.BlowfishCBC");
        config.put("hmac-sha1", "lib.multi.jce.HMACSHA1");
        config.put("hmac-sha1-96", "lib.multi.jce.HMACSHA196");
        config.put("hmac-md5", "lib.multi.jce.HMACMD5");
        config.put("hmac-md5-96", "lib.multi.jce.HMACMD596");
        config.put("sha-1", "lib.multi.jce.SHA1");
        config.put("md5", "lib.multi.jce.MD5");
        config.put("signature.dss", "lib.multi.jce.SignatureDSA");
        config.put("signature.rsa", "lib.multi.jce.SignatureRSA");
        config.put("keypairgen.dsa", "lib.multi.jce.KeyPairGenDSA");
        config.put("keypairgen.rsa", "lib.multi.jce.KeyPairGenRSA");
        config.put("random", "lib.multi.jce.Random");

        config.put("none", "lib.multi.CipherNone");

        config.put("aes128-cbc", "lib.multi.jce.AES128CBC");
        config.put("aes192-cbc", "lib.multi.jce.AES192CBC");
        config.put("aes256-cbc", "lib.multi.jce.AES256CBC");

        config.put("aes128-ctr", "lib.multi.jce.AES128CTR");
        config.put("aes192-ctr", "lib.multi.jce.AES192CTR");
        config.put("aes256-ctr", "lib.multi.jce.AES256CTR");
        config.put("3des-ctr", "lib.multi.jce.TripleDESCTR");
        config.put("arcfour", "lib.multi.jce.ARCFOUR");
        config.put("arcfour128", "lib.multi.jce.ARCFOUR128");
        config.put("arcfour256", "lib.multi.jce.ARCFOUR256");

        config.put("userauth.none", "lib.multi.UserAuthNone");
        config.put("userauth.password", "lib.multi.UserAuthPassword");
        config.put("userauth.keyboard-interactive", "lib.multi.UserAuthKeyboardInteractive");
        config.put("userauth.publickey", "lib.multi.UserAuthPublicKey");
        config.put("userauth.gssapi-with-mic", "lib.multi.UserAuthGSSAPIWithMIC");
        config.put("gssapi-with-mic.krb5", "lib.multi.jgss.GSSContextKrb5");

        config.put("zlib", "lib.multi.jcraft.Compression");
        config.put("zlib@openssh.com", "lib.multi.jcraft.Compression");

        config.put("StrictHostKeyChecking", "ask");
        config.put("HashKnownHosts", "no");
        //config.put("HashKnownHosts",  "yes");
        config.put("PreferredAuthentications", "gssapi-with-mic,publickey,keyboard-interactive,password");

        config.put("CheckCiphers", "aes256-ctr,aes192-ctr,aes128-ctr,aes256-cbc,aes192-cbc,aes128-cbc,3des-ctr,arcfour,arcfour128,arcfour256");
    }

    Vector pool = new Vector();
    Vector identities = new Vector();
    private HostKeyRepository known_hosts = null;

    private static final Logger DEVNULL = new Logger() {
        public boolean isEnabled(int level) {
            return false;
        }

        public void log(int level, String message) {
        }
    };
    static Logger logger = DEVNULL;

    public JSch() {

        try {
            String osname = (String) (System.getProperties().get("os.name"));
            if (osname != null && osname.equals("Mac OS X")) {
                config.put("hmac-sha1", "lib.multi.jcraft.HMACSHA1");
                config.put("hmac-md5", "lib.multi.jcraft.HMACMD5");
                config.put("hmac-md5-96", "lib.multi.jcraft.HMACMD596");
                config.put("hmac-sha1-96", "lib.multi.jcraft.HMACSHA196");
            }
        } catch (Exception e) {
        }

    }

    public Session getSession(String username, String host) throws JSchException {
        return getSession(username, host, 22);
    }

    public Session getSession(String username, String host, int port) throws JSchException {
        if (username == null) {
            throw new JSchException("username must not be null.");
        }
        if (host == null) {
            throw new JSchException("host must not be null.");
        }
        Session s = new Session(this);
        s.setUserName(username);
        s.setHost(host);
        s.setPort(port);
        //pool.addElement(s);
        return s;
    }

    protected void addSession(Session session) {
        synchronized (pool) {
            pool.addElement(session);
        }
    }

    protected boolean removeSession(Session session) {
        synchronized (pool) {
            return pool.remove(session);
        }
    }

    public void setHostKeyRepository(HostKeyRepository hkrepo) {
        known_hosts = hkrepo;
    }

    public void setKnownHosts(String filename) throws JSchException {
        if (known_hosts == null) known_hosts = new KnownHosts(this);
        if (known_hosts instanceof KnownHosts) {
            synchronized (known_hosts) {
                ((KnownHosts) known_hosts).setKnownHosts(filename);
            }
        }
    }

    public void setKnownHosts(InputStream stream) throws JSchException {
        if (known_hosts == null) known_hosts = new KnownHosts(this);
        if (known_hosts instanceof KnownHosts) {
            synchronized (known_hosts) {
                ((KnownHosts) known_hosts).setKnownHosts(stream);
            }
        }
    }

    public HostKeyRepository getHostKeyRepository() {
        if (known_hosts == null) known_hosts = new KnownHosts(this);
        return known_hosts;
    }

    public void addIdentity(String prvkey) throws JSchException {
        addIdentity(prvkey, (byte[]) null);
    }

    public void addIdentity(String prvkey, String passphrase) throws JSchException {
        byte[] _passphrase = null;
        if (passphrase != null) {
            _passphrase = Util.str2byte(passphrase);
        }
        addIdentity(prvkey, _passphrase);
        if (_passphrase != null)
            Util.bzero(_passphrase);
    }

    public void addIdentity(String prvkey, byte[] passphrase) throws JSchException {
        Identity identity = IdentityFile.newInstance(prvkey, null, this);
        addIdentity(identity, passphrase);
    }

    public void addIdentity(String prvkey, String pubkey, byte[] passphrase) throws JSchException {
        Identity identity = IdentityFile.newInstance(prvkey, pubkey, this);
        addIdentity(identity, passphrase);
    }

    public void addIdentity(String name, byte[] prvkey, byte[] pubkey, byte[] passphrase) throws JSchException {
        Identity identity = IdentityFile.newInstance(name, prvkey, pubkey, this);
        addIdentity(identity, passphrase);
    }

    public void addIdentity(Identity identity, byte[] passphrase) throws JSchException {
        if (passphrase != null) {
            try {
                byte[] goo = new byte[passphrase.length];
                System.arraycopy(passphrase, 0, goo, 0, passphrase.length);
                passphrase = goo;
                identity.setPassphrase(passphrase);
            } finally {
                Util.bzero(passphrase);
            }
        }
        synchronized (identities) {
            if (!identities.contains(identity)) {
                identities.addElement(identity);
            }
        }
    }

    public void removeIdentity(String name) throws JSchException {
        synchronized (identities) {
            for (int i = 0; i < identities.size(); i++) {
                Identity identity = (Identity) (identities.elementAt(i));
                if (!identity.getName().equals(name))
                    continue;
                identities.removeElement(identity);
                identity.clear();
                break;
            }
        }
    }

    public Vector getIdentityNames() throws JSchException {
        Vector foo = new Vector();
        synchronized (identities) {
            for (int i = 0; i < identities.size(); i++) {
                Identity identity = (Identity) (identities.elementAt(i));
                foo.addElement(identity.getName());
            }
        }
        return foo;
    }

    public void removeAllIdentity() throws JSchException {
        synchronized (identities) {
            Vector foo = getIdentityNames();
            for (int i = 0; i < foo.size(); i++) {
                String name = ((String) foo.elementAt(i));
                removeIdentity(name);
            }
        }
    }

    public static String getConfig(String key) {
        synchronized (config) {
            return (String) (config.get(key));
        }
    }

    public static void setConfig(java.util.Hashtable newconf) {
        synchronized (config) {
            for (java.util.Enumeration e = newconf.keys(); e.hasMoreElements(); ) {
                String key = (String) (e.nextElement());
                config.put(key, (String) (newconf.get(key)));
            }
        }
    }

    public static void setConfig(String key, String value) {
        config.put(key, value);
    }

    public static void setLogger(Logger logger) {
        if (logger == null) JSch.logger = DEVNULL;
        JSch.logger = logger;
    }

    static Logger getLogger() {
        return logger;
    }
}