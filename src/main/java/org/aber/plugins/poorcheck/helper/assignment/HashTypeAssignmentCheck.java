package org.aber.plugins.poorcheck.helper.assignment;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;

import static org.aber.plugins.poorcheck.helper.CheckUtils.doIfInstanceIs;

public class HashTypeAssignmentCheck extends AbstractAssignmentCheck {

    public HashTypeAssignmentCheck() {
        super("com.google.common.collect.ImmutableSet", "of", null);
    }

    @Override
    public void check(PsiMethodCallExpression methodCallExpression, AnnotationHolder annotationHolder) {
        baseMethodCheck(methodCallExpression, methodCallCheckData -> {

            doIfInstanceIs(methodCallExpression, PsiMethodCallExpressionImpl.class, builderCallExpression -> {

                checkExpressionInferredType(builderCallExpression, annotationHolder);

            });

        });
    }
}
