import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.awt.Color;

/**
 * Classe principal do simulador predador-presa.
 * 
 * Este simulador coordena a execução da simulação ecológica,
 * controlando o tempo, o campo, os animais e a interface gráfica.
 * Ele gerencia a criação inicial dos animais, os ciclos de vida,
 * reprodução, movimentação e morte, além de atualizar a visualização
 * a cada passo
 * 
 * Inicialmente, o simulador trabalha com duas espécies: raposas
 * (predadores) e coelhos (presas). Novas espécies podem ser adicionadas
 * facilmente, desde que herdem da classe {@code Animal} e implementem
 * o método {@code act}
 * 
 * O campo é representado por duas instâncias de {@link Field}:
 * uma para o estado atual e outra para o próximo estado. Isso evita
 * conflitos durante a movimentação dos animais
 * 
 * A interface gráfica {@link SimulatorView} mostra o campo e
 * as estatísticas populacionais em tempo real
 * 

 */
public class Simulator {
    //Largura padrão do campo (número de colunas)
    private static final int DEFAULT_WIDTH = 50;
    /** Profundidade padrão do campo (número de linhas). */
    private static final int DEFAULT_DEPTH = 50;

    /** Probabilidade de criação de uma raposa em cada célula. */
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    /** Probabilidade de criação de um coelho em cada célula. */
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;

    /** Lista de todos os animais vivos no campo. */
    private List<Animal> animals;
    /** Lista temporária para armazenar animais recém-nascidos. */
    private List<Animal> newAnimals;

    /** Campo atual da simulação. */
    private Field field;
    /** Campo auxiliar para construir o próximo estado. */
    private Field updatedField;

    /** Contador de passos da simulação. */
    private int step;

    /** Interface gráfica que mostra o campo e as populações. */
    private SimulatorView view;

    /**
     * Constrói o simulador com dimensões padrão.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Constrói o simulador com dimensões personalizadas.
     * 
     * Se os valores forem inválidos (menores ou iguais a zero),
     * o simulador usará os valores padrão
     * 
     * @param depth profundidade do campo.
     * @param width largura do campo.
     */
    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            System.out.println("Dimensões inválidas. Usando valores padrão.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        animals = new ArrayList<>();
        newAnimals = new ArrayList<>();
        field = new Field(depth, width);
        updatedField = new Field(depth, width);

        // Configura a interface gráfica e define cores para cada espécie
        view = new SimulatorView(depth, width);
        view.setColor(Fox.class, Color.blue);
        view.setColor(Rabbit.class, Color.orange);

        // Inicializa o campo com animais
        reset();
    }

    /**
     * Executa a simulação por 500 passos.
     * Ideal para testes longos ou observação de padrões populacionais.
     */
    public void runLongSimulation() {
        simulate(500);
    }

    /**
     * Executa a simulação por um número definido de passos.
     * 
     * <p>Se a simulação deixar de ser viável (apenas uma espécie viva),
     * ela será interrompida antes do fim.</p>
     * 
     * @param numSteps número de passos a executar.
     */
    public void simulate(int numSteps) {
        for (int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
        }
    }

    /**
     * Executa um único passo da simulação.
     * 
     * <p>Todos os animais vivos executam sua ação principal
     * (definida em {@code Animal.act}). Animais mortos são removidos
     * da lista. Novos animais nascidos são adicionados ao final do passo.</p>
     */
    public void simulateOneStep() {
        step++;
        newAnimals.clear();

        for (Iterator<Animal> iter = animals.iterator(); iter.hasNext();) {
            Animal animal = iter.next();
            if (animal.isAlive()) {
                animal.act(field, updatedField, newAnimals);
            } else {
                iter.remove(); // remove mortos
            }
        }

        animals.addAll(newAnimals);

        // Troca os campos para preparar o próximo passo
        Field temp = field;
        field = updatedField;
        updatedField = temp;
        updatedField.clear();

        // Atualiza a interface gráfica com o novo estado
        view.showStatus(step, field);
    }

    /**
     * Reseta a simulação para o estado inicial.
     * 
     * Limpa os campos e popula com animais novos
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
     * Popula o campo com raposas e coelhos.
     * 
     * Cada célula tem chance independente de receber uma raposa
     * ou um coelho. Se uma raposa for criada, a célula fica ocupada
     * e o coelho só será criado se a célula estiver livre
     * 
     * Esse método pode ser expandido para incluir outras espécies
     * como lobos, cobras, águias, caçadores ou plantas
     * 
     * @param field campo a ser populado.
     */
    private void populate(Field field) {
        Random rand = new Random();
        field.clear();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {

                // Tenta criar raposa
                if (rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Fox fox = new Fox(true);
                    animals.add(fox);
                    fox.setLocation(row, col);
                    field.place(fox, row, col);
                }

                // Tenta criar coelho, se a célula estiver livre
                if (field.getObjectAt(row, col) == null &&
                    rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Rabbit rabbit = new Rabbit(true);
                    animals.add(rabbit);
                    rabbit.setLocation(row, col);
                    field.place(rabbit, row, col);
                }
                // System.out.println("Criando coelho em (" + row + "," + col + ")");
               
            }
            
        }

        // Embaralha a ordem dos animais para evitar viés de execução
        Collections.shuffle(animals);

        
    }
}
