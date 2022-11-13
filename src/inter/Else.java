package inter;

import symbols.Type;

public class Else extends Stmt {

   Expr expr; Stmt stmt1, stmt2;

   public Else(Expr x, Stmt s1, Stmt s2) {
       //包含else语句的if语句
       //x是条件（bool），若为真，执行s1，若为假，执行s2
      expr = x; stmt1 = s1; stmt2 = s2;
      if( expr.type != Type.Bool ) expr.error("boolean required in if");
   }
   public void gen(int b, int a) {}
   
   public void display(){
       //执行输出语句
	   emit("stmt : else begin");
	   emit("if true");
	   stmt1.display();
	   emit("else");
	   stmt2.display();
	   emit("stmt : else end");
   }
}
