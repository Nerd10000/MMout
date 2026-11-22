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
/*    */ public class Deserializer implements JsonDeserializer<ItemTransforms> {
/*    */   public ItemTransforms deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 44 */     JsonObject object = json.getAsJsonObject();
/* 46 */     ItemTransform thirdPersonRightHand = getTransform(context, object, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND);
/* 47 */     ItemTransform thirdPersonLeftHand = getTransform(context, object, ItemDisplayContext.THIRD_PERSON_LEFT_HAND);
/* 48 */     if (thirdPersonLeftHand == ItemTransform.NO_TRANSFORM)
/* 49 */       thirdPersonLeftHand = thirdPersonRightHand; 
/* 51 */     ItemTransform firstPersonRightHand = getTransform(context, object, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND);
/* 52 */     ItemTransform firstPersonLeftHand = getTransform(context, object, ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
/* 53 */     if (firstPersonLeftHand == ItemTransform.NO_TRANSFORM)
/* 54 */       firstPersonLeftHand = firstPersonRightHand; 
/* 56 */     ItemTransform head = getTransform(context, object, ItemDisplayContext.HEAD);
/* 57 */     ItemTransform gui = getTransform(context, object, ItemDisplayContext.GUI);
/* 58 */     ItemTransform ground = getTransform(context, object, ItemDisplayContext.GROUND);
/* 59 */     ItemTransform fixed = getTransform(context, object, ItemDisplayContext.FIXED);
/* 60 */     ItemTransform fixedFromBottom = getTransform(context, object, ItemDisplayContext.ON_SHELF);
/* 62 */     return new ItemTransforms(thirdPersonLeftHand, thirdPersonRightHand, firstPersonLeftHand, firstPersonRightHand, head, gui, ground, fixed, fixedFromBottom);
/*    */   }
/*    */   
/*    */   private ItemTransform getTransform(JsonDeserializationContext context, JsonObject object, ItemDisplayContext transform) {
/* 66 */     String name = transform.getSerializedName();
/* 67 */     if (object.has(name))
/* 68 */       return (ItemTransform)context.deserialize(object.get(name), ItemTransform.class); 
/* 70 */     return ItemTransform.NO_TRANSFORM;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\cuboid\ItemTransforms$Deserializer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */