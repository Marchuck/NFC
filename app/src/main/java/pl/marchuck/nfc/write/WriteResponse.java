package pl.marchuck.nfc.write;

/**
 * Project "NFC"
 * <p>
 * Created by Lukasz Marczak
 * on 25.03.2017.
 */

class WriteResponse {
    private int status;
    private String message;

    WriteResponse(int Status, String Message) {
        this.status = Status;
        this.message = Message;
    }

    int getStatus() {
        return status;
    }

    String getMessage() {
        return message;
    }
}
