/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class MagmaBlock extends Block {
/* 11 */   public static final MapCodec<MagmaBlock> CODEC = simpleCodec(MagmaBlock::new);
/*    */   
/*    */   public MapCodec<MagmaBlock> codec() {
/* 15 */     return CODEC;
/*    */   }
/*    */   
/*    */   public MagmaBlock(BlockBehaviour.Properties properties) {
/* 19 */     super(properties);
/*    */   }
/*    */   
/*    */   public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity) {
/* 24 */     if (!entity.isSteppingCarefully() && entity instanceof net.minecraft.world.entity.LivingEntity)
/* 25 */       entity.hurt(level.damageSources().hotFloor(), 1.0F); 
/* 28 */     super.stepOn(level, pos, onState, entity);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\MagmaBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */