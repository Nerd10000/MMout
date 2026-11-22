/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*    */ import net.minecraft.advancements.predicates.ItemPredicate;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public class ShotCrossbowTrigger extends SimpleCriterionTrigger<ShotCrossbowTrigger.TriggerInstance> {
/*    */   public Codec<TriggerInstance> codec() {
/* 19 */     return TriggerInstance.CODEC;
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer player, ItemStack itemStack) {
/* 23 */     trigger(player, t -> t.matches(itemStack));
/*    */   }
/*    */   
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */     private final Optional<ContextAwarePredicate> player;
/*    */     
/*    */     private final Optional<ItemPredicate> item;
/*    */     
/*    */     public static final Codec<TriggerInstance> CODEC;
/*    */     
/*    */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item) {
/* 26 */       this.player = player;
/* 26 */       this.item = item;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/ShotCrossbowTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #26	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/ShotCrossbowTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/ShotCrossbowTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #26	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/ShotCrossbowTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/ShotCrossbowTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #26	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/triggers/ShotCrossbowTrigger$TriggerInstance;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Optional<ContextAwarePredicate> player() {
/* 26 */       return this.player;
/*    */     }
/*    */     
/*    */     public Optional<ItemPredicate> item() {
/* 26 */       return this.item;
/*    */     }
/*    */     
/*    */     static {
/* 30 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)ItemPredicate.CODEC.optionalFieldOf("item").forGetter(TriggerInstance::item)).apply((Applicative)i, TriggerInstance::new));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> shotCrossbow(Optional<ItemPredicate> item) {
/* 36 */       return CriteriaTriggers.SHOT_CROSSBOW.createCriterion(new TriggerInstance(Optional.empty(), item));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> shotCrossbow(HolderGetter<Item> items, ItemLike itemlike) {
/* 40 */       return CriteriaTriggers.SHOT_CROSSBOW.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(ItemPredicate.Builder.item().of(items, new ItemLike[] { itemlike }).build())));
/*    */     }
/*    */     
/*    */     public boolean matches(ItemStack itemStack) {
/* 44 */       return (this.item.isEmpty() || ((ItemPredicate)this.item.get()).test((ItemInstance)itemStack));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\ShotCrossbowTrigger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */