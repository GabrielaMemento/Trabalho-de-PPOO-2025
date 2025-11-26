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
 * - Popular o campo inicial com raposas, coelhos, lobos e plantas.
 * - Coordenar a interação entre animais e plantas a cada passo.
 * - Atualizar a interface gráfica (SimulatorView) com o estado atual.
 * - Gerenciar controles de velocidade, pausa e passo único.
 *
 * @author Base: Barnes & Kolling
 * @author Modificações: Leonardo Elias Rodrigues
 * @version 2025-11 (comentado e revisado para todas as fases)
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
    private static final double WOLF_CREATION_PROBABILITY = 0.015;

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
    /** Controlador de condições climáticas. */
    private ControladorClima controladorClima;
    /** Controlador de velocidade da simulação. */
    private ControladorVelocidade controladorVelocidade;
    /** Estado de pausa da simulação. */
    private boolean pausado = false;
    /** Flag para execução de passo único. */
    private boolean passoUnico = false;

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
        controladorClima = new ControladorClima();

        // Configurar a interface gráfica
        view = new SimulatorView(depth, width);
        
        // Configurar listeners da GUI
        view.setSimuladorListener(new SimulatorView.SimuladorListener() {
            @Override
            public void onPausaChanged(boolean pausado) {
                setPausado(pausado);
            }
            
            @Override
            public void onReset() {
                reset();
            }
        
            @Override
            public void onPassoUnico() {
                passoUnico();
            }
        });

        // Configurar controlador de velocidade
        controladorVelocidade = new ControladorVelocidade(e -> {
            if (!pausado || passoUnico) {
                simulateOneStep();
                passoUnico = false;
            }
        });

        // Configurar cores das entidades
        view.setColor(Fox.class, Color.blue);
        view.setColor(Rabbit.class, Color.orange);
        view.setColor(Alecrim.class, Color.green);
        view.setColor(Salvia.class, Color.magenta);
        view.setColor(Lobo.class, Color.red);

        reset();
        controladorVelocidade.iniciar();
    }

    /**
     * Define o estado de pausa da simulação.
     *
     * @param pausado true para pausar, false para continuar
     */
    public void setPausado(boolean pausado) {
        this.pausado = pausado;
        if (pausado) {
            controladorVelocidade.parar();
        } else {
            controladorVelocidade.iniciar();
        }
    }

    /**
     * Executa um único passo quando solicitado pela interface.
     */
    public void passoUnico() {
        this.passoUnico = true;
        controladorVelocidade.parar();
        // O passo será executado no próximo tick do timer
    }

    /**
     * Atualiza a velocidade baseado no slider da GUI.
     */
    private void atualizarVelocidade() {
        int velocidadeView = view.getVelocidade();
        controladorVelocidade.setVelocidade(velocidadeView);
    }

    /**
     * Executa uma simulação longa (500 passos).
     * Agora controlada pelo timer com interface gráfica.
     */
    public void runLongSimulation() {
        // A simulação é controlada pelo timer e interface gráfica
        System.out.println("Simulação longa iniciada. Use os controles para gerenciar.");
    }

    /**
     * Executa a simulação por um número específico de passos.
     * Interrompe se a simulação deixar de ser viável.
     * Mantido para compatibilidade com versões anteriores.
     *
     * @param numSteps número de passos a executar.
     */
    public void simulate(int numSteps) {
        // Método mantido para compatibilidade, mas agora usa timer
        for (int i = 1; i <= numSteps && view.isViable(field); i++) {
            simulateOneStep();
            try {
                Thread.sleep(1000 / controladorVelocidade.getVelocidade());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Executa um único passo da simulação:
     * - Incrementa o contador de passos.
     * - Atualiza condições climáticas.
     * - Cada animal age (move, come, reproduz).
     * - Remove animais mortos.
     * - Adiciona novos animais nascidos.
     * - Atualiza a interface gráfica.
     */
    public void simulateOneStep() {
        // Atualizar velocidade se necessário
        atualizarVelocidade();
        
        step++;
        controladorClima.atualizar();
        
        // Atualizar informações na GUI
        view.setClima(controladorClima.getClimaAtual().toString());
        atualizarEstatisticasDetalhadas();
        
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
     * Atualiza estatísticas detalhadas na GUI lateral.
     */
    private void atualizarEstatisticasDetalhadas() {
        // Contar cada tipo de entidade
        int coelhos = 0, raposas = 0, lobos = 0, plantas = 0;
        
        for (Animal animal : animals) {
            if (animal instanceof Rabbit) coelhos++;
            else if (animal instanceof Fox) raposas++;
            else if (animal instanceof Lobo) lobos++;
        }
        
        // Contar plantas (precisamos varrer o campo)
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Object obj = field.getObjectAt(new Location(row, col));
                if (obj instanceof Planta) {
                    plantas++;
                }
            }
        }
        
        String estatisticas = String.format(
            "Coelhos: %d<br>Raposas: %d<br>Lobos: %d<br>Plantas: %d<br>Passo: %d",
            coelhos, raposas, lobos, plantas, step
        );
        
        view.setEstatisticasDetalhadas(estatisticas);
    }

    /**
     * Reinicia a simulação:
     * - Zera o contador de passos.
     * - Limpa lista de animais e campo.
     * - Popula o campo com animais e plantas iniciais.
     * - Atualiza a interface gráfica.
     * - Reinicia controles de velocidade e pausa.
     */
    public void reset() {
        controladorVelocidade.parar();
        step = 0;
        animals.clear();
        field.clear();
        populate(field);
        view.showStatus(step, field);
        pausado = false;
        controladorVelocidade.iniciar();
    }

    /**
     * Popula o campo inicial com raposas, coelhos, lobos e plantas.
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
                    animals.add(new Fox(true, field, location, controladorClima));
                }
                if (rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    animals.add(new Rabbit(true, field, location, controladorClima));
                }
                if (rand.nextDouble() <= WOLF_CREATION_PROBABILITY) {
                    animals.add(new Lobo(true, field, location, controladorClima));
                }
                if (rand.nextDouble() <= PLANT_CREATION_PROBABILITY) {
                    if (rand.nextBoolean()) {
                        field.place(new Alecrim(), location);
                    } else {
                        field.place(new Salvia(), location);
                    }
                }
            }
        }
    }
}