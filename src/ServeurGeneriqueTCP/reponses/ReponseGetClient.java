package ServeurGeneriqueTCP.reponses;

public class ReponseGetClient extends ReponseBSPP
{
    //private int idClient;
    private byte[] selServeur;

    public ReponseGetClient(boolean success, String message, byte[] selServeur)
    {
        super(success, message);
        this.selServeur = selServeur;
    }

    public byte[] getSelServeur()
    {
        return selServeur;
    }

    /*public int getIdClient()
    {
        return idClient;
    }*/

}
