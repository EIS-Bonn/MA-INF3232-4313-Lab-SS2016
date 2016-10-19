/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import java.util.HashMap;
import javax.ejb.Stateful;

/**
 * EJB Class contains persistent data objects
 *
 * @author Gj
 */
@Stateful
public class DataHelper {

    /**
     * root directory of the running application
     */
    public static String rootDirectory = "";

    /**
     * HashMap to store cached results of former outlier detection processes
     *
     * @author Gj
     */
    public static HashMap<String, String> cachedOutlierResults = new HashMap<>();

}
