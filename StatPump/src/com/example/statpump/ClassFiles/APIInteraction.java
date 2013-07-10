package com.example.statpump.ClassFiles;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class APIInteraction 
{
	static //FIX THIS LATER
	String baseURL = "http://api.globalsportsmedia.com/";
	static String auth = "&authkey=865c0c0b4ab0e063e5caa3387c1a8741&username=statp";
	public static void getXML(String url) throws IOException
	{
		Document doc = Jsoup.connect(baseURL + url + auth).timeout(0).get();
		System.out.println(doc.toString());
	}
	
	/**
	 * This is the query function for a list
	 * @param url the url to be parsed
	 * @return the text that was parsed
	 * @throws IOException
	 */
	public static String parseById(Document doc, String params) throws IOException
	{
		StringBuilder result = new StringBuilder(5000);
        Elements links = doc.select(params);
        for (Element element : links) 
        {
        	result.append(element.text() + "\n");
        }
        return result.toString();
	}
}
