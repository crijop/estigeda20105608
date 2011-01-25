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
 * 2010
 */
public class OperacoesImagem
{
   public BufferedImage  bi;
   public WritableRaster wr;

   /**
    * Construtor da Class OperacoesImagem
    * */
   public OperacoesImagem()
   {
      this.bi = null;
      this.wr = null;
   }
   
   /**
    * Metodo open
    * @param nome_ficheiro
    *           - nome do ficheiro para que vai ser aberto
    * */
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
   
   /**
    * Metodo Save
    * @param nome_ficheiro
    *           - nome do ficheiro para que vai ser guardado
    * */
   public void save(String nome_ficheiro)
   {
      File ficheiro;
      ficheiro = new File(nome_ficheiro);
      try
      {
         ImageIO.write(this.bi, "BMP", ficheiro);
      } catch (IOException ex)
      {
         Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   /**
    * Metodo getWidth
    * @return bi.getWidth
    * */
   public int getWidth()
   {
      return bi.getWidth();
   }

   /**
    * Metodo getHeight
    * @return bi.getHeight
    * */
   public int getHeight()
   {
      return bi.getHeight();
   }

   /**
    * Metodo binarization
    * @param th - valor inteiro
    * */
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
}