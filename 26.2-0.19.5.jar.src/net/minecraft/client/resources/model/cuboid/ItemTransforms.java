/*    */ package net.minecraft.client.resources.model.cuboid;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import java.lang.reflect.Type;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ 
/*    */ public final class ItemTransforms extends Record {
/*    */   private final ItemTransform thirdPersonLeftHand;
/*    */   
/*    */   private final ItemTransform thirdPersonRightHand;
/*    */   
/*    */   private final ItemTransform firstPersonLeftHand;
/*    */   
/*    */   private final ItemTransform firstPersonRightHand;
/*    */   
/*    */   private final ItemTransform head;
/*    */   
/*    */   private final ItemTransform gui;
/*    */   
/*    */   private final ItemTransform ground;
/*    */   
/*    */   private final ItemTransform fixed;
/*    */   
/*    */   private final ItemTransform fixedFromBottom;
/*    */   
/*    */   public ItemTransforms(ItemTransform thirdPersonLeftHand, ItemTransform thirdPersonRightHand, ItemTransform firstPersonLeftHand, ItemTransform firstPersonRightHand, ItemTransform head, ItemTransform gui, ItemTransform ground, ItemTransform fixed, ItemTransform fixedFromBottom) {
/* 12 */     this.thirdPersonLeftHand = thirdPersonLeftHand;
/* 12 */     this.thirdPersonRightHand = thirdPersonRightHand;
/* 12 */     this.firstPersonLeftHand = firstPersonLeftHand;
/* 12 */     this.firstPersonRightHand = firstPersonRightHand;
/* 12 */     this.head = head;
/* 12 */     this.gui = gui;
/* 12 */     this.ground = ground;
/* 12 */     this.fixed = fixed;
/* 12 */     this.fixedFromBottom = fixedFromBottom;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/cuboid/ItemTransforms;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/ItemTransforms;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/cuboid/ItemTransforms;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/ItemTransforms;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/cuboid/ItemTransforms;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/cuboid/ItemTransforms;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ItemTransform thirdPersonLeftHand() {
/* 12 */     return this.thirdPersonLeftHand;
/*    */   }
/*    */   
/*    */   public ItemTransform thirdPersonRightHand() {
/* 12 */     return this.thirdPersonRightHand;
/*    */   }
/*    */   
/*    */   public ItemTransform firstPersonLeftHand() {
/* 12 */     return this.firstPersonLeftHand;
/*    */   }
/*    */   
/*    */   public ItemTransform firstPersonRightHand() {
/* 12 */     return this.firstPersonRightHand;
/*    */   }
/*    */   
/*    */   public ItemTransform head() {
/* 12 */     return this.head;
/*    */   }
/*    */   
/*    */   public ItemTransform gui() {
/* 12 */     return this.gui;
/*    */   }
/*    */   
/*    */   public ItemTransform ground() {
/* 12 */     return this.ground;
/*    */   }
/*    */   
/*    */   public ItemTransform fixed() {
/* 12 */     return this.fixed;
/*    */   }
/*    */   
/*    */   public ItemTransform fixedFromBottom() {
/* 12 */     return this.fixedFromBottom;
/*    */   }
/*    */   
/* 24 */   public static final ItemTransforms NO_TRANSFORMS = new ItemTransforms(ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM);
/*    */   
/*    */   public ItemTransform getTransform(ItemDisplayContext type) {
/* 27 */     switch (type) {
/*    */       case THIRD_PERSON_LEFT_HAND:
/*    */       
/*    */       case THIRD_PERSON_RIGHT_HAND:
/*    */       
/*    */       case FIRST_PERSON_LEFT_HAND:
/*    */       
/*    */       case FIRST_PERSON_RIGHT_HAND:
/*    */       
/*    */       case HEAD:
/*    */       
/*    */       case GUI:
/*    */       
/*    */       case GROUND:
/*    */       
/*    */       case FIXED:
/*    */       
/*    */       case ON_SHELF:
/*    */       
/*    */     } 
/* 27 */     return 
/*    */       
/* 37 */       ItemTransform.NO_TRANSFORM;
/*    */   }
/*    */   
/*    */   protected static class Deserializer implements JsonDeserializer<ItemTransforms> {
/*    */     public ItemTransforms deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 44 */       JsonObject object = json.getAsJsonObject();
/* 46 */       ItemTransform thirdPersonRightHand = getTransform(context, object, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND);
/* 47 */       ItemTransform thirdPersonLeftHand = getTransform(context, object, ItemDisplayContext.THIRD_PERSON_LEFT_HAND);
/* 48 */       if (thirdPersonLeftHand == ItemTransform.NO_TRANSFORM)
/* 49 */         thirdPersonLeftHand = thirdPersonRightHand; 
/* 51 */       ItemTransform firstPersonRightHand = getTransform(context, object, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND);
/* 52 */       ItemTransform firstPersonLeftHand = getTransform(context, object, ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
/* 53 */       if (firstPersonLeftHand == ItemTransform.NO_TRANSFORM)
/* 54 */         firstPersonLeftHand = firstPersonRightHand; 
/* 56 */       ItemTransform head = getTransform(context, object, ItemDisplayContext.HEAD);
/* 57 */       ItemTransform gui = getTransform(context, object, ItemDisplayContext.GUI);
/* 58 */       ItemTransform ground = getTransform(context, object, ItemDisplayContext.GROUND);
/* 59 */       ItemTransform fixed = getTransform(context, object, ItemDisplayContext.FIXED);
/* 60 */       ItemTransform fixedFromBottom = getTransform(context, object, ItemDisplayContext.ON_SHELF);
/* 62 */       return new ItemTransforms(thirdPersonLeftHand, thirdPersonRightHand, firstPersonLeftHand, firstPersonRightHand, head, gui, ground, fixed, fixedFromBottom);
/*    */     }
/*    */     
/*    */     private ItemTransform getTransform(JsonDeserializationContext context, JsonObject object, ItemDisplayContext transform) {
/* 66 */       String name = transform.getSerializedName();
/* 67 */       if (object.has(name))
/* 68 */         return (ItemTransform)context.deserialize(object.get(name), ItemTransform.class); 
/* 70 */       return ItemTransform.NO_TRANSFORM;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\cuboid\ItemTransforms.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */