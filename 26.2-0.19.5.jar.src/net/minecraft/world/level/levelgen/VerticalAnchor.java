/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.dimension.DimensionType;
/*    */ 
/*    */ public interface VerticalAnchor {
/* 10 */   public static final Codec<VerticalAnchor> CODEC = Codec.xor(Absolute.CODEC, 
/*    */       
/* 12 */       Codec.xor(AboveBottom.CODEC, BelowTop.CODEC))
/*    */     
/* 16 */     .xmap(VerticalAnchor::merge, VerticalAnchor::split);
/*    */   
/* 22 */   public static final VerticalAnchor BOTTOM = aboveBottom(0);
/*    */   
/* 24 */   public static final VerticalAnchor TOP = belowTop(0);
/*    */   
/*    */   static VerticalAnchor absolute(int value) {
/* 27 */     return new Absolute(value);
/*    */   }
/*    */   
/*    */   static VerticalAnchor aboveBottom(int offset) {
/* 31 */     return new AboveBottom(offset);
/*    */   }
/*    */   
/*    */   static VerticalAnchor belowTop(int offset) {
/* 35 */     return new BelowTop(offset);
/*    */   }
/*    */   
/*    */   static VerticalAnchor bottom() {
/* 39 */     return BOTTOM;
/*    */   }
/*    */   
/*    */   static VerticalAnchor top() {
/* 43 */     return TOP;
/*    */   }
/*    */   
/*    */   private static VerticalAnchor merge(Either<Absolute, Either<AboveBottom, BelowTop>> either) {
/* 47 */     return (VerticalAnchor)either.map(Function.identity(), Either::unwrap);
/*    */   }
/*    */   
/*    */   private static Either<Absolute, Either<AboveBottom, BelowTop>> split(VerticalAnchor anchor) {
/* 51 */     if (anchor instanceof Absolute) {
/* 51 */       Absolute absolute = (Absolute)anchor;
/* 52 */       return Either.left(absolute);
/*    */     } 
/* 54 */     AboveBottom aboveBottom = (AboveBottom)anchor;
/* 54 */     return Either.right((anchor instanceof AboveBottom) ? Either.left(aboveBottom) : Either.right(anchor));
/*    */   }
/*    */   
/*    */   int resolveY(WorldGenerationContext paramWorldGenerationContext);
/*    */   
/*    */   public static final class Absolute extends Record implements VerticalAnchor {
/*    */     private final int y;
/*    */     
/*    */     public Absolute(int y) {
/* 59 */       this.y = y;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/VerticalAnchor$Absolute;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #59	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/VerticalAnchor$Absolute;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/VerticalAnchor$Absolute;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #59	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/VerticalAnchor$Absolute;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public int y() {
/* 59 */       return this.y;
/*    */     }
/*    */     
/* 60 */     public static final Codec<Absolute> CODEC = Codec.intRange(DimensionType.MIN_Y, DimensionType.MAX_Y).fieldOf("absolute").xmap(Absolute::new, Absolute::y).codec();
/*    */     
/*    */     public int resolveY(WorldGenerationContext heightAccessor) {
/* 64 */       return this.y;
/*    */     }
/*    */     
/*    */     public String toString() {
/* 69 */       return "" + this.y + " absolute";
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class AboveBottom extends Record implements VerticalAnchor {
/*    */     private final int offset;
/*    */     
/*    */     public AboveBottom(int offset) {
/* 73 */       this.offset = offset;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/VerticalAnchor$AboveBottom;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #73	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/VerticalAnchor$AboveBottom;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/VerticalAnchor$AboveBottom;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #73	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/VerticalAnchor$AboveBottom;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public int offset() {
/* 73 */       return this.offset;
/*    */     }
/*    */     
/* 74 */     public static final Codec<AboveBottom> CODEC = Codec.intRange(DimensionType.MIN_Y, DimensionType.MAX_Y).fieldOf("above_bottom").xmap(AboveBottom::new, AboveBottom::offset).codec();
/*    */     
/*    */     public int resolveY(WorldGenerationContext heightAccessor) {
/* 78 */       return heightAccessor.getMinGenY() + this.offset;
/*    */     }
/*    */     
/*    */     public String toString() {
/* 83 */       return "" + this.offset + " above bottom";
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class BelowTop extends Record implements VerticalAnchor {
/*    */     private final int offset;
/*    */     
/*    */     public BelowTop(int offset) {
/* 87 */       this.offset = offset;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/VerticalAnchor$BelowTop;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #87	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/VerticalAnchor$BelowTop;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/VerticalAnchor$BelowTop;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #87	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/VerticalAnchor$BelowTop;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public int offset() {
/* 87 */       return this.offset;
/*    */     }
/*    */     
/* 88 */     public static final Codec<BelowTop> CODEC = Codec.intRange(DimensionType.MIN_Y, DimensionType.MAX_Y).fieldOf("below_top").xmap(BelowTop::new, BelowTop::offset).codec();
/*    */     
/*    */     public int resolveY(WorldGenerationContext heightAccessor) {
/* 92 */       return heightAccessor.getGenDepth() - 1 + heightAccessor.getMinGenY() - this.offset;
/*    */     }
/*    */     
/*    */     public String toString() {
/* 97 */       return "" + this.offset + " below top";
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\VerticalAnchor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */