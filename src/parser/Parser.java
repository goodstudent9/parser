package parser;

import inter.*;
import lexer.*;
import symbols.Array;
import symbols.Env;
import symbols.Type;

import java.io.IOException;

public class Parser {

    //词法分析器
    private Lexer lex;
    private Token look;   // 下一个token
    Env top = null;       // current or top symbol table，当前的这个block之内的环境
    int used = 0;         // storage used for declarations

    public Parser(Lexer l) throws IOException {
        lex = l;
        //读取当前的token，赋值给了look
        move();
    }

    void move() throws IOException {
        //读取下一个token
        look = lex.scan();
    }

    void error(String s) {
        //报错
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) throws IOException {
        //利用tag的值进行比较，只有满足，才会读取下一个token
        if (look.tag == t) move();
            //若类型不匹配，在报错
        else error("syntax error");
    }

    //这是子程序调用
    public void program() throws IOException {  // program -> block
        // build the syntax tree
        //调用block子程序
        Stmt s = block();
        // display the syntax tree
        // only display the stmts, without expr
        s.display();
    }

    Stmt block() throws IOException {  // block -> { decls stmts }
        //block开始的地方，匹配{
        match('{');
        Env savedEnv = top;
        //创建一个新的env
        top = new Env(top);
        //对变量声明进行分析
        decls();
        Stmt s = stmts();//对段落进行递归解析
        match('}');//匹配结束符号
        top = savedEnv;
        return s;
    }

    void decls() throws IOException {

        while (look.tag == Tag.BASIC) {   // D -> type ID ;
            Type p = type();//获取当前的变量的类型，look已经到了变量的id
            Token tok = look;//当前的值给tok
            match(Tag.ID);//匹配id这个tag
            match(';');//匹配;

            Id id = new Id((Word) tok, p, used);
            top.put(tok, id);//将当前ID放入hashtable中
            used = used + p.width;//整体的偏移量增加一个width
        }
    }

    Type type() throws IOException {//对变量类型进行匹配

        Type p = (Type) look;            // expect look.tag == Tag.BASIC
        match(Tag.BASIC);//match之后会读取下一个token
        if (look.tag != '[') return p; // T -> basic 若当前的token不是[，则不是数组
        else return dims(p);            // return array type 否则就是数组类型,返回数组的维度
    }

    Type dims(Type p) throws IOException {//对数组进行匹配
        match('[');//匹配[，读取到下一个数字
        Token tok = look;//将数字保留在tok上
        match(Tag.NUM);//验证类型是否是数字
        match(']');//验证数字过后是不是]
        if (look.tag == '[')//如果之后还是[，表明是多维数组
            p = dims(p);
        return new Array(((Num) tok).value, p);//递归的返回大小,如：[2][3]int
    }

    Stmt stmts() throws IOException {
        //如果{的接下来的符号是}，说明是空的{}，那么返回Null
        if (look.tag == '}') return Stmt.Null;
            //否则调用seq函数，可能有多个Stmt
            //todo 层级声明语句的构造
        else return new Seq(stmt(), stmts());
    }

    Stmt stmt() throws IOException {
        Expr x;//x是表达式的类型，用于条件的判断
        Stmt s, s1, s2;
        Stmt savedStmt;         // save enclosing loop for breaks

        switch (look.tag) {

            case ';'://如果读取到;表示直接结束,没有赋值语句{;}
                move();//读取下一个
                return Stmt.Null;

            case Tag.IF://如果下一个token是if     {if}
                match(Tag.IF);//匹配if
                match('(');//匹配（
                x = bool();//表达式类型是bool，创建一个bool表达式
                match(')');//匹配）
                s1 = stmt();//if之后的语句
                if (look.tag != Tag.ELSE) return new If(x, s1);//没有else语句
                match(Tag.ELSE);//对else进行匹配
                s2 = stmt();//返回else的stmt
                return new Else(x, s1, s2);//创建else的stmt

            case Tag.WHILE:
                While whilenode = new While();
                //将当前的状态保留在saveStmt中
                savedStmt = Stmt.Enclosing;
                //当前的封闭部分是whilenode
                Stmt.Enclosing = whilenode;
                //匹配while这个token
                match(Tag.WHILE);
                //x是bool表达式
                match('(');
                x = bool();//
                //匹配右括号
                match(')');
                s1 = stmt();
                //保存结果到中间结点
                whilenode.init(x, s1);
                //将之前的存储的信息恢复
                Stmt.Enclosing = savedStmt;  // reset Stmt.Enclosing
                return whilenode;

            case Tag.DO://do while语句的格式
                Do donode = new Do();//创建do节点
                //todo 看不太懂enclosing，savestmt是什么用处
                savedStmt = Stmt.Enclosing;
                Stmt.Enclosing = donode;//当前的{}是属于donode的
                match(Tag.DO);//匹配do
                s1 = stmt();//匹配do之后的{}
                //匹配while(bool)
                match(Tag.WHILE);
                match('(');
                x = bool();
                match(')');
                match(';');
                donode.init(s1, x);//初始化do节点
                Stmt.Enclosing = savedStmt;  // reset Stmt.Enclosing
                return donode;

            case Tag.BREAK://匹配break
                match(Tag.BREAK);
                match(';');
                return new Break();

            case '{'://如果直接就是{那么就是一个新的block
                return block();
            case Tag.FOR:
                match(Tag.FOR);
                For fornode = new For();
                //将当前的状态保留在saveStmt中
                savedStmt = Stmt.Enclosing;
                Stmt.Enclosing = fornode;
                match('(');
                Stmt init = null;
                Stmt change = null;
                Expr condition = null;
                if (look.tag != 59) {//第一个赋值语句是否是空语句
                    init = assign();//识别赋值语句
                }
                match(';');
                if (look.tag != 59) {
                    condition = bool();
                    if (condition.type != Type.Bool)//中间语句是否是bool表达式，不是的话要报错
                        error("Here need a boolean expression");
                }
                match(';');
                if (look.tag != 59 && look.tag != 41) {
                    change = assign();//识别赋值语句
                }
                match(')');
                var body = stmt();
                //保存结果到中间结点
                fornode.init(condition, init, change, body);
                //将之前的存储的信息恢复
                Stmt.Enclosing = savedStmt;  // reset Stmt.Enclosing
                return fornode;


            default://默认是赋值语句，前面没有修饰
                var tmp = assign();//一个赋值语句
                match(';');
                return tmp;
        }
    }

    Stmt assign() throws IOException {//a=c+6/8;
        Stmt stmt;
        Token t = look;
        match(Tag.ID);
        Id id = top.get(t);//如果id没有找到，那么就是错误的，因为需要提前声明。
        if (id == null) error(t.toString() + " undeclared");

        if (look.tag == '=') {       // S -> id = E ;
            move();
            stmt = new Set(id, bool());//这里的bool纯纯的无语，全部套在一起，要不和陌生人说话
        } else {                        // S -> L = E ;
            Access x = offset(id);
            match('=');
            stmt = new SetElem(x, bool());//这里也是
        }

        //match(';');
        return stmt;
    }

    Expr bool() throws IOException {
        Expr x = join();
        while (look.tag == Tag.OR) {//形如（A）|（B）的判定
            Token tok = look;
            move();
            x = new Or(tok, x, join());//join是处理B的部分，优先级是&&高于||，所以与运算在下面
        }
        return x;
    }

    Expr join() throws IOException {//处理的是（A）&&（B）的部分
        Expr x = equality();//先分析（A）
        while (look.tag == Tag.AND) {
            Token tok = look;
            move();
            x = new And(tok, x, equality());//分析上述的（B）
        }
        return x;
    }

    Expr equality() throws IOException {//分析处理a==b，和a！=b的部分
        Expr x = rel();//先分析a部分
        while (look.tag == Tag.EQ || look.tag == Tag.NE) {//若后续有跟着等号或者不等号
            Token tok = look;
            move();
            x = new Rel(tok, x, rel());//分析b部分
        }
        return x;
    }

    Expr rel() throws IOException {
        Expr x = expr();//一定是一个表达式，先去分析表达式a，在判断这一层后续的符号a<b,a<=b,a>b,a>=b四种情况
        switch (look.tag) {//若还有符号判断，所有的四个符号，分析b
            case '<':
            case Tag.LE:
            case Tag.GE:
            case '>':
                Token tok = look;
                move();
                return new Rel(tok, x, expr());//对b进行分析
            default:
                return x;//若都没有后续的比大小，那么直接就是x本身
        }
    }

    Expr expr() throws IOException {//处理形如a+b，a-b，乘除法优先级高于加减法，因此在后续处理
        Expr x = term();
        while (look.tag == '+' || look.tag == '-') {//有+-号
            Token tok = look;
            move();
            x = new Arith(tok, x, term());//对b的处理，返回整个表达式
        }
        return x;
    }

    Expr term() throws IOException {//形如a*b和a/b的样子
        Expr x = unary();//对一个非数组部分的处理
        while (look.tag == '*' || look.tag == '/') {//后续有乘除号
            Token tok = look;
            move();
            x = new Arith(tok, x, unary());//对b的处理，返回整个表达式
        }
        return x;
    }

    Expr unary() throws IOException {//不是数组
        if (look.tag == '-') {//第一个符号是-
            move();//读取下一个，存储在look上
            return new Unary(Word.minus, unary());//是一个负数
        } else if (look.tag == '!') {
            Token tok = look;//将！保存
            move();//读取下一个
            return new Not(tok, unary());//构造非的部分
        } else return factor();//不是-或者！，是一个具体的式子
    }

    Expr factor() throws IOException {
        Expr x = null;
        switch (look.tag) {
            case '(':
                move();
                x = bool();
                match(')');
                return x;
            case Tag.NUM://当等号后跟的是数字的时候，构造一个常数，类型是int
                x = new Constant(look, Type.Int);
                move();
                return x;
            case Tag.REAL://等号后是一个非int，返回float类型
                x = new Constant(look, Type.Float);
                move();
                return x;
            //等号后是两个bool量
            case Tag.TRUE://等号后是两个bool量
                x = Constant.True;
                move();
                return x;
            case Tag.FALSE:
                x = Constant.False;
                move();
                return x;
            default://若上述都不满足则报错
                error("syntax error");
                return x;
            case Tag.ID://若等号后是一个ID则表示可能是数组
                String s = look.toString();
                Id id = top.get(look);//查找这个id是否被声明
                if (id == null) error(look.toString() + " undeclared");//没有声明
                move();
                if (look.tag != '[') return id;//声明了
                else return offset(id);//返回数组
        }
    }

    Access offset(Id a) throws IOException {   // I -> [E] | [E] I
        Expr i;                             //todo 对数组的判断与识别
        Expr w;
        Expr t1, t2;
        Expr loc;  // inherit id

        Type type = a.type;//获取数组的元素的类型
        match('[');
        i = bool();//对[]内的部分重新调用一次所有的上述过程
        match(']');     // first index, I -> [ E ]
        type = ((Array) type).of;//等号右边的数组的类型
        w = new Constant(type.width);//w是等号右边中元素种类的width
        t1 = new Arith(new Token('*'), i, w);//t1是右边的数组的要赋值的元素的首地址
        loc = t1;//第一个[]内的首地址
        while (look.tag == '[') {      // multi-dimensional I -> [ E ] I
            match('[');//若还有更高的维度，那么继续按照上述的做法进行
            i = bool();
            match(']');
            type = ((Array) type).of;
            w = new Constant(type.width);
            t1 = new Arith(new Token('*'), i, w);//此时的t1是第二个维度的偏移量
            t2 = new Arith(new Token('+'), loc, t1);//加上上一个维度的偏移量就是当前的偏移量，不断执行
            loc = t2;//loc是一个累计变量
        }

        return new Access(a, loc, type);//a是右边数组名，loc是下标，type是右边的类型
    }
}
