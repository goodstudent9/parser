package symbols;

import inter.Id;

import java.util.Hashtable;

import lexer.Token;

public class Env {
	private Hashtable table;//当前 block内的声明的变量存在这里
	protected Env prev;//当前block的上一个block，是一种连续的链表的感觉
	
	public Env(Env n) { table = new Hashtable(); prev=n;}//初始化，第一个的prev应当是null
	
	public void put(Token w, Id i) {
		table.put(w, i);
	}//将这个id放进声明table之中。


	public Id get(Token w){//在所有的block之内的进行查找
		for(Env e=this; e!=null; e=e.prev){//一直向前寻找id
			Id found = (Id)(e.table.get(w));
			if(found!=null) return found;//找到了
		}
		return null;///最终也没找到
	}
}
