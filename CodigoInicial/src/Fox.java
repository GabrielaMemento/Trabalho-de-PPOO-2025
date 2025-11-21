import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Representa uma Raposa no simulador predador-presa.
 * Raposas envelhecem, movem-se, caçam coelhos, reproduzem-se e podem morrer por fome,
 * idade avançada ou superlotação.
 *
 * A classe foi refatorada para herdar de {@link Animal}, concentrando os
 * atributos e comportamentos comuns aos animais (idade, estado de vida, localização),
 * conforme boas práticas discutidas no livro de Barnes & Kolling.
 *
 */
public class Fox extends Animal {
    // Idade mínima para reprodução (em passos de simulação)
    private static final int BREEDING_AGE = 10;
    // Idade máxima que a raposa pode atingir antes de morrer
    private static final int MAX_AGE = 150;
    // Probabilidade de reprodução em um passo de simulação
    private static final double BREEDING_PROBABILITY = 0.09;
    // Tamanho máximo de uma ninhada 
    private static final int MAX_LITTER_SIZE = 3;
    // Valor de comida obtido ao comer um coelho (passos sem precisar comer) 
    private static final int RABBIT_FOOD_VALUE = 4;
    // Limite máximo do nível de comida da raposa
    private static final int MAX_FOOD_VALUE = 20;

    // Nível atual de comida da raposa. Cai a cada passo e aumenta ao comer
    private int foodLevel;

    /**
     * Cria uma nova Raposa
     * Pode nascer com idade e fome aleatórias, ou iniciar jovem e alimentada
     *
     * @param randomAge Se true, inicializa com idade e fome aleatórias
     */
    public Fox(boolean randomAge) {
        super(); // age = 0, alive = true, location = null
        if(randomAge) {
            this.age = rand.nextInt(MAX_AGE);
            this.foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
        } else {
            this.foodLevel = RABBIT_FOOD_VALUE;
        }
    }

    /**
     * Ação principal da raposa em um passo da simulação
     * Ela envelhece, fica com fome, tenta reproduzir e procura alimento (coelhos)
     * Caso não encontre alimento, move-se para uma posição livre adjacente
     * Se não houver espaço, morre por superlotação
     *
     * @param currentField O campo atual onde a raposa está
     * @param updatedField O campo de próxima geração onde resultados são aplicados
     * @param newAnimals Lista onde novos animais (filhotes) são adicionados
     */
    @Override
    public void act(Field currentField, Field updatedField, List<Animal> newAnimals) {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            // Reprodução
            int births = breed();
            for(int b = 0; b < births; b++) {
                Fox newFox = new Fox(false);
                newAnimals.add(newFox);
                Location loc = updatedField.randomAdjacentLocation(getLocation());
                newFox.setLocation(loc);
                updatedField.place(newFox, loc);
            }
            // Caça por alimento (coelhos)
            Location newLocation = findFood(currentField, getLocation());
            if(newLocation == null) {
                newLocation = updatedField.freeAdjacentLocation(getLocation());
            }
            if(newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                setDead(); // morreu por superlotação
            }
        }
    }

    /**
     * Incrementa a idade da raposa e verifica morte por idade avançada
     */
    @Override
    public void incrementAge() {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Reduz o nível de comida, causando morte se chegar a zero
     */
    public void incrementHunger() {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Procura por alimento (coelhos) nas posições adjacentes
     * Ao encontrar um coelho vivo, o mata, aumenta o nível de comida
     * e retorna sua posição para a raposa mover-se para lá
     *
     * @param field Campo em que a busca é realizada
     * @param location Posição atual da raposa
     * @return A localização do alimento encontrado, ou null se não houver
     */
    public Location findFood(Field field, Location location) {
        Iterator<Location> it = field.adjacentLocations(location);
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) {
                    rabbit.setDead();
                    foodLevel += RABBIT_FOOD_VALUE;
                    if(foodLevel > MAX_FOOD_VALUE) {
                        foodLevel = MAX_FOOD_VALUE;
                    }
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Calcula o número de nascimentos neste passo, segundo a probabilidade da espécie
     *
     * @return Quantidade de filhotes gerados (0 a MAX_LITTER_SIZE)
     */
    @Override
    public int breed() {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * Indica se a raposa já pode se reproduzir
     *
     * @return true se a idade for maior ou igual a BREEDING_AGE
     */
    @Override
    public boolean canBreed() {
        return age >= BREEDING_AGE;
    }
}


/*
* - Construtor simples (new Fox(boolean randomAge)), sem parâmetros inúteis.
    - Constantes da espécie (BREEDING_AGE, MAX_AGE, etc.) definidas dentro da classe.
    - Método findFood corrigido para receber Field como parâmetro.
    - Uso correto da herança: atributos comuns (age, alive, location) vêm de Animal.
    - Método act() substitui hunt(), permitindo polimorfismo.

 */
