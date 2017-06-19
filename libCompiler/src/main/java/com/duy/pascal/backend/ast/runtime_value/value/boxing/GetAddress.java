package com.duy.pascal.backend.ast.runtime_value.value.boxing;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.variablecontext.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.debugable.DebuggableReturnValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.value.UnAssignableTypeException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.declaration.lang.types.PointerType;
import com.duy.pascal.backend.declaration.lang.types.RuntimeType;

public class GetAddress extends DebuggableReturnValue {
    private final AssignableValue target;
    private LineInfo line;

    public GetAddress(AssignableValue target) throws UnAssignableTypeException {
        this.line = target.getLineNumber();
        this.target = target;
    }


    @Override
    public RuntimeType getRuntimeType(ExpressionContext f) throws ParsingException {
        return new RuntimeType(new PointerType(target.getRuntimeType(f).declType),
                false);
    }

    @Override
    public String toString() {
        return "@" + target;
    }

    @NonNull
    @Override
    public LineInfo getLineNumber() {
        return target.getLineNumber();
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        return null;
    }

    @Override
    public boolean canDebug() {
        return false;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        return target.getReference(f, main);
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return this;
    }
}
