package ServeurGeneriqueTCP.reponses;

import javax.crypto.SecretKey;
import java.io.Serializable;

public class ReponseVerifIdClient extends ReponseBSPP implements Serializable
{
    private SecretKey secretKey;


    public ReponseVerifIdClient(boolean success, String message, SecretKey secretKey)
    {
        super(success, message);
        this.secretKey = secretKey;
    }

    public SecretKey getSecretKey()
    {
        return secretKey;
    }
}
