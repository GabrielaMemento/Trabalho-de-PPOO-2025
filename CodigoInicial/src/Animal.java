import java.util.List;
import java.util.Random;

/**
 * Classe base abstrata para todos os animais da simulação.
 *
 * Responsabilidades:
 * - Manter estado comum dos animais (idade, se está vivo, localização, nível de comida, campo).
 * - Oferecer operações básicas sobre ciclo de vida (morrer, mover-se).
 * - Definir a interface (métodos abstratos) que cada espécie concreta deve implementar.
 *
 * Contratos que as subclasses devem cumprir:
 * - {@link #act(List)}: executar as ações do animal em um passo (mover, comer, reproduzir, etc.).
 * - {@link #incrementAge()}: envelhecer e verificar morte por velhice.
 * - {@link #breed()}: quantidade de filhotes gerados no passo (0 se não reproduziu).
 * - {@link #canBreed()}: regra de aptidão para reprodução (por idade, energia, estação, etc.).
 *
 * Convenções importantes:
 * - O campo armazena um único objeto por célula.
 * - Sempre que o animal muda de célula, a célula anterior é liberada e a nova célula recebe o animal.
 * - Ao morrer, o animal remove-se do campo (libera a célula), e sua localização/campo são anulados.
 *
 * Observação:
 * - Esta classe não define lógica específica de espécie (como dieta ou terreno permitido).
 *   Isso deve ser implementado nas subclasses (e.g., Rabbit, Fox).
 *
 * @author Base: Barnes & Kolling
 * @version 2002-04-23 (comentado e revisado 2025-11)
 */
public abstract class Animal {
    /** Idade atual do animal (em passos de simulação). */
    public int age;
    /** Indica se o animal está vivo. Quando false, não deve mais agir ou aparecer no campo. */
    public boolean alive;
    /** Localização atual do animal no campo (linha, coluna). */
    public Location location;
    /** Nível de comida/energia. Semântica definida pela espécie (pode morrer se chegar a zero). */
    public int foodLevel;
    /** Referência ao campo (grade) onde o animal está inserido. */
    public Field field;
    /** Gerador de números aleatórios disponível para subclasses. */
    public static final Random rand = new Random();

    /**
     * Construtor padrão para um animal.
     * Inicializa como vivo, com idade zero, e posiciona no campo.
     *
     * @param field campo da simulação onde o animal será colocado.
     * @param location posição inicial do animal.
     */
    public Animal(Field field, Location location) {
        this.age = 0;
        this.alive = true;
        this.field = field;
        setLocation(location);
    }

    /**
     * Executa as ações do animal em um passo da simulação.
     * As ações típicas incluem: envelhecer, movimentar-se, alimentar-se, reproduzir, etc.
     *
     * @param newAnimals lista onde novos animais nascidos devem ser adicionados.
     */
    public abstract void act(List<Animal> newAnimals);

    /**
     * Incrementa a idade do animal e processa efeitos de velhice
     * (e.g., morrer se ultrapassar a idade máxima da espécie).
     */
    public abstract void incrementAge();

    /**
     * Calcula quantos filhotes serão gerados neste passo.
     * Deve respeitar regras de reprodução da espécie (probabilidade, estação, energia).
     *
     * @return número de novos filhotes (0 se não reproduziu).
     */
    public abstract int breed();

    /**
     * Indica se o animal está apto a reproduzir neste momento.
     * Normalmente baseado na idade mínima, mas pode incluir outras regras.
     *
     * @return true se apto a reproduzir; false caso contrário.
     */
    public abstract boolean canBreed();

    /**
     * @return true se o animal ainda está vivo; false se já morreu.
     */
    public boolean isAlive() { return alive; }

    /**
     * Define o animal como morto, removendo-o do campo e limpando suas referências.
     * Efeitos colaterais:
     * - Libera a célula atual no campo (se havia localização válida).
     * - Anula referências a location e field, prevenindo uso posterior indevido.
     */
    public void setDead() {
        alive = false;
        // Se o animal estava posicionado no campo, remover da célula atual.
        if (location != null && field != null) {
            field.clear(location);
        }
        // Zera as referências para evitar que a lógica subsequente use um animal "fantasma".
        location = null;
        field = null;
    }

    /**
     * @return a localização atual do animal no campo (pode ser null se morto).
     */
    public Location getLocation() { return location; }

    /**
     * Move o animal para uma nova localização no campo.
     * Efeitos colaterais:
     * - Libera a célula anterior (se houver).
     * - Ocupa a nova célula com este animal.
     *
     * Pré-condições:
     * - newLocation deve ser uma célula válida dentro dos limites do campo.
     * - A regra de terreno permitido (se existir) deve ser verificada pela subclasse antes de chamar este método.
     *
     * @param newLocation nova posição desejada.
     */
    public void setLocation(Location newLocation) {
        // Remove o animal da célula anterior, se estava colocado.
        if (location != null && field != null) {
            field.clear(location);
        }
        // Atualiza a referência de localização.
        location = newLocation;
        // Coloca este animal na nova célula, se houver campo e localização válidos.
        if (field != null && location != null) {
            field.place(this, location);
        }
    }

    /**
     * @return o campo ao qual este animal pertence (pode ser null se morto).
     */
    public Field getField() {
        return field;
    }
}
