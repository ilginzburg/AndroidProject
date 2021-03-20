public class Triangle {

    void print(char ch, int height)
    {
        int k = height +1;
        for(int i=0; i<=height; ++i)
        {
            for (int j = 0; j < k; ++j)
            {
                if(j > (k- i*2))
                    System.out.print(ch);
                else
                    System.out.print(" ");
            }
            k++;
            System.out.println();
        }
    }
}
