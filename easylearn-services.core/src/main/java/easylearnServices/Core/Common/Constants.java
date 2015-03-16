package easylearnServices.Core.Common;

/**
 * Created by patrickreichelt on 14/03/15.
 */
public class Constants {

    // Database Connection Configuration
    public static final String MONGO_USER = "el_user";
    public static final String MONGO_PASSWORD = "password";

    public static final String MONGO_SERVER = "ds063789.mongolab.com";
    public static final int MONGO_PORT = 63789;
    public static final String MONGO_DATABASE_NAME = "easylearn";

    public static final String MONGO_TEST_SERVER = "ds039341.mongolab.com";
    public static final int MONGO_TEST_PORT = 39341;
    public static final String MONGO_DATABASE_TEST_NAME = "easylearn_test";


    // Database Collection
    public static final String MONGO_COLLECTION_AUTHOR = "author";
    public static final String MONGO_COLLECTION_CARDSTACK = "cardstack";
    public static final String MONGO_COLLECTION_CARD = "card";

    // Field Identifier - Common
    public static final String MONGO_OBJECTID_IDENTIFIER = "_id";

    // Field Identifier - Author
    public static final String AUTHOR_FIRST_NAME = "firstName";
    public static final String AUTHOR_LAST_NAME = "lastName";
    public static final String AUTHOR_USER_NAME = "userName";
    public static final String AUTHOR_E_MAIL = "eMail";
    public static final String AUTHOR_PASSWORD = "password";

    // Field Identifier - Card
    public static final String CARD_NAME = "name";
    public static final String CARD_DEFINITION = "definition";
    public static final String CARD_TERM = "term";
    public static final String CARD_SHORTCUT = "shortcut";
    public static final String CARD_CARDSTACK_REF_ID = "cardstack_refid";

    // Field Identifier - Cardstack
    public static final String CARDSTACK_AUTHOR_REF_ID = "author_refid";
    public static final String CARDSTACK_AUTHOR = "author";
    public static final String CARDSTACK_NAME = "name";
    public static final String CARDSTACK_ALLOWSHARING = "allowsharing";
    public static final String CARDSTACK_DESCRIPTION = "description";
    public static final String CARDSTACK_CARDS = "cards";
    public static final String CARDSTACK_MODIFIEDDATE = "modifieddate";
    public static final String CARDSTACK_CARDSTACK = "cardstack";
    public static final String CARDSTACK_TOPIC = "topic";

    public static final String MODEL_DESCRIPTION_CARD = "card";
    public static final String MODEL_DESCRIPTION_CARDSTACK = "cardstack";
    public static final String MODEL_DESCRIPTION_AUTHOR = "author";

}
