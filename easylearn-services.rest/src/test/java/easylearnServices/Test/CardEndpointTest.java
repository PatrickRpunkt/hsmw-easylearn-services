package easylearnServices.Test;


import easylearnServices.Core.Models.Card;
import easylearnServices.Core.Models.CardCollection;
import easylearnServices.Core.Models.CardStack;
import easylearnServices.Core.Models.CardStackCollection;
import easylearnServices.Core.Test.TestBase;
import easylearnServices.Rest.Endpoint.CardStackEndpoint;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by patrickreichelt on 24/02/15.
 */
public class CardEndpointTest extends TestBase {

    private CardStackEndpoint _endpoint;
    private UriInfo _uriInfo;

    @Before
    public void prepareEndpoint() {
        _endpoint = new CardStackEndpoint();
        _endpoint.setServiceContext(_serviceContext);
    }

    public Card getFirstCardFromSampleData() {

        CardStack cardStack = getFirstCardStackFromSampleData();

        Response response = _endpoint.fetchAllCardsByCardStack(_uriInfo, cardStack.get_idString());
        CardCollection cardCollection = (CardCollection) response.getEntity();
        Card card = cardCollection.getElements().get(0);
        return card;
    }

    public CardStack getFirstCardStackFromSampleData() {
        Response response = _endpoint.fetchAllCardStacks();
        CardStackCollection cardStackCollection = (CardStackCollection) response.getEntity();
        CardStack cardStack = cardStackCollection.getElements().get(0);
        return cardStack;
    }

    /**
     * Mock UriInfo, so tests can build fully qualified urls.
     * (normally done by the web server)
     */
    @Before
    public void mockUriInfo() {
        _uriInfo = mock(UriInfo.class);
        final UriBuilder fromResource = UriBuilder.fromResource(CardStackEndpoint.class);
        when(_uriInfo.getAbsolutePathBuilder()).thenAnswer(new Answer<UriBuilder>() {
            @Override
            public UriBuilder answer(InvocationOnMock invocation) throws Throwable {
                return fromResource;
            }
        });
    }

    @Test
    public void createCardSuccess() {

        CardStack cardStack = getFirstCardStackFromSampleData();

        Response response = _endpoint.createCard(_uriInfo, "Wer ist Superman", "Clark Kent", "C.K", "", cardStack.get_idString());

        Card card = (Card) response.getEntity();

        response = _endpoint.getCard(card.get_id().toString());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        assertThat(card.get_id(), is(((Card) response.getEntity()).get_id()));
    }

    /**
     * test if Card creation fails, when a non-existent Card group is given
     */
    @Test
    public void createCardTestFails() {
        Response response = _endpoint.createCard(_uriInfo, "Wer ist Superman", "Clark Kent", "C.K", "", new ObjectId().toString());
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));

        response = _endpoint.createCard(_uriInfo, "Wer ist Superman", "Clark Kent", "C.K", "", null);
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    /**
     * test if not found is returned when Card with requested id does not exist
     */
    @Test
    public void fetchNotExistingCardTest() {
        Response response = _endpoint.getCard(new ObjectId().toString());
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    /**
     * test if updating a Card is successful
     */
    @Test
    public void updateCardTestSuccess() {
        Card card = getFirstCardFromSampleData();

        Response response = _endpoint.updateCard(_uriInfo, card.get_idString(), card.get_id().toString(), "Wer ist Batman", "Bruce Wayne", "B.W.", "test");
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }


    /**
     * test if updating a Card fails, when Card with requested id does not exist
     */
    @Test
    public void updateCardTestFails() {
        Response response = _endpoint.updateCard(_uriInfo, new ObjectId().toString(), new ObjectId().toString(), "Wer ist Batman", "Bruce Wayne", "B.W.", "test");
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));

        response = _endpoint.updateCard(_uriInfo, new ObjectId().toString(), null, "Wer ist Batman", "Bruce Wayne", "B.W.", "test");
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

        Card card = getFirstCardFromSampleData();

        response = _endpoint.updateCard(_uriInfo, new ObjectId().toString(), card.get_id().toString(), "", "Bruce Wayne", "B.W.", "test");
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

        response = _endpoint.updateCard(_uriInfo, new ObjectId().toString(), card.get_id().toString(), null, "Bruce Wayne", "B.W.", "test");
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    /**
     * test if returning all Cards is successful
     */
    @Test
    public void fetchAllCardsTestSuccess() {
        CardStack cardStack = getFirstCardStackFromSampleData();

        Response response = _endpoint.fetchAllCardsByCardStack(_uriInfo, cardStack.get_idString());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        CardCollection CardCollection = (CardCollection) response.getEntity();
        assertThat(CardCollection.getElements().size(), is(7));
    }

    /**
     * test if deleting a Card is successful
     */
    @Test
    public void deleteCardTestSuccess() {
        Card card = getFirstCardFromSampleData();

        Response response = _endpoint.deleteCard(card.get_id().toString());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }
}
