/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.texture.SpriteLoader;
/*     */ import net.minecraft.client.resources.model.sprite.Material;
/*     */ import net.minecraft.client.resources.model.sprite.MaterialBaker;
/*     */ 
/*     */ class CombinedBlockItemMaterialBaker extends MaterialBaker {
/*     */   private final SpriteLoader.Preparations blockAtlas;
/*     */   
/*     */   private final SpriteLoader.Preparations itemAtlas;
/*     */   
/*     */   public CombinedBlockItemMaterialBaker(SpriteLoader.Preparations blockAtlas, SpriteLoader.Preparations itemAtlas) {
/* 287 */     super(blockAtlas.missing());
/* 288 */     this.blockAtlas = blockAtlas;
/* 289 */     this.itemAtlas = itemAtlas;
/*     */   }
/*     */   
/*     */   protected Material.Baked bake(Material material) {
/* 294 */     Material.Baked itemMaterial = bakeForAtlas(material, this.itemAtlas);
/* 295 */     if (itemMaterial != null)
/* 296 */       return itemMaterial; 
/* 298 */     return bakeForAtlas(material, this.blockAtlas);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\ModelManager$CombinedBlockItemMaterialBaker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */