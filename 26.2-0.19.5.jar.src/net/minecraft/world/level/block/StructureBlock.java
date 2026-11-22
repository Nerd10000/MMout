/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.block.state.properties.StructureMode;
/*    */ import net.minecraft.world.level.redstone.Orientation;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class StructureBlock extends BaseEntityBlock implements GameMasterBlock {
/* 23 */   public static final MapCodec<StructureBlock> CODEC = simpleCodec(StructureBlock::new);
/*    */   
/*    */   public MapCodec<StructureBlock> codec() {
/* 27 */     return CODEC;
/*    */   }
/*    */   
/* 30 */   public static final EnumProperty<StructureMode> MODE = BlockStateProperties.STRUCTUREBLOCK_MODE;
/*    */   
/*    */   protected StructureBlock(BlockBehaviour.Properties properties) {
/* 33 */     super(properties);
/* 35 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)MODE, (Comparable)StructureMode.LOAD));
/*    */   }
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 40 */     return (BlockEntity)new StructureBlockEntity(worldPosition, blockState);
/*    */   }
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 45 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 46 */     if (blockEntity instanceof StructureBlockEntity) {
/* 46 */       StructureBlockEntity structureBlockEntity = (StructureBlockEntity)blockEntity;
/* 47 */       return structureBlockEntity.usedBy(player) ? (InteractionResult)InteractionResult.SUCCESS : (InteractionResult)InteractionResult.PASS;
/*    */     } 
/* 50 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */   
/*    */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {
/* 55 */     if (level.isClientSide())
/*    */       return; 
/* 58 */     if (by != null) {
/* 59 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/* 60 */       if (blockEntity instanceof StructureBlockEntity) {
/* 60 */         StructureBlockEntity structureBlockEntity = (StructureBlockEntity)blockEntity;
/* 61 */         structureBlockEntity.createdBy(by);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 68 */     builder.add(new Property[] { (Property)MODE });
/*    */   }
/*    */   
/*    */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/*    */     ServerLevel serverLevel;
/*    */     StructureBlockEntity structureBlock;
/* 73 */     if (level instanceof ServerLevel) {
/* 73 */       serverLevel = (ServerLevel)level;
/*    */     } else {
/*    */       return;
/*    */     } 
/* 77 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 78 */     if (blockEntity instanceof StructureBlockEntity) {
/* 78 */       structureBlock = (StructureBlockEntity)blockEntity;
/*    */     } else {
/*    */       return;
/*    */     } 
/* 82 */     boolean shouldTrigger = level.hasNeighborSignal(pos);
/* 83 */     boolean isPowered = structureBlock.isPowered();
/* 85 */     if (shouldTrigger && !isPowered) {
/* 86 */       structureBlock.setPowered(true);
/* 87 */       trigger(serverLevel, structureBlock);
/* 88 */     } else if (!shouldTrigger && isPowered) {
/* 89 */       structureBlock.setPowered(false);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void trigger(ServerLevel level, StructureBlockEntity structureBlock) {
/* 94 */     switch (structureBlock.getMode()) {
/*    */       case SAVE:
/* 95 */         structureBlock.saveStructure(false);
/*    */         break;
/*    */       case LOAD:
/* 96 */         structureBlock.placeStructure(level);
/*    */         break;
/*    */       case CORNER:
/* 97 */         structureBlock.unloadStructure();
/*    */         break;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\StructureBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */