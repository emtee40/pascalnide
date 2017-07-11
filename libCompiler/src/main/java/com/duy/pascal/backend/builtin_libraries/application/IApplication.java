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

package com.duy.pascal.backend.builtin_libraries.application;

/**
 * Created by Duy on 16-Jun-17.
 */

public interface IApplication {
    void onCreate();

    void onShow();

    void onActive();

    void onPaint();

    void onResize();

    void onCloseQuery();

    void onClose();

    void onDeactivate();

    void onHide();

    void onDestroy();

}