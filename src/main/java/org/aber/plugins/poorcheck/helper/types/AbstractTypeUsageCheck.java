package org.aber.plugins.poorcheck.helper.types;

import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeVisitor;
import com.intellij.psi.impl.source.PsiImmediateClassType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.aber.plugins.poorcheck.helper.AbstractCheck;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.aber.plugins.poorcheck.helper.CheckUtils.doIfInstanceIs;
import static org.aber.plugins.poorcheck.helper.CheckUtils.doIfNonNull;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
abstract class AbstractTypeUsageCheck extends AbstractCheck implements TypeUsageCheck {

    private List<String> typesToCheck;

    void baseTypeCheck(PsiType type, PsiTypeVisitor<Boolean> typeVisitor) {
        doIfInstanceIs(type, PsiClassType.class, psiClassType -> {

            doIfNonNull(psiClassType.getClassName(), className -> {
                Supplier<Boolean> baseClassesCheck = () -> Stream.of(type.getSuperTypes())
                        .filter(superType -> superType instanceof PsiImmediateClassType)
                        .map(PsiImmediateClassType.class::cast)
                        .anyMatch(superType -> typesToCheck.contains(superType.getClassName()));

                if (typesToCheck.contains(className) || baseClassesCheck.get()) {
                    type.accept(typeVisitor);
                }
            });
        });
    }
}
