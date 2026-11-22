/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Arrays;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.SignApplicator;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SignText;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class SignBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/*  45 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  47 */   private static final VoxelShape SHAPE = Block.column(8.0D, 0.0D, 16.0D);
/*     */   
/*     */   private final WoodType type;
/*     */   
/*     */   protected SignBlock(WoodType type, BlockBehaviour.Properties properties) {
/*  52 */     super(properties);
/*  53 */     this.type = type;
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  61 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/*  62 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/*  65 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  70 */     return SHAPE;
/*     */   }
/*     */   
/*     */   public boolean isPossibleToRespawnInThis(BlockState state) {
/*  75 */     return true;
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  80 */     return (BlockEntity)new SignBlockEntity(worldPosition, blockState);
/*     */   }
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*     */     SignBlockEntity sign;
/*     */     ServerLevel serverLevel;
/*  85 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  85 */     if (blockEntity instanceof SignBlockEntity) {
/*  85 */       sign = (SignBlockEntity)blockEntity;
/*     */     } else {
/*  86 */       return (InteractionResult)InteractionResult.PASS;
/*     */     } 
/*  89 */     Item item = itemStack.getItem();
/*  89 */     SignApplicator applicator = (SignApplicator)item, signApplicator = (item instanceof SignApplicator) ? applicator : null;
/*  90 */     boolean hasApplicatorToUse = (signApplicator != null && player.mayBuild());
/*  92 */     if (level instanceof ServerLevel) {
/*  92 */       serverLevel = (ServerLevel)level;
/*     */     } else {
/*  93 */       return (hasApplicatorToUse || sign.isWaxed()) ? (InteractionResult)InteractionResult.SUCCESS : (InteractionResult)InteractionResult.CONSUME;
/*     */     } 
/*  96 */     if (!hasApplicatorToUse || sign.isWaxed() || otherPlayerIsEditingSign(player, sign))
/*  97 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND; 
/* 100 */     boolean isFrontText = sign.isFacingFrontText(player);
/* 101 */     if (signApplicator.canApplyToSign(sign.getText(isFrontText), itemStack, player) && signApplicator.tryApplyToSign((Level)serverLevel, sign, isFrontText, itemStack, player)) {
/* 102 */       sign.executeClickCommandsIfPresent(serverLevel, player, pos, isFrontText);
/* 103 */       player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
/* 104 */       serverLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, sign.getBlockPos(), GameEvent.Context.of((Entity)player, sign.getBlockState()));
/* 105 */       itemStack.consume(1, (LivingEntity)player);
/* 106 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 109 */     return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*     */     SignBlockEntity sign;
/*     */     ServerLevel serverLevel;
/* 114 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 114 */     if (blockEntity instanceof SignBlockEntity) {
/* 114 */       sign = (SignBlockEntity)blockEntity;
/*     */     } else {
/* 115 */       return (InteractionResult)InteractionResult.PASS;
/*     */     } 
/* 118 */     if (level instanceof ServerLevel) {
/* 118 */       serverLevel = (ServerLevel)level;
/*     */     } else {
/* 119 */       Util.pauseInIde(new IllegalStateException("Expected to only call this on server"));
/* 120 */       return (InteractionResult)InteractionResult.CONSUME;
/*     */     } 
/* 123 */     boolean isFrontText = sign.isFacingFrontText(player);
/* 125 */     boolean executedClickCommand = sign.executeClickCommandsIfPresent(serverLevel, player, pos, isFrontText);
/* 127 */     if (sign.isWaxed()) {
/* 128 */       serverLevel.playSound(null, sign.getBlockPos(), sign.getSignInteractionFailedSoundEvent(), SoundSource.BLOCKS);
/* 129 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */     } 
/* 132 */     if (executedClickCommand)
/* 133 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER; 
/* 136 */     if (!otherPlayerIsEditingSign(player, sign) && player.mayBuild() && hasEditableText(player, sign, isFrontText)) {
/* 137 */       openTextEdit(player, sign, isFrontText);
/* 138 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */     } 
/* 141 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   private boolean hasEditableText(Player player, SignBlockEntity sign, boolean isFrontText) {
/* 145 */     SignText text = sign.getText(isFrontText);
/* 146 */     return Arrays.<Component>stream(text.getMessages(player.isTextFilteringEnabled()))
/* 147 */       .allMatch(message -> (message.equals(CommonComponents.EMPTY) || message.getContents() instanceof net.minecraft.network.chat.contents.PlainTextContents));
/*     */   }
/*     */   
/*     */   public Vec3 getSignHitboxCenterPosition(BlockState state) {
/* 153 */     return new Vec3(0.5D, 0.5D, 0.5D);
/*     */   }
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 158 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 159 */       return Fluids.WATER.getSource(false); 
/* 161 */     return super.getFluidState(state);
/*     */   }
/*     */   
/*     */   public WoodType type() {
/* 165 */     return this.type;
/*     */   }
/*     */   
/*     */   public static WoodType getWoodType(Block block) {
/* 169 */     SignBlock signBlock = (SignBlock)block;
/* 169 */     return (block instanceof SignBlock) ? signBlock.type() : WoodType.OAK;
/*     */   }
/*     */   
/*     */   public void openTextEdit(Player player, SignBlockEntity sign, boolean isFrontText) {
/* 173 */     sign.setAllowedPlayerEditor(player.getUUID());
/* 174 */     player.openTextEdit(sign, isFrontText);
/*     */   }
/*     */   
/*     */   private boolean otherPlayerIsEditingSign(Player player, SignBlockEntity sign) {
/* 178 */     UUID playerWhoMayEdit = sign.getPlayerWhoMayEdit();
/* 179 */     return (playerWhoMayEdit != null && !playerWhoMayEdit.equals(player.getUUID()));
/*     */   }
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 184 */     return createTickerHelper(type, BlockEntityTypes.SIGN, SignBlockEntity::tick);
/*     */   }
/*     */   
/*     */   protected abstract MapCodec<? extends SignBlock> codec();
/*     */   
/*     */   public abstract float getYRotationDegrees(BlockState paramBlockState);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SignBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */