package Auxiliar;


import Auxiliar.Consts;
import Controler.Tela;
import Modelo.Fase;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;


public class MenuJogo{
    
    public int InicioJogo;

    public MenuJogo(){
        
        InicioJogo = 0;
    }
    private void desenharMenu(Graphics g, int larguraTela, int alturaTela) {
        // Centraliza a mensagem
        g.setFont(new Font("Courier New", Font.BOLD, 28));
        int larguraTexto = g.getFontMetrics().stringWidth("APERTE PLAY PRA JOGAR");
        int x = (larguraTela - larguraTexto) / 2;
        int y = alturaTela / 2;
        
        // Efeito de "pulo" (bounce)
        int offset = 0;
        
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
        g.drawString("CLIQUE PRA JOGAR", x + 2, y + 2 + offset);
        
        // Texto principal
        g.setColor(Color.YELLOW);
        g.drawString("APERTE PLAY PRA JOGAR", x, y + offset);
    }
    public void desenhar(Graphics g,Graphics gjogo, Tela tela, Fase fase) {
        int larguraTela = Consts.RES * Consts.CELL_SIDE;
        int alturaTela = Consts.RES * Consts.CELL_SIDE;
        
        desenharMenu(g, larguraTela, alturaTela);
    
    }
    
}

