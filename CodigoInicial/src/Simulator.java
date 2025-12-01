import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * Classe principal que controla o ciclo da simulação do ecossistema.
 *
 * @author Grupo 1
 * @version 2025
 */
public class Simulator {

    // Constantes de Dimensão e Probabilidade
    private static final int DEFAULT_WIDTH = 50;
    private static final int DEFAULT_DEPTH = 50;
    
    // Probabilidades de Criação
    private static final double PLANT_CREATION_PROBABILITY = 0.15; 
    private static final double RABBIT_CREATION_PROBABILITY = 0.20; 
    private static final double FOX_CREATION_PROBABILITY = 0.05; 
    private static final double WOLF_CREATION_PROBABILITY = 0.07; 
    private static final double SNAKE_CREATION_PROBABILITY = 0.02; 
    private static final double EAGLE_CREATION_PROBABILITY = 0.05; 
    private static final double HUNTER_CREATION_PROBABILITY = 0.05; 

    // Dinâmica de Plantas 
    private static final double PLANT_GROWTH_PROBABILITY = 0.25;
    private static final double PLANT_DEATH_PROBABILITY = 0.05;

    /** Lista de atores ativos na simulação (Animais e Plantas). */
    private final List<Actor> actors;
    /** Campo da simulação (grade com objetos e terrenos). */
    public Field field; 
    /** Número do passo atual da simulação. */
    public int step; 
    /** Interface gráfica para exibir o estado da simulação. */
    private final SimulatorView view;
    /** Gerador de números aleatórios. */
    private final Random rand = new Random();
    
    private int initialRunSteps = 0; 

    /**
     * Construtor padrão: cria um simulador com dimensões padrão.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Construtor: cria um simulador com dimensões especificadas.
     */
    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        actors = new ArrayList<>();
        field = new Field(depth, width);

        Barriers.loadRestrictions();

        view = new SimulatorView(depth, width);
        view.setColor(Fox.class, Color.orange);
        view.setColor(Rabbit.class, Color.lightGray);
        view.setColor(Wolf.class, Color.darkGray);
        view.setColor(Eagle.class, Color.yellow);
        view.setColor(Snake.class, Color.black);
        view.setColor(Hunter.class, Color.cyan);
        view.setColor(Plant.ROSEMARY.getClass(), Color.green.darker());
        view.setColor(Plant.SAGE.getClass(), Color.green.brighter());

        view.setSimulator(this);

        reset(); // Chama reset e exibe o Passo 0
    }
    
    /**
     * Define o número de passos a executar e inicia o Timer para rodar a simulação visualmente.
     */
    public void simulate(int numSteps) {
        this.initialRunSteps = numSteps; 
        this.view.setTargetSteps(numSteps);
        this.view.play(); 
    }
    
    public int getInitialRunSteps() {
        return initialRunSteps;
    }

    /**
     * Executa um único passo da simulação.
     */
    public void simulateOneStep() {
        step++;
        
        Field nextField = new Field(field);
        List<Actor> newActors = new ArrayList<>();

        for (Iterator<Actor> iter = actors.iterator(); iter.hasNext();) {
            Actor actor = iter.next();

            actor.act(field, nextField, newActors); 
            
            boolean shouldRemove = false;

            if (actor instanceof Animal animal) {
                if (!animal.isAlive()) {
                    shouldRemove = true;
                }
            } else if (actor instanceof Plant) {
                if (rand.nextDouble() <= PLANT_DEATH_PROBABILITY) {
                    shouldRemove = true;
                }
            }
            
            if (shouldRemove) {
                iter.remove();
                if (actor.getLocation() != null) {
                    nextField.clear(actor.getLocation());
                }
            }
        }

        managePlants(nextField);
        actors.addAll(newActors);
        field = nextField;
        view.showStatus(step, field);
    }

    /**
     * Reinicia a simulação.
     */
    public void reset() {
        step = 0;
        actors.clear();
        field = new Field(field.getDepth(), field.getWidth()); 
        populate();
        view.showStatus(step, field);
    }

    /**
     * Gerencia o ciclo de vida das plantas: apenas o crescimento.
     */
    private void managePlants(Field updatedField) {
        growPlants(updatedField);
    }

    /**
     * Faz crescer novas plantas em células vazias do campo de destino.
     */
    private void growPlants(Field currentField) {
        for (int row = 0; row < currentField.getDepth(); row++) {
            for (int col = 0; col < currentField.getWidth(); col++) {
                Location location = new Location(row, col);
                
                if (currentField.getObjectAt(location) == null) {
                    if (rand.nextDouble() <= PLANT_GROWTH_PROBABILITY) {
                        
                        Plant newPlant = rand.nextBoolean() ? Plant.ROSEMARY : Plant.SAGE;
                        
                        newPlant.setLocation(location);
                        currentField.place(newPlant, location);
                        actors.add(newPlant);
                    }
                }
            }
        }
    }

    /**
     * Popula o campo inicial usando probabilidades independentes (modelo original).
     */
    private void populate() {
        field.clear();
        actors.clear(); // Limpa a lista, pois animais serão adicionados aqui

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Location location = new Location(row, col);
                
                // Variável auxiliar para rastrear o último ator criado (se houver colisão)
                Animal createdAnimal = null;
                
                // 1. ANIMAIS (Probabilidades Independentes - Colisão é possível, a última entidade vence)
                
                if (rand.nextDouble() <= WOLF_CREATION_PROBABILITY) {
                    createdAnimal = new Wolf();
                }
                if (rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    createdAnimal = new Fox();
                }
                if (rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    createdAnimal = new Rabbit();
                }
                if (rand.nextDouble() <= EAGLE_CREATION_PROBABILITY) {
                    createdAnimal = new Eagle();
                }
                if (rand.nextDouble() <= HUNTER_CREATION_PROBABILITY) {
                    createdAnimal = new Hunter();
                }
                if (rand.nextDouble() <= SNAKE_CREATION_PROBABILITY) {
                    createdAnimal = new Snake();
                }
                
                if (createdAnimal != null) {
                    // Inicialização de estado (Idade e Fome)
                    createdAnimal.setAge(rand.nextInt(createdAnimal.getMaxAge())); 
                    createdAnimal.setFoodLevel(rand.nextInt(15) + 5); 
                    
                    // Coloca no campo e na lista de atores
                    createdAnimal.setLocation(location);
                    field.place(createdAnimal, location);
                    actors.add(createdAnimal);
                    
                    // Continuamos, pois um animal já ocupa a célula e venceu a colisão
                    continue; 
                }
                
                // 2. PLANTAS INICIAIS (Só se a célula estiver vazia - não precisa de verificação extra, pois o continue acima já lida com isso)
                if (rand.nextDouble() <= PLANT_CREATION_PROBABILITY) {
                    Plant newPlant = rand.nextBoolean() ? Plant.ROSEMARY : Plant.SAGE;
                    newPlant.setLocation(location); 
                    field.place(newPlant, location);
                    actors.add(newPlant);
                }
            }
        }
    }
}