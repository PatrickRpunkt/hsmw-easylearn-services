package easylearnServices.Core.Models;

import easylearnServices.Core.Common.Constants;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import org.bson.BSONObject;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by patrickreichelt on 24/02/15.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Author implements IModelBase {


    public Author() {
        super();
        set_id(new ObjectId());
    }

    public Author(String firstName, String lastName, String userName, String email) {
        super();
        set_id(new ObjectId());
        set_firstName(firstName);
        set_lastName(lastName);
        set_userName(userName);
        set_eMail(email);
    }

    public Author(Document base) {
        super();
        System.out.println(base);
        if (base != null)
            MapDataMongoDocument(base);
    }

    /// <summary>
    /// Nutzerid
    /// </summary>
    @XmlTransient
    private ObjectId _id;

    /// <summary>
    /// Vorname
    /// </summary>
    @XmlElement(name = Constants.AUTHOR_FIRST_NAME)
    private String _firstName;

    /// <summary>
    /// Nachname
    /// </summary>
    @XmlElement(name = Constants.AUTHOR_LAST_NAME)
    private String _lastName;

    /// <summary>
    /// Nutzername
    /// </summary>
    @XmlElement(name = Constants.AUTHOR_USER_NAME)
    private String _userName;

    /// <summary>
    /// Mailadresse
    /// </summary>
    @XmlElement(name = Constants.AUTHOR_E_MAIL)
    private String _eMail;

    /// <summary>
    /// Passwort
    /// </summary>
    @XmlElement(name = Constants.AUTHOR_PASSWORD)
    private String _password;


    @Override
    public void MapDataMongoDocument(Document doc) {
        set_firstName(doc.getString(Constants.AUTHOR_FIRST_NAME));
        set_lastName(doc.getString(Constants.AUTHOR_LAST_NAME));
        set_userName(doc.getString(Constants.AUTHOR_USER_NAME));
        set_eMail(doc.getString(Constants.AUTHOR_E_MAIL));
        set_password(doc.getString(Constants.AUTHOR_PASSWORD));
        set_id(doc.getObjectId(Constants.MONGO_OBJECTID_IDENTIFIER));
    }


    public Document GenerateMongoDocument() {
        Document doc = new Document();
        doc.put(Constants.MONGO_OBJECTID_IDENTIFIER, get_id());
        doc.put(Constants.AUTHOR_FIRST_NAME, get_firstName());
        doc.put(Constants.AUTHOR_LAST_NAME, get_lastName());
        doc.put(Constants.AUTHOR_USER_NAME, get_userName());
        doc.put(Constants.AUTHOR_E_MAIL, get_eMail());
        doc.put(Constants.AUTHOR_PASSWORD, get_password());
        return doc;
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

    public String get_firstName() {
        return _firstName;
    }

    public void set_firstName(String _firstName) {
        this._firstName = _firstName;
    }

    public String get_lastName() {
        return _lastName;
    }

    public void set_lastName(String _lastName) {
        this._lastName = _lastName;
    }

    public String get_userName() {
        return _userName;
    }

    public void set_userName(String _userName) {
        this._userName = _userName;
    }

    public String get_eMail() {
        return _eMail;
    }

    public void set_eMail(String _eMail) {
        this._eMail = _eMail;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }
}
