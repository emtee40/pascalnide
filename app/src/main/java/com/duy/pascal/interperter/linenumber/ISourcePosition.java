/*
 *  Copyright (c) 2018 Tran Le Duy
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

package com.duy.pascal.interperter.linenumber;

import android.support.annotation.Nullable;

/**
 * Created by Duy on 1/30/2018.
 */

public interface ISourcePosition {
    /**
     * @return current line of source code or token
     */
    @Nullable
    LineNumber getLineNumber();
}
