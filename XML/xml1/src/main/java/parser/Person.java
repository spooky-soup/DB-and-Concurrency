package parser;

import java.lang.reflect.Field;
import java.util.*;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

public class Person {
    public Person(String first_name, String family_name) {
        this.first_name = first_name;
        this.family_name = family_name;
    }
    public Person() {}

    public void setId(String id) {
        if (this.id != null || id == null) {
            return;
        }
        this.id = id;
    }
    public String getID() {
        return id;
    }

    public void setWife(Person wife) {
        if (this.wife != null || wife == null) {
            return;
        }
        this.wife = wife;
    }
    public Person getWife() {
        return wife;
    }
    public Person getHusband() {
        return husband;
    }
    public void setHusband(Person husband) {
        if (this.husband != null || husband == null) {
            return;
        }
        this.husband = husband;
    }
    public void setMother(Person mother) {
        if (this.mother != null || mother == null) {
            return;
        }
        this.mother = mother;
    }
    public Person getMother() {
        return mother;
    }

    public Person getFather() {
        return father;
    }

    public Gender getGender() {
        return gender;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getFamily_name() {
        return family_name;
    }

    public int getSiblings_number() {
        return siblings_number;
    }

    public int getChildren_number() {
        return children_number;
    }

    public List<Person> getSisters() {
        return safe(sisters);
    }

    public List<Person> getBrothers() {
        return safe(brothers);
    }

    public List<Person> getSons() {
        return safe(sons);
    }

    public List<Person> getDaughters() {
        return safe(daughters);
    }

    String id;
    Person wife;
    Person husband;
    Person mother;
    Person father;

    public Person getSpouce() {
        return spouce;
    }

    public void setSpouce(Person spouce) {
        this.spouce = spouce;
    }

    Person spouce;


    public List<Person> getParents() {
        return safe(parents);
    }

    public void setParents(List<Person> parents) {
        this.parents = parents;
    }

    List<Person> parents = new ArrayList<>();
    public enum Gender{M, F};
    Gender gender;
    String first_name;
    String family_name;
    int siblings_number = 0;
    int children_number = 0;

    public void setFather(Person father) {
        if (this.father != null || father == null) {
            return;
        }
        this.father = father;
    }

    public void setGender(Gender g) {
        if (this.gender != null || g == null) {
            return;
        }
        this.gender = g;

        //Меняем этого ребенка на дочь или сына:
        List<Person> allParents = new ArrayList<>();

        allParents.addAll(emptyIfNull(this.parents));
        allParents.add(this.getMother());
        allParents.add(this.getFather());
        for (Person parent : allParents) {
            if (parent != null) {
                if (parent.getChildren().contains(this)) {
                    parent.getChildren().remove(this);
                    switch (g) {
                        case F -> parent.addDaughter(this);
                        case M -> parent.addSon(this);
                    }
                }
            }
        }
        //Меняем этого родителя на мать или отца
        List<Person> allChildren = new ArrayList<>();
        allChildren.addAll(emptyIfNull(this.children));
        allChildren.addAll(emptyIfNull(this.daughters));
        allChildren.addAll(emptyIfNull(this.sons));
        for (Person child : allChildren) {
            if (child.getParents().contains(this)) {
                child.getParents().remove(this);
                switch (g) {
                    case F -> child.setFather(this);
                    case M -> child.setMother(this);
                }
            }
        }
        //Меняем этого бесполого ребенка тех же родителей на брата или сестру
        List<Person> allSiblings = new ArrayList<>();

        allSiblings.addAll(emptyIfNull(this.siblings));
        allSiblings.addAll(emptyIfNull(this.brothers));
        allSiblings.addAll(emptyIfNull(this.sisters));
        for (Person sibling : allSiblings) {
            if (sibling.getSiblings().contains(this)) {
                sibling.getSiblings().remove(this);
                switch (g) {
                    case M -> sibling.addBrother(this);
                    case F -> sibling.addSister(this);
                }
            }
        }

    }

    public void setFullName(String full_name) {
            full_name = full_name.startsWith(" ") ? full_name.substring(1) : full_name;
            String[] parts = full_name.split(" +");
            setFirst_name(parts[0]);
            setFamily_name(parts[1]);
    }

    public void setFirst_name(String first_name) {
        if (this.first_name != null || first_name == null) {
            return;
        }
        this.first_name = first_name.replace(" ", "");
    }

    public void setFamily_name(String family_name) {
        if (this.family_name != null || family_name == null) {
            return;
        }
        this.family_name = family_name.replace(" ", "");
    }

    public void setSiblings_number(int siblings_number) {
        this.siblings_number = siblings_number;
    }

    public void setChildren_number(int children_number) {
        this.children_number = children_number;
    }

    List<Person> sisters = new ArrayList<>();
    List<Person> brothers = new ArrayList<>();

    public List<Person> getSiblings() {
        return safe(siblings);
    }

    public void setSiblings(List<Person> siblings) {
        this.siblings = siblings;
    }

    List<Person> siblings = new ArrayList<>();
    List<Person> sons = new ArrayList<>();
    List<Person> daughters = new ArrayList<>();
    List<Person> children = new ArrayList<>();

    public List<Person> getChildren() {
        return safe(children);
    }

    public void setChildren(List<Person> children) {
        this.children = children;
    }

    public void printInfo() {
        Field[] fields = Person.class.getDeclaredFields();
        System.out.println("____________");
        for (Field field : fields) {
            try {
                Object val = field.get(this);
                if (val != null && !val.equals(0)) {
                    if (Person.class.isAssignableFrom(field.get(this).getClass()))
                        System.out.println(((Person) field.get(this)).getID());
                    System.out.println(field.getName() + ": " + field.get(this));
                }
            } catch (IllegalAccessException e) {
                System.err.println("Can't access some field while printing info...");
            }
        }
    }

    public boolean addInfo(Person newRecord){
        Field[] fields = Person.class.getDeclaredFields();
        boolean acc;
        try {
            for (Field field : fields) {
                acc = field.canAccess(newRecord);
                field.setAccessible(true);
                //Если есть поле, разное для двух записей, то считаем, что это тезка
                if (!Collection.class.isAssignableFrom(field.getType())) {
                    if (field.get(this) != null && field.get(newRecord) != null && field.get(newRecord) != field.get(this)) {
                        return true;
                    }
                }
                field.setAccessible(acc);
            }
            for (Field field : fields) {
                acc = field.canAccess(newRecord);
                field.setAccessible(true);
                if (!Collection.class.isAssignableFrom(field.getType())) {
                    if ((field.get(newRecord) != null) && field.get(this) == null) {
                        field.set(this, field.get(newRecord));
                    }
                }
                //TODO: add list fields check
            }
        } catch (IllegalAccessException e) {
            System.err.println("Can't access some field while adding info...");
        }
            /*this.setId(newRecord.getID());
            this.setGender(newRecord.getGender());
            this.setFirst_name(newRecord.getFirst_name());
            this.setFamily_name(newRecord.getFamily_name());
            this.setMother(newRecord.getMother());
            this.setFather(newRecord.getFather());
            this.setWife(newRecord.getWife());
            this.setHusband(newRecord.getHusband());
            this.setChildren_number(newRecord.getChildren_number());
            this.setSiblings_number(newRecord.getSiblings_number());*/
            for (Person daughter : newRecord.getDaughters()) {
                if (!this.daughters.contains(daughter))
                    this.addDaughter(daughter);
            }
            for (Person son : newRecord.getSons()) {
                if (!this.sons.contains(son))
                    this.addSon(son);
            }
            for (Person child : newRecord.getChildren()) {
                if (!this.children.contains(child))
                    this.addChild(child);
            }
            for (Person brother : newRecord.getBrothers()) {
                if (!this.brothers.contains(brother))
                    this.addBrother(brother);
            }
            for (Person sister : newRecord.getSisters()) {
                if (!this.sisters.contains(sister))
                    this.addSister(sister);
            }
            for (Person sibling : newRecord.getSiblings()) {
                if (!this.siblings.contains(sibling))
                    this.addSibling(sibling);
            }
            for (Person parent : newRecord.getParents()) {
                if (!this.parents.contains(parent))
                    this.addParent(parent);
            }
            return false;
    }
    public void addDaughter(Person daughter) {
        if (!daughters.contains(daughter)) {
            this.daughters.add(daughter);
        }
    }
    public void addSon(Person son) {
        if (!sons.contains(son)) {
            this.sons.add(son);
        }
    }
    public void addChild(Person child) {
        if (child.getGender() == Person.Gender.F) {
            this.addDaughter(child);
            this.children.remove(child);
        } else if (child.getGender() == Person.Gender.M) {
            this.addSon(child);
            this.children.remove(child);
        } else {
            if (!children.contains(child)) {
                children.add(child);
            }
        }
    }
    public void addSister(Person sister) {
        if (!sisters.contains(sister)) {
            this.sisters.add(sister);
        }
    }
    public void addBrother(Person brother) {
        if (!brothers.contains(brother)) {
            this.brothers.add(brother);
        }
    }

    public void addSibling(Person sibling) {
        if (sibling.getGender() == Person.Gender.F) {
            this.addSister(sibling);
            this.siblings.remove(sibling);
        } else if (sibling.getGender() == Person.Gender.M) {
            this.addBrother(sibling);
            this.siblings.remove(sibling);
        } else {
            if (!siblings.contains(sibling)) {
                siblings.add(sibling);
            }
        }
    }

    public void addParent(Person parent) {
        if (parent.getGender() == Gender.M) {
            this.setFather(parent);
            parents.remove(parent);
        } else if (parent.getGender() == Gender.F) {
            this.setMother(parent);
            parents.remove(parent);
        } else {
            parents.add(parent);
        }
    }

    public static List<Person> safe( List<Person> other ) {
        return other == null ? Collections.EMPTY_LIST : other;
    }
}
