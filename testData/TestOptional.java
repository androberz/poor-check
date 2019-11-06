import java.lang.*;
import java.util.Optional;

public class TestOptional {

    public static void main(String[] args) {
        Optional<Integer> opt1 = Optional.of(1);
        Optional<Integer> opt2 = Optional.of(2);

        boolean badPresent = opt1.map(val -> opt2).<warning descr="Possibly always true">isPresent</warning>();
        boolean goodPresent = opt1.flatMap(val -> opt2).isPresent();

        if (opt1.map(val -> opt2).<warning descr="Possibly always true">isPresent</warning>()) {
            System.out.println("Bad usage");
        }

        if (opt1.flatMap(val -> opt2).isPresent()) {
            System.out.println("Good usage");
        }
    }
}