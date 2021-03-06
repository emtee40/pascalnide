package com.duy.pascal.interperter.declaration.lang.types.converter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;

public class NumberToCharType implements RuntimeValue {
    RuntimeValue other;


    public NumberToCharType(RuntimeValue other) {
        this.other = other;
    }


    @Nullable
    @Override
    public Object getValue(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        Number i = (Number) other.getValue(context, main);
        return (char) i.longValue();
    }

    @Nullable
    @Override
    public RuntimeType getRuntimeType(ExpressionContext context)
            throws Exception {
        return new RuntimeType(BasicType.Character, false);
    }

    @NonNull
    @Override
    public LineNumber getLineNumber() {
        return other.getLineNumber();
    }

    @Override
    public void setLineNumber(LineNumber lineNumber) {

    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws Exception {
        Object o = other.compileTimeValue(context);
        if (o != null) {
            return (char) ((Number) o).longValue();
        } else {
            return null;
        }
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws Exception {
        return new NumberToCharType(other.compileTimeExpressionFold(context));
    }

    @Override
    public String toString() {
        return other.toString();
    }

    @Override
    public AssignableValue asAssignableValue(ExpressionContext context) {
        return null;
    }
}
