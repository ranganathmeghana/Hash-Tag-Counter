import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HashTagCounter {

	public static void main(String[] args) throws IOException {

		String fileName = args[0];

		FibonacciHeap fibonacciHeap = new FibonacciHeap();
		String line = null;

		FileWriter fw = new FileWriter("output_file.txt", false);
		BufferedWriter bw = new BufferedWriter(fw);

		InputStream ip = new FileInputStream(fileName);

		InputStreamReader isr = new InputStreamReader(ip, Charset.forName("UTF-8"));
		BufferedReader br = new BufferedReader(isr);

		while ((line = br.readLine()) != null) {

			String str[] = line.split(" ");
			if (str.length > 1) {
				str[0] = str[0].substring(str[0].indexOf("#") + 1);
				fibonacciHeap.insertIntoHeap(str[0], Integer.parseInt(str[1]));
			} else if (str[0].equals("STOP")) {
				break;
			} else {
				int numberOfRemoveMax = Integer.parseInt(str[0]);

				List<String> wordList = fibonacciHeap.maxWords(numberOfRemoveMax);
				StringBuilder maxWordsList = new StringBuilder();
				for (int i = 0; i < wordList.size(); i++) {
					maxWordsList.append(wordList.get(i)).append(",");
				}
				maxWordsList.deleteCharAt(maxWordsList.length() - 1);
				bw.write(maxWordsList.toString());
				bw.newLine();
				System.out.println(maxWordsList.toString());
				
			}
		}
		br.close();
		bw.close();
	}
}
