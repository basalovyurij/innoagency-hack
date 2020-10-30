package org.innoagencyhack.ocrparser.extractors;

import org.innoagencyhack.ocrparser.extractors.pdf.ScansExtractor;
import org.innoagencyhack.ocrparser.models.TableResponse;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.innoagencyhack.ocrparser.models.ParseResponse;
import org.innoagencyhack.ocrparser.extractors.pdf.IPageParser;

public class PdfParser implements IParser {

    private final boolean parseTable;

    public PdfParser(boolean parseTable) {
        this.parseTable = parseTable;
    }
    
    @Override
    public ParseResponse parse(byte[] file) throws Exception {
        try (PDDocument pdfDocument = PDDocument.load(file)) {
            IPageParser parser = new ScansExtractor(parseTable);
            
            StringBuilder sb = new StringBuilder();
            List<TableResponse> tables = new ArrayList<>();
            for (PDPage page : pdfDocument.getPages()) {
                ParseResponse pageResp = parser.parse(page);
                if(pageResp.getError() == null) {
                    sb.append(pageResp.getText());
                    sb.append("\n\n");
                    tables.addAll(pageResp.getTables());
                }
            }

            return new ParseResponse(tables, sb.toString().trim());
        }
    }
}
