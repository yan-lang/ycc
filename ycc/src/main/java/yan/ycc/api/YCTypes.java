package yan.ycc.api;

import yan.foundation.frontend.semantic.v1.Type;
import yan.foundation.frontend.semantic.v1.type.PrimitiveType;

/**
 * 该类包含了YC语言基础数据类型的定义和辅助方法。
 *
 * <p>你可以通过该类的静态成员变量访问YC语言的基础数据类型的定义。</p>
 * <p>你可以通过{@code isXType}系列静态方法判断某个对象是不是某类型。调用该类方法等价于使用{@code x == YCTypes.X}</p>
 */
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
