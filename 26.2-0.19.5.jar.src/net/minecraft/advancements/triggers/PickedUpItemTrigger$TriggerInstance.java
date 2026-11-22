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
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContextSource;
/*    */ 
/*    */ public final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   
/*    */   private final Optional<ItemPredicate> item;
/*    */   
/*    */   private final Optional<ContextAwarePredicate> entity;
/*    */   
/*    */   public static final Codec<TriggerInstance> CODEC;
/*    */   
/*    */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item, Optional<ContextAwarePredicate> entity) {
/* 29 */     this.player = player;
/* 29 */     this.item = item;
/* 29 */     this.entity = entity;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/PickedUpItemTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/PickedUpItemTrigger$TriggerInstance;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/PickedUpItemTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/PickedUpItemTrigger$TriggerInstance;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/PickedUpItemTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/triggers/PickedUpItemTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<ContextAwarePredicate> player() {
/* 29 */     return this.player;
/*    */   }
/*    */   
/*    */   public Optional<ItemPredicate> item() {
/* 29 */     return this.item;
/*    */   }
/*    */   
/*    */   public Optional<ContextAwarePredicate> entity() {
/* 29 */     return this.entity;
/*    */   }
/*    */   
/*    */   static {
/* 34 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)ItemPredicate.CODEC.optionalFieldOf("item").forGetter(TriggerInstance::item), (App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("entity").forGetter(TriggerInstance::entity)).apply((Applicative)i, TriggerInstance::new));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> thrownItemPickedUpByEntity(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item, Optional<ContextAwarePredicate> entity) {
/* 41 */     return CriteriaTriggers.THROWN_ITEM_PICKED_UP_BY_ENTITY.createCriterion(new TriggerInstance(player, item, entity));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> thrownItemPickedUpByPlayer(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item, Optional<ContextAwarePredicate> entity) {
/* 45 */     return CriteriaTriggers.THROWN_ITEM_PICKED_UP_BY_PLAYER.createCriterion(new TriggerInstance(player, item, entity));
/*    */   }
/*    */   
/*    */   public boolean matches(ServerPlayer player, ItemStack itemStack, LootContext pickedUpBy) {
/* 49 */     if (this.item.isPresent() && !((ItemPredicate)this.item.get()).test((ItemInstance)itemStack))
/* 50 */       return false; 
/* 53 */     if (this.entity.isPresent() && !((ContextAwarePredicate)this.entity.get()).matches(pickedUpBy))
/* 54 */       return false; 
/* 57 */     return true;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContextSource validator) {
/* 62 */     super.validate(validator);
/* 64 */     Validatable.validate(validator.entityContext(), "entity", this.entity);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\PickedUpItemTrigger$TriggerInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */