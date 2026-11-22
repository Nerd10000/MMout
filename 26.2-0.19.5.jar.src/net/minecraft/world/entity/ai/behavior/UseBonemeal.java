/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.item.BoneMealItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.CropBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class UseBonemeal extends Behavior<Villager> {
/*     */   private static final int BONEMEALING_DURATION = 80;
/*     */   
/*     */   private long nextWorkCycleTime;
/*     */   
/*     */   private long lastBonemealingSession;
/*     */   
/*     */   private int timeWorkedSoFar;
/*     */   
/*  31 */   private Optional<BlockPos> cropPos = Optional.empty();
/*     */   
/*     */   public UseBonemeal() {
/*  34 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
/*     */   }
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Villager body) {
/*  42 */     if (body.tickCount % 10 != 0 || (this.lastBonemealingSession != 0L && this.lastBonemealingSession + 160L > body.tickCount))
/*  43 */       return false; 
/*  46 */     if (body.getInventory().countItem(Items.BONE_MEAL) <= 0)
/*  47 */       return false; 
/*  49 */     this.cropPos = pickNextTarget(level, body);
/*  50 */     return this.cropPos.isPresent();
/*     */   }
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) {
/*  55 */     return (this.timeWorkedSoFar < 80 && this.cropPos.isPresent());
/*     */   }
/*     */   
/*     */   private Optional<BlockPos> pickNextTarget(ServerLevel level, Villager body) {
/*  59 */     BlockPos.MutableBlockPos mutPos = new BlockPos.MutableBlockPos();
/*  60 */     RandomSource random = level.getRandom();
/*  61 */     Optional<BlockPos> result = Optional.empty();
/*  62 */     int count = 0;
/*  63 */     for (int x = -1; x <= 1; x++) {
/*  64 */       for (int y = -1; y <= 1; y++) {
/*  65 */         for (int z = -1; z <= 1; z++) {
/*  66 */           mutPos.setWithOffset((Vec3i)body.blockPosition(), x, y, z);
/*  67 */           if (validPos((BlockPos)mutPos, level) && 
/*  68 */             random.nextInt(++count) == 0)
/*  69 */             result = Optional.of(mutPos.immutable()); 
/*     */         } 
/*     */       } 
/*     */     } 
/*  76 */     return result;
/*     */   }
/*     */   
/*     */   private boolean validPos(BlockPos blockPos, ServerLevel level) {
/*  80 */     BlockState state = level.getBlockState(blockPos);
/*  81 */     Block block = state.getBlock();
/*  82 */     if (block instanceof CropBlock) {
/*  82 */       CropBlock cropBlock = (CropBlock)block;
/*  82 */       if (!cropBlock.isMaxAge(state));
/*     */     } 
/*  82 */     return false;
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, Villager body, long timestamp) {
/*  87 */     setCurrentCropAsTarget(body);
/*  89 */     body.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.BONE_MEAL));
/*  91 */     this.nextWorkCycleTime = timestamp;
/*  92 */     this.timeWorkedSoFar = 0;
/*     */   }
/*     */   
/*     */   private void setCurrentCropAsTarget(Villager body) {
/*  96 */     this.cropPos.ifPresent(pos -> {
/*     */           BlockPosTracker cropPosWrapper = new BlockPosTracker(pos);
/*     */           body.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, cropPosWrapper);
/*     */           body.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(cropPosWrapper, 0.5F, 1));
/*     */         });
/*     */   }
/*     */   
/*     */   protected void stop(ServerLevel level, Villager body, long timestamp) {
/* 105 */     body.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/* 106 */     this.lastBonemealingSession = body.tickCount;
/*     */   }
/*     */   
/*     */   protected void tick(ServerLevel level, Villager body, long timestamp) {
/* 111 */     BlockPos targetPos = this.cropPos.get();
/* 112 */     if (timestamp < this.nextWorkCycleTime || !targetPos.closerToCenterThan((Position)body.position(), 1.0D))
/*     */       return; 
/* 116 */     ItemStack bonemealStack = ItemStack.EMPTY;
/* 117 */     SimpleContainer inventory = body.getInventory();
/* 118 */     int containerSize = inventory.getContainerSize();
/* 119 */     for (int i = 0; i < containerSize; i++) {
/* 120 */       ItemStack item = inventory.getItem(i);
/* 121 */       if (item.is(Items.BONE_MEAL)) {
/* 122 */         bonemealStack = item;
/*     */         break;
/*     */       } 
/*     */     } 
/* 126 */     if (!bonemealStack.isEmpty() && BoneMealItem.growCrop(bonemealStack, (Level)level, targetPos)) {
/* 127 */       level.levelEvent(1505, targetPos, 15);
/* 129 */       this.cropPos = pickNextTarget(level, body);
/* 130 */       setCurrentCropAsTarget(body);
/* 131 */       this.nextWorkCycleTime = timestamp + 40L;
/*     */     } 
/* 134 */     this.timeWorkedSoFar++;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\UseBonemeal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */