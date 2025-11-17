package Modelo;

public class ParedeH extends Personagem {

    public ParedeH(String NomeImagem, int linha, int coluna){
        super(NomeImagem, linha, coluna);

        this.bTransponivel = false;
        this.bMortal = false;
    }

    @Override
    public void autoDesenho(){
        super.autoDesenho();
    }

    
}

