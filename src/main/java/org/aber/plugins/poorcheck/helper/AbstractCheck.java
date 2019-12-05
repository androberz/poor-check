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
