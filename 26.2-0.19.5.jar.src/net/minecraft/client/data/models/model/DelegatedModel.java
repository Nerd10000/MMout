/*    */ package net.minecraft.client.data.models.model;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class DelegatedModel implements ModelInstance {
/*    */   private final Identifier parent;
/*    */   
/*    */   public DelegatedModel(Identifier parent) {
/* 11 */     this.parent = parent;
/*    */   }
/*    */   
/*    */   public JsonElement get() {
/* 16 */     JsonObject result = new JsonObject();
/* 17 */     result.addProperty("parent", this.parent.toString());
/* 18 */     return (JsonElement)result;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\model\DelegatedModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */