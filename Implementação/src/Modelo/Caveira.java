package Modelo;

public class Caveira extends Coletavel{
    
    
    public Caveira(String sNomeImagePNG, int linha, int coluna) {
        super(sNomeImagePNG, linha, coluna);
        this.bTransponivel = true;
        this.bMortal = false;
        this.val = 0;
    }
}
