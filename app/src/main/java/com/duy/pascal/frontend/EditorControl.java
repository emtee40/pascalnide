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

package com.duy.pascal.frontend;

import android.view.View;

public interface EditorControl {
    boolean doCompile();

    void saveAs();

    void findAndReplace();

    void runProgram();

    boolean isAutoSave();

    void saveFile();

    void showDocumentActivity();

    void createNewSourceFile(View view);

    void goToLine();

    void formatCode();

    void reportBug();

    void openTool();

    void undo();

    void redo();

    void paste();

    void copyAll();

    void selectThemeFont();
}