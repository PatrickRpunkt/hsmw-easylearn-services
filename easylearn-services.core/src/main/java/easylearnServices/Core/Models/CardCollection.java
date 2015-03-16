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
public class CardCollection {

    @XmlElement(name = "card")
    private ArrayList<Card> _collection;

    /**
     * constructor
     */

    public CardCollection() {
        _collection = new ArrayList<Card>();
    }

    public ArrayList<Card> getElements() {
        return _collection;
    }

    public void setElements(ArrayList<Card> elements) {
        _collection = elements;
    }

    public void add(Card element) {
        _collection.add(element);
    }

    public void remove(Card element) {
        _collection.remove(element);
    }

    public boolean isEmpty() {
        return _collection.isEmpty();
    }

}
