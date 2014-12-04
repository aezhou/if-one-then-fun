package com.aezhou.ifonethenfun;

import java.util.*;

/**
 * Class that actually calculates the different one on ones.
 *
 * MATCHING_THRESHOLD is a percentage of the company's employees, where X% if the company is "fully matched," we start
 *   the matching process over. (Fully matched means a person has been matched with all possible team mates, across all
 *   teams. Starting the matching process over means that an employee will begin trying to match with team mates again.
 *
 *   stillMatching is the set of employees who still have teammates left to match with
 *   done is the set of employees who have met with all of their teammates
 *   potentialMatches maps an employee to all teammates with whom they have not been matched with yet (across multiple
 *       rounds of matching)
 *   alreadyMatched maps an employee to all teammates with whom they have already been matched with (across multiple
 *       rounds of matching)
 *
 */
public class Matcher {
    private Organization organization;

    private Set<Person> stillMatching;
    private Set<Person> done;
    private Map<Person, Set<Person>> potentialMatches;
    private Map<Person, Set<Person>> alreadyMatched;

    private static final double MATCHING_THRESHOLD = 0.8;

    public Matcher(Organization o) {
        organization = o;
        resetMatchingData();
    }

    /**
     * Creates 1+1 pairing for a single time (week, or whatever the frequency is)
     * @return a set of matchings, or OneOnOne
     */
    public Set<OneOnOne> getNextMatches(){
        // If enough people have been fully matched, then start a new cycle of matches
        if (done.size() >= MATCHING_THRESHOLD * organization.getEmployees().size()) {
            System.out.println(String.format("\nMore than %.2f of employees are done rotating. Resetting matching...\n",
                    MATCHING_THRESHOLD));
            resetMatchingData();
        }

        List<Person> beingMatchedThisRound = new ArrayList<Person>(stillMatching);

        Collections.sort(beingMatchedThisRound, new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return Integer.compare(potentialMatches.get(p1).size(), potentialMatches.get(p2).size());
            }
        });

        Set<Person> matchedInThisRound = new HashSet<Person>();
        Set<OneOnOne> matches = new HashSet<OneOnOne>();

        // Create matchings (if possible) for all members who need still have teammates left to match with
        for(Person p : beingMatchedThisRound) {
            if (!matchedInThisRound.contains(p)) {
                OneOnOne potentialMatch = createMatchWithUnmatchedTeammate(p, matchedInThisRound);
                if (potentialMatch.x != null && potentialMatch.y != null) {
                    matches.add(potentialMatch);
                }
            }
        }

        // From the people who were matched in non-random matches, if someone has no one left to potentially match with,
        //    move the person from stillMatching to done
        for (Person p : matchedInThisRound) {
            if (potentialMatches.get(p).size() == 0) {
                stillMatching.remove(p);
                done.add(p);
            }
        }

        // Randomly match up the remaining people
        Set<Person> unMatchedInThisRound = new HashSet<Person>(organization.getEmployees());
        unMatchedInThisRound.removeAll(matchedInThisRound);
        createRandomMatches(unMatchedInThisRound, matches);

        return matches;
    }

    /**
     * Creates random matches within a set of people - will make as many matches as possible. If the set contains an
     * number of people, one person will not have a matching.
     *
     * @param randomPool set of employees (Person)
     * @param matches collection of matches - any match made will be added to this
     */
    private void createRandomMatches(Set<Person> randomPool, Collection<OneOnOne> matches) {
        while (randomPool.size() > 1) {
            Person one = getRandomElement(randomPool);
            randomPool.remove(one);

            Person two = getRandomElement(randomPool);
            randomPool.remove(two);

            matches.add(new OneOnOne(one, two, true));
        }

        if (randomPool.size() == 1) {
            matches.add(new OneOnOne(getRandomElement(randomPool)));
        }
    }

    /**
     * Resets global variables back to initial conditions (conditions of the company before any pairings have been
     * assigned).
     */
    public void resetMatchingData() {
        stillMatching = new HashSet<Person>(organization.getEmployees());
        done = new HashSet<Person>();

        alreadyMatched = new HashMap<Person, Set<Person>>();
        potentialMatches = new HashMap<Person, Set<Person>>();

        for(Person p : organization.getEmployees()) {
            Set<Person> potentialMatchesSet = new HashSet<>();
            potentialMatchesSet.addAll(p.getTeammates());
            potentialMatches.put(p, potentialMatchesSet);
            alreadyMatched.put(p, new HashSet<Person>());
        }
    }

    /**
     * Gets a random person from a set of Person
     * @param s set of Person
     * @return a random Person from the set
     */
    public Person getRandomElement(Set<Person> s) {
        int index = new Random().nextInt(s.size());
        Iterator<Person> iter = s.iterator();
        for (int i = 0; i < index; i++) {
            iter.next();
        }
        return iter.next();
    }

    /**
     * Creates a match between a Person and a teammate (who has not been assigned a match yet in this round). If no
     * such match is possible, the Person is matched with null. If a successful matching has occurred, update the set
     * of all people who have been matched in this round and the global matching tables (potentialMatches and
     * alreadyMatched).
     *
     * @param gettingMatched Person getting matched
     * @param matched set of people who have been matched already in this round
     * @return a matching (OneOnOne)
     */
    public OneOnOne createMatchWithUnmatchedTeammate(Person gettingMatched, Set<Person> matched) {
        Set<Person> possibleMatches = getPossibleMatches(gettingMatched, matched);
        Person matchedWith = possibleMatches.size() > 0 ? getRandomElement(possibleMatches) : null;

        OneOnOne match = new OneOnOne(gettingMatched, matchedWith);

        if (matchedWith != null) {
            matched.add(gettingMatched);
            matched.add(matchedWith);

            updateMatchingTables(match);
        }

        return match;
    }

    /**
     * Updates the global matching tables (potentialMatches and alreadyMatched) for a given matching
     * @param o the matching (OneOnOne)
     */
    private void updateMatchingTables(OneOnOne o) {
        Person p1 = o.x;
        Person p2 = o.y;

        potentialMatches.get(p1).remove(p2);
        potentialMatches.get(p2).remove(p1);

        alreadyMatched.get(p1).add(p2);
        alreadyMatched.get(p2).add(p1);
    }

    /**
     * Gets possible matches for a Person, given the people who have already been matched. A possible matching is
     * defined to be any possible teammate with whom the Person has not matched with yet and someone who has not
     * already been assigned a matching fo r this round.
     *
     * @param gettingMatched
     * @param matched
     * @return
     */
    private Set<Person> getPossibleMatches(Person gettingMatched, Set<Person> matched) {
        Set<Person> possible = new HashSet<Person>(potentialMatches.get(gettingMatched));
        possible.removeAll(matched);
        return possible;
    }
}
