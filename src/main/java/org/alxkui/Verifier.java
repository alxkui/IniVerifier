package org.alxkui;

import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.IOException;
import java.util.*;

public class Verifier {
    private String filename;
    public Verifier(String filename) {
        this.filename = filename;
    }

    public void verify() throws IOException {
        Loader loader = new Loader(filename);
        Ini ini = loader.loadIni();
        Collection sections = ini.keySet();
        for(Object section : sections) {
            verifyVariablesExist(ini, section.toString());
        }
    }

    public void verifyVariablesExist(Ini ini, String profile) {
        Profile.Section section = ini.get(profile);
        Collection fields = section.keySet();
        for (Object field : fields) {
            String fetched = section.fetch(field);
            if(fetched.contains("$")) {
                System.out.println(profile + " -> " + field + ": variable not defined");
            }
        }
    }
}
