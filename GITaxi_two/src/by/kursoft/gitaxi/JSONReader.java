package by.kursoft.gitaxi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import by.kursoft.gitaxi.database.DB;

public class JSONReader {
	
		
	public static void saveSheldule(String array, String result, DB db) throws JSONException{
		result = result.substring(1);
		JSONObject rootJson = new JSONObject(result);
		JSONArray jsonArray = rootJson.getJSONArray(array);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject objJson = jsonArray.getJSONObject(i);
			db.addRec(objJson.getString(DB.COLUMN_DAY),
					objJson.getString(DB.COLUMN_TIME),
					objJson.getString(DB.COLUMN_OWNER),
					objJson.getInt(DB.COLUMN_DIRECTION));
		}
	}
	

	public static int getVersion(String result) {
		result = result.substring(1);
		int version;
		try {
			JSONObject rootJson = new JSONObject(result);
			version = rootJson.getInt("version");
		} catch (JSONException e) {
			e.printStackTrace();
			version = -1;
		}

		return version;

	}

}
