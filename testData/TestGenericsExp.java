import java.lang.*;
import java.util.*;
import lombok.EqualsAndHashCode;

public class TestGenericsExp {

    public static void main(String[] args) {
        System.out.println(new HashSet<<warning descr="Not overridden equals()"><warning descr="Not overridden hashCode()">TestGenericsExp</warning></warning>>());
        System.out.println(new HashSet<String>());
        System.out.println(new HashMap<<warning descr="Not overridden equals()"><warning descr="Not overridden hashCode()">TestGenericsExp</warning></warning>, String>());
        System.out.println(new LinkedHashSet<<warning descr="Not overridden equals()"><warning descr="Not overridden hashCode()">TestGenericsExp</warning></warning>>());
        System.out.println(new LinkedHashSet<String>());
    }
}