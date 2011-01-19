package src;

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

      op.open("images/Sem_Titulo.png");
      op.MatrizImage();
      op.twoPass();
      //op.secondPass();
      op.changeColor();
      op.save("images/teste22.png");
   }
}
