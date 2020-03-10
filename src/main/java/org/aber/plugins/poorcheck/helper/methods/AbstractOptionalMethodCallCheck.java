/**
 * The MIT License (MIT)
 *
 * Copyright © «2020» «ANDREY BEREZIN»
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
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import com.intellij.psi.util.PsiTypesUtil;

import java.util.Objects;

import static org.aber.plugins.poorcheck.helper.CheckUtils.doIfInstanceIs;
import static org.aber.plugins.poorcheck.helper.CheckUtils.doIfNonNull;

public abstract class AbstractOptionalMethodCallCheck extends AbstractMethodCallCheck {

    private static final String JAVA_UTIL_OPTIONAL = "java.util.Optional";
    private final String warnMessage;

    public AbstractOptionalMethodCallCheck(String methodName, String warnMessage) {
        super(JAVA_UTIL_OPTIONAL, ImmutableMap.of(methodName, 0));
        this.warnMessage = warnMessage;
    }

    @Override
    public void check(PsiMethodCallExpression methodCallExpression, AnnotationHolder annotationHolder) {
        baseMethodCheck(methodCallExpression, methodCallCheckData -> {
            PsiReferenceExpression methodExpression = methodCallCheckData.getMethodExpression();

            doIfInstanceIs(methodExpression.getFirstChild(), PsiMethodCallExpressionImpl.class, expressionBeforeMethod -> {

                doIfNonNull(expressionBeforeMethod.getType(), expressionType -> {
                    Boolean shouldWarn = expressionType.accept(new PsiTypeVisitor<>() {
                        @Override
                        public Boolean visitClassType(PsiClassType classType) {
                            PsiType[] parameters = classType.getParameters();
                            if (parameters.length == 0) {
                                return false;
                            }

                            PsiType paramType = parameters[0];
                            PsiClass paramClass = PsiTypesUtil.getPsiClass(paramType);

                            if (Objects.isNull(paramClass)) {
                                return false;
                            }

                            String paramClassName = paramClass.getQualifiedName();

                            return Objects.nonNull(paramClassName) && paramClassName.equals(jdkClassName);
                        }
                    });

                    if (shouldWarn) {
                        annotationHolder.createWarningAnnotation(methodExpression.getLastChild(), warnMessage);
                    }
                });
            });
        });
    }
}
