/**
 * 
 */
package src;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * @author Carlos
 * 
 */
public class OperacoesImagem
{
   public BufferedImage  bi;
   public WritableRaster wr;

   private BufferedImage biOut;

   // private WritableRaster wrOut;

   // private int matriz_Image[][] = null;
   //
   // private int matriz_Out[][] = null;

   public OperacoesImagem()
   {
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
         ImageIO.write(this.biOut, "bmp", ficheiro);
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

   // public void MatrizImage()
   // {
   // int coluna, linha;
   // int red, green, blue;
   // int nivel_cinzento;
   //
   // this.matriz_Image = new int[this.getWidth()][this.getHeight()];
   //
   // System.out.println("largura  " + this.getWidth() + " altura  "
   // + this.getHeight());
   // for (coluna = 0; coluna < this.getWidth(); coluna++)
   // {
   // for (linha = 0; linha < this.getHeight(); linha++)
   // {
   // int[] color = null;
   // color = this.wr.getPixel(coluna, linha, color);
   // // System.out.println("color" + color);
   // red = color[0];
   // green = color[1];
   // blue = color[2];
   //
   // nivel_cinzento = (int) ((red + green + blue) / 3.0);
   // // System.out.println("nivel_cinzento" + nivel_cinzento);
   // if (nivel_cinzento == 0)
   // {
   // this.matriz_Image[coluna][linha] = 0;
   // }
   // else
   // {
   // this.matriz_Image[coluna][linha] = 1;
   // }
   // }
   // }
   // /* Imprimir matriz */
   //
   // for (coluna = 0; coluna < this.getWidth(); coluna++)
   // {
   // for (linha = 0; linha < this.getHeight(); linha++)
   // {
   // System.out.printf("%01d", this.matriz_Image[coluna][linha]);
   // }
   // System.out.println();
   // }
   // System.out.println();
   // System.out.println();
   // }
   //   
   //   
   //   
   // public void changeColor()
   // {
   // Random aleatorio = new Random();
   // this.biOut = new BufferedImage(this.getWidth(), this.getHeight(),
   // BufferedImage.TYPE_INT_RGB);
   // this.wrOut = this.biOut.getRaster();
   //
   // int coluna, linha;
   //
   // for (coluna = 0; coluna < this.getWidth(); coluna++)
   // {
   // for (linha = 0; linha < this.getHeight(); linha++)
   // {
   // int[] color = new int[3];
   //
   // if (this.matriz_Out[coluna][linha] != 0)
   // {
   // // color[0] = 23 + (4* this.matriz_Out[coluna][linha]);
   // // color[1] = 54 + (5* this.matriz_Out[coluna][linha]);
   // // color[2] = 100 + (9* this.matriz_Out[coluna][linha]);
   //
   // color[0] = aleatorio.nextInt(255);
   // color[1] = aleatorio.nextInt(255);
   // color[2] = aleatorio.nextInt(255);
   //
   // this.wrOut.setPixel(coluna, linha, color);
   // }
   // }
   // }
   //
   // /* imprime matriz de saida */
   // for (coluna = 0; coluna < this.getWidth(); coluna++)
   // {
   // for (linha = 0; linha < this.getHeight(); linha++)
   // {
   // System.out.printf("%01d", this.matriz_Out[coluna][linha]);
   // }
   // System.out.println();
   // }
}
