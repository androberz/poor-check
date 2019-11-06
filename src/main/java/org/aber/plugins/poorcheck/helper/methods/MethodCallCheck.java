package org.aber.plugins.poorcheck.helper.methods;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiMethodCallExpression;

public interface MethodCallCheck {

    void check(PsiMethodCallExpression methodCallExpression, AnnotationHolder annotationHolder);

}
