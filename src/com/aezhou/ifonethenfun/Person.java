package com.aezhou.ifonethenfun;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class representing the employee.
 *
 * Each employee has a unique identifying email and a set of all their teammates.
 */
public class Person {
    public final String email;
    public final Set<Person> teammates;
    public final Map<Person, String> teams;

    public Person(String email) {
        this.email = email;
        teammates = new HashSet();
        teams = new HashMap();
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
