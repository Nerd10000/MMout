/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*     */ import net.minecraft.world.item.BlockItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.CropBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ public class HarvestFarmland extends Behavior<Villager> {
/*     */   private static final int HARVEST_DURATION = 200;
/*     */   
/*     */   public static final float SPEED_MODIFIER = 0.5F;
/*     */   
/*     */   private BlockPos aboveFarmlandPos;
/*     */   
/*     */   private long nextOkStartTime;
/*     */   
/*     */   private int timeWorkedSoFar;
/*     */   
/*  37 */   private final List<BlockPos> validFarmlandAroundVillager = Lists.newArrayList();
/*     */   
/*     */   public HarvestFarmland() {
/*  40 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SECONDARY_JOB_SITE, MemoryStatus.VALUE_PRESENT));
/*     */   }
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Villager body) {
/*  49 */     if (!((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue())
/*  50 */       return false; 
/*  53 */     if (!body.getVillagerData().profession().is(VillagerProfession.FARMER))
/*  54 */       return false; 
/*  57 */     BlockPos.MutableBlockPos mutPos = body.blockPosition().mutable();
/*  59 */     this.validFarmlandAroundVillager.clear();
/*  60 */     for (int x = -1; x <= 1; x++) {
/*  61 */       for (int y = -1; y <= 1; y++) {
/*  62 */         for (int z = -1; z <= 1; z++) {
/*  63 */           mutPos.set(body.getX() + x, body.getY() + y, body.getZ() + z);
/*  64 */           if (validPos((BlockPos)mutPos, level))
/*  65 */             this.validFarmlandAroundVillager.add(new BlockPos((Vec3i)mutPos)); 
/*     */         } 
/*     */       } 
/*     */     } 
/*  71 */     this.aboveFarmlandPos = getValidFarmland(level);
/*  72 */     return (this.aboveFarmlandPos != null);
/*     */   }
/*     */   
/*     */   private BlockPos getValidFarmland(ServerLevel level) {
/*  76 */     return this.validFarmlandAroundVillager.isEmpty() ? null : this.validFarmlandAroundVillager.get(level.getRandom().nextInt(this.validFarmlandAroundVillager.size()));
/*     */   }
/*     */   
/*     */   private boolean validPos(BlockPos blockPos, ServerLevel level) {
/*     */     // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: aload_1
/*     */     //   2: invokevirtual getBlockState : (Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   5: astore_3
/*     */     //   6: aload_3
/*     */     //   7: invokevirtual getBlock : ()Lnet/minecraft/world/level/block/Block;
/*     */     //   10: astore #4
/*     */     //   12: aload_2
/*     */     //   13: aload_1
/*     */     //   14: invokevirtual below : ()Lnet/minecraft/core/BlockPos;
/*     */     //   17: invokevirtual getBlockState : (Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   20: invokevirtual getBlock : ()Lnet/minecraft/world/level/block/Block;
/*     */     //   23: astore #5
/*     */     //   25: aload #4
/*     */     //   27: instanceof net/minecraft/world/level/block/CropBlock
/*     */     //   30: ifeq -> 49
/*     */     //   33: aload #4
/*     */     //   35: checkcast net/minecraft/world/level/block/CropBlock
/*     */     //   38: astore #6
/*     */     //   40: aload #6
/*     */     //   42: aload_3
/*     */     //   43: invokevirtual isMaxAge : (Lnet/minecraft/world/level/block/state/BlockState;)Z
/*     */     //   46: ifne -> 64
/*     */     //   49: aload_3
/*     */     //   50: invokevirtual isAir : ()Z
/*     */     //   53: ifeq -> 68
/*     */     //   56: aload #5
/*     */     //   58: instanceof net/minecraft/world/level/block/FarmlandBlock
/*     */     //   61: ifeq -> 68
/*     */     //   64: iconst_1
/*     */     //   65: goto -> 69
/*     */     //   68: iconst_0
/*     */     //   69: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #80	-> 0
/*     */     //   #81	-> 6
/*     */     //   #82	-> 12
/*     */     //   #84	-> 25
/*     */     //   #83	-> 33
/*     */     //   #84	-> 50
/*     */     //   #83	-> 69
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   40	9	6	cropBlock	Lnet/minecraft/world/level/block/CropBlock;
/*     */     //   0	70	0	this	Lnet/minecraft/world/entity/ai/behavior/HarvestFarmland;
/*     */     //   0	70	1	blockPos	Lnet/minecraft/core/BlockPos;
/*     */     //   0	70	2	level	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   6	64	3	state	Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   12	58	4	block	Lnet/minecraft/world/level/block/Block;
/*     */     //   25	45	5	blockBelow	Lnet/minecraft/world/level/block/Block;
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, Villager body, long timestamp) {
/*  89 */     if (timestamp > this.nextOkStartTime && this.aboveFarmlandPos != null) {
/*  90 */       body.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.aboveFarmlandPos));
/*  91 */       body.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(this.aboveFarmlandPos), 0.5F, 1));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void stop(ServerLevel level, Villager body, long timestamp) {
/*  97 */     body.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
/*  98 */     body.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/*  99 */     this.timeWorkedSoFar = 0;
/* 100 */     this.nextOkStartTime = timestamp + 40L;
/*     */   }
/*     */   
/*     */   protected void tick(ServerLevel level, Villager body, long timestamp) {
/* 105 */     if (this.aboveFarmlandPos != null && !this.aboveFarmlandPos.closerToCenterThan((Position)body.position(), 1.0D))
/*     */       return; 
/* 109 */     if (this.aboveFarmlandPos != null && timestamp > this.nextOkStartTime) {
/* 110 */       BlockState blockState = level.getBlockState(this.aboveFarmlandPos);
/* 111 */       Block block = blockState.getBlock();
/* 112 */       Block blockBelow = level.getBlockState(this.aboveFarmlandPos.below()).getBlock();
/* 114 */       if (block instanceof CropBlock) {
/* 114 */         CropBlock cropBlock = (CropBlock)block;
/* 114 */         if (cropBlock.isMaxAge(blockState))
/* 115 */           level.destroyBlock(this.aboveFarmlandPos, true, (Entity)body); 
/*     */       } 
/* 118 */       if (blockState.isAir() && blockBelow instanceof net.minecraft.world.level.block.FarmlandBlock && body.hasFarmSeeds()) {
/* 119 */         SimpleContainer inventory = body.getInventory();
/* 120 */         for (int i = 0; i < inventory.getContainerSize(); i++) {
/* 121 */           ItemStack itemStack = inventory.getItem(i);
/* 122 */           boolean ok = false;
/* 123 */           if (!itemStack.isEmpty() && itemStack.is(ItemTags.VILLAGER_PLANTABLE_SEEDS)) {
/* 124 */             Item item = itemStack.getItem();
/* 124 */             if (item instanceof BlockItem) {
/* 124 */               BlockItem blockItem = (BlockItem)item;
/* 125 */               BlockState place = blockItem.getBlock().defaultBlockState();
/* 126 */               level.setBlockAndUpdate(this.aboveFarmlandPos, place);
/* 127 */               level.gameEvent((Holder)GameEvent.BLOCK_PLACE, this.aboveFarmlandPos, GameEvent.Context.of((Entity)body, place));
/* 128 */               ok = true;
/*     */             } 
/*     */           } 
/* 131 */           if (ok) {
/* 132 */             level.playSound(null, this.aboveFarmlandPos.getX(), this.aboveFarmlandPos.getY(), this.aboveFarmlandPos.getZ(), SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 133 */             itemStack.shrink(1);
/* 134 */             if (itemStack.isEmpty())
/* 135 */               inventory.setItem(i, ItemStack.EMPTY); 
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 142 */       if (block instanceof CropBlock) {
/* 142 */         CropBlock cropBlock = (CropBlock)block;
/* 142 */         if (!cropBlock.isMaxAge(blockState)) {
/* 143 */           this.validFarmlandAroundVillager.remove(this.aboveFarmlandPos);
/* 145 */           this.aboveFarmlandPos = getValidFarmland(level);
/* 146 */           if (this.aboveFarmlandPos != null) {
/* 147 */             this.nextOkStartTime = timestamp + 20L;
/* 148 */             body.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(this.aboveFarmlandPos), 0.5F, 1));
/* 149 */             body.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.aboveFarmlandPos));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 154 */     this.timeWorkedSoFar++;
/*     */   }
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) {
/* 159 */     return (this.timeWorkedSoFar < 200);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\HarvestFarmland.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */