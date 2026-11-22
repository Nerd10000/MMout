/*    */ package net.minecraft.world.level.levelgen.structure.pools;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class DimensionPadding extends Record {
/*    */   private final int bottom;
/*    */   
/*    */   private final int top;
/*    */   
/*    */   private static final Codec<DimensionPadding> RECORD_CODEC;
/*    */   
/*    */   public static final Codec<DimensionPadding> CODEC;
/*    */   
/*    */   public DimensionPadding(int bottom, int top) {
/* 10 */     this.bottom = bottom;
/* 10 */     this.top = top;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/pools/DimensionPadding;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/DimensionPadding;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/pools/DimensionPadding;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/DimensionPadding;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/pools/DimensionPadding;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/DimensionPadding;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int bottom() {
/* 10 */     return this.bottom;
/*    */   }
/*    */   
/*    */   public int top() {
/* 10 */     return this.top;
/*    */   }
/*    */   
/*    */   static {
/* 11 */     RECORD_CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.lenientOptionalFieldOf("bottom", Integer.valueOf(0)).forGetter(()), (App)ExtraCodecs.NON_NEGATIVE_INT.lenientOptionalFieldOf("top", Integer.valueOf(0)).forGetter(())).apply((Applicative)i, DimensionPadding::new));
/* 16 */     CODEC = Codec.either(ExtraCodecs.NON_NEGATIVE_INT, RECORD_CODEC).xmap(e -> (DimensionPadding)e.map(DimensionPadding::new, Function.identity()), padding -> padding.hasEqualTopAndBottom() ? Either.left(Integer.valueOf(padding.bottom)) : Either.right(padding));
/*    */   }
/*    */   
/* 20 */   public static final DimensionPadding ZERO = new DimensionPadding(0);
/*    */   
/*    */   public DimensionPadding(int value) {
/* 23 */     this(value, value);
/*    */   }
/*    */   
/*    */   public boolean hasEqualTopAndBottom() {
/* 27 */     return (this.top == this.bottom);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\pools\DimensionPadding.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */