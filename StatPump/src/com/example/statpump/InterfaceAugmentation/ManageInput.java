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
		sports.add("Specific Types of the Sport");
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
			sports.add("AFL - Australia");
			sports.add("SANFL - Australia");
			sports.add("Foxtel Cup - Australia");
			sports.add("DAFL - Denmark");
			sports.add("AFLG - Germany");
			sports.add("SAFF Premier League - Sweden");
			sports.add("International Cup - World");
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
			sports.add("Asia Cup - Asia");
			sports.add("ACC Trophy Elite - Asia");
			sports.add("Asian Test Championship - Asia");
			sports.add("KFC Twenty20 Big Bash - Australia");
			sports.add("Ryobi One-Day Cup - Australia");
			sports.add("Scheffield Shield - Australia");
			sports.add("NCL - Bangladesh");
			sports.add("NCL One-Day - Bangladesh");
			sports.add("BPL T20 - Bangladesh");
			sports.add("County Championship 1 - England");
			sports.add("County Championship 2 - England");
			sports.add("Yorkshire Bank 40 - England");
			sports.add("Friends Life t20 - England");
			sports.add("India Premier League - India");
			sports.add("Carribean Twenty20 - N/C America");
			sports.add("Ford Trophy - New Zealand");
			sports.add("HRV Cup - New Zealand");
			sports.add("Plunket Shield - New Zealand");
			sports.add("Sunfoil Series - South Africa");
			sports.add("Momentum 1-Day Cup - South Africa");
			sports.add("T20 Series - South Africa");
			sports.add("Premier League - Sri Lanka");
			sports.add("Champions League Twenty20 - World");
			sports.add("ICC World Cup - World");
			sports.add("ICC World Twenty20 - World");
			sports.add("ICC World Twenty20 Qualifiers - World");
			sports.add("ICC Test Championship - World");
			sports.add("ICC ODI Championship - World");
			sports.add("Twenty20 Internationals - World");
			sports.add("ICC Champions Trophy - World");
			sports.add("ICC Intercontinental Cup - World");
			sports.add("ICC WCL Championship - World");
			sports.add("ICC World Cricket League - World");
			sports.add("ICC U19 World Cup - World");
		}
		else if(selection.equals("Golf"))
		{
			image.setImageResource(R.drawable.golf);
			sports.add("PGA Tour");
			sports.add("European Tour");
			sports.add("LPGA Tour");
			sports.add("Asian Tour");
			sports.add("Japan Golf Tour");
			sports.add("Sunshine Tours");
			sports.add("Web.com Tour");
			sports.add("Champions Tour");
		}
		else if(selection.equals("Handball"))
		{
			image.setImageResource(R.drawable.handball);
		}
		else if(selection.equals("Hockey"))
		{
			image.setImageResource(R.drawable.hockey);
			sports.add("ALH - Asia");
			sports.add("AIHL - Australia");
			sports.add("EHL - Austria");
			sports.add("Nationalliga - Austria");
			sports.add("Extraliga - Belarus");
			sports.add("Elite League - Belgium");
			sports.add("Memorial Cup - Canada");
			sports.add("OHL - Canada");
			sports.add("WHL - Canada");
			sports.add("LHJMQ - Canada");
			sports.add("Extraliga - Czech Republic");
			sports.add("1. Liga - Czech Republic");
			sports.add("Superisligaen - Denmark");
			sports.add("Elite League - England");
			sports.add("Meistriliiga - Estonia");
			sports.add("Continental Cup - Europe");
			sports.add("Euro Ice Hockey Challenge - Europe");
			sports.add("European Trophy - Europe");
			sports.add("Euro Hockey Tour - Europe");
			sports.add("Inter-National-League - Europe");
			sports.add("SM-Liiga - Finland");
			sports.add("Mestis - Finland");
			sports.add("Lique Magnus - France");
			sports.add("DEL - Germany");
			sports.add("2. Bundesliga - Germany");
			sports.add("Interliga - Hungary");
			sports.add("Serie A1 - Italy");
			sports.add("LHL - Latvia");
			sports.add("Eredivisie - Netherlands");
			sports.add("Bekercompetitie - Netherlands");
			sports.add("GET-ligaen - Norway");
			sports.add("PLH - Poland");
			sports.add("Campionatul National - Romania");
			sports.add("KHL - Russia");
			sports.add("VHL - Russia");
			sports.add("MHL - Russia");
			sports.add("Extraliga - Slovakia");
			sports.add("Slohokej Liga - Slovenia");
			sports.add("Liga Nacional - Spain");
			sports.add("Elitserien - Sweden");
			sports.add("HockeyAllsvenskan - Sweden");
			sports.add("Division 1 - Sweden");
			sports.add("Nationalliga A - Switzerland");
			sports.add("Nationalliga B - Switzerland");
			sports.add("PHL - Ukraine");
			sports.add("NHL - United States");
			sports.add("AHL - United States");
			sports.add("ECHL - United States");
			sports.add("CHL - United States");
			sports.add("FHL - United States");
			sports.add("SPHL - United States");
			sports.add("Olympic Qualification - World");
			sports.add("Olympic Qualification (W) - World");
			sports.add("Olympics");
			sports.add("Olympics (W)");
			sports.add("World Championship");
			sports.add("World Championship div I - World");
			sports.add("World Championship div II - World");
			sports.add("World Championship div III - World");
			sports.add("World Championship U20 - World");
			sports.add("World Championship U18 - World");
			sports.add("World Championship (W) - World");
			sports.add("World Championship div I (W) - World");
			sports.add("World Championship div II (W) - World");
			sports.add("World Championship div III (W) - World");
			
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
			sports.add("National Rugby League - Australia");
			sports.add("Ron Massey Cup - Australia");
			sports.add("State of Origin - Australia");
			sports.add("Division 1 - Belgium");
			sports.add("Canadian Rugby Championship - Canada");
			sports.add("Extraliga - Czech Republic");
			sports.add("Aviva Premiership - England");
			sports.add("Super League - England");
			sports.add("Championship - England");
			sports.add("Championship 1 - England");
			sports.add("Northern Rail Cup - England");
			sports.add("Premiership 7s - England");
			sports.add("Aviva A League - England");
			sports.add("NCL - England");
			sports.add("RaboDirect PRO12 - Europe");
			sports.add("European Rugby Cup - Europe");
			sports.add("Six Nations - Europe");
			sports.add("European Nations Cup - Europe");
			sports.add("U20 European Championship - Europe");
			sports.add("U19 European Championship - Europe");
			sports.add("U18 European Championship - Europe");
			sports.add("U18 European Championship Div A - Europe");
			sports.add("U18 European Championship Div B - Europe");
			sports.add("U18 European Championship Div C - Europe");
			sports.add("U18 European Championship Div D - Europe");
			sports.add("Regional Rugby Championship - Europe");
			sports.add("North Sea Cup - Europe");
			sports.add("British & Irish Cup - Europe");
			sports.add("Viking Tri-Nations - Europe");
			sports.add("Anglo Welsh Cup - Europe");
			sports.add("Challenge Cup - Europe");
			sports.add("7's Grand Prix Series - Europe");
			sports.add("7's Division A - Europe");
			sports.add("Top 14 - France");
			sports.add("Pro D2 - France");
			sports.add("Bundesliga - Germany");
			sports.add("Super 12 - Italy");
			sports.add("Ereklasse - Netherlands");
			sports.add("1e Klasse - Netherlands");
			sports.add("ITM Cup - New Zealand");
			sports.add("Pacific Nations Cup - Oceania");
			sports.add("Superliga - Romania");
			sports.add("RBS Premier League - Scotland");
			sports.add("RBS National League - Scotland");
			sports.add("Currie Cup - South Africa");
			sports.add("Vodacom Cup - South Africa");
			sports.add("South American Rugby Championship - South America");
			sports.add("Division de Honor - Spain");
			sports.add("Division de Honor B - Spain");
			sports.add("Allsvenskan - Sweden");
			sports.add("Premiership - Wales");
			sports.add("World Cup - World");
			sports.add("Four Nations - World");
			sports.add("Rugby League World Cup - World");
			sports.add("Super 15 - World");
			sports.add("The Rugby Championship - World");
			sports.add("IRB Junior World Championship - World");
			sports.add("IRB Junior World Rugby Trophy - World");
			sports.add("Churchill Cup - World");
			sports.add("Test Series - World");
			sports.add("World Club Challenge - World");
			sports.add("RWC Sevens - World");
			sports.add("IRB Sevens World Series - World");
			sports.add("RLWC Qualifiers - World");
			sports.add("Americas Rugby Championship - World");
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
			sports.add("Ligue A - France");
			sports.add("Ligue B - France");
			sports.add("Nationale 1 - France");
			sports.add("Coupe de France - France");
			sports.add("Trophee des Champions - France");
			sports.add("Ligue A feminin - France");
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
			sports.add("Erkekler 1. Ligi - Turkey");
			sports.add("Bayanlar 1. Ligi - Turkey");
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
