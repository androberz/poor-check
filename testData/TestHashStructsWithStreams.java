import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;

public class TestHashStructsWithStreams {

    public static void main(String[] args) {

        List<TestStructWithHashCode> aList1 = new ArrayList<>();
        aList1.add(new TestStructWithHashCode());

        Map<TestStructWithHashCode, String> resultMap1 = aList1.stream().collect(<warning descr="Not overridden equals()">Collectors.toMap(val -> val, val -> "some val")</warning>);

        List<TestStructWithHashCodeAndEquals> aList2 = new ArrayList<>();
        aList2.add(new TestStructWithHashCodeAndEquals());

        Map<TestStructWithHashCodeAndEquals, String> resultMap2 = aList2.stream().collect(Collectors.toMap(val -> val, val -> "some val"));
    }

    public static class TestStructWithHashCode {

        @Override
        public int hashCode() {
            return 1;
        }

    }

    public static class TestStructWithHashCodeAndEquals {

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public boolean equals(Object other) {
            return true;
        }

    }
}