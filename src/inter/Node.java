package inter;

import lexer.Lexer;

//一切节点的子节点
public class Node {
    //用于定位是代码的第几行，用于报错
    int lexline = 0;

    //初始化定位为0
    Node() {
        lexline = Lexer.line;
    }

    //报错信息的产生，后续会有类进行重写
    void error(String s) {
        throw new Error("near line " + lexline + ": " + s);
    }

    static int labels = 0;

    //只有logical这个class有用到这个函数
    public int newlabel() {
        return ++labels;
    }

    //输出一个int值并带有解释
    public void emitlabel(int i) {
        System.out.print("L" + i + ":");
    }
    //输出一个字符串
    public void emit(String s) {
        System.out.println("\t" + s);
    }
}
