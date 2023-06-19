package parser;

import XML_schema.XmlGenerator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class StaxParser {
    public PeopleHashMap getPeopleList() {
        return peopleList;
    }

    static PeopleHashMap peopleList = new PeopleHashMap();
    public static void main(String[] args) {
        String fileName = "people.xml";
        //String fileName = "peoplePartial.xml";
        HashMap<String, Person> peopleList = parseXMLfile(fileName);
        // печатаем в консоль информацию по каждому человеку
        for (Person person : peopleList.values()) {
            person.printInfo();
        }
        System.out.print("-----------------\nTotal count: ");
        System.out.print(peopleList.values().size());

        XmlGenerator generator = new XmlGenerator();
        generator.generateXml(new HashSet<>(peopleList.values()));
    }




    private static HashMap<String, Person> parseXMLfile(String fileName) {
        HashMap<String, String> relativeList = null;
        Person person = null;
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        int relCnt = 0;
        try {
            // инициализируем reader и скармливаем ему xml файл
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(fileName));
            // проходим по всем элементам xml файла
            while (reader.hasNext()) {
                // получаем событие (элемент) и разбираем его по атрибутам
                XMLEvent xmlEvent = reader.nextEvent();
                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();
                    String eventName = startElement.getName().getLocalPart();
                    if (eventName.contains("person")) {
                        relCnt = 0;
                        person = new Person();
                        relativeList = new HashMap<>();
                        String fullName;
                        // Обрабатываем аттрибуты id и name для каждого элемента
                        Attribute idAttr = startElement.getAttributeByName(new QName("id"));
                        Attribute fullNameAttr = startElement.getAttributeByName(new QName("name"));
                        if (idAttr != null) {
                            person.setId(idAttr.getValue());
                        }
                        if (fullNameAttr != null) {
                            fullName = fullNameAttr.getValue();
                            person.setFullName(fullName);
                        }
                    }
                    else if (eventName.contains("first")) {
                        xmlEvent = reader.nextEvent();
                        Attribute nameAttr = startElement.getAttributeByName(new QName("name"));
                        if (nameAttr == null) {
                            nameAttr = startElement.getAttributeByName(new QName("value"));
                        }
                        String firstName;
                        if (nameAttr != null) {
                            firstName = nameAttr.getValue();
                        } else {
                            firstName = xmlEvent.asCharacters().getData();
                        }
                        person.setFirst_name(firstName);
                    }
                    else if (eventName.contains("family")) {
                        xmlEvent = reader.nextEvent();
                        person.setFamily_name(xmlEvent.asCharacters().getData());
                    }
                    else if (eventName.equals("surname")) {
                        String surname = startElement.getAttributeByName(new QName("value")).getValue();
                        person.setFamily_name(surname);
                    }
                    else if (eventName.equals("gender")) {
                        xmlEvent = reader.nextEvent();
                        Attribute genAttr = startElement.getAttributeByName(new QName("value"));
                        Person.Gender g;
                        if (genAttr != null) {
                            g = (genAttr.getValue().equals("male") ? Person.Gender.M : Person.Gender.F);
                        } else {
                            g = (xmlEvent.asCharacters().getData().equals("M") ? Person.Gender.M : Person.Gender.F);
                        }
                        person.setGender(g);
                    }
                    else if (eventName.equals("husband")) {
                        String husbandID = startElement.getAttributeByName(new QName("value")).getValue();
                        //peopleList.createSpouses(person.getID(), husbandID);
                        relativeList.put("husband", husbandID);
                    }
                    else if (eventName.equals("wife")) {
                        String wifeID = startElement.getAttributeByName(new QName("value")).getValue();
                        relativeList.put("wife", wifeID);
                    }
                    else if (eventName.equals("spouce")) {
                        Attribute spouceAttr = startElement.getAttributeByName(new QName("value"));
                        if (spouceAttr != null) {
                            String spouceName = spouceAttr.getValue();
                            if (!spouceName.equals("NONE")) {
                                spouceName = spouceName.startsWith(" ") ? spouceName.substring(1) : spouceName;
                                spouceName = spouceName.replace("  ", " ");
                                relativeList.put("spouse", peopleList.getIDByName(spouceName));
                            }
                        }
                    }
                    else if (eventName.equals("brother")) {
                        xmlEvent = reader.nextEvent();
                        relativeList.put("brother-" + relCnt, xmlEvent.asCharacters().getData());
                        relCnt++;
                    }
                    else if (eventName.equals("sister")) {
                        xmlEvent = reader.nextEvent();
                        relativeList.put("sister-" + relCnt, xmlEvent.asCharacters().getData());
                        relCnt++;
                    }
                    else if (eventName.equals("siblings")) {
                        Attribute sibAttr = startElement.getAttributeByName(new QName("val"));
                        if (sibAttr != null) {
                            for (String sibID : sibAttr.getValue().split(" ")) {
                                relativeList.put("sibling-" + relCnt, sibID);
                                relCnt++;
                            }

                        }
                    }

                    else if (eventName.equals("daughter")) {
                        Attribute dAttr = startElement.getAttributeByName(new QName("id"));
                        if (dAttr != null)
                            relativeList.put("daughter-" + relCnt, dAttr.getValue());
                        relCnt++;
                    }
                    else if (eventName.equals("son")) {
                        Attribute dAttr = startElement.getAttributeByName(new QName("id"));
                        if (dAttr != null)
                            relativeList.put("son-" + relCnt, dAttr.getValue());
                        relCnt++;
                    }
                    else if (eventName.equals("child")) {
                        xmlEvent = reader.nextEvent();
                        relativeList.put("child-" + relCnt, xmlEvent.asCharacters().getData().replace("  ", " "));
                        relCnt++;
                    }
                }
                // если цикл дошел до закрывающего элемента person,
                // то добавляем считанного человека в список
                if (xmlEvent.isEndElement()) {
                    EndElement endElement = xmlEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equals("person")) {
                        peopleList.addPerson(person);
                        //TODO: Добавляем родственные связи:
                        setRelatives(person, relativeList);
                    }
                }
            }

        } catch (FileNotFoundException | XMLStreamException exc) {
            exc.printStackTrace();
        }
        return peopleList;
    }


    private static void setRelatives(Person person, HashMap<String, String> relativeList) {
        for (String relType : relativeList.keySet()) {
            Person rel;
            String nameID = relativeList.get(relType);
            if (nameID.startsWith("P")) {
                peopleList.putIfAbsent(nameID, new Person());
                rel = peopleList.get(nameID);
            }
            else
                rel = peopleList.getByName(nameID);

            switch (relType.split("-")[0]) {
                case ("wife") -> peopleList.createSpouses(rel, person);
                case ("husband") -> peopleList.createSpouses(person, rel);
                case ("spouse") -> peopleList.createUnknownSpouses(person, rel);

                case ("son") -> peopleList.createChildParent(rel, Person.Gender.M, person, person.getGender());
                case ("daughter") -> peopleList.createChildParent(rel, Person.Gender.F, person, person.getGender());
                case ("child") -> peopleList.createChildParent(rel, null, person, person.getGender());

                case ("sister") -> peopleList.createSiblings(person, rel, Person.Gender.F);
                case ("brother") -> peopleList.createSiblings(person, rel, Person.Gender.M);
                case ("sibling") -> peopleList.createSiblings(person, rel, null);
            }
        }
    }

    private String getCharacterData(XMLEvent event, XMLEventReader eventReader) throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        //if (event instanceof CharacterEvent) {
            result = event.asCharacters().getData();
        //}
        return result;

    }
}
