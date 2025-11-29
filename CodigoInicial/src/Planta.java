public enum Planta {
    // Definindo os valores de alimento em cada constante
    ALECRIM(5),
    SALVIA(3);

    private final int valorAlimento;

    /** Construtor do enum. */
    Planta(int valorAlimento) {
        this.valorAlimento = valorAlimento;
    }

    /** Retorna o valor de alimento da constante. */
    public int getValorAlimento() {
        return valorAlimento;
    }
}