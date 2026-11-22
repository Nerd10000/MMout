/*    */ package net.minecraft.client.resources.model.cuboid;
/*    */ 
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.lang.reflect.Type;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.util.Mth;
/*    */ import org.joml.Quaternionf;
/*    */ import org.joml.Quaternionfc;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public final class ItemTransform extends Record {
/*    */   private final Vector3fc rotation;
/*    */   
/*    */   private final Vector3fc translation;
/*    */   
/*    */   private final Vector3fc scale;
/*    */   
/*    */   public ItemTransform(Vector3fc rotation, Vector3fc translation, Vector3fc scale) {
/* 19 */     this.rotation = rotation;
/* 19 */     this.translation = translation;
/* 19 */     this.scale = scale;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/cuboid/ItemTransform;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/ItemTransform;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/cuboid/ItemTransform;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/ItemTransform;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/cuboid/ItemTransform;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/cuboid/ItemTransform;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vector3fc rotation() {
/* 19 */     return this.rotation;
/*    */   }
/*    */   
/*    */   public Vector3fc translation() {
/* 19 */     return this.translation;
/*    */   }
/*    */   
/*    */   public Vector3fc scale() {
/* 19 */     return this.scale;
/*    */   }
/*    */   
/* 20 */   public static final ItemTransform NO_TRANSFORM = new ItemTransform((Vector3fc)new Vector3f(), (Vector3fc)new Vector3f(), (Vector3fc)new Vector3f(1.0F, 1.0F, 1.0F));
/*    */   
/*    */   public void apply(boolean applyLeftHandFix, PoseStack.Pose pose) {
/*    */     float translationX, rotY, rotZ;
/* 23 */     if (this == NO_TRANSFORM) {
/* 24 */       pose.translate(-0.5F, -0.5F, -0.5F);
/*    */       return;
/*    */     } 
/* 32 */     if (applyLeftHandFix) {
/* 36 */       translationX = -this.translation.x();
/* 37 */       rotY = -this.rotation.y();
/* 38 */       rotZ = -this.rotation.z();
/*    */     } else {
/* 40 */       translationX = this.translation.x();
/* 41 */       rotY = this.rotation.y();
/* 42 */       rotZ = this.rotation.z();
/*    */     } 
/* 45 */     pose.translate(translationX, this.translation.y(), this.translation.z());
/* 46 */     pose.rotate((Quaternionfc)(new Quaternionf()).rotationXYZ(this.rotation.x() * 0.017453292F, rotY * 0.017453292F, rotZ * 0.017453292F));
/* 47 */     pose.scale(this.scale.x(), this.scale.y(), this.scale.z());
/* 49 */     pose.translate(-0.5F, -0.5F, -0.5F);
/*    */   }
/*    */   
/*    */   protected static class Deserializer implements JsonDeserializer<ItemTransform> {
/* 53 */     private static final Vector3fc DEFAULT_ROTATION = (Vector3fc)new Vector3f();
/*    */     
/* 54 */     private static final Vector3fc DEFAULT_TRANSLATION = (Vector3fc)new Vector3f();
/*    */     
/* 55 */     private static final Vector3fc DEFAULT_SCALE = (Vector3fc)new Vector3f(1.0F, 1.0F, 1.0F);
/*    */     
/*    */     public static final float MAX_TRANSLATION = 5.0F;
/*    */     
/*    */     public static final float MAX_SCALE = 4.0F;
/*    */     
/*    */     public ItemTransform deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 61 */       JsonObject object = json.getAsJsonObject();
/* 63 */       Vector3f rotation = getVector3f(object, "rotation", DEFAULT_ROTATION);
/* 65 */       Vector3f translation = getVector3f(object, "translation", DEFAULT_TRANSLATION);
/* 66 */       translation.mul(0.0625F);
/* 67 */       translation.set(
/* 68 */           Mth.clamp(translation.x, -5.0F, 5.0F), 
/* 69 */           Mth.clamp(translation.y, -5.0F, 5.0F), 
/* 70 */           Mth.clamp(translation.z, -5.0F, 5.0F));
/* 73 */       Vector3f scale = getVector3f(object, "scale", DEFAULT_SCALE);
/* 74 */       scale.set(
/* 75 */           Mth.clamp(scale.x, -4.0F, 4.0F), 
/* 76 */           Mth.clamp(scale.y, -4.0F, 4.0F), 
/* 77 */           Mth.clamp(scale.z, -4.0F, 4.0F));
/* 80 */       return new ItemTransform((Vector3fc)rotation, (Vector3fc)translation, (Vector3fc)scale);
/*    */     }
/*    */     
/*    */     private static Vector3f getVector3f(JsonObject object, String key, Vector3fc def) {
/* 84 */       if (!object.has(key))
/* 85 */         return new Vector3f(def); 
/* 87 */       JsonArray vecArray = GsonHelper.getAsJsonArray(object, key);
/* 88 */       if (vecArray.size() != 3)
/* 89 */         throw new JsonParseException("Expected 3 " + key + " values, found: " + vecArray.size()); 
/* 92 */       float[] elements = new float[3];
/* 93 */       for (int i = 0; i < elements.length; i++)
/* 94 */         elements[i] = GsonHelper.convertToFloat(vecArray.get(i), key + "[" + key + "]"); 
/* 96 */       return new Vector3f(elements[0], elements[1], elements[2]);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\cuboid\ItemTransform.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */