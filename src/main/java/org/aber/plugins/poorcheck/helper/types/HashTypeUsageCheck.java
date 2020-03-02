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
package org.aber.plugins.poorcheck.helper.types;

import com.google.common.collect.ImmutableList;
import com.intellij.codeInsight.daemon.impl.quickfix.AddMethodFix;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.util.PsiTypesUtil;

import java.util.List;
import java.util.Objects;

import static org.aber.plugins.poorcheck.helper.CheckUtils.doIfInstanceIs;
import static org.aber.plugins.poorcheck.helper.CheckUtils.doIfNonNull;
import static org.aber.plugins.poorcheck.helper.CheckUtils.getIfInstanceIs;

public class HashTypeUsageCheck extends AbstractTypeUsageCheck {

    private static final String METHOD_HASH_CODE = "hashCode";
    private static final String METHOD_EQUALS = "equals";

    private static final String HASH_CODE_METHOD_TEMPLATE = "@Override\npublic int hashCode() {\nreturn \n}\n";
    private static final String EQUALS_METHOD_TEMPLATE = "@Override\npublic boolean equals(Object other) {\nreturn \n}\n";

    private static final List<String> HASH_CODE_REQUIRED_CLASSES = ImmutableList.<String>builder()
            .add("HashMap")
            .add("HashSet")
            .build();

    public HashTypeUsageCheck() {
        super(HASH_CODE_REQUIRED_CLASSES);
    }

    @Override
    public void check(PsiType type, AnnotationHolder annotationHolder) {
        baseTypeCheck(type, new PsiTypeVisitor<>() {
            @Override
            public Boolean visitClassType(PsiClassType classType) {
                PsiType[] parameters = classType.getParameters();
                if (parameters.length > 0) {

                    doIfInstanceIs(parameters[0], PsiClassReferenceType.class, keyType -> {

                        checkHashTypeForMethodsPresence(keyType, keyType.getReference().getElement(), annotationHolder);

                    });
                }

                return true;
            }
        });
    }

    public void checkHashTypeForMethodsPresence(PsiType type, PsiElement warnElement, AnnotationHolder annotationHolder) {
        doIfInstanceIs(type, PsiClassType.class, keyType -> {

            doIfNonNull(PsiTypesUtil.getPsiClass(keyType), keyTypeClass -> {

                Boolean isTypeParam = getIfInstanceIs(keyTypeClass, PsiTypeParameter.class, param -> true);

                if (Objects.nonNull(isTypeParam) && isTypeParam) {
                    return;
                }

                doIfNonNull(getPsiClass(warnElement, JAVA_LANG_OBJECT), psiClass -> {
                    if (methodNotOverridden(psiClass, keyTypeClass, METHOD_HASH_CODE)) {
                        Annotation annotation = annotationHolder.createWarningAnnotation(warnElement, "Not overridden hashCode()");
                        annotation.registerFix(new AddMethodFix(HASH_CODE_METHOD_TEMPLATE, keyTypeClass));
                    }
                    if (methodNotOverridden(psiClass, keyTypeClass, METHOD_EQUALS)) {
                        Annotation annotation = annotationHolder.createWarningAnnotation(warnElement, "Not overridden equals()");
                        annotation.registerFix(new AddMethodFix(EQUALS_METHOD_TEMPLATE, keyTypeClass));
                    }
                });
            });
        });
    }
}
