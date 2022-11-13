package inter;

import lexer.Token;
import symbols.Array;
import symbols.Type;

public class Rel extends Logical {

    public Rel(Token tok, Expr x1, Expr x2) {
        super(tok, x1, x2);
    }//rel是一个构建表达式的函数

    public Type check(Type p1, Type p2) {
        if (p1 instanceof Array || p2 instanceof Array) return null;//只要是有数组
        else if (p1 == p2) return Type.Bool;//如果前后的类型一致，那么返回bool
        else return null;
    }

    public void jumping(int t, int f) {
    }
}
