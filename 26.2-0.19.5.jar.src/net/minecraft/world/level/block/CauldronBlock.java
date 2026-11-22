/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.cauldron.CauldronInteractions;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class CauldronBlock extends AbstractCauldronBlock {
/* 15 */   public static final MapCodec<CauldronBlock> CODEC = simpleCodec(CauldronBlock::new);
/*    */   
/*    */   private static final float RAIN_FILL_CHANCE = 0.05F;
/*    */   
/*    */   private static final float POWDER_SNOW_FILL_CHANCE = 0.1F;
/*    */   
/*    */   public MapCodec<CauldronBlock> codec() {
/* 19 */     return CODEC;
/*    */   }
/*    */   
/*    */   public CauldronBlock(BlockBehaviour.Properties properties) {
/* 26 */     super(properties, CauldronInteractions.EMPTY);
/*    */   }
/*    */   
/*    */   public boolean isFull(BlockState state) {
/* 31 */     return false;
/*    */   }
/*    */   
/*    */   protected static boolean shouldHandlePrecipitation(Level level, Biome.Precipitation precipitation) {
/* 35 */     if (precipitation == Biome.Precipitation.RAIN)
/* 36 */       return (level.getRandom().nextFloat() < 0.05F); 
/* 37 */     if (precipitation == Biome.Precipitation.SNOW)
/* 38 */       return (level.getRandom().nextFloat() < 0.1F); 
/* 40 */     return false;
/*    */   }
/*    */   
/*    */   public void handlePrecipitation(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation) {
/* 45 */     if (!shouldHandlePrecipitation(level, precipitation))
/*    */       return; 
/* 49 */     if (precipitation == Biome.Precipitation.RAIN) {
/* 50 */       level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState());
/* 51 */       level.gameEvent(null, (Holder)GameEvent.BLOCK_CHANGE, pos);
/* 52 */     } else if (precipitation == Biome.Precipitation.SNOW) {
/* 53 */       level.setBlockAndUpdate(pos, Blocks.POWDER_SNOW_CAULDRON.defaultBlockState());
/* 54 */       level.gameEvent(null, (Holder)GameEvent.BLOCK_CHANGE, pos);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected boolean canReceiveStalactiteDrip(Fluid fluid) {
/* 60 */     return true;
/*    */   }
/*    */   
/*    */   protected void receiveStalactiteDrip(BlockState state, Level level, BlockPos pos, Fluid fluid) {
/* 65 */     if (fluid == Fluids.WATER) {
/* 66 */       BlockState newState = Blocks.WATER_CAULDRON.defaultBlockState();
/* 67 */       level.setBlockAndUpdate(pos, newState);
/* 68 */       level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
/* 69 */       level.levelEvent(1047, pos, 0);
/* 70 */     } else if (fluid == Fluids.LAVA) {
/* 71 */       BlockState newState = Blocks.LAVA_CAULDRON.defaultBlockState();
/* 72 */       level.setBlockAndUpdate(pos, newState);
/* 73 */       level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
/* 74 */       level.levelEvent(1046, pos, 0);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\CauldronBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */