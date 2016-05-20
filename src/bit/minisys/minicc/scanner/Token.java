package bit.minisys.minicc.scanner;

import java.util.ArrayList;

/*
 * 存放单词的类
 * 
 * */
public class Token {
	public final static String KEY = "关键字";
	public final static String IDENTIFIER = "标识符";
	public final static String SEPARATOR = "分隔符";
	public final static String OPERATOR = "运算符";
	//常量
	public final static String CONST_CHAR = "字符常量";
	public final static String ESCAPE_CHAR = "转义字符常量";
	public final static String CONST_STR = "字符串常量";
	public final static String INT_16 = "十六进制整数";
	public final static String INT_8 = "八进制整数";
	public final static String INT_10 = "十进制整数";
	public final static String INT_10_LONG = "十进制长整型";
	public final static String NUM_FLOAT = "浮点数";
	public final static String ERROR = "错误";
	public static ArrayList<String> key = new ArrayList<String>();// 关键字
	public static ArrayList<String> seperator = new ArrayList<String>();// 分隔符
	public static ArrayList<String> operator = new ArrayList<String>();// 运算符
	
	int id;// 单词序号
	String value;// 单词的值
	String type;// 单词类型
	int line;// 单词所在行
	boolean flag = true;//单词是否合法
	
	static {
		Token.operator.add("+");
		Token.operator.add("++");
		Token.operator.add("+=");
		Token.operator.add("-");
		Token.operator.add("--");
		Token.operator.add("-=");
		Token.operator.add("*");
		Token.operator.add("*=");
		Token.operator.add("/");
		Token.operator.add("/=");
		Token.operator.add(">");
		Token.operator.add(">=");
		Token.operator.add(">>");
		Token.operator.add(">>=");
		Token.operator.add("<");
		Token.operator.add("<=");
		Token.operator.add("<<");
		Token.operator.add("<<=");
		Token.operator.add("=");
		Token.operator.add("==");
		Token.operator.add("!");
		Token.operator.add("!=");
		Token.operator.add("&");
		Token.operator.add("&=");
		Token.operator.add("&&");
		Token.operator.add("|");
		Token.operator.add("|=");
		Token.operator.add("||");
		Token.operator.add(".");
		Token.operator.add("?");
		
		
		Token.seperator.add("(");
		Token.seperator.add(")");
		Token.seperator.add("{");
		Token.seperator.add("}");
		Token.seperator.add("[");
		Token.seperator.add("]");
		Token.seperator.add(";");
		Token.seperator.add(",");
		
		Token.key.add("auto");
		Token.key.add("break");
		Token.key.add("case");
		Token.key.add("char");
		Token.key.add("const");
		Token.key.add("continue");
		Token.key.add("default");
		Token.key.add("do");
		Token.key.add("double");
		Token.key.add("else");
		Token.key.add("enum");
		Token.key.add("extern");
		Token.key.add("float");
		Token.key.add("for");
		Token.key.add("goto");
		Token.key.add("if");
		Token.key.add("int");
		Token.key.add("long");
		Token.key.add("register");
		Token.key.add("return");
		Token.key.add("short");
		Token.key.add("signed");
		Token.key.add("sizeof");
		Token.key.add("static");
		Token.key.add("struct");
		Token.key.add("swich");
		Token.key.add("typedef");
		Token.key.add("union");
		Token.key.add("unsigned");
		Token.key.add("void");
		Token.key.add("volatile");
		Token.key.add("while");
		Token.key.add("scanf");
		Token.key.add("printf");
	}
	
	
	public Token() {

	}

	public Token(int id, String value, String type, int line) {
		this.id = id;
		this.value = value;
		this.type = type;
		this.line = line;
	}

	public static boolean isKey(String word) {
		return key.contains(word);
	}

	public static boolean isOperator(String word) {
		return operator.contains(word);
	}

	public static boolean isSeperator(String word) {
		return seperator.contains(word);
	}
}
