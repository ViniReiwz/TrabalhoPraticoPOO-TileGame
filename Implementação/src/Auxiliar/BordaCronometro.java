package Auxiliar;

import java.awt.Color;
import java.awt.Graphics;
import Controler.Tela;

public class BordaCronometro {
    
    private int totalSegmentos;
    private int segmentosAtivos;
    private Color corInativa;
    private Color corAtiva;
    private int tamanhoSegmento = 15;
    
    public BordaCronometro() {
        // cálculo automático de quantos segmentos cabem no perímetro
        int larguraTela = Consts.RES * Consts.CELL_SIDE;
        int alturaTela = Consts.RES * Consts.CELL_SIDE;
        int perimetro = 2 * (larguraTela + alturaTela);
        this.totalSegmentos = perimetro / tamanhoSegmento;
        
        this.segmentosAtivos = 0;
        this.corInativa = Color.WHITE;
        this.corAtiva = Color.GREEN;
        
        if(Consts.DEBUG) {
            System.out.println("BordaCronometro: Total de segmentos = " + totalSegmentos);
        }
    }
    
    public void atualizarProgresso(double progresso) {
        this.segmentosAtivos = (int)(progresso * totalSegmentos);
    }
    
    public void desenhar(Graphics g, Tela tela) {
        int larguraTela = Consts.RES * Consts.CELL_SIDE;
        int alturaTela = Consts.RES * Consts.CELL_SIDE;
        
        int espessuraBorda = 6;
        
        int segmentoAtual = 0;
        
        // --- LADO SUPERIOR (esquerda -> direita) ---
        for (int x = 0; x < larguraTela; x += tamanhoSegmento) {
            if (segmentoAtual < segmentosAtivos) {
                g.setColor(corAtiva);
            } else {
                g.setColor(corInativa);
            }
            g.fillRect(x, 0, tamanhoSegmento - 1, espessuraBorda);
            segmentoAtual++;
        }
        
        // --- LADO DIREITO (cima -> baixo) ---
        for (int y = 0; y < alturaTela; y += tamanhoSegmento) {
            if (segmentoAtual < segmentosAtivos) {
                g.setColor(corAtiva);
            } else {
                g.setColor(corInativa);
            }
            g.fillRect(larguraTela - espessuraBorda, y, espessuraBorda, tamanhoSegmento - 1);
            segmentoAtual++;
        }
        
        // --- LADO INFERIOR (direita -> esquerda) ---
        for (int x = larguraTela - tamanhoSegmento; x >= 0; x -= tamanhoSegmento) {
            if (segmentoAtual < segmentosAtivos) {
                g.setColor(corAtiva);
            } else {
                g.setColor(corInativa);
            }
            g.fillRect(x, alturaTela - espessuraBorda, tamanhoSegmento - 1, espessuraBorda);
            segmentoAtual++;
        }
        
        // --- LADO ESQUERDO (baixo -> cima) ---
        for (int y = alturaTela - tamanhoSegmento; y >= 0; y -= tamanhoSegmento) {
            if (segmentoAtual < segmentosAtivos) {
                g.setColor(corAtiva);
            } else {
                g.setColor(corInativa);
            }
            g.fillRect(0, y, espessuraBorda, tamanhoSegmento - 1);
            segmentoAtual++;
        }
    }
    
    public void resetar() {
        this.segmentosAtivos = 0;
    }
}
