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
    
    // Animação de pontuação
    private int pontosExibidos;
    private int pontosAlvo;
    private static final int VELOCIDADE_ANIMACAO = 1;
    
    // Flash vermelho ao ser atingido
    private int flashTimer;
    private static final int DURACAO_FLASH = 30; // frames
    
    // Barra de progresso
    private int progressoBarraAnimado;
    
    // Cores estilo arcade retrô
    private final Color COR_FUNDO_UI = new Color(0, 0, 0, 180);
    private final Color COR_TEXTO_PRINCIPAL = new Color(255, 255, 0);
    private final Color COR_TEXTO_DESTAQUE = new Color(255, 100, 100);
    private final Color COR_PONTUACAO = new Color(0, 255, 0);
    private final Color COR_FLASH = new Color(255, 0, 0, 100); // Vermelho semi-transparente
    
    public GameUI() {
        this.highScore = 0;
        this.feedbackMessage = "";
        this.feedbackTimer = 0;
        this.pontosExibidos = 0;
        this.pontosAlvo = 0;
        this.flashTimer = 0;
        this.progressoBarraAnimado = 0;
        
        carregarIcones();
    }
    
    private void carregarIcones() {
        try {
            ImageIcon tempIcon = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + "joaninhaCima.png");
            Image img = tempIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            vidaIcon = new ImageIcon(img);
            
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
        
        // ==== FLASH VERMELHO ao ser atingido ====
        if (flashTimer > 0) {
            desenharFlashVermelho(g, larguraTela, alturaTela);
            flashTimer--;
        }
        
        // Atualiza animação de pontos
        atualizarAnimacaoPontos(fase.getPontos());
        
        // Desenha barra superior com informações
        desenharBarraSuperior(g, larguraTela, fase, tela.fase_num);
        
        // Desenha vidas
        desenharVidas(g, fase);
        
        // ==== BARRA DE PROGRESSO DA FASE ====
        desenharBarraProgresso(g, larguraTela, alturaTela, fase);
        
        // ==== MENSAGENS DE FEEDBACK ====
        if (feedbackTimer > 0) {
            desenharFeedback(g, larguraTela, alturaTela);
            feedbackTimer--;
        }
        
        // Desenha contador de itens restantes
        desenharContadorItens(g, larguraTela, alturaTela, fase);
    }
    
    /**
     * ==== NOVO! Flash vermelho ao ser atingido ====
     */
    private void desenharFlashVermelho(Graphics g, int larguraTela, int alturaTela) {
        // Efeito pulsante: intensidade varia com o tempo
        int intensidade = (int)(100 * ((double)flashTimer / DURACAO_FLASH));
        Color flashColor = new Color(255, 0, 0, intensidade);
        
        g.setColor(flashColor);
        g.fillRect(0, 0, larguraTela, alturaTela);
        
        // Borda vermelha mais intensa
        g.setColor(new Color(255, 0, 0, Math.min(200, intensidade * 2)));
        for (int i = 0; i < 5; i++) {
            g.drawRect(i, i, larguraTela - i * 2, alturaTela - i * 2);
        }
    }
    
    /**
     * Ativa o flash vermelho (chamado quando o herói é atingido)
     */
    public void ativarFlashVermelho() {
        this.flashTimer = DURACAO_FLASH;
    }
    
    /**
     * ==== NOVO! Animação suave dos pontos ====
     */
    private void atualizarAnimacaoPontos(int pontosReais) {
        pontosAlvo = pontosReais;
        
        if (pontosExibidos < pontosAlvo) {
            // Aumenta gradualmente
            int diferenca = pontosAlvo - pontosExibidos;
            int incremento = Math.max(1, diferenca / 10); // Acelera conforme a diferença
            pontosExibidos += incremento;
            
            if (pontosExibidos > pontosAlvo) {
                pontosExibidos = pontosAlvo;
            }
        } else if (pontosExibidos > pontosAlvo) {
            // Diminui gradualmente (caso implementem penalidades)
            pontosExibidos = pontosAlvo;
        }
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
        
        // PONTUAÇÃO (esquerda) - ANIMADA!
        g.setFont(new Font("Courier New", Font.BOLD, 18));
        g.setColor(COR_TEXTO_PRINCIPAL);
        g.drawString("SCORE", 10, 23);
        
        // Efeito de brilho quando pontos aumentam
        boolean aumentando = pontosExibidos < pontosAlvo;
        if (aumentando && (System.currentTimeMillis() / 100) % 2 == 0) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(COR_PONTUACAO);
        }
        
        g.setFont(new Font("Courier New", Font.BOLD, 20));
        g.drawString(String.format("%06d", pontosExibidos), 85, 23);
        
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
                    g.setColor(Color.RED);
                    g.fillOval(x + (i * 35), y - 20, 25, 25);
                }
            } else {
                // Vida perdida
                if (vidaPerdidaIcon != null) {
                    vidaPerdidaIcon.paintIcon(null, g, x + (i * 35), y - 20);
                } else {
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
     * ==== NOVO! Barra de progresso da fase ====
     */
    private void desenharBarraProgresso(Graphics g, int larguraTela, int alturaTela, Fase fase) {
        int totalItens = 1; // Evita divisão por zero
        int itensColetados = 0;
        
        // Calcula progresso (total - restantes = coletados)
        if (fase.getNum_to_collect() >= 0) {
            totalItens = Math.max(1, fase.getNum_to_collect() + pontosExibidos);
            itensColetados = pontosExibidos;
        }
        
        double progresso = (double)itensColetados / totalItens;
        
        // Anima suavemente a barra
        int progressoAlvo = (int)(progresso * 100);
        if (progressoBarraAnimado < progressoAlvo) {
            progressoBarraAnimado += 2;
        } else if (progressoBarraAnimado > progressoAlvo) {
            progressoBarraAnimado = progressoAlvo;
        }
        
        // Posição da barra
        int barraX = 10;
        int barraY = 75;
        int barraLargura = 200;
        int barraAltura = 15;
        
        // Fundo da barra
        g.setColor(new Color(50, 50, 50, 200));
        g.fillRoundRect(barraX, barraY, barraLargura, barraAltura, 8, 8);
        
        // Borda
        g.setColor(COR_TEXTO_PRINCIPAL);
        g.drawRoundRect(barraX, barraY, barraLargura, barraAltura, 8, 8);
        
        // Preenchimento (progresso)
        int preenchimento = (int)((barraLargura - 4) * progressoBarraAnimado / 100.0);
        
        // Cor gradiente baseada no progresso
        Color corBarra;
        if (progressoBarraAnimado < 33) {
            corBarra = new Color(255, 100, 100); // Vermelho
        } else if (progressoBarraAnimado < 66) {
            corBarra = new Color(255, 200, 0); // Amarelo
        } else {
            corBarra = new Color(0, 255, 100); // Verde
        }
        
        g.setColor(corBarra);
        g.fillRoundRect(barraX + 2, barraY + 2, preenchimento, barraAltura - 4, 6, 6);
        
        // Texto de porcentagem
        g.setFont(new Font("Courier New", Font.BOLD, 11));
        g.setColor(Color.WHITE);
        String textoProgresso = progressoBarraAnimado + "%";
        g.drawString(textoProgresso, barraX + barraLargura + 10, barraY + 12);
        
        // Label "PROGRESSO"
        g.setFont(new Font("Courier New", Font.BOLD, 10));
        g.setColor(COR_TEXTO_PRINCIPAL);
        g.drawString("PROGRESSO:", barraX, barraY - 5);
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
     * ==== NOVO! Desenha mensagens de feedback temporárias ====
     */
    private void desenharFeedback(Graphics g, int larguraTela, int alturaTela) {
        // Centraliza a mensagem
        g.setFont(new Font("Courier New", Font.BOLD, 28));
        int larguraTexto = g.getFontMetrics().stringWidth(feedbackMessage);
        int x = (larguraTela - larguraTexto) / 2;
        int y = alturaTela / 2;
        
        // Efeito de "pulo" (bounce)
        int offset = 0;
        if (feedbackTimer > 40) {
            offset = (50 - feedbackTimer) * 2;
        } else if (feedbackTimer < 10) {
            offset = -feedbackTimer;
        }
        
        // Fundo com sombra
        g.setColor(new Color(0, 0, 0, 220));
        g.fillRoundRect(x - 25, y - 45 + offset, larguraTexto + 50, 60, 20, 20);
        
        // Borda brilhante
        int brilho = (int)(System.currentTimeMillis() / 100) % 2;
        if (brilho == 0) {
            g.setColor(new Color(255, 255, 100));
        } else {
            g.setColor(new Color(255, 200, 0));
        }
        g.drawRoundRect(x - 25, y - 45 + offset, larguraTexto + 50, 60, 20, 20);
        g.drawRoundRect(x - 24, y - 44 + offset, larguraTexto + 48, 58, 19, 19);
        
        // Sombra do texto
        g.setColor(new Color(0, 0, 0, 150));
        g.drawString(feedbackMessage, x + 2, y + 2 + offset);
        
        // Texto principal
        g.setColor(Color.YELLOW);
        g.drawString(feedbackMessage, x, y + offset);
    }
    
    /**
     * Mostra uma mensagem de feedback temporária
     */
    public void mostrarFeedback(String mensagem, int duracao) {
        this.feedbackMessage = mensagem;
        this.feedbackTimer = duracao;
    }
    
    /**
     * Mostra feedback automático baseado em eventos
     */
    public void mostrarFeedbackColeta() {
        String[] mensagens = {"NICE!", "GOOD!", "COOL!", "YEAH!", "GREAT!"};
        int indice = (int)(Math.random() * mensagens.length);
        mostrarFeedback(mensagens[indice], 30);
    }
    
    public void mostrarFeedbackVidaPerdida() {
        mostrarFeedback("OUCH!", 40);
    }
    
    public void mostrarFeedbackFaseCompleta() {
        mostrarFeedback("PERFECT!", 60);
    }
    
    /**
     * Reseta o high score
     */
    public void resetarHighScore() {
        this.highScore = 0;
    }
    
    /**
     * Reseta animações (útil ao trocar de fase)
     */
    public void resetarAnimacoes() {
        this.pontosExibidos = 0;
        this.progressoBarraAnimado = 0;
    }
}