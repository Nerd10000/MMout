/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*    */ import net.minecraft.advancements.predicates.ItemPredicate;
/*    */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ItemDurabilityTrigger extends SimpleCriterionTrigger<ItemDurabilityTrigger.TriggerInstance> {
/*    */   public Codec<TriggerInstance> codec() {
/* 17 */     return TriggerInstance.CODEC;
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer player, ItemStack itemStack, int newDurability) {
/* 21 */     trigger(player, t -> t.matches(itemStack, newDurability));
/*    */   }
/*    */   
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */     private final Optional<ContextAwarePredicate> player;
/*    */     
/*    */     private final Optional<ItemPredicate> item;
/*    */     
/*    */     private final MinMaxBounds.Ints durability;
/*    */     
/*    */     private final MinMaxBounds.Ints delta;
/*    */     
/*    */     public static final Codec<TriggerInstance> CODEC;
/*    */     
/*    */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item, MinMaxBounds.Ints durability, MinMaxBounds.Ints delta) {
/* 24 */       this.player = player;
/* 24 */       this.item = item;
/* 24 */       this.durability = durability;
/* 24 */       this.delta = delta;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/ItemDurabilityTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/ItemDurabilityTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/ItemDurabilityTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/ItemDurabilityTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/ItemDurabilityTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/triggers/ItemDurabilityTrigger$TriggerInstance;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Optional<ContextAwarePredicate> player() {
/* 24 */       return this.player;
/*    */     }
/*    */     
/*    */     public Optional<ItemPredicate> item() {
/* 24 */       return this.item;
/*    */     }
/*    */     
/*    */     public MinMaxBounds.Ints durability() {
/* 24 */       return this.durability;
/*    */     }
/*    */     
/*    */     public MinMaxBounds.Ints delta() {
/* 24 */       return this.delta;
/*    */     }
/*    */     
/*    */     static {
/* 30 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)ItemPredicate.CODEC.optionalFieldOf("item").forGetter(TriggerInstance::item), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("durability", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::durability), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("delta", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::delta)).apply((Applicative)i, TriggerInstance::new));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> changedDurability(Optional<ItemPredicate> item, MinMaxBounds.Ints durability) {
/* 38 */       return changedDurability(Optional.empty(), item, durability);
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> changedDurability(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item, MinMaxBounds.Ints durability) {
/* 42 */       return CriteriaTriggers.ITEM_DURABILITY_CHANGED.createCriterion(new TriggerInstance(player, item, durability, MinMaxBounds.Ints.ANY));
/*    */     }
/*    */     
/*    */     public boolean matches(ItemStack itemStack, int newDurability) {
/* 46 */       if (this.item.isPresent() && !((ItemPredicate)this.item.get()).test((ItemInstance)itemStack))
/* 47 */         return false; 
/* 49 */       if (!this.durability.matches(itemStack.getMaxDamage() - newDurability))
/* 50 */         return false; 
/* 52 */       if (!this.delta.matches(itemStack.getDamageValue() - newDurability))
/* 53 */         return false; 
/* 55 */       return true;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\ItemDurabilityTrigger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */