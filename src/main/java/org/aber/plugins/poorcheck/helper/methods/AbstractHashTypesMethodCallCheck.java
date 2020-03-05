package org.aber.plugins.poorcheck.helper.methods;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeMapper;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import org.aber.plugins.poorcheck.helper.types.HashTypeUsageCheck;

import static org.aber.plugins.poorcheck.helper.CheckUtils.doIfNonNull;

public abstract class AbstractHashTypesMethodCallCheck extends AbstractMethodCallCheck {

    private HashTypeUsageCheck hashTypeUsageCheck = new HashTypeUsageCheck();

    public AbstractHashTypesMethodCallCheck(String jdkClassName, String methodName, Integer argNumber) {
        super(jdkClassName, methodName, argNumber);
    }

    public void checkExpressionInferredType(PsiMethodCallExpressionImpl methodCallExpression, AnnotationHolder annotationHolder) {
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
                hashTypeUsageCheck.checkHashTypeForMethodsPresence(keyType, methodCallExpression, annotationHolder);
            });
        });
    }
}
