/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ 
/*    */ public final class DamagePredicate extends Record implements DataComponentPredicate {
/*    */   private final MinMaxBounds.Ints durability;
/*    */   
/*    */   private final MinMaxBounds.Ints damage;
/*    */   
/*    */   public static final Codec<DamagePredicate> CODEC;
/*    */   
/*    */   public DamagePredicate(MinMaxBounds.Ints durability, MinMaxBounds.Ints damage) {
/*  9 */     this.durability = durability;
/*  9 */     this.damage = damage;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/DamagePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/DamagePredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/DamagePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/DamagePredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/DamagePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/DamagePredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Ints durability() {
/*  9 */     return this.durability;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Ints damage() {
/*  9 */     return this.damage;
/*    */   }
/*    */   
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)MinMaxBounds.Ints.CODEC.optionalFieldOf("durability", MinMaxBounds.Ints.ANY).forGetter(DamagePredicate::durability), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("damage", MinMaxBounds.Ints.ANY).forGetter(DamagePredicate::damage)).apply((Applicative)i, DamagePredicate::new));
/*    */   }
/*    */   
/*    */   public boolean matches(DataComponentGetter components) {
/* 17 */     Integer damage = (Integer)components.get(DataComponents.DAMAGE);
/* 18 */     if (damage == null)
/* 19 */       return false; 
/* 22 */     int maxDamage = ((Integer)components.getOrDefault(DataComponents.MAX_DAMAGE, Integer.valueOf(0))).intValue();
/* 24 */     if (!this.durability.matches(maxDamage - damage.intValue()))
/* 25 */       return false; 
/* 27 */     if (!this.damage.matches(damage.intValue()))
/* 28 */       return false; 
/* 30 */     return true;
/*    */   }
/*    */   
/*    */   public static DamagePredicate durability(MinMaxBounds.Ints range) {
/* 34 */     return new DamagePredicate(range, MinMaxBounds.Ints.ANY);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\predicates\DamagePredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */