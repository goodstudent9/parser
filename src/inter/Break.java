package inter;

public class Break extends Stmt {
   Stmt stmt;

   public Break() {
      //必须在一个完整的closed的{}之中才可以break,否则没有意义。
      if( Stmt.Enclosing == Stmt.Null ) error("unenclosed break");
      stmt = Stmt.Enclosing;
   }

   public void gen(int b, int a) {}
   
   public void display(){
	   emit(" break ");
   }
}
