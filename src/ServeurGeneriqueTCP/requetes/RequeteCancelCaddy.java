package ServeurGeneriqueTCP.requetes;

import MODEL.networking.MyCrypto;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.security.PrivateKey;

public class RequeteCancelCaddy extends RequeteBSPP {
    private int idClient;
    private byte[] signature;

    public RequeteCancelCaddy(int idClient, PrivateKey privateKey) throws Exception {
        super("CANCEL_CADDY");
        this.idClient = idClient;

        // Construction du message pour la signature
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(idClient);
        byte[] data = baos.toByteArray();

        // Signature des données
        this.signature = MyCrypto.sign(data, privateKey);
    }

    public int getIdClient() {
        return idClient;
    }

    public byte[] getSignature() {
        return signature;
    }
}