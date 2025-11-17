package Modelo;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import Auxiliar.Consts;

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

    private int multiplier = 1;

    // Sistema de pontuação
    private int pontos = 0;
    
    // Sistema de vidas (NOVO!)
    private int vidas = 3;

    public Fase()
    {   

        for(int i = 0; i < Consts.MUNDO_ALTURA; i++)
        {
            for(int j = 0; j < Consts.MUNDO_LARGURA; j++)
            {
                Coletavel c = new Coletavel("explosao.png", i, j);
                this.coletaveis.add(c);
            }
        }
        this.addSpecial();
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

    public void updatePoints(Coletavel c)
    {
        int before = this.num_to_collect;
        this.num_to_collect = this.coletaveis.size();
        if(before != this.num_to_collect)
        {
            this.pontos += (c.val * this.multiplier);
        }
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

    // ===== NOVOS MÉTODOS PARA O SISTEMA DE VIDAS =====
    
    /**
     * Retorna o número de vidas restantes
     */
    public int getVidas() {
        return vidas;
    }
    
    /**
     * Remove uma vida do jogador
     * @return true se ainda tem vidas, false se game over
     */
    public boolean perderVida() {
        if (vidas > 0) {
            vidas--;
            System.out.println("Vida perdida! Vidas restantes: " + vidas);
            return vidas > 0;
        }
        return false;
    }
    
    /**
     * Adiciona uma vida (para power-ups futuros)
     */
    public void ganharVida() {
        if (vidas < 3) {
            vidas++;
            System.out.println("Vida recuperada! Vidas: " + vidas);
        }
    }
    
    /**
     * Reseta as vidas para o valor inicial
     */
    public void resetarVidas() {
        vidas = 3;
    }
    
    /**
     * Verifica se o jogador ainda está vivo
     */
    public boolean estaVivo() {
        return vidas > 0;
    }
    
    // ===== MÉTODOS DE PONTUAÇÃO =====
    
    /**
     * Retorna a pontuação atual
     */
    public int getPontos() {
        return pontos;
    }
    
    /**
     * Adiciona pontos (para diferentes tipos de itens)
     */
    public void adicionarPontos(int valor) {
        pontos += valor;
    }
    
    /**
     * Reseta a pontuação
     */
    public void resetarPontos() {
        pontos = 0;
    }

    public void addSpecial()
    {
        for(int i = 0; i < 4; i ++)
        {
            int special_pos = ThreadLocalRandom.current().nextInt(0,this.coletaveis.size());
            Coletavel c = this.coletaveis.get(special_pos);
            ColetavelEspecial cesp = new ColetavelEspecial("bricks.png", c.getPosicao().getLinha(), c.getPosicao().getColuna());
            this.coletaveis.remove(c);
            this.coletaveis.add(cesp);
        }
    }
}