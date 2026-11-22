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
/*     */ public final class CuboidFace extends Record {
/*     */   private final Direction cullForDirection;
/*     */   
/*     */   private final int tintIndex;
/*     */   
/*     */   private final String texture;
/*     */   
/*     */   private final UVs uvs;
/*     */   
/*     */   private final Quadrant rotation;
/*     */   
/*     */   public static final int NO_TINT = -1;
/*     */   
/*     */   public CuboidFace(Direction cullForDirection, int tintIndex, String texture, UVs uvs, Quadrant rotation) {
/*  17 */     this.cullForDirection = cullForDirection;
/*  17 */     this.tintIndex = tintIndex;
/*  17 */     this.texture = texture;
/*  17 */     this.uvs = uvs;
/*  17 */     this.rotation = rotation;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/cuboid/CuboidFace;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #17	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidFace;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/cuboid/CuboidFace;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #17	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidFace;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/cuboid/CuboidFace;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #17	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidFace;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Direction cullForDirection() {
/*  17 */     return this.cullForDirection;
/*     */   }
/*     */   
/*     */   public int tintIndex() {
/*  17 */     return this.tintIndex;
/*     */   }
/*     */   
/*     */   public String texture() {
/*  17 */     return this.texture;
/*     */   }
/*     */   
/*     */   public UVs uvs() {
/*  17 */     return this.uvs;
/*     */   }
/*     */   
/*     */   public Quadrant rotation() {
/*  17 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public static float getU(UVs uvs, Quadrant rotation, int vertex) {
/*  27 */     return uvs.getVertexU(rotation.rotateVertexIndex(vertex)) / 16.0F;
/*     */   }
/*     */   
/*     */   public static float getV(UVs uvs, Quadrant rotation, int index) {
/*  31 */     return uvs.getVertexV(rotation.rotateVertexIndex(index)) / 16.0F;
/*     */   }
/*     */   
/*     */   public static final class UVs extends Record {
/*     */     private final float minU;
/*     */     
/*     */     private final float minV;
/*     */     
/*     */     private final float maxU;
/*     */     
/*     */     private final float maxV;
/*     */     
/*     */     public UVs(float minU, float minV, float maxU, float maxV) {
/*  42 */       this.minU = minU;
/*  42 */       this.minV = minV;
/*  42 */       this.maxU = maxU;
/*  42 */       this.maxV = maxV;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/cuboid/CuboidFace$UVs;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidFace$UVs;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/cuboid/CuboidFace$UVs;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidFace$UVs;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/cuboid/CuboidFace$UVs;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidFace$UVs;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public float minU() {
/*  42 */       return this.minU;
/*     */     }
/*     */     
/*     */     public float minV() {
/*  42 */       return this.minV;
/*     */     }
/*     */     
/*     */     public float maxU() {
/*  42 */       return this.maxU;
/*     */     }
/*     */     
/*     */     public float maxV() {
/*  42 */       return this.maxV;
/*     */     }
/*     */     
/*     */     public float getVertexU(int index) {
/*  50 */       return (index == 0 || index == 1) ? this.minU : this.maxU;
/*     */     }
/*     */     
/*     */     public float getVertexV(int index) {
/*  55 */       return (index == 0 || index == 3) ? this.minV : this.maxV;
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class Deserializer implements JsonDeserializer<CuboidFace> {
/*     */     private static final int DEFAULT_TINT_INDEX = -1;
/*     */     
/*     */     private static final int DEFAULT_ROTATION = 0;
/*     */     
/*     */     public CuboidFace deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/*  65 */       JsonObject object = json.getAsJsonObject();
/*  67 */       Direction cullDirection = getCullFacing(object);
/*  68 */       int tintIndex = getTintIndex(object);
/*  69 */       String texture = getTexture(object);
/*  71 */       CuboidFace.UVs uvs = getUVs(object);
/*  72 */       Quadrant rotation = getRotation(object);
/*  74 */       return new CuboidFace(cullDirection, tintIndex, texture, uvs, rotation);
/*     */     }
/*     */     
/*     */     private static int getTintIndex(JsonObject object) {
/*  78 */       return GsonHelper.getAsInt(object, "tintindex", -1);
/*     */     }
/*     */     
/*     */     private static String getTexture(JsonObject object) {
/*  82 */       return GsonHelper.getAsString(object, "texture");
/*     */     }
/*     */     
/*     */     private static Direction getCullFacing(JsonObject object) {
/*  86 */       String cullFace = GsonHelper.getAsString(object, "cullface", "");
/*  87 */       return Direction.byName(cullFace);
/*     */     }
/*     */     
/*     */     private static Quadrant getRotation(JsonObject object) {
/*  91 */       int rotation = GsonHelper.getAsInt(object, "rotation", 0);
/*  92 */       return Quadrant.parseJson(rotation);
/*     */     }
/*     */     
/*     */     private static CuboidFace.UVs getUVs(JsonObject object) {
/*  96 */       if (!object.has("uv"))
/*  97 */         return null; 
/* 100 */       JsonArray uvArray = GsonHelper.getAsJsonArray(object, "uv");
/* 101 */       if (uvArray.size() != 4)
/* 102 */         throw new JsonParseException("Expected 4 uv values, found: " + uvArray.size()); 
/* 105 */       float minU = GsonHelper.convertToFloat(uvArray.get(0), "minU");
/* 106 */       float minV = GsonHelper.convertToFloat(uvArray.get(1), "minV");
/* 107 */       float maxU = GsonHelper.convertToFloat(uvArray.get(2), "maxU");
/* 108 */       float maxV = GsonHelper.convertToFloat(uvArray.get(3), "maxV");
/* 109 */       return new CuboidFace.UVs(minU, minV, maxU, maxV);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\cuboid\CuboidFace.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */