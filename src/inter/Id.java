package inter;

import lexer.Word;
import symbols.Type;

public class Id extends Expr {
//这个offset是记录的这变量在内存中的位置
    public int offset;
//用token名和类型进行初始化
    public Id(Word id, Type p, int b) {
        super(id, p);
        offset = b;//记录在当前env下的偏移量
    }
}
