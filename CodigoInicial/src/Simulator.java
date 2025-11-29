import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * Classe principal da simulação do ecossistema.
 *
 * Responsabilidades:
 * - Controlar o ciclo da simulação (passos, reinício, execução longa).
 * - Manter a lista de animais ativos e o campo (Field) com objetos e terrenos.
 * - Popular o campo inicial com raposas, coelhos e plantas (Alecrim, Sálvia).
 * - Coordenar a interação entre animais e plantas a cada passo.
 * - Atualizar a interface gráfica (SimulatorView) com o estado atual.
 *
 * Convenções:
 * - Cada passo da simulação corresponde a uma chamada de {@link #simulateOneStep()}.
 * - Animais mortos são removidos da lista principal.
 * - Novos animais nascidos são adicionados ao final de cada passo.
 * - A simulação continua enquanto for viável (mais de uma espécie presente).
 *
 * Extensões:
 * - Probabilidades de criação de espécies podem ser ajustadas para calibrar o ecossistema.
 * - Novos tipos de animais ou plantas podem ser adicionados na função {@link #populate(Field)}.
 *
 * @author
 *   Base: Barnes & Kolling
 * @version
 *   2002-04-23 (comentado e revisado em 2025-11 para plantas e terrenos)
 */
public class Simulator {
    /** Largura padrão do campo (número de colunas). */
    private static final int DEFAULT_WIDTH = 50;
    /** Profundidade padrão do campo (número de linhas). */
    private static final int DEFAULT_DEPTH = 50;

    /** Probabilidade de criação inicial de raposas em cada célula. */
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    /** Probabilidade de criação inicial de coelhos em cada célula. */
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;
    /** Probabilidade de criação inicial de plantas em cada célula. */
    private static final double PLANT_CREATION_PROBABILITY = 0.10;
    /** Probabilidade de criação inicial de lobos em cada célula. */
    private static final double WOLF_CREATION_PROBABILITY = 0.06;
    /** Probabilidade de criação inicial de águias em cada célula. */
    private static final double EAGLE_CREATION_PROBABILITY = 0.012;
    /** Probabilidade de criação inicial de caçadores em cada célula. */
    private static final double HUNTER_CREATION_PROBABILITY = 0.012;
    


    /** Lista de animais ativos na simulação. */
    private final ArrayList<Animal> animals;
    /** Campo da simulação (grade com objetos e terrenos). */
    private Field field;
    /** Número do passo atual da simulação. */
    private int step;
    /** Interface gráfica para exibir o estado da simulação. */
    private SimulatorView view;
    /** Gerador de números aleatórios para inicialização e eventos. */
    private final Random rand = new Random();

    /**
     * Construtor padrão: cria um simulador com dimensões padrão.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Construtor: cria um simulador com dimensões especificadas.
     * Se dimensões inválidas forem fornecidas, usa valores padrão.
     *
     * @param depth número de linhas do campo.
     * @param width número de colunas do campo.
     */
    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        animals = new ArrayList<>();
        field = new Field(depth, width);

        // Configura a interface gráfica e cores das entidades
        view = new SimulatorView(depth, width);
        view.setColor(Fox.class, Color.orange);
        view.setColor(Rabbit.class, Color.lightGray);
        view.setColor(Lobo.class, Color.darkGray);
        view.setColor(Aguia.class, Color.yellow);
        view.setColor(Cobra.class, Color.black);
        view.setColor(Cacador.class, Color.cyan);
        view.setColor(Planta.ALECRIM.getClass(), Color.green);
        view.setColor(Planta.SALVIA.getClass(), Color.green);

        reset();
    }

    /**
     * Executa uma simulação longa (500 passos).
     */
    public void runLongSimulation() {
        simulate(500);
    }

    /**
     * Executa a simulação por um número específico de passos.
     * Interrompe se a simulação deixar de ser viável.
     *
     * @param numSteps número de passos a executar.
     */
    public void simulate(int numSteps) {
        for (int i = 1; i <= numSteps && view.isViable(field); i++) {
            simulateOneStep();
        }
    }

    /**
     * Executa um único passo da simulação:
     * - Incrementa o contador de passos.
     * - Cada animal age (move, come, reproduz).
     * - Remove animais mortos.
     * - Adiciona novos animais nascidos.
     * - Atualiza a interface gráfica.
     */
    public void simulateOneStep() {
        step++;
        List<Animal> newAnimals = new ArrayList<>();

        for (Iterator<Animal> iter = animals.iterator(); iter.hasNext();) {
            Animal animal = iter.next();
            animal.act(newAnimals);
            if (!animal.isAlive()) {
                iter.remove();
            }
        }

        animals.addAll(newAnimals);
        view.showStatus(step, field);
    }

    /**
     * Reinicia a simulação:
     * - Zera o contador de passos.
     * - Limpa lista de animais e campo.
     * - Popula o campo com animais e plantas iniciais.
     * - Atualiza a interface gráfica.
     */
    public void reset() {
        step = 0;
        animals.clear();
        field.clear();
        populate(field);
        view.showStatus(step, field);
    }

    /**
     * Popula o campo inicial com raposas, coelhos e plantas.
     * Cada célula tem uma probabilidade de receber uma entidade.
     *
     * @param field campo a ser populado.
     */
    private void populate(Field field) {
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Location location = new Location(row, col);

                if (rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    animals.add(new Fox(true, field, location));
                }
                if (rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    animals.add(new Rabbit(true, field, location));
                }
                if (rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    animals.add(new Rabbit(true, field, location));
                }
                if (rand.nextDouble() <= WOLF_CREATION_PROBABILITY) {
                    animals.add(new Lobo(true, field, location));
                }
                if (rand.nextDouble() <= EAGLE_CREATION_PROBABILITY) {
                    animals.add(new Aguia(true, field, location));
                }
                if (rand.nextDouble() <= HUNTER_CREATION_PROBABILITY) {
                    animals.add(new Cacador(true, field, location));
                }
                
                if (rand.nextDouble() <= PLANT_CREATION_PROBABILITY) {
                    Planta plantaTipo;

                if (rand.nextBoolean()) {
                    plantaTipo = Planta.ALECRIM;
                } else {
                    plantaTipo = Planta.SALVIA;
                }       
                field.place(plantaTipo, location);
                }
            }
        }
    }
}
