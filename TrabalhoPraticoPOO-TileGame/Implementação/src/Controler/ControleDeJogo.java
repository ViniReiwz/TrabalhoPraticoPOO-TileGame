package Controler;

import Modelo.Chaser;
import Modelo.Coletavel;
import Modelo.ParedeRoda;
import Modelo.Personagem;
import Modelo.Hero;
import Auxiliar.Posicao;
import Modelo.Fase;
import Auxiliar.Consts;
import Auxiliar.BordaCronometro;
import Auxiliar.GameUI;
import java.util.ArrayList;

public class ControleDeJogo {
    
    private int contadorSpawn;
    private int tempoEntreSpawns;
    private int maxInimigos;
    private Posicao posicaoSpawnCentral;
    private BordaCronometro borda;
    
    // Variável para controlar invencibilidade temporária após perder vida
    private int invencibilidadeTimer;
    private final int TEMPO_INVENCIBILIDADE = 60;
    
    // Referência para a UI (para ativar efeitos)
    private GameUI gameUI;

    public ControleDeJogo(){
        this.contadorSpawn = 0;
        this.tempoEntreSpawns = 150;
        this.maxInimigos = 4;
        this.posicaoSpawnCentral = new Posicao(Consts.MUNDO_ALTURA / 2, Consts.MUNDO_LARGURA / 2);
        this.borda = new BordaCronometro();
        this.invencibilidadeTimer = 0;
    }

    public BordaCronometro getBorda(){
        return borda;
    }
    
    /**
     * Define a referência da UI para ativar efeitos visuais
     */
    public void setGameUI(GameUI ui) {
        this.gameUI = ui;
    }

    public void desenhaTudo(Fase fase) {
        fase.spawnAllPers();
        fase.spawnAllColl();
    }
    
    public void processaTudo(Fase fase, boolean cima, boolean baixo, boolean esquerda, boolean direita) {
        Hero hero = fase.heroi;
        Personagem pIesimoPersonagem;
        Coletavel cIesimoColetavel;

        ArrayList<Coletavel> removed = new ArrayList<>();
        
        // Decrementa o timer de invencibilidade
        if (invencibilidadeTimer > 0) {
            invencibilidadeTimer--;
        }
        
        // --- Loop 1: Processar IA dos Inimigos ---
        for (int i = 1; i < fase.getPersonagens().size(); i++) {
            pIesimoPersonagem = fase.getPersonagens().get(i);
                        
            if (pIesimoPersonagem instanceof Chaser) {
                ((Chaser) pIesimoPersonagem).computeDirection(hero.getPosicao());
            }
        }
        
        // --- Loop 2: Processar Colisões (Coletáveis e Morte) ---
        for (int i = fase.getPersonagens().size() - 1; i > 0; i--) { 
            pIesimoPersonagem = fase.getPersonagens().get(i);
            
            if (pIesimoPersonagem instanceof ParedeRoda) {
                if (hero.getPosicao().ParedeVe(pIesimoPersonagem.getPosicao())&&direita){
                    ((ParedeRoda) pIesimoPersonagem).roda(0,hero.getPosicao());
                    hero.moveRight();
                }
                else if (hero.getPosicao().ParedeVd(pIesimoPersonagem.getPosicao())&&esquerda){
                    ((ParedeRoda) pIesimoPersonagem).roda(0,hero.getPosicao());
                    hero.moveLeft();
                }
                else if (hero.getPosicao().ParedeHc(pIesimoPersonagem.getPosicao())&&baixo){
                    ((ParedeRoda) pIesimoPersonagem).roda(0,hero.getPosicao());
                    hero.moveDown();
                }
                else if (hero.getPosicao().ParedeHb(pIesimoPersonagem.getPosicao())&&cima){
                    ((ParedeRoda) pIesimoPersonagem).roda(0,hero.getPosicao());
                    hero.moveUp();
                }
            }
            // Colisão com personagens mortais (apenas se não estiver invencível)
            else if (hero.getPosicao().igual(pIesimoPersonagem.getPosicao())) {
                if (pIesimoPersonagem.isbTransponivel()) { 
                    if (pIesimoPersonagem.isbMortal() && invencibilidadeTimer == 0) {
                        // Perdeu uma vida!
                        boolean aindaVivo = fase.perderVida();
                        
                        // ==== ATIVA EFEITOS VISUAIS ====
                        if (gameUI != null) {
                            gameUI.ativarFlashVermelho();
                            gameUI.mostrarFeedbackVidaPerdida();
                        }
                        
                        if (aindaVivo) {
                            // Ainda tem vidas, reposiciona o herói e ativa invencibilidade
                            hero.setPosicao(4, 7);
                            invencibilidadeTimer = TEMPO_INVENCIBILIDADE;
                            System.out.println("ATENÇÃO! Vida perdida! Vidas restantes: " + fase.getVidas());
                        } else {
                            // Game Over
                            System.out.println("╔════════════════════╗");
                            System.out.println("║    GAME OVER!      ║");
                            System.out.println("║ Pontuação: " + String.format("%06d", fase.getPontos()) + " ║");
                            System.out.println("╚════════════════════╝");
                            
                            if (gameUI != null) {
                                gameUI.mostrarFeedback("GAME OVER", 120);
                            }
                            
                            fase.getPersonagens().remove(hero);
                            break;
                        }
                    } 
                }
            }
        }

        // --- Loop 3: Processar coleta de itens ---
        int itensColetados = 0;
        for(int j = fase.getColetaveis().size() - 1; j >= 0; j--)
        {
            cIesimoColetavel = fase.getColetaveis().get(j);
            if(hero.getPosicao().igual(cIesimoColetavel.getPosicao()))
            {
                removed.add(cIesimoColetavel);
                itensColetados++;
            }
        }

        // Se coletou itens, mostra feedback
        if (itensColetados > 0 && gameUI != null) {
            if (itensColetados == 1) {
                gameUI.mostrarFeedbackColeta();
            } else {
                gameUI.mostrarFeedback("COMBO x" + itensColetados + "!", 40);
            }
        }

        fase.getColetaveis().removeAll(removed);
        for(Coletavel c : removed)
        {
            fase.updatePoints(c);
        }
        
        // --- Loop 4: Gerenciamento do spawn dos inimigos ---
        spawnarInimigos(fase);
    }    

