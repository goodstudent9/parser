package inter;

public class Seq extends Stmt {

	   Stmt stmt1; Stmt stmt2;

	   public Seq(Stmt s1, Stmt s2) { stmt1 = s1; stmt2 = s2; }//构造函数，前后两个stmt

	   public void gen(int b, int a) {}
	   
	   public void display(){
		   //三种情况，进行展示，11,01,10
		  if ( stmt1 == Stmt.Null ) stmt2.display();
		     else if ( stmt2 == Stmt.Null ) stmt1.display();
		     else {
		    	stmt1.display();
		        stmt2.display();
		     }
	   }
	}

