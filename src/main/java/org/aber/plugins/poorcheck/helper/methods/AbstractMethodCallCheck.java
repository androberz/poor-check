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

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.util.PsiTypesUtil;
import lombok.AllArgsConstructor;
import org.aber.plugins.poorcheck.helper.AbstractCheck;
import org.aber.plugins.poorcheck.helper.dto.MethodCallCheckData;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.aber.plugins.poorcheck.helper.CheckUtils.doIfNonNull;

@AllArgsConstructor
abstract class AbstractMethodCallCheck extends AbstractCheck implements MethodCallCheck {

    protected String jdkClassName;
    protected String methodName;
    protected Integer argNumber;

    private Predicate<String> methodNamePredicate() {
        return aMethodName -> aMethodName.equals(methodName);
    }

    private Predicate<Integer> argNumberPredicate() {
        if (Objects.isNull(argNumber)) {
            return anArgNumber -> true;
        }

        return anArgNumber -> anArgNumber.equals(argNumber);
    }

    protected void baseMethodCheck(PsiMethodCallExpression methodCallExpression, Consumer<MethodCallCheckData> consumer) {
        doIfNonNull(methodCallExpression.getMethodExpression(), methodExpression -> {

            doIfNonNull(methodExpression.getReferenceName(), methodName -> {

                if (methodNamePredicate().test(methodName)) {

                    if (argNumberPredicate().test(methodCallExpression.getArgumentList().getExpressionCount())) {

                        doIfNonNull(methodCallExpression.resolveMethod(), psiMethod -> {

                            doIfNonNull(getPsiClass(psiMethod, jdkClassName), foundJdkClass -> {

                                doIfNonNull(foundJdkClass.findMethodBySignature(psiMethod, false), jdkMethod -> {

                                    MethodCallCheckData.MethodCallCheckDataBuilder methodCallCheckDataBuilder = MethodCallCheckData.builder()
                                            .methodExpression(methodExpression)
                                            .psiMethod(psiMethod);

                                    Optional<PsiClass> targetClassOpt = Optional.ofNullable(methodExpression.getType()).map(PsiTypesUtil::getPsiClass);

                                    targetClassOpt.ifPresent(methodCallCheckDataBuilder::targetClass);

                                    consumer.accept(methodCallCheckDataBuilder.build());
                                });
                            });
                        });
                    }
                }
            });
        });
    }
}
