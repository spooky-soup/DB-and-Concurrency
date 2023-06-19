package XML_schema;

import jakarta.xml.bind.annotation.*;
import parser.Person;

import java.util.Set;
import java.util.stream.Collectors;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "person-type", propOrder = {"ID", "firstName", "familyName", "gender", "fatherID", "motherID", "spouseID", "daughtersIDs", "sonsIDs", "sistersIDs", "brothersIDs"})

public class XmlPerson {
    @XmlID
    @XmlElement(name = "ID", required = true)
    private String ID;
    private String firstName;
    private String familyName;
    private String gender;

    private String motherID;
    private String fatherID;

    private String spouseID;

    private final Set<String> sistersIDs;
    private final Set<String> brothersIDs;

    //@jakarta.xml.bind.annotation.XmlIDREF
    //@jakarta.xml.bind.annotation.XmlElementWrapper
    //@jakarta.xml.bind.annotation.XmlElement(name = "daughter")
    private final Set<String> daughtersIDs;
    //@jakarta.xml.bind.annotation.XmlIDREF
    //@jakarta.xml.bind.annotation.XmlElementWrapper
    //@jakarta.xml.bind.annotation.XmlElement(name = "son")
    private final Set<String> sonsIDs;

    public XmlPerson(Person person) {
        this.ID = person.getID();
        this.firstName = person.getFirst_name();
        this.familyName = person.getFamily_name();
        this.gender = (person.getGender() == Person.Gender.M) ? "M" : "F";
        this.motherID = person.getMother() == null ? null : person.getMother().getID();
        this.fatherID = person.getMother() == null ? null : person.getMother().getID();
        this.sistersIDs = person.getSisters().isEmpty() ? null : person.getSisters().stream().map(Person::getID).collect(Collectors.toSet());
        this.brothersIDs = person.getBrothers().isEmpty() ? null : person.getBrothers().stream().map(Person::getID).collect(Collectors.toSet());
        this.daughtersIDs = person.getDaughters().isEmpty() ? null : person.getDaughters().stream().map(Person::getID).collect(Collectors.toSet());
        this.sonsIDs = person.getSons().isEmpty() ? null : person.getSons().stream().map(Person::getID).collect(Collectors.toSet());

        if (person.getWife() != null) {
            this.spouseID = person.getWife().getID();
        } else if (person.getHusband() != null) {
            this.spouseID = person.getHusband().getID();
        } else {
            this.spouseID = person.getSpouce() == null ? null : person.getSpouce().getID();
        }
    }

    @Override
    public String toString() {
        return "people_parser.people_parser.Person{" +
                "id='" + ID + '\'' +
                ", fullName='" + firstName + " " + familyName + '\'' +
                ", gender='" + gender + '\'' +
                ((motherID == null) ? "" : ", mother=" + motherID) +
                ((fatherID == null) ? "" : ", father=" + fatherID) +
                ((spouseID == null) ? "" : ", spouse=" + spouseID) +
                ", siblings=" + (sistersIDs.size() + brothersIDs.size()) +
                ", children=" + (daughtersIDs.size() + sonsIDs.size()) +
                '}';
    }

}
