import java.lang.*;
import lombok.EqualsAndHashCode;

public class TestEquals {
    public static void main(String[] args) {
        // check usage in comments
/*
        String badValue = new TestEquals().equals(new TestEquals());
*/
        // check not overridden equals() call
        boolean badEquals = new TestEquals().<warning descr="Not overridden">equals</warning>(new TestEquals());
        System.out.println(badEquals);

        // check overridden equals() call
        boolean goodEquals = new TestWithOverriddenEquals().equals(new TestWithOverriddenEquals());
        System.out.println(goodEquals);
    }

    private static class TestWithOverriddenEquals {
        @Override
        public boolean equals(Object other) {
            if (other instanceof TestWithOverriddenEquals) {
                return true;
            }
            return false;
        }
    }

    @EqualsAndHashCode
    private static class TestWithLombokEquals {
    }
}