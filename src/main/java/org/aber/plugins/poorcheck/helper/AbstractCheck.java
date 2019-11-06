package org.aber.plugins.poorcheck.helper;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMember;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

import static org.aber.plugins.poorcheck.helper.CheckUtils.getIfInstanceIs;
import static org.aber.plugins.poorcheck.helper.CheckUtils.getIfNonNull;

public abstract class AbstractCheck {

    protected static final String JAVA_LANG_OBJECT = "java.lang.Object";

    protected PsiClass getPsiClass(@NotNull PsiElement elementToGetProject, @NotNull String jdkClassName) {
        return getIfNonNull(elementToGetProject.getProject(), project ->
                JavaPsiFacade.getInstance(project).findClass(jdkClassName, GlobalSearchScope.allScope(project)));
    }

    protected boolean methodNotOverridden(@NotNull PsiClass jdkClass, @NotNull PsiClass checkedClass, @NotNull String methodName) {
        Boolean notOverridden = getIfNonNull(Arrays.stream(jdkClass.findMethodsByName(methodName, false)).findFirst().orElse(null), jdkMethod ->
                getIfNonNull(checkedClass.findMethodBySignature(jdkMethod, true), checkedClassMethod ->
                        getIfInstanceIs(checkedClassMethod.getNavigationElement(), PsiMember.class, navigationMethod ->
                                getIfNonNull(navigationMethod.getContainingClass(), ownerClass ->
                                        getIfNonNull(ownerClass.getQualifiedName(), className -> className.equals(jdkClass.getQualifiedName()))))));

        return Objects.nonNull(notOverridden) && notOverridden;
    }
}
