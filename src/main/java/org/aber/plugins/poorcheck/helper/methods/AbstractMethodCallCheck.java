package org.aber.plugins.poorcheck.helper.methods;

import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import lombok.AllArgsConstructor;
import org.aber.plugins.poorcheck.helper.AbstractCheck;
import org.aber.plugins.poorcheck.helper.dto.MethodCallCheckData;

import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.aber.plugins.poorcheck.helper.CheckUtils.doIfInstanceIs;
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
        return anArgNumber -> anArgNumber.equals(argNumber);
    }

    void baseMethodCheck(PsiMethodCallExpression methodCallExpression, Consumer<MethodCallCheckData> consumer) {
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

                                    doIfNonNull(methodExpression.getQualifierExpression(), qualifierExpression -> {

                                        doIfInstanceIs(qualifierExpression.getType(), PsiClassReferenceType.class, targetClass -> {

                                            doIfNonNull(targetClass.resolve(), methodCallCheckDataBuilder::targetClass);

                                        });

                                    });

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
