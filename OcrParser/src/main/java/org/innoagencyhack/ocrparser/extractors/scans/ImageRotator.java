package org.innoagencyhack.ocrparser.extractors.scans;

import java.util.ArrayList;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.innoagencyhack.ocrparser.extractors.scans.models.AnglesComparator;
import org.innoagencyhack.ocrparser.extractors.scans.models.ImageKernelContainer;

public class ImageRotator {

    private static final int MIN_LINE_LENGTH = 300;
    private static final int MAX_LINE_GAP = 50;

    public static Mat getAlignedImage(Mat image) {
        double angle = getRotateAngle(image);
        Size imageSize = image.size();
        Point imageCenter = new Point((int) imageSize.height / 2, (int) imageSize.width / 2);
        Mat rotMatrix = Imgproc.getRotationMatrix2D(imageCenter, angle, 1.0);
        Mat rotatedImage = new Mat();
        Imgproc.warpAffine(image, rotatedImage, rotMatrix, imageSize, Imgproc.INTER_LINEAR);
        return rotatedImage;
    }
    
    private static double getRotateAngle(Mat image) {
        Mat threshImg = ImageHelper.getReversedTresholdImage(image);
        ImageKernelContainer kernelContainer = ImageHelper.getKernels(threshImg);
        Mat contoursImage = ImageHelper.getContoursImage(image, kernelContainer);

        Mat edges = new Mat();
        Imgproc.Canny(contoursImage, edges, 50, 150, 3, false);
        Mat lines = new Mat();
        Imgproc.HoughLinesP(edges, lines, 1.0, 0.5 * Math.PI / 180, 300, MIN_LINE_LENGTH, MAX_LINE_GAP);

        double angle = calculateAngle(lines);
        return angle;
    }

    private static double calculateAngle(Mat lines) {
        ArrayList<Double> angles = new ArrayList<>();
        for (int index = 0; index < lines.cols(); index++) {
            double[] line = lines.get(0, index);
            double x1 = line[0];
            double x2 = line[2];
            double y1 = line[1];
            double y2 = line[3];

            double a = 1;
            double x = Math.abs(x1 - x2);
            double y = Math.abs(y1 - y2);

            if (x > 0) {
                a = Math.min(1.0 * y / x, a);
            }
            if (y > 0) {
                a = Math.min(1.0 * x / y, a);
            }

            if (a < 0.2) {
                angles.add(a);
            }
        }
        double mean = MathHelper.getMean(angles);
        double std = MathHelper.getStandartDeviation(angles);
        AnglesComparator comparator = new AnglesComparator(mean);
        while (std > 0.5 * Math.abs(mean)) {
            angles.sort(comparator);
            angles.remove(angles.get(0));
            mean = MathHelper.getMean(angles);
            std = MathHelper.getStandartDeviation(angles);
        }

        double arctan = Math.atan(mean);
        double angle = Math.toDegrees(arctan);
        return angle;
    }
}
