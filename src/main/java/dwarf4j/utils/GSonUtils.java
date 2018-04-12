package dwarf4j.utils;

public class GSonUtils {

  private final String CONST_DATA_PATTERN = "dd/MM/yyyy HH:mm:ss";

  private static GSonUtils instance = new GSonUtils();
  public static GSonUtils getInstance() { return instance; }

  private com.google.gson.Gson gson;

  private java.lang.reflect.Type tipoMap;

  private GSonUtils() {
    this.gson = new com.google.gson.GsonBuilder()
                  .disableHtmlEscaping()
                  .setDateFormat(CONST_DATA_PATTERN)
                  .setPrettyPrinting()
                  .serializeNulls()
               .create();
    this.tipoMap = new com.google.gson.reflect.TypeToken<java.util.Map<String, Object>>(){}.getType();
  }

  public String obj2Json(Object obj) { return new String(this.gson.toJson(obj)); }
  public java.util.Map<String, Object> json2Map(String json) { return this.gson.fromJson(json, this.tipoMap); }

}
