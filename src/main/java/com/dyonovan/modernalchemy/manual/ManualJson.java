package com.dyonovan.modernalchemy.manual;

import java.util.ArrayList;

public class ManualJson {

    public String title, typeComponent;
    public int numPages;
    public ArrayList<ManualComponents> component;

    public ManualJson(String title, int numPages,String typeComponent,  ArrayList<ManualComponents> component) {
        this.title = title;
        this.numPages = numPages;
        this.typeComponent = typeComponent;
        this.component = component;
    }
}
