package protocol;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ProtocolParser {
	private InputStream in;
	public ProtocolParser(InputStream inputStream) {
		in = inputStream;
	}
	public static JSONObject Parser(InputStream in){
		JSONObject ret = new JSONObject(
				new JSONTokener(in));
		return ret;
	}
	public ProtocolInterface read(){
		JSONObject object = ProtocolParser.Parser(in);
		if (object.isNull("method")){
			return getProtocolReturn(object);
		} else return getProtocolMethod(object);
	}
	public ProtocolMethod readMethod(){
		JSONObject obj = Parser(in);
		return getProtocolMethod(obj);
	}
	public ProtocolReturn readReturn(){
		return getProtocolReturn(Parser(in));
	}
	
	public static ProtocolMethod getProtocolMethod(JSONObject json){
		try {
			String method_name = json.getString("method");
			JSONArray method_param = json.getJSONArray("param");
			Object[] param = new Object[method_param.length()];
			for (int i = 0; i < param.length; i++) {
				Object object = method_param.get(i);
				param[i] = object;
			}
			int id = json.getInt("id");
			
			return new ProtocolMethod(method_name, param, id);
		} catch (JSONException e) {
			// TODO: handle exception
		}
		return null;
	}
	
	public static ProtocolReturn getProtocolReturn(JSONObject json){
		try {
			String method_return = json.getString("return");
			JSONArray result = json.getJSONArray("result");
			Object[] res = new Object[result.length()];
			for (int i = 0; i < res.length; i++) {
				Object object = res[i];
				res[i] = object;
			}
			JSONObject error = json.getJSONObject("error");
			int code = error.getInt("code");
			String string = error.getString("message");
			int id = json.getInt("id");
			ProtocolError ee = new ProtocolError(code, string);
			return new ProtocolReturn(method_return, res, id, ee);
		} catch (JSONException e) {
			// TODO: handle exception
		}
		return null;
	}
}
