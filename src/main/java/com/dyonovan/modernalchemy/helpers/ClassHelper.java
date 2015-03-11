package com.dyonovan.modernalchemy.helpers;

import com.dyonovan.teambrcore.utils.FileUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

public class ClassHelper {
    public static Collection<Class<?>> getClassesInJar(URL url) {
        Collection<Class<?>> classes = new ArrayList<>();
        ArrayList<String> fileNames = FileUtils.getJarDirList(url);

        for(String name : fileNames) {
            if(name.endsWith(".class")) {
                String className = "com.dyonovan.modernalchemy." + name.replace('/', '.').substring(0, name.length() - 6);
                if(className.contains("$1"))
                    continue;
                try {
                    Class<?> c = Class.forName(className);
                    classes.add(c);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        return classes;
    }
}
