package src;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;

/**
 * @author Carlos
 *
 */
public class TwoPass
{
   public BufferedImage                 img_marcas        = null;
   public BufferedImage                 img_entrada       = null;

   /*
    * opera-se directamente na imagem através dos seus rasters
    */
   private WritableRaster               wr_entrada        = null;
   private WritableRaster               wr_marcas         = null;

   private int                          NCOLS;
   private int                          NLINES;

   /**
    * carlos Palma
    */
   private LinkedList<HashSet<Integer>> listaEquivalentes = null;

   public TwoPass(BufferedImage img_entrada)
   {
      /*
       * img_entrada - imagem monocromatica com valores 0 ou 255
       */
      this.img_entrada = img_entrada;
      /*
       * inicializacao das estruturas de dados usadas no algoritmo
       */

      /*
       * dimensões da imagem
       */
      this.NCOLS = this.img_entrada.getWidth();
      this.NLINES = this.img_entrada.getHeight();

      /*
       * a img_marcas tem a mesma dimensão da imagem de entrada
       */
      this.img_marcas = new BufferedImage(this.NCOLS, this.NLINES,
               BufferedImage.TYPE_INT_RGB);

      this.wr_entrada = this.img_entrada.getRaster();
      this.wr_marcas = this.img_marcas.getRaster();

      this.listaEquivalentes = new LinkedList<HashSet<Integer>>();
   }

   public int buscarPixel(int col, int line, WritableRaster wr)
   {
      int[] cor = null;

      cor = wr.getPixel(col, line, cor);

      return cor[0];
   }

   private void colorizar()
   {
      HashSet<Integer> conjuntoUnicoMarcas = new HashSet<Integer>();

      int marcasNaImagem = 0;
      for (int col = 1; col < this.NCOLS - 1; col++)
      {
         for (int line = 1; line < this.NLINES - 1; line++)
         {
            int marca = this.buscarPixel(col, line, this.wr_marcas);
            if (marca > 0)
            {
               conjuntoUnicoMarcas.add(marca);
            }
         }
      }

      Hashtable<Integer, int[]> mapaCores = new Hashtable<Integer, int[]>();

      Random rng = new Random();
      for (Integer marca : conjuntoUnicoMarcas)
      {
         int marcaInteira = marca.intValue();

         int[] cor = new int[3];
         cor[0] = rng.nextInt(255);
         cor[1] = rng.nextInt(255);
         cor[2] = rng.nextInt(255);
         // System.out.print(marcaInteira);
         // System.out.print(" " + cor[0]);
         // System.out.print(" " + cor[1]);
         // System.out.print(" " + cor[2]);
         // System.out.println();
         mapaCores.put(marcaInteira, cor);
      }
      for (int col = 1; col < this.NCOLS - 1; col++)
      {
         for (int line = 1; line < this.NLINES - 1; line++)
         {
            int marca = this.buscarPixel(col, line, this.wr_marcas);

            if (marca > 0)
            {
               int[] c = mapaCores.get(marca);

               this.wr_marcas.setPixel(col, line, c);
            }
         }
      }
   }

   private int[] buscarMarcasVizinhas(int col, int line)
   {
      int[] marcasVizinhas = new int[4];
      marcasVizinhas[0] = this.buscarPixel(col - 1, line, wr_marcas);
      marcasVizinhas[1] = this.buscarPixel(col - 1, line - 1, wr_marcas);
      marcasVizinhas[2] = this.buscarPixel(col, line - 1, wr_marcas);
      marcasVizinhas[3] = this.buscarPixel(col + 1, line - 1, wr_marcas);
      return marcasVizinhas;
   }

   private int contarMarcasDiferentes(int[] marcasVizinhas)
   {
      int NVIZINHOS = 4;

      HashSet<Integer> conjuntoMarcasDiferentes = new HashSet<Integer>();

      for (int k = 0; k < NVIZINHOS; k++)
      {
         if (marcasVizinhas[k] > 0)
         {
            conjuntoMarcasDiferentes.add(marcasVizinhas[k]);
         }
      }
      return conjuntoMarcasDiferentes.size();
   }

