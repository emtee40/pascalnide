package com.duy.pascal.backend.declaration.lang.types;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.parse_exception.ParsingException;

public abstract class ObjectType extends TypeInfo {

    public abstract Type getMemberType(String name) throws ParsingException;

    @NonNull
    public String getEntityType() {
        return "object type";
    }

}
