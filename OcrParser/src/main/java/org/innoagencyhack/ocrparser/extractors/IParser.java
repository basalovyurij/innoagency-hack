package org.innoagencyhack.ocrparser.extractors;

import org.innoagencyhack.ocrparser.models.ParseResponse;

public interface IParser {
    ParseResponse parse(byte[] bytes) throws Exception;
}
