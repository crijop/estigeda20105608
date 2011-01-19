/**
 * 
 */
package src;

/**
 * @author Carlos
 * 
 */
public class LinkedList_3
{
   public int[]     key;
   public int[]     prev;
   public int[]     next;
   private Memory_3 memory;
   public int       nilp;

   public LinkedList_3(int max_list_size)
   {
      memory = new Memory_3(max_list_size);

      key = memory.key;
      next = memory.next;
      prev = memory.prev;

      nilp = memory.nilp;
   }

   public void list_delete(int x)
   {
      next[prev[x]] = next[x];
      prev[next[x]] = prev[x];
   }

   public void list_insert(int x)
   {
      next[x] = next[nilp];
      prev[next[nilp]] = x;
      next[nilp] = x;
      prev[x] = nilp;
   }

   public int list_search(int k)
   {
      int x = next[nilp];
      while (x != nilp && key[x] != k)
      {
         x = next[x];
      }
      return x;
   }

   public int allocate_object()
   {
      return memory.allocate_object();
   }

   public void free_object(int x)
   {
      memory.free_object(x);
   }
}
