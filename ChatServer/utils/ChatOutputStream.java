package utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.*;

/**
 * @author Phuc
 *
 */
public class ChatOutputStream implements Runnable {

	private OutputStream outputStream;
	private InputStream iStream;
	public ChatOutputStream(OutputStream os, InputStream in){
		this.outputStream = os;
		this.iStream = in;
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// Read from console
		Writer writer = new OutputStreamWriter(outputStream);
		String line;
		BufferedReader in  = new BufferedReader(new InputStreamReader(iStream));
		try {
			while(true){
				//line = in.readLine();
				//System.out.println("Read");
				line = "";
				while(true){
					int c = iStream.read();
					if((char)c == '\n')
						break;
					else
						line += (char)c;						
				}
				
				if(line.equals(".")) break;
				writer.write(line + "\r\n");
				writer.flush();
			}
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
