package org.aber.plugins.poorcheck.helper.types;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiType;

public interface TypeUsageCheck {

    void check(PsiType type, AnnotationHolder annotationHolder);

}
