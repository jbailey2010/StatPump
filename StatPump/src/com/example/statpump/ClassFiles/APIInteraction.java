package com.example.statpump.ClassFiles;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class APIInteraction 
{
	public static Document getXML(String url, APIObject queryObj) throws IOException
	{
		Document doc = Jsoup.connect(queryObj.urlBase + url + queryObj.urlValidate).timeout(0).get();
		return doc;
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
