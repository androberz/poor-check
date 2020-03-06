/**
 * The MIT License (MIT)
 *
 * Copyright © «2019» «ANDREY BEREZIN»
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the “Software”), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.aber.plugins.poorcheck.helper.methods;

import com.google.common.collect.ImmutableMap;
import com.intellij.codeInsight.daemon.impl.quickfix.AddMethodFix;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;

import static org.aber.plugins.poorcheck.helper.CheckUtils.doIfNonNull;

public abstract class ObjectMethodCallCheck extends AbstractMethodCallCheck {

    private final String warnMessage = "Not overridden";
    private final String methodName;

    ObjectMethodCallCheck(String methodName, Integer argNumber) {
        super(JAVA_LANG_OBJECT, ImmutableMap.of(methodName, argNumber));
        this.methodName = methodName;
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
