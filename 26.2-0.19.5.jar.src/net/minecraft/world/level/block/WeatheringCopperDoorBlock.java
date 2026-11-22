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
/*    */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class WeatheringCopperDoorBlock extends DoorBlock implements WeatheringCopper {
/*    */   public static final MapCodec<WeatheringCopperDoorBlock> CODEC;
/*    */   
/*    */   private final WeatheringCopper.WeatherState weatherState;
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockSetType.CODEC.fieldOf("block_set_type").forGetter(DoorBlock::type), (App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(WeatheringCopperDoorBlock::getAge), (App)propertiesCodec()).apply((Applicative)i, WeatheringCopperDoorBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<WeatheringCopperDoorBlock> codec() {
/* 21 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected WeatheringCopperDoorBlock(BlockSetType type, WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
/* 27 */     super(type, properties);
/* 28 */     this.weatherState = weatherState;
/*    */   }
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 33 */     if (state.getValue((Property)DoorBlock.HALF) == DoubleBlockHalf.LOWER)
/* 34 */       changeOverTime(state, level, pos, random); 
/*    */   }
/*    */   
/*    */   protected boolean isRandomlyTicking(BlockState state) {
/* 40 */     return WeatheringCopper.getNext(state.getBlock()).isPresent();
/*    */   }
/*    */   
/*    */   public WeatheringCopper.WeatherState getAge() {
/* 45 */     return this.weatherState;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\WeatheringCopperDoorBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */