import java.awt.*;
import javax.swing.*;
import java.util.HashMap;

/**
 * Visualização gráfica da grade da simulação.
 *
 * - Mostra cada célula do campo como um retângulo colorido.
 * - Usa cores diferentes para cada espécie/planta (definidas com setColor).
 * - Mostra o passo atual e estatísticas populacionais agregadas.
 * - Também desenha tipos de terreno (rios, montanhas, cavernas, tocas, vegetação, campo aberto).
 *
 * Trabalha em conjunto com FieldStats para calcular e exibir populações.
 *
 * @author David J. Barnes
 * @author Michael Kolling
 * @version 2002-04-23 (revisado 2025-11 para visualizar terrenos)
 */
public class SimulatorView extends JFrame {
    /** Cor para células vazias (não utilizada quando desenhamos terreno). */
    //private static final Color EMPTY_COLOR = Color.white;
    /** Cor para objetos sem cor definida explicitamente. */
    private static final Color UNKNOWN_COLOR = Color.gray;

    // Cores padrão para terrenos
    private static final Color MOUNTAIN_COLOR = Color.darkGray;
    private static final Color RIVER_COLOR = Color.blue;
    private static final Color BURROW_COLOR = new Color(139,69,19); // marrom
    private static final Color CAVE_COLOR = Color.black;
    private static final Color VEGETATION_COLOR = Color.green;
    private static final Color PLAIN_COLOR = Color.lightGray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private JLabel stepLabel, population;
    private FieldView fieldView;

    /** Mapa de cores por classe (Animal, Plant). */
    private HashMap<Class<?>, Color> colors;
    /** Estatísticas populacionais do campo. */
    private FieldStats stats;

    /**
     * Cria a janela de visualização com dimensões específicas.
     *
     * @param height número de linhas da grade.
     * @param width número de colunas da grade.
     */
    public SimulatorView(int height, int width) {
        stats = new FieldStats();
        colors = new HashMap<Class<?>, Color>();

        setTitle("Fox, Rabbit and Ecosystem Simulation");
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
     * Define uma cor para uma classe de entidade (animal ou planta).
     *
     * @param entityClass classe a ser colorida.
     * @param color cor para exibir.
     */
    public void setColor(Class<?> entityClass, Color color) {
        colors.put(entityClass, color);
    }

    /**
     * Recupera a cor associada a uma classe. Se não houver, usa cinza.
     *
     * @param entityClass classe desejada.
     * @return cor mapeada ou UNKNOWN_COLOR.
     */
    private Color getColor(Class<?> entityClass) {
        Color col = colors.get(entityClass);
        if(col == null) {
            return UNKNOWN_COLOR;
        }
        else {
            return col;
        }
    }

    /**
     * Exibe o estado atual do campo:
     * - Atualiza o rótulo com o número do passo.
     * - Percorre todas as células da grade.
     * - Desenha objetos com suas cores.
     * - Se célula estiver vazia, desenha a cor do terreno.
     * - Atualiza estatísticas populacionais.
     *
     * @param step passo atual da simulação.
     * @param field campo com objetos e terrenos.
     */
    public void showStatus(int step, Field field) {
        if(!isVisible())
            setVisible(true);

        stepLabel.setText(STEP_PREFIX + step);

        stats.reset();
        fieldView.preparePaint();

        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Location loc = new Location(row, col);
                Object obj = field.getObjectAt(loc);

                if(obj != null) {
                    stats.incrementCount(obj.getClass());
                    fieldView.drawMark(col, row, getColor(obj.getClass()));
                }
                else {
                    // Célula vazia: desenha o tipo de terreno
                    Terreno terrain = field.getTerrainAt(loc);
                    fieldView.drawMark(col, row, getTerrainColor(terrain));
                }
            }
        }

        // Compatibilidade com versões anteriores (opcional).
        // Se sua FieldStats não possui countFinished(), remova esta chamada
        // ou implemente countFinished() como countsValid = true;
        // stats.countFinished();

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Mapeia o tipo de terreno para sua cor padrão de visualização.
     *
     * @param terrain tipo de terreno da célula.
     * @return cor usada para pintar o terreno.
     */
    private Color getTerrainColor(Terreno terrain) {
        switch(terrain) {
            case MOUNTAIN: return MOUNTAIN_COLOR;
            case RIVER: return RIVER_COLOR;
            case BURROW: return BURROW_COLOR;
            case CAVE: return CAVE_COLOR;
            case DENSE_VEGETATION: return VEGETATION_COLOR;
            case PLAIN:
            default: return PLAIN_COLOR;
        }
    }

    /**
     * Verifica se a simulação é viável: deve haver mais de uma espécie presente.
     *
     * @param field campo atual.
     * @return true se mais de um contador tiver valores > 0.
     */
    public boolean isViable(Field field) {
        return stats.isViable(field);
    }

    /**
     * Painel interno responsável por desenhar a grade do campo.
     * Mantém uma imagem interna para evitar flicker e melhorar performance.
     */
    private class FieldView extends JPanel {
        /** Fator de escala padrão para cada célula da grade. */
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Constrói a visualização da grade com altura e largura especificadas.
         *
         * @param height número de linhas.
         * @param width número de colunas.
         */
        public FieldView(int height, int width) {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tamanho preferido do painel, com base na escala e tamanho da grade.
         */
        public Dimension getPreferredSize() {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                                 gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepara a pintura, reconstruindo a imagem interna se o painel foi
         * redimensionado e recalculando a escala de desenho.
         */
        public void preparePaint() {
            if(! size.equals(getSize())) {
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if(yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }

        /**
         * Desenha um retângulo representando uma célula da grade na cor fornecida.
         *
         * @param x coluna da célula.
         * @param y linha da célula.
         * @param color cor a ser usada na pintura.
         */
        public void drawMark(int x, int y, Color color) {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
        }

        /**
         * Renderiza a imagem interna no painel (double-buffering).
         */
        public void paintComponent(Graphics g) {
            if(fieldImage != null) {
                g.drawImage(fieldImage, 0, 0, null);
            }
        }
    }
}
