package MODEL.networking;

import java.io.*;
import java.security.*;
import javax.crypto.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class GenereCleDES
{
    public static void main(String args[]) throws NoSuchAlgorithmException, IOException, NoSuchProviderException
    {
        Security.addProvider(new BouncyCastleProvider());
// Génération de la clé secrète
        KeyGenerator cleGen = KeyGenerator.getInstance("DES","BC");
        cleGen.init(new SecureRandom());
        SecretKey cle = cleGen.generateKey();
        System.out.println("***** Clé générée = " + cle.toString());
// Sérialisation de la clé secrète dans un fichier
        ObjectOutputStream oos = new ObjectOutputStream(new
                FileOutputStream("cleSecrete.ser"));
        oos.writeObject(cle);
        oos.close();
        System.out.println("Sérialisation de la clé dans le fichier cleSecrete.ser");
    }
}
