package com.aezhou.ifonethenfun;

import java.util.HashSet;
import java.util.Set;

/**
 * Class for the employee
 */
public class Person {
    public final String email;
    public final Set<Person> teammates;
    private Set<String> teams;

    public Person(String email) {
        this.email = email;
        teammates = new HashSet();
    }

    public Set<Person> getTeammates() {
        return teammates;
    }

    @Override
    public String toString() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (!email.equals(person.email)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
