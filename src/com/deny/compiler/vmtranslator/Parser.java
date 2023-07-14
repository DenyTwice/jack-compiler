package com.deny.compiler.vmtranslator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import com.deny.compiler.FileHandler;

public class Parser {

    String filePath;
    ArrayList<String> translatedContent = new ArrayList<String>();

    public Parser(String filePath) {
        this.filePath = filePath;
    }

    ArrayList<String> parse() {
        FileHandler file = new FileHandler(filePath);
        try {
            ArrayList<String> fileContent = file.read();
            for (int i = 0; i < fileContent.size(); i++) {
                String line = fileContent.get(i);
                if (line.isEmpty()) {
                    continue;
                } else if (line.charAt(0) == '/') {
                    continue;
                }
                String translated;
                if (line.charAt(0) == 'p') {
                    if (line.charAt(1) == 'u') {
                        translated = _pushInstruction(line);
                    } else {
                        translated = _popInstruction(line);
                    }
                } else if (line.toUpperCase().charAt(0) == 'L') {
                    translated = _lblInstruction(line);
                } else if (line.charAt(0) == 'g') {
                    translated = _jmpInstruction(line);
                } else {
                    continue;
                }

                this.translatedContent.add(translated);

            }
        } catch (FileNotFoundException e) {
            System.out.println("yikes");
        }

        return this.translatedContent;
    }

    // push memseg i
    private String _pushInstruction(String line) {
        String translatedCode;
        String[] tokens = line.split(" ");
        String segment = tokens[1];
        String index = tokens[2];

        switch (segment) {
            case "local":
                translatedCode = "@LCL\nD=M\n@" + index + "\n"
                        + "D=M+A\nA=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
                break;
            case "this":
                translatedCode = "@this\nD=M\n@" + index + "\n"
                        + "D=M+A\nA=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
                break;
            case "that":
                translatedCode = "@that\nD=M\n@" + index + "\n"
                        + "D=M+A\nA=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
                break;
            case "pointer":
                if (index == "1") {
                    translatedCode = "@R3\nA=M\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
                } else {
                    translatedCode = "@R4\nA=M\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
                }
                break;
            case "constant":
                translatedCode = "@" + index + "\n"
                        + "D=A\n@SP\nA=M\nM=D\n@SP\nM=M+1";
                break;
            case "temp":
                translatedCode = "@R5\nD=M\n@" + index + "\n"
                        + "D=M+A\nA=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
                break;
            case "static":
                translatedCode = "@16\nD=M\n@" + index + "\n"
                        + "D=M+A\nA=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
                break;
            case "argument":
                translatedCode = "@ARG\nD=M\n@" + index + "\n"
                        + "D=M+A\nA=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
                break;
            default:
                translatedCode = "";
                break;
        }
        return translatedCode;
    }

    private String _popInstruction(String line) {
        String translatedCode;
        String[] tokens = line.split(" ");
        String segment = tokens[1];
        String index = tokens[2];

        switch (segment) {
            case "local":
                translatedCode = "@" + index
                        + "\nD=A\n@LCL\nD=D+M\n@addr\nM=D\n@SP\nA=M\nD=M\n@SP\nM=M-1\n@addr\nA=M\nM=D";
                break;
            case "this":
                translatedCode = "@" + index
                        + "\nD=A\n@THIS\nD=D+M\n@addr\nM=D\n@SP\nA=M\nD=M\n@SP\nM=M-1\n@addr\nA=M\nM=D";
                break;
            case "that":
                translatedCode = "@" + index
                        + "\nD=A\n@LCL\nD=D+M\n@addr\nM=D\n@SP\nA=M\nD=M\n@SP\nM=M-1\n@addr\nA=M\nM=D";
                break;
            case "pointer":
                translatedCode = "@" + index
                        + "\nD=A\n@LCL\nD=D+M\n@addr\nM=D\n@SP\nA=M\nD=M\n@SP\nM=M-1\n@addr\nA=M\nM=D";
                break;
            case "temp":
                translatedCode = "@" + index
                        + "\nD=A\n@R5\nD=D+M\n@addr\nM=D\n@SP\nA=M\nD=M\n@SP\nM=M-1\n@addr\nA=M\nM=D";
                break;
            case "static":
                translatedCode = "@" + index
                        + "\nD=A\n@16\nD=D+M\n@addr\nM=D\n@SP\nA=M\nD=M\n@SP\nM=M-1\n@addr\nA=M\nM=D";
                break;
            case "argument":
                translatedCode = "@" + index
                        + "\nD=A\n@ARG\nD=D+M\n@addr\nM=D\n@SP\nA=M\nD=M\n@SP\nM=M-1\n@addr\nA=M\nM=D";
                break;
            default:
                translatedCode = "";
                break;
        }
        return translatedCode;
    }

    private String _lblInstruction(String line) {
        String translatedCode;
        String label = line.substring(6);
        translatedCode = "(" + label + ")";
        return translatedCode;
    }

    private String _jmpInstruction(String line) {
        String translatedCode;
        translatedCode = "0;JMP\n";
        return translatedCode;
    }

}
