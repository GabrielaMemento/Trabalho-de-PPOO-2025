import java.util.List;
import java.util.Random;


public abstract class Animal
{

/**
 * Atributos comuns a todos os animais
 *  */
private static Random rand;
private int age;
private boolean alive;
private Location location;
private Field field;

public Animal(boolean randomAge, Field field, Location location)
{   
    rand = new Random();
    this.age = 0; 
    this.alive = true;
    this.field = field;
    setLocation(location);

    if (randomAge) {
            this.age = rand.nextInt(getMaxAge()); 
        }
}

/**
 * Métodos comuns a todos os animais
 *  */

public abstract int getMaxAge();
public abstract int getBreedingAge();
public abstract void act(List<Animal> newAnimals);

public void incrementAge()
{
    age++;
    if(age > getMaxAge()) {
    setDead();
    }
}


public boolean canBreed()
{
    return age >= getBreedingAge();
}

public Field getField() 
{
    return field;
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

    if (location != null && field != null) {
            field.clear(location);
            location = null;
            field = null;
        }
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