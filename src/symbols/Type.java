package symbols;

import lexer.Tag;
import lexer.Word;
//是在word基础上的一个子类，对数字类型的类型进行了定义，其中主要多出了这个变量的大小
public class Type extends Word {
    //这个class定义了每一个变量的类型名称以及这个类型的大小
    //这种类型变量的大小
    public int width = 0;

    //初始化type，分别是类型名称、类型的Tag和类型的大小
    public Type(String s, int tag, int w) {
        super(s, tag);
        width = w;
    }
//这里是定义出来了几个常用的类型
    public static final Type
            Int = new Type("int", Tag.BASIC, 4),
            Float = new Type("float", Tag.BASIC, 8),
            Char = new Type("char", Tag.BASIC, 1),
            Bool = new Type("bool", Tag.BASIC, 1);

    //判断这个token的类型是不是数字
    public static boolean numeric(Type p) {
        //char、int、float是，否则不是
        if (p == Type.Char || p == Type.Int || p == Type.Float) return true;
        else return false;
    }

    public static Type max(Type p1, Type p2) {
        //对变量类型进行兼容，可以确保数字类型的变量都可以在一起使用，都按照最精确的那个变量类型来计算。
        if (!numeric(p1) || !numeric(p2)) return null;//只有数字才会利用这个max函数来确定大的变量的大小。
        else if (p1 == Type.Float || p2 == Type.Float) return Type.Float;
        else if (p1 == Type.Int || p2 == Type.Int) return Type.Int;
        else return Type.Char;
    }

}
