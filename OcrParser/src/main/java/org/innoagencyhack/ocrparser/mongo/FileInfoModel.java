package org.innoagencyhack.ocrparser.mongo;

import org.apache.commons.codec.digest.DigestUtils;
import org.bson.codecs.pojo.annotations.BsonId;

/**
 *
 * @author yurij
 */
public class FileInfoModel {

    private final String fileName;
    private final String text;

    public FileInfoModel(String fileName, String text) {
        this.fileName = fileName;
        this.text = text;
    }
    
    @BsonId
    public String getId() {
        return DigestUtils.md5Hex(fileName);
    }
    
    public String getFileName() {
        return fileName;
    }

    public String getText() {
        return text;
    }    
}
