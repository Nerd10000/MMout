/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.dimension.DimensionType;
/*    */ 
/*    */ public final class BelowTop extends Record implements VerticalAnchor {
/*    */   private final int offset;
/*    */   
/*    */   public BelowTop(int offset) {
/* 87 */     this.offset = offset;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/VerticalAnchor$BelowTop;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #87	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/VerticalAnchor$BelowTop;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/VerticalAnchor$BelowTop;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #87	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/VerticalAnchor$BelowTop;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int offset() {
/* 87 */     return this.offset;
/*    */   }
/*    */   
/* 88 */   public static final Codec<BelowTop> CODEC = Codec.intRange(DimensionType.MIN_Y, DimensionType.MAX_Y).fieldOf("below_top").xmap(BelowTop::new, BelowTop::offset).codec();
/*    */   
/*    */   public int resolveY(WorldGenerationContext heightAccessor) {
/* 92 */     return heightAccessor.getGenDepth() - 1 + heightAccessor.getMinGenY() - this.offset;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 97 */     return "" + this.offset + " below top";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\VerticalAnchor$BelowTop.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */