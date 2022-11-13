package inter;

import symbols.Type;

public class While extends Stmt {

    Expr expr;
    Stmt stmt;

    public While() {
        expr = null;
        stmt = null;
    }

    public void init(Expr x, Stmt s) {//while语句的构建
        expr = x;//一个条件判断，跳出循环
        stmt = s;//循环体
        if (expr.type != Type.Bool) expr.error("boolean required in while");//条件判断必须是bool
    }

    public void gen(int b, int a) {
    }

    public void display() {//while语句的输出部分
        emit("stmt : while begin");
        stmt.display();
        emit("stmt : while end");
    }
}
