package ss16lab.outliers;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

public class Chauvenet implements OutlierMethod {

    Double[] samples;

    public Chauvenet(Double[] samples) {
        this.samples = samples;
    }

    public static void main(String[] args) {
        Double[] s = {53248.0, 60.0, 61.0, 62.0, 61.0, 57.0, 63.0, 62.0, 61.0, 58.0, 61.0, 60.0, 60.0, 50.0, 53.0, 64.0, 61.0, 55.0, 55.0,
            56.0, 61.0, 548.0, 67.0, 4234347.0, 62.0, 68.0, 57.0, 63.0, 72.0, 58.0};

        Chauvenet c = new Chauvenet(s);
        List<Double> l = c.run();
        int x = 0;
    }

    public List<Double> run() {
        List<Double> ret = new ArrayList<>();
        Statistics st = new Statistics(ArrayUtils.toPrimitive(this.samples));
        int n = this.samples.length; // number of samples
        double norminv = norminv((1 / (4 * (double) n)));
        double std = st.getStdDev();
        double mean = st.getMean();
        double min = st.getMin();
        double max = st.getMax();

        for (int i = 0; i < samples.length; i++) {
            Double sample = samples[i];
            if (Math.abs((sample - mean) / std) >= norminv) {
                ret.add(sample);
            }
//            if (Math.random() < 0.5) {
//                ret.add(sample);
//            }
        }
        return ret;
    }

    double norminv(double pval) {

        double normsinv;
        double[] aval = {-3.969683028665376e+01, 2.209460984245205e+02, -2.759285104469687e+02, 1.383577518672690e+02, -3.066479806614716e+01, 2.506628277459239e+00};
        double[] bval = {-5.447609879822406e+01, 1.615858368580409e+02, -1.556989798598866e+02, 6.680131188771972e+01, -1.328068155288572e+01};
        double[] cval = {-7.784894002430293e-03, -3.223964580411365e-01, -2.400758277161838e+00, -2.549732539343734e+00, 4.374664141464968e+00, 2.938163982698783e+00};
        double[] dval = {7.784695709041462e-03, 3.224671290700398e-01, 2.445134137142996e+00, 3.754408661907416e+00};

        double pvallow = 0.02425;
        double pvalhigh = 1 - pvallow;

        if (pval < pvallow) {
            // Lower Part Region
            double qval = Math.sqrt(-2 * Math.log(pval));
            normsinv = (((((cval[0] * qval + cval[1]) * qval + cval[2]) * qval + cval[3]) * qval + cval[4]) * qval + cval[5]) / ((((dval[0] * qval + dval[1]) * qval + dval[2]) * qval + dval[3]) * qval + 1);
        } else if (pvalhigh < pval) {
            // Upper Part Region
            double qval = Math.sqrt(-2 * Math.log(1 - pval));
            normsinv = -(((((cval[0] * qval + cval[1]) * qval + cval[2]) * qval + cval[3]) * qval + cval[4]) * qval + cval[5]) / ((((dval[0] * qval + dval[1]) * qval + dval[2]) * qval + dval[3]) * qval + 1);
        } else {
            // Central Part Region
            double qval = pval - 0.5;
            double rval = qval * qval;
            normsinv = (((((aval[0] * rval + aval[1]) * rval + aval[2]) * rval + aval[3]) * rval + aval[4]) * rval + aval[5]) * qval / (((((bval[0] * rval + bval[1]) * rval + bval[2]) * rval + bval[3]) * rval + bval[4]) * rval + 1);
        }
        return Math.abs(normsinv);
    }
}
