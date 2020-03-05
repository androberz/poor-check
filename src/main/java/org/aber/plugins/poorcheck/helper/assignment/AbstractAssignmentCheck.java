package org.aber.plugins.poorcheck.helper.assignment;

import org.aber.plugins.poorcheck.helper.methods.AbstractHashTypesMethodCallCheck;

public abstract class AbstractAssignmentCheck extends AbstractHashTypesMethodCallCheck {

    public AbstractAssignmentCheck(String jdkClassName, String methodName, Integer argNumber) {
        super(jdkClassName, methodName, argNumber);
    }
}
