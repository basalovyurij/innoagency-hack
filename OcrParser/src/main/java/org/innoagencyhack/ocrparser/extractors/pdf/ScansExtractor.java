package org.innoagencyhack.ocrparser.extractors.pdf;

import java.awt.image.BufferedImage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.innoagencyhack.ocrparser.extractors.ImgParser;
import org.innoagencyhack.ocrparser.models.ParseResponse;

public class ScansExtractor implements IPageParser {
   
    private final boolean parseTable;

    public ScansExtractor(boolean parseTable) {
        this.parseTable = parseTable;
    }
    
    @Override
    public ParseResponse parse(PDPage page) {
        try (ImgParser imgParser = new ImgParser(parseTable)) {
            int dpi = (int) (250000 / Math.max(page.getMediaBox().getWidth(), page.getMediaBox().getHeight()));
            try (PDDocument document = new PDDocument()) {
                document.addPage(page);
                PDFRenderer renderer = new PDFRenderer(document);
                document.close();
                BufferedImage img = renderer.renderImageWithDPI(0, dpi, ImageType.GRAY);
                return imgParser.parse(img, dpi);
            }
        } catch (Exception ex) {
            return new ParseResponse(ex);
        }
    }
}
