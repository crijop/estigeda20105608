/**
 * 
 */
package src;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * @author Carlos
 * 
 */
public class OperacoesImagem
{
   private BufferedImage     bi;
   public WritableRaster     wr;

   private BufferedImage     biOut;
   private WritableRaster    wrOut;

   private int               matriz_Image[][] = null;

   // private LinkedList_3 linkedList;
   private int               matriz_Out[][]   = null;
   public ArrayList<MakeSet> linked;

   public OperacoesImagem()
   {
      this.linked = new ArrayList<MakeSet>();
      this.bi = null;
      this.wr = null;
   }

   public void open(String nome_ficheiro)
   {
      File ficheiro;
      ficheiro = new File(nome_ficheiro);
      try
      {
         this.bi = ImageIO.read(ficheiro);
      } catch (IOException ex)
      {
         Logger.getLogger(OperacoesImagem.class.getName()).log(Level.SEVERE,
                  null, ex);
      }
      this.wr = this.bi.getRaster();
   }

   public void save(String nome_ficheiro)
   {
      File ficheiro;
      ficheiro = new File(nome_ficheiro);
      try
      {
         ImageIO.write(this.biOut, "jpg", ficheiro);
      } catch (IOException ex)
      {
         Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public int getWidth()
   {
      return bi.getWidth();
   }

   public int getHeight()
   {
      return bi.getHeight();
   }

   public void binarization(int th)
   {
      int red, green, blue;
      int coluna, linha;
      int nivel_cinzento;

      for (coluna = 0; coluna < this.getWidth(); coluna++)
      {
         for (linha = 0; linha < this.getHeight(); linha++)
         {
            int[] color = null;
            color = this.wr.getPixel(coluna, linha, color);

            red = color[0];
            green = color[1];
            blue = color[2];

            nivel_cinzento = (int) ((red + green + blue) / 3.0);

            if (nivel_cinzento < th)
            {
               color[0] = 0xff;
               color[1] = 0xff;
               color[2] = 0xff;
               this.wr.setPixel(coluna, linha, color);
            }
            else
            {
               color[0] = 0x00;
               color[1] = 0x00;
               color[2] = 0x00;
               this.wr.setPixel(coluna, linha, color);
            }
         }
      }
   }

   public void MatrizImage()
   {
      int coluna, linha;
      int red, green, blue;
      int nivel_cinzento;
      int largura = bi.getWidth();
      int altura = bi.getHeight();

      this.matriz_Image = new int[largura][altura];

      System.out.println("largura  " + largura + " altura  " + altura);
      for (coluna = 0; coluna < largura; coluna++)
      {
         for (linha = 0; linha < altura; linha++)
         {
            int[] color = null;
            color = this.wr.getPixel(coluna, linha, color);
            // System.out.println("color" + color);
            red = color[0];
            green = color[1];
            blue = color[2];

            nivel_cinzento = (int) ((red + green + blue) / 3.0);
            // System.out.println("nivel_cinzento" + nivel_cinzento);
            if (nivel_cinzento == 0)
            {
               this.matriz_Image[coluna][linha] = 0;
            }
            else
            {
               this.matriz_Image[coluna][linha] = 1;
            }
         }
      }
      /* Imprimir matriz */

      for (coluna = 0; coluna < largura; coluna++)
      {
         for (linha = 0; linha < altura; linha++)
         {
            System.out.printf("%01d", this.matriz_Image[coluna][linha]);
         }
         System.out.println();
      }
      System.out.println();
      System.out.println();
   }

   public void twoPass()
   {
      this.matriz_Out = new int[this.getWidth()][this.getHeight()];// inicializa
      // a
      // matriz

      int coluna, linha; // cria as variaveis responsaveis pelas colunas e
      // linhas
      ArrayList<Integer> ListaVizinhos = new ArrayList();

      int vizinho_Esquerda, vizinho_EsquerdaTopo, vizinho_Topo, vizinho_DireitaTopo;
      int nextLabel = 1;

      // Cria a matriz de saida a zeros (matrizOut)

      for (coluna = 0; coluna < this.getWidth(); coluna++)
      {
         for (linha = 0; linha < this.getHeight(); linha++)
         {
            this.matriz_Out[coluna][linha] = 0;
         }
      }

      // .......................................................................................................//

      for (coluna = 0; coluna < this.getWidth(); coluna++)
      {
         for (linha = 0; linha < this.getHeight(); linha++)
         {
            if (this.matriz_Image[coluna][linha] != 0)
            {
               vizinho_Esquerda = this.matriz_Image[coluna - 1][linha];
               vizinho_EsquerdaTopo = this.matriz_Image[coluna - 1][linha - 1];
               vizinho_Topo = this.matriz_Image[coluna][linha - 1];
               vizinho_DireitaTopo = this.matriz_Image[coluna + 1][linha - 1];

               if (vizinho_Esquerda == 0 && vizinho_EsquerdaTopo == 0
                        && vizinho_Topo == 0 && vizinho_DireitaTopo == 0)
               {

                  this.linked.add(new MakeSet(nextLabel));
                  this.matriz_Out[coluna][linha] = nextLabel;
                  nextLabel += 1;

               }
               else
               {
                  if (vizinho_Esquerda != 0)
                  {
                     ListaVizinhos.add(vizinho_Esquerda);
                  }
                  if (vizinho_EsquerdaTopo != 0)
                  {
                     ListaVizinhos.add(vizinho_EsquerdaTopo);
                  }
                  if (vizinho_Topo != 0)
                  {
                     ListaVizinhos.add(vizinho_Topo);
                  }
                  if (vizinho_DireitaTopo != 0)
                  {

                     ListaVizinhos.add(vizinho_DireitaTopo);
                  }

                  this.matriz_Image[coluna][linha] = this.min(ListaVizinhos);

                  for (int i = 0; i < ListaVizinhos.size() - 1; i++)
                  {
                     this.Union(this.linked.get(ListaVizinhos.get(0) - 1), this.linked
                              .get(ListaVizinhos.get(i) - 1));
                  }
                  ListaVizinhos.clear();

               }
            }
         }
      }
   }

   public void secondPass()
   {
      int coluna, linha; // cria as variaveis responsaveis pelas colunas e
      // linhas

      for (coluna = 0; coluna < this.getWidth(); coluna++)
      {
         for (linha = 0; linha < this.getHeight(); linha++)
         {
            if (this.matriz_Out[coluna][linha] != 0)
            {
               this.matriz_Out[coluna][linha] = this.Find(
                        this.linked.get(this.matriz_Out[coluna][linha] - 1))
                        .getId();
            }
         }
      }
   }

   public void Union(MakeSet x, MakeSet y)
   {
      MakeSet xRoot = this.Find(x);
      MakeSet yRoot = this.Find(y);
      
      xRoot.parent = yRoot;
   }

   public void changeColor()
   {
      Random aleatorio = new Random();
      this.biOut = new BufferedImage(this.getWidth(), this.getHeight(),
               BufferedImage.TYPE_INT_RGB);
      this.wrOut = this.biOut.getRaster();

      int coluna, linha;

      for (coluna = 0; coluna < this.getWidth(); coluna++)
      {
         for (linha = 0; linha < this.getHeight(); linha++)
         {
            int[] color = new int[3];

            if (this.matriz_Out[coluna][linha] != 0)
            {
               // color[0] = 23 + (4* this.matriz_Out[coluna][linha]);
               // color[1] = 54 + (5* this.matriz_Out[coluna][linha]);
               // color[2] = 100 + (9* this.matriz_Out[coluna][linha]);

               color[0] = aleatorio.nextInt(255);
               color[1] = aleatorio.nextInt(255);
               color[2] = aleatorio.nextInt(255);

               this.wrOut.setPixel(coluna, linha, color);
            }
         }
      }
      
      for (coluna = 0; coluna < this.getWidth(); coluna++)
      {
         for (linha = 0; linha < this.getHeight(); linha++)
         {
            System.out.printf("%01d", this.matriz_Out[coluna][linha]);
         }
         System.out.println();
      }
   }

   public MakeSet Find(MakeSet x)
   {

      if (x.parent == x)
      {
         return x;
      }
      else
      {
         return Find(x.parent);
      }
   }

   public int min(ArrayList<Integer> L)
   {
      int min = 0;

      for (int i = 0; i < L.size() - 1; i++)
      {
         if (L.get(i) <= L.get(i + 1))
         {
            min = L.get(i);
         }
         else

            min = L.get(i + 1);
      }
      return min;
   }

}
