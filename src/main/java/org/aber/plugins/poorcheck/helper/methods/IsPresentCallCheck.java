package org.aber.plugins.poorcheck.helper.methods;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import com.intellij.psi.util.PsiTypesUtil;

import java.util.Objects;

import static org.aber.plugins.poorcheck.helper.CheckUtils.doIfInstanceIs;
import static org.aber.plugins.poorcheck.helper.CheckUtils.doIfNonNull;

public class IsPresentCallCheck extends AbstractMethodCallCheck {

    private static final String JAVA_UTIL_OPTIONAL = "java.util.Optional";
    private static final String METHOD_IS_PRESENT = "isPresent";

    private final String warnMessage = "Possibly always true";

    public IsPresentCallCheck() {
        super(JAVA_UTIL_OPTIONAL, METHOD_IS_PRESENT, 0);
    }

    @Override
    public void check(PsiMethodCallExpression methodCallExpression, AnnotationHolder annotationHolder) {
        baseMethodCheck(methodCallExpression, methodCallCheckData -> {
            PsiReferenceExpression methodExpression = methodCallCheckData.getMethodExpression();

            doIfInstanceIs(methodExpression.getFirstChild(), PsiMethodCallExpressionImpl.class, expressionBeforeMethod -> {

                doIfNonNull(expressionBeforeMethod.getType(), expressionType -> {
                    Boolean shouldWarn = expressionType.accept(new PsiTypeVisitor<Boolean>() {
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
