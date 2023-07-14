package com.deny.compiler.assembler;

import java.util.ArrayList;
import java.util.HashMap;

// Handles translation of assembly commands to binary
public class Translator {

    private static ArrayList<String> _translatedContent = new ArrayList<String>();

    public static ArrayList<String> translate(ArrayList<String> fileContent, HashMap<String, Integer> symbolTable) {
        _translatedContent.clear();

        return _iterate(fileContent, symbolTable);
    }

    private static ArrayList<String> _iterate(ArrayList<String> fileContent, HashMap<String, Integer> symbolTable) {
        for (int i = 0; i < fileContent.size(); i++) {
            String line = fileContent.get(i);
            if (line.charAt(0) == '@') {
                _aInstruction(line, symbolTable);
            } else if (line.charAt(0) == '(') {
                continue;
            } else {
                _cInstruction(line);
            }
        }

        return _translatedContent;
    }

    private static void _aInstruction(String instruction, HashMap<String, Integer> symbolTable) {
        if (Character.isDigit(instruction.charAt(1))) {

            Integer address = Integer.parseInt(instruction.substring(1));
            String binaryAddress = String.format("%16s", Integer.toBinaryString(address))
                    .replace(' ', '0');
            _translatedContent.add(binaryAddress);

        } else {

            Integer address = symbolTable.get(instruction.substring(1));
            String binaryAddress = String.format("%16s", Integer.toBinaryString(address))
                    .replace(' ', '0');
            _translatedContent.add(binaryAddress);

        }
    }

    private static void _cInstruction(String instruction) {
        if (instruction.contains("=") && instruction.contains(";")) {

            String[] assign = instruction.split("=");
            String[] branching = assign[1].split(";");

            _translatedContent
                    .add("111"
                            + _destTranslator(assign[0])
                            + _compTranslator(branching[0])
                            + _jumpTranslator(branching[1]));

        } else if (instruction.contains("=")) {

            String[] assign = instruction.split("=");

            _translatedContent
                    .add("111"
                            + _compTranslator(assign[1])
                            + _destTranslator(assign[0])
                            + _jumpTranslator("null"));

        } else {

            String[] branching = instruction.split(";");
            _translatedContent
                    .add("111"
                            + _compTranslator(branching[0])
                            + _destTranslator("null")
                            + _jumpTranslator(branching[1]));
        }
    }

    private static String _compTranslator(String comp) {
        HashMap<String, String> compTable = new HashMap<>();

        compTable.put("0", "0101010");
        compTable.put("1", "0111111");
        compTable.put("-1", "0111010");
        compTable.put("D", "0001100");
        compTable.put("A", "0110000");
        compTable.put("!D", "0001101");
        compTable.put("!A", "0110001");
        compTable.put("-D", "0001111");
        compTable.put("-A", "0110011");
        compTable.put("D+1", "0011111");
        compTable.put("A+1", "0110111");
        compTable.put("D-1", "0001110");
        compTable.put("A-1", "0110010");
        compTable.put("D+A", "0000010");
        compTable.put("D-A", "0010011");
        compTable.put("A-D", "0000111");
        compTable.put("D&A", "0000000");
        compTable.put("D|A", "0010101");
        compTable.put("M", "1110000");
        compTable.put("!M", "1110001");
        compTable.put("-M", "1110011");
        compTable.put("M+1", "1110111");
        compTable.put("M-1", "1110010");
        compTable.put("D+M", "1000010");
        compTable.put("D-M", "1010011");
        compTable.put("M-D", "1000111");
        compTable.put("D&M", "1000000");
        compTable.put("D|M", "1010101");

        return compTable.get(comp);
    }

    private static String _destTranslator(String dest) {
        HashMap<String, String> destTable = new HashMap<>();

        destTable.put("null", "000");
        destTable.put("M", "001");
        destTable.put("D", "010");
        destTable.put("MD", "011");
        destTable.put("A", "100");
        destTable.put("AM", "101");
        destTable.put("AD", "110");
        destTable.put("AMD", "111");

        return destTable.get(dest);
    }

    private static String _jumpTranslator(String jmp) {
        HashMap<String, String> jumpTable = new HashMap<>();

        jumpTable.put("null", "000");
        jumpTable.put("JGT", "001");
        jumpTable.put("JEQ", "010");
        jumpTable.put("JGE", "011");
        jumpTable.put("JLT", "100");
        jumpTable.put("JNE", "101");
        jumpTable.put("JLE", "110");
        jumpTable.put("JMP", "111");

        return jumpTable.get(jmp);
    }

}
