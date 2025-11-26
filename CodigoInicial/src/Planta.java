/**
 * Classe abstrata para plantas no campo.
 * Subclasses específicas (Alecrim, Sálvia) definem seus efeitos.
 */
public abstract class Planta implements Comestivel {
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

  // Implementação da interface Comestivel (@autor Leonardo Elias Rodrigues)
    @Override
    public int getValorNutricional() {
        return getValorAlimento();
    }
    
    @Override
    public boolean podeSerComido() {
        return isAlive();
    }
    
    @Override
    public void foiComido() {
        setDead();
    }
}
    

