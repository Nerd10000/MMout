/*     */ package net.minecraft.client.resources.metadata.gui;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum Type implements StringRepresentable {
/* 103 */   STRETCH("stretch", (MapCodec)GuiSpriteScaling.Stretch.CODEC),
/* 104 */   TILE("tile", (MapCodec)GuiSpriteScaling.Tile.CODEC),
/* 105 */   NINE_SLICE("nine_slice", (MapCodec)GuiSpriteScaling.NineSlice.CODEC);
/*     */   
/*     */   public static final Codec<Type> CODEC;
/*     */   
/*     */   private final String key;
/*     */   
/*     */   private final MapCodec<? extends GuiSpriteScaling> codec;
/*     */   
/*     */   static {
/* 108 */     CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/*     */   }
/*     */   
/*     */   Type(String key, MapCodec<? extends GuiSpriteScaling> codec) {
/* 114 */     this.key = key;
/* 115 */     this.codec = codec;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 120 */     return this.key;
/*     */   }
/*     */   
/*     */   public MapCodec<? extends GuiSpriteScaling> codec() {
/* 124 */     return this.codec;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\metadata\gui\GuiSpriteScaling$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */