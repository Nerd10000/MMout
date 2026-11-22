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
/*    */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*    */ 
/*    */ public class WeatheringCopperTrapDoorBlock extends TrapDoorBlock implements WeatheringCopper {
/*    */   public static final MapCodec<WeatheringCopperTrapDoorBlock> CODEC;
/*    */   
/*    */   private final WeatheringCopper.WeatherState weatherState;
/*    */   
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockSetType.CODEC.fieldOf("block_set_type").forGetter(TrapDoorBlock::getType), (App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(WeatheringCopperTrapDoorBlock::getAge), (App)propertiesCodec()).apply((Applicative)i, WeatheringCopperTrapDoorBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<WeatheringCopperTrapDoorBlock> codec() {
/* 20 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected WeatheringCopperTrapDoorBlock(BlockSetType type, WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
/* 26 */     super(type, properties);
/* 27 */     this.weatherState = weatherState;
/*    */   }
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 32 */     changeOverTime(state, level, pos, random);
/*    */   }
/*    */   
/*    */   protected boolean isRandomlyTicking(BlockState state) {
/* 37 */     return WeatheringCopper.getNext(state.getBlock()).isPresent();
/*    */   }
/*    */   
/*    */   public WeatheringCopper.WeatherState getAge() {
/* 42 */     return this.weatherState;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\WeatheringCopperTrapDoorBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */