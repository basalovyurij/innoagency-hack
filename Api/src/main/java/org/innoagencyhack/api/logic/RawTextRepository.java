package org.innoagencyhack.api.logic;

import org.innoagencyhack.core.FileInfoModel;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

/**
 *
 * @author yurij
 */
public class RawTextRepository implements AutoCloseable {

    private final MongoClient mongoClient;
    private final MongoCollection<FileInfoModel> fileInfos;
    
    public RawTextRepository() {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost");
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), 
                                                     pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                                                                .applyConnectionString(connectionString)
                                                                .codecRegistry(codecRegistry)
                                                                .build();
        mongoClient = MongoClients.create(clientSettings);
        MongoDatabase db = mongoClient.getDatabase("innoagencyhack");
        fileInfos = db.getCollection("files", FileInfoModel.class);
    }

    public FileInfoModel find(String id) {
        return fileInfos.find(new BasicDBObject("_id", id)).first();
    }

    @Override
    public void close() throws Exception {
        mongoClient.close();
    }    
}
