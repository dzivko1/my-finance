package hr.ferit.dominikzivko.myfinance;

import androidx.annotation.StringRes;

public class ActionFailedException extends Exception {

    @StringRes
    private int messageResID;

    public ActionFailedException() {
    }

    public ActionFailedException(String message) {
        super(message);
    }

    public ActionFailedException(@StringRes int messageResID) {
        this.messageResID = messageResID;
    }

    public int getMessageResID() {
        return messageResID;
    }
}
