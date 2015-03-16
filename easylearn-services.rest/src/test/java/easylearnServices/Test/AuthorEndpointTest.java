package easylearnServices.Test;


import easylearnServices.Core.Models.Author;
import easylearnServices.Core.Models.AuthorCollection;
import easylearnServices.Core.Test.TestBase;
import easylearnServices.Rest.Endpoint.AuthorEndpoint;
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
public class AuthorEndpointTest extends TestBase {

    private AuthorEndpoint _endpoint;
    private UriInfo _uriInfo;

    @Before
    public void prepareEndpoint() {
        _endpoint = new AuthorEndpoint();
        _endpoint.setServiceContext(_serviceContext);
    }

    /**
     * Mock UriInfo, so tests can build fully qualified urls.
     * (normally done by the web server)
     */
    @Before
    public void mockUriInfo() {
        _uriInfo = mock(UriInfo.class);
        final UriBuilder fromResource = UriBuilder.fromResource(AuthorEndpoint.class);
        when(_uriInfo.getAbsolutePathBuilder()).thenAnswer(new Answer<UriBuilder>() {
            @Override
            public UriBuilder answer(InvocationOnMock invocation) throws Throwable {
                return fromResource;
            }
        });
    }

    @Test
    public void createAuthorSuccess() {

        Response response = _endpoint.createAuthor(_uriInfo, "Tony", "Stark", "mark1", "tony.stark@stark-industries.com");
        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));

        Author author = (Author) response.getEntity();

        response = _endpoint.getAuthor(author.get_id().toString());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        assertThat(author.get_id(), is(((Author) response.getEntity()).get_id()));
    }

    /**
     * test if author creation fails, when a non-existent author group is given
     */
    @Test
    public void createAuthorTestFails() {
        Response response = _endpoint.createAuthor(_uriInfo, null, null, null, null);
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    /**
     * test if not found is returned when author with requested id does not exist
     */
    @Test
    public void fetchNotExistingAuthorTest() {
        Response response = _endpoint.getAuthor(new ObjectId().toString());
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    /**
     * test if updating a author is successful
     */
    @Test
    public void updateAuthorTestSuccess() {
        Response response = _endpoint.createAuthor(_uriInfo, "Steve", "Rogers", "captainamerica", "captain.america@shield.com");
        Author author = (Author) response.getEntity();

        response = _endpoint.updateAuthor(author.get_id().toString(), "Stevie", "Rogers", "cap", "captain.america@avengers.com");
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }


    /**
     * test if updating a author fails, when author with requested id does not exist
     */
    @Test
    public void updateAuthorTestFails() {
        Response response = _endpoint.updateAuthor(new ObjectId().toString(), "Steve", "Rogers", "captainamerica", "captain.america@shield.com");
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));

        response = _endpoint.updateAuthor(null, "Steve", "Rogers", "captainamerica", "captain.america@shield.com");
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

        response = _endpoint.createAuthor(_uriInfo, "Steve", "Rogers", "captainamerica", "captain.america@shield.com");
        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));

        Author author = (Author) response.getEntity();

        response = _endpoint.updateAuthor(author.get_id().toString(), "Steve", "Rogers", null, "captain.america@shield.com");
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    /**
     * test if returning all authors is successful
     */
    @Test
    public void fetchAllAuthorsTestSuccess() {
        Response response = _endpoint.fetchAllAuthors();
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        AuthorCollection authorCollection = (AuthorCollection) response.getEntity();
        assertThat(authorCollection.getElements().size(), is(2));
    }

    /**
     * test if deleting a author is successful
     */
    @Test
    public void deleteAuthorTestSuccess() {
        Response response = _endpoint.createAuthor(_uriInfo, "Bruce", "Wayne", "batman", "bruce.wayne@wayne-enterprises.com");
        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));

        Author author = (Author) response.getEntity();

        response = _endpoint.deleteAuthor(author.get_id().toString());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }
}
