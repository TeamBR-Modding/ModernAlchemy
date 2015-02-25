package com.dyonovan.modernalchemy.manual.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManualPages {
    public static ManualPages instance = new ManualPages();

    public static HashMap<String, BasePage> pages;

    public ManualPages() {
        pages = new HashMap<String, BasePage>();
    }

    public void init() {
        pages.put("Main Page", new MainPage());
    }

    public void addPage(BasePage page) {
        pages.put(page.getID(), page);
    }

    public BasePage getPage(String id) {
        return pages.get(id);
    }
}
