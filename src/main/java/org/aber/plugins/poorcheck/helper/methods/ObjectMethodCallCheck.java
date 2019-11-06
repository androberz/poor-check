package org.aber.plugins.poorcheck.helper.methods;

import com.intellij.codeInsight.daemon.impl.quickfix.AddMethodFix;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;

import static org.aber.plugins.poorcheck.helper.CheckUtils.doIfNonNull;

public abstract class ObjectMethodCallCheck extends AbstractMethodCallCheck {

    private final String warnMessage = "Not overridden";

    ObjectMethodCallCheck(String methodName, Integer argNumber) {
        super(JAVA_LANG_OBJECT, methodName, argNumber);
    }

    @Override
    public void check(PsiMethodCallExpression methodCallExpression, AnnotationHolder annotationHolder) {
        baseMethodCheck(methodCallExpression, methodCallCheckData -> {
            PsiMethod psiMethod = methodCallCheckData.getPsiMethod();

            doIfNonNull(psiMethod.getContainingClass(), methodClass -> {
                doIfNonNull(getPsiClass(psiMethod, jdkClassName), jdkClass -> {
                    if (methodNotOverridden(methodClass, jdkClass, methodName)) {
                        PsiReferenceExpression methodExpression = methodCallCheckData.getMethodExpression();
                        Annotation annotation = annotationHolder.createWarningAnnotation(methodExpression.getLastChild(), warnMessage);

                        doIfNonNull(methodCallCheckData.getTargetClass(), targetClass -> {
                            annotation.registerFix(new AddMethodFix(getAddMethodTemplate(), targetClass));
                        });
                    }
                });
            });
        });
    }

    abstract String getAddMethodTemplate();
}
