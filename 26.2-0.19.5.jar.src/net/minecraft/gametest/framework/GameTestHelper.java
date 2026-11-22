/*      */ package net.minecraft.gametest.framework;
/*      */ 
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*      */ import com.mojang.datafixers.util.Either;
/*      */ import io.netty.channel.ChannelHandler;
/*      */ import io.netty.channel.embedded.EmbeddedChannel;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.UUID;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.IntPredicate;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.stream.LongStream;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.network.Connection;
/*      */ import net.minecraft.network.chat.CommonComponents;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.protocol.PacketFlow;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.commands.FillBiomeCommand;
/*      */ import net.minecraft.server.level.ClientInformation;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.server.network.CommonListenerCookie;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.clock.WorldClock;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.effect.MobEffect;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.EntityTypes;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.npc.InventoryCarrier;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.BlockItem;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.alchemy.PotionContents;
/*      */ import net.minecraft.world.item.context.BlockPlaceContext;
/*      */ import net.minecraft.world.item.context.UseOnContext;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.biome.Biome;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.ButtonBlock;
/*      */ import net.minecraft.world.level.block.HorizontalDirectionalBlock;
/*      */ import net.minecraft.world.level.block.LeverBlock;
/*      */ import net.minecraft.world.level.block.Mirror;
/*      */ import net.minecraft.world.level.block.Rotation;
/*      */ import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.entity.EntityTypeTest;
/*      */ import net.minecraft.world.level.levelgen.Heightmap;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*      */ import net.minecraft.world.level.pathfinder.Path;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ public class GameTestHelper {
/*      */   private final GameTestInfo testInfo;
/*      */   
/*      */   private boolean finalCheckAdded;
/*      */   
/*      */   public GameTestHelper(GameTestInfo testInfo) {
/*   89 */     this.testInfo = testInfo;
/*      */   }
/*      */   
/*      */   public GameTestAssertException assertionException(Component description) {
/*   95 */     return new GameTestAssertException(description, this.testInfo.getTick());
/*      */   }
/*      */   
/*      */   public GameTestAssertException assertionException(String descriptionId, Object... arguments) {
/*   99 */     return assertionException((Component)Component.translatableEscape(descriptionId, arguments));
/*      */   }
/*      */   
/*      */   public GameTestAssertPosException assertionException(BlockPos pos, Component description) {
/*  103 */     return new GameTestAssertPosException(description, absolutePos(pos), pos, this.testInfo.getTick());
/*      */   }
/*      */   
/*      */   public GameTestAssertPosException assertionException(BlockPos pos, String descriptionId, Object... arguments) {
/*  107 */     return assertionException(pos, (Component)Component.translatableEscape(descriptionId, arguments));
/*      */   }
/*      */   
/*      */   public ServerLevel getLevel() {
/*  113 */     return this.testInfo.getLevel();
/*      */   }
/*      */   
/*      */   public BlockState getBlockState(BlockPos pos) {
/*  117 */     return getLevel().getBlockState(absolutePos(pos));
/*      */   }
/*      */   
/*      */   public <T extends BlockEntity> T getBlockEntity(BlockPos pos, Class<T> type) {
/*  121 */     BlockEntity blockEntity = getLevel().getBlockEntity(absolutePos(pos));
/*  122 */     if (blockEntity == null)
/*  123 */       throw assertionException(pos, "test.error.missing_block_entity", new Object[0]); 
/*  125 */     if (type.isInstance(blockEntity))
/*  126 */       return type.cast(blockEntity); 
/*  128 */     throw assertionException(pos, "test.error.wrong_block_entity", new Object[] { blockEntity.typeHolder().getRegisteredName() });
/*      */   }
/*      */   
/*      */   public void killAllEntities() {
/*  134 */     killAllEntitiesOfClass(Entity.class);
/*      */   }
/*      */   
/*      */   public void killAllEntitiesOfClass(Class<? extends Entity> baseClass) {
/*  138 */     AABB bounds = getBounds();
/*  139 */     List<? extends Entity> entities = getLevel().getEntitiesOfClass(baseClass, bounds.inflate(1.0D), mob -> !(mob instanceof Player));
/*  140 */     entities.forEach(entity -> entity.kill(getLevel()));
/*      */   }
/*      */   
/*      */   public ItemEntity spawnItem(Item item, Vec3 pos) {
/*  144 */     ServerLevel level = getLevel();
/*  145 */     Vec3 absoluteVec = absoluteVec(pos);
/*  146 */     ItemEntity itemEntity = new ItemEntity((Level)level, absoluteVec.x, absoluteVec.y, absoluteVec.z, new ItemStack((ItemLike)item, 1));
/*  147 */     itemEntity.setDeltaMovement(0.0D, 0.0D, 0.0D);
/*  148 */     level.addFreshEntity((Entity)itemEntity);
/*  149 */     return itemEntity;
/*      */   }
/*      */   
/*      */   public ItemEntity spawnItem(Item item, float x, float y, float z) {
/*  153 */     return spawnItem(item, new Vec3(x, y, z));
/*      */   }
/*      */   
/*      */   public ItemEntity spawnItem(Item item, BlockPos pos) {
/*  157 */     return spawnItem(item, pos.getX(), pos.getY(), pos.getZ());
/*      */   }
/*      */   
/*      */   public void despawnItem(BlockPos pos, double distance) {
/*  161 */     BlockPos absolutePos = absolutePos(pos);
/*  162 */     List<ItemEntity> despawnItem = getLevel().getEntities((EntityTypeTest)EntityTypes.ITEM, (new AABB(absolutePos)).inflate(distance), Entity::isAlive);
/*  163 */     for (ItemEntity entity : despawnItem)
/*  164 */       entity.remove(Entity.RemovalReason.KILLED); 
/*      */   }
/*      */   
/*      */   public <E extends Entity> E spawn(EntityType<E> entityType, BlockPos pos) {
/*  169 */     return spawn(entityType, Vec3.atBottomCenterOf((Vec3i)pos));
/*      */   }
/*      */   
/*      */   public <E extends Entity> List<E> spawn(EntityType<E> entityType, BlockPos pos, int amount) {
/*  173 */     return spawn(entityType, Vec3.atBottomCenterOf((Vec3i)pos), amount);
/*      */   }
/*      */   
/*      */   public <E extends Entity> List<E> spawn(EntityType<E> entityType, Vec3 pos, int amount) {
/*  177 */     return spawnEntity(entityType, pos).spawn(amount);
/*      */   }
/*      */   
/*      */   public <E extends Entity> E spawn(EntityType<E> entityType, Vec3 pos) {
/*  181 */     return spawn(entityType, pos, (EntitySpawnReason)null);
/*      */   }
/*      */   
/*      */   public <E extends Mob> E spawn(EntityType<E> entityType, double x, double y, double z, EntitySpawnReason entitySpawnReason) {
/*  185 */     return (E)spawn(entityType, new Vec3(x, y, z), entitySpawnReason);
/*      */   }
/*      */   
/*      */   public <E extends Entity> E spawn(EntityType<E> entityType, BlockPos pos, EntitySpawnReason spawnReason) {
/*  189 */     return spawnEntity(entityType, pos).spawnReason(spawnReason).spawn();
/*      */   }
/*      */   
/*      */   public <E extends Entity> E spawn(EntityType<E> entityType, Vec3 pos, EntitySpawnReason spawnReason) {
/*  193 */     return spawnEntity(entityType, pos).spawnReason(spawnReason).spawn();
/*      */   }
/*      */   
/*      */   public <E extends Entity> GameTestEntityBuilder<E> spawnEntity(EntityType<E> entityType, Vec3 position) {
/*  197 */     return new GameTestEntityBuilder<>(this, entityType, position);
/*      */   }
/*      */   
/*      */   public <E extends Entity> GameTestEntityBuilder<E> spawnEntity(EntityType<E> entityType, BlockPos position) {
/*  201 */     return spawnEntity(entityType, Vec3.atBottomCenterOf((Vec3i)position));
/*      */   }
/*      */   
/*      */   public <E extends Entity> GameTestEntityBuilder<E> spawnEntity(EntityType<E> entityType, int x, int y, int z) {
/*  205 */     return spawnEntity(entityType, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public <E extends Entity> GameTestEntityBuilder<E> spawnEntity(EntityType<E> entityType, float x, float y, float z) {
/*  209 */     return spawnEntity(entityType, new Vec3(x, y, z));
/*      */   }
/*      */   
/*      */   public <E extends Mob> GameTestMobBuilder<E> spawnMob(EntityType<E> entityType, Vec3 position) {
/*  213 */     return new GameTestMobBuilder<>(this, entityType, position);
/*      */   }
/*      */   
/*      */   public <E extends Mob> GameTestMobBuilder<E> spawnMob(EntityType<E> entityType, BlockPos position) {
/*  217 */     return spawnMob(entityType, Vec3.atBottomCenterOf((Vec3i)position));
/*      */   }
/*      */   
/*      */   public <E extends Mob> GameTestMobBuilder<E> spawnMob(EntityType<E> entityType, int x, int y, int z) {
/*  221 */     return spawnMob(entityType, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public <E extends Mob> GameTestMobBuilder<E> spawnMob(EntityType<E> entityType, float x, float y, float z) {
/*  225 */     return spawnMob(entityType, new Vec3(x, y, z));
/*      */   }
/*      */   
/*      */   public void hurt(Entity entity, DamageSource source, float damage) {
/*  229 */     entity.hurtServer(getLevel(), source, damage);
/*      */   }
/*      */   
/*      */   public void kill(Entity entity) {
/*  233 */     entity.kill(getLevel());
/*      */   }
/*      */   
/*      */   public void discard(Entity entity) {
/*  237 */     entity.discard();
/*      */   }
/*      */   
/*      */   public <E extends Entity> E findOneEntity(EntityType<E> entityType) {
/*  241 */     return findClosestEntity(entityType, 0, 0, 0, 2.147483647E9D);
/*      */   }
/*      */   
/*      */   public <E extends Entity> E findClosestEntity(EntityType<E> entityType, int x, int y, int z, double distance) {
/*  245 */     List<E> entities = findEntities(entityType, x, y, z, distance);
/*  246 */     if (entities.isEmpty())
/*  247 */       throw assertionException("test.error.expected_entity_around", new Object[] { entityType.getDescription(), Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z) }); 
/*  249 */     if (entities.size() > 1)
/*  250 */       throw assertionException("test.error.too_many_entities", new Object[] { entityType.toShortString(), Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z), Integer.valueOf(entities.size()) }); 
/*  252 */     Vec3 center = absoluteVec(new Vec3(x, y, z));
/*  254 */     entities.sort((e1, e2) -> {
/*      */           double d1 = e1.position().distanceTo(center);
/*      */           double d2 = e2.position().distanceTo(center);
/*      */           return Double.compare(d1, d2);
/*      */         });
/*  259 */     return entities.get(0);
/*      */   }
/*      */   
/*      */   public <E extends Entity> List<E> findEntities(EntityType<E> entityType, int x, int y, int z, double distance) {
/*  263 */     return findEntities(entityType, Vec3.atBottomCenterOf((Vec3i)new BlockPos(x, y, z)), distance);
/*      */   }
/*      */   
/*      */   public <E extends Entity> List<E> findEntities(EntityType<E> entityType, Vec3 pos, double distance) {
/*  267 */     ServerLevel level = getLevel();
/*  268 */     Vec3 absoluteVec = absoluteVec(pos);
/*  269 */     AABB structureBounds = this.testInfo.getStructureBounds();
/*  270 */     AABB containedBounds = new AABB(absoluteVec.add(-distance, -distance, -distance), absoluteVec.add(distance, distance, distance));
/*  271 */     return level.getEntities((EntityTypeTest)entityType, structureBounds, e -> (e.getBoundingBox().intersects(containedBounds) && e.isAlive()));
/*      */   }
/*      */   
/*      */   public <E extends Entity> E spawn(EntityType<E> entityType, int x, int y, int z) {
/*  275 */     return spawn(entityType, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public <E extends Entity> E spawn(EntityType<E> entityType, float x, float y, float z) {
/*  279 */     return spawn(entityType, new Vec3(x, y, z));
/*      */   }
/*      */   
/*      */   public <E extends Mob> E spawnWithNoFreeWill(EntityType<E> entityType, BlockPos pos) {
/*  283 */     return spawnMob(entityType, pos).withNoFreeWill().spawn();
/*      */   }
/*      */   
/*      */   public <E extends Mob> E spawnWithNoFreeWill(EntityType<E> entityType, int x, int y, int z) {
/*  287 */     return spawnWithNoFreeWill(entityType, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public <E extends Mob> E spawnWithNoFreeWill(EntityType<E> entityType, Vec3 pos) {
/*  291 */     Mob mob = spawn(entityType, pos);
/*  292 */     mob.removeFreeWill();
/*  293 */     return (E)mob;
/*      */   }
/*      */   
/*      */   public <E extends Mob> E spawnWithNoFreeWill(EntityType<E> entityType, float x, float y, float z) {
/*  297 */     return spawnWithNoFreeWill(entityType, new Vec3(x, y, z));
/*      */   }
/*      */   
/*      */   public void moveTo(Mob mob, float x, float y, float z) {
/*  301 */     moveTo(mob, new Vec3(x, y, z));
/*      */   }
/*      */   
/*      */   public void moveTo(Mob mob, BlockPos pos) {
/*  305 */     moveTo(mob, Vec3.atBottomCenterOf((Vec3i)pos));
/*      */   }
/*      */   
/*      */   public void moveTo(Mob mob, Vec3 pos) {
/*  309 */     Vec3 absoluteVec = absoluteVec(pos);
/*  310 */     mob.snapTo(absoluteVec.x, absoluteVec.y, absoluteVec.z, mob.getYRot(), mob.getXRot());
/*      */   }
/*      */   
/*      */   public GameTestSequence walkTo(Mob mob, BlockPos targetPos, float speedModifier) {
/*  314 */     return startSequence().thenExecuteAfter(2, () -> {
/*      */           Path path = mob.getNavigation().createPath(absolutePos(targetPos), 0);
/*      */           mob.getNavigation().moveTo(path, speedModifier);
/*      */         });
/*      */   }
/*      */   
/*      */   public void pressButton(int x, int y, int z) {
/*  321 */     pressButton(new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public void pressButton(BlockPos buttonPos) {
/*  325 */     assertBlockTag(BlockTags.BUTTONS, buttonPos);
/*  327 */     BlockPos absolutePos = absolutePos(buttonPos);
/*  328 */     BlockState blockState = getLevel().getBlockState(absolutePos);
/*  330 */     ButtonBlock buttonBlock = (ButtonBlock)blockState.getBlock();
/*  331 */     buttonBlock.press(blockState, (Level)getLevel(), absolutePos, null);
/*      */   }
/*      */   
/*      */   public void useBlock(BlockPos relativePos) {
/*  335 */     useBlock(relativePos, makeMockPlayer(GameType.CREATIVE));
/*      */   }
/*      */   
/*      */   public void useBlock(BlockPos relativePos, Player player) {
/*  339 */     BlockPos absolutePos = absolutePos(relativePos);
/*  340 */     useBlock(relativePos, player, new BlockHitResult(Vec3.atCenterOf((Vec3i)absolutePos), Direction.NORTH, absolutePos, true));
/*      */   }
/*      */   
/*      */   public void useBlock(BlockPos relativePos, Player player, BlockHitResult hitResult) {
/*  344 */     BlockPos absolutePos = absolutePos(relativePos);
/*  345 */     BlockState blockState = getLevel().getBlockState(absolutePos);
/*  347 */     InteractionHand hand = InteractionHand.MAIN_HAND;
/*  349 */     InteractionResult itemInteractionResult = blockState.useItemOn(player.getItemInHand(hand), (Level)getLevel(), player, hand, hitResult);
/*  350 */     if (itemInteractionResult.consumesAction())
/*      */       return; 
/*  354 */     if (itemInteractionResult instanceof InteractionResult.TryEmptyHandInteraction && blockState.useWithoutItem((Level)getLevel(), player, hitResult).consumesAction())
/*      */       return; 
/*  358 */     UseOnContext context = new UseOnContext(player, hand, hitResult);
/*  359 */     player.getItemInHand(hand).useOn(context);
/*      */   }
/*      */   
/*      */   public LivingEntity makeAboutToDrown(LivingEntity entity) {
/*  363 */     entity.setAirSupply(0);
/*  364 */     entity.setHealth(0.25F);
/*  365 */     return entity;
/*      */   }
/*      */   
/*      */   public LivingEntity withLowHealth(LivingEntity entity) {
/*  369 */     entity.setHealth(0.25F);
/*  370 */     return entity;
/*      */   }
/*      */   
/*      */   public Player makeMockPlayer(final GameType gameType) {
/*  374 */     return new Player(this, (Level)getLevel(), new GameProfile(UUID.randomUUID(), "test-mock-player")) {
/*      */         public GameType gameMode() {
/*  377 */           return gameType;
/*      */         }
/*      */         
/*      */         public boolean isClientAuthoritative() {
/*  382 */           return false;
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   public Player makeMockServerPlayer(final GameType gameType) {
/*  388 */     ServerPlayer serverPlayer = new ServerPlayer(this, getLevel().getServer(), getLevel(), new GameProfile(UUID.randomUUID(), "test-mock-player"), ClientInformation.createDefault()) {
/*      */         public GameType gameMode() {
/*  391 */           return gameType;
/*      */         }
/*      */         
/*      */         public boolean isClientAuthoritative() {
/*  396 */           return false;
/*      */         }
/*      */       };
/*  399 */     gameType.updatePlayerAbilities(serverPlayer.getAbilities());
/*  400 */     return (Player)serverPlayer;
/*      */   }
/*      */   
/*      */   @Deprecated(forRemoval = true)
/*      */   public ServerPlayer makeMockServerPlayerInLevel() {
/*  407 */     CommonListenerCookie cookie = CommonListenerCookie.createInitial(new GameProfile(UUID.randomUUID(), "test-mock-player"), false);
/*  408 */     ServerPlayer player = new ServerPlayer(this, getLevel().getServer(), getLevel(), cookie.gameProfile(), cookie.clientInformation()) {
/*      */         public GameType gameMode() {
/*  411 */           return GameType.CREATIVE;
/*      */         }
/*      */       };
/*  414 */     Connection connection = new Connection(PacketFlow.SERVERBOUND);
/*  417 */     EmbeddedChannel channel = new EmbeddedChannel(new ChannelHandler[] { (ChannelHandler)connection });
/*  418 */     getLevel().getServer().getPlayerList().placeNewPlayer(connection, player, cookie);
/*  419 */     return player;
/*      */   }
/*      */   
/*      */   public void pullLever(int x, int y, int z) {
/*  423 */     pullLever(new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public void pullLever(BlockPos leverPos) {
/*  427 */     assertBlockPresent(Blocks.LEVER, leverPos);
/*  429 */     BlockPos absolutePos = absolutePos(leverPos);
/*  430 */     BlockState blockState = getLevel().getBlockState(absolutePos);
/*  432 */     LeverBlock leverBlock = (LeverBlock)blockState.getBlock();
/*  433 */     leverBlock.pull(blockState, (Level)getLevel(), absolutePos, null);
/*      */   }
/*      */   
/*      */   private class TestBlockPlaceContext extends BlockPlaceContext {
/*      */     private final Direction placeDirection;
/*      */     
/*      */     public TestBlockPlaceContext(GameTestHelper this$0, Level level, InteractionHand hand, ItemStack itemStackInHand, BlockHitResult hitResult, Direction placeDirection) {
/*  440 */       super(level, null, hand, itemStackInHand, hitResult);
/*  441 */       this.placeDirection = placeDirection;
/*      */     }
/*      */     
/*      */     public Direction getNearestLookingDirection() {
/*  446 */       return this.placeDirection;
/*      */     }
/*      */     
/*      */     public Direction[] getNearestLookingDirections() {
/*  451 */       Direction[] directions = Direction.values();
/*  452 */       Arrays.sort(directions, Comparator.comparingDouble(d -> d.getUnitVec3().distanceTo(this.placeDirection.getUnitVec3())));
/*  453 */       return directions;
/*      */     }
/*      */   }
/*      */   
/*      */   public void placeBlock(BlockPos relativePos, Block block, Direction relativePlaceDirection) {
/*  459 */     BlockPos pos = absolutePos(relativePos);
/*  460 */     Direction placeDirection = getAbsoluteDirection(relativePlaceDirection);
/*  461 */     Item item = block.asItem();
/*  462 */     if (item instanceof BlockItem) {
/*  462 */       BlockItem blockItem = (BlockItem)item;
/*  463 */       BlockHitResult hitResult = new BlockHitResult(Vec3.atCenterOf((Vec3i)pos), Direction.DOWN, pos, true);
/*  464 */       BlockPlaceContext context = new TestBlockPlaceContext(this, (Level)getLevel(), InteractionHand.MAIN_HAND, item.getDefaultInstance(), hitResult, placeDirection);
/*  465 */       blockItem.place(context);
/*      */     } else {
/*  467 */       fail((Component)Component.translatable("test.error.not_a_block_item"));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void placeBlock(int x, int y, int z, Block block, Direction placeDirection) {
/*  472 */     placeBlock(new BlockPos(x, y, z), block, placeDirection);
/*      */   }
/*      */   
/*      */   public void pulseRedstone(BlockPos pos, long duration) {
/*  476 */     setBlock(pos, Blocks.REDSTONE_BLOCK);
/*  477 */     runAfterDelay(duration, () -> setBlock(pos, Blocks.AIR));
/*      */   }
/*      */   
/*      */   public void destroyBlock(BlockPos pos) {
/*  481 */     getLevel().destroyBlock(absolutePos(pos), false, null);
/*      */   }
/*      */   
/*      */   public void setBlock(int x, int y, int z, Block block) {
/*  485 */     setBlock(new BlockPos(x, y, z), block);
/*      */   }
/*      */   
/*      */   public void setBlock(int x, int y, int z, BlockState state) {
/*  489 */     setBlock(new BlockPos(x, y, z), state);
/*      */   }
/*      */   
/*      */   public void setBlock(BlockPos blockPos, Block block) {
/*  493 */     setBlock(blockPos, block.defaultBlockState());
/*      */   }
/*      */   
/*      */   public void setBlock(BlockPos blockPos, BlockState state) {
/*  497 */     getLevel().setBlock(absolutePos(blockPos), state, 3);
/*      */   }
/*      */   
/*      */   public void setBlock(BlockPos blockPos, Block block, Direction direction) {
/*  501 */     setBlock(blockPos, block.defaultBlockState(), direction);
/*      */   }
/*      */   
/*      */   public void setBlock(BlockPos blockPos, BlockState blockState, Direction direction) {
/*  505 */     BlockState state = blockState;
/*  507 */     if (blockState.hasProperty((Property)HorizontalDirectionalBlock.FACING))
/*  508 */       state = (BlockState)blockState.setValue((Property)HorizontalDirectionalBlock.FACING, (Comparable)direction); 
/*  511 */     if (blockState.hasProperty((Property)BlockStateProperties.FACING))
/*  512 */       state = (BlockState)blockState.setValue((Property)BlockStateProperties.FACING, (Comparable)direction); 
/*  515 */     getLevel().setBlock(absolutePos(blockPos), state, 3);
/*      */   }
/*      */   
/*      */   public void assertBlockPresent(Block blockType, int x, int y, int z) {
/*  521 */     assertBlockPresent(blockType, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public void assertBlockPresent(Block blockType, BlockPos pos) {
/*  525 */     BlockState state = getBlockState(pos);
/*  526 */     assertBlock(pos, block -> state.is(blockType), block -> Component.translatable("test.error.expected_block", new Object[] { blockType.getName(), block.getName() }));
/*      */   }
/*      */   
/*      */   public void assertBlockPresent(Block blockType) {
/*  530 */     AABB aabb = getRelativeBounds().contract(1.0D, 1.0D, 1.0D);
/*  531 */     boolean foundBlock = BlockPos.MutableBlockPos.betweenClosedStream(aabb).anyMatch(blockPos -> getBlockState(blockPos).is(blockType));
/*  532 */     if (!foundBlock)
/*  533 */       throw assertionException(Component.translatable("test.error.expected_block_present", new Object[] { blockType.getName() })); 
/*      */   }
/*      */   
/*      */   public void assertBlockNotPresent(Block blockType, int x, int y, int z) {
/*  538 */     assertBlockNotPresent(blockType, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public void assertBlockNotPresent(Block blockType, BlockPos pos) {
/*  542 */     assertBlock(pos, block -> !getBlockState(pos).is(blockType), block -> Component.translatable("test.error.unexpected_block", new Object[] { blockType.getName() }));
/*      */   }
/*      */   
/*      */   public void assertBlockTag(TagKey<Block> tag, BlockPos pos) {
/*  546 */     assertBlockState(pos, state -> state.is(tag), state -> Component.translatable("test.error.expected_block_tag", new Object[] { Component.translationArg(tag.location()), state.getBlock().getName() }));
/*      */   }
/*      */   
/*      */   public void succeedWhenBlockPresent(Block block, int x, int y, int z) {
/*  550 */     succeedWhenBlockPresent(block, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public void succeedWhenBlockPresent(Block block, BlockPos pos) {
/*  554 */     succeedWhen(() -> assertBlockPresent(block, pos));
/*      */   }
/*      */   
/*      */   public void assertBlock(BlockPos pos, Predicate<Block> predicate, Function<Block, Component> errorMessage) {
/*  560 */     assertBlockState(pos, blockState -> predicate.test(blockState.getBlock()), state -> (Component)errorMessage.apply(state.getBlock()));
/*      */   }
/*      */   
/*      */   public <T extends Comparable<T>> void assertBlockProperty(BlockPos pos, Property<T> property, T value) {
/*  564 */     BlockState blockState = getBlockState(pos);
/*  565 */     boolean hasProperty = blockState.hasProperty(property);
/*  566 */     if (!hasProperty)
/*  567 */       throw assertionException(pos, "test.error.block_property_missing", new Object[] { property.getName(), value }); 
/*  568 */     if (!blockState.getValue(property).equals(value))
/*  569 */       throw assertionException(pos, "test.error.block_property_mismatch", new Object[] { property.getName(), value, blockState.getValue(property) }); 
/*      */   }
/*      */   
/*      */   public <T extends Comparable<T>> void assertBlockProperty(BlockPos pos, Property<T> property, Predicate<T> predicate, Component errorMessage) {
/*  574 */     assertBlockState(pos, blockState -> {
/*      */           if (!blockState.hasProperty(property))
/*      */             return false; 
/*      */           Comparable comparable = blockState.getValue(property);
/*      */           return predicate.test(comparable);
/*      */         }state -> errorMessage);
/*      */   }
/*      */   
/*      */   public void assertBlockState(BlockPos pos, BlockState expected) {
/*  584 */     BlockState blockState = getBlockState(pos);
/*  585 */     if (!blockState.equals(expected))
/*  586 */       throw assertionException(pos, "test.error.state_not_equal", new Object[] { expected, blockState }); 
/*      */   }
/*      */   
/*      */   public void assertBlockState(BlockPos pos, Predicate<BlockState> predicate, Function<BlockState, Component> errorMessage) {
/*  591 */     BlockState blockState = getBlockState(pos);
/*  592 */     if (!predicate.test(blockState))
/*  593 */       throw assertionException(pos, (Component)errorMessage.apply(blockState)); 
/*      */   }
/*      */   
/*      */   public <T extends BlockEntity> void assertBlockEntityData(BlockPos pos, Class<T> type, Predicate<T> predicate, Supplier<Component> errorMessage) {
/*  598 */     T blockEntity = getBlockEntity(pos, type);
/*  599 */     if (!predicate.test(blockEntity))
/*  600 */       throw assertionException(pos, (Component)errorMessage.get()); 
/*      */   }
/*      */   
/*      */   public void assertRedstoneSignal(BlockPos pos, Direction direction, IntPredicate levelPredicate, Supplier<Component> errorMessage) {
/*  605 */     BlockPos blockPos = absolutePos(pos);
/*  606 */     ServerLevel level = getLevel();
/*  607 */     BlockState blockState = level.getBlockState(blockPos);
/*  608 */     int signal = blockState.getSignal((BlockGetter)level, blockPos, direction);
/*  609 */     if (!levelPredicate.test(signal))
/*  610 */       throw assertionException(pos, (Component)errorMessage.get()); 
/*      */   }
/*      */   
/*      */   public void assertEntityPresent(EntityType<?> entityType) {
/*  617 */     if (!getLevel().hasEntities((EntityTypeTest)entityType, getBounds(), Entity::isAlive))
/*  618 */       throw assertionException("test.error.expected_entity_in_test", new Object[] { entityType.getDescription() }); 
/*      */   }
/*      */   
/*      */   public void assertEntityPresent(EntityType<?> entityType, int x, int y, int z) {
/*  623 */     assertEntityPresent(entityType, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public void assertEntityPresent(EntityType<?> entityType, BlockPos pos) {
/*  627 */     BlockPos absolutePos = absolutePos(pos);
/*  628 */     if (!getLevel().hasEntities((EntityTypeTest)entityType, new AABB(absolutePos), Entity::isAlive))
/*  629 */       throw assertionException(pos, "test.error.expected_entity", new Object[] { entityType.getDescription() }); 
/*      */   }
/*      */   
/*      */   public void assertEntityPresent(EntityType<?> entityType, AABB relativeAABB) {
/*  634 */     AABB absoluteAABB = absoluteAABB(relativeAABB);
/*  635 */     if (!getLevel().hasEntities((EntityTypeTest)entityType, absoluteAABB, Entity::isAlive))
/*  636 */       throw assertionException(BlockPos.containing(relativeAABB.getCenter()), "test.error.expected_entity", new Object[] { entityType.getDescription() }); 
/*      */   }
/*      */   
/*      */   public void assertEntityPresent(EntityType<?> entityType, AABB relativeAABB, Component message) {
/*  641 */     AABB absoluteAABB = absoluteAABB(relativeAABB);
/*  642 */     if (!getLevel().hasEntities((EntityTypeTest)entityType, absoluteAABB, Entity::isAlive))
/*  643 */       throw assertionException(BlockPos.containing(relativeAABB.getCenter()), message); 
/*      */   }
/*      */   
/*      */   public void assertEntitiesPresent(EntityType<?> entityType, int expectedEntities) {
/*  648 */     List<? extends Entity> entities = getLevel().getEntities((EntityTypeTest)entityType, getBounds(), Entity::isAlive);
/*  649 */     if (entities.size() != expectedEntities)
/*  650 */       throw assertionException("test.error.expected_entity_count", new Object[] { Integer.valueOf(expectedEntities), entityType.getDescription(), Integer.valueOf(entities.size()) }); 
/*      */   }
/*      */   
/*      */   public void assertEntitiesPresent(EntityType<?> entityType, BlockPos pos, int numOfExpectedEntities, double distance) {
/*  655 */     BlockPos absolutePos = absolutePos(pos);
/*  656 */     List<? extends Entity> entities = (List)getEntities(entityType, pos, distance);
/*  657 */     if (entities.size() != numOfExpectedEntities)
/*  658 */       throw assertionException(pos, "test.error.expected_entity_count", new Object[] { Integer.valueOf(numOfExpectedEntities), entityType.getDescription(), Integer.valueOf(entities.size()) }); 
/*      */   }
/*      */   
/*      */   public void assertEntityPresent(EntityType<?> entityType, BlockPos pos, double distance) {
/*  663 */     List<? extends Entity> entities = (List)getEntities(entityType, pos, distance);
/*  664 */     if (entities.isEmpty()) {
/*  665 */       BlockPos absolutePos = absolutePos(pos);
/*  666 */       throw assertionException(pos, "test.error.expected_entity", new Object[] { entityType.getDescription() });
/*      */     } 
/*      */   }
/*      */   
/*      */   public <T extends Entity> List<T> getEntities(EntityType<T> entityType, BlockPos pos, double distance) {
/*  671 */     BlockPos absolutePos = absolutePos(pos);
/*  672 */     return getLevel().getEntities((EntityTypeTest)entityType, (new AABB(absolutePos)).inflate(distance), Entity::isAlive);
/*      */   }
/*      */   
/*      */   public <T extends Entity> List<T> getEntities(EntityType<T> entityType) {
/*  676 */     return getLevel().getEntities((EntityTypeTest)entityType, getBounds(), Entity::isAlive);
/*      */   }
/*      */   
/*      */   public void assertEntityInstancePresent(Entity entity, int x, int y, int z) {
/*  680 */     assertEntityInstancePresent(entity, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public void assertEntityInstancePresent(Entity entity, BlockPos pos) {
/*  684 */     assertEntityInstancePresent(entity, pos, 0.0D);
/*      */   }
/*      */   
/*      */   public void assertEntityInstancePresent(Entity entity, BlockPos pos, double inflate) {
/*  688 */     BlockPos absolutePos = absolutePos(pos);
/*  689 */     List<? extends Entity> entities = getLevel().getEntities((EntityTypeTest)entity.getType(), (new AABB(absolutePos)).inflate(inflate), Entity::isAlive);
/*  690 */     entities.stream().filter(it -> (it == entity)).findFirst().orElseThrow(() -> assertionException(pos, "test.error.expected_entity", new Object[] { entity.getType().getDescription() }));
/*      */   }
/*      */   
/*      */   public void assertItemEntityCountIs(Item itemType, BlockPos pos, double distance, int count) {
/*  694 */     BlockPos absolutePos = absolutePos(pos);
/*  695 */     List<ItemEntity> entities = getLevel().getEntities((EntityTypeTest)EntityTypes.ITEM, (new AABB(absolutePos)).inflate(distance), Entity::isAlive);
/*  697 */     int num = 0;
/*  698 */     for (ItemEntity entity : entities) {
/*  699 */       ItemStack itemStack = entity.getItem();
/*  700 */       if (itemStack.is(itemType))
/*  701 */         num += itemStack.getCount(); 
/*      */     } 
/*  705 */     if (num != count)
/*  706 */       throw assertionException(pos, "test.error.expected_items_count", new Object[] { Integer.valueOf(count), getItemName(itemType), Integer.valueOf(num) }); 
/*      */   }
/*      */   
/*      */   public void assertItemEntityPresent(Item itemType, BlockPos pos, double distance) {
/*  711 */     BlockPos absolutePos = absolutePos(pos);
/*  712 */     Predicate<ItemEntity> isSameItem = entity -> (entity.isAlive() && entity.getItem().is(itemType));
/*  713 */     if (!getLevel().hasEntities((EntityTypeTest)EntityTypes.ITEM, (new AABB(absolutePos)).inflate(distance), isSameItem))
/*  714 */       throw assertionException(pos, "test.error.expected_item", new Object[] { getItemName(itemType) }); 
/*      */   }
/*      */   
/*      */   public void assertItemEntityNotPresent(Item itemType, BlockPos pos, double distance) {
/*  719 */     BlockPos absolutePos = absolutePos(pos);
/*  720 */     Predicate<ItemEntity> isSameItem = entity -> (entity.isAlive() && entity.getItem().is(itemType));
/*  721 */     if (getLevel().hasEntities((EntityTypeTest)EntityTypes.ITEM, (new AABB(absolutePos)).inflate(distance), isSameItem))
/*  722 */       throw assertionException(pos, "test.error.unexpected_item", new Object[] { getItemName(itemType) }); 
/*      */   }
/*      */   
/*      */   public void assertItemEntityPresent(Item itemType) {
/*  727 */     Predicate<ItemEntity> isSameItem = entity -> (entity.isAlive() && entity.getItem().is(itemType));
/*  728 */     if (!getLevel().hasEntities((EntityTypeTest)EntityTypes.ITEM, getBounds(), isSameItem))
/*  729 */       throw assertionException("test.error.expected_item", new Object[] { getItemName(itemType) }); 
/*      */   }
/*      */   
/*      */   public void assertItemEntityNotPresent(Item itemType) {
/*  734 */     Predicate<ItemEntity> isSameItem = entity -> (entity.isAlive() && entity.getItem().is(itemType));
/*  735 */     if (getLevel().hasEntities((EntityTypeTest)EntityTypes.ITEM, getBounds(), isSameItem))
/*  736 */       throw assertionException("test.error.unexpected_item", new Object[] { getItemName(itemType) }); 
/*      */   }
/*      */   
/*      */   public void assertEntityNotPresent(EntityType<?> entityType) {
/*  741 */     List<? extends Entity> entities = getLevel().getEntities((EntityTypeTest)entityType, getBounds(), Entity::isAlive);
/*  742 */     if (!entities.isEmpty())
/*  743 */       throw assertionException(((Entity)entities.getFirst()).blockPosition(), "test.error.unexpected_entity", new Object[] { entityType.getDescription() }); 
/*      */   }
/*      */   
/*      */   public void assertEntityNotPresent(EntityType<?> entityType, int x, int y, int z) {
/*  748 */     assertEntityNotPresent(entityType, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public void assertEntityNotPresent(EntityType<?> entityType, BlockPos pos) {
/*  752 */     BlockPos absolutePos = absolutePos(pos);
/*  753 */     if (getLevel().hasEntities((EntityTypeTest)entityType, new AABB(absolutePos), Entity::isAlive))
/*  754 */       throw assertionException(pos, "test.error.unexpected_entity", new Object[] { entityType.getDescription() }); 
/*      */   }
/*      */   
/*      */   public void assertEntityNotPresent(EntityType<?> entityType, AABB relativeAABB) {
/*  759 */     AABB absoluteAABB = absoluteAABB(relativeAABB);
/*  760 */     List<? extends Entity> entities = getLevel().getEntities((EntityTypeTest)entityType, absoluteAABB, Entity::isAlive);
/*  761 */     if (!entities.isEmpty())
/*  762 */       throw assertionException(((Entity)entities.getFirst()).blockPosition(), "test.error.unexpected_entity", new Object[] { entityType.getDescription() }); 
/*      */   }
/*      */   
/*      */   public void assertEntityTouching(EntityType<?> entityType, double x, double y, double z) {
/*  767 */     Vec3 vec = new Vec3(x, y, z);
/*  768 */     Vec3 absoluteVec = absoluteVec(vec);
/*  769 */     Predicate<? super Entity> predicate = e -> e.getBoundingBox().intersects(absoluteVec, absoluteVec);
/*  770 */     if (!getLevel().hasEntities((EntityTypeTest)entityType, getBounds(), predicate))
/*  771 */       throw assertionException("test.error.expected_entity_touching", new Object[] { entityType.getDescription(), Double.valueOf(absoluteVec.x()), Double.valueOf(absoluteVec.y()), Double.valueOf(absoluteVec.z()), Double.valueOf(x), Double.valueOf(y), Double.valueOf(z) }); 
/*      */   }
/*      */   
/*      */   public void assertEntityNotTouching(EntityType<?> entityType, double x, double y, double z) {
/*  776 */     Vec3 vec = new Vec3(x, y, z);
/*  777 */     Vec3 absoluteVec = absoluteVec(vec);
/*  778 */     Predicate<? super Entity> predicate = e -> !e.getBoundingBox().intersects(absoluteVec, absoluteVec);
/*  779 */     if (!getLevel().hasEntities((EntityTypeTest)entityType, getBounds(), predicate))
/*  780 */       throw assertionException("test.error.expected_entity_not_touching", new Object[] { entityType.getDescription(), Double.valueOf(absoluteVec.x()), Double.valueOf(absoluteVec.y()), Double.valueOf(absoluteVec.z()), Double.valueOf(x), Double.valueOf(y), Double.valueOf(z) }); 
/*      */   }
/*      */   
/*      */   public <E extends Entity, T> void assertEntityData(BlockPos pos, EntityType<E> entityType, Predicate<E> test) {
/*  785 */     BlockPos absolutePos = absolutePos(pos);
/*  786 */     List<E> entities = getLevel().getEntities((EntityTypeTest)entityType, new AABB(absolutePos), Entity::isAlive);
/*  787 */     if (entities.isEmpty())
/*  788 */       throw assertionException(pos, "test.error.expected_entity", new Object[] { entityType.getDescription() }); 
/*  791 */     for (Entity entity : entities) {
/*  792 */       if (!test.test((E)entity))
/*  793 */         throw assertionException(entity.blockPosition(), "test.error.expected_entity_data_predicate", new Object[] { entity.getName() }); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public <E extends Entity, T> void assertEntityData(BlockPos pos, EntityType<E> entityType, Function<? super E, T> dataAccessor, T data) {
/*  799 */     assertEntityData(new AABB(pos), entityType, dataAccessor, data);
/*      */   }
/*      */   
/*      */   public <E extends Entity, T> void assertEntityData(AABB box, EntityType<E> entityType, Function<? super E, T> dataAccessor, T data) {
/*  803 */     List<E> entities = getLevel().getEntities((EntityTypeTest)entityType, absoluteAABB(box), Entity::isAlive);
/*  804 */     if (entities.isEmpty())
/*  805 */       throw assertionException(BlockPos.containing(box.getBottomCenter()), "test.error.expected_entity", new Object[] { entityType.getDescription() }); 
/*  808 */     for (Entity entity : entities) {
/*  809 */       T actual = dataAccessor.apply((E)entity);
/*  811 */       if (!Objects.equals(actual, data))
/*  812 */         throw assertionException(BlockPos.containing(box.getBottomCenter()), "test.error.expected_entity_data", new Object[] { data, actual }); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public <E extends LivingEntity> void assertEntityIsHolding(BlockPos pos, EntityType<E> entityType, Item item) {
/*  818 */     BlockPos absolutePos = absolutePos(pos);
/*  820 */     List<E> entities = getLevel().getEntities((EntityTypeTest)entityType, new AABB(absolutePos), Entity::isAlive);
/*  821 */     if (entities.isEmpty())
/*  822 */       throw assertionException(pos, "test.error.expected_entity", new Object[] { entityType.getDescription() }); 
/*  825 */     for (LivingEntity livingEntity : entities) {
/*  826 */       if (livingEntity.isHolding(item))
/*      */         return; 
/*      */     } 
/*  831 */     throw assertionException(pos, "test.error.expected_entity_holding", new Object[] { getItemName(item) });
/*      */   }
/*      */   
/*      */   public <E extends Entity & InventoryCarrier> void assertEntityInventoryContains(BlockPos pos, EntityType<E> entityType, Item item) {
/*  835 */     BlockPos absolutePos = absolutePos(pos);
/*  837 */     List<E> entities = getLevel().getEntities((EntityTypeTest)entityType, new AABB(absolutePos), rec$ -> rec$.isAlive());
/*  838 */     if (entities.isEmpty())
/*  839 */       throw assertionException(pos, "test.error.expected_entity", new Object[] { entityType.getDescription() }); 
/*  842 */     for (Entity entity : entities) {
/*  843 */       if (((InventoryCarrier)entity).getInventory().hasAnyMatching(itemStack -> itemStack.is(item)))
/*      */         return; 
/*      */     } 
/*  848 */     throw assertionException(pos, "test.error.expected_entity_having", new Object[] { getItemName(item) });
/*      */   }
/*      */   
/*      */   public void assertContainerEmpty(BlockPos pos) {
/*  852 */     BaseContainerBlockEntity container = getBlockEntity(pos, BaseContainerBlockEntity.class);
/*  853 */     if (!container.isEmpty())
/*  854 */       throw assertionException(pos, "test.error.expected_empty_container", new Object[0]); 
/*      */   }
/*      */   
/*      */   public void assertContainerContainsSingle(BlockPos pos, Item item) {
/*  859 */     BaseContainerBlockEntity container = getBlockEntity(pos, BaseContainerBlockEntity.class);
/*  860 */     if (container.countItem(item) != 1)
/*  861 */       throw assertionException(pos, "test.error.expected_container_contents_single", new Object[] { getItemName(item) }); 
/*      */   }
/*      */   
/*      */   public void assertContainerContains(BlockPos pos, Item item) {
/*  866 */     BaseContainerBlockEntity container = getBlockEntity(pos, BaseContainerBlockEntity.class);
/*  867 */     if (container.countItem(item) == 0)
/*  868 */       throw assertionException(pos, "test.error.expected_container_contents", new Object[] { getItemName(item) }); 
/*      */   }
/*      */   
/*      */   public void assertSameBlockStates(BoundingBox sourceBoundingBox, BlockPos targetBoundingBoxCorner) {
/*  874 */     BlockPos.betweenClosedStream(sourceBoundingBox)
/*  875 */       .forEach(sourcePos -> {
/*      */           BlockPos targetPos = targetBoundingBoxCorner.offset(sourcePos.getX() - sourceBoundingBox.minX(), sourcePos.getY() - sourceBoundingBox.minY(), sourcePos.getZ() - sourceBoundingBox.minZ());
/*      */           assertSameBlockState(sourcePos, targetPos);
/*      */         });
/*      */   }
/*      */   
/*      */   public void assertSameBlockState(BlockPos sourcePos, BlockPos targetPos) {
/*  882 */     BlockState sourceState = getBlockState(sourcePos);
/*  883 */     BlockState targetState = getBlockState(targetPos);
/*  884 */     if (sourceState != targetState)
/*  885 */       throw assertionException(sourcePos, "test.error.state_not_equal", new Object[] { targetState, sourceState }); 
/*      */   }
/*      */   
/*      */   public void assertAtTickTimeContainerContains(long time, BlockPos pos, Item item) {
/*  890 */     runAtTickTime(time, () -> assertContainerContainsSingle(pos, item));
/*      */   }
/*      */   
/*      */   public void assertAtTickTimeContainerEmpty(long time, BlockPos pos) {
/*  894 */     runAtTickTime(time, () -> assertContainerEmpty(pos));
/*      */   }
/*      */   
/*      */   public <E extends Entity, T> void succeedWhenEntityData(BlockPos pos, EntityType<E> entityType, Function<E, T> dataAccessor, T data) {
/*  898 */     succeedWhen(() -> assertEntityData(pos, entityType, dataAccessor, data));
/*      */   }
/*      */   
/*      */   public <E extends Entity> void assertEntityProperty(E entity, Predicate<E> test, Component description) {
/*  902 */     if (!test.test(entity))
/*  903 */       throw assertionException(entity.blockPosition(), "test.error.entity_property", new Object[] { entity.getName(), description }); 
/*      */   }
/*      */   
/*      */   public <E extends Entity, T> void assertEntityProperty(E entity, Function<E, T> test, T expected, Component description) {
/*  908 */     T actual = test.apply(entity);
/*  909 */     if (!actual.equals(expected))
/*  910 */       throw assertionException(entity.blockPosition(), "test.error.entity_property_details", new Object[] { entity.getName(), description, actual, expected }); 
/*      */   }
/*      */   
/*      */   public void assertLivingEntityHasMobEffect(LivingEntity entity, Holder<MobEffect> mobEffect, int amplifier) {
/*  915 */     MobEffectInstance mobEffectInstance = entity.getEffect(mobEffect);
/*  916 */     if (mobEffectInstance == null || mobEffectInstance.getAmplifier() != amplifier)
/*  917 */       throw assertionException("test.error.expected_entity_effect", new Object[] { entity.getName(), PotionContents.getPotionDescription(mobEffect, amplifier) }); 
/*      */   }
/*      */   
/*      */   public void succeedWhenEntityPresent(EntityType<?> entityType, int x, int y, int z) {
/*  922 */     succeedWhenEntityPresent(entityType, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public void succeedWhenEntityPresent(EntityType<?> entityType, BlockPos pos) {
/*  926 */     succeedWhen(() -> assertEntityPresent(entityType, pos));
/*      */   }
/*      */   
/*      */   public void succeedWhenEntityNotPresent(EntityType<?> entityType, int x, int y, int z) {
/*  932 */     succeedWhenEntityNotPresent(entityType, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public void succeedWhenEntityNotPresent(EntityType<?> entityType, BlockPos pos) {
/*  936 */     succeedWhen(() -> assertEntityNotPresent(entityType, pos));
/*      */   }
/*      */   
/*      */   public void succeed() {
/*  944 */     this.testInfo.succeed();
/*      */   }
/*      */   
/*      */   private void ensureSingleFinalCheck() {
/*  948 */     if (this.finalCheckAdded)
/*  949 */       throw new IllegalStateException("This test already has final clause"); 
/*  951 */     this.finalCheckAdded = true;
/*      */   }
/*      */   
/*      */   public void succeedIf(Runnable asserter) {
/*  955 */     ensureSingleFinalCheck();
/*  956 */     this.testInfo.createSequence()
/*  957 */       .thenWaitUntil(0L, asserter)
/*  958 */       .thenSucceed();
/*      */   }
/*      */   
/*      */   public void succeedWhen(Runnable asserter) {
/*  962 */     ensureSingleFinalCheck();
/*  963 */     this.testInfo.createSequence()
/*  964 */       .thenWaitUntil(asserter)
/*  965 */       .thenSucceed();
/*      */   }
/*      */   
/*      */   public void succeedOnTickWhen(int tick, Runnable asserter) {
/*  969 */     ensureSingleFinalCheck();
/*  970 */     this.testInfo.createSequence()
/*  971 */       .thenWaitUntil(tick, asserter)
/*  972 */       .thenSucceed();
/*      */   }
/*      */   
/*      */   public void runAtTickTime(long time, Runnable asserter) {
/*  976 */     this.testInfo.setRunAtTickTime(time, asserter);
/*      */   }
/*      */   
/*      */   public void runBeforeTestEnd(Runnable asserter) {
/*  980 */     runAtTickTime((this.testInfo.getTimeoutTicks() - 1), asserter);
/*      */   }
/*      */   
/*      */   public void runAfterDelay(long ticksToDelay, Runnable whatToRun) {
/*  984 */     runAtTickTime(this.testInfo.getTick() + ticksToDelay, whatToRun);
/*      */   }
/*      */   
/*      */   public void setTime(long ticks) {
/*  988 */     ServerLevel level = getLevel();
/*  989 */     Holder<WorldClock> clock = level.dimensionType().defaultClock().orElseThrow();
/*  990 */     level.clockManager().setTotalTicks(clock, ticks);
/*      */   }
/*      */   
/*      */   public void randomTick(BlockPos pos) {
/*  994 */     BlockPos absolutePos = absolutePos(pos);
/*  995 */     ServerLevel level = getLevel();
/*  996 */     level.getBlockState(absolutePos).randomTick(level, absolutePos, level.getRandom());
/*      */   }
/*      */   
/*      */   public void tickBlock(BlockPos pos) {
/* 1000 */     BlockPos absolutePos = absolutePos(pos);
/* 1001 */     ServerLevel level = getLevel();
/* 1002 */     level.getBlockState(absolutePos).tick(level, absolutePos, level.getRandom());
/*      */   }
/*      */   
/*      */   public void tickPrecipitation(BlockPos pos) {
/* 1006 */     BlockPos absolutePos = absolutePos(pos);
/* 1007 */     ServerLevel level = getLevel();
/* 1008 */     level.tickPrecipitation(absolutePos);
/*      */   }
/*      */   
/*      */   public void tickPrecipitation() {
/* 1012 */     AABB aabb = getRelativeBounds();
/* 1013 */     int maxX = (int)Math.floor(aabb.maxX);
/* 1014 */     int maxZ = (int)Math.floor(aabb.maxZ);
/* 1015 */     int maxY = (int)Math.floor(aabb.maxY);
/* 1016 */     for (int x = (int)Math.floor(aabb.minX); x < maxX; x++) {
/* 1017 */       for (int z = (int)Math.floor(aabb.minZ); z < maxZ; z++)
/* 1018 */         tickPrecipitation(new BlockPos(x, maxY, z)); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getHeight(Heightmap.Types heightmap, int x, int z) {
/* 1024 */     BlockPos absolutePos = absolutePos(new BlockPos(x, 0, z));
/* 1025 */     return relativePos(getLevel().getHeightmapPos(heightmap, absolutePos)).getY();
/*      */   }
/*      */   
/*      */   public void fail(Component message, BlockPos pos) {
/* 1029 */     throw assertionException(pos, message);
/*      */   }
/*      */   
/*      */   public void fail(Component message, Entity entity) {
/* 1033 */     throw assertionException(entity.blockPosition(), message);
/*      */   }
/*      */   
/*      */   public void fail(Component message) {
/* 1037 */     throw assertionException(message);
/*      */   }
/*      */   
/*      */   public void fail(String message) {
/* 1041 */     throw assertionException(Component.literal(message));
/*      */   }
/*      */   
/*      */   public void failIf(Runnable asserter) {
/* 1045 */     this.testInfo.createSequence()
/* 1046 */       .thenWaitUntil(asserter)
/* 1047 */       .thenFail(() -> assertionException("test.error.fail", new Object[0]));
/*      */   }
/*      */   
/*      */   public void failIfEver(Runnable asserter) {
/* 1051 */     LongStream.range(this.testInfo.getTick(), this.testInfo.getTimeoutTicks())
/* 1052 */       .forEach(i -> {
/*      */           Objects.requireNonNull(asserter);
/*      */           this.testInfo.setRunAtTickTime(i, asserter::run);
/*      */         });
/*      */   }
/*      */   
/*      */   public GameTestSequence startSequence() {
/* 1056 */     return this.testInfo.createSequence();
/*      */   }
/*      */   
/*      */   public BlockPos absolutePos(BlockPos relativePos) {
/* 1060 */     BlockPos testPos = this.testInfo.getTestOrigin();
/* 1061 */     BlockPos absolutePosBeforeTransform = testPos.offset((Vec3i)relativePos);
/* 1062 */     return StructureTemplate.transform(absolutePosBeforeTransform, Mirror.NONE, this.testInfo.getRotation(), testPos);
/*      */   }
/*      */   
/*      */   public BlockPos relativePos(BlockPos absolutePos) {
/* 1066 */     BlockPos testPos = this.testInfo.getTestOrigin();
/* 1067 */     Rotation inverseRotation = this.testInfo.getRotation().getRotated(Rotation.CLOCKWISE_180);
/* 1068 */     BlockPos absolutePosBeforeTransform = StructureTemplate.transform(absolutePos, Mirror.NONE, inverseRotation, testPos);
/* 1069 */     return absolutePosBeforeTransform.subtract((Vec3i)testPos);
/*      */   }
/*      */   
/*      */   public AABB absoluteAABB(AABB relativeAABB) {
/* 1073 */     Vec3 min = absoluteVec(relativeAABB.getMinPosition());
/* 1074 */     Vec3 max = absoluteVec(relativeAABB.getMaxPosition());
/* 1075 */     return new AABB(min, max);
/*      */   }
/*      */   
/*      */   public AABB relativeAABB(AABB absoluteAABB) {
/* 1079 */     Vec3 min = relativeVec(absoluteAABB.getMinPosition());
/* 1080 */     Vec3 max = relativeVec(absoluteAABB.getMaxPosition());
/* 1081 */     return new AABB(min, max);
/*      */   }
/*      */   
/*      */   public Vec3 absoluteVec(Vec3 relativeVec) {
/* 1085 */     Vec3 testPosVec = Vec3.atLowerCornerOf((Vec3i)this.testInfo.getTestOrigin());
/* 1086 */     return StructureTemplate.transform(testPosVec.add(relativeVec), Mirror.NONE, this.testInfo.getRotation(), this.testInfo.getTestOrigin());
/*      */   }
/*      */   
/*      */   public Vec3 relativeVec(Vec3 absoluteVec) {
/* 1090 */     Vec3 testPosVec = Vec3.atLowerCornerOf((Vec3i)this.testInfo.getTestOrigin());
/* 1091 */     return StructureTemplate.transform(absoluteVec.subtract(testPosVec), Mirror.NONE, this.testInfo.getRotation(), this.testInfo.getTestOrigin());
/*      */   }
/*      */   
/*      */   public Rotation getTestRotation() {
/* 1095 */     return this.testInfo.getRotation();
/*      */   }
/*      */   
/*      */   public Direction getTestDirection() {
/* 1099 */     return this.testInfo.getRotation().rotate(Direction.SOUTH);
/*      */   }
/*      */   
/*      */   public Direction getAbsoluteDirection(Direction direction) {
/* 1103 */     return getTestRotation().rotate(direction);
/*      */   }
/*      */   
/*      */   public void assertTrue(boolean condition, Component errorMessage) {
/* 1107 */     if (!condition)
/* 1108 */       throw assertionException(errorMessage); 
/*      */   }
/*      */   
/*      */   public void assertTrue(boolean condition, String errorMessage) {
/* 1113 */     assertTrue(condition, (Component)Component.literal(errorMessage));
/*      */   }
/*      */   
/*      */   public <N> void assertValueEqual(N value, N expected, String valueName) {
/* 1117 */     assertValueEqual(value, expected, (Component)Component.literal(valueName));
/*      */   }
/*      */   
/*      */   public <N> void assertValueEqual(N value, N expected, Component valueName) {
/* 1121 */     if (!value.equals(expected))
/* 1122 */       throw assertionException("test.error.value_not_equal", new Object[] { valueName, expected, value }); 
/*      */   }
/*      */   
/*      */   public <N extends Comparable<N>> void assertValueInBetween(N lowerBound, N value, N upperBound, String valueName) {
/* 1127 */     assertValueInBetween(lowerBound, value, upperBound, (Component)Component.literal(valueName));
/*      */   }
/*      */   
/*      */   public <N extends Comparable<N>> void assertValueInBetween(N lowerBound, N value, N upperBound, Component valueName) {
/* 1131 */     if (value.compareTo(lowerBound) < 0 || value.compareTo(upperBound) > 0)
/* 1132 */       throw assertionException("test.error.value_not_in_between", new Object[] { valueName, lowerBound, upperBound, value }); 
/*      */   }
/*      */   
/*      */   public void assertFalse(boolean condition, Component errorMessage) {
/* 1137 */     assertTrue(!condition, errorMessage);
/*      */   }
/*      */   
/*      */   public void assertFalse(boolean condition, String errorMessage) {
/* 1141 */     assertFalse(condition, (Component)Component.literal(errorMessage));
/*      */   }
/*      */   
/*      */   public long getTick() {
/* 1147 */     return this.testInfo.getTick();
/*      */   }
/*      */   
/*      */   public AABB getBounds() {
/* 1151 */     return this.testInfo.getStructureBounds();
/*      */   }
/*      */   
/*      */   public AABB getBoundsWithPadding() {
/* 1155 */     return getBounds().inflate(this.testInfo.getTest().padding());
/*      */   }
/*      */   
/*      */   public AABB getRelativeBounds() {
/* 1159 */     AABB absolute = this.testInfo.getStructureBounds();
/* 1160 */     Rotation rotation = this.testInfo.getRotation();
/* 1161 */     switch (rotation) {
/*      */       case COUNTERCLOCKWISE_90:
/*      */       case CLOCKWISE_90:
/*      */       
/*      */     } 
/* 1161 */     return 
/*      */       
/* 1163 */       new AABB(0.0D, 0.0D, 0.0D, absolute.getXsize(), absolute.getYsize(), absolute.getZsize());
/*      */   }
/*      */   
/*      */   public void forEveryBlockInStructure(Consumer<BlockPos> forBlock) {
/* 1168 */     AABB aabb = getRelativeBounds().contract(1.0D, 1.0D, 1.0D);
/* 1169 */     BlockPos.MutableBlockPos.betweenClosedStream(aabb).forEach(forBlock);
/*      */   }
/*      */   
/*      */   public void onEachTick(Runnable action) {
/* 1173 */     LongStream.range(this.testInfo.getTick(), this.testInfo.getTimeoutTicks()).forEach(i -> {
/*      */           Objects.requireNonNull(action);
/*      */           this.testInfo.setRunAtTickTime(i, action::run);
/*      */         });
/*      */   }
/*      */   
/*      */   public void placeAt(Player player, ItemStack blockStack, BlockPos pos, Direction face) {
/* 1179 */     BlockPos absolute = absolutePos(pos.relative(face));
/* 1180 */     BlockHitResult hitResult = new BlockHitResult(Vec3.atCenterOf((Vec3i)absolute), face, absolute, false);
/* 1181 */     UseOnContext context = new UseOnContext(player, InteractionHand.MAIN_HAND, hitResult);
/* 1182 */     blockStack.useOn(context);
/*      */   }
/*      */   
/*      */   public void setBiome(ResourceKey<Biome> biome) {
/* 1187 */     AABB bounds = getBoundsWithPadding();
/* 1188 */     BlockPos low = BlockPos.containing(bounds.minX, bounds.minY, bounds.minZ);
/* 1189 */     BlockPos high = BlockPos.containing(bounds.maxX, bounds.maxY, bounds.maxZ);
/* 1190 */     Either<Integer, CommandSyntaxException> result = FillBiomeCommand.fill(getLevel(), low, high, (Holder)getLevel().registryAccess().lookupOrThrow(Registries.BIOME).getOrThrow(biome));
/* 1191 */     if (result.right().isPresent())
/* 1192 */       throw assertionException("test.error.set_biome", new Object[0]); 
/*      */   }
/*      */   
/*      */   private static Component getItemName(Item itemType) {
/* 1197 */     return (Component)itemType.components().getOrDefault(DataComponents.ITEM_NAME, CommonComponents.EMPTY);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestHelper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */