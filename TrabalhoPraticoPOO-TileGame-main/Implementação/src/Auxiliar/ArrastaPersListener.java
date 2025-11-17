package Auxiliar;

import java.awt.dnd.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;
import Controler.Tela;
import java.awt.Point;

import Modelo.Personagem;

import java.awt.datatransfer.DataFlavor;

public class ArrastaPersListener implements DropTargetListener {

    // Tela na qual o personagem deve ser escrito
    public Tela tela;

    public ArrastaPersListener(Tela tela)
    {   
        this.tela = tela;
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        // Nada a fazer
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        // Nada a fazer
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        // Nada a fazer
    }

    // Sobreescrevendo o método 'drop' para que ele possa ler o arquivo e spawnar o objeto na tela
    @Override
    public void drop(DropTargetDropEvent dtde) {

        dtde.acceptDrop(DnDConstants.ACTION_COPY);  // Ação de cópia de arquivo

        // Tratamento básico de exceção
        try{
            List<?> arq = (List<?>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor); // Recebe a lista de arquivos transferidos pela ação de drop
            for(Object o : arq)
            {
                if(o instanceof File f)                                                                    // Verifica se o recebido pelo drop é do tipo 'File'
                {
                    
                    if(Consts.DEBUG){System.out.println("Arquivo dropado --> " + f.getAbsolutePath());}    // Exibe o caminho absoluto se DEUBG == true

                    FileInputStream fis = new FileInputStream(f);                                          // Usa as Streams e Decorators para ler o arquivo
                    GZIPInputStream gzi = new GZIPInputStream(fis);
                    ObjectInputStream ois = new ObjectInputStream(gzi);

                    Object p = ois.readObject();                                                            // Instância para 'p' o arquivo lido

                    if(p instanceof Personagem pers)                                                        // Verifica se 'p' é da classe 'Personagem'
                    {
                        this.tela.addPersonagem(pers);                                                      // Adiciona 'pers' à lista de personagems

                        Point posicao = dtde.getLocation();                                                 // Pega a posição (em pixels) do mouse no momento do drop
                        pers.setPosicao((posicao.y / Consts.CELL_SIDE) - 1 + this.tela.getCameraLinha() , (posicao.x / Consts.CELL_SIDE) + this.tela.getCameraColuna());  // Normaliza a posição e corrige o posicionamento para se encaixar nos tiles da tela 
                        if(Consts.DEBUG)
                        {
                            System.out.println("Posicao do mouse --> " + posicao);
                            // Exibe a posição (em tiles) que o objeto deve ser dropado
                            System.out.println("Posição do drop: x ==> " + pers.getPosicao().getColuna() + " y ==> " + pers.getPosicao().getLinha());
                        }

                        pers.autoDesenho();                                                                 // Spawna o personagem

                        dtde.dropComplete(true);                                                            // Sucesso no drop
                    }
                    else
                    {
                        dtde.dropComplete(false);
                    }
                    ois.close();                                                                            // Fecha as streams dos arquivos e o arquivo em si
                }
                else{dtde.dropComplete(false);}
            }

        }
        // Caso encontre alguma exceção, exibe no terminal
        catch(Exception e)
        {
            System.out.println(e);
            dtde.dropComplete(false);
        }
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        // Nada a fazer
    }
}
