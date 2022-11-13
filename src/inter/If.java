package inter;

import symbols.Type;

public class If extends Stmt {//if语句的识别

   Expr expr; Stmt stmt;

   public If(Expr x, Stmt s) {
      expr = x;  stmt = s;//判断x的类型，必须是bool类型。
      if( expr.type != Type.Bool ) expr.error("boolean required in if");
   }

   public void gen(int b, int a) {}
   
   public void display(){//对if语句的识别进行输出
	   emit("stmt : if begin");
	   //expr
	   stmt.display();
	   emit("stmt : if end");
   }
}
