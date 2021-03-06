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
package org.aber.plugins.poorcheck.helper.assignment;

import com.google.common.collect.ImmutableMap;

public class GuavaSetAssignmentCheck extends AbstractGuavaAssignmentCheck {

    private static final String CLASS_IMMUTABLE_SET = "com.google.common.collect.ImmutableSet";
    private static final String METHOD_OF = "of";
    private static final String METHOD_BUILDER = "builder";

    public GuavaSetAssignmentCheck() {
        super(CLASS_IMMUTABLE_SET, ImmutableMap.<String, Integer> builder()
                .put(METHOD_OF, -1)
                .put(METHOD_BUILDER, 0)
                .build());
    }
}
