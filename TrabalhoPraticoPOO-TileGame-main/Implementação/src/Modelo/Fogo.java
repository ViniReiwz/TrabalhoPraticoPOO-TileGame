package Modelo;

import Auxiliar.Desenho;

public class Fogo extends Personagem{
            
    public Fogo(String sNomeImagePNG, int linha, int coluna) {
        super(sNomeImagePNG, linha, coluna);
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
        if(!this.moveRight())
            Desenho.acessoATelaDoJogo().removePersonagem(this);
    }
    
}
