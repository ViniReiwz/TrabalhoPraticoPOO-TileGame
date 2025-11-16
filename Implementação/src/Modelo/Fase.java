package Modelo;

import java.util.ArrayList;

import Auxiliar.Consts;

// Classe onde tudo relacionado à uma fase deve ser feito
// A ideia é em Tela.java, chamar o construtor e adicionar os personagens
// Todo o manuseio interno (deletar personagem, adicionar, mudar estado das paredes, posição das coisas, etc é pra ser feito nessa classe)
public class Fase 
{
    // ArrayList com todos os personagens não-coletaveis
    private ArrayList<Personagem> personagens;

    // ArrayList com os coletáveis
    private ArrayList<Coletavel> coletaveis;

    // Número de coletaveis (ao chegar em 0 a fase deve acabar - Não implementado ainda)
    private int num_to_collect;

    // Heroi da fase
    public Hero heroi;

    public Fase(ArrayList<Personagem> personagens)
    {
        this.personagens = personagens;
        // this.heroi = (Hero)this.personagens.get(0);

        this.coletaveis = new ArrayList<Coletavel>();
        
        for(int i = 0; i < Consts.MUNDO_ALTURA; i++)
        {
            for(int j = 0; j < Consts.MUNDO_LARGURA; j++)
            {
                Coletavel c = new Coletavel("explosao.png", i, j);
                this.coletaveis.add(c);
            }
        }
    }

    // Retorna o array list com os personagens
    public ArrayList<Personagem> getPersonagens() 
    {
        return personagens;
    }

    // Spawna todos os personagens
    public void spawnAllPers()
    {
        for(Personagem p : new ArrayList<>(this.personagens))
        {
            p.autoDesenho();
        }
    }

    // Spawna todos os coletaveis
    public void spawnAllColl()
    {
        for (Coletavel c : new ArrayList<>(this.coletaveis))
        {
            c.autoDesenho();
        }
    }
}
