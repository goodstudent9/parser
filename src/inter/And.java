package inter;

import lexer.Token;

public class And extends Logical{//与条件的识别与构造
	public And(Token tok, Expr x1, Expr x2) { super(tok,x1,x2);}
	
	public void jumping(int t, int f){}

}
