package com.deny.assembler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Assembler {

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println(
                    "ERROR: Missing operands.\n"
                            + "Usage:java Compiler.Assembler.Assemble [Assembly File Path] [Output File Name]");
            System.exit(1);
        }

        FileHandler assemblyFile = new FileHandler(args[0]);
        FileHandler outputFile = new FileHandler(args[1]);

        if (!assemblyFile.exists()) {
            System.out.println("ERROR: Assembly file not found.");
            System.exit(1);
        }

        FileHandler.Formatter assemblyFFormatter = assemblyFile.new Formatter();
        try {
            ArrayList<String> formattedCode = assemblyFFormatter.format();
            HashMap<String, Integer> SymblTabl = SymbolTable.initializeTable(formattedCode);
            outputFile.write(Translator.translate(formattedCode, SymblTabl));
            System.out.println("Assembled sucessfully");
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Unable to read file.");
        } catch (IOException e) {
            System.out.println("ERROR: Unable to write to file.");
        }
    }
}
