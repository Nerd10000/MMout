/*    */ package net.minecraft.client.resources.model.sprite;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class Builder {
/* 76 */   private final Map<String, TextureSlots.SlotContents> textureMap = new HashMap<>();
/*    */   
/*    */   public Builder addReference(String slot, String reference) {
/* 79 */     this.textureMap.put(slot, new TextureSlots.Reference(reference));
/* 80 */     return this;
/*    */   }
/*    */   
/*    */   public Builder addTexture(String slot, Material material) {
/* 84 */     this.textureMap.put(slot, new TextureSlots.Value(material));
/* 85 */     return this;
/*    */   }
/*    */   
/*    */   public TextureSlots.Data build() {
/* 89 */     if (this.textureMap.isEmpty())
/* 90 */       return TextureSlots.Data.EMPTY; 
/* 93 */     return new TextureSlots.Data(Map.copyOf(this.textureMap));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\sprite\TextureSlots$Data$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */