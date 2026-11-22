/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.item.PrimedTnt;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class TntBlock extends Block {
/*  33 */   public static final MapCodec<TntBlock> CODEC = simpleCodec(TntBlock::new);
/*     */   
/*     */   public MapCodec<TntBlock> codec() {
/*  37 */     return CODEC;
/*     */   }
/*     */   
/*  40 */   public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;
/*     */   
/*     */   public TntBlock(BlockBehaviour.Properties properties) {
/*  43 */     super(properties);
/*  44 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)UNSTABLE, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/*  49 */     if (oldState.is(state.getBlock()))
/*     */       return; 
/*  52 */     if (level.hasNeighborSignal(pos) && 
/*  53 */       prime(level, pos))
/*  54 */       level.removeBlock(pos, false); 
/*     */   }
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/*  61 */     if (level.hasNeighborSignal(pos) && 
/*  62 */       prime(level, pos))
/*  63 */       level.removeBlock(pos, false); 
/*     */   }
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/*  70 */     if (!level.isClientSide() && !(player.getAbilities()).instabuild && ((Boolean)state.getValue((Property)UNSTABLE)).booleanValue())
/*  71 */       prime(level, pos); 
/*  74 */     return super.playerWillDestroy(level, pos, state, player);
/*     */   }
/*     */   
/*     */   public void wasExploded(ServerLevel level, BlockPos pos, Explosion explosion) {
/*  79 */     if (!((Boolean)level.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue())
/*     */       return; 
/*  83 */     PrimedTnt primed = new PrimedTnt((Level)level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, explosion.getIndirectSourceEntity());
/*  84 */     primed.setFuse(PrimedTnt.getRandomShortFuse(primed.getFuse(), level.getRandom()));
/*  85 */     level.addFreshEntity((Entity)primed);
/*     */   }
/*     */   
/*     */   public static boolean prime(Level level, BlockPos pos) {
/*  89 */     return prime(level, pos, (LivingEntity)null);
/*     */   }
/*     */   
/*     */   private static boolean prime(Level level, BlockPos pos, LivingEntity source) {
/*  93 */     if (level instanceof ServerLevel) {
/*  93 */       ServerLevel serverLevel = (ServerLevel)level;
/*  93 */       if (((Boolean)serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/*  97 */         PrimedTnt tnt = new PrimedTnt(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, source);
/*  98 */         level.addFreshEntity((Entity)tnt);
/*  99 */         level.playSound(null, tnt.getX(), tnt.getY(), tnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 100 */         level.gameEvent((Entity)source, (Holder)GameEvent.PRIME_FUSE, pos);
/* 101 */         return true;
/*     */       } 
/*     */     } 
/*     */     return false;
/*     */   }
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/* 106 */     if (!itemStack.is(Items.FLINT_AND_STEEL) && !itemStack.is(Items.FIRE_CHARGE))
/* 107 */       return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult); 
/* 110 */     if (prime(level, pos, (LivingEntity)player)) {
/* 111 */       level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
/* 113 */       Item item = itemStack.getItem();
/* 114 */       if (itemStack.is(Items.FLINT_AND_STEEL)) {
/* 115 */         itemStack.hurtAndBreak(1, (LivingEntity)player, hand.asEquipmentSlot());
/*     */       } else {
/* 117 */         itemStack.consume(1, (LivingEntity)player);
/*     */       } 
/* 119 */       player.awardStat(Stats.ITEM_USED.get(item));
/* 120 */     } else if (level instanceof ServerLevel) {
/* 120 */       ServerLevel serverLevel = (ServerLevel)level;
/* 120 */       if (!((Boolean)serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/* 121 */         player.sendOverlayMessage((Component)Component.translatable("block.minecraft.tnt.disabled"));
/* 122 */         return (InteractionResult)InteractionResult.PASS;
/*     */       } 
/*     */     } 
/* 124 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   protected void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile projectile) {
/* 129 */     if (level instanceof ServerLevel) {
/* 129 */       ServerLevel serverLevel = (ServerLevel)level;
/* 130 */       BlockPos pos = blockHit.getBlockPos();
/* 131 */       Entity owner = projectile.getOwner();
/* 132 */       if (projectile.isOnFire() && projectile.mayInteract(serverLevel, pos)) {
/* 133 */         LivingEntity livingEntity = (LivingEntity)owner;
/* 133 */         if (prime(level, pos, (owner instanceof LivingEntity) ? livingEntity : null))
/* 134 */           level.removeBlock(pos, false); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean dropFromExplosion(Explosion explosion) {
/* 142 */     return false;
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 147 */     builder.add(new Property[] { (Property)UNSTABLE });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\TntBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */