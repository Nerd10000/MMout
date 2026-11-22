/*     */ package net.minecraft.client.resources.model.cuboid;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.mojang.math.Quadrant;
/*     */ import java.lang.reflect.Type;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ 
/*     */ public class Deserializer implements JsonDeserializer<CuboidFace> {
/*     */   private static final int DEFAULT_TINT_INDEX = -1;
/*     */   
/*     */   private static final int DEFAULT_ROTATION = 0;
/*     */   
/*     */   public CuboidFace deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/*  65 */     JsonObject object = json.getAsJsonObject();
/*  67 */     Direction cullDirection = getCullFacing(object);
/*  68 */     int tintIndex = getTintIndex(object);
/*  69 */     String texture = getTexture(object);
/*  71 */     CuboidFace.UVs uvs = getUVs(object);
/*  72 */     Quadrant rotation = getRotation(object);
/*  74 */     return new CuboidFace(cullDirection, tintIndex, texture, uvs, rotation);
/*     */   }
/*     */   
/*     */   private static int getTintIndex(JsonObject object) {
/*  78 */     return GsonHelper.getAsInt(object, "tintindex", -1);
/*     */   }
/*     */   
/*     */   private static String getTexture(JsonObject object) {
/*  82 */     return GsonHelper.getAsString(object, "texture");
/*     */   }
/*     */   
/*     */   private static Direction getCullFacing(JsonObject object) {
/*  86 */     String cullFace = GsonHelper.getAsString(object, "cullface", "");
/*  87 */     return Direction.byName(cullFace);
/*     */   }
/*     */   
/*     */   private static Quadrant getRotation(JsonObject object) {
/*  91 */     int rotation = GsonHelper.getAsInt(object, "rotation", 0);
/*  92 */     return Quadrant.parseJson(rotation);
/*     */   }
/*     */   
/*     */   private static CuboidFace.UVs getUVs(JsonObject object) {
/*  96 */     if (!object.has("uv"))
/*  97 */       return null; 
/* 100 */     JsonArray uvArray = GsonHelper.getAsJsonArray(object, "uv");
/* 101 */     if (uvArray.size() != 4)
/* 102 */       throw new JsonParseException("Expected 4 uv values, found: " + uvArray.size()); 
/* 105 */     float minU = GsonHelper.convertToFloat(uvArray.get(0), "minU");
/* 106 */     float minV = GsonHelper.convertToFloat(uvArray.get(1), "minV");
/* 107 */     float maxU = GsonHelper.convertToFloat(uvArray.get(2), "maxU");
/* 108 */     float maxV = GsonHelper.convertToFloat(uvArray.get(3), "maxV");
/* 109 */     return new CuboidFace.UVs(minU, minV, maxU, maxV);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\cuboid\CuboidFace$Deserializer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */