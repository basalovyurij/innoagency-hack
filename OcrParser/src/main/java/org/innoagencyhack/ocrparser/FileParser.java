package org.innoagencyhack.ocrparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.innoagencyhack.ocrparser.extractors.IParser;
import org.innoagencyhack.ocrparser.extractors.ImgParser;
import org.innoagencyhack.ocrparser.extractors.PdfParser;
import org.innoagencyhack.ocrparser.models.ParseRequest;
import org.innoagencyhack.ocrparser.models.ParseResponse;
import org.innoagencyhack.ocrparser.mongo.FileInfoModel;
import org.innoagencyhack.ocrparser.mongo.RawTextRepository;

/**
 *
 * @author yurij
 */
public class FileParser implements AutoCloseable {
    
    private static final Logger logger = LogManager.getLogger(FileParser.class);
    
    private final RawTextRepository repo;

    public FileParser() {
        this.repo = new RawTextRepository();
    }    
    
    public void run() throws IOException {
        Files.walk(Paths.get("../dataset"))
            .filter(Files::isRegularFile)
            .parallel()
            .forEach(p -> process(p));
    }   
    
    public void reParseUndefined() throws IOException {
        Files.walk(Paths.get("../texts/undefined"))
            .filter(Files::isRegularFile)
            .parallel()
            .forEach(p -> {
                FileInfoModel info = repo.find(p.getFileName().toString().split("\\.")[0]);
                process(Paths.get(info.getFileName()));
            });
    }
    
    private void process(List<Path> paths) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for(Path path : paths) {
            executor.submit(() -> process(path));
        }
        executor.awaitTermination(1, TimeUnit.DAYS);
    }
    
    private void process(Path path) {
        try {
            ParseRequest request = new ParseRequest(
                    Files.readAllBytes(path), 
                    false, false);

            IParser parser = request.isImage() ? new ImgParser(request.isParseTable()) : new PdfParser(request.isParseTable());
            ParseResponse response = parser.parse(request.getFile());
            System.out.println(response.getText());
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
