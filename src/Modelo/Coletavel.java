package Modelo;

public class Coletavel extends Personagem {

    protected int val = 10;

    public Coletavel(String NomeImagem, int linha, int coluna){
        super(NomeImagem, linha, coluna);

        this.bTransponivel = true;
        this.bMortal = false;
    }
}
