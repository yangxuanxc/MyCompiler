package bit.minisys.minicc.scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.sun.corba.se.spi.orbutil.threadpool.Work;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class LexAnalyse1 {

	int index = 0;// 开始状态0，字符下标
	int line = 1;// 当前行数
	int len;// 文件的字符长度
	char[] str = new char[50000];// 源文件
	ArrayList<Token> TokenList = new ArrayList<Token>();// 单词表
	ArrayList<ErrorWord> ErrorWordList = new ArrayList<ErrorWord>();// 错误信息列表
	int TokenNum = 0;// 统计单词个数
	int ErrorNum = 0;// 统计错误个数
	Boolean ifError = false;
	Token token = null;
	ErrorWord errorword;
	// 文件IO
	File inFile;
	File outFile;
	Reader inFileReader;
	Writer outFileWriter;

	public LexAnalyse1(String iFile, String oFile) {

		// 将输入文件用File 类表示
		inFile = new File(iFile);
		outFile = new File(oFile);
		// 初始化字符流 类

		try {
			inFileReader = new FileReader(inFile);
			outFileWriter = new FileWriter(outFile);
			len = inFileReader.read(str);
			a0();
			// 这里要写文件
			// =========================================
			outputXMLfile(outFile);
			// ==========================================
			outFileWriter.close();
			inFileReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	//处理错误信息
	void error(String errorInfo, String tempStr) {
		ErrorNum++;
		ifError = true;
		// 遇到空白符号就回到a0,开始检测新的词
		while (true) {
			if (index >= len)
				return;
			if (str[index] == ' ' || str[index] == '\t' || str[index] == '\r') {
				break;
			}
			tempStr+=str[index];
			index++;
		}
		TokenNum++;
		token = new Token();
		token.id = TokenNum;
		token.value = tempStr;
		token.type = Token.ERROR;
		token.line = line;
		token.flag = false;
		TokenList.add(token);
		ErrorWord error = new ErrorWord();
		error.id = ErrorNum;
		error.info = errorInfo;
		error.line = line;
		error.token = token; 
		ErrorWordList.add(error);
		a0();
	}

	// 换行代码段
	boolean if_change_line() {
		if (str[index] == '\r' && str[index + 1] == '\n') {
			line++;
			index++;
			index++;
			return true;
		}
		return false;
	}

	// 起始状态
	void a0() {
		if (index >= len)
			return;
		// 遇到空白符号就回到a0
		while (str[index] == ' ' || str[index] == '\t'||str[index] == '\r') {
			//是否换行
			if(if_change_line()){
				index--;
			}
			index++;
		}
		
		// 标识符或关键字，跳到状态1
		if (('a' <= str[index] && str[index] <= 'z')
				|| ('A' <= str[index] && str[index] <= 'Z')
				|| str[index] == '_') {
			a1();
		}
		// {,},[,],(,),\,,;状态2,分隔符
		else if ('{' == str[index] || str[index] == '}' || str[index] == '['
				|| str[index] == ']' || str[index] == '(' || str[index] == ')'
				|| str[index] == ',' || str[index] == '.' || str[index] == ';') {
			a2();
		}
		// 字符常量
		else if ('\'' == str[index]) {
			a3();
		}
		// 字符串常量
		else if ('\"' == str[index]) {
			a4();
		}
		// 数字常量,8 进制 16进制 整形及浮点数，科学计数法
		else if (str[index] == '0') {
			a5();
		}
		// 数字常量,10进制 整形及浮点数，科学计数法
		else if ('1' <= str[index] && str[index] <= '9') {
			a6();
		}
		// +
		else if ('+' == str[index]) {
			a7();
		}
		// -
		else if ('-' == str[index]) {
			a8();
		}
		// *
		else if ('*' == str[index]) {
			a9();
		}
		// /
		else if ('/' == str[index]) {
			a10();
		}
		// %
		else if ('%' == str[index]) {
			a11();
		}
		// &
		else if ('&' == str[index]) {
			a12();
		}
		// =
		else if ('=' == str[index]) {
			a13();
		} else if ('<' == str[index]) {
			a14();
		} else if ('>' == str[index]) {
			a15();
		} else if ('|' == str[index]) {
			a16();
		} else if ('!' == str[index]) {
			a17();
		} else if ('^' == str[index]) {
			a18();
		} else if ('.' == str[index]) {
			a19();
		} else if ('?' == str[index]) {
			a20();
		} else {
			error("不是合法的字符","");
		}

	}

	// 标识符和关键字，状态1
	void a1() {
		if (index >= len)
			return;
		// 暂时字符串；
		String temp_str = "";
		temp_str += str[index];
		index++;
		//if_change_line();
		while (('a' <= str[index] && str[index] <= 'z')
				|| ('A' <= str[index] && str[index] <= 'Z') || str[index] == '_'
				|| str[index] == '_'||('0' <= str[index] && str[index] <= '9')) {
			temp_str += str[index];
			index++;
			//if_change_line();
		}
		// ============================================
		TokenNum++;
		// 判断是否是关键字
		if (Token.isKey(temp_str)) {
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.KEY;
			token.line = line;
			TokenList.add(token);
		}
		// 标识符
		else {
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.IDENTIFIER;
			token.line = line;
			TokenList.add(token);
		}
		// ===========================================
		a0();
	}

	// {,},[,],(,),\,,;状态2,分隔符,接收状态
	void a2() {
		if (index >= len)
			return;
		// 暂时字符串；
		String temp_str = "";
		temp_str += str[index];
		index++;
		// ============================================
		TokenNum++;
		token = new Token();
		token.id = TokenNum;
		token.value = temp_str;
		token.type = Token.SEPARATOR;
		token.line = line;
		TokenList.add(token);
		// ===========================================
		a0();
	}

	// 状态3 字符常量
	void a3() {
		if (index >= len)
			return;
		// 暂时字符串；
		String temp_str = "";
		temp_str += str[index];
		index++;
		// 单个非\和'的字符
		if (str[index] != '\\' && str[index] != '\'') {
			temp_str += str[index];
			index++;
			// 单引号，字符接收
			if (str[index] == '\'') {
				temp_str += str[index];
				// ============================================
				TokenNum++;
				token = new Token();
				token.id = TokenNum;
				token.value = temp_str;
				token.type = Token.CONST_CHAR;
				token.line = line;
				TokenList.add(token);
				// ===========================================
				index++;
			} else {
				error("这不是一个合法的字符常量",temp_str);
				
			}

		}// 转义符号开始
		else if (str[index] == '\\') {
			index++;
			// 转义 a，b，f，n,r,t,v,\,',",?,0
			if (str[index] == 'a' || str[index] == 'b' || str[index] == 'f'
					|| str[index] == 'n' || str[index] == 'r'
					|| str[index] == 't' || str[index] == 'v'
					|| str[index] == '\\' || str[index] == '\''
					|| str[index] == '\"' || str[index] == '?'
					|| str[index] == '0') {
				temp_str += str[index];
				index++;
				if (str[index] == '\'') {// 单引号结尾，字符常量接收
					temp_str += str[index];
					index++;
					// ============================================
					TokenNum++;
					token = new Token();
					token.id = TokenNum;
					token.value = temp_str;
					token.type = Token.ESCAPE_CHAR;
					token.line = line;
					TokenList.add(token);
					// ============================================
				}

			} else {
				error("这不是一个合法的转义字符常量",temp_str);
				
			}
		} else {
			error("这不是一个合法的字符常量",temp_str);
			
		}
		// ============================================

		// ===========================================
		a0();
	}

	// 状态4 字符串常量
	void a4() {

		if (index >= len)
			return;
		// 暂时字符串；
		String temp_str = "";
		temp_str += str[index];
		index++;
		while (true) {
			// 字符串中换行，错误
			if (if_change_line()) {
				error("字符串中不允许换行",temp_str);
				break;
			}
			if (str[index] != '\\' && str[index] != '\"') {
				temp_str += str[index];
				index++;
			} else if (str[index] == '\\') {
				temp_str += str[index];
				index++;
				temp_str += str[index];
				index++;

			} else if (str[index] == '\"') {
				temp_str += str[index];
				index++;
				// ====================================
				TokenNum++;
				token = new Token();
				token.id = TokenNum;
				token.value = temp_str;
				token.type = Token.CONST_STR;
				token.line = line;
				TokenList.add(token);
				// ====================================
				// 接收跳出
				break;
			}
			// 字符串没结尾
			if (index >= len) {
				error("缺少一个结尾的\"",temp_str);
				break;
			}

		}
		a0();
	}

	// 数字常量,8 进制 16进制 整形及浮点数，科学计数法
	void a5() {
		if (index >= len)
			return;
		// 暂时字符串；
		String temp_str = "";
		temp_str += str[index];
		index++;
		// 0.xxxxxx的浮点数
		if (str[index] == '.') {
			temp_str += str[index];
			index++;
			while ('1' <= str[index] && str[index] <= '9') {
				temp_str += str[index];
				index++;
			}
			// ============================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.NUM_FLOAT;
			token.line = line;
			TokenList.add(token);
			// ============================================
		}
		// 8进制
		else if ('0' <= str[index] && str[index] <= '7') {
			temp_str += str[index];
			index++;
			while ('0' <= str[index] && str[index] <= '7') {
				temp_str += str[index];
				index++;
			}
			
			// 8进制浮点数
			if (str[index] == '.' || str[index] == 'e' || str[index] == 'E') {
				temp_str += str[index];
				index++;
				if ('0' <= str[index] && str[index] <= '7') {
					temp_str += str[index];
					index++;
					while ('0' <= str[index] && str[index] <= '7') {
						temp_str += str[index];
						index++;
					}
					// 8进制浮点数接收
					// ==================================
					TokenNum++;
					token = new Token();
					token.id = TokenNum;
					token.value = temp_str;
					token.type = Token.NUM_FLOAT;
					token.line = line;
					TokenList.add(token);
					// =====================================
				} else {
					error("这不是一个合法的八进制常量",temp_str);
					
				}
			}
			else{
				// 8进制整数接收
				// ============================================
				TokenNum++;
				token = new Token();
				token.id = TokenNum;
				token.value = temp_str;
				token.type = Token.INT_8;
				token.line = line;
				TokenList.add(token);
				// ============================================
			}
		}
		// 16进制
		else if (str[index] == 'x' || str[index] == 'X') {
			index++;
			if (('0' <= str[index] && str[index] <= '9')
					|| ('a' <= str[index] && str[index] <= 'f' || ('A' <= str[index] && str[index] <= 'F'))) {
				temp_str += str[index];
				index++;
				while (('0' <= str[index] && str[index] <= '9')
						|| ('a' <= str[index] && str[index] <= 'f' || ('A' <= str[index] && str[index] <= 'F'))) {
					temp_str += str[index];
					index++;
				}
				// 16进制
				// ==================================
				TokenNum++;
				token = new Token();
				token.id = TokenNum;
				token.value = temp_str;
				token.type = Token.INT_16;
				token.line = line;
				TokenList.add(token);
				// =====================================
			} else {
				error("这不是一个合法的十六进制常量",temp_str);
				
			}
		}

		a0();
	}

	// 数字常量,10进制 整形及浮点数，科学计数法
	void a6() {
		if (index >= len)
			return;
		// 暂时字符串；
		String temp_str = "";
		temp_str += str[index];
		index++;
		while ('0' <= str[index] && str[index] <= '9') {
			temp_str += str[index];
			index++;
		}
		if (str[index] == '.' || str[index] == 'e' || str[index] == 'E') {
			temp_str += str[index];
			index++;
			if ('0' <= str[index] && str[index] <= '9') {
				temp_str += str[index];
				index++;
				while ('0' <= str[index] && str[index] <= '9') {
					temp_str += str[index];
					index++;
				}
				// 10进制浮点数接收
				// ==================================
				TokenNum++;
				token = new Token();
				token.id = TokenNum;
				token.value = temp_str;
				token.type = Token.NUM_FLOAT;
				token.line = line;
				TokenList.add(token);
				// =====================================
			} else {
				error("这不是一个合法的数字常量",temp_str);
				
			}
		}
		// 长整形接收
		else if (str[index] == 'L') {
			temp_str += str[index];
			index++;
			// =========================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.INT_10_LONG;
			token.line = line;
			TokenList.add(token);
			// =========================================
		} else {
			// 10进制整形接收
			// =============================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.INT_10;
			token.line = line;
			TokenList.add(token);
			// =============================================
		}

		a0();
	}

	// + ++ +=
	void a7() {
		if (index >= len)
			return;
		// 暂时字符串；
		String temp_str = "";
		temp_str += str[index];
		index++;
		// ++
		if (str[index] == '+') {
			temp_str += str[index];
			index++;
			// ++接收
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
		}
		// +=
		else if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// +=接收
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
		} else {
			// +接收
			// ===========================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ===========================================
		}
		a0();
	}

	// - -- -=
	void a8() {
		if (index >= len)
			return;
		// 暂时字符串；
		String temp_str = "";
		temp_str += str[index];
		index++;
		// ++
		if (str[index] == '-') {
			temp_str += str[index];
			index++;
			// ++接收
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
		}
		// +=
		else if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// +=接收
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
			
		} else {
			// -接收
			// ===========================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ===========================================
		}
		a0();
	}

	// * *=
	void a9() {
		if (index >= len)
			return;
		// 暂时字符串；
		String temp_str = "";
		temp_str += str[index];
		index++;
		// *=
		if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// +=接收
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
			
		} else {
			// *接收
			// ===========================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ===========================================
		}
		a0();
	}

	// / /=
	void a10() {
		if (index >= len)
			return;
		// 暂时字符串；
		String temp_str = "";
		temp_str += str[index];
		index++;
		// /=
		if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// +=接收
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
			return;
		} else {
			// /接收
			// ===========================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ===========================================
		}
		a0();
	}

	// % %=
	void a11() {
		if (index >= len)
			return;
		// 暂时字符串；
		String temp_str = "";
		temp_str += str[index];
		index++;
		// %=
		if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// %=接收
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
		} else {
			// %接收
			// ===========================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ===========================================
		}
		a0();
	}

	// & && &=
	void a12() {
		if (index >= len)
			return;
		// 暂时字符串；
		String temp_str = "";
		temp_str += str[index];
		index++;
		// &&
		if (str[index] == '&') {
			temp_str += str[index];
			index++;
			// &&接收
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
			return;
		}
		// &=
		else if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// &=接收
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
		} else {
			// &接收
			// ===========================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ===========================================
		}
		a0();
	}

	// = ==
	void a13() {
		if (index >= len)
			return;
		// 暂时字符串；
		String temp_str = "";
		temp_str += str[index];
		index++;
		// ==
		if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// ==接收
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
		} else {
			// =接收
			// ===========================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ===========================================
		}
		a0();

	}

	// < << <= <<=
	void a14() {
		if (index >= len)
			return;
		// 暂时字符串；
		String temp_str = "";
		temp_str += str[index];
		index++;
		// <<
		if (str[index] == '<') {
			temp_str += str[index];
			index++;
			// <<=
			if (str[index] == '=') {
				temp_str += str[index];
				index++;
				// <<=接收
				// =======================================
				TokenNum++;
				token = new Token();
				token.id = TokenNum;
				token.value = temp_str;
				token.type = Token.OPERATOR;
				token.line = line;
				TokenList.add(token);
				// ==========================================
			}
			// <<接收
			// =======================================

			// ==========================================
		
		}
		// <=
		else if (str[index] == '=') {
			temp_str += str[index];
			index++;

			// <=接收
			// =======================================

			// ==========================================
		
		} else {
			// &接收
			// ===========================================

			// ===========================================
		}
		a0();
	}

	// > >> >= >>=
	void a15() {
		if (index >= len)
			return;
		// 暂时字符串；
		String temp_str = "";
		temp_str += str[index];
		index++;
		// >>
		if (str[index] == '>') {
			temp_str += str[index];
			index++;
			// >>
			if (str[index] == '=') {
				temp_str += str[index];
				index++;
				// >>=接收
				// =======================================
				TokenNum++;
				token = new Token();
				token.id = TokenNum;
				token.value = temp_str;
				token.type = Token.OPERATOR;
				token.line = line;
				TokenList.add(token);
				// ==========================================
				
			}
			// <<接收
			// =======================================

			// ==========================================
			
		}
		// >=
		else if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// <<=

			// <=接收
			// =======================================

			// ==========================================
			
		} else {
			// &接收
			// ===========================================

			// ===========================================
		}
		a0();
	}

	// | || |=
	void a16() {
		if (index >= len)
			return;
		// 暂时字符串；
		String temp_str = "";
		temp_str += str[index];
		index++;
		// ||
		if (str[index] == '|') {
			temp_str += str[index];
			index++;
			// &&接收
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
			
		}
		// |=
		else if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// |=接收
			// =======================================

			// ==========================================
			
		} else {
			// &接收
			// ===========================================

			// ===========================================
		}
		a0();
	}

	// ! !=
	void a17() {
		if (index >= len)
			return;
		// 暂时字符串；
		String temp_str = "";
		temp_str += str[index];
		index++;
		// !=
		if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// !=接收
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
			
		} else {
			// !接收
			// ===========================================

			// ===========================================
		}
		a0();
	}

	// ^ ^=
	void a18() {
		if (index >= len)
			return;
		// 暂时字符串；
		String temp_str = "";
		temp_str += str[index];
		index++;
		// !=
		if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// ^=接收
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
			
		} else {
			// ^接收
			// ===========================================

			// ===========================================
		}
		a0();
	}

	// .
	void a19() {
		if (index >= len)
			return;
		// 暂时字符串；
		String temp_str = "";
		temp_str += str[index];
		index++;
		// ====================================
		TokenNum++;
		token = new Token();
		token.id = TokenNum;
		token.value = temp_str;
		token.type = Token.OPERATOR;
		token.line = line;
		TokenList.add(token);
		// ====================================
		a0();
	}

	// ?
	void a20() {
		if (index >= len)
			return;
		// 暂时字符串；
		String temp_str = "";
		temp_str += str[index];
		index++;
		// ====================================
		TokenNum++;
		token = new Token();
		token.id = TokenNum;
		token.value = temp_str;
		token.type = Token.OPERATOR;
		token.line = line;
		TokenList.add(token);
		// ====================================
		a0();
	}
	//输出信息
	public String outputXMLfile(File file) throws IOException {
		//File file = new File(".\\input\\");
		if (!file.exists()) {// 如果这个文件不存在就创建它
			file.mkdirs();
			file.createNewFile();
		}
		String path = file.getAbsolutePath();

		Element root = new Element("project").setAttribute("name", "test.token.xml");

		Document Doc = new Document(root);
		//初始标签
		Element tokens = new Element("tokens");
		root.addContent(tokens);
		for (int i = 0; i < this.TokenList.size(); i++) {
			Token word = (Token) this.TokenList.get(i);

			Element elements = new Element("token");
			elements.addContent(new Element("number").setText(new Integer(
					word.id).toString()));
			elements.addContent(new Element("value").setText(word.value));
			elements.addContent(new Element("type").setText(word.type));
			elements.addContent(new Element("line").setText(new Integer(
					word.line).toString()));
			elements.addContent(new Element("valid").setText(new Boolean(
					word.flag).toString()));

			tokens.addContent(elements);
		}
		if (ifError) {
			for (int i = 0; i < ErrorWordList.size(); i++) {
				ErrorWord error = (ErrorWord) this.ErrorWordList.get(i);

				Element elements = new Element("错误信息");

				elements.addContent(new Element("错误编号").setText(new Integer(
						error.id).toString()));
				elements.addContent(new Element("错误信息").setText(error.info));
				elements.addContent(new Element("错误所在行").setText(new Integer(
						error.line).toString()));
				elements.addContent(new Element("错误单词")
						.setText(error.token.value));

				root.addContent(elements);
			}
		}
		Format format = Format.getPrettyFormat();
		XMLOutputter XMLOut = new XMLOutputter(format);
		XMLOut.output(Doc, new FileOutputStream(path));

		return path;
	}
}
