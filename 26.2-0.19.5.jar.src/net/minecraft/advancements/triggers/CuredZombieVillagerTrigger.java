/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContextSource;
/*    */ 
/*    */ public class CuredZombieVillagerTrigger extends SimpleCriterionTrigger<CuredZombieVillagerTrigger.TriggerInstance> {
/*    */   public Codec<TriggerInstance> codec() {
/* 19 */     return TriggerInstance.CODEC;
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer player, Zombie zombie, Villager villager) {
/* 23 */     LootContext zombieContext = EntityPredicate.createContext(player, (Entity)zombie);
/* 24 */     LootContext villagerContext = EntityPredicate.createContext(player, (Entity)villager);
/* 26 */     trigger(player, t -> t.matches(zombieContext, villagerContext));
/*    */   }
/*    */   
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */     private final Optional<ContextAwarePredicate> player;
/*    */     
/*    */     private final Optional<ContextAwarePredicate> zombie;
/*    */     
/*    */     private final Optional<ContextAwarePredicate> villager;
/*    */     
/*    */     public static final Codec<TriggerInstance> CODEC;
/*    */     
/*    */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> zombie, Optional<ContextAwarePredicate> villager) {
/* 29 */       this.player = player;
/* 29 */       this.zombie = zombie;
/* 29 */       this.villager = villager;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/CuredZombieVillagerTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/CuredZombieVillagerTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/CuredZombieVillagerTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/CuredZombieVillagerTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/CuredZombieVillagerTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/triggers/CuredZombieVillagerTrigger$TriggerInstance;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Optional<ContextAwarePredicate> player() {
/* 29 */       return this.player;
/*    */     }
/*    */     
/*    */     public Optional<ContextAwarePredicate> zombie() {
/* 29 */       return this.zombie;
/*    */     }
/*    */     
/*    */     public Optional<ContextAwarePredicate> villager() {
/* 29 */       return this.villager;
/*    */     }
/*    */     
/*    */     static {
/* 34 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("zombie").forGetter(TriggerInstance::zombie), (App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("villager").forGetter(TriggerInstance::villager)).apply((Applicative)i, TriggerInstance::new));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> curedZombieVillager() {
/* 41 */       return CriteriaTriggers.CURED_ZOMBIE_VILLAGER.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
/*    */     }
/*    */     
/*    */     public boolean matches(LootContext zombie, LootContext villager) {
/* 45 */       if (this.zombie.isPresent() && !((ContextAwarePredicate)this.zombie.get()).matches(zombie))
/* 46 */         return false; 
/* 48 */       if (this.villager.isPresent() && !((ContextAwarePredicate)this.villager.get()).matches(villager))
/* 49 */         return false; 
/* 51 */       return true;
/*    */     }
/*    */     
/*    */     public void validate(ValidationContextSource validator) {
/* 56 */       super.validate(validator);
/* 58 */       Validatable.validate(validator.entityContext(), "zombie", this.zombie);
/* 59 */       Validatable.validate(validator.entityContext(), "villager", this.villager);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\CuredZombieVillagerTrigger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */