import java.util.Scanner;

public class Main {

    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("Введите символ ");
        char ch = scan.nextLine().charAt(0);
        System.out.println("Введите высоту ");
        int height = Integer.parseInt(scan.nextLine());
        Triangle triangle = new Triangle();
        triangle.print(ch,height);
    }
}
