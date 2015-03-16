package easylearnServices.Test;


import easylearnServices.Core.Models.CardStack;
import easylearnServices.Core.Models.CardStackCollection;
import easylearnServices.Core.Test.TestBase;
import org.bson.types.ObjectId;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CardStackTest extends TestBase {

    @Test
    public void createCardStackTest() {

        ObjectId id = new ObjectId();
        CardStack cardStack = _serviceContext.createCardStack("DC Universe","Quiz alles ums Theme DC Comics",id,"Comics",true);

        assertNotNull(cardStack);
        assertNotNull(cardStack.get_id());

        assertThat(cardStack.get_name(), is("DC Universe"));
        assertThat(cardStack.get_description(), is("Quiz alles ums Theme DC Comics"));
        assertThat(cardStack.get_topic(), is("Comics"));
        assertThat(cardStack.is_allowSharing(), is(true));
        assertThat(cardStack.get_authorRefId(), is(id));

        CardStack sameCardStack = _serviceContext.getCardStack(cardStack.get_id());

        assertThat(cardStack.get_id(), is(sameCardStack.get_id()));

        CardStackCollection cardCollection = _serviceContext.fetchAllCardStacks();
        assertThat(cardCollection.getElements().get(3).get_name(), is("DC Universe"));

    }

    @Test
    public void updateCardStackTest() {

        CardStackCollection cardStackCollection = _serviceContext.fetchAllCardStacks();
        CardStack cardStack = cardStackCollection.getElements().get(0);

        ObjectId id = cardStack.get_id();
        cardStack = _serviceContext.updateCardStack(id, "MARVEL Universe","Quiz alles ums Theme MARVEL Comics",id,"Comics",true);

        assertNotNull(cardStack);

        assertThat(cardStack.get_name(), is("MARVEL Universe"));
        assertThat(cardStack.get_description(), is("Quiz alles ums Theme MARVEL Comics"));
        assertThat(cardStack.get_topic(), is("Comics"));
        assertThat(cardStack.is_allowSharing(), is(true));
        assertThat(cardStack.get_authorRefId(), is(id));
    }

    @Test
    public void fetchAllCardStackStacksTest() {

        CardStackCollection cardStackCollection = _serviceContext.fetchAllCardStacks();
        assertThat(cardStackCollection.getElements().size(), is(3));
    }

    @Test
    public void deleteCardStackTest() {

        CardStackCollection cardStackCollection = _serviceContext.fetchAllCardStacks();
        CardStack cardStack = cardStackCollection.getElements().get(0);

        ObjectId id = cardStack.get_id();
        _serviceContext.deleteCardStack(id);

        CardStackCollection samecardStackCollection = _serviceContext.fetchAllCardStacks();
        assertThat(samecardStackCollection.getElements().size(), is(2));

        CardStack deletedCardStack = _serviceContext.getCardStack(id);
        assertNull(deletedCardStack);
    }
}
