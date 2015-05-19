package ro.pub.cs.systems.pdsd.practicaltest02var04;


import ro.pub.cs.systems.pdsd.practicaltest02var04.R;
import ro.pub.cs.systems.pdsd.practicaltest02var04.Constants;
import ro.pub.cs.systems.pdsd.practicaltest02var04.ClientThread;
import ro.pub.cs.systems.pdsd.practicaltest02var04.ServerThread;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02Var04MainActivity extends Activity {
	
	// Server widgets
	private EditText     serverPortEditText       = null;
	private Button       connectButton            = null;
	
	// Client widgets
	private EditText     clientAddressEditText    = null;
	private EditText     clientPortEditText       = null;
	private EditText     urlEditText             = null;
	private Spinner      informationTypeSpinner   = null;
	private Button       navigateToUrl 			= null;
	private TextView     pageDisplayTextView  = null;
	
	private ServerThread serverThread             = null;
	private ClientThread clientThread             = null;
	
	// facem o clasa listener care porneste serverul cu thread nou pe portul scris in serverPortEditText
	private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
	private class ConnectButtonClickListener implements Button.OnClickListener {
		
		@Override
		public void onClick(View view) {
			String serverPort = serverPortEditText.getText().toString();
			if (serverPort == null || serverPort.isEmpty()) {
				Toast.makeText(
					getApplicationContext(),
					"Server port should be filled!",
					Toast.LENGTH_SHORT
				).show();
				return;
			}
			// si acum lansam un server thread de-ala facut de noi cu parametru port luat din edittext
			serverThread = new ServerThread(Integer.parseInt(serverPort));
			if (serverThread.getServerSocket() != null) {
				serverThread.start();
			} else {
				Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not creat server thread!");
			}
			
		}
	}
	
	// clasa listener pentru client
	private GetWeatherForecastButtonClickListener getWeatherForecastButtonClickListener = new GetWeatherForecastButtonClickListener();
	private class GetWeatherForecastButtonClickListener implements Button.OnClickListener {
		
		@Override
		public void onClick(View view) {
			/* luam adresa si portul scrise de client in edittexturile aferente*/
			String clientAddress = clientAddressEditText.getText().toString();
			String clientPort    = clientPortEditText.getText().toString();
			// daca vreunul e gol, nasol.
			if (clientAddress == null || clientAddress.isEmpty() ||
				clientPort == null || clientPort.isEmpty()) {
				Toast.makeText(getApplicationContext(),"Client connection parameters should be filled!",Toast.LENGTH_SHORT).show();
				return;
			}
			
			// verificam sa fie pornit server thread-ul, ca e nevoie de el sa ne conectam
			if (serverThread == null || !serverThread.isAlive()) {
				Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
				return;
			}
			
			// ce vrei? oras si informatia ceruta. le luam de la edittext-uri. sa nu fie nule.
			String urele = urlEditText.getText().toString();
			
			if (urele == null || urele.isEmpty()) {
				Toast.makeText(
					getApplicationContext(),
					"Parameters from client (url) should be filled!",
					Toast.LENGTH_SHORT
				).show();
				return;
			}
			// aista iaste rezultatul. incepem prin a il face gol.
			pageDisplayTextView.setText(Constants.EMPTY_STRING);
			
			clientThread = new ClientThread(
					clientAddress,
					Integer.parseInt(clientPort),
					urele,
					pageDisplayTextView);
			clientThread.start();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practical_test02_var04_main);
		
		// la server: legam edittextul de ontrolul grafic
		serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
		// legam connectButton pe butonul din controlul grafic
		// si legam listenerul pe buton
		connectButton = (Button)findViewById(R.id.connect_button);
		connectButton.setOnClickListener(connectButtonClickListener);
		
		// legam si la client restul de controale
		clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
		clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
		urlEditText = (EditText)findViewById(R.id.url_edit_text);
		
		navigateToUrl = (Button)findViewById(R.id.navigate_to_URL_button);
		navigateToUrl.setOnClickListener(getWeatherForecastButtonClickListener);
		pageDisplayTextView = (TextView)findViewById(R.id.display_URL_text_view);
	}
	
	// orpim threadul serverului cand inchidem aplicatia pentru ca motive
	@Override
	protected void onDestroy() {
		if (serverThread != null) {
			serverThread.stopThread();
		}
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.practical_test02_var04_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}