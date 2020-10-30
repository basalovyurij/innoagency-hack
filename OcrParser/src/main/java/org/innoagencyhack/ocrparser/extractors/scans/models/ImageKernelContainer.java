package org.innoagencyhack.ocrparser.extractors.scans.models;

import org.opencv.core.Mat;

public class ImageKernelContainer {

    public Mat horizontalKernel;
    public Mat verticalKernel;
    public Mat mainKernel;

    public ImageKernelContainer(Mat _verticalKernel, Mat _horizontalKernel, Mat _mainKernel) {
        verticalKernel = _verticalKernel;
        horizontalKernel = _horizontalKernel;
        mainKernel = _mainKernel;
    }
}
