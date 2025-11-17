package Modelo;

import java.util.ArrayList;

// Classe onde tudo relacionado à uma fase deve ser feito
// A ideia é em Tela.java, chamar o construtor e adicionar os personagens
// Todo o manuseio interno (deletar personagem, adicionar, mudar estado das paredes, posição das coisas, etc é pra ser feito nessa classe)
public class Fase 
{
    // ArrayList com todos os personagens não-coletaveis
    private ArrayList<Personagem> personagens = new ArrayList<>();

    // ArrayList com os coletáveis
    private ArrayList<Coletavel> coletaveis = new ArrayList<>();

    // Número de coletaveis (ao chegar em 0 a fase deve acabar - Não implementado ainda)
    private int num_to_collect = -1;

    // Heroi da fase
    public Hero heroi;

    // Sistema de pontuação
    private int pontos = 0;
    
    // Sistema de vidas (NOVO!)
    private int vidas = 3;

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
}