import java.util.List;
import java.util.Random;


public abstract class Animal implements Actor {
    private int age;
    private boolean alive;
    private Location location;
    private int foodLevel;
    private Field field;
    private static final Random RAND = new Random();


    public abstract void act(List<Animal> newAnimals);

    
    public abstract int getBreedingAge();

    public abstract double getBreedingProbability();

    public abstract int getMaxLitterSize();

    public abstract int getMaxAge();

    public abstract Location findFood();



    public Animal(boolean randomAge, Field field, Location location) {
        this.age = 0;
        this.alive = true;
        this.field = field;
        this.foodLevel = 0;
        setLocation(location);
        
        if (randomAge) {
            this.age = RAND.nextInt(getMaxAge()); 
        }
    }


    public void incrementAge() {
        age++;
        if (age > getMaxAge()) {
            setDead();
        }
    }

    
    public boolean canBreed() {
        return age >= getBreedingAge(); 
    }

    public int breed() {
        if (canBreed() && RAND.nextDouble() <= getBreedingProbability()) {
            return RAND.nextInt(getMaxLitterSize()) + 1;
        }
        return 0;
    }

    public void incrementHunger() {
        setFoodLevel(foodLevel - 1);
        if (foodLevel <= 0) setDead();
    }

    public void giveBirth(List<Animal> newAnimals) {
        List<Location> free = getField().getFreeAdjacent(getLocation());
        int births = breed();
        for (int b = 0; b < births && !free.isEmpty(); b++) {
            Location loc = free.remove(0);
            newAnimals.add(new Rabbit(false, getField(), loc));
        }
    }

    public void setDead() {
        alive = false;
        if (location != null && field != null) {
            field.clear(location);            
        }
        location = null;
        field = null;
    }


    public void setLocation(Location newLocation) {
        if (location != null && field != null) {
            field.clear(location);
        }
        location = newLocation;
        if (field != null && newLocation != null) {
            field.place(this, newLocation);
        }
    }

    public void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
    }

    public int getFoodLevel() {
        return foodLevel;
    }
    
    public boolean isAlive() {
        return alive; 
    }

    public Field getField() {
        return field;
    }

    public Location getLocation() { 
        return location; 
    }


}
