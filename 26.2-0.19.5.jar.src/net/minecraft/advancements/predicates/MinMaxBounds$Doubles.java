/*     */ package net.minecraft.advancements.predicates;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public final class Doubles extends Record implements MinMaxBounds<Double> {
/*     */   private final MinMaxBounds.Bounds<Double> bounds;
/*     */   
/*     */   private final MinMaxBounds.Bounds<Double> boundsSqr;
/*     */   
/*     */   public Doubles(MinMaxBounds.Bounds<Double> bounds, MinMaxBounds.Bounds<Double> boundsSqr) {
/*  81 */     this.bounds = bounds;
/*  81 */     this.boundsSqr = boundsSqr;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/MinMaxBounds$Doubles;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #81	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Doubles;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/MinMaxBounds$Doubles;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #81	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Doubles;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/MinMaxBounds$Doubles;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #81	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Doubles;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public MinMaxBounds.Bounds<Double> bounds() {
/*  81 */     return this.bounds;
/*     */   }
/*     */   
/*     */   public MinMaxBounds.Bounds<Double> boundsSqr() {
/*  81 */     return this.boundsSqr;
/*     */   }
/*     */   
/*  82 */   public static final Doubles ANY = new Doubles(MinMaxBounds.Bounds.any());
/*     */   
/*  84 */   public static final Codec<Doubles> CODEC = MinMaxBounds.Bounds.<Number>createCodec((Codec<Number>)Codec.DOUBLE)
/*  85 */     .validate(MinMaxBounds.Bounds::validateSwappedBoundsInCodec)
/*  86 */     .xmap(Doubles::new, Doubles::bounds);
/*     */   
/*  88 */   public static final StreamCodec<ByteBuf, Doubles> STREAM_CODEC = MinMaxBounds.Bounds.<ByteBuf, Number>createStreamCodec(ByteBufCodecs.DOUBLE)
/*  89 */     .map(Doubles::new, Doubles::bounds);
/*     */   
/*     */   private Doubles(MinMaxBounds.Bounds<Double> bounds) {
/*  92 */     this(bounds, bounds.map(Mth::square));
/*     */   }
/*     */   
/*     */   public static Doubles exactly(double value) {
/*  96 */     return new Doubles(MinMaxBounds.Bounds.exactly(Double.valueOf(value)));
/*     */   }
/*     */   
/*     */   public static Doubles between(double min, double max) {
/* 100 */     return new Doubles(MinMaxBounds.Bounds.between(Double.valueOf(min), Double.valueOf(max)));
/*     */   }
/*     */   
/*     */   public static Doubles atLeast(double value) {
/* 104 */     return new Doubles(MinMaxBounds.Bounds.atLeast(Double.valueOf(value)));
/*     */   }
/*     */   
/*     */   public static Doubles atMost(double value) {
/* 108 */     return new Doubles(MinMaxBounds.Bounds.atMost(Double.valueOf(value)));
/*     */   }
/*     */   
/*     */   public boolean matches(double value) {
/* 112 */     if (this.bounds.min.isPresent() && ((Double)this.bounds.min.get()).doubleValue() > value)
/* 113 */       return false; 
/* 115 */     return (this.bounds.max.isEmpty() || ((Double)this.bounds.max.get()).doubleValue() >= value);
/*     */   }
/*     */   
/*     */   public boolean matchesSqr(double valueSqr) {
/* 119 */     if (this.boundsSqr.min.isPresent() && ((Double)this.boundsSqr.min.get()).doubleValue() > valueSqr)
/* 120 */       return false; 
/* 122 */     return (this.boundsSqr.max.isEmpty() || ((Double)this.boundsSqr.max.get()).doubleValue() >= valueSqr);
/*     */   }
/*     */   
/*     */   public static Doubles fromReader(StringReader reader) throws CommandSyntaxException {
/* 126 */     int start = reader.getCursor();
/* 128 */     Objects.requireNonNull(CommandSyntaxException.BUILT_IN_EXCEPTIONS);
/* 128 */     MinMaxBounds.Bounds<Double> bounds = MinMaxBounds.Bounds.fromReader(reader, Double::parseDouble, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidDouble);
/* 130 */     if (bounds.areSwapped()) {
/* 131 */       reader.setCursor(start);
/* 132 */       throw ERROR_SWAPPED.createWithContext(reader);
/*     */     } 
/* 135 */     return new Doubles(bounds);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\MinMaxBounds$Doubles.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */