package inter;

import symbols.Type;
import lexer.Token;

public class Arith extends Op {
	//运算，分为加减乘除四种运算

	   public Expr expr1, expr2;//两个表达式位于符号的两边

	   public Arith(Token tok, Expr x1, Expr x2)  {
	      super(tok, null); expr1 = x1; expr2 = x2;
	      type = Type.max(expr1.type, expr2.type);//如果两个符号不兼容，例如，一个bool，一个char，报错
	      if (type == null ) error("type error");
	   }

	   public Expr gen() {
	      return new Arith(op, expr1.reduce(), expr2.reduce());
	   }

	   public String toString() {
	      return expr1.toString()+" "+op.toString()+" "+expr2.toString();
	   }
	}
