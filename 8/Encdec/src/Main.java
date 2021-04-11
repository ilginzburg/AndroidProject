import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main {
    public static void main(String[] args) {
        final int key = 4567;
        String textIn = "\tКИНЖАЛ\n (М.Ю.Лермонтов) \n" +
                "Люблю тебя, булатный мой кинжал,\n" +
                "Товарищ светлый и холодный.\n" +
                "Задумчивый грузин на месть тебя ковал,\n" +
                "На грозный бой точил черкес свободный.\n" +
                "Лилейная рука тебя мне поднесла\n" +
                "В знак памяти, в минуту расставанья,\n" +
                "И в первый раз не кровь вдоль по тебе текла,\n" +
                "Но светлая слеза — жемчужина страданья.\n" +
                "И черные глаза, остановясь на мне,\n" +
                "Исполнены таинственной печали,\n" +
                "Как сталь твоя при трепетном огне,\n" +
                "То вдруг тускнели, то сверкали.\n" +
                "Ты дан мне в спутники, любви залог немой,\n" +
                "И страннику в тебе пример не бесполезный:\n" +
                "Да, я не изменюсь и буду тверд душой,\n" +
                "Как ты, как ты, мой друг железный.";
        String textOut = "";
        textIn +=  " \n(sent on "+ new SimpleDateFormat("yyyy MMM dd HH:mm:ss").format(Calendar.getInstance().getTime()) +")";

        // Encoding
        for (int i = 0; i < textIn.length(); ++i)
            textIn = textIn.substring(0,i) + (char)(textIn.charAt(i)+key) + textIn.substring(i+1,textIn.length());
        System.out.println(textIn);

        // Decoding
        textOut = textIn;
        for (int i = 0; i < textIn.length(); ++i)
            textOut = textOut.substring(0,i) + (char)(textIn.charAt(i)-key) + textIn.substring(i+1,textIn.length());
        System.out.println(textOut);
    }
}
