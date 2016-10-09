/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss16lab.outliers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KMeans implements OutlierMethod {

    public int NUM_CLUSTERS = 3;    // Total clusters.
    public int TOTAL_DATA = 9;      // Total data points.
    public static double min_value;
    public static double med_value;
    public static double highest_value;

    public double SAMPLES[][];

    private ArrayList<Data> dataSet = new ArrayList<Data>();
    private ArrayList<Centroid> centroids = new ArrayList<Centroid>();

    public KMeans(Double[] samples) {
        this.SAMPLES = new double[samples.length][2];
        this.TOTAL_DATA = samples.length;
        for (int i = 0; i < samples.length; i++) {
            Double sample = samples[i];
            this.SAMPLES[i][0] = sample;
            this.SAMPLES[i][1] = 0.0;
        }

        // med value .
        double sum = 0.0;
        for (int j = 0; j < samples.length; j++) {
            sum = sum + samples[j];
        }
        med_value = sum / samples.length;
        // highest value 
        highest_value = getMaxValue(samples);
        // min value :
        min_value = getMinValue(samples);
    }

    public static double getMinValue(Double[] array) {
        double minValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < minValue) {
                minValue = array[i];
            }
        }
        return minValue;
    }

    public static double getMaxValue(Double[] array) {
        double maxValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxValue = array[i];
            }
        }
        return maxValue;
    }

    public List<Double> run() {
        initialize();
        kMeanCluster();

        List<ArrayList<String>> List_clusers = new ArrayList<ArrayList<String>>();
        ArrayList<Integer> Cluster_size = new ArrayList<>();

        for (int i = 0; i < NUM_CLUSTERS; i++) {
            ArrayList<String> Cluster = new ArrayList<String>();

//            System.out.println("Cluster " + i + " includes:");
            for (int j = 0; j < TOTAL_DATA; j++) {
                if (dataSet.get(j).cluster() == i) {
//                    System.out.println("     (" + dataSet.get(j).X() + ", " + dataSet.get(j).Y() + ")");
                    Cluster.add("(" + dataSet.get(j).X() + ", " + dataSet.get(j).Y() + ")");
                }
            }
            List_clusers.add(Cluster);
            int size = Cluster.size();
            Cluster_size.add(size);
//            System.out.println();
        }

        for (int u = 0; u < Cluster_size.size(); u++) {
            if (Cluster_size.get(u) == 0) {
                Cluster_size.remove(u);
            }
        }
        int size_cluster_outlier = min(Cluster_size);

//        System.out.println("The cluster of outlier :----");
        ArrayList<Double> Final_output = new ArrayList<>();
        ArrayList<String> string_cluster = new ArrayList<String>();

        for (int a = 0; a < List_clusers.size(); a++) {

            if (List_clusers.get(a).size() == size_cluster_outlier) {

                for (int b = 0; b < List_clusers.get(a).size(); b++) {
                    String aString = List_clusers.get(a).get(b).toString();
                    string_cluster.add(aString);
                }
            }

        }

//        List<Double> outliers = new ArrayList();
        List<Double> outliers = new ArrayList();
        for (int x = 0; x < string_cluster.size(); x++) {

            String Value = string_cluster.get(x);

            String[] p1 = Value.split(",", 2);

            String tem_S_value1 = p1[0];
            String S_value1 = tem_S_value1.replace("(", "");
            double svalue_d1 = Double.parseDouble(S_value1);

            outliers.add(svalue_d1);

        }

        return outliers;
    }

    private int min(ArrayList<Integer> array) {
        int min = array.get(0);
        if (min > 0) {
            for (int i = 1; i < array.size(); i++) {
                if (array.get(i) < min) {
                    min = array.get(i);
                }
            }
        }
        return min;
    }

    private void initialize() {
//        System.out.println("Centroids initialized at:");

        // you have to find lowest  elemnt 
        // you have to find mid  elemnt 
        // you have to find high  elemnt 
        centroids.add(new Centroid(min_value, 0.0)); // lowest set.
        centroids.add(new Centroid(med_value, 0.0)); // Med set.
        centroids.add(new Centroid(highest_value, 0.0)); // highest set.
//        System.out.println("     (" + centroids.get(0).X() + ", " + centroids.get(0).Y() + ")");
//        System.out.println("     (" + centroids.get(1).X() + ", " + centroids.get(1).Y() + ")");
//        System.out.println("     (" + centroids.get(2).X() + ", " + centroids.get(2).Y() + ")");

        return;
    }

    private void kMeanCluster() {
        final double bigNumber = Math.pow(10, 90);    // some big number that's sure to be larger than our data range.
        double minimum = bigNumber;                   // The minimum value to beat. 
        double distance = 0.0;                        // The current minimum value.
        int sampleNumber = 0;
        int cluster = 0;
        boolean isStillMoving = true;
        Data newData = null;

        // Add in new data, one at a time, recalculating centroids with each new one. 
        while (dataSet.size() < TOTAL_DATA) {
            newData = new Data(SAMPLES[sampleNumber][0], SAMPLES[sampleNumber][1]);
            dataSet.add(newData);
            minimum = bigNumber;
            for (int i = 0; i < NUM_CLUSTERS; i++) {
                distance = dist(newData, centroids.get(i));
                if (distance < minimum) {
                    minimum = distance;
                    cluster = i;
                }
            }
            newData.cluster(cluster);

            // calculate new centroids.
            for (int i = 0; i < NUM_CLUSTERS; i++) {
                int totalX = 0;
                int totalY = 0;
                int totalInCluster = 0;
                for (int j = 0; j < dataSet.size(); j++) {
                    if (dataSet.get(j).cluster() == i) {
                        totalX += dataSet.get(j).X();
                        totalY += dataSet.get(j).Y();
                        totalInCluster++;
                    }
                }
                if (totalInCluster > 0) {
                    centroids.get(i).X(totalX / totalInCluster);
                    centroids.get(i).Y(totalY / totalInCluster);
                }
            }
            sampleNumber++;
        }

        // Now, keep shifting centroids until equilibrium occurs.
        while (isStillMoving) {
            // calculate new centroids.
            for (int i = 0; i < NUM_CLUSTERS; i++) {
                int totalX = 0;
                int totalY = 0;
                int totalInCluster = 0;
                for (int j = 0; j < dataSet.size(); j++) {
                    if (dataSet.get(j).cluster() == i) {
                        totalX += dataSet.get(j).X();
                        totalY += dataSet.get(j).Y();
                        totalInCluster++;
                    }
                }
                if (totalInCluster > 0) {
                    centroids.get(i).X(totalX / totalInCluster);
                    centroids.get(i).Y(totalY / totalInCluster);
                }
            }

            // Assign all data to the new centroids
            isStillMoving = false;

            for (int i = 0; i < dataSet.size(); i++) {
                Data tempData = dataSet.get(i);
                minimum = bigNumber;
                for (int j = 0; j < NUM_CLUSTERS; j++) {
                    distance = dist(tempData, centroids.get(j));
                    if (distance < minimum) {
                        minimum = distance;
                        cluster = j;
                    }
                }
                tempData.cluster(cluster);
                if (tempData.cluster() != cluster) {
                    tempData.cluster(cluster);
                    isStillMoving = true;
                }
            }
        }
        return;
    }

    /**
     * // Calculate Euclidean distance.
     *
     * @param d - Data object.
     * @param c - Centroid object.
     * @return - double value.
     */
    private double dist(Data d, Centroid c) {
        return Math.sqrt(Math.pow((c.Y() - d.Y()), 2) + Math.pow((c.X() - d.X()), 2));
    }

    public static void main(String[] args) {

        Double[] s = {53248.0, 60.0, 61.0, 62.0, 61.0, 57.0, 63.0, 62.0, 61.0, 58.0, 61.0, 60.0, 60.0, 50.0, 53.0, 64.0, 61.0, 55.0, 55.0,
            56.0, 61.0, 548.0, 67.0, 4234347.0, 62.0, 68.0, 57.0, 63.0, 72.0, 58.0};

        KMeans km = new KMeans(s);
        List<Double> l = km.run();
        l = new Chauvenet(s).run();
        return;
    }
}

class Data {

    private double mX = 0;
    private double mY = 0;
    private int mCluster = 0;

    public Data() {
        return;
    }

    public Data(double x, double y) {
        this.X(x);
        this.Y(y);
        return;
    }

    public void X(double x) {
        this.mX = x;
        return;
    }

    public double X() {
        return this.mX;
    }

    public void Y(double y) {
        this.mY = y;
        return;
    }

    public double Y() {
        return this.mY;
    }

    public void cluster(int clusterNumber) {
        this.mCluster = clusterNumber;
        return;
    }

    public int cluster() {
        return this.mCluster;
    }
}

class Centroid {

    private double mX = 0.0;
    private double mY = 0.0;

    public Centroid() {
        return;
    }

    public Centroid(double newX, double newY) {
        this.mX = newX;
        this.mY = newY;
        return;
    }

    public void X(double newX) {
        this.mX = newX;
        return;
    }

    public double X() {
        return this.mX;
    }

    public void Y(double newY) {
        this.mY = newY;
        return;
    }

    public double Y() {
        return this.mY;
    }
}
