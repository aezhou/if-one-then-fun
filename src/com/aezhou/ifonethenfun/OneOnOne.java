package com.aezhou.ifonethenfun;

/**
 * Class representing the match itself.
 *
 * isRandom is a flag that is true when the match is made "at random" - a person gets a random match when either he/she
 *   has been matched with all of his possible teammates or if all of his/her possible teammates have already been
 *   matched with in this round of matching.
 */
public class OneOnOne {
    public final Person x;
    public final Person y;
    boolean isRandom;

    public OneOnOne(Person x, Person y) {
        this.x = x;
        this.y = y;
        this.isRandom = false;
    }

    public OneOnOne(Person x, Person y, boolean isRandom) {
        this.x = x;
        this.y = y;
        this.isRandom = true;
    }

    public OneOnOne(Person x) {
        this.x = x;
        this.y = null;
        this.isRandom = false;
    }

    @Override
    public String toString(){
        if (y == null) {
            return "unmatched: " + x.toString();
        }
        return String.format("%s <-> %s", x.toString(), y.toString()) + (isRandom ? " for fun :)" : " for team bonding");
    }
}