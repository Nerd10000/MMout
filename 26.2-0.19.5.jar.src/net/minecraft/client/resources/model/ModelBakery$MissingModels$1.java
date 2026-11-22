/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
/*     */ import net.minecraft.client.resources.model.sprite.MaterialBaker;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class null implements ModelBaker {
/*     */   public ResolvedModel getModel(Identifier location) {
/*  95 */     throw new IllegalStateException("Missing model can't have dependencies, but asked for " + String.valueOf(location));
/*     */   }
/*     */   
/*     */   public BlockStateModelPart missingBlockModelPart() {
/* 101 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public <T> T compute(ModelBaker.SharedOperationKey<T> key) {
/* 107 */     return key.compute(this);
/*     */   }
/*     */   
/*     */   public MaterialBaker materials() {
/* 112 */     return materials;
/*     */   }
/*     */   
/*     */   public ModelBaker.Interner interner() {
/* 117 */     return interner;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\ModelBakery$MissingModels$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */