/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.resource.ResourceHandle;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public interface TargetBundle {
/*     */   static TargetBundle of(final Identifier targetId, final ResourceHandle<RenderTarget> target) {
/* 180 */     return new TargetBundle() {
/* 181 */         private ResourceHandle<RenderTarget> handle = target;
/*     */         
/*     */         public void replace(Identifier id, ResourceHandle<RenderTarget> handle) {
/* 185 */           if (id.equals(targetId)) {
/* 186 */             this.handle = handle;
/*     */           } else {
/* 188 */             throw new IllegalArgumentException("No target with id " + String.valueOf(id));
/*     */           } 
/*     */         }
/*     */         
/*     */         public ResourceHandle<RenderTarget> get(Identifier id) {
/* 194 */           return id.equals(targetId) ? this.handle : null;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   void replace(Identifier paramIdentifier, ResourceHandle<RenderTarget> paramResourceHandle);
/*     */   
/*     */   ResourceHandle<RenderTarget> get(Identifier paramIdentifier);
/*     */   
/*     */   default ResourceHandle<RenderTarget> getOrThrow(Identifier id) {
/* 204 */     ResourceHandle<RenderTarget> handle = get(id);
/* 205 */     if (handle == null)
/* 206 */       throw new IllegalArgumentException("Missing target with id " + String.valueOf(id)); 
/* 208 */     return handle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\PostChain$TargetBundle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */