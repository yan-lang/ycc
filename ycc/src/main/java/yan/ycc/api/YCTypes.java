package yan.ycc.api;

import yan.foundation.frontend.semantic.v1.Type;
import yan.foundation.frontend.semantic.v1.type.PrimitiveType;

public class YCTypes {
    public static final PrimitiveType Error = new PrimitiveType("error");
    public static final PrimitiveType Void = new PrimitiveType("void");
    public static final PrimitiveType Bool = new PrimitiveType("bool");
    public static final PrimitiveType Int = new PrimitiveType("int");
    public static final PrimitiveType Float = new PrimitiveType("float");

    public static boolean isErrorType(Type type) {
        return type == Error;
    }

    public static boolean isNumericType(Type type) {
        return type == Int || type == Float;
    }

    public static boolean isBoolType(Type type) {
        return type == Bool;
    }

    public static boolean isIntType(Type type) {
        return type == Int;
    }

    public static boolean isFloatType(Type type) {
        return type == Float;
    }

    public static boolean isVoidType(Type type) {
        return type == Void;
    }
}
