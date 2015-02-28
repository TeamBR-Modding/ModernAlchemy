package com.dyonovan.modernalchemy.manual;

import java.util.ArrayList;

public class ManualJson {

    public String title;
    public int numPages;
    public ArrayList<ManualComponents> component;

    public ManualJson(String title, int numPages, ArrayList<ManualComponents> component) {
        this.title = title;
        this.numPages = numPages;
        this.component = component;
    }
}
