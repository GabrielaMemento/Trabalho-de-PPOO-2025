/**
 * Classe abstrata para plantas no campo.
 * Subclasses específicas (Alecrim, Sálvia) definem seus efeitos.
 */
public abstract class Planta {
    private boolean taVivo;

    public Planta() {
        this.taVivo = true;
    }

    public boolean isAlive() {
        return taVivo;
    }

    public void setDead() {
        taVivo = false;
    }

    /** Cada planta define o valor de alimento que fornece. */
    public abstract int getValorAlimento();

    /** Nome da planta (para estatísticas e visualização). */
    public abstract String getName();
 
    
}
