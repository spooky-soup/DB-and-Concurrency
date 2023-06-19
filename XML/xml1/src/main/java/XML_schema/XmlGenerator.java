package XML_schema;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.xml.sax.SAXException;
import parser.Person;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class XmlGenerator {
    private Set<XmlPerson> setToXml(Set<Person> peopleSet) {
        Set<XmlPerson> xmlPeople = new HashSet<>();
        for (Person person : peopleSet) {
            if (person.getID() != null && person.getFirst_name() != null && person.getFamily_name() != null) {
                xmlPeople.add(new XmlPerson(person));
            }
        }
        return xmlPeople;
    }

    public void generateXml(Set<Person> peopleSet) {
        XmlPeopleList people = new XmlPeopleList();
        people.addAll(setToXml(peopleSet));
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File("people.xsd"));

            JAXBContext context = JAXBContext.newInstance(XmlPeopleList.class);
            Marshaller mar = context.createMarshaller();
            mar.setSchema(schema);
            mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            mar.marshal(people, new File("new_people.xml"));

        } catch (SAXException | JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
