package easylearnServices.Rest.Endpoint;

import easylearnServices.Core.Common.Constants;
import easylearnServices.Core.Common.IEasylearnService;
import easylearnServices.Core.Models.Author;
import easylearnServices.Core.Models.AuthorCollection;
import easylearnServices.Core.Models.CardStack;
import easylearnServices.Core.Models.CardStackCollection;
import org.bson.types.ObjectId;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Created by patrickreichelt on 24/02/15.
 */
@Path("author")
public class AuthorEndpoint {

    @Context
    private IEasylearnService _serviceContext;

    /**
     * Mainly used for test cases to set their own implementation of the service.
     * @param context Instance of the database wrapper.
     */
    public void setServiceContext(IEasylearnService context) {
        this._serviceContext = context;
    }


    /**
     * Fetches all existing authors.
     *
     * @return 200 OK and a list of all existing authors
     * 404 NOT FOUND if no author exist.
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON + ";charset=utf-8"})
    public Response fetchAllAuthors()
    {
        AuthorCollection authors = _serviceContext.fetchAllAuthors();

        if (authors == null || authors.isEmpty())
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(authors).build();
    }

    /**
     * Creates a new {@link Author} with the given name.
     *
     * @param uriInfo Injected by JAX-RS. Used for building the correct path to
     * the newly created resource.
     * @param firstName First name of the author
     * @param lastName Last name of the author
     * @param userName User name of the author
     * @param email Email address of the author
     *
     * @return 201 CREATED and the author object that was created.
     * 400 BAD REQUEST if creation failed.
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})

    public Response createAuthor(
            @Context UriInfo uriInfo,
            @FormParam(Constants.AUTHOR_FIRST_NAME) String firstName,
            @FormParam(Constants.AUTHOR_LAST_NAME) String lastName,
            @FormParam(Constants.AUTHOR_USER_NAME) String userName,
            @FormParam(Constants.AUTHOR_E_MAIL) String email) {

        Author author = _serviceContext.createAuthor(firstName, lastName, userName, email);

        // creation failed, return 400
        if (author == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        // success, return 201 and the author object
        return Response.status(Response.Status.CREATED).entity(author).build();
    }

    /**
     * Fetches the author with the requested ID.
     *
     * @param authorId Object ID in database of the author.
     * @return 200 OK and the author object.
     * 404 NOT FOUND if there is no author with the specified ID.
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getAuthor(
            @PathParam("id") String authorId) {

        // retrieve the author from the database
        Author author = _serviceContext.getAuthor(new ObjectId(authorId));

        if (author == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.status(Response.Status.OK).entity(author).build();
    }

    /**
     * Updates the author with the requested ID.
     *
     * @param firstName First name of the author
     * @param lastName Last name of the author
     * @param userName User name of the author
     * @param email Email address of the author
     *
     * @return 200 OK and the updated author object.
     * 404 NOT FOUND if the requested author does not exist.
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateAuthor(
            @PathParam("id") String authorId,
            @FormParam(Constants.AUTHOR_FIRST_NAME) String firstName,
            @FormParam(Constants.AUTHOR_LAST_NAME) String lastName,
            @FormParam(Constants.AUTHOR_USER_NAME) String userName,
            @FormParam(Constants.AUTHOR_E_MAIL) String email) {

        if(authorId == null || authorId.isEmpty() || userName == null || userName.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).build();

        Author author = _serviceContext.updateAuthor(new ObjectId(authorId), firstName, lastName, userName, email);

        if(author == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.status(Response.Status.OK).entity(author).build();
    }

    /**
     * Delete the author with the requested ID.
     *
     * @param authorId The ID of the requested author.
     *
     * @return 200 OK.
     * 404 NOT FOUND if the requested author does not exist.
     */
    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response deleteAuthor(@PathParam("id") String authorId)
    {
        boolean success = _serviceContext.deleteAuthor(new ObjectId(authorId));

        if(!success)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.status(Response.Status.OK).build();
    }



}
