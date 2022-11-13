package inter;

import symbols.Type;

public class Do extends Stmt {
//do while语句需要的是一个bool表达式和一个stmt
   Expr expr; Stmt stmt;

   public Do() { expr = null; stmt = null; }

   public void init(Stmt s, Expr x) {
      expr = x; stmt = s;//赋值
      if( expr.type != Type.Bool ) expr.error("boolean required in do");//报错
   }

   public void gen(int b, int a) {}
   
   public void display(){//do while语句的输出。
	  emit("stmt : do begin");
	  stmt.display();
	  //expr.jumping(b,0);
	  emit("stmt : do end");
   }
}