# Ecosystem Game Solver
Author: Georgios Kokkinos 
## Overview 
This is a solver to a well known ecosystem game.

You’re given a library of species split into producers and animals. These organisms have caloric needs, caloric output if eaten, and habitat constraints. The task is to pick exactly eight organisms, position them on a single map tile that meets every species’ environmental requirements, and set a feeding order so that every animal covers its calorie need without any prey being over-consumed. 

## Rules

The species with the highest Calories Provided eats first. It eats its Food Source with the highest Calories Provided. In case of a tie, it eats equally from both species.

When a Food Source is eaten, its Calories Provided decrease permanently by an amount equal to the eating species' Calories Needed. If the eating species needs more calories, it eats another Food Source based on current Calories Provided.

Then the species with the next highest current Calories Provided eats. Species who end with their Calories Needed fully met and more then zero Calories Provided survive.

## Software structure
I tried to use a Model View Controller (MVC) architecture + an infrastructure layer where:
    
    Model: What the program is about: the data and the business rules. Organism class holds all state for a species (calories, prey list, flags) Logic class contains pure algorithms that act on Organism lists (eg. pruning, combination generation, feeding simulation)
    
    View: How the user sees it. The CLI I/O layer.Only prints messages (printLine, printOrganismTable, etc.) and reads a line from Scanner. It knows nothing about rules or file formats.
    
    Controller: It pulls everything together. It coordinates one whole run: Prompts the user, calls ExcelReader to build the model, Invokes Logic to prune lists, generate combos, run feeding loops, Decides when to stop and tells the View to display results.
    
    Infrastructure: ExcelReader to convert an excel file into Model objects. 

## How to use
Outside of software:

1.	Pick one habitat tile
    Decide the depth / temperature / salinity range you want to play with.
	
2.	Build an Excel sheet (one row per species) with the following header 
 name | type | calNeed | calGive | eats | eatenBy | cond1 | cond2 | cond3 | cond4
	
3.	Save the file somewhere convenient (e.g. ~/Downloads/MyHabitat.xlsx).
	
4.	Run the program

➜  What the software does

1.	Load & validate the spreadsheet into Organism objects.
	
2.	Prune any animal that could never meet its calorie need with the species provided.
	
3.	Generate every 8-organism subset from the remaining pool.
	
4.	Simulate feeding on each subset, one predator at a time:
    •	Highest-calorie hungry animal eats equal shares from its richest prey.
    •	A combination fails if any prey is eaten to zero or a predator stays hungry.
	
5.	Stop at the first sustainable chain and print a neatly formatted table.

If none works, it prints a red ❌ summary instead.

That’s all—adjust your Excel file or habitat assumptions and run again to explore different ecosystems.


# Disclaimer

This tool is not to be used to obtain unfair advantage against other candiates or violate rules set by the companies who are using it as an assessment meant. It was designed purely for practice purposes and because it was a personal challenge. 
    
    

