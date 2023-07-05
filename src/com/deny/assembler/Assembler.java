package com.deny.assembler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.ArrayIndexOutOfBoundsException;

// Runs assembler with arguments from CLI
public class Assembler {

    public static void main(String[] args) {

        try {

            HashMap<String, FileHandler> files = Assembler._initalizeFiles(args);
            Assembler._runAssembler(files);

        } catch (ArrayIndexOutOfBoundsException e) {

            System.out.println(
                    "ERROR: Missing operands.\n"
                            + "Usage:java Compiler.Assembler.Assemble "
                            + "[Assembly File Path] [Output File Name]");
            System.exit(1);

        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Assembly file not found.");
            System.exit(1);
        }
    }

    private static HashMap<String, FileHandler> _initalizeFiles(String[] args)
            throws ArrayIndexOutOfBoundsException, FileNotFoundException {
        if (args.length < 2) {
            throw new ArrayIndexOutOfBoundsException(1);
        }

        FileHandler assemblyFile = new FileHandler(args[0]);
        FileHandler outputFile = new FileHandler(args[1]);

        if (!assemblyFile.exists()) {
            throw new FileNotFoundException();
        }

        HashMap<String, FileHandler> files = new HashMap<>();
        files.put("assemblyFile", assemblyFile);
        files.put("outputFile", outputFile);

        return files;
    }

    private static void _runAssembler(HashMap<String, FileHandler> files) {
        try {
            Assembler._assemble(files);
            System.out.println("Assembled sucessfully");
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Unable to read file.");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("ERROR: Unable to write to file.");
            System.exit(1);
        }
    }

    private static void _assemble(HashMap<String, FileHandler> files) throws FileNotFoundException, IOException {
        FileHandler assemblyFile = files.get("assemblyFile");
        FileHandler outputFile = files.get("outputFile");
        FileHandler.Formatter assemblyFFormatter = assemblyFile.new Formatter();
        ArrayList<String> formattedCode = assemblyFFormatter.format();
        HashMap<String, Integer> symbolTable = SymbolTable
                .initializeTable(formattedCode);
        outputFile.write(Translator.translate(formattedCode, symbolTable));
    }
}

