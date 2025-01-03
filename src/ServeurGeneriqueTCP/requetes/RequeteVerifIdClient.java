package ServeurGeneriqueTCP.requetes;

import java.security.MessageDigest;

public class RequeteVerifIdClient extends RequeteBSPP
{
    byte[] DigestClient;
    private String nom;
    private String prenom;

    public RequeteVerifIdClient(String nom,String prenom, byte[] digestClient) throws Exception
    {
        super("VERIF_ID_CLIENT");


        this.nom = nom;
        this.prenom = prenom;

        this.DigestClient = digestClient;
    }

    public String getNom()
    {
        return nom;
    }

    public String getPrenom()
    {
        return prenom;
    }

    public byte[] getDigestClient()
    {
        return DigestClient;
    }

}
