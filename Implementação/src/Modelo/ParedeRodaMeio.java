package Modelo;

import Auxiliar.Consts;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;

public class ParedeRodaMeio extends Personagem {
    
    protected ImageIcon horizontal;
    protected ImageIcon vertical;
    private int v;

    public ParedeRodaMeio(String NomeImagem, int linha, int coluna){
        super(NomeImagem, linha, coluna);

        this.bTransponivel = false;
        this.bMortal = false;
        this.vertical=carregarImagem("ParedeRodaMeioVertical.png");
        this.horizontal=carregarImagem("ParedeRodaMeioHorizontal.png");
        if (NomeImagem=="ParedeRodaMeioVertical.png"){
        this.v=1;}
    }
    
    
    private ImageIcon carregarImagem(String sNomeImagePNG) {
        try {
            ImageIcon iNewImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + sNomeImagePNG);
            Image img = iNewImage.getImage();
            BufferedImage bi = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            g.drawImage(img, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
            return new ImageIcon(bi);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
    
    public void roda(){
        
        if(v==1){
            
            this.iImage=this.horizontal;
            v=0;
        }
        else{
            this.iImage=vertical;
            v=1;
        }
    }

    @Override
    public void autoDesenho(){
        super.autoDesenho();
    }

    
}
