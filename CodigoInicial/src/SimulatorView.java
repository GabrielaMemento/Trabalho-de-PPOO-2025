import java.awt.*;
import javax.swing.*;
import java.util.HashMap;

/**
 * Classe responsável pela visualização gráfica do simulador.
 * 
 * {@code SimulatorView} exibe o campo da simulação em uma janela,
 * mostrando cada célula com uma cor correspondente ao conteúdo:
 * animais, plantas ou espaços vazios
 *
 * Além disso, apresenta informações sobre o passo atual da simulação
 * e estatísticas populacionais. As cores de cada espécie podem ser
 * configuradas dinamicamente através do método {@link #setColor}
 *
 * Esta classe utiliza uma classe interna {@code FieldView} para
 * desenhar o campo em forma de grade
 *
 */
public class SimulatorView extends JFrame {
    // Cor usada para células vazias. 
    private static final Color EMPTY_COLOR = Color.white;
    // Cor usada para classes sem cor definida
    private static final Color UNKNOWN_COLOR = Color.gray;

    // Prefixo do texto que mostra o passo atual
    private final String STEP_PREFIX = "Passo: ";
    // Prefixo do texto que mostra a população
    private final String POPULATION_PREFIX = "População: ";

    // Label que mostra o passo atual
    private JLabel stepLabel;
    // Label que mostra estatísticas populacionais
    private JLabel population;
    // Componente gráfico que desenha o campo
    private FieldView fieldView;

    // Mapa que associa classes de animais às suas cores
    private HashMap<Class, Color> colors;
    // Objeto que calcula e armazena estatísticas da simulação
    private FieldStats stats;

    /**
     * Cria uma visualização gráfica para o campo da simulação
     *
     * @param height altura do campo (número de linhas)
     * @param width largura do campo (número de colunas)
     */
    public SimulatorView(int height, int width) {
        stats = new FieldStats();
        colors = new HashMap<>();

        setTitle("Simulação Predador-Presa");
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);

        setLocation(100, 50);

        fieldView = new FieldView(height, width);

        Container contents = getContentPane();
        contents.add(stepLabel, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    /**
     * Define a cor a ser usada para uma determinada classe de animal.
     *
     * @param animalClass classe do animal.
     * @param color cor associada.
     */
    public void setColor(Class animalClass, Color color) {
        colors.put(animalClass, color);
    }

    /**
     * Retorna a cor associada a uma classe de animal.
     * Caso não exista cor definida, retorna {@link #UNKNOWN_COLOR}.
     *
     * @param animalClass classe do animal.
     * @return cor correspondente.
     */
    private Color getColor(Class animalClass) {
        Color col = colors.get(animalClass);
        return (col == null) ? UNKNOWN_COLOR : col;
    }

    /**
     * Atualiza a interface gráfica com o estado atual do campo.
     *
     * @param step número do passo atual da simulação.
     * @param field campo da simulação.
     */
    public void showStatus(int step, Field field) {
        if(!isVisible())
            setVisible(true);

        stepLabel.setText(STEP_PREFIX + step);

        stats.reset();
        fieldView.preparePaint();

        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Object animal = field.getObjectAt(row, col);
                if(animal != null) {
                    stats.incrementCount(animal.getClass());
                    fieldView.drawMark(col, row, getColor(animal.getClass()));
                } else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        stats.countFinished();

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Verifica se a simulação ainda é viável.
     * 
     * @param field campo da simulação.
     * @return true se houver mais de uma espécie viva.
     */
    public boolean isViable(Field field) {
        return stats.isViable(field);
    }

    /**
     * Classe interna responsável por desenhar o campo em forma de grade.
     * Cada célula é representada por um retângulo colorido.
     */
    private class FieldView extends JPanel {
        /** Fator de escala para o tamanho das células. */
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        private Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Cria um novo componente FieldView.
         *
         * @param height altura do campo.
         * @param width largura do campo.
         */
        public FieldView(int height, int width) {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /** Retorna o tamanho preferido do componente. */
        public Dimension getPreferredSize() {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                                 gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepara o componente para uma nova rodada de pintura.
         * Recalcula os fatores de escala caso o tamanho da janela tenha mudado
         */
        public void preparePaint() {
            if(! size.equals(getSize())) {
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) xScale = GRID_VIEW_SCALING_FACTOR;

                yScale = size.height / gridHeight;
                if(yScale < 1) yScale = GRID_VIEW_SCALING_FACTOR;
            }
        }

        /**
         * Desenha uma célula do campo em uma cor específica
         *
         * @param x coordenada horizontal.
         * @param y coordenada vertical
         * @param color cor da célula
         */
        public void drawMark(int x, int y, Color color) {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
        }

        /**
         * Redesenha o componente copiando a imagem interna para a tela
         */
        public void paintComponent(Graphics g) {
            if(fieldImage != null) {
                g.drawImage(fieldImage, 0, 0, null);
            }
        }
    }
}
