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

package com.duy.pascal.backend.system_function.builtin;


import android.support.annotation.NonNull;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.instructions.FieldReference;
import com.duy.pascal.backend.ast.variablecontext.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.declaration.lang.types.ArgumentType;
import com.duy.pascal.backend.declaration.lang.types.Type;
import com.duy.pascal.backend.declaration.lang.types.JavaClassBasedType;
import com.duy.pascal.backend.declaration.lang.types.PointerType;
import com.duy.pascal.backend.declaration.lang.types.RuntimeType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * d
 */

public class NewInstanceObject implements IMethodDeclaration {
    private ArgumentType[] argumentTypes =
            {new RuntimeType(new JavaClassBasedType(Object.class), true)};

    @Override
    public String getName() {
        return "new".toLowerCase();
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        RuntimeValue pointer = arguments[0];
        return new InstanceObjectCall(pointer, pointer.getRuntimeType(f), line);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line, RuntimeValue[] values, ExpressionContext f) throws ParsingException {
        return generateCall(line, values, f);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        return argumentTypes;
    }

    @Override
    public Type returnType() {
        return null;
    }

    @Override
    public String description() {
        return null;

    }

    private class InstanceObjectCall extends FunctionCall {
        private RuntimeValue pointer;
        private RuntimeType type;
        private LineInfo line;

        InstanceObjectCall(RuntimeValue value, RuntimeType type, LineInfo line) {
            this.pointer = value;
            this.type = type;
            this.line = line;
        }

        @Override
        public RuntimeType getRuntimeType(ExpressionContext f) throws ParsingException {
            return new RuntimeType(pointer.getRuntimeType(f).declType, false);
        }

        @NonNull
        @Override
        public LineInfo getLineNumber() {
            return line;
        }

        @Override
        public void setLineNumber(LineInfo lineNumber) {

        }

        @Override
        public Object compileTimeValue(CompileTimeContext context) {
            return null;
        }

        @Override
        public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
                throws ParsingException {
            return new InstanceObjectCall(pointer, type, line);
        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws ParsingException {
            return new InstanceObjectCall(pointer, type, line);
        }

        @Override
        protected String getFunctionName() {
            return "new";
        }

        @Override
        public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            //get references of variable
            FieldReference pointer = (FieldReference) this.pointer.getValue(f, main);

            //get class type of variable
            JavaClassBasedType javaType = (JavaClassBasedType) ((PointerType) type.declType).pointedToType;

            Class<?> clazz = javaType.getStorageClass();

            Constructor<?> constructor;
            try {
                constructor = clazz.getConstructor();
                try {
                    Object value = constructor.newInstance();
                    pointer.set(value);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
