// This file contains all possible rules for the final algorithm which will be specified in the controller

package com.myapp.ecosystem;

import java.util.List;

public class Logic {

    // // This method removes all animals which can not be satisfied wafter eating all their food sources. 
    // public static List<Organism> eliminateUnusableAnimals(List<Organism> organismsList){




    // }

    // Helper method which takes an Organism and sees if it can be satisfied by its food sources
    public static boolean canPredatorSatisfyCalNeed(Organism predator, List<Organism> organismsList){
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
                System.out.println(preyName + "does not exist in the sample data");
                continue;
            } 
            // If it exsits, assume that it is eaten by the predator
            calRemainingNeed = calRemainingNeed - prey.getCalGive();
            
            // Early exit if the predator is satisfied already
            if (calRemainingNeed <= 0) return true;
        }
        // If the predator has not been satisfied by the last prey in hte list, then it wont be satisfied and its a useless animal.
        System.out.println(predator + "calorific needs will never be satisfied. Remove it from the data. Unusuable Animal");
        return false;     
    }
    
}
