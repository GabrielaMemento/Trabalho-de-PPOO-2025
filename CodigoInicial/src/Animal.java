import java.util.List;
import java.util.Random;


public abstract class Animal
{
private Random rand;
private boolean randomAge;
private static int BREEDING_AGE;
private static int MAX_AGE;
private static double BREEDING_PROBABILITY;
private static int MAX_LITTER_SIZE;
private static int age;
private static boolean alive;
private static Location location;
private static int foodLevel;

public Animal()
{   
    rand = new Random();
    randomAge = true;
    BREEDING_AGE = 0;
    MAX_AGE = 0;
    BREEDING_PROBABILITY = 0;
    MAX_LITTER_SIZE = 0;
    age = 0; 
    alive = true;
    foodLevel = 0;
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