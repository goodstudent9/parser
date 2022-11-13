package inter;

import lexer.Token;

public class Not extends Logical {//逻辑非的判定

    public Not(Token tok, Expr x2) {
        super(tok, x2, x2);
    }//构造

    public void jumping(int t, int f) {//本次实验没有用到
        expr2.jumping(f, t);
    }

    public String toString() {
        return op.toString() + " " + expr2.toString();
    }//输出关于非的识别。
}
