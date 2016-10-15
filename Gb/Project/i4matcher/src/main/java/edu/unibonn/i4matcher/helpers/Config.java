package edu.unibonn.i4matcher.helpers;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by phil on 11.10.16.
 */
public final class Config {
    public static final String USER;
    public static final String PASS;
    public static final String SRV;
    public static final String SRVOPTS;
    static {
        Locale locale = Locale.ENGLISH;
        ResourceBundle dbconn = ResourceBundle.getBundle("dbconn",
                locale);
        USER = dbconn.getString("user");
        PASS = dbconn.getString("password");
        SRV = dbconn.getString("srv");
        SRVOPTS = dbconn.getString("srvopts");

    }
}
