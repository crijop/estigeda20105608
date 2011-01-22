package src;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class Main
{
   /**
    * @param args
    */
   public static void main(String[] args)
   {

      OperacoesImagem op = new OperacoesImagem();

      // op.open("images/alicates.jpg");
      // op.binarization(200);
      // op.save("images/alicatesCizento.jpg");

      op.open("images/alicatesCizento.bmp");

      // op.MatrizImage();
      // op.twoPass();
      // op.secondPass();
      // op.changeColor();
      // op.save("images/teste22.bmp");
      TwoPass tp = new TwoPass(op.bi);
      tp.executar();

      File ficheiro;
      ficheiro = new File("images/treta.bmp");
      try
      {
         ImageIO.write(tp.img_marcas, "bmp", ficheiro);
      } catch (IOException ex)
      {
         Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}
