package org.aber.plugins.poorcheck.helper.methods;

public class ToStringMethodCallCheck extends ObjectMethodCallCheck {

    private static final String METHOD_TO_STRING = "toString";
    private static final String TO_STRING_METHOD_TEMPLATE = "@Override\npublic String toString() {\n\n}\n";

    public ToStringMethodCallCheck() {
        super(METHOD_TO_STRING, 0);
    }

    @Override
    String getAddMethodTemplate() {
        return TO_STRING_METHOD_TEMPLATE;
    }
}
