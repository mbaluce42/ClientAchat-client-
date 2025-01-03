package ServeurGeneriqueTCP.requetes;

import MODEL.networking.MyCrypto;

import java.io.*;

//client_id#book_id#quantity
public class RequeteAddCaddyItem extends RequeteBSPP
{
    /*private int idClient;
    private int idBook;
    private int quantity;*/

    private byte[] data;

    public RequeteAddCaddyItem(int idClient, int idBook, int quantity) throws Exception {
        super("ADD_CADDY_ITEM");
        /*this.idClient = idClient;
        this.idBook = idBook;
        this.quantity = quantity;*/

        //crypte les donnees
        //Contruction du vecteur de bytes du message clair
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(idClient);
        dos.writeInt(idBook);
        dos.writeInt(quantity);

        this.data = baos.toByteArray();//recupere le message clair

        //cryptage des donnees
        data = MyCrypto.CryptSymDES(MyCrypto.RecupereCleSecrete(), data);

    }

    public int getIdClient() throws Exception
    {
        //decrypte les donnees
        byte[] dataDecrypt = MyCrypto.DecryptSymDES(MyCrypto.RecupereCleSecrete(), this.data);

        // Récupération des données claires
        ByteArrayInputStream bais = new ByteArrayInputStream(dataDecrypt);
        DataInputStream dis = new DataInputStream(bais);
        int idClient = 0;
        idClient= dis.readInt();
        return idClient;

    }

    public int getIdBook() throws Exception
    {
        //decrypte les donnees
        byte[] dataDecrypt = MyCrypto.DecryptSymDES(MyCrypto.RecupereCleSecrete(), this.data);

        // Récupération des données claires
        ByteArrayInputStream bais = new ByteArrayInputStream(dataDecrypt);
        DataInputStream dis = new DataInputStream(bais);
        int idclient = 0;
        int idBook = 0;
        idclient= dis.readInt();
        idBook = dis.readInt();
        return idBook;
    }

    public int getQuantity() throws Exception
    {
        //decrypte les donnees
        byte[] dataDecrypt = MyCrypto.DecryptSymDES(MyCrypto.RecupereCleSecrete(), this.data);

        // Récupération des données claires
        ByteArrayInputStream bais = new ByteArrayInputStream(dataDecrypt);
        DataInputStream dis = new DataInputStream(bais);
        int idclient = 0;
        int idBook = 0;
        int quantity = 0;
        idclient= dis.readInt();
        idBook = dis.readInt();
        quantity = dis.readInt();
        return quantity;
    }

}
