/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class ReplaceBlock extends Record implements EnchantmentEntityEffect {
/*    */   private final Vec3i offset;
/*    */   
/*    */   private final Optional<BlockPredicate> predicate;
/*    */   
/*    */   private final BlockStateProvider blockState;
/*    */   
/*    */   private final Optional<Holder<GameEvent>> triggerGameEvent;
/*    */   
/*    */   public static final MapCodec<ReplaceBlock> CODEC;
/*    */   
/*    */   public ReplaceBlock(Vec3i offset, Optional<BlockPredicate> predicate, BlockStateProvider blockState, Optional<Holder<GameEvent>> triggerGameEvent) {
/* 18 */     this.offset = offset;
/* 18 */     this.predicate = predicate;
/* 18 */     this.blockState = blockState;
/* 18 */     this.triggerGameEvent = triggerGameEvent;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/ReplaceBlock;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ReplaceBlock;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/ReplaceBlock;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ReplaceBlock;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/ReplaceBlock;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/ReplaceBlock;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vec3i offset() {
/* 18 */     return this.offset;
/*    */   }
/*    */   
/*    */   public Optional<BlockPredicate> predicate() {
/* 18 */     return this.predicate;
/*    */   }
/*    */   
/*    */   public BlockStateProvider blockState() {
/* 18 */     return this.blockState;
/*    */   }
/*    */   
/*    */   public Optional<Holder<GameEvent>> triggerGameEvent() {
/* 18 */     return this.triggerGameEvent;
/*    */   }
/*    */   
/*    */   static {
/* 24 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Vec3i.CODEC.optionalFieldOf("offset", Vec3i.ZERO).forGetter(ReplaceBlock::offset), (App)BlockPredicate.CODEC.optionalFieldOf("predicate").forGetter(ReplaceBlock::predicate), (App)BlockStateProvider.CODEC.fieldOf("block_state").forGetter(ReplaceBlock::blockState), (App)GameEvent.CODEC.optionalFieldOf("trigger_game_event").forGetter(ReplaceBlock::triggerGameEvent)).apply((Applicative)i, ReplaceBlock::new));
/*    */   }
/*    */   
/*    */   public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
/* 33 */     BlockPos pos = BlockPos.containing((Position)position).offset(this.offset);
/* 34 */     if (((Boolean)this.predicate.<Boolean>map(p -> Boolean.valueOf(p.test(serverLevel, pos))).orElse(Boolean.valueOf(true))).booleanValue() && 
/* 35 */       serverLevel.setBlockAndUpdate(pos, this.blockState.getState((WorldGenLevel)serverLevel, entity.getRandom(), pos)))
/* 36 */       this.triggerGameEvent.ifPresent(event -> serverLevel.gameEvent(entity, event, pos)); 
/*    */   }
/*    */   
/*    */   public MapCodec<ReplaceBlock> codec() {
/* 43 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\effects\ReplaceBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */