/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.gametest.framework.FailedTestTracker;
/*     */ import net.minecraft.gametest.framework.GameTestInfo;
/*     */ import net.minecraft.gametest.framework.GameTestInstance;
/*     */ import net.minecraft.gametest.framework.GameTestRunner;
/*     */ import net.minecraft.gametest.framework.GameTestTicker;
/*     */ import net.minecraft.gametest.framework.RetryOptions;
/*     */ import net.minecraft.gametest.framework.StructureUtils;
/*     */ import net.minecraft.gametest.framework.TestCommand;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.loader.TemplatePathFactory;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TestInstanceBlockEntity extends BlockEntity implements BoundingBoxRenderable, BeaconBeamOwner {
/*  58 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  60 */   private static final Component INVALID_TEST_NAME = (Component)Component.translatable("test_instance_block.invalid_test");
/*     */   
/*  61 */   private static final List<BeaconBeamOwner.Section> BEAM_CLEARED = List.of();
/*     */   
/*  62 */   private static final List<BeaconBeamOwner.Section> BEAM_RUNNING = List.of(new BeaconBeamOwner.Section(ARGB.color(128, 128, 128)));
/*     */   
/*  63 */   private static final List<BeaconBeamOwner.Section> BEAM_SUCCESS = List.of(new BeaconBeamOwner.Section(ARGB.color(0, 255, 0)));
/*     */   
/*  64 */   private static final List<BeaconBeamOwner.Section> BEAM_REQUIRED_FAILED = List.of(new BeaconBeamOwner.Section(ARGB.color(255, 0, 0)));
/*     */   
/*  65 */   private static final List<BeaconBeamOwner.Section> BEAM_OPTIONAL_FAILED = List.of(new BeaconBeamOwner.Section(ARGB.color(255, 128, 0)));
/*     */   
/*     */   public enum Status implements StringRepresentable {
/*  68 */     CLEARED("cleared", 0),
/*  69 */     RUNNING("running", 1),
/*  70 */     FINISHED("finished", 2);
/*     */     
/*     */     private static final IntFunction<Status> ID_MAP;
/*     */     
/*  73 */     public static final Codec<Status> CODEC = (Codec<Status>)StringRepresentable.fromEnum(Status::values);
/*     */     
/*     */     public static final StreamCodec<ByteBuf, Status> STREAM_CODEC;
/*     */     
/*     */     private final String id;
/*     */     
/*     */     private final int index;
/*     */     
/*     */     static {
/*  74 */       STREAM_CODEC = ByteBufCodecs.idMapper(Status::byIndex, s -> s.index);
/*     */     }
/*     */     
/*     */     Status(String id, int index) {
/*  80 */       this.id = id;
/*  81 */       this.index = index;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/*  86 */       return this.id;
/*     */     }
/*     */     
/*     */     public static Status byIndex(int index) {
/*  90 */       return ID_MAP.apply(index);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Data extends Record {
/*     */     private final Optional<ResourceKey<GameTestInstance>> test;
/*     */     
/*     */     private final Vec3i size;
/*     */     
/*     */     private final Rotation rotation;
/*     */     
/*     */     private final boolean ignoreEntities;
/*     */     
/*     */     private final TestInstanceBlockEntity.Status status;
/*     */     
/*     */     private final Optional<Component> errorMessage;
/*     */     
/*     */     public static final Codec<Data> CODEC;
/*     */     
/*     */     public Data(Optional<ResourceKey<GameTestInstance>> test, Vec3i size, Rotation rotation, boolean ignoreEntities, TestInstanceBlockEntity.Status status, Optional<Component> errorMessage) {
/*  94 */       this.test = test;
/*  94 */       this.size = size;
/*  94 */       this.rotation = rotation;
/*  94 */       this.ignoreEntities = ignoreEntities;
/*  94 */       this.status = status;
/*  94 */       this.errorMessage = errorMessage;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #94	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #94	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #94	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Optional<ResourceKey<GameTestInstance>> test() {
/*  94 */       return this.test;
/*     */     }
/*     */     
/*     */     public Vec3i size() {
/*  94 */       return this.size;
/*     */     }
/*     */     
/*     */     public Rotation rotation() {
/*  94 */       return this.rotation;
/*     */     }
/*     */     
/*     */     public boolean ignoreEntities() {
/*  94 */       return this.ignoreEntities;
/*     */     }
/*     */     
/*     */     public TestInstanceBlockEntity.Status status() {
/*  94 */       return this.status;
/*     */     }
/*     */     
/*     */     public Optional<Component> errorMessage() {
/*  94 */       return this.errorMessage;
/*     */     }
/*     */     
/*     */     static {
/*  95 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)ResourceKey.codec(Registries.TEST_INSTANCE).optionalFieldOf("test").forGetter(Data::test), (App)Vec3i.CODEC.fieldOf("size").forGetter(Data::size), (App)Rotation.CODEC.fieldOf("rotation").forGetter(Data::rotation), (App)Codec.BOOL.fieldOf("ignore_entities").forGetter(Data::ignoreEntities), (App)TestInstanceBlockEntity.Status.CODEC.fieldOf("status").forGetter(Data::status), (App)ComponentSerialization.CODEC.optionalFieldOf("error_message").forGetter(Data::errorMessage)).apply((Applicative)i, Data::new));
/*     */     }
/*     */     
/* 103 */     public static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
/* 104 */         ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.TEST_INSTANCE)), Data::test, Vec3i.STREAM_CODEC, Data::size, Rotation.STREAM_CODEC, Data::rotation, ByteBufCodecs.BOOL, Data::ignoreEntities, TestInstanceBlockEntity.Status.STREAM_CODEC, Data::status, 
/*     */         
/* 109 */         ByteBufCodecs.optional(ComponentSerialization.STREAM_CODEC), Data::errorMessage, Data::new);
/*     */     
/*     */     public Data withSize(Vec3i size) {
/* 114 */       return new Data(this.test, size, this.rotation, this.ignoreEntities, this.status, this.errorMessage);
/*     */     }
/*     */     
/*     */     public Data withStatus(TestInstanceBlockEntity.Status status) {
/* 118 */       return new Data(this.test, this.size, this.rotation, this.ignoreEntities, status, Optional.empty());
/*     */     }
/*     */     
/*     */     public Data withError(Component error) {
/* 122 */       return new Data(this.test, this.size, this.rotation, this.ignoreEntities, TestInstanceBlockEntity.Status.FINISHED, Optional.of(error));
/*     */     }
/*     */   }
/*     */   
/* 127 */   private static final Vec3i STRUCTURE_OFFSET = new Vec3i(0, 1, 1);
/*     */   
/*     */   private Data data;
/*     */   
/* 130 */   private final List<ErrorMarker> errorMarkers = new ArrayList<>();
/*     */   
/*     */   public TestInstanceBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 133 */     super(BlockEntityTypes.TEST_INSTANCE_BLOCK, worldPosition, blockState);
/* 134 */     this.data = new Data(Optional.empty(), Vec3i.ZERO, Rotation.NONE, false, Status.CLEARED, Optional.empty());
/*     */   }
/*     */   
/*     */   public void set(Data data) {
/* 138 */     this.data = data;
/* 139 */     setChanged();
/*     */   }
/*     */   
/*     */   public static Optional<Vec3i> getStructureSize(ServerLevel level, ResourceKey<GameTestInstance> testKey) {
/* 143 */     return getStructureTemplate(level, testKey).map(StructureTemplate::getSize);
/*     */   }
/*     */   
/*     */   public BoundingBox getStructureBoundingBox() {
/* 147 */     BlockPos corner1 = getStructurePos();
/* 148 */     BlockPos corner2 = corner1.offset(getTransformedSize()).offset(-1, -1, -1);
/* 150 */     return BoundingBox.fromCorners((Vec3i)corner1, (Vec3i)corner2);
/*     */   }
/*     */   
/*     */   public BoundingBox getTestBoundingBox() {
/* 154 */     return getStructureBoundingBox().inflatedBy(getPadding());
/*     */   }
/*     */   
/*     */   public AABB getStructureBounds() {
/* 158 */     return AABB.of(getStructureBoundingBox());
/*     */   }
/*     */   
/*     */   public AABB getTestBounds() {
/* 162 */     return getStructureBounds().inflate(getPadding());
/*     */   }
/*     */   
/*     */   private static Optional<StructureTemplate> getStructureTemplate(ServerLevel level, ResourceKey<GameTestInstance> testKey) {
/* 166 */     return level.registryAccess().get(testKey)
/* 167 */       .map(test -> ((GameTestInstance)test.value()).structure())
/* 168 */       .flatMap(template -> level.getStructureManager().get(template));
/*     */   }
/*     */   
/*     */   public Optional<ResourceKey<GameTestInstance>> test() {
/* 172 */     return this.data.test();
/*     */   }
/*     */   
/*     */   public Component getTestName() {
/* 176 */     return test().<Component>map(key -> Component.literal(key.identifier().toString())).orElse(INVALID_TEST_NAME);
/*     */   }
/*     */   
/*     */   private Optional<Holder.Reference<GameTestInstance>> getTestHolder() {
/* 180 */     Objects.requireNonNull(this.level.registryAccess());
/* 180 */     return test().flatMap(this.level.registryAccess()::get);
/*     */   }
/*     */   
/*     */   public boolean ignoreEntities() {
/* 184 */     return this.data.ignoreEntities();
/*     */   }
/*     */   
/*     */   public Vec3i getSize() {
/* 188 */     return this.data.size();
/*     */   }
/*     */   
/*     */   public Rotation getRotation() {
/* 192 */     return ((Rotation)getTestHolder()
/* 193 */       .map(Holder::value)
/* 194 */       .map(GameTestInstance::rotation)
/* 195 */       .orElse(Rotation.NONE))
/* 196 */       .getRotated(this.data.rotation());
/*     */   }
/*     */   
/*     */   public Optional<Component> errorMessage() {
/* 200 */     return this.data.errorMessage();
/*     */   }
/*     */   
/*     */   public void setErrorMessage(Component errorMessage) {
/* 204 */     set(this.data.withError(errorMessage));
/*     */   }
/*     */   
/*     */   public void setSuccess() {
/* 208 */     set(this.data.withStatus(Status.FINISHED));
/*     */   }
/*     */   
/*     */   public void setRunning() {
/* 212 */     set(this.data.withStatus(Status.RUNNING));
/*     */   }
/*     */   
/*     */   public void setChanged() {
/* 217 */     super.setChanged();
/* 218 */     if (this.level instanceof ServerLevel)
/* 219 */       this.level.sendBlockUpdated(getBlockPos(), Blocks.AIR.defaultBlockState(), getBlockState(), 3); 
/*     */   }
/*     */   
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 225 */     return ClientboundBlockEntityDataPacket.create(this);
/*     */   }
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
/* 230 */     return saveCustomOnly(registries);
/*     */   }
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 235 */     input.read("data", Data.CODEC).ifPresent(this::set);
/* 237 */     this.errorMarkers.clear();
/* 238 */     this.errorMarkers.addAll(input.read("errors", ErrorMarker.LIST_CODEC).orElse(List.of()));
/*     */   }
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 243 */     output.store("data", Data.CODEC, this.data);
/* 244 */     if (!this.errorMarkers.isEmpty())
/* 245 */       output.store("errors", ErrorMarker.LIST_CODEC, this.errorMarkers); 
/*     */   }
/*     */   
/*     */   public BoundingBoxRenderable.Mode renderMode() {
/* 251 */     return BoundingBoxRenderable.Mode.BOX;
/*     */   }
/*     */   
/*     */   public BlockPos getStructurePos() {
/* 255 */     int padding = getPadding();
/* 256 */     return getStructurePos(getBlockPos().offset(padding, padding, padding));
/*     */   }
/*     */   
/*     */   public static BlockPos getStructurePos(BlockPos blockPos) {
/* 260 */     return blockPos.offset(STRUCTURE_OFFSET);
/*     */   }
/*     */   
/*     */   public BoundingBoxRenderable.RenderableBox getRenderableBox() {
/* 265 */     int padding = getPadding();
/* 266 */     return new BoundingBoxRenderable.RenderableBox((new BlockPos(STRUCTURE_OFFSET)).offset(padding, padding, padding), getTransformedSize());
/*     */   }
/*     */   
/*     */   public List<BeaconBeamOwner.Section> getBeamSections() {
/* 271 */     switch (this.data.status().ordinal()) {
/*     */       default:
/* 271 */         throw new MatchException(null, null);
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */         break;
/*     */     } 
/* 271 */     return 
/*     */       
/* 274 */       errorMessage().isEmpty() ? 
/* 275 */       BEAM_SUCCESS : (
/* 276 */       ((Boolean)getTestHolder().map(Holder::value).map(GameTestInstance::required).orElse(Boolean.valueOf(true))).booleanValue() ? 
/* 277 */       BEAM_REQUIRED_FAILED : 
/* 278 */       BEAM_OPTIONAL_FAILED);
/*     */   }
/*     */   
/*     */   private Vec3i getTransformedSize() {
/* 283 */     Vec3i size = getSize();
/* 284 */     Rotation rotation = getRotation();
/* 285 */     boolean axesSwitched = (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90);
/* 286 */     int xSize = axesSwitched ? size.getZ() : size.getX();
/* 287 */     int zSize = axesSwitched ? size.getX() : size.getZ();
/* 288 */     return new Vec3i(xSize, size.getY(), zSize);
/*     */   }
/*     */   
/*     */   public void resetTest(Consumer<Component> feedbackOutput) {
/* 292 */     removeBarriers();
/* 293 */     clearErrorMarkers();
/* 294 */     boolean placed = placeStructure();
/* 295 */     if (placed)
/* 296 */       feedbackOutput.accept(Component.translatable("test_instance_block.reset_success", new Object[] { getTestName() }).withStyle(ChatFormatting.GREEN)); 
/* 298 */     set(this.data.withStatus(Status.CLEARED));
/*     */   }
/*     */   
/*     */   public Optional<Identifier> saveTest(Consumer<Component> feedbackOutput) {
/*     */     Optional<Identifier> identifier;
/* 302 */     Optional<Holder.Reference<GameTestInstance>> test = getTestHolder();
/* 304 */     if (test.isPresent()) {
/* 305 */       identifier = Optional.of(((GameTestInstance)((Holder.Reference)test.get()).value()).structure());
/*     */     } else {
/* 308 */       identifier = test().map(ResourceKey::identifier);
/*     */     } 
/* 310 */     if (identifier.isEmpty()) {
/* 311 */       BlockPos pos = getBlockPos();
/* 312 */       feedbackOutput.accept(Component.translatable("test_instance_block.error.unable_to_save", new Object[] { Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()) }).withStyle(ChatFormatting.RED));
/* 313 */       return identifier;
/*     */     } 
/* 315 */     Level level = this.level;
/* 315 */     if (level instanceof ServerLevel) {
/* 315 */       ServerLevel serverLevel = (ServerLevel)level;
/* 316 */       StructureBlockEntity.saveStructure(serverLevel, identifier.get(), getStructurePos(), getSize(), ignoreEntities(), "", true, List.of(Blocks.AIR));
/*     */     } 
/* 318 */     return identifier;
/*     */   }
/*     */   
/*     */   public boolean exportTest(Consumer<Component> feedbackOutput) {
/*     */     ServerLevel serverLevel;
/* 322 */     Optional<Identifier> saved = saveTest(feedbackOutput);
/* 323 */     if (!saved.isEmpty()) {
/* 323 */       Level level = this.level;
/* 323 */       if (level instanceof ServerLevel) {
/* 323 */         serverLevel = (ServerLevel)level;
/*     */       } else {
/* 324 */         return false;
/*     */       } 
/*     */     } else {
/* 324 */       return false;
/*     */     } 
/* 326 */     return export(serverLevel, saved.get(), feedbackOutput);
/*     */   }
/*     */   
/*     */   public static boolean export(ServerLevel level, Identifier structureId, Consumer<Component> feedbackOutput) {
/* 330 */     StructureTemplateManager structureManager = level.getStructureManager();
/* 331 */     TemplatePathFactory testTemplatePathFactory = structureManager.testTemplates();
/* 332 */     if (testTemplatePathFactory == null) {
/* 333 */       feedbackOutput.accept(Component.literal("Test structure exporting is disabled").withStyle(ChatFormatting.RED));
/* 334 */       return true;
/*     */     } 
/* 337 */     Optional<StructureTemplate> structureTemplate = structureManager.get(structureId);
/* 338 */     if (structureTemplate.isEmpty()) {
/* 339 */       feedbackOutput.accept(Component.literal("Could not find structure " + String.valueOf(structureId)).withStyle(ChatFormatting.RED));
/* 340 */       return true;
/*     */     } 
/* 343 */     Path outputFile = testTemplatePathFactory.createAndValidatePathToStructure(structureId, StructureTemplateManager.RESOURCE_TEXT_STRUCTURE_LISTER);
/*     */     try {
/* 345 */       StructureTemplateManager.save(outputFile, structureTemplate.get(), true);
/* 346 */     } catch (Exception e) {
/* 347 */       LOGGER.error("Failed to save structure file {} to {}", new Object[] { structureId, outputFile, e });
/* 348 */       feedbackOutput.accept(Component.literal("Failed to save structure file " + String.valueOf(structureId) + " to " + String.valueOf(outputFile)).withStyle(ChatFormatting.RED));
/* 349 */       return true;
/*     */     } 
/* 352 */     feedbackOutput.accept(Component.literal("Exported " + String.valueOf(structureId) + " to " + String.valueOf(outputFile.toAbsolutePath())));
/* 353 */     return false;
/*     */   }
/*     */   
/*     */   public void runTest(Consumer<Component> feedbackOutput) {
/*     */     ServerLevel serverLevel;
/* 357 */     Level level = this.level;
/* 357 */     if (level instanceof ServerLevel) {
/* 357 */       serverLevel = (ServerLevel)level;
/*     */     } else {
/*     */       return;
/*     */     } 
/* 360 */     Optional<Holder.Reference<GameTestInstance>> test = getTestHolder();
/* 361 */     BlockPos pos = getBlockPos();
/* 362 */     if (test.isEmpty()) {
/* 363 */       feedbackOutput.accept(Component.translatable("test_instance_block.error.no_test", new Object[] { Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()) }).withStyle(ChatFormatting.RED));
/*     */       return;
/*     */     } 
/* 367 */     if (!placeStructure()) {
/* 368 */       feedbackOutput.accept(Component.translatable("test_instance_block.error.no_test_structure", new Object[] { Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()) }).withStyle(ChatFormatting.RED));
/*     */       return;
/*     */     } 
/* 372 */     clearErrorMarkers();
/* 373 */     GameTestTicker.SINGLETON.clear();
/* 374 */     FailedTestTracker.forgetFailedTests();
/* 375 */     feedbackOutput.accept(Component.translatable("test_instance_block.starting", new Object[] { ((Holder.Reference)test.get()).getRegisteredName() }));
/* 377 */     GameTestInfo gameTestInfo = new GameTestInfo(test.get(), this.data.rotation(), serverLevel, RetryOptions.noRetries());
/* 378 */     gameTestInfo.setTestBlockPos(pos);
/* 380 */     GameTestRunner runner = GameTestRunner.Builder.fromInfo(List.of(gameTestInfo), serverLevel).build();
/* 381 */     TestCommand.trackAndStartRunner(serverLevel.getServer().createCommandSourceStack(), runner);
/*     */   }
/*     */   
/*     */   public boolean placeStructure() {
/* 385 */     Level level = this.level;
/* 385 */     if (level instanceof ServerLevel) {
/* 385 */       ServerLevel serverLevel = (ServerLevel)level;
/* 386 */       Optional<StructureTemplate> template = this.data.test().flatMap(test -> getStructureTemplate(serverLevel, test));
/* 387 */       if (template.isPresent()) {
/* 388 */         placeStructure(serverLevel, template.get());
/* 389 */         return true;
/*     */       } 
/*     */     } 
/* 392 */     return false;
/*     */   }
/*     */   
/*     */   private void placeStructure(ServerLevel level, StructureTemplate template) {
/* 396 */     StructurePlaceSettings placeSettings = (new StructurePlaceSettings()).setRotation(getRotation()).setIgnoreEntities(this.data.ignoreEntities()).setKnownShape(true);
/* 397 */     BlockPos pos = getStartCorner();
/* 399 */     forceLoadChunks();
/* 400 */     int padding = getPadding();
/* 401 */     StructureUtils.clearSpaceForStructure(getTestBoundingBox(), level);
/* 402 */     removeEntities();
/* 403 */     template.placeInWorld((ServerLevelAccessor)level, pos, pos, placeSettings, level.getRandom(), 818);
/*     */   }
/*     */   
/*     */   private int getPadding() {
/* 407 */     return ((Integer)getTestHolder().<Integer>map(r -> Integer.valueOf(((GameTestInstance)r.value()).padding())).orElse(Integer.valueOf(0))).intValue();
/*     */   }
/*     */   
/*     */   private void removeEntities() {
/* 411 */     this.level.getEntities(null, getTestBounds()).stream().filter(entity -> !(entity instanceof net.minecraft.world.entity.player.Player)).forEach(Entity::discard);
/*     */   }
/*     */   
/*     */   private void forceLoadChunks() {
/* 415 */     Level level = this.level;
/* 415 */     if (level instanceof ServerLevel) {
/* 415 */       ServerLevel serverLevel = (ServerLevel)level;
/* 416 */       getStructureBoundingBox().intersectingChunks().forEach(pos -> serverLevel.setChunkForced(pos.x(), pos.z(), true));
/*     */     } 
/*     */   }
/*     */   
/*     */   public BlockPos getStartCorner() {
/* 421 */     Vec3i structureSize = getSize();
/* 422 */     Rotation rotation = getRotation();
/* 423 */     BlockPos northWestCorner = getStructurePos();
/* 424 */     switch (rotation) {
/*     */       default:
/* 424 */         throw new MatchException(null, null);
/*     */       case NONE:
/*     */       
/*     */       case CLOCKWISE_90:
/*     */       
/*     */       case CLOCKWISE_180:
/*     */       
/*     */       case COUNTERCLOCKWISE_90:
/*     */         break;
/*     */     } 
/* 424 */     return 
/*     */       
/* 428 */       northWestCorner.offset(0, 0, structureSize.getX() - 1);
/*     */   }
/*     */   
/*     */   public void encaseStructure() {
/* 433 */     processStructureBoundary(blockPos -> {
/*     */           if (!this.level.getBlockState(blockPos).is(Blocks.TEST_INSTANCE_BLOCK))
/*     */             this.level.setBlockAndUpdate(blockPos, Blocks.BARRIER.defaultBlockState()); 
/*     */         });
/*     */   }
/*     */   
/*     */   public void removeBarriers() {
/* 443 */     processStructureBoundary(blockPos -> {
/*     */           if (this.level.getBlockState(blockPos).is(Blocks.BARRIER))
/*     */             this.level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState()); 
/*     */         });
/*     */   }
/*     */   
/*     */   public void processStructureBoundary(Consumer<BlockPos> action) {
/* 453 */     AABB bounds = getStructureBounds();
/* 454 */     boolean hasCeiling = !((Boolean)getTestHolder().<Boolean>map(h -> Boolean.valueOf(((GameTestInstance)h.value()).skyAccess())).orElse(Boolean.valueOf(false))).booleanValue();
/* 456 */     BlockPos low = BlockPos.containing(bounds.minX, bounds.minY, bounds.minZ).offset(-1, -1, -1);
/* 457 */     BlockPos high = BlockPos.containing(bounds.maxX, bounds.maxY, bounds.maxZ);
/* 458 */     BlockPos.betweenClosedStream(low, high).forEach(blockPos -> {
/* 460 */           boolean isNonCeilingEdge = (blockPos.getX() == low.getX() || blockPos.getX() == high.getX() || blockPos.getZ() == low.getZ() || blockPos.getZ() == high.getZ() || blockPos.getY() == low.getY());
/*     */           boolean isCeiling = (blockPos.getY() == high.getY());
/*     */           if (isNonCeilingEdge || (isCeiling && hasCeiling))
/*     */             action.accept(blockPos); 
/*     */         });
/*     */   }
/*     */   
/*     */   public void markError(BlockPos pos, Component text) {
/* 470 */     this.errorMarkers.add(new ErrorMarker(pos, text));
/* 471 */     setChanged();
/*     */   }
/*     */   
/*     */   public void clearErrorMarkers() {
/* 475 */     if (!this.errorMarkers.isEmpty()) {
/* 476 */       this.errorMarkers.clear();
/* 477 */       setChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<ErrorMarker> getErrorMarkers() {
/* 482 */     return this.errorMarkers;
/*     */   }
/*     */   
/*     */   public static final class ErrorMarker extends Record {
/*     */     private final BlockPos pos;
/*     */     
/*     */     private final Component text;
/*     */     
/*     */     public static final Codec<ErrorMarker> CODEC;
/*     */     
/*     */     public ErrorMarker(BlockPos pos, Component text) {
/* 485 */       this.pos = pos;
/* 485 */       this.text = text;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$ErrorMarker;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #485	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$ErrorMarker;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$ErrorMarker;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #485	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$ErrorMarker;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$ErrorMarker;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #485	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$ErrorMarker;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public BlockPos pos() {
/* 485 */       return this.pos;
/*     */     }
/*     */     
/*     */     public Component text() {
/* 485 */       return this.text;
/*     */     }
/*     */     
/*     */     static {
/* 489 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockPos.CODEC.fieldOf("pos").forGetter(ErrorMarker::pos), (App)ComponentSerialization.CODEC.fieldOf("text").forGetter(ErrorMarker::text)).apply((Applicative)i, ErrorMarker::new));
/*     */     }
/*     */     
/* 494 */     public static final Codec<List<ErrorMarker>> LIST_CODEC = CODEC.listOf();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\TestInstanceBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */