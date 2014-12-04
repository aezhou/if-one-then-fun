package com.aezhou.ifonethenfun;

/**
 * Class for tuples
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

    @Override
    public String toString(){
        return String.format("%s <-> %s", x.toString(), y.toString()) + (isRandom ? " for fun :)" : " for work :|");
    }
}