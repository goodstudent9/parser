package inter;

import lexer.Tag;
import lexer.Word;
import symbols.Type;

public class Access extends Op {

    public Id array;//数组的id
    public Expr index;//要访问的数组的index

    public Access(Id a, Expr i, Type p) {
        super(new Word("[]", Tag.INDEX), p);//符号是[],类型是type p
        array = a;//a是id
        index = i;//i是索引
    }

    public Expr gen() {
        return new Access(array, index.reduce(), type);
    }

    public void jumping(int t, int f) {
    }

    public String toString() {
        return array.toString() + " [ " + index.toString() + " ]";
    }
}
