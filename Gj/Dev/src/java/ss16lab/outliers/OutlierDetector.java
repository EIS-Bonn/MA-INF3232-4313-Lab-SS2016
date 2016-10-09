/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss16lab.outliers;

import java.util.List;

/**
 * Composes an outlier method Class and returns the outliers
 *
 * @author Gj
 */
public class OutlierDetector {

    OutlierMethod om;
    public List<Double> outliers;

    public OutlierDetector(OutlierMethod om) {
        this.om = om;
        this.outliers = om.run();
    }
}
