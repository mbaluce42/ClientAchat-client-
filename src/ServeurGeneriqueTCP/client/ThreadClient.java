package ServeurGeneriqueTCP.client;

import ServeurGeneriqueTCP.exception.FinConnexionException;
import ServeurGeneriqueTCP.protocol.Protocole;
import ServeurGeneriqueTCP.protocol.Reponse;
import ServeurGeneriqueTCP.protocol.Requete;
import ServeurGeneriqueTCP.utils.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class ThreadClient extends Thread
{
    protected Protocole protocole;
    protected Socket csocket;
    protected Logger logger;
    private int numero;

    private static int numCourant = 1;

    // Constructeur pour mode "à la demande"
    protected ThreadClient(Protocole protocole, Socket csocket, Logger logger) throws IOException
    {
        super("TH Client " + numCourant + " (protocole=" + protocole.getNom() + ")");
        this.protocole = protocole;
        this.csocket = csocket;
        this.logger = logger;
        this.numero = numCourant++;
    }

    // Constructeur pour mode "pool"
    protected ThreadClient(Protocole protocole, ThreadGroup groupe, Logger logger) throws IOException
    {
        super(groupe, "TH Client " + numCourant + " (protocole=" + protocole.getNom() + ")");
        this.protocole = protocole;
        this.csocket = null;  // La socket sera assignée plus tard
        this.logger = logger;
        this.numero = numCourant++;
    }

    @Override
    public void run()
    {
        try
        {
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;

            try
            {
                ois = new ObjectInputStream(csocket.getInputStream());
                oos = new ObjectOutputStream(csocket.getOutputStream());

                while(true)
                {
                    Requete requete = (Requete)ois.readObject();
                    Reponse reponse = protocole.TraiteRequete(requete, csocket);
                    if(reponse != null)
                    {
                        oos.writeObject(reponse);
                    }
                }
            }
            catch(FinConnexionException ex)
            {
                logger.Trace("Fin connexion demandée par protocole");
                if(oos != null && ex.getReponse() != null) // Envoi d'une réponse avant de fermer la connexion
                {
                    oos.writeObject(ex.getReponse());
                }
            }
        }
        catch(Exception ex )
        {
            logger.Trace("Erreur: " + ex.getMessage());

        }
    }
}