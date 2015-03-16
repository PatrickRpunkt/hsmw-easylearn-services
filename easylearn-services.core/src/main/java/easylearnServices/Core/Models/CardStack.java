package easylearnServices.Core.Models;

import easylearnServices.Core.Common.Constants;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.*;

/**
 * Created by patrickreichelt on 24/02/15.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CardStack implements IModelBase {

    /// <summary>
    /// Konstruktor
    /// </summary>
    public CardStack() {
        super();
        set_cards(new CardCollection());
        set_id(new ObjectId());
    }

    public CardStack(String name) {
        super();
        set_name(name);
        set_cards(new CardCollection());
        set_id(new ObjectId());
    }

    public CardStack(Document base) {
        super();
        set_cards(new CardCollection());
        if (base != null)
            MapDataMongoDocument(base);
    }

    @XmlElement(name = Constants.MONGO_OBJECTID_IDENTIFIER)
    public String get_idString() {
        return _id != null ?_id.toString() : "n/a";
    }

    /// <summary>
    /// ID des Kartenstapels
    /// </summary>
    @XmlTransient
    private ObjectId _id;

    /// <summary>
    /// Name des Kartenstapels
    /// </summary>
    @XmlElement(name = Constants.CARDSTACK_NAME)
    private String _name;

    /// <summary>
    /// Thema des Kartenstapels
    /// </summary>
    @XmlElement(name = Constants.CARDSTACK_TOPIC)
    private String _topic;

    /// <summary>
    /// Flag ob Stapel im Store auftauchen darf
    /// </summary>
    @XmlElement(name = Constants.CARDSTACK_ALLOWSHARING)
    private boolean _allowSharing;

    /// <summary>
    /// Name des Autors
    /// </summary>
    @XmlElement(name = Constants.CARDSTACK_AUTHOR)
    private Author _author;

    @XmlTransient
    private ObjectId _authorId;

    /// <summary>
    /// Beschreibung des Kartenstapels
    /// </summary>
    @XmlElement(name = Constants.CARDSTACK_DESCRIPTION)
    private String _description;

    /// <summary>
    /// Karten des Stapels
    /// </summary>
    @XmlElement(name = Constants.CARDSTACK_CARDS)
    private CardCollection _cards;

    /// <summary>
    /// Letztes Bearbeitungsdatum
    /// </summary>
    @XmlElement(name = Constants.CARDSTACK_MODIFIEDDATE)
    private DateTime _modifiedDate;


    @Override
    public void MapDataMongoDocument(Document doc) {
        set_authorRefId(doc.getObjectId(Constants.CARDSTACK_AUTHOR_REF_ID));
        set_id(doc.getObjectId(Constants.MONGO_OBJECTID_IDENTIFIER));
        set_allowSharing(doc.getBoolean(Constants.CARDSTACK_ALLOWSHARING));
        set_description(doc.getString(Constants.CARDSTACK_DESCRIPTION));
        set_name(doc.getString(Constants.CARDSTACK_NAME));
        set_topic(doc.getString(Constants.CARDSTACK_TOPIC));
    }

    @Override
    public Document GenerateMongoDocument() {
        Document document = new Document();

        document.put(Constants.MONGO_OBJECTID_IDENTIFIER , get_id());
        document.put(Constants.CARDSTACK_AUTHOR_REF_ID , get_authorRefId());
        document.put(Constants.CARDSTACK_NAME , get_name());
        document.put(Constants.CARDSTACK_ALLOWSHARING , is_allowSharing());
        document.put(Constants.CARDSTACK_DESCRIPTION , get_description());
        document.put(Constants.CARDSTACK_MODIFIEDDATE , get_modifiedDate());
        document.put(Constants.CARDSTACK_TOPIC,get_topic());

        return document;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_topic() {
        return _topic;
    }

    public void set_topic(String _topic) {
        this._topic = _topic;
    }

    public boolean is_allowSharing() {
        return _allowSharing;
    }

    public void set_allowSharing(boolean _allowSharing) {
        this._allowSharing = _allowSharing;
    }

    public Author get_author() {
        return _author;
    }

    public void set_author(Author _author) {
        this._author = _author;
    }

    public ObjectId get_authorRefId() {
        return _authorId;
    }

    public void set_authorRefId(ObjectId _authorId) {
        this._authorId = _authorId;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public CardCollection get_cards() {
        return _cards;
    }

    public void set_cards(CardCollection _cards) {
        this._cards = _cards;
    }

    public DateTime get_modifiedDate() {
        return _modifiedDate;
    }

    public void set_modifiedDate(DateTime _modifiedDate) {
        this._modifiedDate = _modifiedDate;
    }
}
