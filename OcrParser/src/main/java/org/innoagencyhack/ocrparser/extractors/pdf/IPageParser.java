package org.innoagencyhack.ocrparser.extractors.pdf;

import org.apache.pdfbox.pdmodel.PDPage;
import org.innoagencyhack.ocrparser.models.ParseResponse;

public interface IPageParser {
    ParseResponse parse(PDPage page);
}
