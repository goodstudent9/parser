package inter;

import symbols.Type;
import lexer.Token;

public class Unary extends Op {

   public Expr expr;

   public Unary(Token tok, Expr x) {    // 对于带-的表达式进行如下处理
      super(tok, null);  expr = x;//tok是前面的符号
      type = Type.max(Type.Int, expr.type);//对后面的表达式符号，需要和int进行比较，因为可能是float，double之类的。
      if (type == null ) error("type error");//报错
   }

   public Expr gen() { return new Unary(op, expr.reduce()); }

   public String toString() { return op.toString()+" "+expr.toString(); }
}

