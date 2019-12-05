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
package org.aber.plugins.poorcheck.annotators;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.testFramework.IdeaTestUtil;
import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.PsiTestUtil;
import com.intellij.testFramework.fixtures.DefaultLightProjectDescriptor;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;

public class PoorCheckTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        PsiTestUtil.addLibrary(myFixture.getModule(), "lombok", getBasePath() + "libs", "lombok-1.18.6.jar");
    }

    @Override
    protected String getTestDataPath() {
        return "testData";
    }

    @NotNull
    @Override
    protected LightProjectDescriptor getProjectDescriptor() {
        return new DefaultLightProjectDescriptor() {
            @Override
            public void configureModule(@NotNull Module module, @NotNull ModifiableRootModel model, @NotNull ContentEntry contentEntry) {
                super.configureModule(module, model, contentEntry);
                String libsPath = getBasePath() + "libs";
                PsiTestUtil.addLibrary(module, "lombok", libsPath, "lombok-1.18.6.jar");
            }

            @Override
            public Sdk getSdk() {
                return IdeaTestUtil.getMockJdk18();
            }
        };
    }

    public void testHighlightingForToString() {
        myFixture.configureByFiles("TestToString.java");

        myFixture.checkHighlighting();
    }

    public void testHighlightingForEquals() {
        myFixture.configureByFiles("TestEquals.java");

        myFixture.checkHighlighting();
    }

    public void testHighlightingForMapKeys() {
        myFixture.configureByFiles("TestMaps.java");

        myFixture.checkHighlighting();
    }

    public void testHighlightingForGenericsKeys() {
        myFixture.configureByFiles("TestGenerics.java");

        myFixture.checkHighlighting();
    }

    public void testHighlightingForOptionalIsPresent() {
        myFixture.configureByFiles("TestOptional.java");

        myFixture.checkHighlighting();
    }

    public void testHighlightingForGenericsExp() {
        myFixture.configureByFiles("TestGenericsExp.java");

        myFixture.checkHighlighting();
    }

    public void testHighlightingForHashKeysWithStreams() {
        myFixture.configureByFile("TestHashStructsWithStreams.java");

        myFixture.checkHighlighting();
    }
}