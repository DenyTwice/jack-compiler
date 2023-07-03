package com.deny.assembler;

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

        try {
            FileHandler.Formatter assemblyFFormatter = assemblyFile.new Formatter();
            ArrayList<String> formattedCode = assemblyFFormatter.format();
            HashMap<String, Integer> SymblTabl = SymbolTable.initializeTable(formattedCode);
            outputFile.write(Translator.translate(formattedCode, SymblTabl));
            System.out.println("Assembled sucessfully");
        } catch (Throwable t) {
            System.out.println("okay");
        }

    }
}
