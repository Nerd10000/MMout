/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public final class Layer extends Record {
/*     */   private final boolean translucent;
/*     */   
/*     */   private final Identifier textureAtlasLocation;
/*     */   
/*     */   private final RenderPipeline pipeline;
/*     */   
/*     */   public Layer(boolean translucent, Identifier textureAtlasLocation, RenderPipeline pipeline) {
/* 133 */     this.translucent = translucent;
/* 133 */     this.textureAtlasLocation = textureAtlasLocation;
/* 133 */     this.pipeline = pipeline;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/particle/SingleQuadParticle$Layer;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #133	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/particle/SingleQuadParticle$Layer;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/particle/SingleQuadParticle$Layer;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #133	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/particle/SingleQuadParticle$Layer;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/particle/SingleQuadParticle$Layer;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #133	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/particle/SingleQuadParticle$Layer;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public boolean translucent() {
/* 133 */     return this.translucent;
/*     */   }
/*     */   
/*     */   public Identifier textureAtlasLocation() {
/* 133 */     return this.textureAtlasLocation;
/*     */   }
/*     */   
/*     */   public RenderPipeline pipeline() {
/* 133 */     return this.pipeline;
/*     */   }
/*     */   
/* 134 */   public static final Layer OPAQUE_TERRAIN = new Layer(false, TextureAtlas.LOCATION_BLOCKS, RenderPipelines.OPAQUE_PARTICLE);
/*     */   
/* 135 */   public static final Layer TRANSLUCENT_TERRAIN = new Layer(true, TextureAtlas.LOCATION_BLOCKS, RenderPipelines.TRANSLUCENT_PARTICLE);
/*     */   
/* 136 */   public static final Layer OPAQUE_ITEMS = new Layer(false, TextureAtlas.LOCATION_ITEMS, RenderPipelines.OPAQUE_PARTICLE);
/*     */   
/* 137 */   public static final Layer TRANSLUCENT_ITEMS = new Layer(true, TextureAtlas.LOCATION_ITEMS, RenderPipelines.TRANSLUCENT_PARTICLE);
/*     */   
/* 138 */   public static final Layer OPAQUE = new Layer(false, TextureAtlas.LOCATION_PARTICLES, RenderPipelines.OPAQUE_PARTICLE);
/*     */   
/* 139 */   public static final Layer TRANSLUCENT = new Layer(true, TextureAtlas.LOCATION_PARTICLES, RenderPipelines.TRANSLUCENT_PARTICLE);
/*     */   
/*     */   public static Layer bySprite(TextureAtlasSprite sprite) {
/* 142 */     boolean translucent = sprite.transparency().hasTranslucent();
/* 143 */     if (sprite.atlasLocation().equals(TextureAtlas.LOCATION_BLOCKS))
/* 144 */       return translucent ? TRANSLUCENT_TERRAIN : OPAQUE_TERRAIN; 
/* 145 */     if (sprite.atlasLocation().equals(TextureAtlas.LOCATION_ITEMS))
/* 146 */       return translucent ? TRANSLUCENT_ITEMS : OPAQUE_ITEMS; 
/* 148 */     return translucent ? TRANSLUCENT : OPAQUE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\SingleQuadParticle$Layer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */