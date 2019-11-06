package org.aber.plugins.poorcheck.helper.methods;

public class EqualsMethodCallCheck extends ObjectMethodCallCheck {

    private static final String METHOD_EQUALS = "equals";
    private static final String EQUALS_METHOD_TEMPLATE = "@Override\npublic boolean equals(Object other) {\nreturn \n}\n";

    public EqualsMethodCallCheck() {
        super(METHOD_EQUALS, 1);
    }

    @Override
    String getAddMethodTemplate() {
        return EQUALS_METHOD_TEMPLATE;
    }
}
