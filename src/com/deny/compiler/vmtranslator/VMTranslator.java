package com.deny.compiler.vmtranslator;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.deny.compiler.FileHandler;

import java.util.ArrayList;

public class VMTranslator {
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Error: Too many arguments.");
            System.exit(0);
        }
        // directory
        if (args[0].endsWith("/")) {
            Set<String> filesList = VMTranslator.listFilesUsingJavaIO(args[0]);
            Parser[] parserObjArray = new Parser[filesList.size()];
            for (int i = 0; i < filesList.size(); i++) {
                for (String file : filesList) {
                    System.out.println(file);
                    parserObjArray[i] = new Parser(args[0] + file);
                    ArrayList<String> translated = parserObjArray[i].parse();
                    try {
                        FileHandler outputFile = new FileHandler(args[1]);
                        outputFile.write(translated);
                    } catch (Throwable t) {
                        System.out.println("ERROR: Cannot write to output file.");
                    }
                }
            }
        } else { // single file
            Parser parserObj = new Parser(args[0].toString());
            ArrayList<String> translated = parserObj.parse();
            try {
                FileHandler outputFile = new FileHandler(args[1]);
                outputFile.write(translated);
            } catch (Throwable t) {
                System.out.println("ERROR: Cannot write to output file.");
            }
        }

    }

    public static Set<String> listFilesUsingJavaIO(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }
}
