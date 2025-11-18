import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.awt.Color;

/**
 * Um simulador simples de predador-presa, baseado em um campo contendo
 * coelhos, raposas e outros atores do ecossistema.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-09 (expandido por Grupo 1 - 2025)
 */
public class Simulator {
    // Configurações padrão do campo
    private static final int DEFAULT_WIDTH = 50;
    private static final int DEFAULT_DEPTH = 50;

    // Probabilidades de criação dos atores
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;
    private static final double WOLF_CREATION_PROBABILITY = 0.01;
    private static final double SNAKE_CREATION_PROBABILITY = 0.01;
    private static final double EAGLE_CREATION_PROBABILITY = 0.005;
    private static final double HUNTER_CREATION_PROBABILITY = 0.003;
    private static final double ROSEMARY_CREATION_PROBABILITY = 0.03;
    private static final double SAGE_CREATION_PROBABILITY = 0.03;

    // Lista de animais vivos
    private List<Animal> animals;
    // Lista de animais recém-nascidos
    private List<Animal> newAnimals;
    // Estado atual do campo
    private Field field;
    // Campo auxiliar para o próximo estado
    private Field updatedField;
    // Passo atual da simulação
    private int step;
    // Visualização gráfica da simulação
    private SimulatorView view;

    /**
     * Constrói um campo de simulação com tamanho padrão.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Cria um campo de simulação com tamanho definido.
     * @param depth Profundidade do campo.
     * @param width Largura do campo.
     */
    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            System.out.println("As dimensões devem ser maiores que zero.");
            System.out.println("Usando valores padrão.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        animals = new ArrayList<>();
        newAnimals = new ArrayList<>();
        field = new Field(depth, width);
        updatedField = new Field(depth, width);

        // Cria a visualização gráfica do campo
        view = new SimulatorView(depth, width);
        view.setColor(Fox.class, Color.blue);
        view.setColor(Rabbit.class, Color.orange);

        // Expansão: adiciona cores para novos atores
        view.setColor(Wolf.class, Color.darkGray);
        view.setColor(Snake.class, Color.green);
        view.setColor(Eagle.class, Color.red);
        view.setColor(Hunter.class, Color.black);
        view.setColor(Rosemary.class, new Color(34, 139, 34)); // verde escuro
        view.setColor(Sage.class, new Color(107, 142, 35));    // verde oliva

        // Inicializa o campo
        reset();
    }

    /**
     * Executa a simulação por um período longo (ex: 500 passos).
     */
    public void runLongSimulation() {
        simulate(500);
    }

    /**
     * Executa a simulação por um número definido de passos.
     * Interrompe se o campo deixar de ser viável.
     */
    public void simulate(int numSteps) {
        for (int i = 1; i <= numSteps && view.isViable(field); i++) {
            simulateOneStep();
        }
    }

    /**
     * Executa um único passo da simulação.
     * Atualiza o estado de todos os animais.
     */
    public void simulateOneStep() {
        step++;
        newAnimals.clear();

        // Todos os animais agem
        for (Iterator<Animal> iter = animals.iterator(); iter.hasNext(); ) {
            Animal animal = iter.next();
            if (animal.isAlive()) {
                animal.act(field, updatedField, newAnimals);
            }
            if (!animal.isAlive()) {
                iter.remove(); // remove animais mortos
            }
        }

        // Adiciona os recém-nascidos
        animals.addAll(newAnimals);

        // Troca os campos
        Field temp = field;
        field = updatedField;
        updatedField = temp;
        updatedField.clear();

        // Atualiza a visualização
        view.showStatus(step, field);
    }

    /**
     * Reinicia a simulação para o estado inicial.
     */
    public void reset() {
        step = 0;
        animals.clear();
        field.clear();
        updatedField.clear();
        populate(field);
        view.showStatus(step, field);
    }

    /**
     * Popula o campo com os atores iniciais.
     */
    private void populate(Field field) {
        Random rand = new Random();
        field.clear();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Location loc = new Location(row, col);
                double r = rand.nextDouble();

                if (r <= FOX_CREATION_PROBABILITY) {
                    Fox fox = new Fox(true);
                    fox.setLocation(loc);
                    animals.add(fox);
                    field.place(fox, loc);
                } else if (r <= FOX_CREATION_PROBABILITY + RABBIT_CREATION_PROBABILITY) {
                    Rabbit rabbit = new Rabbit(true);
                    rabbit.setLocation(loc);
                    animals.add(rabbit);
                    field.place(rabbit, loc);
                } else if (r <= FOX_CREATION_PROBABILITY + RABBIT_CREATION_PROBABILITY + WOLF_CREATION_PROBABILITY) {
                    Wolf wolf = new Wolf(true);
                    wolf.setLocation(loc);
                    animals.add(wolf);
                    field.place(wolf, loc);
                } else if (r <= FOX_CREATION_PROBABILITY + RABBIT_CREATION_PROBABILITY + WOLF_CREATION_PROBABILITY + SNAKE_CREATION_PROBABILITY) {
                    Snake snake = new Snake(true);
                    snake.setLocation(loc);
                    animals.add(snake);
                    field.place(snake, loc);
                } else if (r <= FOX_CREATION_PROBABILITY + RABBIT_CREATION_PROBABILITY + WOLF_CREATION_PROBABILITY + SNAKE_CREATION_PROBABILITY + EAGLE_CREATION_PROBABILITY) {
                    Eagle eagle = new Eagle(true);
                    eagle.setLocation(loc);
                    animals.add(eagle);
                    field.place(eagle, loc);
                } else if (r <= FOX_CREATION_PROBABILITY + RABBIT_CREATION_PROBABILITY + WOLF_CREATION_PROBABILITY + SNAKE_CREATION_PROBABILITY + EAGLE_CREATION_PROBABILITY + HUNTER_CREATION_PROBABILITY) {
                    Hunter hunter = new Hunter(true);
                    hunter.setLocation(loc);
                    animals.add(hunter);
                    field.place(hunter, loc);
                } else if (rand.nextDouble() < ROSEMARY_CREATION_PROBABILITY) {
                    Rosemary rosemary = new Rosemary();
                    rosemary.setLocation(loc);
                    field.place(rosemary, loc);
                } else if (rand.nextDouble() < SAGE_CREATION_PROBABILITY) {
                    Sage sage = new Sage();
                    sage.setLocation(loc);
                    field.place(sage, loc);
                }
            }
        }

        Collections.shuffle(animals);
    }
}