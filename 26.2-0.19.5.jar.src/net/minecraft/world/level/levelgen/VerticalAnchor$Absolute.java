/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.dimension.DimensionType;
/*    */ 
/*    */ public final class Absolute extends Record implements VerticalAnchor {
/*    */   private final int y;
/*    */   
/*    */   public Absolute(int y) {
/* 59 */     this.y = y;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/VerticalAnchor$Absolute;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #59	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/VerticalAnchor$Absolute;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/VerticalAnchor$Absolute;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #59	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/VerticalAnchor$Absolute;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int y() {
/* 59 */     return this.y;
/*    */   }
/*    */   
/* 60 */   public static final Codec<Absolute> CODEC = Codec.intRange(DimensionType.MIN_Y, DimensionType.MAX_Y).fieldOf("absolute").xmap(Absolute::new, Absolute::y).codec();
/*    */   
/*    */   public int resolveY(WorldGenerationContext heightAccessor) {
/* 64 */     return this.y;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 69 */     return "" + this.y + " absolute";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\VerticalAnchor$Absolute.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */