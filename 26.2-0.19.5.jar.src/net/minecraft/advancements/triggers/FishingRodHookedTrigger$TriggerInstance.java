/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Collection;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*    */ import net.minecraft.advancements.predicates.ItemPredicate;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContextSource;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   
/*    */   private final Optional<ItemPredicate> rod;
/*    */   
/*    */   private final Optional<ContextAwarePredicate> entity;
/*    */   
/*    */   private final Optional<ItemPredicate> item;
/*    */   
/*    */   public static final Codec<TriggerInstance> CODEC;
/*    */   
/*    */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> rod, Optional<ContextAwarePredicate> entity, Optional<ItemPredicate> item) {
/* 32 */     this.player = player;
/* 32 */     this.rod = rod;
/* 32 */     this.entity = entity;
/* 32 */     this.item = item;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/FishingRodHookedTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/FishingRodHookedTrigger$TriggerInstance;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/FishingRodHookedTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/FishingRodHookedTrigger$TriggerInstance;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/FishingRodHookedTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/triggers/FishingRodHookedTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<ContextAwarePredicate> player() {
/* 32 */     return this.player;
/*    */   }
/*    */   
/*    */   public Optional<ItemPredicate> rod() {
/* 32 */     return this.rod;
/*    */   }
/*    */   
/*    */   public Optional<ContextAwarePredicate> entity() {
/* 32 */     return this.entity;
/*    */   }
/*    */   
/*    */   public Optional<ItemPredicate> item() {
/* 32 */     return this.item;
/*    */   }
/*    */   
/*    */   static {
/* 38 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)ItemPredicate.CODEC.optionalFieldOf("rod").forGetter(TriggerInstance::rod), (App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("entity").forGetter(TriggerInstance::entity), (App)ItemPredicate.CODEC.optionalFieldOf("item").forGetter(TriggerInstance::item)).apply((Applicative)i, TriggerInstance::new));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> fishedItem(Optional<ItemPredicate> rod, Optional<EntityPredicate> entity, Optional<ItemPredicate> item) {
/* 46 */     return CriteriaTriggers.FISHING_ROD_HOOKED.createCriterion(new TriggerInstance(Optional.empty(), rod, EntityPredicate.wrap(entity), item));
/*    */   }
/*    */   
/*    */   public boolean matches(ItemStack rod, LootContext hookedIn, Collection<ItemStack> items) {
/* 50 */     if (this.rod.isPresent() && !((ItemPredicate)this.rod.get()).test((ItemInstance)rod))
/* 51 */       return false; 
/* 53 */     if (this.entity.isPresent() && !((ContextAwarePredicate)this.entity.get()).matches(hookedIn))
/* 54 */       return false; 
/* 56 */     if (this.item.isPresent()) {
/* 57 */       boolean matched = false;
/* 59 */       Entity hookedInEntity = (Entity)hookedIn.getOptionalParameter(LootContextParams.THIS_ENTITY);
/* 60 */       if (hookedInEntity instanceof ItemEntity) {
/* 60 */         ItemEntity item = (ItemEntity)hookedInEntity;
/* 61 */         if (((ItemPredicate)this.item.get()).test((ItemInstance)item.getItem()))
/* 62 */           matched = true; 
/*    */       } 
/* 65 */       for (ItemStack item : items) {
/* 66 */         if (((ItemPredicate)this.item.get()).test((ItemInstance)item)) {
/* 67 */           matched = true;
/*    */           break;
/*    */         } 
/*    */       } 
/* 71 */       if (!matched)
/* 72 */         return false; 
/*    */     } 
/* 75 */     return true;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContextSource validator) {
/* 80 */     super.validate(validator);
/* 82 */     Validatable.validate(validator.entityContext(), "entity", this.entity);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\FishingRodHookedTrigger$TriggerInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */