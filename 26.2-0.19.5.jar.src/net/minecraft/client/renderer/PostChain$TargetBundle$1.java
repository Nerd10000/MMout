/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.resource.ResourceHandle;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class null implements PostChain.TargetBundle {
/* 181 */   private ResourceHandle<RenderTarget> handle = target;
/*     */   
/*     */   public void replace(Identifier id, ResourceHandle<RenderTarget> handle) {
/* 185 */     if (id.equals(targetId)) {
/* 186 */       this.handle = handle;
/*     */     } else {
/* 188 */       throw new IllegalArgumentException("No target with id " + String.valueOf(id));
/*     */     } 
/*     */   }
/*     */   
/*     */   public ResourceHandle<RenderTarget> get(Identifier id) {
/* 194 */     return id.equals(targetId) ? this.handle : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\PostChain$TargetBundle$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */