/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public final class Ints extends Record implements MinMaxBounds<Integer> {
/*    */   private final MinMaxBounds.Bounds<Integer> bounds;
/*    */   
/*    */   private final MinMaxBounds.Bounds<Long> boundsSqr;
/*    */   
/*    */   public Ints(MinMaxBounds.Bounds<Integer> bounds, MinMaxBounds.Bounds<Long> boundsSqr) {
/* 23 */     this.bounds = bounds;
/* 23 */     this.boundsSqr = boundsSqr;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/MinMaxBounds$Ints;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Ints;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/MinMaxBounds$Ints;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Ints;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/MinMaxBounds$Ints;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Ints;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Bounds<Integer> bounds() {
/* 23 */     return this.bounds;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Bounds<Long> boundsSqr() {
/* 23 */     return this.boundsSqr;
/*    */   }
/*    */   
/* 24 */   public static final Ints ANY = new Ints(MinMaxBounds.Bounds.any());
/*    */   
/* 26 */   public static final Codec<Ints> CODEC = MinMaxBounds.Bounds.<Number>createCodec((Codec<Number>)Codec.INT)
/* 27 */     .validate(MinMaxBounds.Bounds::validateSwappedBoundsInCodec)
/* 28 */     .xmap(Ints::new, Ints::bounds);
/*    */   
/* 30 */   public static final StreamCodec<ByteBuf, Ints> STREAM_CODEC = MinMaxBounds.Bounds.<ByteBuf, Number>createStreamCodec(ByteBufCodecs.INT)
/* 31 */     .map(Ints::new, Ints::bounds);
/*    */   
/*    */   private Ints(MinMaxBounds.Bounds<Integer> bounds) {
/* 34 */     this(bounds, bounds.map(i -> Long.valueOf(Mth.square(i.longValue()))));
/*    */   }
/*    */   
/*    */   public static Ints exactly(int value) {
/* 38 */     return new Ints(MinMaxBounds.Bounds.exactly(Integer.valueOf(value)));
/*    */   }
/*    */   
/*    */   public static Ints between(int min, int max) {
/* 42 */     return new Ints(MinMaxBounds.Bounds.between(Integer.valueOf(min), Integer.valueOf(max)));
/*    */   }
/*    */   
/*    */   public static Ints atLeast(int value) {
/* 46 */     return new Ints(MinMaxBounds.Bounds.atLeast(Integer.valueOf(value)));
/*    */   }
/*    */   
/*    */   public static Ints atMost(int value) {
/* 50 */     return new Ints(MinMaxBounds.Bounds.atMost(Integer.valueOf(value)));
/*    */   }
/*    */   
/*    */   public boolean matches(int value) {
/* 54 */     if (this.bounds.min.isPresent() && ((Integer)this.bounds.min.get()).intValue() > value)
/* 55 */       return false; 
/* 57 */     return (this.bounds.max.isEmpty() || ((Integer)this.bounds.max.get()).intValue() >= value);
/*    */   }
/*    */   
/*    */   public boolean matchesSqr(long valueSqr) {
/* 61 */     if (this.boundsSqr.min.isPresent() && ((Long)this.boundsSqr.min.get()).longValue() > valueSqr)
/* 62 */       return false; 
/* 64 */     return (this.boundsSqr.max.isEmpty() || ((Long)this.boundsSqr.max.get()).longValue() >= valueSqr);
/*    */   }
/*    */   
/*    */   public static Ints fromReader(StringReader reader) throws CommandSyntaxException {
/* 68 */     int start = reader.getCursor();
/* 70 */     Objects.requireNonNull(CommandSyntaxException.BUILT_IN_EXCEPTIONS);
/* 70 */     MinMaxBounds.Bounds<Integer> bounds = MinMaxBounds.Bounds.fromReader(reader, Integer::parseInt, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidInt);
/* 72 */     if (bounds.areSwapped()) {
/* 73 */       reader.setCursor(start);
/* 74 */       throw ERROR_SWAPPED.createWithContext(reader);
/*    */     } 
/* 77 */     return new Ints(bounds);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\MinMaxBounds$Ints.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */