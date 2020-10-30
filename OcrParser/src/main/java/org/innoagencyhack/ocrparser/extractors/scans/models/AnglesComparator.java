package org.innoagencyhack.ocrparser.extractors.scans.models;

import java.util.Comparator;

public class AnglesComparator implements Comparator<Double> {

    private final double mean;

    public AnglesComparator(double mean) {
        this.mean = mean;
    }

    @Override
    public int compare(Double prev, Double next) {
        double prevDeviation = Math.pow(prev - this.mean, 2.0);
        double nextDeviation = Math.pow(next - this.mean, 2.0);
        return (int) (prevDeviation - nextDeviation);
    }
}
