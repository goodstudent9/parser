package main;

import java.io.IOException;

import parser.Parser;
import lexer.Lexer;

public class Main {

	public static void main(String[] args) throws IOException {
		Lexer lex = new Lexer();//创建一个词法分析器
		Parser parser = new Parser(lex);//利用词法分析器构造语法分析器
		parser.program();//调用其中的program进行语法分析
		System.out.print("\n");
	}

}
