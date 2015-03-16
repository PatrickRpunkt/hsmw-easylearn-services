package easylearnServices.Core.Models;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 * Created by patrickreichelt on 26/02/15.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CardStackCollection {

    @XmlElement(name = "cardstack")
    private ArrayList<CardStack> _cardstackCollection;

    /**
     * constructor
     */

    public CardStackCollection() {
        _cardstackCollection = new ArrayList<CardStack>();
    }

    public ArrayList<CardStack> getElements() {
        return _cardstackCollection;
    }

    public void setElements(ArrayList<CardStack> elements) {
        _cardstackCollection = elements;
    }

    public void add(CardStack element) {
        _cardstackCollection.add(element);
    }

    public void remove(CardStack element) {
       _cardstackCollection.remove(element);
    }

    public boolean isEmpty() {
        return _cardstackCollection.isEmpty();
    }

}
