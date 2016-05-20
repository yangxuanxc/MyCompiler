package bit.minisys.minicc.scanner;

import java.io.IOException;

public class Scanner  {

	public void run(String iFile, String oFile) throws IOException {
		// TODO Auto-generated method stub
		 LexAnalyse1 lex = new LexAnalyse1(iFile,oFile);
		    System.out.println("2. 词法分析完成");
	}

}
