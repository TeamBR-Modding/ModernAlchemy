package com.dyonovan.modernalchemy.collections;

public class Couple<A, B> {
    private A a;
    private B b;

    public Couple(A typeA, B typeB) {
        a = typeA;
        b = typeB;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    public void setA(A typeA) {
        a = typeA;
    }

    public void setB(B typeB) {
        b = typeB;
    }

    @Override
    public boolean equals(Object obj) {
        return a.equals(((Couple)obj).a) && b.equals(((Couple)obj).b);
    }

    @Override
    public String toString() {
        return "Class A = " + a.getClass() + " toString: " + a.toString() + "\nClass B = " + b.getClass() + " toString: " + b.toString();
    }
}
