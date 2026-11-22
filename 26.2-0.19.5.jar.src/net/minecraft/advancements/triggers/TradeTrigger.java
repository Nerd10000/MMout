/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*    */ import net.minecraft.advancements.predicates.ItemPredicate;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.npc.villager.AbstractVillager;
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContextSource;
/*    */ 
/*    */ public class TradeTrigger extends SimpleCriterionTrigger<TradeTrigger.TriggerInstance> {
/*    */   public Codec<TriggerInstance> codec() {
/* 20 */     return TriggerInstance.CODEC;
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer player, AbstractVillager villager, ItemStack itemStack) {
/* 24 */     LootContext villagerContext = EntityPredicate.createContext(player, (Entity)villager);
/* 25 */     trigger(player, t -> t.matches(villagerContext, itemStack));
/*    */   }
/*    */   
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */     private final Optional<ContextAwarePredicate> player;
/*    */     
/*    */     private final Optional<ContextAwarePredicate> villager;
/*    */     
/*    */     private final Optional<ItemPredicate> item;
/*    */     
/*    */     public static final Codec<TriggerInstance> CODEC;
/*    */     
/*    */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> villager, Optional<ItemPredicate> item) {
/* 28 */       this.player = player;
/* 28 */       this.villager = villager;
/* 28 */       this.item = item;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/TradeTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/TradeTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/TradeTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/TradeTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/TradeTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/triggers/TradeTrigger$TriggerInstance;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Optional<ContextAwarePredicate> player() {
/* 28 */       return this.player;
/*    */     }
/*    */     
/*    */     public Optional<ContextAwarePredicate> villager() {
/* 28 */       return this.villager;
/*    */     }
/*    */     
/*    */     public Optional<ItemPredicate> item() {
/* 28 */       return this.item;
/*    */     }
/*    */     
/*    */     static {
/* 33 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("villager").forGetter(TriggerInstance::villager), (App)ItemPredicate.CODEC.optionalFieldOf("item").forGetter(TriggerInstance::item)).apply((Applicative)i, TriggerInstance::new));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> tradedWithVillager() {
/* 40 */       return CriteriaTriggers.TRADE.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> tradedWithVillager(EntityPredicate.Builder player) {
/* 44 */       return CriteriaTriggers.TRADE.createCriterion(new TriggerInstance(Optional.of(EntityPredicate.wrap(player)), Optional.empty(), Optional.empty()));
/*    */     }
/*    */     
/*    */     public boolean matches(LootContext villager, ItemStack itemStack) {
/* 48 */       if (this.villager.isPresent() && !((ContextAwarePredicate)this.villager.get()).matches(villager))
/* 49 */         return false; 
/* 51 */       if (this.item.isPresent() && !((ItemPredicate)this.item.get()).test((ItemInstance)itemStack))
/* 52 */         return false; 
/* 54 */       return true;
/*    */     }
/*    */     
/*    */     public void validate(ValidationContextSource validator) {
/* 59 */       super.validate(validator);
/* 61 */       Validatable.validate(validator.entityContext(), "villager", this.villager);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\TradeTrigger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */