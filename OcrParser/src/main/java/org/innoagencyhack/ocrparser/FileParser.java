package org.innoagencyhack.ocrparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.innoagencyhack.ocrparser.extractors.IParser;
import org.innoagencyhack.ocrparser.extractors.ImgParser;
import org.innoagencyhack.ocrparser.extractors.PdfParser;
import org.innoagencyhack.ocrparser.models.ParseRequest;
import org.innoagencyhack.ocrparser.models.ParseResponse;
import org.innoagencyhack.ocrparser.mongo.FileInfoModel;
import org.innoagencyhack.ocrparser.mongo.MongoRepository;

/**
 *
 * @author yurij
 */
public class FileParser implements AutoCloseable {
    
    private static final Logger logger = LogManager.getLogger(FileParser.class);
    
    private final MongoRepository repo;

    public FileParser() {
        this.repo = new MongoRepository();
    }    
    
    public void run() throws IOException {
        Files.walk(Paths.get("../dataset"))
            .filter(Files::isRegularFile)
            .forEach(p -> process(p));
    }
    
    private void process(Path path) {
        try {
            ParseRequest request = new ParseRequest(
                    Files.readAllBytes(path), 
                    false, false);

            IParser parser = request.isImage() ? new ImgParser(request.isParseTable()) : new PdfParser(request.isParseTable());
            ParseResponse response = parser.parse(request.getFile());
            
            repo.upsert(new FileInfoModel(path.toString(), response.getText()));
            
            logger.info(String.format("Done [%s]", path));
        } catch (Exception e) {
            logger.error(String.format("Error [%s]", path), e);
        }
    }

    @Override
    public void close() throws Exception {
        repo.close();
    }
}
