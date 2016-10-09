/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss16lab.outliers;

import java.util.List;

/**
 * This interface provides abstraction for applied outlier methods
 *
 * @author Gj
 */
public interface OutlierMethod {

    /**
     * Runs applied outlier method
     * @return list of outliers
     * @author Gj
     */
    public abstract List<Double> run();
}
