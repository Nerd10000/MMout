/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.predicates.BlockPredicate;
/*    */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*    */ import net.minecraft.advancements.predicates.ItemPredicate;
/*    */ import net.minecraft.advancements.predicates.LocationPredicate;
/*    */ import net.minecraft.advancements.predicates.entity.EntityEquipmentPredicate;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class PlayerTrigger extends SimpleCriterionTrigger<PlayerTrigger.TriggerInstance> {
/*    */   public Codec<TriggerInstance> codec() {
/* 23 */     return TriggerInstance.CODEC;
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer player) {
/* 27 */     trigger(player, t -> true);
/*    */   }
/*    */   
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */     private final Optional<ContextAwarePredicate> player;
/*    */     
/*    */     public static final Codec<TriggerInstance> CODEC;
/*    */     
/*    */     public TriggerInstance(Optional<ContextAwarePredicate> player) {
/* 30 */       this.player = player;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/PlayerTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/PlayerTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/PlayerTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/PlayerTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/PlayerTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/triggers/PlayerTrigger$TriggerInstance;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Optional<ContextAwarePredicate> player() {
/* 30 */       return this.player;
/*    */     }
/*    */     
/*    */     static {
/* 33 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player)).apply((Applicative)i, TriggerInstance::new));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> located(LocationPredicate.Builder location) {
/* 38 */       return CriteriaTriggers.LOCATION.createCriterion(new TriggerInstance(Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().located(location)))));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> located(EntityPredicate.Builder player) {
/* 42 */       return CriteriaTriggers.LOCATION.createCriterion(new TriggerInstance(Optional.of(EntityPredicate.wrap(player.build()))));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> located(Optional<EntityPredicate> player) {
/* 46 */       return CriteriaTriggers.LOCATION.createCriterion(new TriggerInstance(EntityPredicate.wrap(player)));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> sleptInBed() {
/* 50 */       return CriteriaTriggers.SLEPT_IN_BED.createCriterion(new TriggerInstance(Optional.empty()));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> raidWon() {
/* 54 */       return CriteriaTriggers.RAID_WIN.createCriterion(new TriggerInstance(Optional.empty()));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> avoidVibration() {
/* 58 */       return CriteriaTriggers.AVOID_VIBRATION.createCriterion(new TriggerInstance(Optional.empty()));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> tick() {
/* 62 */       return CriteriaTriggers.TICK.createCriterion(new TriggerInstance(Optional.empty()));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> walkOnBlockWithEquipment(HolderGetter<Block> blocks, HolderGetter<Item> items, Block stepOnBlock, Item requiredEquipment) {
/* 66 */       return located(EntityPredicate.Builder.entity()
/* 67 */           .equipment(
/* 68 */             EntityEquipmentPredicate.Builder.equipment().feet(ItemPredicate.Builder.item().of(items, new ItemLike[] { (ItemLike)requiredEquipment }))).steppingOn(
/* 70 */             LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(blocks, new Block[] { stepOnBlock }))));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\PlayerTrigger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */