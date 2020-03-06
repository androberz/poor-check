import java.lang.*;
import java.util.*;
import lombok.EqualsAndHashCode;
import com.google.common.collect.ImmutableMap;

public class TestMaps {

    public static void main(String[] args) {
//        Map<TestMaps, Integer> badMap = new HashMap<>();

        Map<<warning descr="Not overridden equals()"><warning descr="Not overridden hashCode()">TestMaps</warning></warning>, Integer> badMap1 = new HashMap<>();
        System.out.println(badMap1);

        Map<<warning descr="Not overridden equals()"><warning descr="Not overridden hashCode()">Object</warning></warning>, Integer> badMap2 = new HashMap<>();
        System.out.println(badMap2);

        Map<<warning descr="Not overridden equals()">TestMapsWithHashCode</warning>, Integer> mapWithoutEquals = new HashMap<>();
        System.out.println(mapWithoutEquals);

        Map<TestMapsWithHashCodeAndEquals, Integer> goodMap1 = new HashMap<>();
        System.out.println(goodMap1);

        Map<String, Integer> goodMap2 = new HashMap<>();
        System.out.println(goodMap2);

        Map<TestMaps, String> guavaBadMap1 = <warning descr="Not overridden equals()"><warning descr="Not overridden hashCode()">ImmutableMap.of(new TestMaps(), "")</warning></warning>;
        Map<TestMapsWithHashCodeAndEquals, String> guavaGoodMap1 = ImmutableMap.of(new TestMapsWithHashCodeAndEquals(), "");
        Map<TestMaps, String> guavaBadMap2 = <warning descr="Not overridden equals()"><warning descr="Not overridden hashCode()">ImmutableMap.<TestMaps, String> builder()</warning></warning>.put(new TestMaps(), "").build();
        Map<TestMapsWithHashCodeAndEquals, String> guavaGoodMap2 = ImmutableMap.<TestMapsWithHashCodeAndEquals, String> builder().put(new TestMapsWithHashCodeAndEquals(), "").build();
    }

    public static class TestMapsWithHashCode {

        @Override
        public int hashCode() {
            return 1;
        }

    }

    public static class TestMapsWithHashCodeAndEquals {

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public boolean equals(Object other) {
            return true;
        }

    }

    private static HashMap<<warning descr="Not overridden equals()">TestMapsWithHashCode</warning>, String> doSmth1(HashMap<<warning descr="Not overridden equals()">TestMapsWithHashCode</warning>, String> hashMap) {
        return hashMap;
    }
}