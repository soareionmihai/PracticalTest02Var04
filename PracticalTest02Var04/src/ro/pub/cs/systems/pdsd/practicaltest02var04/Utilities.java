package ro.pub.cs.systems.pdsd.practicaltest02var04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Utilities {
	
	// citeste de pe un socket
	public static BufferedReader getReader(Socket socket) throws IOException {
		return new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	// scrie pe un socket
	public static PrintWriter getWriter(Socket socket) throws IOException {
		return new PrintWriter(socket.getOutputStream(), true);
	}

}