package symbols;

import lexer.Tag;

//在给出了type的基础上对array进行定义
public class Array extends Type {
	//定义这个array的元素的类型
    public Type of;
	//定义这个array的大小size
    public int size = 1;
//初始化
    public Array(int sz, Type p) {
		//首先对超类进行初始化，p就是数组元素的类型，sz是数组元素的个数
        super("[]", Tag.INDEX, sz * p.width);
        size = sz;
        of = p;
    }

    public String toString() {
		//输出这个数组的大小和数组的类型
        return "[" + size + "]" + of.toString();
    }
}
