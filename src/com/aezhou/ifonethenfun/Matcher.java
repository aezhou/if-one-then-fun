package com.aezhou.ifonethenfun;

import java.util.*;

/**
 * Class for the Matcher
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

    public Set<OneOnOne> getNextMatches(){
        // If enough people have been fully matched, then start a new cycle of matches
        if (done.size() >= MATCHING_THRESHOLD * organization.getEmployees().size()) {
            System.out.println(String.format("\nMore than %.2f of employees are done rotating. Resetting matching...\n", MATCHING_THRESHOLD));
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

        for(Person p : beingMatchedThisRound) {
            if (!matchedInThisRound.contains(p)) {
                OneOnOne potentialMatch = createMatchWithUnmatchedTeammate(p, matchedInThisRound);
                if (potentialMatch.x != null && potentialMatch.y != null) {
                    matches.add(potentialMatch);
                }
            }
        }

        // Randomly match up the remaining people
        Set<Person> unMatchedInThisRound = new HashSet<Person>(organization.getEmployees());
        unMatchedInThisRound.removeAll(matchedInThisRound);
        createRandomMatches(unMatchedInThisRound, matches);

        // With this set of matches, if people have no potential matches left, move from stillMatching to done
        for (Person p : matchedInThisRound) {
            if (potentialMatches.get(p).size() == 0) {
                stillMatching.remove(p);
                done.add(p);
            }
        }

        return matches;
    }

    private void createRandomMatches(Set<Person> randomPool,
                                     Collection<OneOnOne> matches) {
        while (randomPool.size() > 1) {
            Person one = getRandomElement(randomPool);
            randomPool.remove(one);

            Person two = getRandomElement(randomPool);
            randomPool.remove(two);

            matches.add(new OneOnOne(one, two, true));
        }
    }

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
        System.out.println("Matching data reset");
    }

    public Person getRandomElement(Set<Person> s) {
        int index = new Random().nextInt(s.size());
        Iterator<Person> iter = s.iterator();
        for (int i = 0; i < index; i++) {
            iter.next();
        }
        return iter.next();
    }

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

    private void updateMatchingTables(OneOnOne o) {
        Person p1 = o.x;
        Person p2 = o.y;

        potentialMatches.get(p1).remove(p2);
        potentialMatches.get(p2).remove(p1);

        alreadyMatched.get(p1).add(p2);
        alreadyMatched.get(p2).add(p1);
    }

    private Set<Person> getPossibleMatches(Person gettingMatched, Set<Person> matched) {
        Set<Person> possible = new HashSet<Person>(potentialMatches.get(gettingMatched));
        possible.removeAll(matched);
        return possible;
    }
}
