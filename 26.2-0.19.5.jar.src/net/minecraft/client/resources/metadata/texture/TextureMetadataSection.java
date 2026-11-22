/*    */ package net.minecraft.client.resources.metadata.texture;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.client.renderer.texture.MipmapStrategy;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ 
/*    */ public final class TextureMetadataSection extends Record {
/*    */   private final boolean blur;
/*    */   
/*    */   private final boolean clamp;
/*    */   
/*    */   private final MipmapStrategy mipmapStrategy;
/*    */   
/*    */   private final float alphaCutoffBias;
/*    */   
/*    */   public static final boolean DEFAULT_BLUR = false;
/*    */   
/*    */   public static final boolean DEFAULT_CLAMP = false;
/*    */   
/*    */   public static final float DEFAULT_ALPHA_CUTOFF_BIAS = 0.0F;
/*    */   
/*    */   public static final Codec<TextureMetadataSection> CODEC;
/*    */   
/*    */   public TextureMetadataSection(boolean blur, boolean clamp, MipmapStrategy mipmapStrategy, float alphaCutoffBias) {
/*  8 */     this.blur = blur;
/*  8 */     this.clamp = clamp;
/*  8 */     this.mipmapStrategy = mipmapStrategy;
/*  8 */     this.alphaCutoffBias = alphaCutoffBias;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/metadata/texture/TextureMetadataSection;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/texture/TextureMetadataSection;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/metadata/texture/TextureMetadataSection;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/texture/TextureMetadataSection;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/metadata/texture/TextureMetadataSection;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/metadata/texture/TextureMetadataSection;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public boolean blur() {
/*  8 */     return this.blur;
/*    */   }
/*    */   
/*    */   public boolean clamp() {
/*  8 */     return this.clamp;
/*    */   }
/*    */   
/*    */   public MipmapStrategy mipmapStrategy() {
/*  8 */     return this.mipmapStrategy;
/*    */   }
/*    */   
/*    */   public float alphaCutoffBias() {
/*  8 */     return this.alphaCutoffBias;
/*    */   }
/*    */   
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.BOOL.optionalFieldOf("blur", Boolean.valueOf(false)).forGetter(TextureMetadataSection::blur), (App)Codec.BOOL.optionalFieldOf("clamp", Boolean.valueOf(false)).forGetter(TextureMetadataSection::clamp), (App)MipmapStrategy.CODEC.optionalFieldOf("mipmap_strategy", MipmapStrategy.AUTO).forGetter(TextureMetadataSection::mipmapStrategy), (App)Codec.FLOAT.optionalFieldOf("alpha_cutoff_bias", Float.valueOf(0.0F)).forGetter(TextureMetadataSection::alphaCutoffBias)).apply((Applicative)i, TextureMetadataSection::new));
/*    */   }
/*    */   
/* 25 */   public static final MetadataSectionType<TextureMetadataSection> TYPE = new MetadataSectionType("texture", CODEC);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\metadata\texture\TextureMetadataSection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */