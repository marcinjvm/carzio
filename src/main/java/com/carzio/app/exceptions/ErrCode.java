package com.carzio.app.exceptions;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public enum ErrCode {
    USER_OF_ID_NOT_FOUND,
    CAR_OF_ID_NOT_FOUND,
    CARS_OF_USER_OF_ID_NOT_FOUND,
    ROLE_NOT_FOUND;


    public String getMessage(String message, Locale locale) {
        String messages = ResourceBundle.getBundle("messages", locale)
                .getString(this.toString().toLowerCase());
        return MessageFormat.format(messages, message);
    }

    public String getMessage(Locale locale) {
        return ResourceBundle.getBundle("messages", locale)
                .getString(this.toString().toLowerCase());
    }
}
