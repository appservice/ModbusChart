package eu.luckyApp.modbus.exeptions;

/**
 * Created by lmochel on 2016-03-15.
 */
public class DeepCloneException extends Exception {
    public DeepCloneException(String message) {
        super(message);
    }

    public DeepCloneException(Throwable cause) {
        super(cause);
    }
}
