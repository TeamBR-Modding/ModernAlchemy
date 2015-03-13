package com.dyonovan.teambrcore.utils;

import com.dyonovan.teambrcore.helpers.LogHelper;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;

@SuppressWarnings("unused")
public class FileUtils {

    /**
     * Lists files in a specified Jar/Filesystem Directory
     * @param url {@link java.net.URL}
     * //@param dir {@link String} directory name to list
     * @return An {@link java.util.ArrayList} of {@link String}
     */

    public static ArrayList<String> getJarDirList(URL url) {
        ArrayList<String> files = new ArrayList<>();

        if (url.toString().substring(0,3).equalsIgnoreCase("jar")) {
            try {
                String[] urlString = url.toString().split("!");
                JarURLConnection jarCon = (JarURLConnection)new URL(urlString[0] + "!/").openConnection();
                Enumeration<JarEntry> entries = jarCon.getJarFile().entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    files.add(entry.getName().replaceAll("\\\\", File.separator));
                }
            } catch (IOException e) {
                LogHelper.severe("Could not open Jar File");
            }
        } else {
            try {
                File apps = new File(url.toURI());
                Collection<File> list = org.apache.commons.io.FileUtils.listFilesAndDirs(apps, TrueFileFilter.TRUE, TrueFileFilter.TRUE);
                //noinspection ConstantConditions

                for (File app : list) {
                    String[] path = app.getPath().split("main");
                    if (path.length > 1)
                        files.add(path[1].replaceAll("\\\\", File.separator));
                }
            } catch (URISyntaxException e) {
                LogHelper.severe("Could not open Directory");
            }
        }
        return files;
    }
}
