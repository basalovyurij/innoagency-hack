package org.innoagencyhack.ocrparser.extractors.scans;

import java.util.ArrayList;

public class MathHelper {

    public static double getMean(ArrayList<Double> array) {
        double sum = 0;
        for (int i = 0; i < array.size(); i++) {
            sum += array.get(i);
        }
        return sum / array.size();
    }

    public static double getStandartDeviation(ArrayList<Double> array) {
        double mean = MathHelper.getMean(array);
        double sum = 0;
        for (int i = 0; i < array.size(); i++) {
            sum += Math.pow(array.get(i) - mean, 2);
        }
        double variance = sum / array.size();
        double deviation = Math.sqrt(variance);
        return deviation;
    }
}
