import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * Modelo simples de um caçador.
 * O caçador elimina aleatoriamente coelhos, raposas e lobos.
 * 
 * 
 * 
 */
public class Hunter extends Animal {
    private static final int MAX_AGE = 100;
    private static final double KILL_PROBABILITY = 0.25;
    private static final Random rand = new Random();

    public Hunter(boolean randomAge) {
        super();
        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
        }
    }

    @Override
    public void act(Field currentField, Field updatedField, List<Animal> newHunters) {
        incrementAge(MAX_AGE);
        if(isAlive()) {
            Location location = getLocation();

            // Tenta eliminar um animal nas proximidades
            Iterator adjacent = currentField.adjacentLocations(location);
            while(adjacent.hasNext()) {
                Location where = (Location) adjacent.next();
                Object target = currentField.getObjectAt(where);
                if(target instanceof Rabbit || target instanceof Fox || target instanceof Wolf) {
                    Animal animal = (Animal) target;
                    if(animal.isAlive() && rand.nextDouble() <= KILL_PROBABILITY) {
                        animal.setDead();
                        break;
                    }
                }
            }

            // Move-se aleatoriamente
            Location newLocation = updatedField.freeAdjacentLocation(location);
            if(newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                setDead(); // superpopulação
            }
        }
    }
}