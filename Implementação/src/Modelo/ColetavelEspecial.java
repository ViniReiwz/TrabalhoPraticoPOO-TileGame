package Modelo;

public class ColetavelEspecial extends Coletavel 
{
    public ColetavelEspecial(String NomeImagem, int linha, int coluna)
    {
        super(NomeImagem,linha,coluna);
        this.val = 100;
    }
}
