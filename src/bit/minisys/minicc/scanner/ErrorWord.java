package bit.minisys.minicc.scanner;

public class ErrorWord {

	int id;// ´íÎó±àºÅ
	String info;// ´íÎóĞÅÏ¢
	int line;// ´íÎóĞĞÊı
	Token token;// ´íÎó
	
	public ErrorWord() {
		// TODO Auto-generated constructor stub
	}

	public ErrorWord(int id, String info, int line, Token word) {
		this.id = id;
		this.info = info;
		this.line = line;
		this.token = word;
	}
}
