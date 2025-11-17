package Auxiliar;

import java.awt.Color;
import java.awt.Graphics;
import Controler.Tela;

public class BordaCronometro {
    
    private int totalSegmentos;             // total de quadradinhos da borda
    private int segmentosAtivos;            // quantos estão acesos
    private Color corInativa;               // cor quando não conta (BRANCO)
    private Color corAtiva;                 // cor quando contando (VERDE)
    
    public BordaCronometro() {
        // de acordo com a Internet, a borda do Lady Bug tem aproximadamente 60-80 segmentos
        this.totalSegmentos = 60;
        this.segmentosAtivos = 0;
        this.corInativa = Color.WHITE;
        this.corAtiva = Color.GREEN;
    }
    
    /**
     * Atualiza o progresso da borda baseado no timer
     * @param progresso valor de 0.0 a 1.0 (0% a 100%)
     */
    public void atualizarProgresso(double progresso) {
        // Converte o progresso (0.0 a 1.0) para número de segmentos
        this.segmentosAtivos = (int)(progresso * totalSegmentos);
    }
    
    /**
     * Desenha a borda ao redor da tela
     */
    public void desenhar(Graphics g, Tela tela) {
        int larguraTela = Consts.RES * Consts.CELL_SIDE;
        int alturaTela = Consts.RES * Consts.CELL_SIDE;
        
        int tamanhoSegmento = 20;                // pixels de largura de cada quadradinho
        int espessuraBorda = 10;                 // pixels de altura/largura da borda
        
        // Calcula quantos segmentos cabem em cada lado
        int segmentosPorLado = totalSegmentos / 4;
        
        int segmentoAtual = 0;
        
        // --- LADO SUPERIOR (esquerda -> direita) ---
        for (int i = 0; i < segmentosPorLado; i++) {
            if (segmentoAtual < segmentosAtivos) {
                g.setColor(corAtiva);
            } else {
                g.setColor(corInativa);
            }
            g.fillRect(i * tamanhoSegmento, 0, tamanhoSegmento - 1, espessuraBorda);
            segmentoAtual++;
        }
        
        // --- LADO DIREITO (cima -> baixo) ---
        for (int i = 0; i < segmentosPorLado; i++) {
            if (segmentoAtual < segmentosAtivos) {
                g.setColor(corAtiva);
            } else {
                g.setColor(corInativa);
            }
            g.fillRect(larguraTela - espessuraBorda, i * tamanhoSegmento, 
                      espessuraBorda, tamanhoSegmento - 1);
            segmentoAtual++;
        }
        
        // --- LADO INFERIOR (direita -> esquerda) ---
        for (int i = segmentosPorLado - 1; i >= 0; i--) {
            if (segmentoAtual < segmentosAtivos) {
                g.setColor(corAtiva);
            } else {
                g.setColor(corInativa);
            }
            g.fillRect(i * tamanhoSegmento, alturaTela - espessuraBorda, 
                      tamanhoSegmento - 1, espessuraBorda);
            segmentoAtual++;
        }
        
        // --- LADO ESQUERDO (baixo -> cima) ---
        for (int i = segmentosPorLado - 1; i >= 0; i--) {
            if (segmentoAtual < segmentosAtivos) {
                g.setColor(corAtiva);
            } else {
                g.setColor(corInativa);
            }
            g.fillRect(0, i * tamanhoSegmento, espessuraBorda, tamanhoSegmento - 1);
            segmentoAtual++;
        }
    }
    
    /**
     * Reseta a borda (quando spawna um inimigo)
     */
    public void resetar() {
        this.segmentosAtivos = 0;
    }
}
