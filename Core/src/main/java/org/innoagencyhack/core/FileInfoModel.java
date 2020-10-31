package org.innoagencyhack.core;

import org.apache.commons.codec.digest.DigestUtils;
import org.bson.codecs.pojo.annotations.BsonId;

/**
 *
 * @author yurij
 */
public class FileInfoModel {

    private String fileName;
    private String text;

    public FileInfoModel() {
    }
    
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

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
