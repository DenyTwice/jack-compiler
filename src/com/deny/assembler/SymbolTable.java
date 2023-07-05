package com.deny.assembler;

import java.util.HashMap;
import java.util.ArrayList;

// Handles creation of symbol table for assembly file
public class SymbolTable {

    public static HashMap<String, Integer> initializeTable(ArrayList<String> fileContent) {
        HashMap<String, Integer> symbolTable = new HashMap<>();
        int labelCount = 0;
        int variableCount = 16;

        for (Integer i = 0; i < 16; i++) {
            symbolTable.put("R" + i.toString(), i);
        }

        symbolTable.put("SCREEN", 16384);
        symbolTable.put("KBD", 24576);
        symbolTable.put("SP", 0);
        symbolTable.put("LCL", 1);
        symbolTable.put("ARG", 2);
        symbolTable.put("THIS", 3);
        symbolTable.put("THAT", 4);

        for (int i = 0; i < fileContent.size(); i++) {
            String line = fileContent.get(i);
            if (!line.isEmpty()) {
                if (line.charAt(0) == '(') {
                    symbolTable
                            .put(line.substring(1, line.indexOf(')')), i - labelCount);
                    labelCount++;
                }
            }
        }

        for (int i = 0; i < fileContent.size(); i++) {
            String line = fileContent.get(i);
            if (line.charAt(0) == '@') {
                if (!symbolTable.containsKey(line.substring(1))
                        && Character.isLetter(line.charAt(1))) {

                    symbolTable.put(line.substring(1), variableCount);
                    variableCount++;

                }
            }

        }

        return symbolTable;
    }
}
