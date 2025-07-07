package com.myapp.ecosystem;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
            // Give a default location 
            View.printLine("This is a suggested path: " + "/Users/george/Downloads/SampleData.xlsx");
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

            // // - 4 Do a quick check to identify which animals are useless in a first iteration
            // for(Organism organism : tabulatedData){
            //     Logic.canPredatorSatisfyCalNeed(organism, tabulatedData);
            // }

            // - 5 Get rid of all useless animal 
            List<Organism> processedList = new ArrayList<>();
            processedList = Logic.eliminateUnusableAnimals(tabulatedData);
            View.printAllNames(processedList);

            // Define a success boolean where when true it means we found a sustainable chain of 8 animals
            boolean isChainSustainable = false;

            outer:
            while (!isChainSustainable) {

                // Ensure that we start with fresh organism satus and calories
                Logic.resetOrganisms(processedList);       
                // 6. Get a sample of 8 organisms (3 produces + 5 random animals from the processed list)
                List<Organism> sample = Logic.sampleUpToEight(processedList);

                // Check that we got some data
                if (sample.isEmpty()) continue;             // not enough animals

                // 7. Start 5 feeding cycles, one for each animal.
                for (int cycle = 0; cycle < 5; cycle++) {

                    // Get the name of the animal which eats now. It is the one with hte highest calories provided
                    String predatorName = Logic.nameOfAnimalHighestCaloriesProvided(sample);
                    Organism predator   = Logic.getOrganismByName(predatorName, sample);

                    // If the predator is dead, break the loop. The ecosystem failed. 
                    if (predator == null || Logic.isOrganismDead(predator)) break;

                    // Get the prey of that predator
                    List<Organism> prey = Logic.listOfPrey(sample, predatorName);
                    // If there is no prey, then break because it will never satisfy its needs.
                    if (prey.isEmpty()) break;

                    boolean ok = Logic.performFeed(prey, predator);
                    if (!ok) continue outer;                // predator died -> resample
                }

                boolean allFed = sample.stream()
                        .noneMatch(Organism::getIsHungry);

                if (allFed) {
                    System.out.println("✅ Sustainable chain found");
                    View.printOrganismTable(sample);
                    break;                                  // success
                }
                // else: try another sample
            }

            
        }
    }
}