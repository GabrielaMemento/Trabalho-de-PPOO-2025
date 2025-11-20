import java.util.List;
import java.util.Random;


public abstract class Animal
{
private int BREEDING_AGE;
private int MAX_AGE;
private double BREEDING_PROBABILITY;
private int MAX_LITTER_SIZE;
private Random rand;
private int age;
private boolean alive;
private Location location;

public Animal()
{   age = 0; 
    alive = true;
    rand = new Random();
    BREEDING_AGE = 0;
    MAX_AGE = 0;
}


public void incrementAge()
{
    age++;
    if(age > MAX_AGE) {
    alive = false;
    }
}

public int breed()
{
    int births = 0;
    if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
    births = rand.nextInt(MAX_LITTER_SIZE) + 1;
    }
    return births;
}

public boolean canBreed()
{
    return age >= BREEDING_AGE;
}

public Location getLocation()
{
    return location;
}

public boolean isAlive()
{
    return alive;
}

public void setDead() {
    alive = false;
}

public void setLocation(int row, int col)
{
    this.location = new Location(row, col);
}

public void setLocation(Location location)
{
    this.location = location;
}

}