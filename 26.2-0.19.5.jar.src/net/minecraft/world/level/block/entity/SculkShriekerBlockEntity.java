/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.GameEventTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.SpawnUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.warden.Warden;
/*     */ import net.minecraft.world.entity.monster.warden.WardenSpawnTracker;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.SculkShriekerBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.BlockPositionSource;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.GameEventListener;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class SculkShriekerBlockEntity extends BlockEntity implements GameEventListener.Provider<VibrationSystem.Listener>, VibrationSystem {
/*     */   private static final int WARNING_SOUND_RADIUS = 10;
/*     */   
/*     */   private static final int WARDEN_SPAWN_ATTEMPTS = 20;
/*     */   
/*     */   private static final int WARDEN_SPAWN_RANGE_XZ = 5;
/*     */   
/*     */   private static final int WARDEN_SPAWN_RANGE_Y = 6;
/*     */   
/*     */   private static final int DARKNESS_RADIUS = 40;
/*     */   
/*     */   private static final int SHRIEKING_TICKS = 90;
/*     */   
/*     */   private static final Int2ObjectMap<SoundEvent> SOUND_BY_LEVEL;
/*     */   
/*     */   private static final int DEFAULT_WARNING_LEVEL = 0;
/*     */   
/*     */   static {
/*  51 */     SOUND_BY_LEVEL = (Int2ObjectMap<SoundEvent>)Util.make(new Int2ObjectOpenHashMap(), map -> {
/*     */           map.put(1, SoundEvents.WARDEN_NEARBY_CLOSE);
/*     */           map.put(2, SoundEvents.WARDEN_NEARBY_CLOSER);
/*     */           map.put(3, SoundEvents.WARDEN_NEARBY_CLOSEST);
/*     */           map.put(4, SoundEvents.WARDEN_LISTENING_ANGRY);
/*     */         });
/*     */   }
/*     */   
/*  59 */   private int warningLevel = 0;
/*     */   
/*     */   private final VibrationSystem.User vibrationUser;
/*     */   
/*     */   private VibrationSystem.Data vibrationData;
/*     */   
/*     */   private final VibrationSystem.Listener vibrationListener;
/*     */   
/*     */   public SculkShriekerBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  65 */     super(BlockEntityTypes.SCULK_SHRIEKER, worldPosition, blockState);
/*  66 */     this.vibrationUser = new VibrationUser();
/*  67 */     this.vibrationData = new VibrationSystem.Data();
/*  68 */     this.vibrationListener = new VibrationSystem.Listener(this);
/*     */   }
/*     */   
/*     */   public VibrationSystem.Data getVibrationData() {
/*  73 */     return this.vibrationData;
/*     */   }
/*     */   
/*     */   public VibrationSystem.User getVibrationUser() {
/*  78 */     return this.vibrationUser;
/*     */   }
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  83 */     super.loadAdditional(input);
/*  85 */     this.warningLevel = input.getIntOr("warning_level", 0);
/*  87 */     this.vibrationData = input.read("listener", VibrationSystem.Data.CODEC).orElseGet(net.minecraft.world.level.gameevent.vibrations.VibrationSystem.Data::new);
/*     */   }
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  92 */     super.saveAdditional(output);
/*  94 */     output.putInt("warning_level", this.warningLevel);
/*  96 */     output.store("listener", VibrationSystem.Data.CODEC, this.vibrationData);
/*     */   }
/*     */   
/*     */   public static ServerPlayer tryGetPlayer(Entity sourceEntity) {
/* 101 */     if (sourceEntity instanceof ServerPlayer) {
/* 101 */       ServerPlayer player = (ServerPlayer)sourceEntity;
/* 102 */       return player;
/*     */     } 
/* 106 */     if (sourceEntity != null) {
/* 106 */       LivingEntity livingEntity = sourceEntity.getControllingPassenger();
/* 106 */       if (livingEntity instanceof ServerPlayer) {
/* 106 */         ServerPlayer player = (ServerPlayer)livingEntity;
/* 107 */         return player;
/*     */       } 
/*     */     } 
/* 110 */     if (sourceEntity instanceof Projectile) {
/* 110 */       Projectile projectile = (Projectile)sourceEntity;
/* 110 */       Entity entity = projectile.getOwner();
/* 110 */       if (entity instanceof ServerPlayer) {
/* 110 */         ServerPlayer player = (ServerPlayer)entity;
/* 111 */         return player;
/*     */       } 
/*     */     } 
/* 114 */     if (sourceEntity instanceof ItemEntity) {
/* 114 */       ItemEntity item = (ItemEntity)sourceEntity;
/* 114 */       Entity entity = item.getOwner();
/* 114 */       if (entity instanceof ServerPlayer) {
/* 114 */         ServerPlayer player = (ServerPlayer)entity;
/* 115 */         return player;
/*     */       } 
/*     */     } 
/* 118 */     return null;
/*     */   }
/*     */   
/*     */   public void tryShriek(ServerLevel level, ServerPlayer player) {
/* 122 */     if (player == null)
/*     */       return; 
/* 126 */     BlockState state = getBlockState();
/* 127 */     if (((Boolean)state.getValue((Property)SculkShriekerBlock.SHRIEKING)).booleanValue())
/*     */       return; 
/* 131 */     this.warningLevel = 0;
/* 132 */     if (canRespond(level) && !tryToWarn(level, player))
/*     */       return; 
/* 136 */     shriek(level, (Entity)player);
/*     */   }
/*     */   
/*     */   private boolean tryToWarn(ServerLevel level, ServerPlayer player) {
/* 140 */     OptionalInt maybeWarningLevel = WardenSpawnTracker.tryWarn(level, getBlockPos(), player);
/* 141 */     maybeWarningLevel.ifPresent(warningLevel -> this.warningLevel = warningLevel);
/* 142 */     return maybeWarningLevel.isPresent();
/*     */   }
/*     */   
/*     */   private void shriek(ServerLevel level, Entity sourceEntity) {
/* 146 */     BlockPos pos = getBlockPos();
/* 147 */     BlockState state = getBlockState();
/* 148 */     level.setBlock(pos, (BlockState)state.setValue((Property)SculkShriekerBlock.SHRIEKING, Boolean.valueOf(true)), 2);
/* 149 */     level.scheduleTick(pos, state.getBlock(), 90);
/* 150 */     level.levelEvent(3007, pos, 0);
/* 151 */     level.gameEvent((Holder)GameEvent.SHRIEK, pos, GameEvent.Context.of(sourceEntity));
/*     */   }
/*     */   
/*     */   private boolean canRespond(ServerLevel level) {
/* 155 */     return (((Boolean)getBlockState().getValue((Property)SculkShriekerBlock.CAN_SUMMON)).booleanValue() && level
/* 156 */       .getDifficulty() != Difficulty.PEACEFUL && ((Boolean)level
/* 157 */       .getGameRules().get(GameRules.SPAWN_WARDENS)).booleanValue());
/*     */   }
/*     */   
/*     */   public void preRemoveSideEffects(BlockPos pos, BlockState state) {
/* 162 */     if (((Boolean)state.getValue((Property)SculkShriekerBlock.SHRIEKING)).booleanValue()) {
/* 162 */       Level level = this.level;
/* 162 */       if (level instanceof ServerLevel) {
/* 162 */         ServerLevel serverLevel = (ServerLevel)level;
/* 164 */         tryRespond(serverLevel);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tryRespond(ServerLevel level) {
/* 169 */     if (canRespond(level) && this.warningLevel > 0) {
/* 170 */       if (!trySummonWarden(level))
/* 171 */         playWardenReplySound((Level)level); 
/* 174 */       Warden.applyDarknessAround(level, Vec3.atCenterOf((Vec3i)getBlockPos()), null, 40);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void playWardenReplySound(Level level) {
/* 179 */     SoundEvent sound = (SoundEvent)SOUND_BY_LEVEL.get(this.warningLevel);
/* 180 */     if (sound != null) {
/* 181 */       BlockPos pos = getBlockPos();
/* 182 */       RandomSource random = level.getRandom();
/* 183 */       int x = pos.getX() + Mth.randomBetweenInclusive(random, -10, 10);
/* 184 */       int y = pos.getY() + Mth.randomBetweenInclusive(random, -10, 10);
/* 185 */       int z = pos.getZ() + Mth.randomBetweenInclusive(random, -10, 10);
/* 187 */       level.playSound(null, x, y, z, sound, SoundSource.HOSTILE, 5.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean trySummonWarden(ServerLevel level) {
/* 192 */     if (this.warningLevel < 4)
/* 193 */       return false; 
/* 196 */     return SpawnUtil.trySpawnMob(EntityTypes.WARDEN, EntitySpawnReason.TRIGGERED, level, getBlockPos(), 20, 5, 6, SpawnUtil.Strategy.ON_TOP_OF_COLLIDER, false).isPresent();
/*     */   }
/*     */   
/*     */   public VibrationSystem.Listener getListener() {
/* 201 */     return this.vibrationListener;
/*     */   }
/*     */   
/*     */   private class VibrationUser implements VibrationSystem.User {
/*     */     private static final int LISTENER_RADIUS = 8;
/*     */     
/* 209 */     private final PositionSource positionSource = (PositionSource)new BlockPositionSource(SculkShriekerBlockEntity.this.worldPosition);
/*     */     
/*     */     public int getListenerRadius() {
/* 214 */       return 8;
/*     */     }
/*     */     
/*     */     public PositionSource getPositionSource() {
/* 219 */       return this.positionSource;
/*     */     }
/*     */     
/*     */     public TagKey<GameEvent> getListenableEvents() {
/* 224 */       return GameEventTags.SHRIEKER_CAN_LISTEN;
/*     */     }
/*     */     
/*     */     public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, GameEvent.Context context) {
/* 229 */       return (!((Boolean)SculkShriekerBlockEntity.this.getBlockState().getValue((Property)SculkShriekerBlock.SHRIEKING)).booleanValue() && SculkShriekerBlockEntity.tryGetPlayer(context.sourceEntity()) != null);
/*     */     }
/*     */     
/*     */     public void onReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, Entity sourceEntity, Entity projectileOwner, float receivingDistance) {
/* 234 */       SculkShriekerBlockEntity.this.tryShriek(level, SculkShriekerBlockEntity.tryGetPlayer((projectileOwner != null) ? projectileOwner : sourceEntity));
/*     */     }
/*     */     
/*     */     public void onDataChanged() {
/* 239 */       SculkShriekerBlockEntity.this.setChanged();
/*     */     }
/*     */     
/*     */     public boolean requiresAdjacentChunksToBeTicking() {
/* 244 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\SculkShriekerBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */