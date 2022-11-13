package inter;

import lexer.Token;
import symbols.Type;

public class Expr extends Node {

    //一个是token op还有一个是类型type
    public Token op;
    public Type type;

    //初始化expr
    Expr(Token tok, Type p) {
        op = tok;
        type = p;
    }

    public Expr gen() {
        return this;
    }

    public Expr reduce() {
        return this;
    }

    public void jumping(int t, int f) {
    }

    public void emitjumps(String test, int t, int f) {
    }

    public String toString() {
        return op.toString();
    }

}
