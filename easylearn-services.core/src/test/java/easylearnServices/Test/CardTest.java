package easylearnServices.Test;


import easylearnServices.Core.Models.Card;
import easylearnServices.Core.Models.CardCollection;
import easylearnServices.Core.Models.CardStack;
import easylearnServices.Core.Models.CardStackCollection;
import easylearnServices.Core.Test.TestBase;
import org.bson.types.ObjectId;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CardTest extends TestBase {

    @Test
    public void createCardTest() {

        CardStackCollection cardStackCollection = _serviceContext.fetchAllCardStacks();
        CardStack cardStack = cardStackCollection.getElements().get(0);

        Card card = _serviceContext.createCard("Card", "Definition", "Shortcut", "Term", cardStack.get_id());

        assertNotNull(card);
        assertNotNull(card.get_id());

        assertThat(card.get_name(), is("Card"));
        assertThat(card.get_definition(), is("Definition"));
        assertThat(card.get_shortcut(), is("Shortcut"));
        assertThat(card.get_term(), is("Term"));
        assertThat(card.get_cardStackRefId(), is(cardStack.get_id()));

        Card sameCard = _serviceContext.getCard(card.get_id());

        assertThat(card.get_id(), is(sameCard.get_id()));

        CardCollection cardCollection = _serviceContext.fetchCardsByCardstack(cardStack.get_id());
        assertThat(cardCollection.getElements().get(7).get_name(), is("Card"));

    }

    @Test
    public void updateCardTest() {

        CardStackCollection cardStackCollection = _serviceContext.fetchAllCardStacks();
        CardStack cardStack = cardStackCollection.getElements().get(0);

        CardCollection cardCollection = _serviceContext.fetchCardsByCardstack(cardStack.get_id());
        Card card = cardCollection.getElements().get(0);

        ObjectId id = card.get_id();
        card = _serviceContext.updateCard(id, "updatedName", "updatedDescription", "updatedShortcut", "updatedTerm");

        assertNotNull(card);

        assertThat(card.get_id(), is(id));
        assertThat(card.get_name(), is("updatedName"));
        assertThat(card.get_definition(), is("updatedDescription"));
        assertThat(card.get_shortcut(), is("updatedShortcut"));
        assertThat(card.get_term(), is("updatedTerm"));
    }

    @Test
    public void fetchAllCardsTest() {

        CardStackCollection cardStackCollection = _serviceContext.fetchAllCardStacks();
        CardStack cardStack = cardStackCollection.getElements().get(0);

        CardCollection cardCollection = _serviceContext.fetchCardsByCardstack(cardStack.get_id());

        assertThat(cardCollection.getElements().size(), is(7));

        assertThat(cardCollection.getElements().get(0).get_name(), is("Karte 1"));
        assertThat(cardCollection.getElements().get(1).get_name(), is("Karte 2"));
        assertThat(cardCollection.getElements().get(2).get_name(), is("Karte 3"));
        assertThat(cardCollection.getElements().get(3).get_name(), is("Karte 4"));
        assertThat(cardCollection.getElements().get(4).get_name(), is("Karte 5"));
        assertThat(cardCollection.getElements().get(5).get_name(), is("Karte 6"));
        assertThat(cardCollection.getElements().get(6).get_name(), is("Karte 7"));

    }

    @Test
    public void deleteCardTest() {

        CardStackCollection cardStackCollection = _serviceContext.fetchAllCardStacks();
        CardStack cardStack = cardStackCollection.getElements().get(0);

        CardCollection cardCollection = _serviceContext.fetchCardsByCardstack(cardStack.get_id());

        ObjectId id = cardCollection.getElements().get(0).get_id();
        _serviceContext.deleteCard(id);

        CardCollection sameCardCollection = _serviceContext.fetchCardsByCardstack(cardStack.get_id());
        assertThat(sameCardCollection.getElements().size(), is(6));

        Card deletedCard = _serviceContext.getCard(id);
        assertNull(deletedCard);
    }
}
