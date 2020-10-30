package org.innoagencyhack.ocrparser.extractors.scans.tesseract;

/**
 *
 * @author Yuriy Basalov
 */
public class OritentationAndTextInfo {

    private final int angle;
    private final String text;

    public OritentationAndTextInfo(int angle, String text) {
        this.angle = angle;
        this.text = text;
    }

    public int getAngle() {
        return angle;
    }

    public String getText() {
        return text;
    }
}
