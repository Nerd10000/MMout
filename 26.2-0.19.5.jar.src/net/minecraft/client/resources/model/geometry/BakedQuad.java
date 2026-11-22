/*    */ package net.minecraft.client.resources.model.geometry;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.Transparency;
/*    */ import java.lang.annotation.ElementType;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ import net.minecraft.client.renderer.Sheets;
/*    */ import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.client.resources.model.sprite.Material;
/*    */ import net.minecraft.core.Direction;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public final class BakedQuad extends Record {
/*    */   private final Vector3fc position0;
/*    */   
/*    */   private final Vector3fc position1;
/*    */   
/*    */   private final Vector3fc position2;
/*    */   
/*    */   private final Vector3fc position3;
/*    */   
/*    */   private final long packedUV0;
/*    */   
/*    */   private final long packedUV1;
/*    */   
/*    */   private final long packedUV2;
/*    */   
/*    */   private final long packedUV3;
/*    */   
/*    */   private final Direction direction;
/*    */   
/*    */   private final MaterialInfo materialInfo;
/*    */   
/*    */   public static final int VERTEX_COUNT = 4;
/*    */   
/*    */   public static final int FLAG_TRANSLUCENT = 1;
/*    */   
/*    */   public static final int FLAG_ANIMATED = 2;
/*    */   
/*    */   public BakedQuad(Vector3fc position0, Vector3fc position1, Vector3fc position2, Vector3fc position3, long packedUV0, long packedUV1, long packedUV2, long packedUV3, Direction direction, MaterialInfo materialInfo) {
/* 20 */     this.position0 = position0;
/* 20 */     this.position1 = position1;
/* 20 */     this.position2 = position2;
/* 20 */     this.position3 = position3;
/* 20 */     this.packedUV0 = packedUV0;
/* 20 */     this.packedUV1 = packedUV1;
/* 20 */     this.packedUV2 = packedUV2;
/* 20 */     this.packedUV3 = packedUV3;
/* 20 */     this.direction = direction;
/* 20 */     this.materialInfo = materialInfo;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/geometry/BakedQuad;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/geometry/BakedQuad;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/geometry/BakedQuad;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/geometry/BakedQuad;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/geometry/BakedQuad;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/geometry/BakedQuad;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vector3fc position0() {
/* 20 */     return this.position0;
/*    */   }
/*    */   
/*    */   public Vector3fc position1() {
/* 20 */     return this.position1;
/*    */   }
/*    */   
/*    */   public Vector3fc position2() {
/* 20 */     return this.position2;
/*    */   }
/*    */   
/*    */   public Vector3fc position3() {
/* 20 */     return this.position3;
/*    */   }
/*    */   
/*    */   public long packedUV0() {
/* 20 */     return this.packedUV0;
/*    */   }
/*    */   
/*    */   public long packedUV1() {
/* 20 */     return this.packedUV1;
/*    */   }
/*    */   
/*    */   public long packedUV2() {
/* 20 */     return this.packedUV2;
/*    */   }
/*    */   
/*    */   public long packedUV3() {
/* 20 */     return this.packedUV3;
/*    */   }
/*    */   
/*    */   public Direction direction() {
/* 20 */     return this.direction;
/*    */   }
/*    */   
/*    */   public MaterialInfo materialInfo() {
/* 20 */     return this.materialInfo;
/*    */   }
/*    */   
/*    */   public Vector3fc position(int vertex) {
/* 41 */     switch (vertex) {
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */       case 3:
/*    */       
/*    */     } 
/* 46 */     throw new IndexOutOfBoundsException(vertex);
/*    */   }
/*    */   
/*    */   public long packedUV(int vertex) {
/* 51 */     switch (vertex) {
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */       case 3:
/*    */       
/*    */     } 
/* 56 */     throw new IndexOutOfBoundsException(vertex);
/*    */   }
/*    */   
/*    */   public static final class MaterialInfo extends Record {
/*    */     private final TextureAtlasSprite sprite;
/*    */     
/*    */     private final ChunkSectionLayer layer;
/*    */     
/*    */     private final RenderType itemRenderType;
/*    */     
/*    */     private final int tintIndex;
/*    */     
/*    */     private final boolean shade;
/*    */     
/*    */     private final int lightEmission;
/*    */     
/*    */     public MaterialInfo(TextureAtlasSprite sprite, ChunkSectionLayer layer, RenderType itemRenderType, int tintIndex, boolean shade, int lightEmission) {
/* 60 */       this.sprite = sprite;
/* 60 */       this.layer = layer;
/* 60 */       this.itemRenderType = itemRenderType;
/* 60 */       this.tintIndex = tintIndex;
/* 60 */       this.shade = shade;
/* 60 */       this.lightEmission = lightEmission;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/geometry/BakedQuad$MaterialInfo;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #60	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/model/geometry/BakedQuad$MaterialInfo;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/geometry/BakedQuad$MaterialInfo;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #60	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/model/geometry/BakedQuad$MaterialInfo;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/geometry/BakedQuad$MaterialInfo;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #60	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/resources/model/geometry/BakedQuad$MaterialInfo;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public TextureAtlasSprite sprite() {
/* 60 */       return this.sprite;
/*    */     }
/*    */     
/*    */     public ChunkSectionLayer layer() {
/* 60 */       return this.layer;
/*    */     }
/*    */     
/*    */     public RenderType itemRenderType() {
/* 60 */       return this.itemRenderType;
/*    */     }
/*    */     
/*    */     public int tintIndex() {
/* 60 */       return this.tintIndex;
/*    */     }
/*    */     
/*    */     public boolean shade() {
/* 60 */       return this.shade;
/*    */     }
/*    */     
/*    */     public int lightEmission() {
/* 60 */       return this.lightEmission;
/*    */     }
/*    */     
/*    */     public static MaterialInfo of(Material.Baked material, Transparency transparency, int tintIndex, boolean shade, int lightEmission) {
/*    */       RenderType itemRenderType;
/* 69 */       ChunkSectionLayer layer = ChunkSectionLayer.byTransparency(transparency);
/* 71 */       if (material.sprite().atlasLocation().equals(TextureAtlas.LOCATION_BLOCKS)) {
/* 72 */         itemRenderType = transparency.hasTranslucent() ? Sheets.translucentBlockItemSheet() : Sheets.cutoutBlockItemSheet();
/*    */       } else {
/* 74 */         itemRenderType = transparency.hasTranslucent() ? Sheets.translucentItemSheet() : Sheets.cutoutItemSheet();
/*    */       } 
/* 76 */       return new MaterialInfo(material.sprite(), layer, itemRenderType, tintIndex, shade, lightEmission);
/*    */     }
/*    */     
/*    */     public boolean isTinted() {
/* 80 */       return (this.tintIndex != -1);
/*    */     }
/*    */     
/*    */     public int flags() {
/* 84 */       int flags = 0;
/* 85 */       flags |= this.layer.translucent() ? 1 : 0;
/* 86 */       flags |= this.sprite.contents().isAnimated() ? 2 : 0;
/* 87 */       return flags;
/*    */     }
/*    */   }
/*    */   
/*    */   @Retention(RetentionPolicy.CLASS)
/*    */   @Target({ElementType.TYPE_USE})
/*    */   public static @interface MaterialFlags {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\geometry\BakedQuad.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */