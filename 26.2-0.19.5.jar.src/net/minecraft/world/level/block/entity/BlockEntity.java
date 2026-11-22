/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.HashSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.TypedInstance;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponentPatch;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.component.PatchedDataComponentMap;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.debug.DebugValueSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class BlockEntity implements DebugValueSource, TypedInstance<BlockEntityType<?>> {
/*  43 */   private static final Codec<BlockEntityType<?>> TYPE_CODEC = BuiltInRegistries.BLOCK_ENTITY_TYPE.byNameCodec();
/*     */   
/*  45 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final BlockEntityType<?> type;
/*     */   
/*     */   protected Level level;
/*     */   
/*     */   protected final BlockPos worldPosition;
/*     */   
/*     */   protected boolean remove;
/*     */   
/*     */   private BlockState blockState;
/*     */   
/*  54 */   private DataComponentMap components = DataComponentMap.EMPTY;
/*     */   
/*     */   public BlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
/*  57 */     this.type = type;
/*  58 */     this.worldPosition = worldPosition.immutable();
/*  59 */     validateBlockState(blockState);
/*  60 */     this.blockState = blockState;
/*     */   }
/*     */   
/*     */   private void validateBlockState(BlockState blockState) {
/*  64 */     if (!isValidBlockState(blockState))
/*  65 */       throw new IllegalStateException("Invalid block entity " + getNameForReporting() + " state at " + String.valueOf(this.worldPosition) + ", got " + String.valueOf(blockState)); 
/*     */   }
/*     */   
/*     */   public boolean isValidBlockState(BlockState blockState) {
/*  70 */     return this.type.isValid(blockState);
/*     */   }
/*     */   
/*     */   public static BlockPos getPosFromTag(ChunkPos base, CompoundTag entityTag) {
/*  74 */     int x = entityTag.getIntOr("x", 0);
/*  75 */     int y = entityTag.getIntOr("y", 0);
/*  76 */     int z = entityTag.getIntOr("z", 0);
/*  78 */     int sectionX = SectionPos.blockToSectionCoord(x);
/*  79 */     int sectionZ = SectionPos.blockToSectionCoord(z);
/*  81 */     if (sectionX != base.x() || sectionZ != base.z()) {
/*  82 */       LOGGER.warn("Block entity {} found in a wrong chunk, expected position from chunk {}", entityTag, base);
/*  90 */       x = base.getBlockX(SectionPos.sectionRelative(x));
/*  91 */       z = base.getBlockZ(SectionPos.sectionRelative(z));
/*     */     } 
/*  94 */     return new BlockPos(x, y, z);
/*     */   }
/*     */   
/*     */   public Level getLevel() {
/*  98 */     return this.level;
/*     */   }
/*     */   
/*     */   public void setLevel(Level level) {
/* 102 */     this.level = level;
/*     */   }
/*     */   
/*     */   public boolean hasLevel() {
/* 106 */     return (this.level != null);
/*     */   }
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {}
/*     */   
/*     */   public final void loadWithComponents(ValueInput input) {
/* 113 */     loadAdditional(input);
/* 115 */     this.components = input.read("components", DataComponentMap.CODEC).orElse(DataComponentMap.EMPTY);
/*     */   }
/*     */   
/*     */   public final void loadCustomOnly(ValueInput input) {
/* 119 */     loadAdditional(input);
/*     */   }
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {}
/*     */   
/*     */   public final CompoundTag saveWithFullMetadata(HolderLookup.Provider registries) {
/* 128 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(problemPath(), LOGGER);
/*     */     try {
/* 129 */       TagValueOutput output = TagValueOutput.createWithContext((ProblemReporter)reporter, registries);
/* 130 */       saveWithFullMetadata((ValueOutput)output);
/* 131 */       CompoundTag compoundTag = output.buildResult();
/* 132 */       reporter.close();
/*     */       return compoundTag;
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         reporter.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void saveWithFullMetadata(ValueOutput output) {
/* 136 */     saveWithoutMetadata(output);
/* 137 */     saveMetadata(output);
/*     */   }
/*     */   
/*     */   public void saveWithId(ValueOutput output) {
/* 143 */     saveWithoutMetadata(output);
/* 144 */     saveId(output);
/*     */   }
/*     */   
/*     */   public final CompoundTag saveWithoutMetadata(HolderLookup.Provider registries) {
/* 149 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(problemPath(), LOGGER);
/*     */     try {
/* 150 */       TagValueOutput output = TagValueOutput.createWithContext((ProblemReporter)reporter, registries);
/* 151 */       saveWithoutMetadata((ValueOutput)output);
/* 152 */       CompoundTag compoundTag = output.buildResult();
/* 153 */       reporter.close();
/*     */       return compoundTag;
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         reporter.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void saveWithoutMetadata(ValueOutput output) {
/* 157 */     saveAdditional(output);
/* 158 */     output.store("components", DataComponentMap.CODEC, this.components);
/*     */   }
/*     */   
/*     */   public final CompoundTag saveCustomOnly(HolderLookup.Provider registries) {
/* 162 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(problemPath(), LOGGER);
/*     */     try {
/* 163 */       TagValueOutput output = TagValueOutput.createWithContext((ProblemReporter)reporter, registries);
/* 164 */       saveCustomOnly((ValueOutput)output);
/* 165 */       CompoundTag compoundTag = output.buildResult();
/* 166 */       reporter.close();
/*     */       return compoundTag;
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         reporter.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void saveCustomOnly(ValueOutput output) {
/* 170 */     saveAdditional(output);
/*     */   }
/*     */   
/*     */   private void saveId(ValueOutput output) {
/* 174 */     addEntityType(output, getType());
/*     */   }
/*     */   
/*     */   public static void addEntityType(ValueOutput output, BlockEntityType<?> type) {
/* 178 */     output.store("id", TYPE_CODEC, type);
/*     */   }
/*     */   
/*     */   private void saveMetadata(ValueOutput output) {
/* 182 */     saveId(output);
/* 183 */     output.putInt("x", this.worldPosition.getX());
/* 184 */     output.putInt("y", this.worldPosition.getY());
/* 185 */     output.putInt("z", this.worldPosition.getZ());
/*     */   }
/*     */   
/*     */   public static BlockEntity loadStatic(BlockPos pos, BlockState state, CompoundTag tag, HolderLookup.Provider registries) {
/*     */     BlockEntity entity;
/* 189 */     BlockEntityType<?> type = tag.read("id", TYPE_CODEC).orElse(null);
/* 190 */     if (type == null) {
/* 191 */       LOGGER.error("Skipping block entity with invalid type: {}", tag.get("id"));
/* 192 */       return null;
/*     */     } 
/*     */     try {
/* 197 */       entity = (BlockEntity)type.create(pos, state);
/* 198 */     } catch (Throwable t) {
/* 199 */       LOGGER.error("Failed to create block entity {} for block {} at position {} ", new Object[] { type, pos, state, t });
/* 200 */       return null;
/*     */     } 
/*     */     try {
/* 203 */       ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(entity.problemPath(), LOGGER);
/*     */       try {
/* 204 */         entity.loadWithComponents(TagValueInput.create((ProblemReporter)reporter, registries, tag));
/* 205 */         BlockEntity blockEntity = entity;
/* 206 */         reporter.close();
/*     */         return blockEntity;
/*     */       } catch (Throwable throwable) {
/*     */         try {
/*     */           reporter.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */         throw throwable;
/*     */       } 
/* 206 */     } catch (Throwable t) {
/* 207 */       LOGGER.error("Failed to load data for block entity {} for block {} at position {}", new Object[] { type, pos, state, t });
/* 208 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setChanged() {
/* 213 */     if (this.level != null)
/* 214 */       setChanged(this.level, this.worldPosition, this.blockState); 
/*     */   }
/*     */   
/*     */   protected static void setChanged(Level level, BlockPos worldPosition, BlockState blockState) {
/* 219 */     level.blockEntityChanged(worldPosition);
/* 221 */     if (!blockState.isAir())
/* 222 */       level.updateNeighbourForOutputSignal(worldPosition, blockState.getBlock()); 
/*     */   }
/*     */   
/*     */   public BlockPos getBlockPos() {
/* 227 */     return this.worldPosition;
/*     */   }
/*     */   
/*     */   public BlockState getBlockState() {
/* 231 */     return this.blockState;
/*     */   }
/*     */   
/*     */   public Packet<ClientGamePacketListener> getUpdatePacket() {
/* 235 */     return null;
/*     */   }
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
/* 239 */     return new CompoundTag();
/*     */   }
/*     */   
/*     */   public boolean isRemoved() {
/* 243 */     return this.remove;
/*     */   }
/*     */   
/*     */   public void setRemoved() {
/* 247 */     this.remove = true;
/*     */   }
/*     */   
/*     */   public void clearRemoved() {
/* 251 */     this.remove = false;
/*     */   }
/*     */   
/*     */   public void preRemoveSideEffects(BlockPos pos, BlockState state) {
/* 258 */     BlockEntity blockEntity = this;
/* 258 */     if (blockEntity instanceof Container) {
/* 258 */       Container container = (Container)blockEntity;
/* 258 */       if (this.level != null)
/* 259 */         Containers.dropContents(this.level, pos, container); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean triggerEvent(int b0, int b1) {
/* 264 */     return false;
/*     */   }
/*     */   
/*     */   public void fillCrashReportCategory(CrashReportCategory category) {
/* 268 */     category.setDetail("Name", this::getNameForReporting);
/* 269 */     Objects.requireNonNull(getBlockState());
/* 269 */     category.setDetail("Cached block", getBlockState()::toString);
/* 271 */     if (this.level == null) {
/* 272 */       category.setDetail("Block location", () -> String.valueOf(this.worldPosition) + " (world missing)");
/*     */     } else {
/* 274 */       Objects.requireNonNull(this.level.getBlockState(this.worldPosition));
/* 274 */       category.setDetail("Actual block", this.level.getBlockState(this.worldPosition)::toString);
/* 275 */       CrashReportCategory.populateBlockLocationDetails(category, (LevelHeightAccessor)this.level, this.worldPosition);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getNameForReporting() {
/* 280 */     return typeHolder().getRegisteredName() + " // " + typeHolder().getRegisteredName();
/*     */   }
/*     */   
/*     */   public BlockEntityType<?> getType() {
/* 284 */     return this.type;
/*     */   }
/*     */   
/*     */   public Holder<BlockEntityType<?>> typeHolder() {
/* 289 */     return (Holder<BlockEntityType<?>>)this.type.builtInRegistryHolder();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void setBlockState(BlockState blockState) {
/* 295 */     validateBlockState(blockState);
/* 296 */     this.blockState = blockState;
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {}
/*     */   
/*     */   public final void applyComponentsFromItemStack(ItemStack stack) {
/* 303 */     applyComponents(stack.getPrototype(), stack.getComponentsPatch());
/*     */   }
/*     */   
/*     */   public final void applyComponents(DataComponentMap prototype, DataComponentPatch patch) {
/* 307 */     final Set<DataComponentType<?>> implicitComponents = new HashSet<>();
/* 308 */     implicitComponents.add(DataComponents.BLOCK_ENTITY_DATA);
/* 309 */     implicitComponents.add(DataComponents.BLOCK_STATE);
/* 311 */     final PatchedDataComponentMap fullView = PatchedDataComponentMap.fromPatch(prototype, patch);
/* 312 */     applyImplicitComponents(new DataComponentGetter(this) {
/*     */           public <T> T get(DataComponentType<? extends T> type) {
/* 315 */             implicitComponents.add(type);
/* 316 */             return (T)fullView.get(type);
/*     */           }
/*     */           
/*     */           public <T> T getOrDefault(DataComponentType<? extends T> type, T defaultValue) {
/* 321 */             implicitComponents.add(type);
/* 322 */             return (T)fullView.getOrDefault(type, defaultValue);
/*     */           }
/*     */         });
/* 325 */     Objects.requireNonNull(implicitComponents);
/* 325 */     DataComponentPatch newPatch = patch.forget(implicitComponents::contains);
/* 332 */     this.components = newPatch.split().added();
/*     */   }
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {}
/*     */   
/*     */   @Deprecated
/*     */   public void removeComponentsFromTag(ValueOutput output) {}
/*     */   
/*     */   public final DataComponentMap collectComponents() {
/* 345 */     DataComponentMap.Builder result = DataComponentMap.builder();
/* 346 */     result.addAll(this.components);
/* 347 */     collectImplicitComponents(result);
/* 348 */     return result.build();
/*     */   }
/*     */   
/*     */   public DataComponentMap components() {
/* 352 */     return this.components;
/*     */   }
/*     */   
/*     */   public void setComponents(DataComponentMap components) {
/* 356 */     this.components = components;
/*     */   }
/*     */   
/*     */   public static Component parseCustomNameSafe(ValueInput input, String name) {
/* 360 */     return input.read(name, ComponentSerialization.CODEC).orElse(null);
/*     */   }
/*     */   
/*     */   private static final class BlockEntityPathElement extends Record implements ProblemReporter.PathElement {
/*     */     private final BlockEntity blockEntity;
/*     */     
/*     */     private BlockEntityPathElement(BlockEntity blockEntity) {
/* 363 */       this.blockEntity = blockEntity;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/BlockEntity$BlockEntityPathElement;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #363	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BlockEntity$BlockEntityPathElement;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/BlockEntity$BlockEntityPathElement;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #363	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BlockEntity$BlockEntityPathElement;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/BlockEntity$BlockEntityPathElement;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #363	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/entity/BlockEntity$BlockEntityPathElement;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public BlockEntity blockEntity() {
/* 363 */       return this.blockEntity;
/*     */     }
/*     */     
/*     */     public String get() {
/* 366 */       return this.blockEntity.getNameForReporting() + "@" + this.blockEntity.getNameForReporting();
/*     */     }
/*     */   }
/*     */   
/*     */   public ProblemReporter.PathElement problemPath() {
/* 371 */     return new BlockEntityPathElement(this);
/*     */   }
/*     */   
/*     */   public void registerDebugValues(ServerLevel level, DebugValueSource.Registration registration) {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */