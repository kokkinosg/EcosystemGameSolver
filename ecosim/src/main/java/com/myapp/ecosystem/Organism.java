package com.myapp.ecosystem;
// This is the class which will produce animals and producer
import java.util.ArrayList;
import java.util.List;

public class Organism{

    // Private variables
    private String name;               // e.g. "Rabbit"
    private String type;               // "Animal" or "Producer"
    private float calNeed;             // calories needed per turn
    private float calGive;             // calories provided if eaten
    private float calRemainGive;       // calories remaining to give. At start it is same as calGive
    private float calRemainNeed;       // calories remaining to eat. At start is the same as calNeed
    private List<String> eats;         // names of organisms it eats
    private List<String> eatenBy;      // names of organisms that eat it
    private boolean isHungry;          // At the start all animals are hungry. Once they eat, they are NOT hungry 
    private String cond1;              // habitat condition #1 eg: Temperature or Humidity
    private String cond2;              // habitat condition #2
    private String cond3;              // habitat condition #3
    private String cond4;              // habitat condition #4

    // Constructor
    public Organism() {
        // Initialise lists so they’re never null
        this.eats    = new ArrayList<>();
        this.eatenBy = new ArrayList<>();

    
    }

    // Getters
    public String getName()      { return name; }
    public String getType()      { return type; }
    public float  getCalNeed()   { return calNeed; }
    public float  getCalGive()   { return calGive; }
    public float  getCalRemainNeed()   { return calRemainNeed; }
    public float  getCalRemainGive()   { return calRemainGive; }
    public List<String> getEats()     { return eats; }
    public List<String> getEatenBy()  { return eatenBy; }
    public boolean  getIsHungry() {return isHungry;}
    public String getCond1()     { return cond1; }
    public String getCond2()     { return cond2; }
    public String getCond3()     { return cond3; }
    public String getCond4()     { return cond4; }

    // Setters
    public void setName(String name)                { this.name = name; }


    public void setType(String type){ 
        if ("producer".equals(type)){
            // Also set the isHungry to false if it is producers otherwise is true. 
            this.type = type; 
            this.isHungry = false;
        } else {
            this.type = "animal";
            this.isHungry = true;
        }
    }
    public void setCalNeed(float calNeed) {
        if (calNeed < 0) throw new IllegalArgumentException("calNeed must be ≥ 0");
        this.calNeed = calNeed;
        this.calRemainNeed = calNeed;   // Initialy the remaining need clas are the same as the calNeed. 
    }
    
    public void setCalGive(float calGive) {
        if (calGive < 0) throw new IllegalArgumentException("calGive must be ≥ 0");
        this.calGive = calGive;
        this.calRemainGive = calGive;   // keep in sync
    }

    public void setCalRemainNeed(float calRemainNeed)           { this.calRemainNeed = calRemainNeed; }
    public void setCalRemainGive(float calRemainGive)           { this.calRemainGive = calRemainGive; }
    public void setEats(List<String> eats)          { this.eats = (eats != null) ? new ArrayList<>(eats) : new ArrayList<>(); }
    public void setEatenBy(List<String> eatenBy)    { this.eatenBy = (eatenBy != null) ? new ArrayList<>(eatenBy) : new ArrayList<>(); }
    public void setIsHungry(boolean isHungry)       { this.isHungry = isHungry;}
    public void setCond1(String cond1)              { this.cond1 = cond1; }
    public void setCond2(String cond2)              { this.cond2 = cond2; }
    public void setCond3(String cond3)              { this.cond3 = cond3; }
    public void setCond4(String cond4)              { this.cond4 = cond4; }

    // Method to return the whole object in a string for quick checks 
    @Override
    public String toString() {
        return "Organism{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", calNeed=" + calNeed +
                ", calGive=" + calGive +
                ", calRemainingGive=" + calRemainGive +
                ", calRemainingNeed=" + calRemainNeed +
                ", eats=" + eats +
                ", eatenBy=" + eatenBy +
                ", cond1='" + cond1 + '\'' +
                ", cond2='" + cond2 + '\'' +
                ", cond3='" + cond3 + '\'' +
                ", cond4='" + cond4 + '\'' +
                ", is Hungry = " + isHungry+
                '}';
    }
}