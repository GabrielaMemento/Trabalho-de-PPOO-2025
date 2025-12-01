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
    private static final double PLANT_CREATION_PROBABILITY = 0.10; // Base da cadeia alimentar
    private static final double RABBIT_CREATION_PROBABILITY = 0.08; // Herbívoro primário
    private static final double FOX_CREATION_PROBABILITY = 0.04; // Predador secundário
    private static final double WOLF_CREATION_PROBABILITY = 0.06; // Predador intermediário
    private static final double SNAKE_CREATION_PROBABILITY = 0.04; // Predador de topo
    private static final double EAGLE_CREATION_PROBABILITY = 0.04; // Superpredador
    private static final double HUNTER_CREATION_PROBABILITY = 0.03; // Fator externo raro

    // Dinâmica de Plantas 
    private static final double PLANT_GROWTH_PROBABILITY = 0.25; // Crescimento sazonal
    private static final double PLANT_DEATH_PROBABILITY = 0.05;  // Mortalidade natural

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
    
    // CORREÇÃO: Variável para armazenar o limite de passos inicial passado pelo Main.
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
     *
     * @param numSteps Número total de passos a executar a partir do Passo 0.
     */
    public void simulate(int numSteps) {
        // CORREÇÃO: Armazena o valor passado (ex: 10) para uso futuro pelo Reset
        this.initialRunSteps = numSteps; 
        
        // Define a meta de parada (Passo 0 + 10 = Passo 10)
        this.view.setTargetSteps(numSteps);
        
        // Dispara o Timer (equivalente a clicar em 'Play')
        this.view.play(); 
    }
    
    // Novo método para permitir que o SimulatorView obtenha o limite inicial de passos
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
     * Popula o campo inicial com entidades.
     */
    private void populate() {
        field.clear();
        actors.clear();
        
        double currentProb = 0.0;
        
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Location location = new Location(row, col);
                double r = rand.nextDouble();
                currentProb = 0.0;

                // 1. Tentativa de criar um Animal (Ordem de prioridade por densidade)
                Animal newAnimal = null;

                currentProb += RABBIT_CREATION_PROBABILITY;
                if (r < currentProb) {
                    newAnimal = new Rabbit();
                } else {
                    currentProb += FOX_CREATION_PROBABILITY;
                    if (r < currentProb) {
                        newAnimal = new Fox();
                    } else {
                        currentProb += WOLF_CREATION_PROBABILITY;
                        if (r < currentProb) {
                            newAnimal = new Wolf();
                        } else {
                            currentProb += SNAKE_CREATION_PROBABILITY;
                            if (r < currentProb) {
                                newAnimal = new Snake();
                            } else {
                                currentProb += EAGLE_CREATION_PROBABILITY;
                                if (r < currentProb) {
                                    newAnimal = new Eagle();
                                } else {
                                    currentProb += HUNTER_CREATION_PROBABILITY;
                                    if (r < currentProb) {
                                        newAnimal = new Hunter();
                                    }
                                }
                            }
                        }
                    }
                }
                
                if (newAnimal != null) {
                    newAnimal.setAge(rand.nextInt(newAnimal.getMaxAge())); 
                    newAnimal.setFoodLevel(rand.nextInt(15) + 5); 
                    
                    newAnimal.setLocation(location);
                    field.place(newAnimal, location);
                    actors.add(newAnimal);
                    continue; 
                }
                
                // 2. Plantas iniciais (Só se a célula estiver vazia)
                if (r < PLANT_CREATION_PROBABILITY) {
                    Plant newPlant = rand.nextBoolean() ? Plant.ROSEMARY : Plant.SAGE;
                    newPlant.setLocation(location); 
                    field.place(newPlant, location);
                    actors.add(newPlant);
                }
            }
        }
    }
}