/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.advancements.predicates.CollectionPredicate;
/*    */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*    */ import net.minecraft.advancements.predicates.SingleComponentItemPredicate;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.component.FireworkExplosion;
/*    */ import net.minecraft.world.item.component.Fireworks;
/*    */ 
/*    */ public final class FireworksPredicate extends Record implements SingleComponentItemPredicate<Fireworks> {
/*    */   private final Optional<CollectionPredicate<FireworkExplosion, FireworkExplosionPredicate.FireworkPredicate>> explosions;
/*    */   
/*    */   private final MinMaxBounds.Ints flightDuration;
/*    */   
/*    */   public static final Codec<FireworksPredicate> CODEC;
/*    */   
/*    */   public FireworksPredicate(Optional<CollectionPredicate<FireworkExplosion, FireworkExplosionPredicate.FireworkPredicate>> explosions, MinMaxBounds.Ints flightDuration) {
/* 15 */     this.explosions = explosions;
/* 15 */     this.flightDuration = flightDuration;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/FireworksPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/FireworksPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/FireworksPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/FireworksPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/FireworksPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/FireworksPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<CollectionPredicate<FireworkExplosion, FireworkExplosionPredicate.FireworkPredicate>> explosions() {
/* 15 */     return this.explosions;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Ints flightDuration() {
/* 15 */     return this.flightDuration;
/*    */   }
/*    */   
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)CollectionPredicate.codec(FireworkExplosionPredicate.FireworkPredicate.CODEC).optionalFieldOf("explosions").forGetter(FireworksPredicate::explosions), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("flight_duration", MinMaxBounds.Ints.ANY).forGetter(FireworksPredicate::flightDuration)).apply((Applicative)i, FireworksPredicate::new));
/*    */   }
/*    */   
/*    */   public DataComponentType<Fireworks> componentType() {
/* 26 */     return DataComponents.FIREWORKS;
/*    */   }
/*    */   
/*    */   public boolean matches(Fireworks value) {
/* 31 */     if (this.explosions.isPresent() && !((CollectionPredicate)this.explosions.get()).test(value.explosions()))
/* 32 */       return false; 
/* 35 */     if (!this.flightDuration.matches(value.flightDuration()))
/* 36 */       return false; 
/* 39 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\predicates\FireworksPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */