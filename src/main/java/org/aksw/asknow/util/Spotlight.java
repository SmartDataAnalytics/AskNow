package org.aksw.asknow.util;

import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
//import lombok.extern.slf4j.Slf4j;
import java.util.regex.Pattern;

/** API for DBpedia Spotlight. Use {@link #getDBpLookup} in a static fashion.*/
public class Spotlight
{
	private Spotlight()	{}

	/** Relies on a http connection to the service, which may be down.
	 *  @param phrase
	 ** @return */
	public static String getDBpLookup(String phrase)
	{
		String DBpEquivalent = "";
		String argument = phrase.replaceAll(" ","%20");
		try
		{
			URL oracle = new URL("http://spotlight.sztaki.hu:2222/rest/annotate?text=" + argument);
			// URL oracle = new URL("http://spotlight.dbpedia.org/rest/annotate?text="+argument);
		//	System.out.println(oracle);
			try
			{
				URLConnection yc = oracle.openConnection();
				try (BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream())))
				{
					//log.trace(in.readLine());
					int startindex =0,index=0;
					String inputLine;
					while ((inputLine = in.readLine()) != null)
					{   
						Pattern p = Pattern.compile("href=\"([^\"]*)\"");
						Matcher m = p.matcher(inputLine);
						while (m.find()) {
							DBpEquivalent=DBpEquivalent+" <"+m.group(1)+"> ";
							

						}
					}
				}
				catch(Exception e){
					
				}
			}
			catch(Exception e){
				
			}
		}
		catch(Exception e){
			
		}
		return DBpEquivalent;	
	}
	public static String getEntity(String uri)
	{
		return uri.substring(
				uri.lastIndexOf(
						'/') + 1);
	}

	public static String getDbpEntity(String entity)
	{
		// case1 resource
		String uri;
		entity = entity.trim();
		entity = entity.replaceAll(" ",	"_");

		uri = checkRes(entity.substring(0,entity.length() - 2));
		if (!uri.contains("pageNotFound")) {
			return uri; }
		uri = checkOntology(entity);
		if (!uri.contains("pageNotFound")) { 
			return uri; }
		uri = checkRes(entity);
		if (!uri.contains("pageNotFound")) { 
			return uri; }
		uri = checkOntology(entity);
		if (!uri.contains("pageNotFound")) {
			return uri; }
		entity = entity.substring(0,1).toUpperCase()+ entity.substring(1);
		uri = checkRes(entity);
		if (!uri.contains("pageNotFound")) { 
			return uri; }
		uri = checkOntology(entity);
		if (!uri.contains("pageNotFound")) { 
			return uri; }

		return "notFound";
	}

	public static String checkRes(String entity)
	{

		try
		{
			URL uri = new URL("http://dbpedia.org/page/" + entity);
			URLConnection yc = uri.openConnection();

			try (BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream())))
			{
				String temppage = in.toString();
				if (temppage.contains(
						"No further information is available. (The requested entity is unknown)"))
					return ("pageNotFound");
				else return uri.toString();
			}

		}
		catch (MalformedURLException e)
		{
			System.out.println(
					" the URL is not in a valid form");
		}
		catch (IOException e)
		{
			// System.out.println(" the connection couldn't be established");
			return ("pageNotFound");
		}
		return null;
	}

	public static String checkOntology(String entity)
	{

		try
		{
			URL uri = new URL("http://dbpedia.org/ontology/" + entity);
			URLConnection yc = uri.openConnection();

			try (BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream())))
			{
				String temppage = in.toString();
				if (temppage.contains(
						"No further information is available. (The requested entity is unknown)"))
					return ("pageNotFound");
				else return "PageFound" + uri;
			}

		}
		catch (MalformedURLException e)
		{
			System.out.println(
					" the URL is not in a valid form");
		}
		catch (IOException e)
		{
			// System.out.println(" the connection couldn't be established");
			return ("pageNotFound");
		}
		// TODO KO@MO: why return null? check and comment
		return null;

	}

}