/**
 *
 */
public class ChangeFileNamesException extends RuntimeException {

    public ChangeFileNamesException () {
        this("Unknown error occurred.");
    }

    public ChangeFileNamesException (String msg) {
        super(msg);
    }

    public ChangeFileNamesException (String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
