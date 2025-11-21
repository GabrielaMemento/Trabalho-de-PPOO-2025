import java.util.Random;

/**
 * Classe abstrata que representa um animal genérico.
 * Cada animal possui idade, estado de vida e localização.
 *  As Subclasses devem definir seus  limites de idade,
 * reprodução e comportamento.
 * 
 */
public abstract class Animal {
    public int age;
    public boolean alive;
    public Location location;
    public int foodLevel; // usado apenas por predadores
    public static final Random rand = new Random();

    public Animal() {
        this.age = 0;
        this.alive = true;
        this.location = null;
    }

    // Cada animal deve implementar sua ação principal (caçar, correr, reproduzir)
    public abstract void act(Field field, Field updatedField, java.util.List<Animal> newAnimals);

    // Cada animal define como envelhece
    public abstract void incrementAge();

    // Cada animal define como se reproduz
    public abstract int breed();

    // Cada animal define se pode reproduzir
    public abstract boolean canBreed();

    public boolean isAlive() {
        return alive;
    }

    public void setDead() {
        alive = false;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLocation(int row, int col) {
        this.location = new Location(row, col);
    }
}

    

    /*
    * Uso de static em atributos que deveriam ser de instância  
            - age, alive, foodLevel não deveriam ser static
            - Se forem static, todos os animais compartilham a mesma idade e estado de vida, 
            o que não faz sentido. Cada animal precisa ter seus próprios valores.
    * Contantes genéricas na classe abstrata
            - BREEDING_AGE, MAX_AGE, BREEDING_PROBABILITY, MAX_LITTER_SIZE não podem ser 
            fixos, pois cada espécie tem valores diferentes
            Solução: deixar esses valores definidos nas subclasses
    * Métodos concretos que deveriam ser 
            -  incrementAge(), breed() e incrementHunger() têm lógica que depende da 
            melhor declarar como abstratos ou fornecer uma implementação genérica e permitir que as 
            subclasses sobrescrevam
    * Construtor zerando tudo
            - Melhor: inicializar apenas os atributos comuns (age = 0, alive = true) 
            e deixar as subclasses configurarem seus próprios limites


    */
