package com.myapp.ecosystem;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;


/**
 * The “C” in MVC.  Owns the Scanner, delegates all I/O to {@link View},
 * and coordinates a single-step workflow:
 *
 *  1. ask the user for the path to an Excel file
 *  2. load the file into a List&lt;Organism&gt;
 *  3. print how many organisms were read (plus the first few as a preview)
 *  4. exit
 *
 * Keeps everything deliberately simple so you can extend later (add
 * commands, loops, habitat selection, solver trigger, etc.).
 */
public final class Controller {

    /** Entry point used by Main (or tests). */
    public void run() {

        /* Controller owns the Scanner and closes it exactly once. */
        try (Scanner in = new Scanner(System.in)) {

            // ── 1  prompt for file path ──────────────────────────────────
            String pathStr = View.getUserInput("Path to .xlsx file: ", in);
            Path   file    = Paths.get(pathStr.trim());

            // ── 2  load organisms ───────────────────────────────────────
            List<Organism> tabulatedData;
            try {
                tabulatedData = ExcelReader.load(file);
            } catch (Exception e) {
                View.printLine("❌  Error: " + e.getMessage());
                return;
            }

            // ── 3  present a quick summary to the user ──────────────────
            View.printLine("✅  Loaded " + tabulatedData.size() + " organisms.");
            View.printOrganismTable(tabulatedData);
        }
    }
}