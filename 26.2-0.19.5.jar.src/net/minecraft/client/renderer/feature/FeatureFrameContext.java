/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ import net.minecraft.client.color.block.BlockColors;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.renderer.StagedVertexBuffer;
/*    */ import net.minecraft.client.renderer.block.BlockStateModelSet;
/*    */ import net.minecraft.client.renderer.state.OptionsRenderState;
/*    */ import net.minecraft.client.renderer.texture.TextureManager;
/*    */ import net.minecraft.client.resources.model.sprite.AtlasManager;
/*    */ 
/*    */ public final class FeatureFrameContext extends Record {
/*    */   private final OptionsRenderState options;
/*    */   
/*    */   private final Font font;
/*    */   
/*    */   private final BlockStateModelSet blockStateModelSet;
/*    */   
/*    */   private final BlockColors blockColors;
/*    */   
/*    */   private final TextureManager textureManager;
/*    */   
/*    */   private final AtlasManager atlasManager;
/*    */   
/*    */   private final GpuTextureView lightmap;
/*    */   
/*    */   private final StagedVertexBuffer stagedVertexBuffer;
/*    */   
/*    */   public FeatureFrameContext(OptionsRenderState options, Font font, BlockStateModelSet blockStateModelSet, BlockColors blockColors, TextureManager textureManager, AtlasManager atlasManager, GpuTextureView lightmap, StagedVertexBuffer stagedVertexBuffer) {
/* 12 */     this.options = options;
/* 12 */     this.font = font;
/* 12 */     this.blockStateModelSet = blockStateModelSet;
/* 12 */     this.blockColors = blockColors;
/* 12 */     this.textureManager = textureManager;
/* 12 */     this.atlasManager = atlasManager;
/* 12 */     this.lightmap = lightmap;
/* 12 */     this.stagedVertexBuffer = stagedVertexBuffer;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/FeatureFrameContext;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/FeatureFrameContext;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/FeatureFrameContext;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/FeatureFrameContext;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/FeatureFrameContext;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/feature/FeatureFrameContext;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public OptionsRenderState options() {
/* 12 */     return this.options;
/*    */   }
/*    */   
/*    */   public Font font() {
/* 12 */     return this.font;
/*    */   }
/*    */   
/*    */   public BlockStateModelSet blockStateModelSet() {
/* 12 */     return this.blockStateModelSet;
/*    */   }
/*    */   
/*    */   public BlockColors blockColors() {
/* 12 */     return this.blockColors;
/*    */   }
/*    */   
/*    */   public TextureManager textureManager() {
/* 12 */     return this.textureManager;
/*    */   }
/*    */   
/*    */   public AtlasManager atlasManager() {
/* 12 */     return this.atlasManager;
/*    */   }
/*    */   
/*    */   public GpuTextureView lightmap() {
/* 12 */     return this.lightmap;
/*    */   }
/*    */   
/*    */   public StagedVertexBuffer stagedVertexBuffer() {
/* 12 */     return this.stagedVertexBuffer;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\FeatureFrameContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */