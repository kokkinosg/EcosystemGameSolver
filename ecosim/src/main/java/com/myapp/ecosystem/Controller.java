package com.myapp.ecosystem;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * The “C” in MVC.  Owns the Scanner, delegates all I/O to View,
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
            // Give a default location 
            View.printLine("This is a suggested path: " + "/Users/george/Downloads/solve.xlsx");
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

            // Print a clean line 
            System.out.println();
            // ── 3  present a quick summary to the user ──────────────────
            View.printLine("✅  Loaded " + tabulatedData.size() + " organisms.");
            View.printOrganismTable(tabulatedData);

            // Print a clean line 
            System.out.println();

            // - 4 Do a quick check to identify which animals are useless in a first iteration
            // for(Organism organism : tabulatedData){
            //     Logic.canPredatorSatisfyCalNeed(organism, tabulatedData);
            // }

            // - 5 Get rid of all useless animal 
            List<Organism> processedList = new ArrayList<>();
            processedList = Logic.eliminateUnusableAnimals(tabulatedData);
            View.printAllNames(processedList);

            // Define a success boolean where when true it means we found a sustainable chain of 8 animals
            boolean isChainSustainable = false;

            // 6. Get all possible combinations of 8 organisms where all producers are always included
            List<List<Organism>> allPossibleCombo = Logic.allEightWayCombos(processedList); 
            System.out.println("There are " + allPossibleCombo.size() + " possible combinations of 8 organisms.");


            int loopCounter = 0;
            outer:
            for (List<Organism> sample : allPossibleCombo) {

                Logic.resetOrganisms(processedList);   // reset master pool. 
                loopCounter++; // Increase the loop counter
                
                System.out.println();
                System.out.println();
                System.out.println(" Testing sample " + loopCounter + " / " + allPossibleCombo.size());

                
                // Count the number of animals in my sample
                int animalCounter = Logic.countAnimals(sample);
                
                // five feeding steps (one per animal) – you *have* five animals by construction
                for (int cycle = 0; cycle < animalCounter; cycle++) {

                    String predatorName = Logic.nameOfAnimalHighestCaloriesProvided(sample);
                    Organism predator   = Logic.getOrganismByName(predatorName, sample);

                    if (predator == null || Logic.isOrganismDead(predator)) {
                        continue outer;                // sample fails, try next combo
                    }

                    List<Organism> prey = Logic.listOfPrey(sample, predatorName);
                    if (prey.isEmpty()) continue outer;

                    boolean ok = Logic.performFeed(prey, predator);
                    if (!ok) {
                        continue outer;           // prey died OR predator still hungry
                    }
                }

                // after X cycles, check hunger
                boolean allFed = sample.stream().noneMatch(Organism::getIsHungry);
                if (allFed) {
                    System.out.println("✅ Sustainable chain found");
                    View.printOrganismTable(sample);
                    isChainSustainable = true;         // <-- set flag
                    break;                             // leave outer loop
                }
            }
            
            if (!isChainSustainable)
                System.out.println("❌ No sustainable chain found in the tested combos.");
        }
    }
}