package edu.unibonn.i4matcher.helpers.OPCUA;

public class NamingUtil {

    public static String createPropertyName(String prefix, String propName) {

        if(prefix == null || prefix.equals(""))
            return propName;
        else {
            StringBuilder sb = new StringBuilder();
            sb.append(prefix).append(Character.toUpperCase(propName.charAt(0)))
                    .append(propName.substring(1));
            return sb.toString();
        }
    }

}
