package easylearnServices.Core.Common;

import easylearnServices.Core.Models.*;
import org.bson.types.ObjectId;

import java.util.UUID;

/**
 * Created by patrickreichelt on 24/02/15.
 */
public interface IEasylearnService {

    // Cardstack
    public CardStack createCardStack(String name, String description, ObjectId authorRefId,String topic,boolean allowSharing);
    public CardStack getCardStack(ObjectId id);
    public CardStack updateCardStack(ObjectId id,String name, String description, ObjectId authorRefId,String topic,boolean allowSharing);
    public boolean deleteCardStack(ObjectId id);
    public CardStackCollection fetchAllCardStacks();

    // Cards
    public Card createCard(String name, String definition, String shortCut,String term, ObjectId cardstackRefId);
    public Card getCard(ObjectId id);
    public Card updateCard(ObjectId id,String name, String definition, String shortCut,String term);
    public boolean deleteCard(ObjectId id);
    public CardCollection fetchCardsByCardstack(ObjectId id);

    // Author
    public Author createAuthor(String firstName,String lastName, String userName, String email);
    public Author getAuthor(ObjectId id);
    public boolean deleteAuthor(ObjectId id);
    public AuthorCollection fetchAllAuthors();
    public Author updateAuthor(ObjectId id, String firstName, String lastName, String userName, String email);

    // Sample Data
    public void createSampleData();

}
