/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class PumpkinBlock extends Block {
/* 24 */   public static final MapCodec<PumpkinBlock> CODEC = simpleCodec(PumpkinBlock::new);
/*    */   
/*    */   public MapCodec<PumpkinBlock> codec() {
/* 28 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected PumpkinBlock(BlockBehaviour.Properties properties) {
/* 32 */     super(properties);
/*    */   }
/*    */   
/*    */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*    */     ServerLevel serverLevel;
/* 37 */     if (!itemStack.is(Items.SHEARS))
/* 38 */       return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult); 
/* 40 */     if (level instanceof ServerLevel) {
/* 40 */       serverLevel = (ServerLevel)level;
/*    */     } else {
/* 41 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/* 43 */     Direction clickedDirection = hitResult.getDirection();
/* 44 */     Direction direction = (clickedDirection.getAxis() == Direction.Axis.Y) ? player.getDirection().getOpposite() : clickedDirection;
/* 46 */     dropFromBlockInteractLootTable(serverLevel, BuiltInLootTables.CARVE_PUMPKIN, state, level.getBlockEntity(pos), (ItemInstance)itemStack, (Entity)player, (ignored, pumpkinSeeds) -> {
/*    */           ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5D + direction.getStepX() * 0.65D, pos.getY() + 0.1D, pos.getZ() + 0.5D + direction.getStepZ() * 0.65D, pumpkinSeeds);
/*    */           RandomSource random = level.getRandom();
/*    */           entity.setDeltaMovement(0.05D * direction.getStepX() + random.nextDouble() * 0.02D, 0.05D, 0.05D * direction.getStepZ() + random.nextDouble() * 0.02D);
/*    */           level.addFreshEntity((Entity)entity);
/*    */         });
/* 65 */     level.playSound(null, pos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 66 */     level.setBlock(pos, (BlockState)Blocks.CARVED_PUMPKIN.defaultBlockState().setValue((Property)CarvedPumpkinBlock.FACING, (Comparable)direction), 11);
/* 67 */     itemStack.hurtAndBreak(1, (LivingEntity)player, hand.asEquipmentSlot());
/* 68 */     level.gameEvent((Entity)player, (Holder)GameEvent.SHEAR, pos);
/* 69 */     player.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
/* 71 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\PumpkinBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */