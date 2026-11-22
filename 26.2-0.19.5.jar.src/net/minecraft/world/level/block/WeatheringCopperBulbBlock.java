/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WeatheringCopperBulbBlock extends CopperBulbBlock implements WeatheringCopper {
/*    */   public static final MapCodec<WeatheringCopperBulbBlock> CODEC;
/*    */   
/*    */   private final WeatheringCopper.WeatherState weatherState;
/*    */   
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(WeatheringCopperBulbBlock::getAge), (App)propertiesCodec()).apply((Applicative)i, WeatheringCopperBulbBlock::new));
/*    */   }
/*    */   
/*    */   protected MapCodec<WeatheringCopperBulbBlock> codec() {
/* 18 */     return CODEC;
/*    */   }
/*    */   
/*    */   public WeatheringCopperBulbBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
/* 23 */     super(properties);
/* 24 */     this.weatherState = weatherState;
/*    */   }
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 29 */     changeOverTime(state, level, pos, random);
/*    */   }
/*    */   
/*    */   protected boolean isRandomlyTicking(BlockState state) {
/* 34 */     return WeatheringCopper.getNext(state.getBlock()).isPresent();
/*    */   }
/*    */   
/*    */   public WeatheringCopper.WeatherState getAge() {
/* 39 */     return this.weatherState;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\WeatheringCopperBulbBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */