/*    */ package net.minecraft.client.resources.metadata.gui;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class Tile extends Record implements GuiSpriteScaling {
/*    */   private final int width;
/*    */   
/*    */   private final int height;
/*    */   
/*    */   public static final MapCodec<Tile> CODEC;
/*    */   
/*    */   public Tile(int width, int height) {
/* 29 */     this.width = width;
/* 29 */     this.height = height;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Tile;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Tile;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Tile;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Tile;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Tile;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Tile;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int width() {
/* 29 */     return this.width;
/*    */   }
/*    */   
/*    */   public int height() {
/* 29 */     return this.height;
/*    */   }
/*    */   
/*    */   static {
/* 30 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(Tile::width), (App)ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(Tile::height)).apply((Applicative)i, Tile::new));
/*    */   }
/*    */   
/*    */   public GuiSpriteScaling.Type type() {
/* 37 */     return GuiSpriteScaling.Type.TILE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\metadata\gui\GuiSpriteScaling$Tile.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */