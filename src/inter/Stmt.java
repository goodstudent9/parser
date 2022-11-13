package inter;

//声明语句
public class Stmt extends Node {

    public Stmt() {//构造函数
    }

    public static Stmt Null = new Stmt();//静态变量空声明Null

    public void gen(int b, int a) {//todo 看不懂gen
    } // called with labels begin and after

    int after = 0;                   // 保留之后的label
    public static Stmt Enclosing = Stmt.Null;  // 当声明语句结束

    public void display() {
    }
}
