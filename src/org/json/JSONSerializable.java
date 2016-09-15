package org.json;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import usf.saav.common.SystemX;

public interface JSONSerializable {

	public JSONObject toJSON();
	
	public abstract class Default implements JSONSerializable {
		public static JSONObject parseJsonObjectFile( String filename ) throws JSONException, IOException {
			return new JSONObject(SystemX.readFileContents(filename));
		}

		public static JSONArray parseJsonArrayFile( String filename ) throws JSONException, IOException {
			return new JSONArray(SystemX.readFileContents(filename));
		}

		public static void writeJsonFile( JSONSerializable json, String filename ) throws FileNotFoundException {
			PrintWriter pw = new PrintWriter( filename );
			pw.print( json.toJSON().toString(1) );
			pw.close();
		}
	}
}
