/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.food.FoodData;
/*    */ 
/*    */ public final class FoodPredicate extends Record {
/*    */   private final MinMaxBounds.Ints level;
/*    */   
/*    */   private final MinMaxBounds.Doubles saturation;
/*    */   
/*    */   public FoodPredicate(MinMaxBounds.Ints level, MinMaxBounds.Doubles saturation) {
/*  7 */     this.level = level;
/*  7 */     this.saturation = saturation;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/FoodPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/FoodPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/FoodPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/FoodPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/FoodPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/FoodPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Ints level() {
/*  7 */     return this.level;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Doubles saturation() {
/*  7 */     return this.saturation;
/*    */   }
/*    */   
/* 11 */   public static final FoodPredicate ANY = new FoodPredicate(MinMaxBounds.Ints.ANY, MinMaxBounds.Doubles.ANY);
/*    */   
/*    */   public static final Codec<FoodPredicate> CODEC;
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)MinMaxBounds.Ints.CODEC.optionalFieldOf("level", MinMaxBounds.Ints.ANY).forGetter(FoodPredicate::level), (App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("saturation", MinMaxBounds.Doubles.ANY).forGetter(FoodPredicate::saturation)).apply((Applicative)i, FoodPredicate::new));
/*    */   }
/*    */   
/*    */   public boolean matches(FoodData food) {
/* 19 */     if (!this.level.matches(food.getFoodLevel()))
/* 20 */       return false; 
/* 23 */     if (!this.saturation.matches(food.getSaturationLevel()))
/* 24 */       return false; 
/* 27 */     return true;
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 31 */     private MinMaxBounds.Ints level = MinMaxBounds.Ints.ANY;
/*    */     
/* 32 */     private MinMaxBounds.Doubles saturation = MinMaxBounds.Doubles.ANY;
/*    */     
/*    */     public Builder withLevel(MinMaxBounds.Ints level) {
/* 35 */       this.level = level;
/* 36 */       return this;
/*    */     }
/*    */     
/*    */     public Builder withSaturation(MinMaxBounds.Doubles saturation) {
/* 40 */       this.saturation = saturation;
/* 41 */       return this;
/*    */     }
/*    */     
/*    */     public static Builder food() {
/* 45 */       return new Builder();
/*    */     }
/*    */     
/*    */     public FoodPredicate build() {
/* 49 */       return new FoodPredicate(this.level, this.saturation);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\FoodPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */