package Modelo;

public class ParedeV extends Personagem {

    public ParedeV(String NomeImagem, int linha, int coluna){
        super(NomeImagem, linha, coluna);

        this.bTransponivel = false;
        this.bMortal = false;
    }

    @Override
    public void autoDesenho(){
        super.autoDesenho();
    }

    
}

