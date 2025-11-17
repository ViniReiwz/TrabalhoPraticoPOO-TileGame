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
    private int num_to_collect = -1;

    // Heroi da fase
    public Hero heroi;

    public Fase(ArrayList<Personagem> personagens)
    {
        this.personagens = personagens;

        this.coletaveis = new ArrayList<Coletavel>();
        
        for(int i = 1; i < Consts.MUNDO_ALTURA-1; i++)
        {
            for(int j = 1; j < Consts.MUNDO_LARGURA-1; j++)
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

    public ArrayList<Coletavel> getColetaveis() 
    {
        return coletaveis;
    }
    
    public int getNum_to_collect() 
    {
        return num_to_collect;
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

    public void updatePoints()
    {
        this.num_to_collect = this.coletaveis.size();
    }

    public void addHero(Hero hero)
    {
        this.heroi = hero;
    }

    public void addPers(Personagem pers)
    {
        this.personagens.add(pers);
    }


}
