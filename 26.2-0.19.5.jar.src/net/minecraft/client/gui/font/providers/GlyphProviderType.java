/*    */ package net.minecraft.client.gui.font.providers;
/*    */ 
/*    */ import com.mojang.blaze3d.font.SpaceProvider;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum GlyphProviderType implements StringRepresentable {
/*  9 */   BITMAP("bitmap", (MapCodec)BitmapProvider.Definition.CODEC),
/* 10 */   TTF("ttf", (MapCodec)TrueTypeGlyphProviderDefinition.CODEC),
/* 11 */   SPACE("space", SpaceProvider.Definition.CODEC),
/* 12 */   UNIHEX("unihex", (MapCodec)UnihexProvider.Definition.CODEC),
/* 13 */   REFERENCE("reference", (MapCodec)ProviderReferenceDefinition.CODEC);
/*    */   
/*    */   public static final Codec<GlyphProviderType> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final MapCodec<? extends GlyphProviderDefinition> codec;
/*    */   
/*    */   static {
/* 16 */     CODEC = (Codec<GlyphProviderType>)StringRepresentable.fromEnum(GlyphProviderType::values);
/*    */   }
/*    */   
/*    */   GlyphProviderType(String name, MapCodec<? extends GlyphProviderDefinition> codec) {
/* 22 */     this.name = name;
/* 23 */     this.codec = codec;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 28 */     return this.name;
/*    */   }
/*    */   
/*    */   public MapCodec<? extends GlyphProviderDefinition> mapCodec() {
/* 32 */     return this.codec;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\providers\GlyphProviderType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */