package Modelo;

import Auxiliar.Consts;
import Auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;

public class ParedeRoda extends Personagem {
    
    protected ImageIcon horizontal;
    protected ImageIcon vertical;
    protected boolean v;
    protected boolean metade;
    public ParedeRoda prox;
    public ParedeRoda ant;
    public ParedeRodaMeio meio;

    public ParedeRoda(String NomeImagem, int linha, int coluna, boolean metade, ParedeRoda ant, boolean v,ParedeRodaMeio meio){
        super(NomeImagem, linha, coluna);
        
        this.vertical=carregarImagem("paredeRodaVertical.png");
        this.horizontal=carregarImagem("paredeRodaHorizontal.png");
        this.bTransponivel = false;
        this.bMortal = false;
        this.metade=metade;
        this.ant=ant;
        this.v=v;
        if(v){
            iImage=vertical;
            //cria o meio e a utra metade da parede
            if(this.metade){
            this.meio = new ParedeRodaMeio("ParedeRodaMeioVertical.png",linha+1,coluna);
            this.prox= new ParedeRoda(NomeImagem,linha+2,coluna,!metade, this, v, this.meio);
            }
            else{
                this.prox=null;
                this.meio=meio;
            }
        }
        else{
            iImage=horizontal;
            if(this.metade){
                this.meio = new ParedeRodaMeio("ParedeRodaMeioHorizontal.png",linha,coluna+1);
                this.prox= new ParedeRoda(NomeImagem,linha,coluna+2,!metade, this, v,this.meio);
            }
            else{
                this.prox=null;
                this.meio=meio;
            }
        }
    }


    @Override
    public void autoDesenho(){
        super.autoDesenho();
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

    public void roda(int i, Posicao heroPos){
        int aux=0;
        //muda para a imagem complementar e muda a posição da parede
        if (this.iImage==this.horizontal){
            
            if(heroPos.getLinha()!=this.getPosicao().getLinha()){//caso em que passa do lado de uma parede
                aux=1;
                this.iImage=this.vertical;
                this.moveUp();
                this.setPosicao(this.getPosicao().getLinha()+1,this.getPosicao().getColuna() );
                if(this.metade){
                    this.moveUp();
                    this.moveRight();
                }
                else{
                    this.moveDown();
                    this.moveLeft();                    
                }
            }
        }
        else if(heroPos.getColuna()!=this.getPosicao().getColuna()){
            aux=1;
            this.iImage=this.horizontal;
            
            if(this.metade){
                this.moveDown();
                this.moveLeft();
            }
            else{
                this.moveUp();
                this.moveRight();
            }
        }
        
    
        //roda a outra metade da parede
        if(i==0){
            if(this.metade){
                prox.roda(1,heroPos);
            }
            else{
                ant.roda(1,heroPos);
            }
            if(aux==1){
                meio.roda();     
            }
  
        }
    }
}

