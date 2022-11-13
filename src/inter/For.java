package inter;

public class For extends Stmt {
    Expr expr;//循环条件
    Stmt init;//赋初值
    Stmt change;//循环变化量
    Stmt body;//循环体

    public For() {//初始化
        expr = null;
        init = null;
        change = null;
        body = null;
    }

    public void init(Expr x, Stmt s1, Stmt s2, Stmt s3) {//赋值
        expr = x;
        init = s1;
        change = s2;
        body = s3;
    }

    @Override
    public void gen(int b, int a) {
        super.gen(b, a);
    }

    public void display() {
        emit("stmt : for begin");
        System.out.println("    for initial:");
        if (init == null) {//判断是否是空语句
            System.out.println("    empty init");
        } else
            init.display();

        System.out.println("    for iterator change:");
        if (change == null) {//判断是否是空语句
            System.out.println("    empty iterator change");
        } else
            change.display();
        System.out.println("    for body:");
        body.display();
        emit("stmt : for end");
    }
}
