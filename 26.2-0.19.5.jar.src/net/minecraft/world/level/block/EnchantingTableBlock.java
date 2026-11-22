/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.SimpleMenuProvider;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*     */ import net.minecraft.world.inventory.EnchantmentMenu;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class EnchantingTableBlock extends BaseEntityBlock {
/*  32 */   public static final MapCodec<EnchantingTableBlock> CODEC = simpleCodec(EnchantingTableBlock::new);
/*     */   
/*     */   public static final List<BlockPos> BOOKSHELF_OFFSETS;
/*     */   
/*     */   public MapCodec<EnchantingTableBlock> codec() {
/*  36 */     return CODEC;
/*     */   }
/*     */   
/*     */   static {
/*  42 */     BOOKSHELF_OFFSETS = BlockPos.betweenClosedStream(-2, 0, -2, 2, 1, 2).filter(pos -> (Math.abs(pos.getX()) == 2 || Math.abs(pos.getZ()) == 2)).map(BlockPos::immutable).toList();
/*     */   }
/*     */   
/*  44 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 12.0D);
/*     */   
/*     */   protected EnchantingTableBlock(BlockBehaviour.Properties properties) {
/*  47 */     super(properties);
/*     */   }
/*     */   
/*     */   public static boolean isValidBookShelf(Level level, BlockPos pos, BlockPos offset) {
/*  51 */     return (level.getBlockState(pos.offset((Vec3i)offset)).is(BlockTags.ENCHANTMENT_POWER_PROVIDER) && level.getBlockState(pos.offset(offset.getX() / 2, offset.getY(), offset.getZ() / 2)).is(BlockTags.ENCHANTMENT_POWER_TRANSMITTER));
/*     */   }
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState state) {
/*  56 */     return true;
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  61 */     return SHAPE;
/*     */   }
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/*  66 */     super.animateTick(state, level, pos, random);
/*  68 */     for (BlockPos offset : BOOKSHELF_OFFSETS) {
/*  69 */       if (random.nextInt(16) == 0 && isValidBookShelf(level, pos, offset))
/*  70 */         level.addParticle((ParticleOptions)ParticleTypes.ENCHANT, pos.getX() + 0.5D, pos.getY() + 2.0D, pos.getZ() + 0.5D, (offset.getX() + random.nextFloat()) - 0.5D, (offset.getY() - random.nextFloat() - 1.0F), (offset.getZ() + random.nextFloat()) - 0.5D); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  77 */     return (BlockEntity)new EnchantingTableBlockEntity(worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/*  82 */     return level.isClientSide() ? createTickerHelper(type, BlockEntityTypes.ENCHANTING_TABLE, EnchantingTableBlockEntity::bookAnimationTick) : null;
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  87 */     if (!level.isClientSide())
/*  88 */       player.openMenu(state.getMenuProvider(level, pos)); 
/*  90 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
/*  95 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  96 */     if (blockEntity instanceof EnchantingTableBlockEntity) {
/*  96 */       EnchantingTableBlockEntity enchantingTable = (EnchantingTableBlockEntity)blockEntity;
/*  97 */       Component title = enchantingTable.getDisplayName();
/*  99 */       return (MenuProvider)new SimpleMenuProvider((containerId, inventory, player) -> new EnchantmentMenu(containerId, inventory, ContainerLevelAccess.create(level, pos)), title);
/*     */     } 
/* 101 */     return null;
/*     */   }
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 106 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\EnchantingTableBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */