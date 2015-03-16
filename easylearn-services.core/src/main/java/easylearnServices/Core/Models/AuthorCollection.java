package easylearnServices.Core.Models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * Created by patrickreichelt on 26/02/15.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AuthorCollection {

    @XmlElement(name = "author")
    private ArrayList<Author> _collection;

    /**
     * constructor
     */

    public AuthorCollection() {
        _collection = new ArrayList<Author>();
    }

    public ArrayList<Author> getElements() {
        return _collection;
    }

    public void setElements(ArrayList<Author> elements) {
        _collection = elements;
    }

    public void add(Author element) {
        _collection.add(element);
    }

    public void remove(Author element) {
        _collection.remove(element);
    }

    public boolean isEmpty() {
        return _collection.isEmpty();
    }

}
