package easylearnServices.Test;

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
public class CardStackEndpointTest extends TestBase {

    private CardStackEndpoint _endpoint;
    private UriInfo _uriInfo;

    @Before
    public void prepareEndpoint() {
        _endpoint = new CardStackEndpoint();
        _endpoint.setServiceContext(_serviceContext);
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
    public void createCardStackSuccess() {

        Response response = _endpoint.createCardStack(_uriInfo, "Marvel Universe", "Alle Fragen um das Marvel Univerum und seine Helde", "", "Comic", true);
        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));

        CardStack cardstack = (CardStack) response.getEntity();

        response = _endpoint.getCardStack(cardstack.get_id().toString());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        assertThat(cardstack.get_id(), is(((CardStack) response.getEntity()).get_id()));
    }

    /**
     * test if CardStack creation fails, when a non-existent CardStack group is given
     */
    @Test
    public void createCardStackTestFails() {
        Response response = _endpoint.createCardStack(_uriInfo, null, "Alle Fragen um das Marvel Univerum und seine Helde", "", "Comic", true);
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    /**
     * test if not found is returned when CardStack with requested id does not exist
     */
    @Test
    public void fetchNotExistingCardStackTest() {
        Response response = _endpoint.getCardStack(new ObjectId().toString());
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    /**
     * test if updating a CardStack is successful
     */
    @Test
    public void updateCardStackTestSuccess() {
        Response response = _endpoint.createCardStack(_uriInfo, "Marvel Universe", "Alle Fragen um das Marvel Univerum und seine Helde", "", "Comic", true);
        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));

        CardStack CardStack = (CardStack) response.getEntity();

        response = _endpoint.updateCardStack(_uriInfo, CardStack.get_id().toString(), "DC Universe", "Alle Fragen um das DC Univerum und seine Helde", "", "Comic", true);
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }


    /**
     * test if updating a CardStack fails, when CardStack with requested id does not exist
     */
    @Test
    public void updateCardStackTestFails() {
        Response response = _endpoint.updateCardStack(_uriInfo, new ObjectId().toString(), "DC Universe", "Alle Fragen um das DC Univerum und seine Helde", "", "Comic", true);
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));

        response = _endpoint.updateCardStack(_uriInfo, null, "DC Universe", "Alle Fragen um das DC Univerum und seine Helde", "", "Comic", true);
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

        response = _endpoint.createCardStack(_uriInfo, "Marvel Universe", "Alle Fragen um das Marvel Univerum und seine Helde", "", "Comic", true);
        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));

        CardStack cardStack = (CardStack) response.getEntity();

        response = _endpoint.updateCardStack(_uriInfo, cardStack.get_id().toString(), "", "Alle Fragen um das DC Univerum und seine Helde", "", "Comic", true);
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

        response = _endpoint.updateCardStack(_uriInfo, cardStack.get_id().toString(), null, "Alle Fragen um das DC Univerum und seine Helde", "", "Comic", true);
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    /**
     * test if returning all CardStacks is successful
     */
    @Test
    public void fetchAllCardStacksTestSuccess() {
        Response response = _endpoint.fetchAllCardStacks();
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        CardStackCollection CardStackCollection = (CardStackCollection) response.getEntity();
        assertThat(CardStackCollection.getElements().size(), is(3));
    }

    /**
     * test if deleting a CardStack is successful
     */
    @Test
    public void deleteCardStackTestSuccess() {
        Response response = _endpoint.createCardStack(_uriInfo, "Marvel Universe", "Alle Fragen um das Marvel Univerum und seine Helde", "", "Comic", true);
        ;
        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));

        CardStack cardstack = (CardStack) response.getEntity();

        response = _endpoint.deleteCardStack(cardstack.get_id().toString());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }
}
