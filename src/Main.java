package src;

/**
 * @author Carlos
 * 
 */
/**
 * Class Main
 * */
public class Main
{
   /**
    * @param args
    */
   public static void main(String[] args)
   {
      OperacoesImagem op = new OperacoesImagem();
      op.open("images/alicates.bmp");
      op.binarization(240);

      /*
       * cria��o do objecto que representa o resultado da marca��o de
       * componentes conexos
       */
      TwoPass tp = new TwoPass(op.wr);
      double tempo = System.nanoTime();
      tp.executar();
      tempo = System.nanoTime() - tempo;
      System.out.println(tempo * 0.000000001 + " segundos");
      tp.save("images/alicates_Cores.bmp");
      op.save("images/alicates_Monocromatico.bmp");

   }
}
