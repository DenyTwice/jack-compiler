package com.deny.assembler;

import java.util.HashMap;
import java.util.ArrayList;

public class SymbolTable {

    static HashMap<String, Integer> initializeTable(ArrayList<String> fileContent) {
        HashMap<String, Integer> SymbolTable = new HashMap<>();
        int labelCount = 0;
        int variableCount = 16;

        for (Integer i = 0; i < 16; i++) {
            SymbolTable.put("R" + i.toString(), i);
        }

        SymbolTable.put("SCREEN", 16384);
        SymbolTable.put("KBD", 24576);
        SymbolTable.put("SP", 0);
        SymbolTable.put("LCL", 1);
        SymbolTable.put("ARG", 2);
        SymbolTable.put("THIS", 3);
        SymbolTable.put("THAT", 4);

        for (int i = 0; i < fileContent.size(); i++) {
            String line = fileContent.get(i);
            if (!line.isEmpty()) {
                if (line.charAt(0) == '(') {
                    SymbolTable.put(line.substring(1, line.indexOf(')')), i - labelCount);
                    labelCount++;
                }
            }
        }

        for (int i = 0; i < fileContent.size(); i++) {
            String line = fileContent.get(i);
            if (line.charAt(0) == '@') {
                if (!SymbolTable.containsKey(line.substring(1)) && Character.isLetter(line.charAt(1))) {
                    SymbolTable.put(line.substring(1), variableCount);
                    variableCount++;
                }
            }

        }

        return SymbolTable;
    }
}
