package org.aber.plugins.poorcheck.helper.types;

import com.google.common.collect.ImmutableList;
import com.intellij.codeInsight.daemon.impl.quickfix.AddMethodFix;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeVisitor;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.util.PsiTypesUtil;

import java.util.List;

import static org.aber.plugins.poorcheck.helper.CheckUtils.doIfInstanceIs;
import static org.aber.plugins.poorcheck.helper.CheckUtils.doIfNonNull;

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
        baseTypeCheck(type, new PsiTypeVisitor<Boolean>() {
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
