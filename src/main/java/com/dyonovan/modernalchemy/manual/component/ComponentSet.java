package com.dyonovan.modernalchemy.manual.component;

import java.util.ArrayList;
import java.util.List;

public class ComponentSet {
    public List<IComponent> components =  new ArrayList<IComponent>();

    public void add(IComponent comp) {
        components.add(comp);
    }

    public IComponent get(int index) {
        return components.get(index);
    }

    public int size() {
      return components.size();
    }
}
