/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*    */ import net.minecraft.advancements.predicates.StatePropertiesPredicate;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class EnterBlockTrigger extends SimpleCriterionTrigger<EnterBlockTrigger.TriggerInstance> {
/*    */   public Codec<TriggerInstance> codec() {
/* 20 */     return TriggerInstance.CODEC;
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer player, BlockState state) {
/* 24 */     trigger(player, t -> t.matches(state));
/*    */   }
/*    */   
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */     private final Optional<ContextAwarePredicate> player;
/*    */     
/*    */     private final Optional<Holder<Block>> block;
/*    */     
/*    */     private final Optional<StatePropertiesPredicate> state;
/*    */     
/*    */     public static final Codec<TriggerInstance> CODEC;
/*    */     
/*    */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<Holder<Block>> block, Optional<StatePropertiesPredicate> state) {
/* 27 */       this.player = player;
/* 27 */       this.block = block;
/* 27 */       this.state = state;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/EnterBlockTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/EnterBlockTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/EnterBlockTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/EnterBlockTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/EnterBlockTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/triggers/EnterBlockTrigger$TriggerInstance;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Optional<ContextAwarePredicate> player() {
/* 27 */       return this.player;
/*    */     }
/*    */     
/*    */     public Optional<Holder<Block>> block() {
/* 27 */       return this.block;
/*    */     }
/*    */     
/*    */     public Optional<StatePropertiesPredicate> state() {
/* 27 */       return this.state;
/*    */     }
/*    */     
/*    */     static {
/* 36 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)BuiltInRegistries.BLOCK.holderByNameCodec().optionalFieldOf("block").forGetter(TriggerInstance::block), (App)StatePropertiesPredicate.CODEC.optionalFieldOf("state").forGetter(TriggerInstance::state)).apply((Applicative)i, TriggerInstance::new)).validate(TriggerInstance::validate);
/*    */     }
/*    */     
/*    */     private static DataResult<TriggerInstance> validate(TriggerInstance trigger) {
/* 39 */       return trigger.block.<DataResult<TriggerInstance>>flatMap(block -> trigger.state.flatMap(()).map(()))
/*    */         
/* 42 */         .orElseGet(() -> DataResult.success(trigger));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> entersBlock(Block block) {
/* 46 */       return CriteriaTriggers.ENTER_BLOCK.createCriterion(new TriggerInstance(Optional.empty(), (Optional)Optional.of(block.builtInRegistryHolder()), Optional.empty()));
/*    */     }
/*    */     
/*    */     public boolean matches(BlockState state) {
/* 50 */       if (this.block.isPresent() && !state.is(this.block.get()))
/* 51 */         return false; 
/* 53 */       if (this.state.isPresent() && !((StatePropertiesPredicate)this.state.get()).matches(state))
/* 54 */         return false; 
/* 56 */       return true;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\EnterBlockTrigger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */