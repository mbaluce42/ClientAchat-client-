package ServeurGeneriqueTCP.requetes;

import MODEL.networking.MyCrypto;

import java.io.*;
import java.security.PrivateKey;

public class RequetePayCaddy extends RequeteBSPP
{
    //private int idClient;
    private byte[] data;
    private byte[] signature;

    public RequetePayCaddy(int idClient, String cardNumber, String cardName, PrivateKey privateKey) throws Exception
    {
        super("PAY_CADDY");

        // Construction du message avec les données de carte
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(idClient);
        dos.writeUTF(cardNumber);
        dos.writeUTF(cardName);

        // Récupère les données en clair
        byte[] clearData = baos.toByteArray();

        // Signer uniquement l'ID client (et non les données sensibles de la carte)
        ByteArrayOutputStream baosId = new ByteArrayOutputStream();
        DataOutputStream dosId = new DataOutputStream(baosId);
        dosId.writeInt(idClient);
        this.signature = MyCrypto.sign(baosId.toByteArray(), privateKey);

        // Crypter toutes les données
        this.data = MyCrypto.CryptSymDES(MyCrypto.RecupereCleSecrete(), clearData);
    }



    public int getIdClient() throws Exception {
        //decrypte les donnees
        byte[] dataDecrypt = MyCrypto.DecryptSymDES(MyCrypto.RecupereCleSecrete(), this.data);

        // Récupération des données claires
        ByteArrayInputStream bais = new ByteArrayInputStream(dataDecrypt);
        DataInputStream dis = new DataInputStream(bais);
        int idClient = 0;
        idClient= dis.readInt();
        return idClient;
    }

    public String getCardNumber() throws Exception
    {
        //decrypte les donnees
        byte[] dataDecrypt = MyCrypto.DecryptSymDES(MyCrypto.RecupereCleSecrete(), this.data);

        // Récupération des données claires
        ByteArrayInputStream bais = new ByteArrayInputStream(dataDecrypt);
        DataInputStream dis = new DataInputStream(bais);
        int idClient = 0;
        idClient= dis.readInt();
        String cardNumber = dis.readUTF();
        return cardNumber;
    }

    public String getCardName() throws Exception
    {
        //decrypte les donnees
        byte[] dataDecrypt = MyCrypto.DecryptSymDES(MyCrypto.RecupereCleSecrete(), this.data);

        // Récupération des données claires
        ByteArrayInputStream bais = new ByteArrayInputStream(dataDecrypt);
        DataInputStream dis = new DataInputStream(bais);
        int idClient = 0;
        idClient= dis.readInt();
        String cardNumber = dis.readUTF();
        String cardName = dis.readUTF();
        return cardName;
    }

    public byte[] getSignature()
    {
        return signature;
    }

}
