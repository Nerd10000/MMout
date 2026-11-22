/*    */ package net.minecraft.network.chat.contents.objects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.data.AtlasIds;
/*    */ import net.minecraft.network.chat.FontDescription;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class AtlasSprite extends Record implements ObjectInfo {
/*    */   private final Identifier atlas;
/*    */   
/*    */   private final Identifier sprite;
/*    */   
/*    */   public AtlasSprite(Identifier atlas, Identifier sprite) {
/*  9 */     this.atlas = atlas;
/*  9 */     this.sprite = sprite;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/contents/objects/AtlasSprite;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/objects/AtlasSprite;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/objects/AtlasSprite;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/objects/AtlasSprite;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/objects/AtlasSprite;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/objects/AtlasSprite;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier atlas() {
/*  9 */     return this.atlas;
/*    */   }
/*    */   
/*    */   public Identifier sprite() {
/*  9 */     return this.sprite;
/*    */   }
/*    */   
/* 13 */   public static final Identifier DEFAULT_ATLAS = AtlasIds.BLOCKS;
/*    */   
/*    */   public static final MapCodec<AtlasSprite> MAP_CODEC;
/*    */   
/*    */   static {
/* 15 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.optionalFieldOf("atlas", DEFAULT_ATLAS).forGetter(AtlasSprite::atlas), (App)Identifier.CODEC.fieldOf("sprite").forGetter(AtlasSprite::sprite)).apply((Applicative)i, AtlasSprite::new));
/*    */   }
/*    */   
/*    */   public MapCodec<AtlasSprite> codec() {
/* 22 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public FontDescription fontDescription() {
/* 27 */     return (FontDescription)new FontDescription.AtlasSprite(this.atlas, this.sprite);
/*    */   }
/*    */   
/*    */   private static String toShortName(Identifier id) {
/* 31 */     return id.getNamespace().equals("minecraft") ? id.getPath() : id.toString();
/*    */   }
/*    */   
/*    */   public String defaultFallback() {
/* 37 */     String shortName = toShortName(this.sprite);
/* 38 */     return this.atlas.equals(DEFAULT_ATLAS) ? ("[" + 
/* 39 */       shortName + "]") : ("[" + 
/*    */       
/* 41 */       shortName + "@" + toShortName(this.atlas) + "]");
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\contents\objects\AtlasSprite.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */