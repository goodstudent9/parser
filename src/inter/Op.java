package inter;

import lexer.Token;
import symbols.Type;

public class Op extends Expr {//操作，主要是为了后续的三个类而创建的基类
    public Op(Token tok, Type p) {
        super(tok, p);
    }

    public Expr reduce() {
        Expr x = gen();
        Temp t = new Temp(type);//输出部分。
        emit(t.toString() + " = " + x.toString());
        return t;
    }
}
