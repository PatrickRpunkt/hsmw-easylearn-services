package easylearnServices.Core.Common;

import easylearnServices.Core.Models.*;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Arrays;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by patrickreichelt on 24/02/15.
 */
public class EasylearnService implements IEasylearnService {


    private MongoDatabase context;

    public EasylearnService() {
        try {
            // create auth credentials for Mongo DB
            MongoCredential credential = MongoCredential.createCredential(Constants.MONGO_USER, Constants.MONGO_DATABASE_NAME, Constants.MONGO_PASSWORD.toCharArray());
            // create client object
            MongoClient client = new MongoClient(new ServerAddress(Constants.MONGO_SERVER, Constants.MONGO_PORT), Arrays.asList(credential));
            // get the db context for the easylearn Database
            context = client.getDatabase(Constants.MONGO_DATABASE_NAME);

        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

    //region CardStack Methods
    @Override
    public CardStack createCardStack(String name, String description, ObjectId authorRefId, String topic, boolean allowSharing) {
        MongoCollection table = context.getCollection(Constants.MONGO_COLLECTION_CARDSTACK);
        /**** Insert ****/
        try {
            //prepare the object for insert
            CardStack object = new CardStack();
            object.set_name(name);
            object.set_description(description);
            object.set_authorRefId(authorRefId);
            object.set_allowSharing(allowSharing);
            object.set_topic(topic);
            //perform insert
            table.insertOne(object.GenerateMongoDocument());
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CardStack getCardStack(ObjectId id) {

        MongoCollection table = context.getCollection(Constants.MONGO_COLLECTION_CARDSTACK);
        CardStack cardStack = new CardStack((Document) table.find(eq(Constants.MONGO_OBJECTID_IDENTIFIER, id)).first());
        Author author = getAuthor(cardStack.get_authorRefId());
        if (author != null)
            cardStack.set_author(author);
        CardCollection cards = fetchCardsByCardstack(cardStack.get_id());
        if (cards != null)
            cardStack.set_cards(cards);
        return cardStack.get_id() == null ? null : cardStack;
    }

    @Override
    public CardStack updateCardStack(ObjectId id, String name, String description, ObjectId authorRefId, String topic, boolean allowSharing) {
        MongoCollection table = context.getCollection(Constants.MONGO_COLLECTION_CARDSTACK);
        CardStack cardstack = new CardStack((Document) table.find(eq(Constants.MONGO_OBJECTID_IDENTIFIER, id)).first());
        if (cardstack.get_id() == null)
            return null;
        table.deleteOne(cardstack.GenerateMongoDocument());
        // prepare update object
        CardStack updated_cardstack = new CardStack(name);
        updated_cardstack.set_description(description);
        updated_cardstack.set_authorRefId(authorRefId);
        updated_cardstack.set_topic(topic);
        updated_cardstack.set_allowSharing(allowSharing);
        updated_cardstack.set_id(id);
        // perform insert
        table.insertOne(updated_cardstack.GenerateMongoDocument());

        return updated_cardstack;
    }

    @Override
    public boolean deleteCardStack(ObjectId id) {
        MongoCollection cardstackTable = context.getCollection(Constants.MONGO_COLLECTION_CARDSTACK);
        MongoCollection cardTable = context.getCollection(Constants.MONGO_COLLECTION_CARD);
        // fetch the cardstack for delete
        CardStack cardStack = new CardStack((Document) cardstackTable.find(eq(Constants.MONGO_OBJECTID_IDENTIFIER, id)).first());
        // fetch all cards from cardstack and delete them
        CardCollection collection = fetchCardsByCardstack(cardStack.get_id());
        for (Card c : collection.getElements()) {
            cardTable.deleteOne(c.GenerateMongoDocument());
        }
        // after all delete the cardstack
        cardstackTable.deleteOne(cardStack.GenerateMongoDocument());
        return true;
    }

    @Override
    public CardStackCollection fetchAllCardStacks() {
        MongoCollection table = context.getCollection(Constants.MONGO_COLLECTION_CARDSTACK);
        MongoCursor cursor = table.find().iterator();
        CardStackCollection list = new CardStackCollection();
        try {
            while (cursor.hasNext()) {
                // create the cardstack object
                CardStack cs = new CardStack((Document) cursor.next());
                // fetch the author of the cardstack
                Author author = getAuthor(cs.get_authorRefId());
                if (author != null)
                    // and set them
                    cs.set_author(author);
                // fetch the cards of the cardstack
                CardCollection cards = fetchCardsByCardstack(cs.get_id());
                if (cards != null)
                    // and set them on cardstack
                    cs.set_cards(cards);
                // add the final object to list
                list.add(cs);
            }
        } finally {
            cursor.close();
        }
        return list;
    }
    //endregion

    //region Card Methods
    @Override
    public Card createCard(String name, String definition, String shortCut, String term, ObjectId cardstackRefId) {
        MongoCollection table = context.getCollection(Constants.MONGO_COLLECTION_CARD);
        MongoCollection tableCardStack = context.getCollection(Constants.MONGO_COLLECTION_CARDSTACK);
        // fetch cardstack of the card that will be create
        CardStack cardStack = new CardStack((Document) tableCardStack.find(eq(Constants.MONGO_OBJECTID_IDENTIFIER, cardstackRefId)).first());
        // check whether a cardstack was found
        if(cardStack.get_id() == null || cardStack.get_idString().isEmpty())
            //otherwise return null
            return null;
        //prepare card object
        Card c = new Card();
        c.set_name(name);
        c.set_definition(definition);
        c.set_shortcut(shortCut);
        c.set_term(term);
        c.set_cardStackRefId(cardstackRefId);
        //insert card
        table.insertOne(c.GenerateMongoDocument());
        //and return them
        return c;
    }

    @Override
    public Card getCard(ObjectId id) {
        MongoCollection table = context.getCollection(Constants.MONGO_COLLECTION_CARD);
        // fetch card by id
        Card card = new Card((Document) table.find(eq(Constants.MONGO_OBJECTID_IDENTIFIER, id)).first());
        // check card object is null then return null otherwise return the card object
        return card.get_id() == null ? null : card;
    }

    @Override
    public Card updateCard(ObjectId id, String name, String definition, String shortCut, String term) {
        MongoCollection table = context.getCollection(Constants.MONGO_COLLECTION_CARD);
        Card card = new Card((Document) table.find(eq(Constants.MONGO_OBJECTID_IDENTIFIER, id)).first());
        // check whether a card was found
        if (card.get_id() == null)
            return null;
        // delete this card
        table.deleteOne(card.GenerateMongoDocument());
        // prepare the updated card object
        Card updated_card = new Card();
        updated_card.set_name(name);
        updated_card.set_definition(definition);
        updated_card.set_shortcut(shortCut);
        updated_card.set_term(term);

        updated_card.set_id(id);
        // insert the updated card with the same id from the old card
        table.insertOne(updated_card.GenerateMongoDocument());
        // and return the updated object
        return updated_card;
    }

    @Override
    public boolean deleteCard(ObjectId id) {
        try {
            MongoCollection table = context.getCollection(Constants.MONGO_COLLECTION_CARD);
            // fetch the card that will be deleted
            Document card = (Document) table.find(eq(Constants.MONGO_OBJECTID_IDENTIFIER, id)).first();
            // if no card found return false because of no card will be deleted
            if(card.isEmpty())
                return false;
            //delete card
            table.deleteOne(card);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public CardCollection fetchCardsByCardstack(ObjectId id) {
        MongoCollection table = context.getCollection(Constants.MONGO_COLLECTION_CARD);
        MongoCursor cursor = table.find(eq(Constants.CARD_CARDSTACK_REF_ID, id)).iterator();
        CardCollection list = new CardCollection();
        // iterate over all cards in fetched collection
        try {
            while (cursor.hasNext()) {
                Card c = new Card((Document) cursor.next());
                list.add(c);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {

            cursor.close();
        }
        //return the list
        return list;

    }
    //endregion

    //region Author Methods
    @Override
    public Author createAuthor(String firstName, String lastName, String userName, String email) {
        MongoCollection table = context.getCollection(Constants.MONGO_COLLECTION_AUTHOR);
        try {
            //setup the Object
            Author object = new Author();
            object.set_firstName(firstName);
            object.set_lastName(lastName);
            if (userName == null || userName.isEmpty())
                return null;
            object.set_userName(userName);
            object.set_eMail(email);
            object.set_password("test");
            // insert Object to Mongo Database
            table.insertOne(object.GenerateMongoDocument());
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Author getAuthor(ObjectId id) {
        //get context for author collection
        MongoCollection authorTable = context.getCollection(Constants.MONGO_COLLECTION_AUTHOR);
        // get author record for the given id
        Author author = new Author((Document) authorTable.find(eq(Constants.MONGO_OBJECTID_IDENTIFIER, id)).first());
        // if the returned Object null then return null otherwise return the author
        return author.get_id() == null ? null : author;
    }

    @Override
    public boolean deleteAuthor(ObjectId id) {
        //get context for author collection
        MongoCollection authorTable = context.getCollection(Constants.MONGO_COLLECTION_AUTHOR);
        //find the record for the given id
        Author author = new Author((Document) authorTable.find(eq(Constants.MONGO_OBJECTID_IDENTIFIER, id)).first());
        // delete the record
        authorTable.deleteOne(author.GenerateMongoDocument());
        return true;
    }

    @Override
    public AuthorCollection fetchAllAuthors() {
        //get context for author collection
        MongoCollection table = context.getCollection(Constants.MONGO_COLLECTION_AUTHOR);
        // get iterator for the returned collection
        MongoCursor cursor = table.find().iterator();
        AuthorCollection list = new AuthorCollection();
        try {
            while (cursor.hasNext()) {
                // create author object from the returned document and added to list
                Author author = new Author((Document) cursor.next());
                list.add(author);
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    @Override
    public Author updateAuthor(ObjectId id, String firstName, String lastName, String userName, String email) {
        //get context for author collection
        MongoCollection table = context.getCollection(Constants.MONGO_COLLECTION_AUTHOR);
        //fetch author for given id
        Author author = new Author((Document) table.find(eq(Constants.MONGO_OBJECTID_IDENTIFIER, id)).first());

        // check found object
        if (author.get_id() == null)
            return null;
        // delete author object
        table.deleteOne(author.GenerateMongoDocument());

        // create updated record with same id
        Author updated_author = new Author(firstName, lastName, userName, email);
        updated_author.set_id(id);
        table.insertOne(updated_author.GenerateMongoDocument());

        return updated_author;
    }
    //endregion

    //region Sample Data
    @Override
    public void createSampleData() {
        MongoCollection cardstackTable = context.getCollection(Constants.MONGO_COLLECTION_CARDSTACK);
        MongoCollection authorTable = context.getCollection(Constants.MONGO_COLLECTION_AUTHOR);
        MongoCollection cardTable = context.getCollection(Constants.MONGO_COLLECTION_CARD);
        try {
            cardstackTable.dropCollection();
            authorTable.dropCollection();
            cardTable.dropCollection();
            Author author = createAuthor("Frank", "Jüstel", "franky", "franky@hs-mittweida.de");

            CardStack object = new CardStack();
            System.out.println("Cardstack ObjectID: " + object.get_id());
            object.set_name("Programmieren mit Swift – Crashkurs");
            object.set_description("Mit Version 8 von iOS – dem Betriebssystem für seine mobile Geräte – stellt Apple eine gänzlich neue Programmiersprache vor, die ihre Vorgängerin Objective-C ablösen soll. Dieser Crashkurs führt Sie in die wichtigsten Sprachmerkmale ein, stellt moderne Eigenschaften wie Generics, Extensions, Tupels und Optionals vor und macht Sie fit für den ersten praktischen Einsatz.\n");
            object.set_authorRefId(author.get_id());
            object.set_topic("Mobile Development");
            System.out.println("Cardstack ObjectID: " + object.get_id());
            cardstackTable.insertOne(object.GenerateMongoDocument());
            System.out.println("Cardstack ObjectID: " + object.get_id());
            createCard("Karte 1", "definition text", "strg+a", "das ist ein begriff", object.get_id());
            createCard("Karte 2", "definition text", "strg+s", "das ist ein begriff", object.get_id());
            createCard("Karte 3", "definition text", "strg+d", "das ist ein begriff", object.get_id());
            createCard("Karte 4", "definition text", "strg+f", "das ist ein begriff", object.get_id());
            createCard("Karte 5", "definition text", "strg+h", "das ist ein begriff", object.get_id());
            createCard("Karte 6", "definition text", "strg+j", "das ist ein begriff", object.get_id());
            createCard("Karte 7", "definition text", "strg+c", "das ist ein begriff", object.get_id());

            CardStack object2 = new CardStack();
            object2.set_name("Core Data in iOS 8");
            object2.set_description("Mit Version 8 von iOS – dem Betriebssystem für seine mobile Geräte – stellt Apple eine gänzlich neue Programmiersprache vor, die ihre Vorgängerin Objective-C ablösen soll. Dieser Crashkurs führt Sie in die wichtigsten Sprachmerkmale ein, stellt moderne Eigenschaften wie Generics, Extensions, Tupels und Optionals vor und macht Sie fit für den ersten praktischen Einsatz.\n");
            object2.set_authorRefId(author.get_id());
            object2.set_topic("Mobile Development");
            cardstackTable.insertOne(object2.GenerateMongoDocument());
            createCard("Karte a", "definition text", "strg+a", "das ist ein begriff", object2.get_id());

            Author author2 = createAuthor("Patrick", "Reichelt", "StarLord", "StarLord@hs-mittweida.de");
            CardStack object3 = new CardStack();
            object3.set_name("MongoDB für Dummies");
            object3.set_description("Mit Version 8 von iOS – dem Betriebssystem für seine mobile Geräte – stellt Apple eine gänzlich neue Programmiersprache vor, die ihre Vorgängerin Objective-C ablösen soll. Dieser Crashkurs führt Sie in die wichtigsten Sprachmerkmale ein, stellt moderne Eigenschaften wie Generics, Extensions, Tupels und Optionals vor und macht Sie fit für den ersten praktischen Einsatz.\n");
            object3.set_authorRefId(author2.get_id());
            object3.set_topic("Datebanken");
            cardstackTable.insertOne(object3.GenerateMongoDocument());
            createCard("Karte 11", "definition text", "strg+a", "das ist ein begriff", object3.get_id());
            createCard("Karte 22", "definition text", "strg+s", "das ist ein begriff", object3.get_id());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion
}
