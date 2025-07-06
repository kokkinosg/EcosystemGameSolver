// This file contains all possible rules for the final algorithm which will be specified in the controller

package com.myapp.ecosystem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    // Method which performs the feeding operation. 
    public static void performFeed(List<Organism> organismsList, String nameAnimalEatingNow){


        // Prey names list, i

    }


    // Helper method to sort the preys in order of highest amount of calories provide 
    private static List<Organism> sortedPreyByRemainCalProvided(List<Organism> unsortedPreyList) {

        List<Organism> sortedList =  unsortedPreyList.stream()
                // compare by getCalRemainGive() – use getCalGive() if you prefer total capacity
                .sorted(Comparator.comparing(Organism::getCalRemainGive).reversed())// largest first
                .collect(Collectors.toList()); // new List, original untouched
        
        return sortedList;
    }

    // Helper method to count how many preys share the same number of calories remianing to give as the top prey with the highest remaining claories given.
    // The argument MUST be a sorted prey list by the highest order of calories remaining to give.
    private static int countPreysWithSameCalRemainGive(List<Organism> sortedPreyList){

        // Null Check 
        if (sortedPreyList == null || sortedPreyList.isEmpty()) {
            System.out.println("Cannot count preys with the same number of calories given because the input list is null or empty");
            return 0;
        }
        // The default is 1 be cause there is always one prey
        int sameCalProvCounter = 1;

        // Get the highest number of calories which by default is in the same position
        float topRemainCalGive = sortedPreyList.get(0).getCalRemainGive();

        // Loop through the next elemtns and count how many are equal, skip the first elemtn
        for (int i = 1; i < sortedPreyList.size(); i++ ){
            if (sortedPreyList.get(i).getCalRemainGive() == topRemainCalGive){
                // Increase the counter by 1
                sameCalProvCounter++;
            }
        }
        return sameCalProvCounter;
    }    

    // TO BE PRIVATE Helper method to get a list of prey 
    public static List<Organism> listOfPrey(List<Organism> organismList, String predatorName){
        
        // Get the predator by its name
        Organism predator = getOrganismByName(predatorName, organismList);

        // Get a list of preyNames
        List<String> preyNames = predator.getEats();

        // Declare an organism list 
        List<Organism> preyList = new ArrayList<>();

        // Add each element 
        for (String name:preyNames){

            // Check if the organism exists in in hte organism list. Sometimes organisms may prey to animals not in the list
            Organism prey = getOrganismByName(name, organismList);

            // Do a null check. If not null, add it to the list 
            if (prey != null){
                preyList.add(prey);
            }
        }
        return preyList;
    }

    //  TO BE PRIVATE Get the organism by name 
    public static Organism getOrganismByName(String name, List<Organism> organismList){
        // Get the organism by its name. If the organism cannot be found, return null 
        Organism organism = organismList.stream()
                .filter(o -> o.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        return organism;
    }

    // TO BE PRIVATE Helper method to find the name of the animal which eats now. This is the animal with the highest calories provided.
    public static String nameOfAnimalHighestCaloriesProvided(List<Organism> organismsList){
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

    //  Helper method which takes an Organism and sees if it can be satisfied by its food sources
    private static boolean canPredatorSatisfyCalNeed(Organism predator, List<Organism> organismsList){
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
    
    // TO BE PRIVATE Helper method to check if an Organism is dead which happens when the remaining calories given is 0
    public static boolean isOrganismDead(Organism organism){
        if (organism.getCalRemainGive() <= 0){
            return true;
        }
        return false;
    }
}
