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

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeMapper;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import org.aber.plugins.poorcheck.helper.types.HashTypeUsageCheck;

import java.util.Map;

import static org.aber.plugins.poorcheck.helper.CheckUtils.doIfNonNull;

public abstract class AbstractHashTypesMethodCallCheck extends AbstractMethodCallCheck {

    private HashTypeUsageCheck hashTypeUsageCheck = new HashTypeUsageCheck();

    public AbstractHashTypesMethodCallCheck(String jdkClassName, Map<String, Integer> methodNameAndArgNumber) {
        super(jdkClassName, methodNameAndArgNumber);
    }

    public void checkExpressionInferredType(PsiMethodCallExpressionImpl methodCallExpression,
                                            PsiMethodCallExpression warnCallExpression,
                                            AnnotationHolder annotationHolder) {
        doIfNonNull(methodCallExpression.getType(), expressionInferredType -> {

            PsiType mappedType = expressionInferredType.accept(new PsiTypeMapper() {
                @Override
                public PsiType visitClassType(PsiClassType classType) {
                    PsiType[] parameters = classType.getParameters();

                    if (parameters.length > 0) {
                        return parameters[0].accept(new PsiTypeMapper() {
                            @Override
                            public PsiType visitClassType(PsiClassType classType) {
                                return classType.rawType();
                            }
                        });
                    }

                    return null;
                }
            });

            doIfNonNull(mappedType, keyType -> {
                hashTypeUsageCheck.checkHashTypeForMethodsPresence(keyType, warnCallExpression, annotationHolder);
            });
        });
    }

    public void checkExpressionInferredType(PsiMethodCallExpressionImpl methodCallExpression,
                                            AnnotationHolder annotationHolder) {
        checkExpressionInferredType(methodCallExpression, methodCallExpression, annotationHolder);
    }
}
