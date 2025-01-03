package MODEL.networking;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.util.*;

public class SecurityManager
{
    private static final String SYMMETRIC_ALGO = "DES";
    private static final String ASYMMETRIC_ALGO = "RSA";
    private static final String SIGNATURE_ALGO = "SHA256withRSA";
    private static final String HMAC_ALGO = "HmacSHA256";

    private static final byte[] vecteurInit = new byte[16];

    static
    {
        // Initialisation du vecteur au démarrage
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(vecteurInit);

        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    public static byte[] CryptSymAES(byte[] data, SecretKey cle) throws Exception
    {
        Security.addProvider(new BouncyCastleProvider());

        // Chiffrement avec le vecteurInit partagé
        Cipher chiffrement = Cipher.getInstance(SYMMETRIC_ALGO, "BC");
        chiffrement.init(Cipher.ENCRYPT_MODE, cle, new IvParameterSpec(vecteurInit));
        return chiffrement.doFinal(data);
    }

    public static byte[] decryptSymAES(byte[] data, SecretKey cle) throws Exception
    {
        Security.addProvider(new BouncyCastleProvider());

        // Déchiffrement avec le même vecteurInit
        Cipher chiffrement = Cipher.getInstance(SYMMETRIC_ALGO, "BC");
        chiffrement.init(Cipher.DECRYPT_MODE, cle, new IvParameterSpec(vecteurInit));
        return chiffrement.doFinal(data);
    }

    // Génération de signature
    public static byte[] sign(String data, PrivateKey privateKey) throws Exception
    {
        Signature signature = Signature.getInstance(SIGNATURE_ALGO);
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        return signature.sign();
    }

    // Vérification de signature
    public static boolean verifySignature(byte[] data, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGO);
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(signatureBytes);
    }

    // Génération de HMAC
    public static byte[] generateHMAC(String data, SecretKey key) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGO);
        mac.init(key);
        return mac.doFinal(data.getBytes());
    }

    // Génération de sel pour le digest
    public static byte[] generateSalt()
    {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    // Génération de clé symétrique de session
    public static SecretKey generateSessionKey() throws Exception
    {
        KeyGenerator keyGen = KeyGenerator.getInstance(SYMMETRIC_ALGO, "BC");
        keyGen.init(new SecureRandom());
        return keyGen.generateKey();
    }
}
