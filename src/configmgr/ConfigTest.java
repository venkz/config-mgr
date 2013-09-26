package configmgr;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
public class ConfigTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConfigTest tmain = new ConfigTest();
		tmain.readCmdLine();
	}
	
	public void readCmdLine()
	{
		
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
