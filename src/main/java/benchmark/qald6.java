package benchmark;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

import annotation.AnnotationOrch;
import annotation.OneHopRelationQuery;
import annotation.relationAnnotationToken;
import init.initializer;
import phrase.phrase;
import phrase.phraseOrch;
import phraseMerger.phraseMergerOrch;
import queryConstructor.SparqlSelector;
import question.quesOrch;
import question.questionAnnotation;
import token.token;
import utils.parseQald;
import utils.queryExecutor;

public class qald6 {
/*
 * Class executes the QALD6 train dataset. And comapres the results with the sparql-answers provided by the qald
 * 
 * */
	public static void main(String args[]){
		//initializing the pipeline
		initializer init = new initializer();
		//parsing the qaldjson file for answers.
		ArrayList<String[]> qaldTuple = parseQald.parseQald6("src/main/resources/qald-6-train-multilingual.json");
		Integer counter = 0;
		for(String[] temp: qaldTuple){
			String question = temp[0];
			String sparql = temp[1];
			if(!sparql.equals("")){
				//TODO: replaceAll should have more factors. 
				sparql = sparql.replaceAll("rdf:type", "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>");
				ArrayList<String> qald_result = returnResultsQald(sparql);
				ArrayList<String> askNow_answer = executeQuestion.execute(question,true);
				if (askNow_answer != null){
					if(askNow_answer.containsAll(qald_result) && qald_result.containsAll(askNow_answer)){
						System.out.println("Atleast one right answer");
						counter = counter + 1;
						
					}
				}
				System.out.println("********&&&*******" + counter);
				
			}
		}
	}
	
	//TODO: Add this to utils pacakage.
	public static ArrayList<String> returnResultsQald(String sparql){
		
		/*
		 * Returns a parsed results from a given sparql query.
		 * */
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ResultSetFormatter.outputAsJSON(outputStream, queryExecutor.query(sparql));
		String json = new String(outputStream.toByteArray());
		JSONParser parser = new JSONParser();
		ArrayList<String> result_temp = new ArrayList<String>();
		try {
			JSONObject json_obj = (JSONObject) parser.parse(json);
			JSONObject json_results = (JSONObject) json_obj.get("results");
			JSONArray json_result_array =  (JSONArray) json_results.get("bindings");
			for(int i=0;i<json_result_array.size();i++){
				JSONObject binding_object = (JSONObject) json_result_array.get(i);
//				System.out.println(binding_object);
				JSONObject binding_object_uri = (JSONObject) binding_object.get("uri");
//				System.out.println(binding_object_uri);
				result_temp.add(binding_object_uri.get("value").toString());
			}
			
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return result_temp;
		}
		
		return result_temp;
		
	}
	
	
public static ArrayList<String> returnResults(String sparql){
		
		/*
		 * Returns a parsed results from a given sparql query.
		 * */
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ResultSetFormatter.outputAsJSON(outputStream, queryExecutor.query(sparql));
		String json = new String(outputStream.toByteArray());
		JSONParser parser = new JSONParser();
		ArrayList<String> result_temp = new ArrayList<String>();
		try {
			JSONObject json_obj = (JSONObject) parser.parse(json);
			JSONObject json_results = (JSONObject) json_obj.get("results");
			JSONArray json_result_array =  (JSONArray) json_results.get("bindings");
			for(int i=0;i<json_result_array.size();i++){
				JSONObject binding_object = (JSONObject) json_result_array.get(i);
//				System.out.println(binding_object);
				JSONObject binding_object_uri = (JSONObject) binding_object.get("var");
//				System.out.println(binding_object_uri);
				result_temp.add(binding_object_uri.get("value").toString());
			}
			
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return result_temp;
		}
		
		return result_temp;
		
	}
	
}
