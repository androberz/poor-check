package org.aber.plugins.poorcheck.helper.methods;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeMapper;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import org.aber.plugins.poorcheck.helper.types.HashTypeUsageCheck;

import static org.aber.plugins.poorcheck.helper.CheckUtils.doIfInstanceIs;
import static org.aber.plugins.poorcheck.helper.CheckUtils.doIfNonNull;

public class StreamToMapCollectorCallCheck extends AbstractMethodCallCheck {

    private static final String JAVA_UTIL_STREAM_COLLECTORS = "java.util.stream.Collectors";
    private static final String METHOD_TO_MAP = "toMap";

    private HashTypeUsageCheck hashTypeUsageCheck = new HashTypeUsageCheck();

    public StreamToMapCollectorCallCheck() {
        super(JAVA_UTIL_STREAM_COLLECTORS, METHOD_TO_MAP, 2);
    }

    @Override
    public void check(PsiMethodCallExpression methodCallExpression, AnnotationHolder annotationHolder) {
        baseMethodCheck(methodCallExpression, methodCallCheckData -> {

            doIfNonNull(methodCallExpression.getParent(), collectorExpression -> {

                doIfInstanceIs(collectorExpression.getParent(), PsiMethodCallExpressionImpl.class, streamExpression -> {

                    doIfNonNull(streamExpression.getType(), expressionInferredType -> {

                        PsiType mappedType = expressionInferredType.accept(new PsiTypeMapper() {
                            @Override
                            public PsiType visitClassType(PsiClassType classType) {
                                PsiType[] parameters = classType.getParameters();

                                if (parameters.length > 0) {
                                    return parameters[0].accept(new PsiTypeMapper() {
                                        @Override
                                        public PsiType visitClassType(PsiClassType classType) {
                                            return classType.rawType();
                                        }
                                    });
                                }

                                return null;
                            }
                        });

                        doIfNonNull(mappedType, keyType -> {
                            hashTypeUsageCheck.checkHashTypeForMethodsPresence(keyType, methodCallExpression, annotationHolder);
                        });
                    });
                });
            });
        });
    }
}
