package inter;

import symbols.Type;

public class Set extends Stmt {

    public Id id;
    public Expr expr;

    public Set(Id i, Expr x) {
        id = i;
        expr = x;
        if (check(id.type, expr.type) == null) error("type error");
    }

    public Type check(Type p1, Type p2) {
       //确保赋值的左右两边类型是一样的。如果不一样，那么就报错
        if (Type.numeric(p1) && Type.numeric(p2)) return p2;
        else if (p1 == Type.Bool && p2 == Type.Bool) return p2;
        else return null;
    }

    public void gen(int b, int a) {
    }

    public void display() {
        emit(" assignment ");
    }
}