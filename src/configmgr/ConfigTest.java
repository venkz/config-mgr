package configmgr;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ConfigTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * ConfigTest tmain = new ConfigTest(); tmain.readCmdLine();
		 */

		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

		ConfigXMLParser configParser = new ConfigXMLParser();

		try {
			SAXParser sp = saxParserFactory.newSAXParser();
			sp.parse("test.xml", configParser);
			System.out.println("Read complete");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void readCmdLine() {

		InputStreamReader istr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(istr);
		try {
			String cmdString = br.readLine();
			System.out.println(cmdString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
