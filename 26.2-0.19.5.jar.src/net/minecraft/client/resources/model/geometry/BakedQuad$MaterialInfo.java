/*    */ package net.minecraft.client.resources.model.geometry;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.Transparency;
/*    */ import net.minecraft.client.renderer.Sheets;
/*    */ import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.client.resources.model.sprite.Material;
/*    */ 
/*    */ public final class MaterialInfo extends Record {
/*    */   private final TextureAtlasSprite sprite;
/*    */   
/*    */   private final ChunkSectionLayer layer;
/*    */   
/*    */   private final RenderType itemRenderType;
/*    */   
/*    */   private final int tintIndex;
/*    */   
/*    */   private final boolean shade;
/*    */   
/*    */   private final int lightEmission;
/*    */   
/*    */   public MaterialInfo(TextureAtlasSprite sprite, ChunkSectionLayer layer, RenderType itemRenderType, int tintIndex, boolean shade, int lightEmission) {
/* 60 */     this.sprite = sprite;
/* 60 */     this.layer = layer;
/* 60 */     this.itemRenderType = itemRenderType;
/* 60 */     this.tintIndex = tintIndex;
/* 60 */     this.shade = shade;
/* 60 */     this.lightEmission = lightEmission;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/geometry/BakedQuad$MaterialInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #60	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/geometry/BakedQuad$MaterialInfo;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/geometry/BakedQuad$MaterialInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #60	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/geometry/BakedQuad$MaterialInfo;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/geometry/BakedQuad$MaterialInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #60	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/geometry/BakedQuad$MaterialInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public TextureAtlasSprite sprite() {
/* 60 */     return this.sprite;
/*    */   }
/*    */   
/*    */   public ChunkSectionLayer layer() {
/* 60 */     return this.layer;
/*    */   }
/*    */   
/*    */   public RenderType itemRenderType() {
/* 60 */     return this.itemRenderType;
/*    */   }
/*    */   
/*    */   public int tintIndex() {
/* 60 */     return this.tintIndex;
/*    */   }
/*    */   
/*    */   public boolean shade() {
/* 60 */     return this.shade;
/*    */   }
/*    */   
/*    */   public int lightEmission() {
/* 60 */     return this.lightEmission;
/*    */   }
/*    */   
/*    */   public static MaterialInfo of(Material.Baked material, Transparency transparency, int tintIndex, boolean shade, int lightEmission) {
/*    */     RenderType itemRenderType;
/* 69 */     ChunkSectionLayer layer = ChunkSectionLayer.byTransparency(transparency);
/* 71 */     if (material.sprite().atlasLocation().equals(TextureAtlas.LOCATION_BLOCKS)) {
/* 72 */       itemRenderType = transparency.hasTranslucent() ? Sheets.translucentBlockItemSheet() : Sheets.cutoutBlockItemSheet();
/*    */     } else {
/* 74 */       itemRenderType = transparency.hasTranslucent() ? Sheets.translucentItemSheet() : Sheets.cutoutItemSheet();
/*    */     } 
/* 76 */     return new MaterialInfo(material.sprite(), layer, itemRenderType, tintIndex, shade, lightEmission);
/*    */   }
/*    */   
/*    */   public boolean isTinted() {
/* 80 */     return (this.tintIndex != -1);
/*    */   }
/*    */   
/*    */   public int flags() {
/* 84 */     int flags = 0;
/* 85 */     flags |= this.layer.translucent() ? 1 : 0;
/* 86 */     flags |= this.sprite.contents().isAnimated() ? 2 : 0;
/* 87 */     return flags;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\geometry\BakedQuad$MaterialInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */