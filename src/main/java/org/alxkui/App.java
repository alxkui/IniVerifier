package org.alxkui;

import java.io.IOException;

public class App 
{
    public static void main( String[] args ) throws IOException {
        String file = "/Users/sandev/Documents/dev/cashemutest/src/main/resources/example.ini";
        Verifier verifier = new Verifier(file);
        verifier.verify();
    }
}
