package Modelo;

public class Coletavel extends Personagem {

    public Coletavel(String NomeImagem, int linha, int coluna){
        super(NomeImagem, linha, coluna);

        this.bTransponivel = true;
        this.bMortal = false;
    }
}
