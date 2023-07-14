package com.deny.compiler;

import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

// File manipulation
public class FileHandler {

    private File _fileObj;
    private ArrayList<String> _fileContent;
    private boolean _isRead = false;

    public FileHandler(String filePath) {
        this._fileObj = new File(filePath);
    }

    public void write(ArrayList<String> content) throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(this._fileObj));

        for (String line : content) {
            fileWriter.write(line);
            fileWriter.newLine();
        }
        fileWriter.close();
    }

    public ArrayList<String> read() throws FileNotFoundException {

        if (!this._isRead) {
            ArrayList<String> fileContent = new ArrayList<String>();
            Scanner fileReader = new Scanner(this._fileObj);

            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                fileContent.add(line);
            }

            fileReader.close();
            this._fileContent = fileContent;
            _isRead = true;
        }

        return this._fileContent;
    }

    public boolean exists() {
        return _fileObj.exists();
    }

    public File getFileObj() {
        return this._fileObj;
    }

    // Handles operations to format code for translation
    public class Formatter {

        public ArrayList<String> format() throws FileNotFoundException {
            ArrayList<String> fileContent = FileHandler.this.read();
            ArrayList<String> formattedContent = new ArrayList<String>();

            // : Improve this process if possible.
            for (int i = 0; i < fileContent.size(); i++) {
                String line = fileContent.get(i);
                if (!line.isEmpty()) {
                    if (line.charAt(0) == '/') {
                        continue;
                    } else if (line.contains("/")) {
                        int commentIndex = line.indexOf("/");
                        if (commentIndex != -1) {
                            String WithoutWS = fileContent.get(i).substring(0, commentIndex).replace(" ", "");
                            formattedContent.add(WithoutWS);
                        }
                    } else {
                        String WithoutWS = fileContent.get(i).replace(" ", "");
                        formattedContent.add(WithoutWS);
                    }
                }
            }

            return formattedContent;
        }
    }

}
