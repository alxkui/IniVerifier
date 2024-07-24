package org.alxkui;

import org.ini4j.Ini;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;

public class Loader {
    private String fileName;
    private final String tempFile = "./temp.ini";
    public Loader(String fileName) throws IOException {
        this.fileName = fileName;
    }

    public void copyContentsFromMainIni() throws IOException {
        FileChannel src = new FileInputStream(this.fileName).getChannel();
        FileChannel dest = new FileOutputStream(this.tempFile).getChannel();
        dest.transferFrom(src, 0, src.size());
    }

    public List<String> gatherRequires() throws IOException {
        List<String> requires = new ArrayList<>();
        File file = new File(this.fileName);
        Scanner reader = new Scanner(file);
        while(reader.hasNext("#require")) {
            String data = reader.nextLine();
            String path = data.split(" ")[1];
            requires.add(path);
        }
        reader.close();
        return requires;
    }

    public void replaceAndWrite(String replaceLine, String replaceString) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(this.tempFile));
        StringBuffer inputBuffer = new StringBuffer();
        String line;

        inputBuffer.append("[default]\n");

        while((line = bufferedReader.readLine()) != null) {
            if(!line.equalsIgnoreCase("[default]")) {
                inputBuffer.append(line);
                inputBuffer.append("\n");
            }
        }
        bufferedReader.close();

        String inputString = inputBuffer.toString();

        if(inputString.contains(replaceString)) {
            inputString = inputString.replace(replaceString, replaceLine);
        }

        FileOutputStream fileOut = new FileOutputStream(this.tempFile);
        fileOut.write(inputString.getBytes());
        fileOut.close();
    }

    public void loadRequiredAndAppend(String require) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(require));
        StringBuffer inputBuffer = new StringBuffer();
        String line;

        while((line = file.readLine()) != null) {
            if(!line.equalsIgnoreCase("[default]")) {
                inputBuffer.append(line);
                inputBuffer.append("\n");
            }
        }
        file.close();

        replaceAndWrite(inputBuffer.toString(), "#require " + require);
    }

    public Ini loadIni() throws IOException {
        copyContentsFromMainIni();

        List<String> requires = gatherRequires();

        if(!requires.isEmpty()) {
            for(String requirePath : requires) {
                System.out.println(requirePath);
                loadRequiredAndAppend(requirePath);
            }

            return new Ini(new File(this.tempFile));
        }

        return new Ini(new File(this.fileName));
    }
}
