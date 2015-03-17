package easylearnServices.Rest.Endpoint;

import easylearnServices.Core.Common.Constants;
import easylearnServices.Core.Common.IEasylearnService;
import easylearnServices.Core.Models.Card;
import easylearnServices.Core.Models.CardCollection;
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
@Path("cardstack")
public class CardStackEndpoint {

    @Context
    private IEasylearnService _serviceContext;

    /**
     * Mainly used for test cases to set their own implementation of the service.
     *
     * @param context Instance of the database wrapper.
     */
    public void setServiceContext(IEasylearnService context) {
        this._serviceContext = context;
    }

    //region Cardstack
    /**
     * Fetches all existing cardstacks.
     *
     * @return 404 NOT FOUND if no cardstacks exist.
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON + ";charset=utf-8"})
    public Response fetchAllCardStacks() {
        CardStackCollection cardstacks = _serviceContext.fetchAllCardStacks();

        if (cardstacks == null || cardstacks.isEmpty())
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(cardstacks).build();
    }

    /**
     * Fetch an existing cardstack.
     *
     * @return 404 NOT FOUND if no cardstacks exist.
     */
    @GET
    @Path("{"+Constants.MONGO_OBJECTID_IDENTIFIER+"}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON + ";charset=utf-8"})
    public Response getCardStack(@PathParam(Constants.MONGO_OBJECTID_IDENTIFIER) String id) {
        CardStack cardstack = _serviceContext.getCardStack(new ObjectId(id));

        if (cardstack == null)
            return Response.status(Response.Status.NOT_FOUND).build();


        return Response.ok(cardstack).build();
    }

    /**
     * Creates a new {@link easylearnServices.Core.Models.CardStack} with the given name.
     *
     * @param uriInfo Injected by JAX-RS. Used for building the correct path to
     *                the newly created resource.
     * @param name              The name of the cardstack.
     * @param description       The description of the cardstack.
     * @param authorRefId      The id that references of the author.
     * @param topic             The topic of the cardstack.
     * @param allowSharing      Indicates whether it can be shared with others.
     * @return 400 BAD REQUEST if creation failed.
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createCardStack(
            @Context UriInfo uriInfo,
            @FormParam(Constants.CARDSTACK_NAME) String name,
            @FormParam(Constants.CARDSTACK_DESCRIPTION) String description,
            @FormParam(Constants.CARDSTACK_AUTHOR_REF_ID) String authorRefId,
            @FormParam(Constants.CARDSTACK_TOPIC) String topic,
            @FormParam(Constants.CARDSTACK_ALLOWSHARING) boolean allowSharing ){

        ObjectId  authorRefObjectId= (authorRefId == null || authorRefId.isEmpty()) ? null : new ObjectId(authorRefId);

        if (name == null || name.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).build();

        CardStack cardStack = _serviceContext.createCardStack(name, description, authorRefObjectId, topic, allowSharing);
        // creation failed, return 400
        if (cardStack == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        // success, return 201 and the cardstack object
        return Response.status(Response.Status.CREATED).entity(cardStack).build();
    }

    /**
     * Delete an existing cardstack with the requested ID.
     *
     * @param cardstackObjectId The ID of the requested cardstack.
     * @return 404 NOT FOUND if the requested cardstack does not exist.
     */
    @DELETE
    @Path("{"+Constants.CARD_CARDSTACK_REF_ID+"}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response deleteCardStack(
            @PathParam(Constants.CARD_CARDSTACK_REF_ID) String cardstackObjectId) {
        if (cardstackObjectId == null || cardstackObjectId.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).build();

        // delete the cardstack in the database
        boolean success = _serviceContext.deleteCardStack(new ObjectId(cardstackObjectId));

        if (!success)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.status(Response.Status.OK).build();
    }


    /**
     * Updates a {@link easylearnServices.Core.Models.CardStack} with the given id.
     *
     * @param uriInfo Injected by JAX-RS. Used for building the correct path to
     *                the newly created resource.
     * @param id              The id of the card stack that will be updated
     * @param name              The name of the cardstack.
     * @param description       The description of the cardstack.
     * @param authorRefId      The id that references of the author.
     * @param topic             The topic of the cardstack.
     * @param allowSharing      Indicates whether it can be shared with others.
     * @return 400 BAD REQUEST if creation failed.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateCardStack(
            @Context UriInfo uriInfo,
            @FormParam(Constants.MONGO_OBJECTID_IDENTIFIER) String id,
            @FormParam(Constants.CARDSTACK_NAME) String name,
            @FormParam(Constants.CARDSTACK_DESCRIPTION) String description,
            @FormParam(Constants.CARDSTACK_AUTHOR_REF_ID) String authorRefId,
            @FormParam(Constants.CARDSTACK_TOPIC) String topic,
            @FormParam(Constants.CARDSTACK_ALLOWSHARING) boolean allowSharing ){

        if(id == null || id.isEmpty() || name == null || name.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).build();

        ObjectId  authorRefObjectId= (authorRefId == null || authorRefId.isEmpty()) ? null : new ObjectId(authorRefId);

        CardStack cardStack = _serviceContext.updateCardStack(new ObjectId(id),name, description, authorRefObjectId, topic, allowSharing);
        // creation failed, return 400
        if (cardStack == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        // success, return 200 and the cardstack object
        return Response.status(Response.Status.OK).entity(cardStack).build();
    }

    //endregion

    //region Card
    /**
     * Creates a new {@link easylearnServices.Core.Models.Card} with the given name.
     *
     * @param uriInfo        Injected by JAX-RS. Used for building the correct path to
     *                       the newly created resource.
     * @param name           The name of the card.
     * @param definition     The name of the card.
     * @param shortcut       The name of the card.
     * @param term           The name of the card.
     * @param cardstackRefId The name of the card.
     * @return 400 BAD REQUEST if creation failed.
     */
    @POST
    @Path("{"+ Constants.CARD_CARDSTACK_REF_ID  +"}/"+ Constants.MODEL_DESCRIPTION_CARD)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createCard(
            @Context UriInfo uriInfo,
            @FormParam(Constants.CARD_NAME) String name,
            @FormParam(Constants.CARD_DEFINITION) String definition,
            @FormParam(Constants.CARD_SHORTCUT) String shortcut,
            @FormParam(Constants.CARD_TERM) String term,
            @PathParam(Constants.CARD_CARDSTACK_REF_ID) String cardstackRefId) {

        if(cardstackRefId == null || cardstackRefId.isEmpty()){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Card card = _serviceContext.createCard(name, definition, shortcut, term, new ObjectId(cardstackRefId));
        // creation failed, return 400
        if (card == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        // success, return 201 and the cardstack object
        return Response.status(Response.Status.CREATED).entity(card).build();
    }

    /**
     * Fetch an existing card.
     *
     * @return 404 NOT FOUND if no cardstacks exist.
     */
    @GET
    @Path("{"+ Constants.CARD_CARDSTACK_REF_ID  +"}/"+ Constants.MODEL_DESCRIPTION_CARD +"/{"+ Constants.MONGO_OBJECTID_IDENTIFIER +"}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON + ";charset=utf-8"})
    public Response getCard(@PathParam(Constants.MONGO_OBJECTID_IDENTIFIER) String id) {
        Card card = _serviceContext.getCard(new ObjectId(id));

        if (card == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(card).build();
    }


    /**
     * Delete an existing card with the requested ID.
     *
     * @param cardObjectId The ID of the requested card.
     * @return 404 NOT FOUND if the requested card does not exist.
     */
    @DELETE
    @Path("{"+ Constants.CARD_CARDSTACK_REF_ID  +"}/"+ Constants.MODEL_DESCRIPTION_CARD +"/{"+ Constants.MONGO_OBJECTID_IDENTIFIER +"}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response deleteCard(
            @PathParam(Constants.CARD_CARDSTACK_REF_ID) String cardObjectId) {
        if (cardObjectId.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).build();

        // delete the card in the database
        boolean success = _serviceContext.deleteCard(new ObjectId(cardObjectId));

        if (!success)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.status(Response.Status.OK).build();
    }


    /**
     * Updates a {@link easylearnServices.Core.Models.Card} with the given id.
     *
     * @param uriInfo Injected by JAX-RS. Used for building the correct path to
     *                the newly created resource.
     * @param id              The id of the card that will be updated
     * @param name           The name of the card.
     * @param definition     The name of the card.
     * @param shortcut       The name of the card.
     * @param term           The name of the card.
     * @return 400 BAD REQUEST if creation failed.
     */
    @PUT
    @Path("{"+ Constants.CARD_CARDSTACK_REF_ID  +"}/"+ Constants.MODEL_DESCRIPTION_CARD +"/{"+ Constants.MONGO_OBJECTID_IDENTIFIER +"}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateCard(
            @Context UriInfo uriInfo,
            @PathParam(Constants.CARD_CARDSTACK_REF_ID) String cardStackObjectId,
            @PathParam(Constants.MONGO_OBJECTID_IDENTIFIER) String id,
            @FormParam(Constants.CARD_NAME) String name,
            @FormParam(Constants.CARD_DEFINITION) String definition,
            @FormParam(Constants.CARD_SHORTCUT) String shortcut,
            @FormParam(Constants.CARD_TERM) String term){

        if (id == null || id.isEmpty() || name == null || name.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).build();

        Card card = _serviceContext.updateCard(new ObjectId(id), name, definition, shortcut, term);
        // creation failed, return 400
        if (card == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        // success, return 201 and the cardstack object
        return Response.status(Response.Status.OK).entity(card).build();
    }


    /**
     * Fetches all Cards by the requested Cardstack ID.
     *
     * @param cardstackRefId The ID of the requested cardstack.
     * @return 404 NOT FOUND if the requested card does not exist.
     */
    @GET
    @Path("{"+ Constants.CARD_CARDSTACK_REF_ID  +"}/"+ Constants.MODEL_DESCRIPTION_CARD)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response fetchAllCardsByCardStack(
            @Context UriInfo uriInfo,
            @PathParam(Constants.CARD_CARDSTACK_REF_ID) String cardstackRefId){

        if (cardstackRefId == null || cardstackRefId.isEmpty())
            return Response.status(Response.Status.NOT_FOUND).build();

        CardCollection cards = _serviceContext.fetchCardsByCardstack(new ObjectId(cardstackRefId));

        if (cards == null || cards.isEmpty())
            return Response.status(Response.Status.NOT_FOUND).build();

        // creation failed, return 400
        if (cards == null || cards.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).build();

        // success, return 201 and the cardstack object
        return Response.status(Response.Status.OK).entity(cards).build();
    }

    //endregion


    /**
     * Creates sample Data.
     *
     * @return 201 CREATED if all data created.
     */
    @GET
    @Path("sampledata")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON + ";charset=utf-8"})
    public Response dummy() {
        _serviceContext.createSampleData();
        // success, return 201 and the cardstack object
        return Response.status(Response.Status.CREATED).build();
    }

}
