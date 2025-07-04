// This file contains all possible rules for the final algorithm which will be specified in the controller

package com.myapp.ecosystem;

import java.util.ArrayList;
import java.util.List;

public class Logic {

    // // This method removes all animals which can not be satisfied wafter eating all their food sources. 
    public static List<Organism> eliminateUnusableAnimals(List<Organism> organismsList){

        // Create a copy of the organismList to mutate it 
        List<Organism> remaining = new ArrayList<>(organismsList);

        // Boolean to track whether the list is still changing 
        boolean changed;
        // Go over all elements of the lsit at least once and then keep looping until nothing changes
        do {
            changed = false;

            // iterate over a snapshot to avoid CME
            for (Organism predator : new ArrayList<>(remaining)) {
                if (!canPredatorSatisfyCalNeed(predator, remaining)) {
                    //remove organisms which mathc the name
                    remaining.removeIf(o -> o.getName().equalsIgnoreCase(predator.getName()));
                    // This means that the list changed
                    changed = true;
                }
            }
        } while (changed);            // repeat until no more deletions

        // Return the remaing list
        return remaining;
    }

    // Method which means an animal feeds on others.

    // Helper method to find the name of the animal which eats now. This is the animal with the highest calories provided.
    public static String nameOfAnimalEatingNow(List<Organism> organismsList){
        // Start with an empty string. 
        String name = "";
        float calRemainingGivePrev = 0;

        // Go over each animal and compare the calories it provides. At the end it will ensure that we got the name of the organism with the highest 
        for(Organism organism : organismsList){
            // Get the calories given by the current organism in the list
            float calRemainingGiveCurrent = organism.getCalRemainGive();
            //Compare it with the cal given of hte previous prey and if it is larger, then this animal feeds before the previous
            if(calRemainingGiveCurrent>calRemainingGivePrev){
                name = organism.getName();
            }
            calRemainingGivePrev = calRemainingGiveCurrent;
        }
        return name;
    }

    // Helper method which takes an Organism and sees if it can be satisfied by its food sources
    public static boolean canPredatorSatisfyCalNeed(Organism predator, List<Organism> organismsList){
        // First check that the predator is an animal and not a producer because producers have 0 needs and eat nothing. If it is a producer do nothing. 
        if (predator.getType() == "animal"){
            // Create a list of food sources from this organism
            List<String> foodSources = predator.getEats();
            // Get the remaining calories needed of this animal. It will be the same as the calories needed. 
            float calRemainingNeed = predator.getCalRemainNeed();

            for(String preyName: foodSources){
                // find the prey organism by name (case-insensitive match). if it doesnt exist return null. 
                Organism prey = organismsList.stream()
                        .filter(o -> o.getName().equalsIgnoreCase(preyName))
                        .findFirst()
                        .orElse(null);

                // Check if it exsits in the data, if not it might be because it wouldnt survive at that environment.
                if (prey == null){
                    System.out.println(preyName + " --- does not exist in the sample data");
                    continue;
                } 
                // If it exsits, assume that it is eaten by the predator
                calRemainingNeed = calRemainingNeed - prey.getCalGive();
                
                // Early exit if the predator is satisfied already
                if (calRemainingNeed <= 0) return true;
            }
            // If the predator has not been satisfied by the last prey in hte list, then it wont be satisfied and its a useless animal.
            System.out.println(predator.getName() + " --- calorific needs will never be satisfied. Remove it from the data. Unusuable Animal");
            return false;     
        }
        // return true if the organism is a producer
        return true;
    }
    
    // Helper method to check if an Organism is dead which happens when the remaining calories given is 0
    public static boolean isOrganismDead(Organism organism){
        if (organism.getCalRemainGive() <= 0){
            return true;
        }
        return false;
    }
}
