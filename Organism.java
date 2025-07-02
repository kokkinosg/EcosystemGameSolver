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
    private String cond1;              // habitat condition #1 eg: Temperature or Humidity
    private String cond2;              // habitat condition #2
    private String cond3;              // habitat condition #3
    private String cond4;              // habitat condition #4

    // Constructor
    public Organism() {
        // Initialise lists so they’re never null
        this.eats    = new ArrayList<>();
        this.eatenBy = new ArrayList<>();
        // Initialise the calories remaining
        this.calRemainGive = this.calGive;
        this.calRemainNeed = this.calNeed;
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
    public String getCond1()     { return cond1; }
    public String getCond2()     { return cond2; }
    public String getCond3()     { return cond3; }
    public String getCond4()     { return cond4; }

    // Setters
    public void setName(String name)                { this.name = name; }

    public void setType(String type){ 
        if ("producer".equals(type)){
            this.type = type; 
        } else {
            this.type = "animal";
        }
    }

    public void setCalNeed(float calNeed)           { this.calNeed = calNeed; }
    public void setCalGive(float calGive)           { this.calGive = calGive; }
    public void setCalRemainNeed(float calRemainNeed)           { this.calRemainNeed = calRemainNeed; }
    public void setCalRemainGive(float calRemainGive)           { this.calRemainGive = calRemainGive; }

    public void setEats(List<String> eats)          { this.eats = (eats != null) ? new ArrayList<>(eats) : new ArrayList<>(); }
    public void setEatenBy(List<String> eatenBy)    { this.eatenBy = (eatenBy != null) ? new ArrayList<>(eatenBy) : new ArrayList<>(); }
    public void setCond1(String cond1)              { this.cond1 = cond1; }
    public void setCond2(String cond2)              { this.cond2 = cond2; }
    public void setCond3(String cond3)              { this.cond3 = cond3; }
    public void setCond4(String cond4)              { this.cond4 = cond4; }

    // Method to return the whole object in a string 
    @Override
    public String toString() {
        return "Organism{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", calNeed=" + calNeed +
                ", calGive=" + calGive +
                ", eats=" + eats +
                ", eatenBy=" + eatenBy +
                ", cond1='" + cond1 + '\'' +
                ", cond2='" + cond2 + '\'' +
                ", cond3='" + cond3 + '\'' +
                ", cond4='" + cond4 + '\'' +
                '}';
    }
}