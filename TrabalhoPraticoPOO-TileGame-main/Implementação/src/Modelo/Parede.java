package Modelo;

public class Parede extends Personagem {

    public Parede(String NomeImagem, int linha, int coluna){
        super(NomeImagem, linha, coluna);

        this.bTransponivel = false;
        this.bMortal = false;
    }

    @Override
    public void autoDesenho(){
        super.autoDesenho();
    }

    
}