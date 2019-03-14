package util.json;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtils {

	public static <T> T toObjectWithGson(String json, Class<T> cls) {
		Gson gson = new Gson();
		return gson.fromJson(json, cls);
	}

	public static <T> List<T> toListWithGson(String json, Type typeOfT) {
		Gson gson = new Gson();
		return gson.fromJson(json, typeOfT);
	}

	public static String toJsonWithGson(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}

	public static String toJsonWithGson(Object obj, Type type) {
		Gson gson = new Gson();
		return gson.toJson(obj, type);
	}

	@SuppressWarnings("rawtypes")
	public static String toJsonWithGson(List list) {
		Gson gson = new Gson();
		return gson.toJson(list);
	}

	@SuppressWarnings("rawtypes")
	public static String toJsonWithGson(List list, Type type) {
		Gson gson = new Gson();
		return gson.toJson(list, type);
	}

	public static String toJsonWithGsonBuilder(Object obj) {
		Gson gson = new GsonBuilder()
				.setExclusionStrategies(new JsonExclusionStrategy())
				.serializeNulls().create();
		return gson.toJson(obj);
	}

	public static String toJsonWithGsonBuilder(Object obj, Type type) {
		Gson gson = new GsonBuilder()
				.setExclusionStrategies(new JsonExclusionStrategy())
				.serializeNulls().create();
		return gson.toJson(obj, type);
	}

	@SuppressWarnings("rawtypes")
	public static String toJsonWithGsonBuilder(List list) {
		Gson gson = new GsonBuilder()
				.setExclusionStrategies(new JsonExclusionStrategy())
				.serializeNulls().create();
		return gson.toJson(list);
	}

	@SuppressWarnings("rawtypes")
	public static String toJsonWithGsonBuilder(List list, Type type) {
		Gson gson = new GsonBuilder()
				.setExclusionStrategies(new JsonExclusionStrategy())
				.serializeNulls().create();
		return gson.toJson(list, type);
	}
}
