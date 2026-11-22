/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*    */ import java.util.Locale;
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ public enum ChunkSectionLayerGroup {
/*  9 */   OPAQUE(new ChunkSectionLayer[] { ChunkSectionLayer.SOLID, ChunkSectionLayer.CUTOUT }),
/* 10 */   TRANSLUCENT(new ChunkSectionLayer[] { ChunkSectionLayer.TRANSLUCENT });
/*    */   
/*    */   private final String label;
/*    */   
/*    */   private final ChunkSectionLayer[] layers;
/*    */   
/*    */   ChunkSectionLayerGroup(ChunkSectionLayer... layers) {
/* 17 */     this.layers = layers;
/* 18 */     this.label = toString().toLowerCase(Locale.ROOT);
/*    */   }
/*    */   
/*    */   public String label() {
/* 22 */     return this.label;
/*    */   }
/*    */   
/*    */   public ChunkSectionLayer[] layers() {
/* 26 */     return this.layers;
/*    */   }
/*    */   
/*    */   public RenderTarget outputTarget() {
/* 30 */     Minecraft minecraft = Minecraft.getInstance();
/* 31 */     switch (ordinal()) {
/*    */       case 1:
/*    */       
/*    */       default:
/*    */         break;
/*    */     } 
/* 31 */     RenderTarget renderTarget = 
/*    */       
/* 33 */       minecraft.gameRenderer.mainRenderTarget();
/* 35 */     return (renderTarget != null) ? renderTarget : minecraft.gameRenderer.mainRenderTarget();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\chunk\ChunkSectionLayerGroup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */