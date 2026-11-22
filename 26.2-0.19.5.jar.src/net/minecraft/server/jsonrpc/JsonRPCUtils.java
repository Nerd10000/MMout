/*    */ package net.minecraft.server.jsonrpc;
/*    */ 
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ 
/*    */ public class JsonRPCUtils {
/*    */   public static final String JSON_RPC_VERSION = "2.0";
/*    */   
/*    */   public static final String OPEN_RPC_VERSION = "1.3.2";
/*    */   
/*    */   public static JsonObject createSuccessResult(JsonElement id, JsonElement result) {
/* 18 */     JsonObject response = new JsonObject();
/* 19 */     response.addProperty("jsonrpc", "2.0");
/* 20 */     response.add("id", id);
/* 21 */     response.add("result", result);
/* 22 */     return response;
/*    */   }
/*    */   
/*    */   public static JsonObject createRequest(Integer id, Identifier method, List<JsonElement> params) {
/* 26 */     return createRequest(id, method.toString(), params);
/*    */   }
/*    */   
/*    */   public static JsonObject createRequest(Integer id, String method, List<JsonElement> params) {
/* 30 */     JsonObject request = new JsonObject();
/* 31 */     request.addProperty("jsonrpc", "2.0");
/* 32 */     if (id != null)
/* 33 */       request.addProperty("id", id); 
/* 35 */     request.addProperty("method", method);
/* 36 */     if (!params.isEmpty()) {
/* 37 */       JsonArray jsonArray = new JsonArray(params.size());
/* 38 */       for (JsonElement param : params)
/* 39 */         jsonArray.add(param); 
/* 41 */       request.add("params", (JsonElement)jsonArray);
/*    */     } 
/* 43 */     return request;
/*    */   }
/*    */   
/*    */   public static JsonObject createError(JsonElement id, String message, int errorCode, String data) {
/* 47 */     JsonObject errorResponse = new JsonObject();
/* 48 */     errorResponse.addProperty("jsonrpc", "2.0");
/* 49 */     errorResponse.add("id", id);
/* 50 */     JsonObject error = new JsonObject();
/* 51 */     error.addProperty("code", Integer.valueOf(errorCode));
/* 52 */     error.addProperty("message", message);
/* 53 */     if (data != null && !data.isBlank())
/* 54 */       error.addProperty("data", data); 
/* 56 */     errorResponse.add("error", (JsonElement)error);
/* 57 */     return errorResponse;
/*    */   }
/*    */   
/*    */   public static JsonElement getRequestId(JsonObject jsonObject) {
/* 61 */     return jsonObject.get("id");
/*    */   }
/*    */   
/*    */   public static String getMethodName(JsonObject jsonObject) {
/* 65 */     return GsonHelper.getAsString(jsonObject, "method", null);
/*    */   }
/*    */   
/*    */   public static JsonElement getParams(JsonObject jsonObject) {
/* 69 */     return jsonObject.get("params");
/*    */   }
/*    */   
/*    */   public static JsonElement getResult(JsonObject jsonObject) {
/* 73 */     return jsonObject.get("result");
/*    */   }
/*    */   
/*    */   public static JsonObject getError(JsonObject jsonObject) {
/* 77 */     return GsonHelper.getAsJsonObject(jsonObject, "error", null);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\JsonRPCUtils.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */