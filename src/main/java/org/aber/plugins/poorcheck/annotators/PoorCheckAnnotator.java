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
                    Arrays.stream(psiExpression.getParameters()).forEach(jvmParameter -> {
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
