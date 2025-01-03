package MODEL.networking;

import java.io.FileInputStream;
import java.security.*;
import java.security.cert.Certificate;

public class KeystoreManager
{
    private KeyStore keystore;
    private String keystorePassword;
    private String keyPassword;

    public KeystoreManager(String keystorePath, String keystorePassword, String keyPassword) throws Exception {
        this.keystorePassword = keystorePassword;
        this.keyPassword = keyPassword;
        loadKeystore(keystorePath);
    }

    private void loadKeystore(String path) throws Exception
    {
        keystore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(path))
        {
            keystore.load(fis, keystorePassword.toCharArray());
        }
    }

    public PrivateKey getPrivateKey(String alias) throws Exception
    {
        return (PrivateKey) keystore.getKey(alias, keyPassword.toCharArray());
    }

    public Certificate getCertificate(String alias) throws Exception
    {
        return keystore.getCertificate(alias);
    }

    public PublicKey getPublicKey(String alias) throws Exception
    {
        Certificate cert = getCertificate(alias);
        return cert != null ? cert.getPublicKey() : null;
    }
}