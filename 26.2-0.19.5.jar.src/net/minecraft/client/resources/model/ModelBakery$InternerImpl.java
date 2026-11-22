/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import com.google.common.collect.Interner;
/*     */ import com.google.common.collect.Interners;
/*     */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ class InternerImpl implements ModelBaker.Interner {
/* 267 */   private final Interner<Vector3fc> vectors = Interners.newStrongInterner();
/*     */   
/* 268 */   private final Interner<BakedQuad.MaterialInfo> materialInfos = Interners.newStrongInterner();
/*     */   
/*     */   public Vector3fc vector(Vector3fc v) {
/* 272 */     return (Vector3fc)this.vectors.intern(v);
/*     */   }
/*     */   
/*     */   public BakedQuad.MaterialInfo materialInfo(BakedQuad.MaterialInfo material) {
/* 277 */     return (BakedQuad.MaterialInfo)this.materialInfos.intern(material);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\ModelBakery$InternerImpl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */