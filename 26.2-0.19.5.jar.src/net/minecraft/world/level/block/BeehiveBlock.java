/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.animal.bee.Bee;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BeehiveBlock extends BaseEntityBlock {
/*  65 */   public static final MapCodec<BeehiveBlock> CODEC = simpleCodec(BeehiveBlock::new);
/*     */   
/*     */   public MapCodec<BeehiveBlock> codec() {
/*  69 */     return CODEC;
/*     */   }
/*     */   
/*  72 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*  73 */   public static final IntegerProperty HONEY_LEVEL = BlockStateProperties.LEVEL_HONEY;
/*     */   
/*     */   public static final int MAX_HONEY_LEVELS = 5;
/*     */   
/*     */   public BeehiveBlock(BlockBehaviour.Properties properties) {
/*  78 */     super(properties);
/*  79 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HONEY_LEVEL, Integer.valueOf(0))).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*     */   }
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState state) {
/*  84 */     return true;
/*     */   }
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/*  89 */     return ((Integer)state.getValue((Property)HONEY_LEVEL)).intValue();
/*     */   }
/*     */   
/*     */   public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack destroyedWith) {
/*  94 */     super.playerDestroy(level, player, pos, state, blockEntity, destroyedWith);
/*  96 */     if (!level.isClientSide() && 
/*  97 */       blockEntity instanceof BeehiveBlockEntity) {
/*  97 */       BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
/*  98 */       if (!EnchantmentHelper.hasTag(destroyedWith, EnchantmentTags.PREVENTS_BEE_SPAWNS_WHEN_MINING)) {
/*  99 */         beehiveBlockEntity.emptyAllLivingFromHive(player, state, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
/* 101 */         Containers.updateNeighboursAfterDestroy(state, level, pos);
/* 103 */         angerNearbyBees(level, pos);
/*     */       } 
/* 106 */       CriteriaTriggers.BEE_NEST_DESTROYED.trigger((ServerPlayer)player, state, destroyedWith, beehiveBlockEntity.getOccupantCount());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/* 113 */     super.onExplosionHit(state, level, pos, explosion, onHit);
/* 114 */     angerNearbyBees((Level)level, pos);
/*     */   }
/*     */   
/*     */   private void angerNearbyBees(Level level, BlockPos pos) {
/* 118 */     AABB areaAroundBeehive = (new AABB(pos)).inflate(8.0D, 6.0D, 8.0D);
/* 119 */     List<Bee> beesToAnger = level.getEntitiesOfClass(Bee.class, areaAroundBeehive);
/* 120 */     if (!beesToAnger.isEmpty()) {
/* 121 */       List<Player> playersToBeAngryAt = level.getEntitiesOfClass(Player.class, areaAroundBeehive);
/* 122 */       if (playersToBeAngryAt.isEmpty())
/*     */         return; 
/* 125 */       for (Bee bee : beesToAnger) {
/* 126 */         if (bee.getTarget() == null) {
/* 127 */           Player angerTarget = (Player)Util.getRandom(playersToBeAngryAt, level.getRandom());
/* 128 */           bee.setTarget((LivingEntity)angerTarget);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void dropHoneycomb(ServerLevel level, ItemStack tool, BlockState blockState, BlockEntity blockEntity, Entity entity, BlockPos pos) {
/* 135 */     dropFromBlockInteractLootTable(level, BuiltInLootTables.HARVEST_BEEHIVE, blockState, blockEntity, (ItemInstance)tool, entity, (serverLevel, stack) -> popResource((Level)serverLevel, pos, stack));
/*     */   }
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*     */     // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: getstatic net/minecraft/world/level/block/BeehiveBlock.HONEY_LEVEL : Lnet/minecraft/world/level/block/state/properties/IntegerProperty;
/*     */     //   4: invokevirtual getValue : (Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;
/*     */     //   7: checkcast java/lang/Integer
/*     */     //   10: invokevirtual intValue : ()I
/*     */     //   13: istore #8
/*     */     //   15: iconst_0
/*     */     //   16: istore #9
/*     */     //   18: iload #8
/*     */     //   20: iconst_5
/*     */     //   21: if_icmplt -> 275
/*     */     //   24: aload_1
/*     */     //   25: invokevirtual getItem : ()Lnet/minecraft/world/item/Item;
/*     */     //   28: astore #10
/*     */     //   30: aload_3
/*     */     //   31: instanceof net/minecraft/server/level/ServerLevel
/*     */     //   34: ifeq -> 127
/*     */     //   37: aload_3
/*     */     //   38: checkcast net/minecraft/server/level/ServerLevel
/*     */     //   41: astore #11
/*     */     //   43: aload_1
/*     */     //   44: getstatic net/minecraft/world/item/Items.SHEARS : Lnet/minecraft/world/item/Item;
/*     */     //   47: invokevirtual is : (Ljava/lang/Object;)Z
/*     */     //   50: ifeq -> 127
/*     */     //   53: aload #11
/*     */     //   55: aload_1
/*     */     //   56: aload_2
/*     */     //   57: aload_3
/*     */     //   58: aload #4
/*     */     //   60: invokevirtual getBlockEntity : (Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;
/*     */     //   63: aload #5
/*     */     //   65: aload #4
/*     */     //   67: invokestatic dropHoneycomb : (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;)V
/*     */     //   70: aload_3
/*     */     //   71: aconst_null
/*     */     //   72: aload #5
/*     */     //   74: invokevirtual getX : ()D
/*     */     //   77: aload #5
/*     */     //   79: invokevirtual getY : ()D
/*     */     //   82: aload #5
/*     */     //   84: invokevirtual getZ : ()D
/*     */     //   87: getstatic net/minecraft/sounds/SoundEvents.BEEHIVE_SHEAR : Lnet/minecraft/sounds/SoundEvent;
/*     */     //   90: getstatic net/minecraft/sounds/SoundSource.BLOCKS : Lnet/minecraft/sounds/SoundSource;
/*     */     //   93: fconst_1
/*     */     //   94: fconst_1
/*     */     //   95: invokevirtual playSound : (Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V
/*     */     //   98: aload_1
/*     */     //   99: iconst_1
/*     */     //   100: aload #5
/*     */     //   102: aload #6
/*     */     //   104: invokevirtual asEquipmentSlot : ()Lnet/minecraft/world/entity/EquipmentSlot;
/*     */     //   107: invokevirtual hurtAndBreak : (ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V
/*     */     //   110: iconst_1
/*     */     //   111: istore #9
/*     */     //   113: aload_3
/*     */     //   114: aload #5
/*     */     //   116: getstatic net/minecraft/world/level/gameevent/GameEvent.SHEAR : Lnet/minecraft/core/Holder$Reference;
/*     */     //   119: aload #4
/*     */     //   121: invokevirtual gameEvent : (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V
/*     */     //   124: goto -> 250
/*     */     //   127: aload_1
/*     */     //   128: getstatic net/minecraft/world/item/Items.GLASS_BOTTLE : Lnet/minecraft/world/item/Item;
/*     */     //   131: invokevirtual is : (Ljava/lang/Object;)Z
/*     */     //   134: ifeq -> 250
/*     */     //   137: aload_1
/*     */     //   138: iconst_1
/*     */     //   139: invokevirtual shrink : (I)V
/*     */     //   142: aload_3
/*     */     //   143: aload #5
/*     */     //   145: aload #5
/*     */     //   147: invokevirtual getX : ()D
/*     */     //   150: aload #5
/*     */     //   152: invokevirtual getY : ()D
/*     */     //   155: aload #5
/*     */     //   157: invokevirtual getZ : ()D
/*     */     //   160: getstatic net/minecraft/sounds/SoundEvents.BOTTLE_FILL : Lnet/minecraft/sounds/SoundEvent;
/*     */     //   163: getstatic net/minecraft/sounds/SoundSource.BLOCKS : Lnet/minecraft/sounds/SoundSource;
/*     */     //   166: fconst_1
/*     */     //   167: fconst_1
/*     */     //   168: invokevirtual playSound : (Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V
/*     */     //   171: aload_1
/*     */     //   172: invokevirtual isEmpty : ()Z
/*     */     //   175: ifeq -> 198
/*     */     //   178: aload #5
/*     */     //   180: aload #6
/*     */     //   182: new net/minecraft/world/item/ItemStack
/*     */     //   185: dup
/*     */     //   186: getstatic net/minecraft/world/item/Items.HONEY_BOTTLE : Lnet/minecraft/world/item/Item;
/*     */     //   189: invokespecial <init> : (Lnet/minecraft/world/level/ItemLike;)V
/*     */     //   192: invokevirtual setItemInHand : (Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V
/*     */     //   195: goto -> 236
/*     */     //   198: aload #5
/*     */     //   200: invokevirtual getInventory : ()Lnet/minecraft/world/entity/player/Inventory;
/*     */     //   203: new net/minecraft/world/item/ItemStack
/*     */     //   206: dup
/*     */     //   207: getstatic net/minecraft/world/item/Items.HONEY_BOTTLE : Lnet/minecraft/world/item/Item;
/*     */     //   210: invokespecial <init> : (Lnet/minecraft/world/level/ItemLike;)V
/*     */     //   213: invokevirtual add : (Lnet/minecraft/world/item/ItemStack;)Z
/*     */     //   216: ifne -> 236
/*     */     //   219: aload #5
/*     */     //   221: new net/minecraft/world/item/ItemStack
/*     */     //   224: dup
/*     */     //   225: getstatic net/minecraft/world/item/Items.HONEY_BOTTLE : Lnet/minecraft/world/item/Item;
/*     */     //   228: invokespecial <init> : (Lnet/minecraft/world/level/ItemLike;)V
/*     */     //   231: iconst_0
/*     */     //   232: invokevirtual drop : (Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/item/ItemEntity;
/*     */     //   235: pop
/*     */     //   236: iconst_1
/*     */     //   237: istore #9
/*     */     //   239: aload_3
/*     */     //   240: aload #5
/*     */     //   242: getstatic net/minecraft/world/level/gameevent/GameEvent.FLUID_PICKUP : Lnet/minecraft/core/Holder$Reference;
/*     */     //   245: aload #4
/*     */     //   247: invokevirtual gameEvent : (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V
/*     */     //   250: aload_3
/*     */     //   251: invokevirtual isClientSide : ()Z
/*     */     //   254: ifne -> 275
/*     */     //   257: iload #9
/*     */     //   259: ifeq -> 275
/*     */     //   262: aload #5
/*     */     //   264: getstatic net/minecraft/stats/Stats.ITEM_USED : Lnet/minecraft/stats/StatType;
/*     */     //   267: aload #10
/*     */     //   269: invokevirtual get : (Ljava/lang/Object;)Lnet/minecraft/stats/Stat;
/*     */     //   272: invokevirtual awardStat : (Lnet/minecraft/stats/Stat;)V
/*     */     //   275: iload #9
/*     */     //   277: ifeq -> 334
/*     */     //   280: aload_3
/*     */     //   281: aload #4
/*     */     //   283: invokestatic isSmokeyPos : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z
/*     */     //   286: ifne -> 322
/*     */     //   289: aload_0
/*     */     //   290: aload_3
/*     */     //   291: aload #4
/*     */     //   293: invokevirtual hiveContainsBees : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z
/*     */     //   296: ifeq -> 306
/*     */     //   299: aload_0
/*     */     //   300: aload_3
/*     */     //   301: aload #4
/*     */     //   303: invokevirtual angerNearbyBees : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V
/*     */     //   306: aload_0
/*     */     //   307: aload_3
/*     */     //   308: aload_2
/*     */     //   309: aload #4
/*     */     //   311: aload #5
/*     */     //   313: getstatic net/minecraft/world/level/block/entity/BeehiveBlockEntity$BeeReleaseStatus.EMERGENCY : Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$BeeReleaseStatus;
/*     */     //   316: invokevirtual releaseBeesAndResetHoneyLevel : (Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$BeeReleaseStatus;)V
/*     */     //   319: goto -> 330
/*     */     //   322: aload_0
/*     */     //   323: aload_3
/*     */     //   324: aload_2
/*     */     //   325: aload #4
/*     */     //   327: invokevirtual resetHoneyLevel : (Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V
/*     */     //   330: getstatic net/minecraft/world/InteractionResult.SUCCESS : Lnet/minecraft/world/InteractionResult$Success;
/*     */     //   333: areturn
/*     */     //   334: aload_0
/*     */     //   335: aload_1
/*     */     //   336: aload_2
/*     */     //   337: aload_3
/*     */     //   338: aload #4
/*     */     //   340: aload #5
/*     */     //   342: aload #6
/*     */     //   344: aload #7
/*     */     //   346: invokespecial useItemOn : (Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;
/*     */     //   349: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #140	-> 0
/*     */     //   #141	-> 15
/*     */     //   #143	-> 18
/*     */     //   #144	-> 24
/*     */     //   #145	-> 30
/*     */     //   #146	-> 53
/*     */     //   #147	-> 70
/*     */     //   #148	-> 98
/*     */     //   #149	-> 110
/*     */     //   #150	-> 113
/*     */     //   #151	-> 127
/*     */     //   #152	-> 137
/*     */     //   #153	-> 142
/*     */     //   #154	-> 171
/*     */     //   #155	-> 178
/*     */     //   #156	-> 198
/*     */     //   #157	-> 219
/*     */     //   #159	-> 236
/*     */     //   #160	-> 239
/*     */     //   #162	-> 250
/*     */     //   #163	-> 262
/*     */     //   #167	-> 275
/*     */     //   #168	-> 280
/*     */     //   #170	-> 289
/*     */     //   #171	-> 299
/*     */     //   #173	-> 306
/*     */     //   #175	-> 322
/*     */     //   #177	-> 330
/*     */     //   #180	-> 334
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   43	84	11	serverLevel	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   30	245	10	item	Lnet/minecraft/world/item/Item;
/*     */     //   0	350	0	this	Lnet/minecraft/world/level/block/BeehiveBlock;
/*     */     //   0	350	1	itemStack	Lnet/minecraft/world/item/ItemStack;
/*     */     //   0	350	2	state	Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   0	350	3	level	Lnet/minecraft/world/level/Level;
/*     */     //   0	350	4	pos	Lnet/minecraft/core/BlockPos;
/*     */     //   0	350	5	player	Lnet/minecraft/world/entity/player/Player;
/*     */     //   0	350	6	hand	Lnet/minecraft/world/InteractionHand;
/*     */     //   0	350	7	hitResult	Lnet/minecraft/world/phys/BlockHitResult;
/*     */     //   15	335	8	honeyLevel	I
/*     */     //   18	332	9	hiveEmptied	Z
/*     */   }
/*     */   
/*     */   private boolean hiveContainsBees(Level level, BlockPos pos) {
/* 184 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 185 */     if (blockEntity instanceof BeehiveBlockEntity) {
/* 185 */       BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
/* 186 */       return !beehiveBlockEntity.isEmpty();
/*     */     } 
/* 189 */     return false;
/*     */   }
/*     */   
/*     */   public void releaseBeesAndResetHoneyLevel(Level level, BlockState state, BlockPos pos, Player player, BeehiveBlockEntity.BeeReleaseStatus beeReleaseStatus) {
/* 193 */     resetHoneyLevel(level, state, pos);
/* 195 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 196 */     if (blockEntity instanceof BeehiveBlockEntity) {
/* 196 */       BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
/* 197 */       beehiveBlockEntity.emptyAllLivingFromHive(player, state, beeReleaseStatus);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resetHoneyLevel(Level level, BlockState state, BlockPos pos) {
/* 202 */     level.setBlock(pos, (BlockState)state.setValue((Property)HONEY_LEVEL, Integer.valueOf(0)), 3);
/*     */   }
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 207 */     if (((Integer)state.getValue((Property)HONEY_LEVEL)).intValue() >= 5)
/* 208 */       for (int i = 0; i < random.nextInt(1) + 1; i++)
/* 209 */         trySpawnDripParticles(level, pos, state);  
/*     */   }
/*     */   
/*     */   private void trySpawnDripParticles(Level level, BlockPos pos, BlockState state) {
/* 215 */     if (!state.getFluidState().isEmpty() || level.getRandom().nextFloat() < 0.3F)
/*     */       return; 
/* 219 */     VoxelShape collisionShape = state.getCollisionShape((BlockGetter)level, pos);
/* 220 */     double topSideHeight = collisionShape.max(Direction.Axis.Y);
/* 221 */     if (topSideHeight >= 1.0D && !state.is(BlockTags.IMPERMEABLE)) {
/* 222 */       double bottomSideHeight = collisionShape.min(Direction.Axis.Y);
/* 223 */       if (bottomSideHeight > 0.0D) {
/* 224 */         spawnParticle(level, pos, collisionShape, pos.getY() + bottomSideHeight - 0.05D);
/*     */       } else {
/* 226 */         BlockPos below = pos.below();
/* 227 */         BlockState belowState = level.getBlockState(below);
/* 228 */         VoxelShape belowShape = belowState.getCollisionShape((BlockGetter)level, below);
/* 229 */         double belowTopSideHeight = belowShape.max(Direction.Axis.Y);
/* 230 */         if ((belowTopSideHeight < 1.0D || !belowState.isCollisionShapeFullBlock((BlockGetter)level, below)) && belowState.getFluidState().isEmpty())
/* 231 */           spawnParticle(level, pos, collisionShape, pos.getY() - 0.05D); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spawnParticle(Level level, BlockPos pos, VoxelShape dripShape, double height) {
/* 238 */     spawnFluidParticle(level, pos.getX() + dripShape.min(Direction.Axis.X), pos
/* 239 */         .getX() + dripShape.max(Direction.Axis.X), pos
/* 240 */         .getZ() + dripShape.min(Direction.Axis.Z), pos
/* 241 */         .getZ() + dripShape.max(Direction.Axis.Z), height);
/*     */   }
/*     */   
/*     */   private void spawnFluidParticle(Level level, double x1, double x2, double z1, double z2, double y) {
/* 246 */     level.addParticle((ParticleOptions)ParticleTypes.DRIPPING_HONEY, Mth.lerp(level.getRandom().nextDouble(), x1, x2), y, Mth.lerp(level.getRandom().nextDouble(), z1, z2), 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 251 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)context.getHorizontalDirection().getOpposite());
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 256 */     builder.add(new Property[] { (Property)HONEY_LEVEL, (Property)FACING });
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 261 */     return (BlockEntity)new BeehiveBlockEntity(worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 266 */     return level.isClientSide() ? null : createTickerHelper(type, BlockEntityTypes.BEEHIVE, BeehiveBlockEntity::serverTick);
/*     */   }
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/* 271 */     if (level instanceof ServerLevel) {
/* 271 */       ServerLevel serverLevel = (ServerLevel)level;
/* 271 */       if (player.preventsBlockDrops() && ((Boolean)serverLevel.getGameRules().get(GameRules.BLOCK_DROPS)).booleanValue()) {
/* 272 */         BlockEntity blockEntity = level.getBlockEntity(pos);
/* 273 */         if (blockEntity instanceof BeehiveBlockEntity) {
/* 273 */           BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
/* 274 */           int honeyLevel = ((Integer)state.getValue((Property)HONEY_LEVEL)).intValue();
/* 275 */           boolean hasBees = !beehiveBlockEntity.isEmpty();
/* 278 */           if (hasBees || honeyLevel > 0) {
/* 279 */             ItemStack itemStack = new ItemStack(this);
/* 280 */             itemStack.applyComponents(beehiveBlockEntity.collectComponents());
/* 283 */             itemStack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with((Property)HONEY_LEVEL, Integer.valueOf(honeyLevel)));
/* 285 */             ItemEntity entity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), itemStack);
/* 286 */             entity.setDefaultPickUpDelay();
/* 287 */             level.addFreshEntity((Entity)entity);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 292 */     return super.playerWillDestroy(level, pos, state, player);
/*     */   }
/*     */   
/*     */   protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
/* 297 */     Entity entity = (Entity)params.getOptionalParameter(LootContextParams.THIS_ENTITY);
/* 300 */     if (entity instanceof net.minecraft.world.entity.item.PrimedTnt || entity instanceof net.minecraft.world.entity.monster.Creeper || entity instanceof net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull || entity instanceof net.minecraft.world.entity.boss.wither.WitherBoss || entity instanceof net.minecraft.world.entity.vehicle.minecart.MinecartTNT) {
/* 306 */       BlockEntity blockEntity = (BlockEntity)params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
/* 307 */       if (blockEntity instanceof BeehiveBlockEntity) {
/* 307 */         BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
/* 308 */         beehiveBlockEntity.emptyAllLivingFromHive(null, state, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
/*     */       } 
/*     */     } 
/* 311 */     return super.getDrops(state, params);
/*     */   }
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 316 */     ItemStack itemStack = super.getCloneItemStack(level, pos, state, includeData);
/* 317 */     if (includeData)
/* 318 */       itemStack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with((Property)HONEY_LEVEL, state.getValue((Property)HONEY_LEVEL))); 
/* 320 */     return itemStack;
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 325 */     if (level.getBlockState(neighbourPos).getBlock() instanceof FireBlock) {
/* 327 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/* 328 */       if (blockEntity instanceof BeehiveBlockEntity) {
/* 328 */         BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
/* 329 */         beehiveBlockEntity.emptyAllLivingFromHive(null, state, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
/*     */       } 
/*     */     } 
/* 332 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   public BlockState rotate(BlockState state, Rotation rotation) {
/* 337 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   public BlockState mirror(BlockState state, Mirror mirror) {
/* 342 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\BeehiveBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */