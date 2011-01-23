package src;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * @author Carlos
 * 
 */
public class TwoPass
{
   private BufferedImage                img_marcas        = null;

   /*
    * opera-se directamente na imagem através dos seus rasters
    */

   private WritableRaster               wr_img_entrada    = null;
   private WritableRaster               wr_marcas         = null;

   private int                          NCOLS;
   private int                          NLINES;

   private Hashtable<Integer, int[]>    mapaCores         = null;
   private LinkedList<HashSet<Integer>> listaEquivalentes = null;

   /**
    * 
    * */
   public TwoPass(WritableRaster wr_img_entrada)
   {
      /*
       * img_entrada - imagem monocromatica com valores 0 ou 255
       */
      this.wr_img_entrada = wr_img_entrada;

      /*
       * inicializacao das estruturas de dados usadas no algoritmo
       */
      /*
       * dimensões da imagem
       */
      this.NCOLS = this.wr_img_entrada.getWidth();
      this.NLINES = this.wr_img_entrada.getHeight();

      /*
       * a img_marcas tem a mesma dimensão da imagem de entrada
       */
      this.img_marcas = new BufferedImage(this.NCOLS, this.NLINES,
               BufferedImage.TYPE_INT_RGB);
      this.wr_marcas = this.img_marcas.getRaster();

      /* inicializar as listas */
      this.mapaCores = new Hashtable<Integer, int[]>();
      this.listaEquivalentes = new LinkedList<HashSet<Integer>>();
   }

   /**
    * 
    * */
   public void save(String nome_ficheiro)
   {
      File ficheiro;
      ficheiro = new File(nome_ficheiro);

      try
      {
         ImageIO.write(this.img_marcas, "BMP", ficheiro);
      } catch (IOException ex)
      {
         Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   /**
    * 
    * */
   public BufferedImage executar()
   {
      int marca = 0;
      for (int line = 1; line < this.NLINES - 1; line++)
      {
         for (int col = 1; col < this.NCOLS - 1; col++)
         {
            /*
             * O algoritmo só opera em objectos. Os pixeis dos objectos têm
             * valor > 0
             */
            int pixel_objecto = this
                     .buscarPixel(col, line, this.wr_img_entrada);

            if (pixel_objecto > 0)
            {
               /*
                * 1. buscam-se as marcas vizinhas 2. contam-se quantas sao
                * diferentes umas das outras 3. se as marcas diferentes forem
                * zero entao não ha marca e aumenta-se marca.
                */
               int[] marcasVizinhas = this.buscarMarcasVizinhas(col, line);

               int marcasDiferentes = this
                        .contarMarcasDiferentes(marcasVizinhas);

               if (marcasDiferentes > 0)
               {
                  this.porPixel(col, line, this
                           .buscarMarcaMaisBaixa(marcasVizinhas), wr_marcas);
                  if (marcasDiferentes > 1)
                  {
                     /*
                      * Equivalencias
                      */
                     ArrayList<HashSet<Integer>> conjuntos = new ArrayList<HashSet<Integer>>(
                              4);

                     for (int k = 0; k < 4; k++)
                     {
                        conjuntos.add(new HashSet<Integer>());
                     }

                     HashSet<Integer> novoConjunto = new HashSet<Integer>();

                     for (int k = 0; k < 4; k++)
                     {
                        conjuntos.set(k, this.AcharConjunto(marcasVizinhas[k]));

                        if (marcasVizinhas[k] > 0)
                        {
                           if (conjuntos.get(k).isEmpty())
                           {
                              HashSet<Integer> conj = new HashSet<Integer>();
                              conj.add(marcasVizinhas[k]);
                              conjuntos.set(k, conj);
                           }
                        }
                     }

                     for (int k = 0; k < 4; k++)
                     {
                        if (!conjuntos.get(k).isEmpty())
                        {
                           this.listaEquivalentes.remove(conjuntos.get(k));
                           novoConjunto.addAll(conjuntos.get(k));
                        }
                     }
                     if (!novoConjunto.isEmpty())
                     {
                        this.listaEquivalentes.add(novoConjunto);
                     }
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
      this.resolverEquivalencias();
      this.colorizar();

      return this.img_marcas;
   }

   /**
    * 
    * */
   private void porPixel(int col, int line, int valor, WritableRaster wr)
   {
      int[] cor = new int[3];
      cor[0] = valor;
      cor[1] = valor;
      cor[2] = valor;
      wr.setPixel(col, line, cor);
   }

   /**
    * 
    * */
   private int buscarPixel(int col, int line, WritableRaster wr)
   {
      int[] cor = null;
      cor = wr.getPixel(col, line, cor);
      return cor[0];
   }

   /**
    * 
    * */
   private int[] buscarMarcasVizinhas(int col, int line)
   {
      int[] marcasVizinhas = new int[4];
      marcasVizinhas[0] = this.buscarPixel(col - 1, line, this.wr_marcas);
      marcasVizinhas[1] = this.buscarPixel(col - 1, line - 1, this.wr_marcas);
      marcasVizinhas[2] = this.buscarPixel(col, line - 1, this.wr_marcas);
      marcasVizinhas[3] = this.buscarPixel(col + 1, line - 1, this.wr_marcas);
      return marcasVizinhas;
   }

   /**
    * 
    * */
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

   /**
    * 
    * */
   private int buscarMarcaMaisBaixa(int[] marcasVizinhas)
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

   /**
    * 
    * */
   private HashSet<Integer> AcharConjunto(int k)
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

   /**
    * 
    * */
   private void resolverEquivalencias()
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
            int marca = this.buscarPixel(col, line, this.wr_marcas);

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
               this.wr_marcas.setPixel(col, line, cor);
            }
         }
      }
   }

   /**
    * 
    * */
   private void colorizar()
   {
      HashSet<Integer> conjuntoUnicoMarcas = new HashSet<Integer>();

      for (int line = 1; line < this.NLINES - 1; line++)
      {
         for (int col = 1; col < this.NCOLS - 1; col++)
         {
            int marca = this.buscarPixel(col, line, this.wr_marcas);
            if (marca > 0)
            {
               conjuntoUnicoMarcas.add(marca);
            }
         }
      }

      Random rng = new Random();

      for (Integer marca : conjuntoUnicoMarcas)
      {
         int marcaInteira = marca.intValue();

         int[] cor = new int[3];
         cor[0] = rng.nextInt(255);
         cor[1] = rng.nextInt(255);
         cor[2] = rng.nextInt(255);

         this.mapaCores.put(marcaInteira, cor);
      }
      for (int line = 1; line < this.NLINES - 1; line++)
      {
         for (int col = 1; col < this.NCOLS - 1; col++)
         {
            int marca = this.buscarPixel(col, line, this.wr_marcas);

            if (marca > 0)
            {
               int[] c = this.mapaCores.get(marca);
               this.wr_marcas.setPixel(col, line, c);
            }
         }
      }
   }
}