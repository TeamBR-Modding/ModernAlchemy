package com.dyonovan.modernalchemy.manual.component;

public class ComponentSpace extends ComponentBase {
    protected int space;

    public ComponentSpace(int s) {
        space = s;
    }

    @Override
    public int getSpace() {
        return space;
    }
}
