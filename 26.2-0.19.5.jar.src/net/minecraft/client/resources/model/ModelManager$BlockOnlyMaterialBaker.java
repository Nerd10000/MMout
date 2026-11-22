/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.texture.SpriteLoader;
/*     */ import net.minecraft.client.resources.model.sprite.Material;
/*     */ import net.minecraft.client.resources.model.sprite.MaterialBaker;
/*     */ 
/*     */ class BlockOnlyMaterialBaker extends MaterialBaker {
/*     */   private final SpriteLoader.Preparations blockAtlas;
/*     */   
/*     */   public BlockOnlyMaterialBaker(SpriteLoader.Preparations blockAtlas) {
/* 272 */     super(blockAtlas.missing());
/* 273 */     this.blockAtlas = blockAtlas;
/*     */   }
/*     */   
/*     */   protected Material.Baked bake(Material material) {
/* 278 */     return bakeForAtlas(material, this.blockAtlas);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\ModelManager$BlockOnlyMaterialBaker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */