/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public interface CaveVines {
/* 22 */   public static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 16.0D);
/*    */   
/* 24 */   public static final BooleanProperty BERRIES = BlockStateProperties.BERRIES;
/*    */   
/*    */   static InteractionResult use(Entity sourceEntity, BlockState state, Level level, BlockPos pos) {
/* 27 */     if (((Boolean)state.getValue((Property)BERRIES)).booleanValue()) {
/* 28 */       if (level instanceof ServerLevel) {
/* 28 */         ServerLevel serverLevel = (ServerLevel)level;
/* 29 */         Block.dropFromBlockInteractLootTable(serverLevel, BuiltInLootTables.HARVEST_CAVE_VINE, state, level
/*    */             
/* 33 */             .getBlockEntity(pos), null, sourceEntity, (serverlvl, itemStack) -> Block.popResource((Level)serverlvl, pos, itemStack));
/* 38 */         float pitch = Mth.randomBetween(serverLevel.getRandom(), 0.8F, 1.2F);
/* 39 */         serverLevel.playSound(null, pos, SoundEvents.CAVE_VINES_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, pitch);
/* 40 */         BlockState newState = (BlockState)state.setValue((Property)BERRIES, Boolean.valueOf(false));
/* 41 */         serverLevel.setBlock(pos, newState, 2);
/* 42 */         serverLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(sourceEntity, newState));
/*    */       } 
/* 44 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/* 46 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */   
/*    */   static boolean hasGlowBerries(BlockState state) {
/* 50 */     return (state.hasProperty((Property)BERRIES) && ((Boolean)state.getValue((Property)BERRIES)).booleanValue());
/*    */   }
/*    */   
/*    */   static ToIntFunction<BlockState> emission(int lightEmission) {
/* 54 */     return state -> ((Boolean)state.getValue((Property)BlockStateProperties.BERRIES)).booleanValue() ? lightEmission : 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\CaveVines.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */