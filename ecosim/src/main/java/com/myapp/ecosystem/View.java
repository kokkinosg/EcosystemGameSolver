package com.myapp.ecosystem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    // Method to print all names of the organisms 
    public static void printAllNames(List<Organism> organismList){

        System.out.println("---------------------");
        System.out.println("These are the names of the organisms in the data:");
        System.out.println("");
        for(Organism organism : organismList){
            System.out.println(organism.getName());
        }
    }

    /* ------------------------------------------------------------------
     * Pretty-print a list of Organism objects as an aligned table
     * showing every numeric and text field.
     * ------------------------------------------------------------------ */
    public static void printOrganismTable(List<Organism> rows) {

        if (rows == null || rows.isEmpty()) {
            System.out.println("(no organisms)");
            return;
        }

        /* 1 ── column headers (11 of them) */
        String[] hdr = { "name", "type",
                         "calNeed", "calGive",
                         "calRemainGive", "calRemainNeed",
                         "eats", "eatenBy", "is hungry?",
                         "cond1", "cond2", "cond3", "cond4" };

        /* 2 ── collect each organism into a String[] row */
        List<String[]> data = new ArrayList<>();
        for (Organism o : rows) {
            data.add(new String[] {
                o.getName(),
                o.getType(),
                String.valueOf(o.getCalNeed()),
                String.valueOf(o.getCalGive()),
                String.valueOf(o.getCalRemainGive()),
                String.valueOf(o.getCalRemainNeed()),
                String.join(", ", o.getEats()),
                String.join(", ", o.getEatenBy()),
                String.valueOf(o.getIsHungry()),
                o.getCond1(),
                o.getCond2(),
                o.getCond3(),
                o.getCond4()
            });
        }

        /* 3 ── compute max width for each column */
        int colCount = hdr.length;
        int[] w = new int[colCount];
        for (int c = 0; c < colCount; c++) {
            w[c] = hdr[c].length();
            for (String[] r : data)
                w[c] = Math.max(w[c], r[c].length());
        }

        /* 4 ── helper that prints one row with padding */
        Consumer<String[]> print = row -> {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < colCount; c++) {
                sb.append(String.format("%-" + w[c] + "s", row[c]));
                if (c < colCount - 1) sb.append(" | ");
            }
            System.out.println(sb);
        };

        /* 5 ── header, separator, then data */
        print.accept(hdr);

        String sep = Arrays.stream(w)
                           .mapToObj(len -> "-".repeat(len))
                           .collect(Collectors.joining("-+-"));
        System.out.println(sep);

        data.forEach(print);
    }

    // Method to get the user input into a string
    public static String getUserInput(String prompt, Scanner scanner) {
        System.out.print(prompt);                  // show message (if any)
        return scanner.nextLine().trim(); // read a whole line and remove leading, trailing spaces
    }

}
