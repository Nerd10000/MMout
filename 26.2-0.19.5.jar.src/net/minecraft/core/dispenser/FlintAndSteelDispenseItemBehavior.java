/*    */ package net.minecraft.core.dispenser;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.monster.cubemob.SulfurCube;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.BaseFireBlock;
/*    */ import net.minecraft.world.level.block.CampfireBlock;
/*    */ import net.minecraft.world.level.block.CandleBlock;
/*    */ import net.minecraft.world.level.block.CandleCakeBlock;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.TntBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class FlintAndSteelDispenseItemBehavior extends OptionalDispenseItemBehavior {
/*    */   protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 24 */     ServerLevel level = source.level();
/* 26 */     setSuccess(true);
/* 28 */     Direction facing = (Direction)source.state().getValue((Property)DispenserBlock.FACING);
/* 29 */     BlockPos targetPos = source.pos().relative(facing);
/* 30 */     BlockState target = level.getBlockState(targetPos);
/* 32 */     if (!tryIgniteExplosiveEntities(level, targetPos))
/* 34 */       if (BaseFireBlock.canBePlacedAt((Level)level, targetPos, facing)) {
/* 35 */         level.setBlockAndUpdate(targetPos, BaseFireBlock.getState((BlockGetter)level, targetPos));
/* 36 */         level.gameEvent(null, (Holder)GameEvent.BLOCK_PLACE, targetPos);
/* 37 */       } else if (CampfireBlock.canLight(target) || CandleBlock.canLight(target) || CandleCakeBlock.canLight(target)) {
/* 38 */         level.setBlockAndUpdate(targetPos, (BlockState)target.setValue((Property)BlockStateProperties.LIT, Boolean.valueOf(true)));
/* 39 */         level.gameEvent(null, (Holder)GameEvent.BLOCK_CHANGE, targetPos);
/* 40 */       } else if (target.getBlock() instanceof TntBlock) {
/* 41 */         if (TntBlock.prime((Level)level, targetPos)) {
/* 42 */           level.removeBlock(targetPos, false);
/*    */         } else {
/* 44 */           setSuccess(false);
/*    */         } 
/*    */       }  
/* 48 */     if (isSuccess())
/* 49 */       dispensed.hurtAndBreak(1, level, null, item -> {
/*    */           
/*    */           }); 
/* 52 */     return dispensed;
/*    */   }
/*    */   
/*    */   private static boolean tryIgniteExplosiveEntities(ServerLevel level, BlockPos pos) {
/* 56 */     List<SulfurCube> entities = level.getEntitiesOfClass(SulfurCube.class, new AABB(pos), SulfurCube::canExplode);
/* 57 */     if (entities.isEmpty())
/* 58 */       return false; 
/* 60 */     ((SulfurCube)entities.getFirst()).primeTime(false);
/* 61 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\FlintAndSteelDispenseItemBehavior.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */