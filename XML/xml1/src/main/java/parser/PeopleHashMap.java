package parser;

import java.util.HashMap;
import java.util.Objects;

public class PeopleHashMap extends HashMap<String, Person>{
    public void createSpouses(Person wife, Person husband) {
        wife.setGender(Person.Gender.F);
        wife.setHusband(husband);
        husband.setGender(Person.Gender.M);
        husband.setWife(wife);
    }

    public void createUnknownSpouses(Person roditel1, Person roditel2) {
        if (roditel1.getGender() == Person.Gender.M || roditel2.getGender() == Person.Gender.F) {
            createSpouses(roditel2, roditel1);
        } else if (roditel2.getGender() == Person.Gender.M || roditel1.getGender() == Person.Gender.F) {
            createSpouses(roditel2, roditel1);
        } else {
            roditel1.setSpouce(roditel2);
            roditel2.setSpouce(roditel1);
        }
    }

    public void createSiblings(Person sib1, Person sib2, Person.Gender gen2) {
        if (gen2 != null && sib2.getGender() == null)
            sib2.setGender(gen2);
        if (gen2 == null) {
            if (sib2.getGender()!=null)
                gen2 = sib2.getGender();
            else
                sib1.addSibling(sib2);
        }
        else if (gen2 == Person.Gender.F)
            sib1.addSister(sib2);
        else
            sib1.addBrother(sib2);

        Person.Gender gen1 = sib1.getGender();
        if (gen1 == null)
            sib2.addSibling(sib1);
        else if (gen1 == Person.Gender.F)
            sib2.addSister(sib1);
        else
            sib2.addBrother(sib1);
    }

    public void createChildParent(Person child, Person.Gender childGender, Person parent, Person.Gender parentGender) {
        if (childGender == null && child.getGender() != null)
            childGender = child.getGender();
        if (childGender == null) parent.addChild(child);
        else {
            switch (childGender) {
                case F -> parent.addDaughter(child);
                case M -> parent.addSon(child);
            }
        }
        if (parentGender == null) child.addParent(parent);
        else {
            switch (parentGender) {
                case F -> child.setMother(parent);
                case M -> child.setFather(parent);
            }
        }
    }

    public Person getByName(String fullName) {
        if (fullName.equals("NONE") || fullName.equals("UNKNOWN")) {
            return null;
        }
        for (Person person : this.values()) {
            String currFullName = person.getFirst_name() + " " + person.getFamily_name();
            if (currFullName.equals(fullName)) {
                return person;
            }
        }
        if (fullName.split(" ", 2).length != 2) {
            System.out.println(fullName);
        }
        Person newPerson = new Person(fullName.split(" ", 2)[0], fullName.split(" ",2)[1]);
        this.put(fullName, newPerson);
        return newPerson;
        //return null;
    }

    public String getIDByName(String fullName) {
        String ID = getByName(fullName).getID();
        return Objects.requireNonNullElse(ID, fullName);
    }

    private boolean isID(String str) {
        return Character.isDigit(str.toCharArray()[1]);
    }

    public void addPerson(Person person) {
        String fullName = person.getFirst_name() + " " + person.getFamily_name();
        if (person.getID() != null) {
            if (!this.containsKey(person.getID())) {
                if (this.containsKey(fullName)) {
                    this.put(person.getID(), this.remove(fullName));
                    boolean isNameSake = this.get(person.getID()).addInfo(person);
                    if (isNameSake) {
                        this.put(person.getID(), person);
                    }
                    return;
                }
                this.putIfAbsent(person.getID(), person);
            } else {
                this.get(person.getID()).addInfo(person);
            }
        } else {
            if (this.containsKey(fullName)) {
                this.put(person.getID(), this.remove(fullName));
                this.get(person.getID()).addInfo(person);
            } else {
                if (this.getByName(fullName) != null) {
                    this.getByName(fullName).addInfo(person);
                }
                else {
                    this.putIfAbsent(fullName, person);
                }
            }
        }

    }
}
