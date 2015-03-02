package com.dyonovan.modernalchemy.manual.util;

import java.util.ArrayList;

public class AbstractManualPage {

    public String title;
    public int numPages;
    public ArrayList<AbstractComponent> component;

    public AbstractManualPage(String title, int numPages, ArrayList<AbstractComponent> component) {
        this.title = title;
        this.numPages = numPages;
        this.component = component;
    }
}
