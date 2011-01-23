package src;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

/**
 * 
 * */
public class Main
{
   /**
    * @param args
    */
   public static void main(String[] args)
   {
      Calendar date1 = new GregorianCalendar();
      
      OperacoesImagem op = new OperacoesImagem();

      op.open("images/alicates.bmp");

      op.binarization(240);

      /*
       * criação do objecto que representa o resultado da marcação de
       * componentes conexos
       */
      Calendar date2 = new GregorianCalendar();
      double time = (((date2.getTimeInMillis() - date1.getTimeInMillis()) / 1000) + 0.5);

      TwoPass tp = new TwoPass(op.wr);
      tp.executar();
      tp.save("images/alicates_Cores.bmp");

      op.save("images/alicates_Monocromatico.bmp");
      
      JOptionPane.showMessageDialog(null,
               "Image Conversion Complete!\nImage Conversion Time: " + time
                        + "s.", "Information", JOptionPane.INFORMATION_MESSAGE);

      
      System.err.println("Image Conversion Time: " + time + "s.");
   }
}
