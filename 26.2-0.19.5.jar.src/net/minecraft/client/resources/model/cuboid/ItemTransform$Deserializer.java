/*    */ package net.minecraft.client.resources.model.cuboid;
/*    */ 
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import java.lang.reflect.Type;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.util.Mth;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class Deserializer implements JsonDeserializer<ItemTransform> {
/* 53 */   private static final Vector3fc DEFAULT_ROTATION = (Vector3fc)new Vector3f();
/*    */   
/* 54 */   private static final Vector3fc DEFAULT_TRANSLATION = (Vector3fc)new Vector3f();
/*    */   
/* 55 */   private static final Vector3fc DEFAULT_SCALE = (Vector3fc)new Vector3f(1.0F, 1.0F, 1.0F);
/*    */   
/*    */   public static final float MAX_TRANSLATION = 5.0F;
/*    */   
/*    */   public static final float MAX_SCALE = 4.0F;
/*    */   
/*    */   public ItemTransform deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 61 */     JsonObject object = json.getAsJsonObject();
/* 63 */     Vector3f rotation = getVector3f(object, "rotation", DEFAULT_ROTATION);
/* 65 */     Vector3f translation = getVector3f(object, "translation", DEFAULT_TRANSLATION);
/* 66 */     translation.mul(0.0625F);
/* 67 */     translation.set(
/* 68 */         Mth.clamp(translation.x, -5.0F, 5.0F), 
/* 69 */         Mth.clamp(translation.y, -5.0F, 5.0F), 
/* 70 */         Mth.clamp(translation.z, -5.0F, 5.0F));
/* 73 */     Vector3f scale = getVector3f(object, "scale", DEFAULT_SCALE);
/* 74 */     scale.set(
/* 75 */         Mth.clamp(scale.x, -4.0F, 4.0F), 
/* 76 */         Mth.clamp(scale.y, -4.0F, 4.0F), 
/* 77 */         Mth.clamp(scale.z, -4.0F, 4.0F));
/* 80 */     return new ItemTransform((Vector3fc)rotation, (Vector3fc)translation, (Vector3fc)scale);
/*    */   }
/*    */   
/*    */   private static Vector3f getVector3f(JsonObject object, String key, Vector3fc def) {
/* 84 */     if (!object.has(key))
/* 85 */       return new Vector3f(def); 
/* 87 */     JsonArray vecArray = GsonHelper.getAsJsonArray(object, key);
/* 88 */     if (vecArray.size() != 3)
/* 89 */       throw new JsonParseException("Expected 3 " + key + " values, found: " + vecArray.size()); 
/* 92 */     float[] elements = new float[3];
/* 93 */     for (int i = 0; i < elements.length; i++)
/* 94 */       elements[i] = GsonHelper.convertToFloat(vecArray.get(i), key + "[" + key + "]"); 
/* 96 */     return new Vector3f(elements[0], elements[1], elements[2]);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\cuboid\ItemTransform$Deserializer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */