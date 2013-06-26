package com.example.statpump.InterfaceAugmentation;

import java.util.ArrayList;
import java.util.List;

import com.example.statpump.R;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
/**
 * Handles some user input in the interface
 * @author Jeff
 *
 */
public class ManageInput 
{
	static Context context;
	static Spinner sportSpinner;
	static Spinner sportSpecSpinner;
	static ImageView sportLogo;
	static String selectedSport;
	static String selectedSportSpec;
	
	/**
	 * Populates the sport spinner
	 * @param spinner
	 * @param cont
	 */
	public static void populateSpinner(Spinner spinner, Context cont)
	{
		sportSpinner = spinner;
		context = cont;
		List<String> sports = new ArrayList<String>();
		sports.add("Select a Sport");
		sports.add("Soccer");
		sports.add("American Football");
		sports.add("Australian Football");
		sports.add("Baseball");
		sports.add("Basketball");
		sports.add("Cricket");
		sports.add("Golf");
		sports.add("Handball");
		sports.add("Hockey");
		sports.add("Motorsports");
		sports.add("Rugby");
		sports.add("Tennis");
		sports.add("Volleyball");
		
		//Setting the adapter
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, 
				android.R.layout.simple_spinner_dropdown_item, sports);
		sportSpinner.setAdapter(spinnerArrayAdapter);
	}
	
	/**
	 * Populates the second spinner with the subset of data
	 * (specific versions of the sports)
	 */
	public static void populateSpecSpinner(String selection, Spinner spinner, Context cont, ImageView image)
	{
		sportSpecSpinner = spinner;
		spinner.setAdapter(null);
		List<String> sports = new ArrayList<String>();
		if(selection.equals("Soccer"))
		{
			image.setImageResource(R.drawable.soccer);
		}
		else if(selection.equals("American Football"))
		{
			image.setImageResource(R.drawable.football);
		}
		else if(selection.equals("Australian Football"))
		{
			image.setImageResource(R.drawable.ausfootball);
		}
		else if(selection.equals("Baseball"))
		{
			image.setImageResource(R.drawable.baseball);
		}
		else if(selection.equals("Basketball"))
		{
			image.setImageResource(R.drawable.basketball);
		}
		else if(selection.equals("Cricket"))
		{
			image.setImageResource(R.drawable.cricket);
		}
		else if(selection.equals("Golf"))
		{
			image.setImageResource(R.drawable.golf);
		}
		else if(selection.equals("Handball"))
		{
			image.setImageResource(R.drawable.handball);
		}
		else if(selection.equals("Hockey"))
		{
			image.setImageResource(R.drawable.hockey);
		}
		else if(selection.equals("Motorsports"))
		{
			image.setImageResource(R.drawable.car);
			sports.add("FIA F1 World Championship");
			sports.add("Nascar Sprint Cup Series");
			sports.add("FIA World Rally Championship");
			sports.add("Indycar");
			sports.add("Superkart");
			sports.add("Deutsche Tourenwagen Masters");
			sports.add("Dakar Series");
			sports.add("Formula Renault 3.5 Series");
			sports.add("British Formula 3");
			sports.add("Eurocup Formula Renault 2.0");
			sports.add("Formula Renault 2.0 ALPS");
			sports.add("Eurocup Megane Trophy");
			sports.add("Eurocup Clio");
			sports.add("ADAC Formel Masters");
			sports.add("FIM MotoGP World Championship");
			sports.add("Middle Easy Rally Championship");
			sports.add("Moto 2");
			sports.add("Moto 3");
			sports.add("Superbike World Championships");
			sports.add("Moto X1");
			sports.add("Moto X2");
			sports.add("AMA Pro AM Superbike");
			sports.add("Formula 3 Euro Series");
			sports.add("AMA Supercross");
			sports.add("Enduro E1");
			sports.add("Enduro E2");
			sports.add("Enduro E3");
			sports.add("FIA European Rally Championship");
			sports.add("GP2 Series");
			sports.add("GP3 Series");
			sports.add("Red Bull Rookies Cup");
			sports.add("Nascar Truck Series");
			sports.add("V8 Supercars");
			sports.add("BMW Talent Cup");
		}
		else if(selection.equals("Rugby"))
		{
			image.setImageResource(R.drawable.rugby);
		}
		else if(selection.equals("Tennis"))
		{
			image.setImageResource(R.drawable.tennis);
			sports.add("ATP World Tour (Grand Slam)");
			sports.add("ATP World Tour (Masters)");
			sports.add("ATP World Tour (ATP500)");
			sports.add("ATP World Tour (ATP250)");
			sports.add("WTA Tour (Premier)");
			sports.add("WTA Tour (International)");
			sports.add("ATP Challenge Tour");
			sports.add("Davis Cup");
			sports.add("Fed Cup");
			sports.add("Hopman Cup (Team Tournament)");
		}
		else if(selection.equals("Volleyball"))
		{
			image.setImageResource(R.drawable.volleyball);
			sports.add("Serie A1 - Argentina");
			sports.add("Liga Femenina - Argentina");
			sports.add("Asian Volleyball Championship - Asia");
			sports.add("Asian Volleyball Championship (W) - Asia");
			sports.add("Asian Cup - Asia");
			sports.add("Asian Cup (W) - Asia");
			sports.add("Arab Championship - Asia");
			sports.add("Damen Volley League - Austria");
			sports.add("Herren Volley League - Austria");
			sports.add("Ere Divisie Dames - Belgium");
			sports.add("Liga Heren - Belgium");
			sports.add("Superliga Masculina - Brazil");
			sports.add("Superliga Feminina - Brazil");
			sports.add("Superliga - Bulgaria");
			sports.add("A1 Liga - Croatia");
			sports.add("Extraliga - Czech Republic");
			sports.add("Extraliga Zeny - Czech Republic");
			sports.add("European Championship - Europe");
			sports.add("European Championship (W) - Europe");
			sports.add("CEV Champions League - Europe");
			sports.add("CEV Cup - Europe");
			sports.add("CEV Challenge Cup - Europe");
			sports.add("CEV Champions League (W) - Europe");
			sports.add("CEV Cup (W) - Europe");
			sports.add("CEV Challenge Cup (W) - Europe");
			sports.add("European Championship Qualifiers - Europe");
			sports.add("European Championship Qualifiers (W) - Europe");
			sports.add("U21 European Championship - Europe");
			sports.add("U20 European Championship (W) - Europe");
			sports.add("MEVZA League - Europe");
			sports.add("MEVZA League (W) - Europe");
			sports.add("WC Qualifying - Europe");
			sports.add("Schenker League - Europe");
			sports.add("WC Qualifying (W) - Europe");
			sports.add("Olympics Qualification - Europe");
			sports.add("Olympics Qualification (W) - Europe");
			sports.add("Miesten Liiga - Finland");
			sports.add("Naisten Liiga - Finland");
			sports.add("Lique A - France");
			sports.add("Lique B - France");
			sports.add("Nationale 1 - France");
			sports.add("Coupe de France - France");
			sports.add("Trophee des Champions - France");
			sports.add("Lique A feminin - France");
			sports.add("Division Excellence - France");
			sports.add("Nationale 1F - France");
			sports.add("Coupe de France (W) - France");
			sports.add("Bundesliga - Germany");
			sports.add("2. Bundesliga - Germany");
			sports.add("Bundesliga (W) - Germany");
			sports.add("2. Bundesliga (W) - Germany");
			sports.add("DVV Pokal - Germany");
			sports.add("DVV Pokal (W) - Germany");
			sports.add("A1 Volley Men - Greece");
			sports.add("A1 Volley Women - Greece");
			sports.add("Serie A1 Maschile - Italy");
			sports.add("Serie A2 Maschile - Italy");
			sports.add("Coppa Italia A1 - Italy");
			sports.add("Coppa Italia A2 - Italy");
			sports.add("Supercoppa - Italy");
			sports.add("Serie A1 Femminile - Italy");
			sports.add("Serie A2 Femminile - Italy");
			sports.add("Coppa Italia A1 (W) - Italy");
			sports.add("Coppa Italia A2 (W) - Italy");
			sports.add("Supercoppa (W) - Italy");
			sports.add("V-League - Korea Republic");
			sports.add("V-League (W) - Korea Republic");
			sports.add("Copa Panamericana U23 (W) - N/C America");
			sports.add("Copa Panamericana - N/C America");
			sports.add("Copa Panamericana (W) - N/C America");
			sports.add("WC Qualifying - N/C America");
			sports.add("WC Qualifying (W) - N/C America");
			sports.add("Olympics Qualification - N/C America");
			sports.add("Olympics Qualification (W) - N/C America");
			sports.add("U21 Continental Championship - N/C America");
			sports.add("U20 Continental Championship (W) - N/C America");
			sports.add("U19 Continental Championship - N/C America");
			sports.add("U18 Continental Championship (W) - N/C America");
			sports.add("Copa Panamericana U23 - N/C America");
			sports.add("Campeonato Centroamericano U23 - N/C America");
			sports.add("Campeonato Centroamericano U23 (W) - N/C America");
			sports.add("A League - Netherlands");
			sports.add("Supercup - Netherlands");
			sports.add("DELA League - Netherlands");
			sports.add("Supercup (W) - Netherlands");
			sports.add("Eliteserien menn - Norway");
			sports.add("Eliteserien kvinner - Norway");
			sports.add("Plusliga - Poland");
			sports.add("Plusliga Kobiet - Poland");
			sports.add("A1 Masculinos - Portugal");
			sports.add("A1 Femininos - Portugal");
			sports.add("Divizia A1 - Romania");
			sports.add("Super League - Russia");
			sports.add("Super League (W) - Russia");
			sports.add("Wiener Stadtische Liga - Serbia");
			sports.add("Wiener Stadtische Liga (W) - Serbia");
			sports.add("DOL moski - Slovenia");
			sports.add("DOL zenske - Slovenia");
			sports.add("Superliga Femenina - Spain");
			sports.add("Superliga Masculina - Spain");
			sports.add("Elitserien herrar - Sweden");
			sports.add("Elitserien damer - Sweden");
			sports.add("Nationalliga A - Switzerland");
			sports.add("Nationalliga A (W) - Switzerland");
			sports.add("Erkekler 1. Liqi - Turkey");
			sports.add("Bayanlar 1. Liqi - Turkey");
			sports.add("World League - World");
			sports.add("FIVB World Grand Prix - World");
			sports.add("Olympics - World");
			sports.add("Olympics (W) - World");
			sports.add("World Cup - World");
			sports.add("World Cup (W) - World");
			sports.add("World Championship - World");
			sports.add("World Championship (W) - World");
			sports.add("World Championship U21 - World");
			sports.add("World Championship U20 (W) - World");
			sports.add("World Championship U19 - World");
			sports.add("World Championship U18 (W) - World");
			sports.add("Grand Champions Cup - World");
			sports.add("Grand Champions Cup (W) - World");
			sports.add("Final Four Cup - World");
			sports.add("Olympics Qualification - World");
			sports.add("Olympics Qualification (W) - World");
			sports.add("FIVB Club World Championship - World");
			sports.add("FIVB Club World Championship (W) - World");
		}
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, 
				android.R.layout.simple_spinner_dropdown_item, sports);
		sportSpecSpinner.setAdapter(spinnerArrayAdapter);
	}
}
