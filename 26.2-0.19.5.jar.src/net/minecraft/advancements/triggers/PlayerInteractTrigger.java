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
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContextSource;
/*    */ 
/*    */ public class PlayerInteractTrigger extends SimpleCriterionTrigger<PlayerInteractTrigger.TriggerInstance> {
/*    */   public Codec<TriggerInstance> codec() {
/* 20 */     return TriggerInstance.CODEC;
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer player, ItemStack itemStack, Entity interactedWith) {
/* 24 */     LootContext context = EntityPredicate.createContext(player, interactedWith);
/* 25 */     trigger(player, t -> t.matches(itemStack, context));
/*    */   }
/*    */   
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */     private final Optional<ContextAwarePredicate> player;
/*    */     
/*    */     private final Optional<ItemPredicate> item;
/*    */     
/*    */     private final Optional<ContextAwarePredicate> entity;
/*    */     
/*    */     public static final Codec<TriggerInstance> CODEC;
/*    */     
/*    */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item, Optional<ContextAwarePredicate> entity) {
/* 28 */       this.player = player;
/* 28 */       this.item = item;
/* 28 */       this.entity = entity;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/PlayerInteractTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/PlayerInteractTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/PlayerInteractTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/PlayerInteractTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/PlayerInteractTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/triggers/PlayerInteractTrigger$TriggerInstance;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Optional<ContextAwarePredicate> player() {
/* 28 */       return this.player;
/*    */     }
/*    */     
/*    */     public Optional<ItemPredicate> item() {
/* 28 */       return this.item;
/*    */     }
/*    */     
/*    */     public Optional<ContextAwarePredicate> entity() {
/* 28 */       return this.entity;
/*    */     }
/*    */     
/*    */     static {
/* 33 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)ItemPredicate.CODEC.optionalFieldOf("item").forGetter(TriggerInstance::item), (App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("entity").forGetter(TriggerInstance::entity)).apply((Applicative)i, TriggerInstance::new));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> itemUsedOnEntity(Optional<ContextAwarePredicate> player, ItemPredicate.Builder item, Optional<ContextAwarePredicate> entity) {
/* 40 */       return CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.createCriterion(new TriggerInstance(player, Optional.of(item.build()), entity));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> equipmentSheared(Optional<ContextAwarePredicate> player, ItemPredicate.Builder item, Optional<ContextAwarePredicate> entity) {
/* 44 */       return CriteriaTriggers.PLAYER_SHEARED_EQUIPMENT.createCriterion(new TriggerInstance(player, Optional.of(item.build()), entity));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> equipmentSheared(ItemPredicate.Builder item, Optional<ContextAwarePredicate> entity) {
/* 48 */       return CriteriaTriggers.PLAYER_SHEARED_EQUIPMENT.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(item.build()), entity));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> itemUsedOnEntity(ItemPredicate.Builder item, Optional<ContextAwarePredicate> entity) {
/* 53 */       return itemUsedOnEntity(Optional.empty(), item, entity);
/*    */     }
/*    */     
/*    */     public boolean matches(ItemStack itemStack, LootContext interactedWith) {
/* 57 */       if (this.item.isPresent() && !((ItemPredicate)this.item.get()).test((ItemInstance)itemStack))
/* 58 */         return false; 
/* 61 */       return (this.entity.isEmpty() || ((ContextAwarePredicate)this.entity.get()).matches(interactedWith));
/*    */     }
/*    */     
/*    */     public void validate(ValidationContextSource validator) {
/* 66 */       super.validate(validator);
/* 68 */       Validatable.validate(validator.entityContext(), "entity", this.entity);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\PlayerInteractTrigger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */