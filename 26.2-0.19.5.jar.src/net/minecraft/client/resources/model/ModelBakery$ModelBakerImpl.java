/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
/*     */ import net.minecraft.client.resources.model.sprite.MaterialBaker;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class ModelBakerImpl implements ModelBaker {
/*     */   private final MaterialBaker materials;
/*     */   
/*     */   private final ModelBaker.Interner interner;
/*     */   
/*     */   private final ModelBakery.MissingModels missingModels;
/*     */   
/* 213 */   private final Map<ModelBaker.SharedOperationKey<Object>, Object> operationCache = new ConcurrentHashMap<>();
/*     */   
/*     */   private final Function<ModelBaker.SharedOperationKey<Object>, Object> cacheComputeFunction;
/*     */   
/*     */   private ModelBakerImpl(MaterialBaker materials, ModelBaker.Interner interner, ModelBakery.MissingModels missingModels) {
/* 214 */     this.cacheComputeFunction = (k -> k.compute(this));
/* 217 */     this.materials = materials;
/* 218 */     this.interner = interner;
/* 219 */     this.missingModels = missingModels;
/*     */   }
/*     */   
/*     */   public BlockStateModelPart missingBlockModelPart() {
/* 224 */     return this.missingModels.blockPart;
/*     */   }
/*     */   
/*     */   public MaterialBaker materials() {
/* 229 */     return this.materials;
/*     */   }
/*     */   
/*     */   public ModelBaker.Interner interner() {
/* 234 */     return this.interner;
/*     */   }
/*     */   
/*     */   public ResolvedModel getModel(Identifier location) {
/* 239 */     ResolvedModel result = ModelBakery.this.resolvedModels.get(location);
/* 240 */     if (result == null) {
/* 241 */       ModelBakery.LOGGER.warn("Requested a model that was not discovered previously: {}", location);
/* 242 */       return ModelBakery.this.missingModel;
/*     */     } 
/* 244 */     return result;
/*     */   }
/*     */   
/*     */   public <T> T compute(ModelBaker.SharedOperationKey<T> key) {
/* 250 */     return (T)this.operationCache.computeIfAbsent(key, this.cacheComputeFunction);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\ModelBakery$ModelBakerImpl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */