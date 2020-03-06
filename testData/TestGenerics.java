import java.lang.*;
import java.util.*;
import lombok.EqualsAndHashCode;
import com.google.common.collect.ImmutableSet;

public class TestGenerics {

    public static void main(String[] args) {
//        Set<TestGenerics> badSet = new HashSet<>();

        Set<<warning descr="Not overridden equals()"><warning descr="Not overridden hashCode()">TestGenerics</warning></warning>> badSet1 = new HashSet<>();
        System.out.println(badSet1);

        Set<<warning descr="Not overridden equals()"><warning descr="Not overridden hashCode()">Object</warning></warning>> badSet2 = new HashSet<>();
        System.out.println(badSet2);

        Set<<warning descr="Not overridden equals()">TestGenericsWithHashCode</warning>> setWithoutEquals = new HashSet<>();
        System.out.println(setWithoutEquals);

        Set<TestGenericsWithHashCodeAndEquals> goodSet1 = new HashSet<>();
        System.out.println(goodSet1);

        Set<String> goodSet2 = new HashSet<>();
        System.out.println(goodSet2);

        Set<TestGenerics> guavaBadSet1 = <warning descr="Not overridden equals()"><warning descr="Not overridden hashCode()">ImmutableSet.of(new TestGenerics())</warning></warning>;
        Set<TestGenerics> guavaBadSet2 = <warning descr="Not overridden equals()"><warning descr="Not overridden hashCode()">ImmutableSet.<TestGenerics> builder()</warning></warning>.add(new TestGenerics()).build();
    }

    public static class TestGenericsWithHashCode {

        @Override
        public int hashCode() {
            return 1;
        }
    }

    public static class TestGenericsWithHashCodeAndEquals {

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public boolean equals(Object other) {
            return true;
        }
    }

    @EqualsAndHashCode
    public static class TestGenericsWithLombokHashCode {
    }
}