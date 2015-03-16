package easylearnServices.Core.Test;

import easylearnServices.Core.Common.Constants;
import easylearnServices.Core.Common.EasylearnService;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import java.util.Arrays;

/**
 * Created by patrickreichelt on 24/02/15.
 */
public class EasylearnServiceMock extends EasylearnService{

    private MongoDatabase context;

    public EasylearnServiceMock() {

        try {
            MongoCredential credential = MongoCredential.createCredential(Constants.MONGO_USER, Constants.MONGO_DATABASE_TEST_NAME, Constants.MONGO_PASSWORD.toCharArray());
            MongoClient client = new MongoClient(new ServerAddress(Constants.MONGO_TEST_SERVER, Constants.MONGO_TEST_PORT), Arrays.asList(credential));
            context = client.getDatabase(Constants.MONGO_DATABASE_TEST_NAME);
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }
}
