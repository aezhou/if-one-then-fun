package com.aezhou.ifonethenfun;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class for the organization
 */
public class Organization {

    private Set<Person> employees;
    private Map<String, Set<Person>> teams;

    public Organization(Map<String, Set<Person>> teams) {
        this.teams = teams;

        employees = new HashSet<Person>();
        for(Set<Person> team : teams.values()){
            employees.addAll(team);
        }

        assignTeammates();

    }

    public Set<Person> getEmployees() {
        return employees;
    }

    public void assignTeammates() {
        for (Set<Person> team: teams.values()) {
            for (Person p : team) {
                p.teammates.addAll(team);
                p.teammates.remove(p);
            }
        }
    }
}
