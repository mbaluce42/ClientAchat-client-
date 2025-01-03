package ServeurGeneriqueTCP.requetes;

import MODEL.networking.MyCrypto;

import java.io.*;

public class RequeteDelCaddyItem extends RequeteBSPP
{
    //private int idItem;
    byte[] data;

    public RequeteDelCaddyItem(int idItem) throws Exception
    {
        super("DEL_CADDY_ITEM");

        //crypte les donnees
        //Contruction du vecteur de bytes du message clair
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(idItem);

        this.data = baos.toByteArray();//recupere le message clair

        //cryptage des donnees
        data = MyCrypto.CryptSymDES(MyCrypto.RecupereCleSecrete(), data);

    }

    public int getIdItem() throws Exception
    {
        //decrypte les donnees
        byte[] dataDecrypt = MyCrypto.DecryptSymDES(MyCrypto.RecupereCleSecrete(), this.data);

        // Récupération des données claires
        ByteArrayInputStream bais = new ByteArrayInputStream(dataDecrypt);
        DataInputStream dis = new DataInputStream(bais);
        int idItem = 0;
        idItem= dis.readInt();
        return idItem;
    }

}
