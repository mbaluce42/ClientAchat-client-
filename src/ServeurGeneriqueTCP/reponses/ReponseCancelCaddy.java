package ServeurGeneriqueTCP.reponses;

import MODEL.networking.MyCrypto;

public class ReponseCancelCaddy extends ReponseBSPP {
    private byte[] hmac;

    public ReponseCancelCaddy(boolean success, String message,byte[] hmac) throws Exception {
        super(success, message);
        this.hmac=hmac;
    }

    public byte[] getHmac() {
        return hmac;
    }
}
