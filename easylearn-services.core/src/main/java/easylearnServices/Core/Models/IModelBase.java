package easylearnServices.Core.Models;

import org.bson.Document;

/**
 * Created by patrickreichelt on 26/02/15.
 */
public interface IModelBase {

    void MapDataMongoDocument(Document doc);

    Document GenerateMongoDocument();
}
