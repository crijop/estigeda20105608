/**
 * 
 */
package src;

/**
 * @author Carlos
 *
 */
public class MakeSet
{
   public MakeSet parent;
   public int     nome;

   public MakeSet(int x)
   {
      this.parent = this;
      this.nome = x;
   }

   public int getId()
   {
      return nome;
   }

   public void setParent(MakeSet parent)
   {
      this.parent = parent;
   }

   public MakeSet getParent()
   {
      return parent;
   }

}
