package src;

public class Main
{
   /**
    * @param args
    */
   public static void main(String[] args)
   {

      OperacoesImagem op = new OperacoesImagem();
      
      op.open("images/alicates.jpg");
      op.binarization(200);
      op.save("images/alicatesCizento.jpg");
   }
}
