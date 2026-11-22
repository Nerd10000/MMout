/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ 
/*    */ final class PositionPredicate extends Record {
/*    */   private final MinMaxBounds.Doubles x;
/*    */   
/*    */   private final MinMaxBounds.Doubles y;
/*    */   
/*    */   private final MinMaxBounds.Doubles z;
/*    */   
/*    */   public static final Codec<PositionPredicate> CODEC;
/*    */   
/*    */   private PositionPredicate(MinMaxBounds.Doubles x, MinMaxBounds.Doubles y, MinMaxBounds.Doubles z) {
/* 79 */     this.x = x;
/* 79 */     this.y = y;
/* 79 */     this.z = z;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/LocationPredicate$PositionPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #79	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/LocationPredicate$PositionPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/LocationPredicate$PositionPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #79	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/LocationPredicate$PositionPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/LocationPredicate$PositionPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #79	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/LocationPredicate$PositionPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Doubles x() {
/* 79 */     return this.x;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Doubles y() {
/* 79 */     return this.y;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Doubles z() {
/* 79 */     return this.z;
/*    */   }
/*    */   
/*    */   static {
/* 80 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("x", MinMaxBounds.Doubles.ANY).forGetter(PositionPredicate::x), (App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("y", MinMaxBounds.Doubles.ANY).forGetter(PositionPredicate::y), (App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("z", MinMaxBounds.Doubles.ANY).forGetter(PositionPredicate::z)).apply((Applicative)i, PositionPredicate::new));
/*    */   }
/*    */   
/*    */   private static Optional<PositionPredicate> of(MinMaxBounds.Doubles x, MinMaxBounds.Doubles y, MinMaxBounds.Doubles z) {
/* 87 */     if (x.isAny() && y.isAny() && z.isAny())
/* 88 */       return Optional.empty(); 
/* 90 */     return Optional.of(new PositionPredicate(x, y, z));
/*    */   }
/*    */   
/*    */   public boolean matches(double x, double y, double z) {
/* 94 */     return (this.x.matches(x) && this.y.matches(y) && this.z.matches(z));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\LocationPredicate$PositionPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */