package inter;

import lexer.Word;
import symbols.Type;

public class Temp extends Expr {
    static int count = 0;
    //count记录的是总共有几个temp类
    //number记录的是这个是第几个temp类。
    int number = 0;

    public Temp(Type p) {
        super(Word.temp, p);
        number = ++count;
    }

    public String toString() {
        return "t" + number;
    }
}
