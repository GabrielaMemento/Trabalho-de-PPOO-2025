import java.util.List;
import java.util.Random;

/**
 * Classe abstrata que representa um animal no campo de simulação.
 * Cada animal possui idade, localização e estado de vida.
 * 
 * 
 */
public abstract class Animal {
    private int age;
    private boolean alive;
    private Location location;
    private int foodLevel;
    private int FOOD_VALUE;

    // The age at which an animal can start to breed.
    private static final int BREEDING_AGE;
    // The age to which an animal can live.
    private static final int MAX_AGE;
    // The likelihood of an animal breeding.
    private static final double BREEDING_PROBABILITY;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE;
    // A shared random number generator to control breeding.
    private static final Random rand = new Random();

    /**
     * Construtor padrão. Inicializa o animal com idade 0 e vivo.
     */
    public Animal() {
        age = 0;
        alive = true;
    }

    /**
     * Define o comportamento do animal a cada passo da simulação.
     * @param field Campo atual.
     * @param updatedField Campo atualizado.
     * @param newAnimals Lista para adicionar novos animais.
     */
    public abstract void act(Field field, Field updatedField, List<Animal> newAnimals);

    /**
     * Verifica se o animal está vivo.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Marca o animal como morto.
     */
    protected void setDead() {
        alive = false;
    }

    /**
     * Incrementa a idade do animal e verifica se ele ultrapassou a idade máxima.
     * @param maxAge Idade máxima permitida.
     */
    protected void incrementAge(int maxAge) {
        age++;
        if (age > maxAge) {
            setDead();
        }
    }

    /**
     * Retorna a idade atual do animal.
     */
    protected int getAge() {
        return age;
    }

    /**
     * Define a idade do animal.
     */
    protected void setAge(int age) {
        this.age = age;
    }

    /**
     * Retorna a localização atual do animal.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Define a nova localização do animal.
     */
    public void setLocation(Location location) {
        this.location = location;
    }
}