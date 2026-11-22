/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class ReplaceDisk extends Record implements EnchantmentEntityEffect {
/*    */   private final LevelBasedValue radius;
/*    */   
/*    */   private final LevelBasedValue height;
/*    */   
/*    */   private final Vec3i offset;
/*    */   
/*    */   private final Optional<BlockPredicate> predicate;
/*    */   
/*    */   private final BlockStateProvider blockState;
/*    */   
/*    */   private final Optional<Holder<GameEvent>> triggerGameEvent;
/*    */   
/*    */   public static final MapCodec<ReplaceDisk> CODEC;
/*    */   
/*    */   public ReplaceDisk(LevelBasedValue radius, LevelBasedValue height, Vec3i offset, Optional<BlockPredicate> predicate, BlockStateProvider blockState, Optional<Holder<GameEvent>> triggerGameEvent) {
/* 21 */     this.radius = radius;
/* 21 */     this.height = height;
/* 21 */     this.offset = offset;
/* 21 */     this.predicate = predicate;
/* 21 */     this.blockState = blockState;
/* 21 */     this.triggerGameEvent = triggerGameEvent;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/ReplaceDisk;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ReplaceDisk;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/ReplaceDisk;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ReplaceDisk;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/ReplaceDisk;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/ReplaceDisk;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public LevelBasedValue radius() {
/* 21 */     return this.radius;
/*    */   }
/*    */   
/*    */   public LevelBasedValue height() {
/* 21 */     return this.height;
/*    */   }
/*    */   
/*    */   public Vec3i offset() {
/* 21 */     return this.offset;
/*    */   }
/*    */   
/*    */   public Optional<BlockPredicate> predicate() {
/* 21 */     return this.predicate;
/*    */   }
/*    */   
/*    */   public BlockStateProvider blockState() {
/* 21 */     return this.blockState;
/*    */   }
/*    */   
/*    */   public Optional<Holder<GameEvent>> triggerGameEvent() {
/* 21 */     return this.triggerGameEvent;
/*    */   }
/*    */   
/*    */   static {
/* 29 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LevelBasedValue.CODEC.fieldOf("radius").forGetter(ReplaceDisk::radius), (App)LevelBasedValue.CODEC.fieldOf("height").forGetter(ReplaceDisk::height), (App)Vec3i.CODEC.optionalFieldOf("offset", Vec3i.ZERO).forGetter(ReplaceDisk::offset), (App)BlockPredicate.CODEC.optionalFieldOf("predicate").forGetter(ReplaceDisk::predicate), (App)BlockStateProvider.CODEC.fieldOf("block_state").forGetter(ReplaceDisk::blockState), (App)GameEvent.CODEC.optionalFieldOf("trigger_game_event").forGetter(ReplaceDisk::triggerGameEvent)).apply((Applicative)i, ReplaceDisk::new));
/*    */   }
/*    */   
/*    */   public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
/* 40 */     BlockPos centerBlock = BlockPos.containing((Position)position).offset(this.offset);
/* 41 */     RandomSource random = entity.getRandom();
/* 42 */     int dist = (int)this.radius.calculate(enchantmentLevel);
/* 43 */     int height = (int)this.height.calculate(enchantmentLevel);
/* 44 */     for (BlockPos pos : BlockPos.betweenClosed(centerBlock.offset(-dist, 0, -dist), centerBlock.offset(dist, Math.min(height - 1, 0), dist))) {
/* 45 */       if (pos.distToCenterSqr(position.x(), pos.getY() + 0.5D, position.z()) < Mth.square(dist) && ((Boolean)this.predicate.<Boolean>map(p -> Boolean.valueOf(p.test(serverLevel, pos))).orElse(Boolean.valueOf(true))).booleanValue() && 
/* 46 */         serverLevel.setBlockAndUpdate(pos, this.blockState.getState((WorldGenLevel)serverLevel, random, pos)))
/* 47 */         this.triggerGameEvent.ifPresent(event -> serverLevel.gameEvent(entity, event, pos)); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public MapCodec<ReplaceDisk> codec() {
/* 55 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\effects\ReplaceDisk.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */