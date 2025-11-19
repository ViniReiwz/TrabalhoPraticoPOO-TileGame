package Modelo;

public class Caveira extends Coletavel{
    
    
    public Caveira(int linha, int coluna) {
        super("caveira.png", linha, coluna);
        this.bTransponivel = true;
        this.bMortal = true;
        this.val = 0;
    }
}
