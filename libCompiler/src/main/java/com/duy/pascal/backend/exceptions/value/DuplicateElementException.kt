/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.backend.exceptions.value

import com.duy.pascal.backend.exceptions.ParsingException
import com.duy.pascal.backend.linenumber.LineInfo

/**
 * Created by Duy on 27-May-17.
 */

class DuplicateElementException : ParsingException {

    private val element: Any
    private val container: Any

    constructor(element: Any, container: Any, line: LineInfo?) : super(line) {
        this.element = element
        this.container = container
    }

    override fun getLocalizedMessage(): String {
        return "Duplicate element $element in $container"
    }
}