   private void porPixel(int col, int line, int valor, WritableRaster wr)
   {
      int[] cor = new int[3];
      cor[0] = valor;
      cor[1] = valor;
      cor[2] = valor;
      wr.setPixel(col, line, cor);
   }

   public int buscarMarcaMaisBaixa(int[] marcasVizinhas)
   {
      int minimo = Integer.MAX_VALUE;
      for (int k = 0; k < 4; k++)
      {
         if (marcasVizinhas[k] > 0 && marcasVizinhas[k] < minimo)
         {
            minimo = marcasVizinhas[k];
         }
      }
      return minimo;
   }

   public BufferedImage executar()
   {
      /*
       * execucao do algoritmo
       */

      /*
       * o algoritmo opera em todos os pixel com a excepção da margem senão pode
       * dar-se uma excepção.
       */
      int marca = 0;

      for (int col = 1; col < this.NCOLS - 1; col++)
      {
         for (int line = 1; line < this.NLINES - 1; line++)
         {
            /*
             * o algoritmo só opera em objectos os pixel dos objectos têm valor
             * superior a 0
             */
            int valor_objecto = this.buscarPixel(col, line, this.wr_entrada);

            if (valor_objecto > 0)
            {

               /*
                * 1. buscam-se as marcas vizinhas 2. contam-se quantas sao
                * diferentes umas das outras 3. se as marcas diferentes forem
                * zero entao não ha marca e aumenta-se marca.
                */
               int[] marcasVizinhas = new int[4];
               marcasVizinhas = this.buscarMarcasVizinhas(col, line);

               int contagemMarcasDiferentes = this
                        .contarMarcasDiferentes(marcasVizinhas);

               if (contagemMarcasDiferentes > 0)
               {
                  if (contagemMarcasDiferentes == 1)
                  {
                     this.porPixel(col, line, this
                              .buscarMarcaMaisBaixa(marcasVizinhas),
                              this.wr_marcas);
                  }
                  else
                  {
                     /*
                      * equivalencias
                      */

                  }
               }
               else
               {
                  marca = marca + 1;
                  this.porPixel(col, line, marca, this.wr_marcas);
               }

            }
         }
      }
      /**
       * carlos Palma
       */
      this.resolverEquivalencias();
      this.colorizar();
      return this.img_marcas;
   }

   /**
    * carlos Palma
    */
   public void resolverEquivalencias()
   {
      Hashtable<Integer, Integer> mapaEquivalente = new Hashtable<Integer, Integer>();
      int valor = 0;
      for (HashSet<Integer> setX : this.listaEquivalentes)
      {
         valor = valor + 1;
         for (Integer marca : setX)
         {
            mapaEquivalente.put(marca, valor);
         }
      }
      for (int line = 1; line < this.NLINES - 1; line++)
      {
         for (int col = 1; col < this.NCOLS - 1; col++)
         {
            int marca = this.buscarPixel(col, line, this.wr_entrada);

            int[] cor = new int[3];
            if (marca > 0)
            {

               if (mapaEquivalente.containsKey(marca))
               {
                  cor[0] = mapaEquivalente.get(marca);
                  cor[1] = mapaEquivalente.get(marca);
                  cor[2] = mapaEquivalente.get(marca);
               }
               else
               {
                  valor = valor + 1;
                  cor[0] = valor;
                  cor[1] = valor;
                  cor[2] = valor;
               }
               this.porPixel(col, line, valor, this.wr_entrada);
            }
         }
      }
   }

   /**
    * carlos Palma
    */
   private HashSet<Integer> findSet(int k)
   {
      HashSet<Integer> conjunto = new HashSet<Integer>();
      for (HashSet<Integer> setX : this.listaEquivalentes)
      {
         if (k > 0)
         {
            if (setX.contains(k))
            {
               conjunto = setX;

               return conjunto;
            }
         }
      }
      return conjunto;
   }
}
