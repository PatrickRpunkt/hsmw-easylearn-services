package easylearnServices.Core.Models;

import easylearnServices.Core.Common.Constants;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.xml.bind.annotation.*;
import java.util.UUID;

/**
 * Created by patrickreichelt on 24/02/15.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Card implements IModelBase{



    /// <summary>
    /// Konstruktor
    /// </summary>
    public Card()
    {
        set_id(new ObjectId());
    }

    public Card(Document base) {
        if (base != null)
            MapDataMongoDocument(base);
    }

    /// <summary>
    /// ID der Karte
    /// </summary>
    @XmlTransient
    private ObjectId _id;

    /// <summary>
    /// Name der Karte
    /// </summary>
    @XmlElement(name = Constants.CARD_NAME)
    private String _name;

    /// <summary>
    /// Definition (Seite hinten)
    /// </summary>
    @XmlElement(name = Constants.CARD_DEFINITION)
    private String _definition;

    /// <summary>
    /// Zu lernendes Wort/Ausdruck
    /// </summary>
    @XmlElement(name = Constants.CARD_TERM)
    private String _term;

    /// <summary>
    /// Kurzbezeichnung
    /// </summary>
    @XmlElement(name = Constants.CARD_SHORTCUT)
    private String _shortcut;

    /// <summary>
    /// Ref Id
    /// </summary>
    @XmlTransient
    private ObjectId _cardStackRefId;


    @Override
    public void MapDataMongoDocument(Document doc) {
        set_id(doc.getObjectId(Constants.MONGO_OBJECTID_IDENTIFIER));
        set_name(doc.getString(Constants.CARD_NAME));
        set_cardStackRefId(doc.getObjectId(Constants.CARD_CARDSTACK_REF_ID));
        set_definition(doc.getString(Constants.CARD_DEFINITION));
        set_shortcut(doc.getString(Constants.CARD_SHORTCUT));
        set_term(doc.getString(Constants.CARD_TERM));
    }

    @Override
    public Document GenerateMongoDocument() {
        Document document = new Document();
        document.put(Constants.MONGO_OBJECTID_IDENTIFIER,get_id());
        document.put(Constants.CARD_NAME,get_name());
        document.put(Constants.CARD_DEFINITION,get_definition());
        document.put(Constants.CARD_TERM,get_term());
        document.put(Constants.CARD_SHORTCUT,get_shortcut());
        document.put(Constants.CARD_CARDSTACK_REF_ID,get_cardStackRefId());
        return document;
    }

    @XmlElement(name = Constants.MONGO_OBJECTID_IDENTIFIER)
    public String get_idString() {
       return _id != null ?_id.toString() : "n/a";
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

    public String get_definition() {
        return _definition;
    }

    public void set_definition(String _definition) {
        this._definition = _definition;
    }

    public String get_term() {
        return _term;
    }

    public void set_term(String _term) {
        this._term = _term;
    }

    public String get_shortcut() {
        return _shortcut;
    }

    public void set_shortcut(String _shortcut) {
        this._shortcut = _shortcut;
    }

    public ObjectId get_cardStackRefId() {
        return _cardStackRefId;
    }

    public void set_cardStackRefId(ObjectId _cardStackRefId) {
        this._cardStackRefId = _cardStackRefId;
    }
}
