package src;

public class Stack_3
{

   private int[] s;
   public int    top;

   public Stack_3(int size_stack)
   {
      s = new int[size_stack];
      top = -1;
   }

   public boolean stack_empty()
   {
      if (top < 0)
      {
         return true;
      }
      else
      {
         return false;
      }
   }

   public void push(int x)
   {
      top = top + 1;
      s[top] = x;
   }

   public int pop()
   {
      if (stack_empty())
      {
         System.out.println("underflow");
         return 0;
      }
      else
      {
         top = top - 1;
         return (s[top + 1]);
      }
   }
}
