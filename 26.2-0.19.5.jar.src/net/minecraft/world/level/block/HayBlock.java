/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class HayBlock extends RotatedPillarBlock {
/* 11 */   public static final MapCodec<HayBlock> CODEC = simpleCodec(HayBlock::new);
/*    */   
/*    */   public MapCodec<HayBlock> codec() {
/* 15 */     return CODEC;
/*    */   }
/*    */   
/*    */   public HayBlock(BlockBehaviour.Properties properties) {
/* 19 */     super(properties);
/* 20 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AXIS, (Comparable)Direction.Axis.Y));
/*    */   }
/*    */   
/*    */   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
/* 25 */     entity.causeFallDamage(fallDistance, 0.2F, level.damageSources().fall());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\HayBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */