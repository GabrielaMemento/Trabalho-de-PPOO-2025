import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Representa um caçador no ecossistema.
 */
public class Cacador extends Animal {
    /** Idade mínima para reprodução. */
    private static final int BREEDING_AGE = 20;
    /** Idade máxima. */
    private static final int MAX_AGE = 300;
    /** Probabilidade de reprodução a cada passo. */
    private static final double BREEDING_PROBABILITY = 0.03;
    /** Tamanho máximo da ninhada. */
    private static final int MAX_LITTER_SIZE = 1;
    /** Valor nutricional da caça (restaura fome). */
    private static final int HUNT_FOOD_VALUE = 5;
    /** RNG local. */
    private static final Random RAND = new Random();

    public Cacador(boolean randomAge, Field field, Location location) {
        super(field, location);
        setFoodLevel(HUNT_FOOD_VALUE);
        if (randomAge) {
            setAge(RAND.nextInt(MAX_AGE));
            setFoodLevel(RAND.nextInt(HUNT_FOOD_VALUE));
        }
    }

    @Override
    public void act(List<Animal> newAnimals) {
        incrementAge();
        incrementHunger();
        if (!isAlive()) return;

        giveBirth(newAnimals);

        Location newLocation = findFood();
        if (newLocation == null) {
            newLocation = getField().freeAdjacentLocation(getLocation());
        }

        if (newLocation != null) {
            Terreno terrain = getField().getTerrainAt(newLocation);
            if (terrain != Terreno.MOUNTAIN && terrain != Terreno.RIVER) {
                setLocation(newLocation);
            }
        } else {
            setDead();
        }
    }

    @Override
    public void incrementAge() {
        setAge((getAge() + 1));
        if (getAge() > MAX_AGE) setDead();
    }

    @Override
    public int breed() {
        if (canBreed() && RAND.nextDouble() <= BREEDING_PROBABILITY) {
            return RAND.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return 0;
    }

    @Override
    public boolean canBreed() { return getAge() >= BREEDING_AGE; }

    private void incrementHunger() {
        setFoodLevel(getFoodLevel() - 1);
        if (getFoodLevel() <= 0) setDead();
    }

    private Location findFood() {
        Iterator<Location> adjacent = getField().adjacentLocations(getLocation()).iterator();
        while (adjacent.hasNext()) {
            Location where = adjacent.next();
            Object obj = getField().getObjectAt(where);
            if (obj instanceof Rabbit || obj instanceof Fox || obj instanceof Lobo || obj instanceof Cobra || obj instanceof Aguia) {
                Animal animal = (Animal) obj;
                if (animal.isAlive()) {
                    animal.setDead();
                    setFoodLevel(HUNT_FOOD_VALUE);
                    return where;
                }
            }
        }
        return null;
    }

    private void giveBirth(List<Animal> newAnimals) {
        List<Location> free = getField().getFreeAdjacent(getLocation());
        int births = breed();
        for (int b = 0; b < births && !free.isEmpty(); b++) {
            Location loc = free.remove(0);
            newAnimals.add(new Cacador(false, getField(), loc));
        }
    }
}