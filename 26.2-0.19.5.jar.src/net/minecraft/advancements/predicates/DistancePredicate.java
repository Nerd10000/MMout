/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public final class DistancePredicate extends Record {
/*    */   private final MinMaxBounds.Doubles x;
/*    */   
/*    */   private final MinMaxBounds.Doubles y;
/*    */   
/*    */   private final MinMaxBounds.Doubles z;
/*    */   
/*    */   private final MinMaxBounds.Doubles horizontal;
/*    */   
/*    */   private final MinMaxBounds.Doubles absolute;
/*    */   
/*    */   public static final Codec<DistancePredicate> CODEC;
/*    */   
/*    */   public DistancePredicate(MinMaxBounds.Doubles x, MinMaxBounds.Doubles y, MinMaxBounds.Doubles z, MinMaxBounds.Doubles horizontal, MinMaxBounds.Doubles absolute) {
/*  7 */     this.x = x;
/*  7 */     this.y = y;
/*  7 */     this.z = z;
/*  7 */     this.horizontal = horizontal;
/*  7 */     this.absolute = absolute;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/DistancePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/DistancePredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/DistancePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/DistancePredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/DistancePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/DistancePredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Doubles x() {
/*  7 */     return this.x;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Doubles y() {
/*  7 */     return this.y;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Doubles z() {
/*  7 */     return this.z;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Doubles horizontal() {
/*  7 */     return this.horizontal;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Doubles absolute() {
/*  7 */     return this.absolute;
/*    */   }
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("x", MinMaxBounds.Doubles.ANY).forGetter(DistancePredicate::x), (App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("y", MinMaxBounds.Doubles.ANY).forGetter(DistancePredicate::y), (App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("z", MinMaxBounds.Doubles.ANY).forGetter(DistancePredicate::z), (App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("horizontal", MinMaxBounds.Doubles.ANY).forGetter(DistancePredicate::horizontal), (App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("absolute", MinMaxBounds.Doubles.ANY).forGetter(DistancePredicate::absolute)).apply((Applicative)i, DistancePredicate::new));
/*    */   }
/*    */   
/*    */   public static DistancePredicate horizontal(MinMaxBounds.Doubles horizontal) {
/* 23 */     return new DistancePredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, horizontal, MinMaxBounds.Doubles.ANY);
/*    */   }
/*    */   
/*    */   public static DistancePredicate vertical(MinMaxBounds.Doubles y) {
/* 27 */     return new DistancePredicate(MinMaxBounds.Doubles.ANY, y, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY);
/*    */   }
/*    */   
/*    */   public static DistancePredicate absolute(MinMaxBounds.Doubles absolute) {
/* 31 */     return new DistancePredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, absolute);
/*    */   }
/*    */   
/*    */   public boolean matches(double x0, double y0, double z0, double x1, double y1, double z1) {
/* 35 */     float xd = (float)(x0 - x1);
/* 36 */     float yd = (float)(y0 - y1);
/* 37 */     float zd = (float)(z0 - z1);
/* 38 */     if (!this.x.matches(Mth.abs(xd)) || !this.y.matches(Mth.abs(yd)) || !this.z.matches(Mth.abs(zd)))
/* 39 */       return false; 
/* 41 */     if (!this.horizontal.matchesSqr((xd * xd + zd * zd)))
/* 42 */       return false; 
/* 44 */     if (!this.absolute.matchesSqr((xd * xd + yd * yd + zd * zd)))
/* 45 */       return false; 
/* 47 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\DistancePredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */