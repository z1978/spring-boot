package com.example.samples;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JacksonSample {
	
	public void test() {
		// JSONに変換するオブジェクトを作成
		Map<String, Object> o = createObject();
		
		// オブジェクトをJSON文字列に変換
		String json = toJSON(o);
		System.out.println("---- Create JSON Result ----");
		System.out.println(json);
		
		// JSON文字列をオブジェクトに戻す
		InputStream is = new ByteArrayInputStream(json.getBytes());
		Map<String, Object> _o = null;
		try {
			_o = parseJSON(is);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 戻したオブジェクトを確認する
		System.out.println("---- Parse JSON Result ----");
		System.out.println("Title:"+_o.get("Title"));
		System.out.println("Note:"+_o.get("Note"));
		List<Person> persons = (List<Person>)_o.get("Persons");
		for(Person person : persons) {
			System.out.println("name:"+person.name);
			System.out.println("age:"+person.age);
			if(person.phone != null) {
				for(String phone : person.phone) {
					System.out.println("phone:"+phone);
				}
			}
		}
	}
	
	/**
	 * サンプルオブジェクトを生成する
	 * @return
	 */
	private static Map<String, Object> createObject() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Title", "PersonList");
		map.put("Note", "For JSON TEST");
		
		List<Person> persons = new ArrayList<Person>();
		Person person1 = new Person();
		person1.name = "Abe Taro";
		person1.age = 3;
		List<String> phone1 = new ArrayList<String>();
		phone1.add("XXX-XXXX");
		phone1.add("YYY-YYYY");
		person1.phone = phone1;
		persons.add(person1);
		
		Person person2 = new Person();
		person2.name = "Okawa Hiroyuki";
		person2.age = 41;
		List<String> phone2 = new ArrayList<String>();
		phone2.add("ZZZ-ZZZZ");
		phone2.add("AAA-AAAA");
		person2.phone = phone2;
		persons.add(person2);
		
		map.put("Persons", persons);
		
		return map;
	}

	/**
	 * オブジェクトからJSON文字列に変換する
	 * @param o
	 * @return
	 */
	private static String toJSON(Map<String, Object> o) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		
		try {
			jsonString = objectMapper.writeValueAsString(o);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonString;
	}
	
	/**
	 * JSON文字列からオブジェクトに変換する
	 * @param is
	 * @return
	 * @throws JsonParseException
	 * @throws IOException
	 */
	private static Map<String, Object> parseJSON(InputStream is) throws JsonParseException, IOException {
		Map<String,Object> o = new HashMap<String,Object>();
		List<Person> persons = null;
		JsonFactory factory = new JsonFactory();
		JsonParser jp = factory.createJsonParser(is);
		if(jp.nextToken() == JsonToken.START_OBJECT) {
			while (jp.nextToken() != JsonToken.END_OBJECT) {
				String _name = jp.getCurrentName();
				jp.nextToken();
				if(_name.equals("Title")) {
					o.put("Title", jp.getText());
				} else if(_name.equals("Note")) {
					o.put("Note", jp.getText());
				} else if(_name.equals("Persons")) {
					persons = parsePersons(jp);
					o.put("Persons", persons);
				} else {
					jp.skipChildren();
				}
			}
		} else {
			jp.skipChildren();
		}
		return o;
	}
	
	private static List<Person> parsePersons(JsonParser jp) throws JsonParseException, NumberFormatException, IOException {
		List<Person> persons = new ArrayList<Person>();
		Person person = null;
		while (jp.nextToken() != JsonToken.END_ARRAY) {
			String _name = jp.getCurrentName();
			if(_name != null) {
				jp.nextToken();
				if(_name.equals("name")) {
					person = new Person();
					persons.add(person);
					person.name = jp.getText();
				} else if(_name.equals("age")) {
					person.age = new Integer(jp.getText());
				} else if(_name.equals("phone")) {
					person.phone = parsePhones(jp);
				} else {
					jp.skipChildren();
				}
			}
		}
		return persons;
	}
	
	private static List<String> parsePhones(JsonParser jp) throws JsonParseException, IOException {
		List<String> phones = new ArrayList<String>();
		while (jp.nextToken() != JsonToken.END_ARRAY) {
			phones.add(jp.getText());
		}
		return phones;
	}
	
	private static class Person {
		public String name;
		public Integer age;
		public List<String> phone;
	}
	
	public static void main(String args[]){
		JacksonSample s = new JacksonSample();
		s.test();
	}
}


//import javax.script.ScriptEngine;
//import javax.script.ScriptEngineManager;
//import javax.script.ScriptException;
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.Map;
//    
//public class JsonUtil {
//    
//    public static void main(String[] args) throws Exception {
//        // 起動時にオプションを指定しなかった場合は、このサンプルデータを使用する。
//        String script = "{ \"key1\" : \"val1\", \"key2\" : \"val2\", \"key3\" : { \"ckey1\" : \"cval1\", \"ckey2\" : [ \"cval2-1\", \"cval2-2\" ] } }";
//        if (args.length > 0) {
//            java.io.File f = new java.io.File(args[0]);
//            if (f.exists() && f.isFile()) {
//                // 起動時の第１引数がファイルならファイルから読み込み（java 6 対応版なので、try-with-resources すら使えません。実際は、こんな書き方せずにちゃんとエラー処理してください）
//                byte[] buf = new byte[new Long(f.length()).intValue()];
//                java.io.FileInputStream fin = null; try { fin = new java.io.FileInputStream(f); fin.read(buf); } catch (Exception ex) { throw ex; } finally { if (fin != null) { fin.close(); }}
//                script = args.length > 1 ? new String(buf, args[1]) : new String(buf); // ファイルの文字コードがシステムの文字コードと異なる場合は、第２引数で指定。例えば「UTF-8」や「JISAutoDetect」など。
//            } else {
//                script = args[0]; // ファイルでなければ、第１引数の文字列をそのまま JSON として扱う
//            }
//        }
//        ScriptEngineManager manager = new ScriptEngineManager();
//        ScriptEngine engine = manager.getEngineByName("JavaScript");
//        // ScriptEngine の eval に JSON を渡す時は、括弧で囲まないと例外が発生します。eval はセキュリティ的には好ましくないので、安全であることが不明なデータを扱うことは想定していません。
//        // 外部ネットワークと連携するプログラムで使用しないでください。
//        Object obj = engine.eval(String.format("(%s)", script));
//        // Rhino は、jdk1.6,7までの JavaScript エンジン。jdk1.8は「jdk.nashorn.api.scripting.NashornScriptEngine」
//        Map<String, Object> map = jsonToMap(obj, engine.getClass().getName().equals("com.sun.script.javascript.RhinoScriptEngine"));
//        System.out.println(map.toString());
//    }
//    
//    static Map<String, Object> jsonToMap(Object obj, boolean rhino) throws Exception {
//        // Nashorn の場合は isArray で obj が配列かどうか判断できますが、特に何もしなくても配列番号をキーにして値を取得し Map に格納できるので、ここでは無視しています。
//        // Rhino だとインデックスを文字列として指定した場合に値が返ってこないようなので、仕方なく処理を切り分けました。
//        // 実際は HashMap なんか使わずに自分で定義したクラス（配列はそのオブジェクトの List プロパティ）にマップすることになると思うので、動作サンプルとしてはこんなもんでよろしいかと。
//        boolean array = rhino ? Class.forName("sun.org.mozilla.javascript.internal.NativeArray").isInstance(obj) : false;
//        Class scriptObjectClass = Class.forName(rhino ? "sun.org.mozilla.javascript.internal.Scriptable" : "jdk.nashorn.api.scripting.ScriptObjectMirror");
//        // キーセットを取得
//        Object[] keys = rhino ? (Object[])obj.getClass().getMethod("getIds").invoke(obj) : ((java.util.Set)obj.getClass().getMethod("keySet").invoke(obj)).toArray();
//        // get メソッドを取得
//        Method method_get = array ? obj.getClass().getMethod("get", int.class, scriptObjectClass) : (rhino ? obj.getClass().getMethod("get", Class.forName("java.lang.String"), scriptObjectClass) : obj.getClass().getMethod("get", Class.forName("java.lang.Object")));
//        Map<String, Object> map = new HashMap<String, Object>();
//        for (Object key : keys) {
//            Object val = array ? method_get.invoke(obj, (Integer)key, null) : (rhino ? method_get.invoke(obj, key.toString(), null) : method_get.invoke(obj, key));
//            if (scriptObjectClass.isInstance(val)) {
//                map.put(key.toString(), jsonToMap(val, rhino));
//            } else {
//                map.put(key.toString(), val.toString()); // サンプルなので、ここでは単純に toString() してますが、実際は val の型を有効に活用した方が良いでしょう。
//            }
//        }
//        return map;
//    }
//}


//public class JsonUtil {
//
//	private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);
//	public static void main(String[] args) throws JSONException {
//		
//		String jsonStr = jsonTest();
//		// TODO Auto-generated method stub
//		log.info("Start time = [" + jsonStr + "]");
//
//	}
//
//	// construct json and output it
//	public static String jsonTest() throws JSONException {
//		JSONObject json = new JSONObject();
//		JSONArray jsonMembers = new JSONArray();
//		JSONObject member1 = new JSONObject();
//		member1.put("loginname", "zhangfan");
//		member1.put("password", "userpass");
//		member1.put("email", "10371443@qq.com");
//		member1.put("sign_date", "2007-06-12");
//		jsonMembers.put(member1);
//
//		JSONObject member2 = new JSONObject();
//		member2.put("loginname", "zf");
//		member2.put("password", "userpass");
//		member2.put("email", "8223939@qq.com");
//		member2.put("sign_date", "2008-07-16");
//		jsonMembers.put(member2);
//		json.put("users", jsonMembers);
//
//		return json.toString();
//	}
//}
