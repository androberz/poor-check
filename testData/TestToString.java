import java.lang.*;

public class TestToString {
    public static void main(String[] args) {
        // check usage in comments
/*
        String badValue = new Test().toString();
*/
        // check not overridden toString() call
        String badValue = new TestToString().<warning descr="Not overridden">toString</warning>();
        System.out.println(badValue);

        System.out.println(new TestToString().<warning descr="Not overridden">toString</warning>());

        // check overridden toString() call
        String stringValue = new TestWithOverriddenToString().toString();
        System.out.println(stringValue);

        System.out.println(new TestWithOverriddenToString().toString());
    }

    private static class TestWithOverriddenToString {
        @Override
        public String toString() {
            return "Overridden toString() call";
        }
    }
}