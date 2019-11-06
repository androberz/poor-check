package org.aber.plugins.poorcheck.helper.dto;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReferenceExpression;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MethodCallCheckData {

    private PsiReferenceExpression methodExpression;

    private PsiMethod psiMethod;

    private PsiClass targetClass;

}
