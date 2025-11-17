package Auxiliar;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import javax.swing.ImageIcon;
import Controler.Tela;
import Modelo.Fase;

public class GameUI {
    
    private ImageIcon vidaIcon;
    private ImageIcon vidaPerdidaIcon;
    private int highScore;
    private String feedbackMessage;
    private int feedbackTimer;
    
    // Cores estilo arcade retrô
    private final Color COR_FUNDO_UI = new Color(0, 0, 0, 180); // Preto semi-transparente
    private final Color COR_TEXTO_PRINCIPAL = new Color(255, 255, 0); // Amarelo
    private final Color COR_TEXTO_DESTAQUE = new Color(255, 100, 100); // Vermelho claro
    private final Color COR_PONTUACAO = new Color(0, 255, 0); // Verde
    
    public GameUI() {
        this.highScore = 0;
        this.feedbackMessage = "";
        this.feedbackTimer = 0;
        
        // Carrega os ícones de vida
        carregarIcones();
    }
    
    private void carregarIcones() {
        try {
            // Ícone de vida (joaninha pequena)
            ImageIcon tempIcon = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + "joaninha.png");
            Image img = tempIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            vidaIcon = new ImageIcon(img);
            
            // Ícone de vida perdida (caveira pequena ou joaninha cinza)
            ImageIcon tempIcon2 = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + "caveira.png");
            Image img2 = tempIcon2.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            vidaPerdidaIcon = new ImageIcon(img2);
            
        } catch (IOException ex) {
            System.out.println("Erro ao carregar ícones da UI: " + ex.getMessage());
        }
    }
    
    /**
     * Desenha toda a interface do usuário
     */
    public void desenhar(Graphics g, Tela tela, Fase fase) {
        int larguraTela = Consts.RES * Consts.CELL_SIDE;
        int alturaTela = Consts.RES * Consts.CELL_SIDE;
        
        // Desenha barra superior com informações
        desenharBarraSuperior(g, larguraTela, fase, tela.fase_num);
        
        // Desenha vidas
        desenharVidas(g, fase);
        
        // Desenha mensagens de feedback (se houver)
        if (feedbackTimer > 0) {
            desenharFeedback(g, larguraTela, alturaTela);
            feedbackTimer--;
        }
        
        // Desenha contador de itens restantes (canto inferior direito)
        desenharContadorItens(g, larguraTela, alturaTela, fase);
    }
    
    /**
     * Desenha a barra superior com pontuação e fase
     */
    private void desenharBarraSuperior(Graphics g, int larguraTela, Fase fase, int numeroFase) {
        int alturaBarra = 35;
        
        // Fundo semi-transparente
        g.setColor(COR_FUNDO_UI);
        g.fillRect(0, 0, larguraTela, alturaBarra);
        
        // Borda inferior da barra
        g.setColor(COR_TEXTO_PRINCIPAL);
        g.drawLine(0, alturaBarra, larguraTela, alturaBarra);
        
        // PONTUAÇÃO (esquerda)
        g.setFont(new Font("Courier New", Font.BOLD, 18));
        g.setColor(COR_TEXTO_PRINCIPAL);
        g.drawString("SCORE", 10, 23);
        
        g.setFont(new Font("Courier New", Font.BOLD, 20));
        g.setColor(COR_PONTUACAO);
        g.drawString(String.format("%06d", fase.getPontos()), 85, 23);
        
        // HIGH SCORE (centro)
        if (fase.getPontos() > highScore) {
            highScore = fase.getPontos();
        }
        
        g.setFont(new Font("Courier New", Font.BOLD, 14));
        g.setColor(COR_TEXTO_DESTAQUE);
        g.drawString("HI", larguraTela / 2 - 50, 18);
        g.setFont(new Font("Courier New", Font.BOLD, 16));
        g.drawString(String.format("%06d", highScore), larguraTela / 2 - 25, 25);
        
        // FASE (direita)
        g.setFont(new Font("Courier New", Font.BOLD, 18));
        g.setColor(COR_TEXTO_PRINCIPAL);
        g.drawString("FASE", larguraTela - 120, 23);
        g.setFont(new Font("Courier New", Font.BOLD, 20));
        g.setColor(Color.CYAN);
        g.drawString(String.valueOf(numeroFase + 1), larguraTela - 40, 23);
    }
    
    /**
     * Desenha as vidas restantes
     */
    private void desenharVidas(Graphics g, Fase fase) {
        int x = 10;
        int y = 45;
        int vidasRestantes = fase.getVidas();
        
        // Label "VIDAS"
        g.setFont(new Font("Courier New", Font.BOLD, 14));
        g.setColor(COR_TEXTO_PRINCIPAL);
        g.drawString("VIDAS:", x, y);
        
        // Desenha os ícones de vida
        x += 70;
        for (int i = 0; i < 3; i++) {
            if (i < vidasRestantes) {
                // Vida ativa
                if (vidaIcon != null) {
                    vidaIcon.paintIcon(null, g, x + (i * 35), y - 20);
                } else {
                    // Fallback: desenhar coração vermelho
                    g.setColor(Color.RED);
                    g.fillOval(x + (i * 35), y - 20, 25, 25);
                }
            } else {
                // Vida perdida
                if (vidaPerdidaIcon != null) {
                    vidaPerdidaIcon.paintIcon(null, g, x + (i * 35), y - 20);
                } else {
                    // Fallback: desenhar círculo cinza
                    g.setColor(Color.DARK_GRAY);
                    g.fillOval(x + (i * 35), y - 20, 25, 25);
                }
            }
        }
        
        // Alerta de vida baixa
        if (vidasRestantes == 1) {
            g.setFont(new Font("Courier New", Font.BOLD, 12));
            g.setColor(Color.RED);
            int piscar = (int)(System.currentTimeMillis() / 500) % 2;
            if (piscar == 0) {
                g.drawString("! DANGER !", x + 120, y);
            }
        }
    }
    
    /**
     * Desenha o contador de itens restantes
     */
    private void desenharContadorItens(Graphics g, int larguraTela, int alturaTela, Fase fase) {
        int itensRestantes = fase.getNum_to_collect();
        
        if (itensRestantes > 0) {
            int x = larguraTela - 130;
            int y = alturaTela - 15;
            
            // Fundo semi-transparente
            g.setColor(COR_FUNDO_UI);
            g.fillRoundRect(x - 10, y - 25, 135, 35, 10, 10);
            
            g.setFont(new Font("Courier New", Font.BOLD, 14));
            g.setColor(COR_TEXTO_PRINCIPAL);
            g.drawString("ITENS:", x, y - 5);
            
            g.setFont(new Font("Courier New", Font.BOLD, 18));
            g.setColor(Color.ORANGE);
            g.drawString(String.valueOf(itensRestantes), x + 75, y - 3);
        } else {
            // Mensagem de fase completa
            int x = larguraTela / 2 - 100;
            int y = alturaTela - 50;
            
            g.setFont(new Font("Courier New", Font.BOLD, 24));
            g.setColor(Color.GREEN);
            int piscar = (int)(System.currentTimeMillis() / 300) % 2;
            if (piscar == 0) {
                g.drawString("FASE COMPLETA!", x, y);
            }
        }
    }
    
    /**
     * Desenha mensagens de feedback temporárias
     */
    private void desenharFeedback(Graphics g, int larguraTela, int alturaTela) {
        int x = larguraTela / 2 - 80;
        int y = alturaTela / 2;
        
        // Fundo
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRoundRect(x - 20, y - 40, 200, 50, 15, 15);
        
        // Texto
        g.setFont(new Font("Courier New", Font.BOLD, 22));
        g.setColor(Color.YELLOW);
        g.drawString(feedbackMessage, x, y);
    }
    
    /**
     * Mostra uma mensagem de feedback temporária
     */
    public void mostrarFeedback(String mensagem, int duracao) {
        this.feedbackMessage = mensagem;
        this.feedbackTimer = duracao;
    }
    
    /**
     * Reseta o high score (útil para reiniciar o jogo)
     */
    public void resetarHighScore() {
        this.highScore = 0;
    }
}