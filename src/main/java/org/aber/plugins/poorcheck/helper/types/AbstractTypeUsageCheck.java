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
