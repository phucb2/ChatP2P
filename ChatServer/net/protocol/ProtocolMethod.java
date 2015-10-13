package protocol;

import org.json.JSONArray;
import org.json.JSONObject;

//import controller.Controller;
//import model.User;

public class ProtocolMethod implements ProtocolInterface{
	public String method_name;
	public Object[] method_param;
	public int id;
	
	public ProtocolMethod(String name, Object[] param, int id){
		method_name = name;
		method_param = param ;
		this.id = id;
	}
	@Override
	public String toString() {
		return this.toJson().toString();
	}
	/**
	 * Return a json object.
	 * @return
	 */
	public JSONObject toJson(){
		JSONObject ret = new JSONObject();
		ret.put("method", this.method_name);
		JSONArray params = new JSONArray();
		for (int i = 0; i < method_param.length; i++) {
			Object object = method_param[i];
			params.put(object);
		}
		ret.put("param", params);
		ret.put("id", id);
		return ret;
	}

	@Override
	public String getType() {
		return "method";
	}
}
