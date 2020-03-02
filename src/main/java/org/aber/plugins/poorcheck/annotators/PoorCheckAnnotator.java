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
package org.aber.plugins.poorcheck.annotators;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiParameterImpl;
import org.aber.plugins.poorcheck.helper.methods.*;
import org.aber.plugins.poorcheck.helper.types.HashTypeUsageCheck;
import org.aber.plugins.poorcheck.helper.types.TypeUsageCheck;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static org.aber.plugins.poorcheck.helper.CheckUtils.doIfInstanceIs;

public class PoorCheckAnnotator implements Annotator {

    private final MethodCallCheck toStringCallCheck = new ToStringMethodCallCheck();
    private final MethodCallCheck equalsCallCheck = new EqualsMethodCallCheck();
    private final MethodCallCheck isPresentCheck = new IsPresentCallCheck();
    private final TypeUsageCheck hashTypeUsageCheck = new HashTypeUsageCheck();
    private final MethodCallCheck toMapCollectorCheck = new StreamToMapCollectorCallCheck();

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        element.accept(new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                doIfInstanceIs(element, PsiMethodCallExpression.class, psiMethodCallExpression -> {
                    toStringCallCheck.check(psiMethodCallExpression, holder);
                    equalsCallCheck.check(psiMethodCallExpression, holder);
                    isPresentCheck.check(psiMethodCallExpression, holder);
                    toMapCollectorCheck.check(psiMethodCallExpression, holder);
                });

                doIfInstanceIs(element, PsiExpression.class, psiExpression -> {
                    PsiType type = psiExpression.getType();

                    hashTypeUsageCheck.check(type, holder);
                });

                doIfInstanceIs(element, PsiMethod.class, psiExpression -> {
                    Arrays.stream(psiExpression.getParameterList().getParameters()).forEach(jvmParameter -> {
                        doIfInstanceIs(jvmParameter, PsiParameterImpl.class, parameter -> {
                            hashTypeUsageCheck.check(parameter.getType(), holder);
                        });
                    });

                    PsiType returnType = psiExpression.getReturnType();
                    hashTypeUsageCheck.check(returnType, holder);
                });
            }
        });
    }
}
