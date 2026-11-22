/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import net.minecraft.client.resources.model.cuboid.ItemTransforms;
/*    */ import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
/*    */ import net.minecraft.client.resources.model.sprite.TextureSlots;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public interface UnbakedModel {
/*    */   public static final String PARTICLE_TEXTURE_REFERENCE = "particle";
/*    */   
/*    */   default Boolean ambientOcclusion() {
/* 14 */     return null;
/*    */   }
/*    */   
/*    */   default GuiLight guiLight() {
/* 18 */     return null;
/*    */   }
/*    */   
/*    */   default ItemTransforms transforms() {
/* 22 */     return null;
/*    */   }
/*    */   
/*    */   default TextureSlots.Data textureSlots() {
/* 26 */     return TextureSlots.Data.EMPTY;
/*    */   }
/*    */   
/*    */   default UnbakedGeometry geometry() {
/* 30 */     return null;
/*    */   }
/*    */   
/*    */   default Identifier parent() {
/* 34 */     return null;
/*    */   }
/*    */   
/*    */   public enum GuiLight {
/* 38 */     FRONT("front"),
/* 39 */     SIDE("side");
/*    */     
/*    */     private final String name;
/*    */     
/*    */     GuiLight(String name) {
/* 44 */       this.name = name;
/*    */     }
/*    */     
/*    */     public static GuiLight getByName(String name) {
/* 48 */       for (GuiLight target : values()) {
/* 49 */         if (target.name.equals(name))
/* 50 */           return target; 
/*    */       } 
/* 53 */       throw new IllegalArgumentException("Invalid gui light: " + name);
/*    */     }
/*    */     
/*    */     public boolean lightLikeBlock() {
/* 57 */       return (this == SIDE);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\UnbakedModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */