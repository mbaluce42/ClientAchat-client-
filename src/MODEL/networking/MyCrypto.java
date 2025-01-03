package MODEL.networking;


import java.io.*;
import java.security.*;
import javax.crypto.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import java.security.*;

public class MyCrypto {
    public static byte[] CryptSymDES(SecretKey cle, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Security.addProvider(new BouncyCastleProvider());
        Cipher chiffrementE = Cipher.getInstance("DES/ECB/PKCS5Padding", "BC");
        chiffrementE.init(Cipher.ENCRYPT_MODE, cle);
        return chiffrementE.doFinal(data);
    }

    public static byte[] DecryptSymDES(SecretKey cle, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Security.addProvider(new BouncyCastleProvider());
        Cipher chiffrementD = Cipher.getInstance("DES/ECB/PKCS5Padding", "BC");
        chiffrementD.init(Cipher.DECRYPT_MODE, cle);
        return chiffrementD.doFinal(data);
    }

    public static byte[] CryptAsymRSA(PublicKey cle, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Security.addProvider(new BouncyCastleProvider());
        Cipher chiffrementE = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        chiffrementE.init(Cipher.ENCRYPT_MODE, cle);
        return chiffrementE.doFinal(data);
    }

    public static byte[] DecryptAsymRSA(PrivateKey cle, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Security.addProvider(new BouncyCastleProvider());
        Cipher chiffrementD = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        chiffrementD.init(Cipher.DECRYPT_MODE, cle);
        return chiffrementD.doFinal(data);
    }

    //generer salt
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    //le client génère un digest à partir de son numéro de client et du sel reçu du serveur, digest qu’il envoie au serveur,
    public static byte[] generateDigest(String clientNumber, byte[] salt) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(clientNumber.getBytes());
        digest.update(salt);
        return digest.digest();
    }

    public static byte[] sign(byte[] data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    public static boolean verifySignature(byte[] data, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(signatureBytes);
    }


    public static byte[] generateHMAC(byte[] data, SecretKey key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
        return mac.doFinal(data);
    }

    public static boolean verifyHMAC(byte[] data, byte[] hmac, SecretKey key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
        byte[] computedHmac = mac.doFinal(data);
        return MessageDigest.isEqual(computedHmac, hmac);
    }

    public static SecretKey RecupereCleSecrete() throws Exception, IOException, ClassNotFoundException {
        // Désérialisation de la clé secrète du fichier
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("cleSecrete.ser"));
        SecretKey cle = (SecretKey) ois.readObject();
        ois.close();
        return cle;
    }

    //recup la cle publique, si elle existe pas, on la genere et on la met dans le fichier
    public static PublicKey RecupereClePublique() throws Exception, IOException, ClassNotFoundException {
        File fichierCle = new File("clePublique.ser");

        // Si le fichier existe, on récupère la clé
        if (fichierCle.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichierCle))) {
                return (PublicKey) ois.readObject();
            } catch (Exception e) {
                System.err.println("Erreur lors de la lecture de la clé publique: " + e.getMessage());
                // Si erreur de lecture, on génère une nouvelle paire de clés
                return genererEtSauvegarderCles().getPublic();
            }
        } else {
            // Si le fichier n'existe pas, on génère une nouvelle paire de clés
            return genererEtSauvegarderCles().getPublic();
        }
    }

    //recup la cle privee, si elle existe pas, on la genere et on la met dans le fichier
    public static PrivateKey RecupereClePrivee() throws Exception, IOException, ClassNotFoundException {
        File fichierCle = new File("clePrivee.ser");

        // Si le fichier existe, on récupère la clé
        if (fichierCle.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichierCle))) {
                return (PrivateKey) ois.readObject();
            } catch (Exception e) {
                System.err.println("Erreur lors de la lecture de la clé privée: " + e.getMessage());
                // Si erreur de lecture, on génère une nouvelle paire de clés
                return genererEtSauvegarderCles().getPrivate();
            }
        } else {
            // Si le fichier n'existe pas, on génère une nouvelle paire de clés
            return genererEtSauvegarderCles().getPrivate();
        }
    }

    // Méthode privée pour générer et sauvegarder une nouvelle paire de clés
    private static KeyPair genererEtSauvegarderCles() throws Exception {
        // Génération de la paire de clés RSA
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);  // Taille de clé recommandée pour RSA
        KeyPair pair = keyGen.generateKeyPair();

        // Sauvegarde de la clé privée
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("clePrivee.ser"))) {
            oos.writeObject(pair.getPrivate());
        }

        // Sauvegarde de la clé publique
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("clePublique.ser"))) {
            oos.writeObject(pair.getPublic());
        }

        return pair;
    }
}