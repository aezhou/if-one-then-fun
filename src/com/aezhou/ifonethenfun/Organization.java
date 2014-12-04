package com.aezhou.ifonethenfun;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class representing the organization - keeps track of all employees, and the different team compositions
 */
public class Organization {

    private Set<Person> employees;
    private Map<String, Set<Person>> teams;

    /**
     * Constructor for organization
     * @param teams maps team name (String) to set of employees in the team (Person)
     *
     * Also compiles a full list of all distinct employees in the organization - saved as global set "employees"
     */
    public Organization(Map<String, Set<Person>> teams) {
        this.teams = teams;

        employees = new HashSet<>();
        for(Set<Person> team : teams.values()){
            employees.addAll(team);
        }

        assignTeammates();

    }

    public Set<Person> getEmployees() {
        return employees;
    }

    /**
     * Iterating through the team assignments, adds new teammates to each person's list of teammates
     */
    public void assignTeammates() {
        for (String teamName: teams.keySet()) {
            Set<Person> team = teams.get(teamName);
            for (Person p : team) {
                p.teammates.addAll(team);
                for (Person x : team) {

                    if (!x.equals(p)) {
                       String setOfTeams = "";
                        if (p.teams.containsKey(x)) {
                            setOfTeams = p.teams.get(x) + "/";
                        }
                        p.teams.put(x, setOfTeams + teamName);
                    }
                }
                p.teammates.remove(p);
            }
        }
    }
}
