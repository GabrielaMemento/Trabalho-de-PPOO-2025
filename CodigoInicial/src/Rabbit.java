import java.util.List;
import java.util.Random;

/**
 * Representa um Coelho no simulador predador-presa.
 * Coelhos envelhecem, movem-se, reproduzem-se e podem morrer por velhice
 * ou por superlotação quando não há espaço livre para se deslocar.
 */
public class Rabbit extends Animal {
    private static final int BREEDING_AGE = 5;
    private static final int MAX_AGE = 50;
    private static final double BREEDING_PROBABILITY = 0.15;
    private static final int MAX_LITTER_SIZE = 5;
    

    public Rabbit(boolean randomAge) {
        super(); // age = 0, alive = true, location = null
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }

    @Override
    public void act(Field currentField, Field updatedField, List<Animal> newAnimals) {
        incrementAge();
        if(isAlive()) {
            int births = breed();
            for(int b = 0; b < births; b++) {
                Rabbit newRabbit = new Rabbit(false);
                newAnimals.add(newRabbit);
                Location loc = updatedField.randomAdjacentLocation(getLocation());
                newRabbit.setLocation(loc);
                updatedField.place(newRabbit, loc);
            }
            Location newLocation = updatedField.freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                setDead();
            }
        }
    }

    @Override
    public void incrementAge() {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    @Override
    public int breed() {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    @Override
    public boolean canBreed() {
        return age >= BREEDING_AGE;
    }
}

/*
 	O construtor e as constantes ficaram confusos.
 	O ideal é manter as constantes fixas dentro da classe e simplificar o construtor.
 	A criação de novos coelhos deve ser apenas 
*/
