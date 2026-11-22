/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WeatheringCopperStairBlock extends StairBlock implements WeatheringCopper {
/*    */   public static final MapCodec<WeatheringCopperStairBlock> CODEC;
/*    */   
/*    */   private final WeatheringCopper.WeatherState weatherState;
/*    */   
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(ChangeOverTimeBlock::getAge), (App)BlockState.CODEC.fieldOf("base_state").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, WeatheringCopperStairBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<WeatheringCopperStairBlock> codec() {
/* 19 */     return CODEC;
/*    */   }
/*    */   
/*    */   public WeatheringCopperStairBlock(WeatheringCopper.WeatherState weatherState, BlockState baseState, BlockBehaviour.Properties properties) {
/* 25 */     super(baseState, properties);
/* 26 */     this.weatherState = weatherState;
/*    */   }
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 31 */     changeOverTime(state, level, pos, random);
/*    */   }
/*    */   
/*    */   protected boolean isRandomlyTicking(BlockState state) {
/* 36 */     return WeatheringCopper.getNext(state.getBlock()).isPresent();
/*    */   }
/*    */   
/*    */   public WeatheringCopper.WeatherState getAge() {
/* 41 */     return this.weatherState;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\WeatheringCopperStairBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */