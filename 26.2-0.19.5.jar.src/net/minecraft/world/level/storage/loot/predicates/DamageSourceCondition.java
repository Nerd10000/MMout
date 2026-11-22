/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.predicates.DamageSourcePredicate;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class DamageSourceCondition extends Record implements LootItemCondition {
/*    */   private final Optional<DamageSourcePredicate> predicate;
/*    */   
/*    */   public static final MapCodec<DamageSourceCondition> MAP_CODEC;
/*    */   
/*    */   public DamageSourceCondition(Optional<DamageSourcePredicate> predicate) {
/* 15 */     this.predicate = predicate;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/DamageSourceCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/DamageSourceCondition;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/DamageSourceCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/DamageSourceCondition;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/DamageSourceCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/DamageSourceCondition;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<DamageSourcePredicate> predicate() {
/* 15 */     return this.predicate;
/*    */   }
/*    */   
/*    */   static {
/* 18 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DamageSourcePredicate.CODEC.optionalFieldOf("predicate").forGetter(DamageSourceCondition::predicate)).apply((Applicative)i, DamageSourceCondition::new));
/*    */   }
/*    */   
/*    */   public MapCodec<DamageSourceCondition> codec() {
/* 24 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 29 */     return Set.of(LootContextParams.ORIGIN, LootContextParams.DAMAGE_SOURCE);
/*    */   }
/*    */   
/*    */   public boolean test(LootContext context) {
/* 34 */     DamageSource damageSource = (DamageSource)context.getOptionalParameter(LootContextParams.DAMAGE_SOURCE);
/* 35 */     Vec3 pos = (Vec3)context.getOptionalParameter(LootContextParams.ORIGIN);
/* 36 */     if (pos == null || damageSource == null)
/* 37 */       return false; 
/* 40 */     return (this.predicate.isEmpty() || ((DamageSourcePredicate)this.predicate.get()).matches(context.getLevel(), pos, damageSource));
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder hasDamageSource(DamageSourcePredicate.Builder builder) {
/* 44 */     return () -> new DamageSourceCondition(Optional.of(builder.build()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\DamageSourceCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */