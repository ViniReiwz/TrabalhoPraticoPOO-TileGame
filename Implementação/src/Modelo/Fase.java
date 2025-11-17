package Modelo;

import java.util.ArrayList;
import java.io.Serializable;

// Classe onde tudo relacionado à uma fase deve ser feito
// A ideia é em Tela.java, chamar o construtor e adicionar os personagens
// Todo o manuseio interno (deletar personagem, adicionar, mudar estado das paredes, posição das coisas, etc é pra ser feito nessa classe)
public class Fase implements Serializable
{
    // ArrayList com todos os personagens não-coletaveis
    private ArrayList<Personagem> personagens = new ArrayList<>();

    // ArrayList com os coletáveis
    private ArrayList<Coletavel> coletaveis = new ArrayList<>();

    // Número de coletaveis (ao chegar em 0 a fase deve acabar - Não implementado ainda)
    private int num_to_collect = -1;

    // Heroi da fase
    public Hero heroi;

    private int pontos = 0;

    public Fase()
    {   
        Coletavel c = new Coletavel("explosao.png", 5, 6);
        this.coletaveis.add(c);
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
        this.pontos++;
    }

    public void addHero(Hero hero)
    {   
        hero.setPosicao(4, 7);
        this.heroi = hero;
    }

    public void addPers(Personagem pers)
    {
        this.personagens.add(pers);
    }


}
