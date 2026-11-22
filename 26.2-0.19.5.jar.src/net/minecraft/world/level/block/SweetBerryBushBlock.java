/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SweetBerryBushBlock extends VegetationBlock implements BonemealableBlock {
/*  34 */   public static final MapCodec<SweetBerryBushBlock> CODEC = simpleCodec(SweetBerryBushBlock::new);
/*     */   
/*     */   private static final float HURT_SPEED_THRESHOLD = 0.003F;
/*     */   
/*     */   public static final int MAX_AGE = 3;
/*     */   
/*     */   public MapCodec<SweetBerryBushBlock> codec() {
/*  38 */     return CODEC;
/*     */   }
/*     */   
/*  43 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
/*     */   
/*  45 */   private static final VoxelShape SHAPE_SAPLING = Block.column(10.0D, 0.0D, 8.0D);
/*     */   
/*  46 */   private static final VoxelShape SHAPE_GROWING = Block.column(14.0D, 0.0D, 16.0D);
/*     */   
/*     */   public SweetBerryBushBlock(BlockBehaviour.Properties properties) {
/*  49 */     super(properties);
/*  50 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*     */   }
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/*  55 */     return new ItemStack((ItemLike)Items.SWEET_BERRIES);
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  60 */     switch (((Integer)state.getValue((Property)AGE)).intValue()) {
/*     */       case 0:
/*     */       
/*     */       case 3:
/*     */       
/*     */     } 
/*  60 */     return 
/*     */       
/*  63 */       SHAPE_GROWING;
/*     */   }
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState state) {
/*  69 */     return (((Integer)state.getValue((Property)AGE)).intValue() < 3);
/*     */   }
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  74 */     int age = ((Integer)state.getValue((Property)AGE)).intValue();
/*  75 */     if (age < 3 && random.nextInt(5) == 0 && level.getRawBrightness(pos.above(), 0) >= 9) {
/*  76 */       BlockState newState = (BlockState)state.setValue((Property)AGE, Integer.valueOf(age + 1));
/*  77 */       level.setBlock(pos, newState, 2);
/*  78 */       level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/*  84 */     if (!(entity instanceof net.minecraft.world.entity.LivingEntity) || entity.is(EntityTypes.FOX) || entity.is(EntityTypes.BEE))
/*     */       return; 
/*  87 */     entity.makeStuckInBlock(state, new Vec3(0.800000011920929D, 0.75D, 0.800000011920929D));
/*  89 */     if (level instanceof ServerLevel) {
/*  89 */       ServerLevel serverLevel = (ServerLevel)level;
/*  89 */       if (((Integer)state.getValue((Property)AGE)).intValue() != 0) {
/*  96 */         Vec3 movement = entity.isClientAuthoritative() ? entity.getKnownMovement() : entity.oldPosition().subtract(entity.position());
/*  98 */         if (movement.horizontalDistanceSqr() > 0.0D) {
/*  99 */           double xs = Math.abs(movement.x());
/* 100 */           double zs = Math.abs(movement.z());
/* 102 */           if (xs >= 0.003000000026077032D || zs >= 0.003000000026077032D)
/* 103 */             entity.hurtServer(serverLevel, level.damageSources().sweetBerryBush(), 1.0F); 
/*     */         } 
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/* 110 */     int age = ((Integer)state.getValue((Property)AGE)).intValue();
/* 111 */     boolean isMaxAge = (age == 3);
/* 113 */     if (!isMaxAge && itemStack.is(Items.BONE_MEAL))
/* 114 */       return (InteractionResult)InteractionResult.PASS; 
/* 116 */     return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 121 */     if (((Integer)state.getValue((Property)AGE)).intValue() > 1) {
/* 122 */       if (level instanceof ServerLevel) {
/* 122 */         ServerLevel serverLevel = (ServerLevel)level;
/* 123 */         Block.dropFromBlockInteractLootTable(serverLevel, BuiltInLootTables.HARVEST_SWEET_BERRY_BUSH, state, level
/*     */             
/* 127 */             .getBlockEntity(pos), (ItemInstance)null, (Entity)player, (serverlvl, itemStack) -> Block.popResource((Level)serverlvl, pos, itemStack));
/* 132 */         serverLevel.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + serverLevel.getRandom().nextFloat() * 0.4F);
/* 133 */         BlockState newState = (BlockState)state.setValue((Property)AGE, Integer.valueOf(1));
/* 134 */         serverLevel.setBlock(pos, newState, 2);
/* 135 */         serverLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of((Entity)player, newState));
/*     */       } 
/* 137 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 140 */     return super.useWithoutItem(state, level, pos, player, hitResult);
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 145 */     builder.add(new Property[] { (Property)AGE });
/*     */   }
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 150 */     return (((Integer)state.getValue((Property)AGE)).intValue() < 3);
/*     */   }
/*     */   
/*     */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 155 */     return true;
/*     */   }
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 160 */     int newAge = Math.min(3, ((Integer)state.getValue((Property)AGE)).intValue() + 1);
/* 161 */     level.setBlock(pos, (BlockState)state.setValue((Property)AGE, Integer.valueOf(newAge)), 2);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SweetBerryBushBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */