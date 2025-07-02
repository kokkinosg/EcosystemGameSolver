package com.myapp.ecosystem;
import java.util.List;
import java.util.Scanner;

/**
 * CLI-only view helper: printing and prompting.
 * <p>
 * The controller owns the Scanner (creation + close) and
 * passes it in whenever user input is required.
 */

public class View {

    // Method to print a line
    public static void printLine(String line){
        System.out.println(line);
    }

    public static void printTable(List<String> table){
        // Iterate and print each row from the table
        for(String singleRow : table){
            printLine(singleRow);
        }
    }

    // Method to get the user input into a string
    public static String getUserInput(String prompt, Scanner scanner) {
        System.out.print(prompt);                  // show message (if any)
        return scanner.nextLine().trim(); // read a whole line and remove leading, trailing spaces
    }

}
