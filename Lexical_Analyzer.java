import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

class tokenTable {
    String keyWordsDB[] = { "auto", "break", "case", "char", "const", "continue", "default", "do", "double", "else",
            "enum", "extern", "float", "for", "goto", "if", "int", "long", "register", "return", "short", "signed",
            "sizeof", "static", "struct", "switch", "typedef", "union", "unsigned", "void", "volatile", "while" };
    String mathOperatorsDB[] = { "+", "-", "=", "*", "/", "%", "++", "--" };
    String logicOperatorsDB[] = { ">", "<", ">=", "<=", "==", "!=", "&&", "||", "!" };

    // [0] keyWords
    // [1] identifiers
    // [2] numerals
    // [3] mathOperators
    // [4] logicOperators
    // [5] others
    LinkedList<String> columns[] = new LinkedList[6];

    tokenTable() {
        super();
        for (int i = 0; i < columns.length; i++)
            columns[i] = new LinkedList<String>();
    }

    boolean containsDB(String db[], String entry) {
        for (int i = 0; i < db.length; i++)
            if (db[i].equals(entry))
                return true;
        return false;
    }

    // alphabets(0), numbers(1), aphabets+numbers(2) or somethingElse(-1)
    int typeCheck(String entry) {
        boolean hasAlphabet = false, hasNumber = false;
        for (int i = 0; i < entry.length(); i++) {
            char temp = entry.charAt(i);
            if ((temp >= 65 && temp <= 90) || (temp >= 97 && temp <= 122))
                hasAlphabet = true;
            else if ((temp >= 48 && temp <= 57))
                hasNumber = true;
            else if (temp == '.')
                return 1;
            else
                return -1;
        }

        if (hasAlphabet) {
            if (hasNumber)
                return 2;
            else
                return 0;
        } else
            return 1;
    }

    String add(String entry) {
        if (!entry.isEmpty()) {
            int type = typeCheck(entry);
            if (type == -1) {
                if (containsDB(mathOperatorsDB, entry))
                    addConcise(3, entry); // mathOperators
                else if (containsDB(logicOperatorsDB, entry))
                    addConcise(4, entry); // logicOperators
                else
                    addConcise(5, entry); // others
            } else if (type == 1)
                addConcise(2, entry); // numerals
            else {
                if (type == 0 && containsDB(keyWordsDB, entry))
                    addConcise(0, entry); // keyWords
                else
                    addConcise(1, entry); // identifiers
            }
        }
        return "";
    }

    void addConcise(int columnNumber, String entry) {
        if (!columns[columnNumber].contains(entry))
            columns[columnNumber].add(entry);
    }

    String outputList(int columnNumber) {
        String outputString = "";
        if (columnNumber != 5) {
            for (String entry : columns[columnNumber])
                outputString += (entry + ", ");
            if (!outputString.isEmpty())
                outputString = outputString.substring(0, outputString.length() - 2);
        } else {
            for (String entry : columns[columnNumber])
                outputString += (entry + " ");
            if (!outputString.isEmpty())
                outputString = outputString.substring(0, outputString.length() - 1);
        }
        return outputString;
    }
}

public class Lexical_Analyzer {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader((new File("input.txt"))));
        tokenTable tokens = new tokenTable();
        String tempWord = "";
        int temp;

        while (true) {
            temp = reader.read();

            if (temp == -1) {
                tempWord = tokens.add(tempWord);
                break;
            } else {
                char tempChar = (char) temp;
                if (tempChar == 32 || tempChar == 10 || tempChar == 9) // Space " " or New Line or TAB
                    tempWord = tokens.add(tempWord);
                else if (tempChar == ';' || tempChar == ',' || tempChar == '(' || tempChar == ')' || tempChar == '{'
                        || tempChar == '}' || tempChar == '[' || tempChar == ']') {
                    tempWord = tokens.add(tempWord);
                    tokens.addConcise(5, "" + tempChar); // others
                } else if (tempWord.equals("!") && !(tempChar == '=')) { // NOT detection
                    tokens.addConcise(4, "!"); // logicOperators
                    tempWord = "" + tempChar;
                } else
                    tempWord += tempChar;
            }
        }
        System.out.println("Keywords: " + tokens.outputList(0) + "\nIdentifiers: " + tokens.outputList(1)
                + "\nMath Operators: " + tokens.outputList(3) + "\nLogical Operators: " + tokens.outputList(4)
                + "\nNumerical Values: " + tokens.outputList(2) + "\nOthers: " + tokens.outputList(5));
    }
}

// Keywords in C
// [https://www.programiz.com/c-programming/list-all-keywords-c-language]
// ASCII [https://www.asciitable.com/]
// Arithmetic Operators
// [https://www.tutorialspoint.com/cprogramming/c_arithmetic_operators.htm]
// Relational Operators
// [https://www.educba.com/relational-operators-in-c]
// Logical Operators
// [https://www.tutorialspoint.com/cprogramming/c_logical_operators.htm]
