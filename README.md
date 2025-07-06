# EcosystemGameSolver
This is a solver to a well known ecosystem game.

## Rules

The species with the highest Calories Provided eats first. It eats its Food Source with the highest Calories Provided. In case of a tie, it eats equally from both species.

When a Food Source is eaten, its Calories Provided decrease permanently by an amount equal to the eating species' Calories Needed. If the eating species needs more calories, it eats another Food Source based on current Calories Provided.

Then the species with the next highest current Calories Provided eats. Species who end with their Calories Needed fully met and more then zero Calories Provided survive.
