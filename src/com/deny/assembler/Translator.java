package com.deny.assembler;

import java.util.ArrayList;
import java.util.HashMap;

public class Translator {

    private static ArrayList<String> translatedContent = new ArrayList<String>();

    public static ArrayList<String> translate(ArrayList<String> fileContent, HashMap<String, Integer> symbolTable) {
        return Parse(fileContent, symbolTable);
    }

    static ArrayList<String> Parse(ArrayList<String> fileContent, HashMap<String, Integer> symbolTable) {
        for (int i = 0; i < fileContent.size(); i++) {
            String line = fileContent.get(i);
            if (line.charAt(0) == '@') {
                aInstruction(line, symbolTable);
            } else if (line.charAt(0) == '(') {
                continue;
            } else {
                cInstruction(line);
            }
        }
        return translatedContent;
    }

    static void aInstruction(String instruction, HashMap<String, Integer> SymbolTable) {
        if (Character.isDigit(instruction.charAt(1))) {
            Integer address = Integer.parseInt(instruction.substring(1));
            String binaryAddress = String.format("%16s", Integer.toBinaryString(address)).replace(' ', '0');
            translatedContent.add(binaryAddress);
        } else {
            try {
                Integer address = SymbolTable.get(instruction.substring(1));
                String binaryAddress = String.format("%16s", Integer.toBinaryString(address)).replace(' ', '0');
                translatedContent.add(binaryAddress);
            } catch (Throwable t) {
                System.out.println("Error translating instruction:" + instruction);
                System.exit(1);
            }
        }
    }

    // cInstruction(instruction: line sent from parser)
    // appends binary string to translated content
    static void cInstruction(String instruction) {
        if (instruction.contains("=") && instruction.contains(";")) {
            String[] assign = instruction.split("=");
            String[] branching = assign[1].split(";");

            translatedContent
                    .add("111" + destTranslator(assign[0]) + compTranslator(branching[0])
                            + jumpTranslator(branching[1]));
        } else if (instruction.contains("=")) {
            String[] assign = instruction.split("=");

            translatedContent
                    .add("111" + compTranslator(assign[1]) + destTranslator(assign[0]) + jumpTranslator("null"));

        } else {
            String[] branching = instruction.split(";");
            translatedContent
                    .add("111" + compTranslator(branching[0]) + destTranslator("null") + jumpTranslator(branching[1]));
        }
    }

    // compTranslator(comp: computation part sent from cInstruction())
    // returns corresponding binary translation
    private static String compTranslator(String comp) {
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

    // destTranslator(dest: destination part sent from cInstruction())
    // returns corresponding binary translation
    private static String destTranslator(String dest) {
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

    // jumpTranslator(jmp: jump part sent from cInstruction())
    // returns corresponding binary translation
    private static String jumpTranslator(String jmp) {
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
