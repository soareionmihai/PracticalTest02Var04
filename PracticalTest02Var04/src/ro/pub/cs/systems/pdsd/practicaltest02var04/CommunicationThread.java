package ro.pub.cs.systems.pdsd.practicaltest02var04;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import android.util.Log;

public class CommunicationThread extends Thread {

	private ServerThread serverThread;
	private Socket       socket;

	// constructor. primeste un ServerThread si un socket.
	public CommunicationThread(ServerThread serverThread, Socket socket) {
		this.serverThread = serverThread;
		this.socket       = socket;
	}

	@Override
	public void run() {
		if (socket != null) {
			try {
				// citim si scriem de si la client
				BufferedReader bufferedReader = Utilities.getReader(socket);
				PrintWriter    printWriter    = Utilities.getWriter(socket);
				if (bufferedReader != null && printWriter != null) {
					Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type)!");
					String urele            = bufferedReader.readLine();

					// luam data existenta deja, adica HashMap-ul
					HashMap<String, String> data = serverThread.getData();
					String pagina = null;
					if (urele != null && !urele.isEmpty()) {
						// cautam in cache
						if (data.containsKey(urele)) {
							Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
							pagina = data.get(urele);
						} else {
							// interogam serverul http
							Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
							HttpClient httpClient = new DefaultHttpClient();

							HttpGet httpGet=new HttpGet(urele);
							ResponseHandler<String> responseHandler = new BasicResponseHandler();

							String response = httpClient.execute(httpGet,responseHandler);

							Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
							if (response != null) {
								Log.i(Constants.TAG, "Response not null");


								serverThread.setData(urele, response);


							}


						}
					}
				}

				socket.close();
			} catch (IOException ioException) {
				Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
				if (Constants.DEBUG) {
					ioException.printStackTrace();
				}
			} 
		} else {
			Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
		}
	}
}
