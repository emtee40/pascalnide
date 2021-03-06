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

package com.duy.pascal.interperter.tokens.ignore;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.tokens.Token;

public class CompileDirectiveToken extends Token {
    private String message;

    public CompileDirectiveToken(LineNumber line, String message) {
        super(line);
        this.message = message;
    }


    @Override
    public String toString() {
        return message;
    }

    @NonNull
    @Override
    public String toCode() {
        return message;
    }

    public String[] getMessage() {
        String msg = message.subSequence(1, message.length() - 1).toString();
        return msg.split("\\s+");
    }
}
