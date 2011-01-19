/**
 * 
 */
package src;

/**
 * @author Carlos
 * 
 */
public class Memory_3
{
   public int      key[];
   public int      prev[];
   public int      next[];
   public int      nilp;
   private Stack_3 memory_stack;

   public Memory_3(int memory_size)
   {
      memory_stack = new Stack_3(memory_size);

      nilp = 0;

      key = new int[memory_size];
      prev = new int[memory_size];
      next = new int[memory_size];

      key[nilp] = 0;
      prev[nilp] = nilp;
      next[nilp] = nilp;

      for (int k = 0; k < memory_size; k++)
      {
         memory_stack.push(k);
      }
   }

   public int allocate_object()
   {
      int x = memory_stack.pop();
      return x;
   }

   public void free_object(int x)
   {
      memory_stack.push(x);
   }
}