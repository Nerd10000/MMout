/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.LockCode;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.Nameable;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.BeaconMenu;
/*     */ import net.minecraft.world.inventory.ContainerData;
/*     */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BeaconBeamBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class BeaconBlockEntity extends BlockEntity implements MenuProvider, Nameable, BeaconBeamOwner {
/*     */   private static final int MAX_LEVELS = 4;
/*     */   
/*     */   private static final int LEVELS_NEEDED_FOR_SECONDARY = 4;
/*     */   
/*  59 */   public static final List<List<Holder<MobEffect>>> BEACON_EFFECTS = List.of(
/*  60 */       List.of(MobEffects.SPEED, MobEffects.HASTE), 
/*  61 */       List.of(MobEffects.RESISTANCE, MobEffects.JUMP_BOOST), 
/*  62 */       List.of(MobEffects.STRENGTH), 
/*  63 */       List.of(MobEffects.REGENERATION));
/*     */   
/*  65 */   private static final Set<Holder<MobEffect>> VALID_EFFECTS = (Set<Holder<MobEffect>>)BEACON_EFFECTS.stream().flatMap(Collection::stream).collect(Collectors.toSet());
/*     */   
/*     */   public static final int DATA_LEVELS = 0;
/*     */   
/*     */   public static final int DATA_PRIMARY = 1;
/*     */   
/*     */   public static final int DATA_SECONDARY = 2;
/*     */   
/*     */   public static final int NUM_DATA_VALUES = 3;
/*     */   
/*     */   private static final int BLOCKS_CHECK_PER_TICK = 10;
/*     */   
/*  73 */   private static final Component DEFAULT_NAME = (Component)Component.translatable("container.beacon");
/*     */   
/*     */   private static final String TAG_PRIMARY = "primary_effect";
/*     */   
/*     */   private static final String TAG_SECONDARY = "secondary_effect";
/*     */   
/*  78 */   private List<BeaconBeamOwner.Section> beamSections = new ArrayList<>();
/*     */   
/*  79 */   private List<BeaconBeamOwner.Section> checkingBeamSections = new ArrayList<>();
/*     */   
/*     */   private int levels;
/*     */   
/*     */   private int lastCheckY;
/*     */   
/*     */   private Holder<MobEffect> primaryPower;
/*     */   
/*     */   private Holder<MobEffect> secondaryPower;
/*     */   
/*     */   private Component name;
/*     */   
/*  89 */   private LockCode lockKey = LockCode.NO_LOCK;
/*     */   
/*  91 */   private final ContainerData dataAccess = new ContainerData() {
/*     */       public int get(int dataId) {
/*  94 */         switch (dataId) {
/*     */           case 0:
/*     */           
/*     */           case 1:
/*     */           
/*     */           case 2:
/*     */           
/*     */         } 
/*  94 */         return 
/*     */           
/*  98 */           0;
/*     */       }
/*     */       
/*     */       public void set(int dataId, int value) {
/* 104 */         switch (dataId) {
/*     */           case 0:
/* 105 */             BeaconBlockEntity.this.levels = value;
/*     */             break;
/*     */           case 1:
/* 107 */             if (!BeaconBlockEntity.this.level.isClientSide() && !BeaconBlockEntity.this.beamSections.isEmpty())
/* 108 */               BeaconBlockEntity.playSound(BeaconBlockEntity.this.level, BeaconBlockEntity.this.worldPosition, SoundEvents.BEACON_POWER_SELECT); 
/* 110 */             BeaconBlockEntity.this.primaryPower = BeaconBlockEntity.filterEffect(BeaconMenu.decodeEffect(value));
/*     */             break;
/*     */           case 2:
/* 112 */             BeaconBlockEntity.this.secondaryPower = BeaconBlockEntity.filterEffect(BeaconMenu.decodeEffect(value));
/*     */             break;
/*     */         } 
/*     */       }
/*     */       
/*     */       public int getCount() {
/* 118 */         return 3;
/*     */       }
/*     */     };
/*     */   
/*     */   private static Holder<MobEffect> filterEffect(Holder<MobEffect> effect) {
/* 123 */     return VALID_EFFECTS.contains(effect) ? effect : null;
/*     */   }
/*     */   
/*     */   public BeaconBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 127 */     super(BlockEntityTypes.BEACON, worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public static void tick(Level level, BlockPos pos, BlockState selfState, BeaconBlockEntity entity) {
/*     */     BlockPos checkPos;
/* 131 */     int x = pos.getX();
/* 132 */     int y = pos.getY();
/* 133 */     int z = pos.getZ();
/* 136 */     if (entity.lastCheckY < y) {
/* 137 */       checkPos = pos;
/* 138 */       entity.checkingBeamSections = Lists.newArrayList();
/* 139 */       entity.lastCheckY = checkPos.getY() - 1;
/*     */     } else {
/* 141 */       checkPos = new BlockPos(x, entity.lastCheckY + 1, z);
/*     */     } 
/* 144 */     BeaconBeamOwner.Section lastBeamSection = entity.checkingBeamSections.isEmpty() ? null : entity.checkingBeamSections.get(entity.checkingBeamSections.size() - 1);
/* 146 */     int lastSetBlock = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
/* 149 */     for (int i = 0; i < 10 && 
/* 150 */       checkPos.getY() <= lastSetBlock; i++) {
/* 153 */       BlockState state = level.getBlockState(checkPos);
/* 154 */       Block block = state.getBlock();
/* 155 */       if (block instanceof BeaconBeamBlock) {
/* 155 */         BeaconBeamBlock beaconBeamBlock = (BeaconBeamBlock)block;
/* 156 */         int color = beaconBeamBlock.getColor().getTextureDiffuseColor();
/* 158 */         if (entity.checkingBeamSections.size() <= 1) {
/* 159 */           lastBeamSection = new BeaconBeamOwner.Section(color);
/* 160 */           entity.checkingBeamSections.add(lastBeamSection);
/* 161 */         } else if (lastBeamSection != null) {
/* 163 */           if (color == lastBeamSection.getColor()) {
/* 164 */             lastBeamSection.increaseHeight();
/*     */           } else {
/* 166 */             lastBeamSection = new BeaconBeamOwner.Section(ARGB.average(lastBeamSection.getColor(), color));
/* 167 */             entity.checkingBeamSections.add(lastBeamSection);
/*     */           } 
/*     */         } 
/* 170 */       } else if (lastBeamSection != null && (state.getLightDampening() < 15 || state.is(Blocks.BEDROCK))) {
/* 171 */         lastBeamSection.increaseHeight();
/*     */       } else {
/* 173 */         entity.checkingBeamSections.clear();
/* 174 */         entity.lastCheckY = lastSetBlock;
/*     */         break;
/*     */       } 
/* 177 */       checkPos = checkPos.above();
/* 178 */       entity.lastCheckY++;
/*     */     } 
/* 181 */     int previousLevels = entity.levels;
/* 183 */     if (level.getGameTime() % 80L == 0L) {
/* 184 */       if (!entity.beamSections.isEmpty())
/* 185 */         entity.levels = updateBase(level, x, y, z); 
/* 188 */       if (entity.levels > 0 && !entity.beamSections.isEmpty()) {
/* 189 */         applyEffects(level, pos, entity.levels, entity.primaryPower, entity.secondaryPower);
/* 190 */         playSound(level, pos, SoundEvents.BEACON_AMBIENT);
/*     */       } 
/*     */     } 
/* 194 */     if (entity.lastCheckY >= lastSetBlock) {
/* 195 */       entity.lastCheckY = level.getMinY() - 1;
/* 196 */       boolean wasActive = (previousLevels > 0);
/* 198 */       entity.beamSections = entity.checkingBeamSections;
/* 200 */       if (!level.isClientSide()) {
/* 201 */         boolean isActive = (entity.levels > 0);
/* 203 */         if (!wasActive && isActive) {
/* 204 */           playSound(level, pos, SoundEvents.BEACON_ACTIVATE);
/* 206 */           for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, (new AABB(x, y, z, x, (y - 4), z)).inflate(10.0D, 5.0D, 10.0D)))
/* 207 */             CriteriaTriggers.CONSTRUCT_BEACON.trigger(player, entity.levels); 
/* 209 */         } else if (wasActive && !isActive) {
/* 210 */           playSound(level, pos, SoundEvents.BEACON_DEACTIVATE);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int updateBase(Level level, int x, int y, int z) {
/* 217 */     int levels = 0;
/* 218 */     for (int step = 1; step <= 4; ) {
/* 219 */       int ly = y - step;
/* 220 */       if (ly < level.getMinY())
/*     */         break; 
/* 224 */       boolean isOk = true;
/* 225 */       for (int lx = x - step; lx <= x + step && isOk; lx++) {
/* 226 */         for (int lz = z - step; lz <= z + step; lz++) {
/* 227 */           if (!level.getBlockState(new BlockPos(lx, ly, lz)).is(BlockTags.BEACON_BASE_BLOCKS)) {
/* 228 */             isOk = false;
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 234 */       if (isOk) {
/* 235 */         levels = step;
/*     */         step++;
/*     */       } 
/*     */     } 
/* 241 */     return levels;
/*     */   }
/*     */   
/*     */   public void setRemoved() {
/* 246 */     playSound(this.level, this.worldPosition, SoundEvents.BEACON_DEACTIVATE);
/* 247 */     super.setRemoved();
/*     */   }
/*     */   
/*     */   public static boolean validateEffects(Holder<MobEffect> primary, Holder<MobEffect> secondary, int levels) {
/* 252 */     if (secondary != null && levels < 4)
/* 253 */       return false; 
/* 257 */     int primaryLevel = getRequiredLevelsFor(primary);
/* 258 */     int secondaryLevel = getRequiredLevelsFor(secondary);
/* 261 */     if (primaryLevel > levels || secondaryLevel > levels)
/* 262 */       return false; 
/* 266 */     if (primaryLevel >= 4)
/* 267 */       return false; 
/* 271 */     if (secondaryLevel != 0 && secondaryLevel < 4 && !primary.equals(secondary))
/* 272 */       return false; 
/* 275 */     return true;
/*     */   }
/*     */   
/*     */   private static int getRequiredLevelsFor(Holder<MobEffect> effect) {
/* 279 */     if (effect == null)
/* 280 */       return 0; 
/* 282 */     for (int i = 0; i < BEACON_EFFECTS.size(); i++) {
/* 283 */       List<Holder<MobEffect>> effectsForLevel = BEACON_EFFECTS.get(i);
/* 284 */       if (effectsForLevel.contains(effect))
/* 285 */         return i + 1; 
/*     */     } 
/* 288 */     return Integer.MAX_VALUE;
/*     */   }
/*     */   
/*     */   private static void applyEffects(Level level, BlockPos worldPosition, int levels, Holder<MobEffect> primaryPower, Holder<MobEffect> secondaryPower) {
/* 292 */     if (level.isClientSide() || primaryPower == null)
/*     */       return; 
/* 296 */     double range = (levels * 10 + 10);
/* 297 */     int baseAmp = 0;
/* 298 */     if (levels >= 4 && Objects.equals(primaryPower, secondaryPower))
/* 299 */       baseAmp = 1; 
/* 301 */     int durationTicks = (9 + levels * 2) * 20;
/* 303 */     AABB bb = (new AABB(worldPosition)).inflate(range).expandTowards(0.0D, level.getHeight(), 0.0D);
/* 304 */     List<Player> players = level.getEntitiesOfClass(Player.class, bb);
/* 305 */     for (Player player : players)
/* 306 */       player.addEffect(new MobEffectInstance(primaryPower, durationTicks, baseAmp, true, true)); 
/* 309 */     if (levels >= 4 && !Objects.equals(primaryPower, secondaryPower) && secondaryPower != null)
/* 310 */       for (Player player : players)
/* 311 */         player.addEffect(new MobEffectInstance(secondaryPower, durationTicks, 0, true, true));  
/*     */   }
/*     */   
/*     */   public static void playSound(Level level, BlockPos worldPosition, SoundEvent event) {
/* 317 */     level.playSound(null, worldPosition, event, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public List<BeaconBeamOwner.Section> getBeamSections() {
/* 322 */     return (this.levels == 0) ? (List<BeaconBeamOwner.Section>)ImmutableList.of() : this.beamSections;
/*     */   }
/*     */   
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 327 */     return ClientboundBlockEntityDataPacket.create(this);
/*     */   }
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
/* 332 */     return saveCustomOnly(registries);
/*     */   }
/*     */   
/*     */   private static void storeEffect(ValueOutput output, String field, Holder<MobEffect> effect) {
/* 336 */     if (effect != null)
/* 337 */       effect.unwrapKey().ifPresent(key -> output.putString(field, key.identifier().toString())); 
/*     */   }
/*     */   
/*     */   private static Holder<MobEffect> loadEffect(ValueInput input, String field) {
/* 343 */     Objects.requireNonNull(VALID_EFFECTS);
/* 343 */     return input.read(field, BuiltInRegistries.MOB_EFFECT.holderByNameCodec()).filter(VALID_EFFECTS::contains)
/* 344 */       .orElse(null);
/*     */   }
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 349 */     super.loadAdditional(input);
/* 351 */     this.primaryPower = loadEffect(input, "primary_effect");
/* 352 */     this.secondaryPower = loadEffect(input, "secondary_effect");
/* 354 */     this.name = parseCustomNameSafe(input, "CustomName");
/* 356 */     this.lockKey = LockCode.fromTag(input);
/*     */   }
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 361 */     super.saveAdditional(output);
/* 363 */     storeEffect(output, "primary_effect", this.primaryPower);
/* 364 */     storeEffect(output, "secondary_effect", this.secondaryPower);
/* 365 */     output.putInt("Levels", this.levels);
/* 367 */     output.storeNullable("CustomName", ComponentSerialization.CODEC, this.name);
/* 369 */     this.lockKey.addToTag(output);
/*     */   }
/*     */   
/*     */   public void setCustomName(Component name) {
/* 373 */     this.name = name;
/*     */   }
/*     */   
/*     */   public Component getCustomName() {
/* 378 */     return this.name;
/*     */   }
/*     */   
/*     */   public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
/* 383 */     if (this.lockKey.canUnlock(player))
/* 384 */       return (AbstractContainerMenu)new BeaconMenu(containerId, (Container)inventory, this.dataAccess, ContainerLevelAccess.create(this.level, getBlockPos())); 
/* 386 */     BaseContainerBlockEntity.sendChestLockedNotifications(Vec3.atCenterOf((Vec3i)getBlockPos()), player, getDisplayName());
/* 388 */     return null;
/*     */   }
/*     */   
/*     */   public Component getDisplayName() {
/* 393 */     return getName();
/*     */   }
/*     */   
/*     */   public Component getName() {
/* 398 */     if (this.name != null)
/* 399 */       return this.name; 
/* 401 */     return DEFAULT_NAME;
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 406 */     super.applyImplicitComponents(components);
/* 407 */     this.name = (Component)components.get(DataComponents.CUSTOM_NAME);
/* 408 */     this.lockKey = (LockCode)components.getOrDefault(DataComponents.LOCK, LockCode.NO_LOCK);
/*     */   }
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {
/* 413 */     super.collectImplicitComponents(components);
/* 414 */     components.set(DataComponents.CUSTOM_NAME, this.name);
/* 415 */     if (!this.lockKey.equals(LockCode.NO_LOCK))
/* 416 */       components.set(DataComponents.LOCK, this.lockKey); 
/*     */   }
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput output) {
/* 422 */     output.discard("CustomName");
/* 423 */     output.discard("lock");
/*     */   }
/*     */   
/*     */   public void setLevel(Level level) {
/* 428 */     super.setLevel(level);
/* 429 */     this.lastCheckY = level.getMinY() - 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BeaconBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */