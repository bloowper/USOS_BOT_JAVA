import java.util.Vector;

public class testowanieJavy {
    public static void main(String[] args) {
        Vector<String> strings = new Vector<>();
        for(var i=0;i<10;i++){
            strings.add(String.valueOf(i));
        }
        System.out.println(strings);
        strings.remove(String.valueOf(5));
        System.out.println(strings);
    }
}