    private void spawnarInimigos(Fase fase){
        int numChasers = 0;
        for(Personagem p : fase.getPersonagens()){
            if(p instanceof Chaser){
                numChasers++;
            }
        }

        if(numChasers < maxInimigos){
            contadorSpawn++;
            double progresso = (double) contadorSpawn / tempoEntreSpawns;
            borda.atualizarProgresso(progresso);

            if(contadorSpawn >= tempoEntreSpawns){
                Chaser novoInimgo = new Chaser("chaser.png", posicaoSpawnCentral.getLinha(), posicaoSpawnCentral.getColuna());
                fase.addPers(novoInimgo);
                contadorSpawn = 0;
                borda.resetar();

                if(Consts.DEBUG){
                    System.out.println("Novo inimigo spawnado no centro! Borda resetada.");
                }
            }
        }
        else{
            borda.resetar();
        }
    }

    public boolean ehPosicaoValida(Fase fase, Posicao p) {
        Personagem pIesimoPersonagem;
        for (int i = 1; i < fase.getPersonagens().size(); i++) {
            pIesimoPersonagem = fase.getPersonagens().get(i);
            if (!pIesimoPersonagem.isbTransponivel()) {
                if (pIesimoPersonagem.getPosicao().igual(p)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void passaFase(Tela tela)
    {
        if (tela.faseAtual.getNum_to_collect() == 0)
        {
            // Mostra feedback de fase completa
            if (gameUI != null) {
                gameUI.mostrarFeedbackFaseCompleta();
            }
            
            tela.fase_num++;
            if(tela.fase_num < tela.getFases().size())
            {
                tela.faseAtual = tela.getFases().get(tela.fase_num);
                
                // Reseta animações da UI para a nova fase
                if (gameUI != null) {
                    gameUI.resetarAnimacoes();
                }
                
                tela.repaint();
            }
            else
            {
                System.out.println("╔═══════════════════════════╗");
                System.out.println("║  PARABÉNS!                ║");
                System.out.println("║  Você completou o jogo!   ║");
                System.out.println("║  Pontuação Final: " + String.format("%06d", tela.faseAtual.getPontos()) + "  ║");
                System.out.println("╚═══════════════════════════╝");
                
                if (gameUI != null) {
                    gameUI.mostrarFeedback("VICTORY!", 180);
                }
                
                System.exit(0);
            }
        }
    }
    
    /**
     * Retorna se o herói está invencível no momento
     */
    public boolean estaInvencivel() {
        return invencibilidadeTimer > 0;
    }
}