package ro.pub.cs.systems.pdsd.practicaltest02var04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.pdsd.practicaltest02var04.Constants;
import ro.pub.cs.systems.pdsd.practicaltest02var04.Utilities;
import android.util.Log;
import android.widget.TextView;

public class ClientThread extends Thread {
	
	private String   address;
	private int      port;
	private String   urele;
	
	private TextView pageDisplayTextView;
	
	private Socket   socket;
	
	// constructor
	public ClientThread(
			String address,
			int port,
			String urele,
			TextView pageDisplayTextView) {
		this.address                 = address;
		this.port                    = port;
		this.urele                   = urele;
		
		this.pageDisplayTextView = pageDisplayTextView;
	}
	
	@Override
	public void run() {
		try {
			// facem socket nou
			socket = new Socket(address, port);
			if (socket == null) {
				Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
			}
			// citim si scriem de si pe el cu toolurile din utilities
			BufferedReader bufferedReader = Utilities.getReader(socket);
			PrintWriter    printWriter    = Utilities.getWriter(socket);
			if (bufferedReader != null && printWriter != null) {
				printWriter.println(urele);
				printWriter.flush();
				
				String weatherInformation;
				// luam ce vine de la server
				while ((weatherInformation = bufferedReader.readLine()) != null) {
					// facem string final si scrie in el
					final String finalizedWeatherInformation = weatherInformation;
					// facem thread nou runnable pe care scriem in controlul grafic
					pageDisplayTextView.post(new Runnable() {
						@Override
						public void run() {
							pageDisplayTextView.append(finalizedWeatherInformation + "\n");
						}
					});
				}
			} else {
				Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
			}
			socket.close();
		} catch (IOException ioException) {
			Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
			if (Constants.DEBUG) {
				ioException.printStackTrace();
			}
		}
	}

}