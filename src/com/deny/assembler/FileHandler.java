package com.deny.assembler;

import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class FileHandler {

    File fileObj;

    FileHandler(String filePath) {
        File file = new File(filePath);
        this.fileObj = file;
    }

    public boolean exists() {
        return fileObj.exists();
    }

    public void write(ArrayList<String> content) throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(this.fileObj));
        for (String line : content) {
            fileWriter.write(line);
            fileWriter.newLine();
        }
        fileWriter.close();
    }

    public ArrayList<String> read() throws FileNotFoundException {
        ArrayList<String> fileContents = new ArrayList<String>();
        Scanner fileReader = new Scanner(this.fileObj);
        while (fileReader.hasNextLine()) {
            String line = fileReader.nextLine();
            fileContents.add(line);
        }
        fileReader.close();
        return fileContents;
    }

    public class Formatter {

        public ArrayList<String> Format() throws FileNotFoundException {
            ArrayList<String> fileContents = FileHandler.this.read();
            ArrayList<String> formattedContents = new ArrayList<String>();

            for (int i = 0; i < fileContents.size(); i++) {
                String line = fileContents.get(i);
                if (!line.isEmpty()) {
                    if (line.charAt(0) == '/') {
                        continue;
                    } else if (line.contains("/")) {
                        int commentIndex = line.indexOf("/");
                        if (commentIndex != -1) {
                            String WithoutWS = fileContents.get(i).substring(0, commentIndex).replace(" ", "");
                            formattedContents.add(WithoutWS);
                        }
                    } else {
                        String WithoutWS = fileContents.get(i).replace(" ", "");
                        formattedContents.add(WithoutWS);
                    }
                }
            }
            System.out.println(formattedContents);
            return formattedContents;
        }
    }

}
