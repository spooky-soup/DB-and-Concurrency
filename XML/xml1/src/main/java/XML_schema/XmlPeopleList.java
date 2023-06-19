package XML_schema;


import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "people-type")
public class XmlPeopleList {
    @XmlElement(name = "xmlPerson")
    private final List<XmlPerson> people;

    public XmlPeopleList() {
        people = new ArrayList<>();
    }

    public void add(XmlPerson person) {
        if (person != null) people.add(person);
    }
    public void addAll(Collection<XmlPerson> people) {
        for (XmlPerson person : people) {
            this.add(person);
        }
    }
}
