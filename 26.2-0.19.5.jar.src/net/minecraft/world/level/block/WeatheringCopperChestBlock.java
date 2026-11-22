/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.ChestType;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class WeatheringCopperChestBlock extends CopperChestBlock implements WeatheringCopper {
/*    */   public static final MapCodec<WeatheringCopperChestBlock> CODEC;
/*    */   
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(CopperChestBlock::getState), (App)BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("open_sound").forGetter(ChestBlock::getOpenChestSound), (App)BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("close_sound").forGetter(ChestBlock::getCloseChestSound), (App)propertiesCodec()).apply((Applicative)i, WeatheringCopperChestBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<WeatheringCopperChestBlock> codec() {
/* 24 */     return CODEC;
/*    */   }
/*    */   
/*    */   public WeatheringCopperChestBlock(WeatheringCopper.WeatherState weatherState, SoundEvent openSound, SoundEvent closeSound, BlockBehaviour.Properties properties) {
/* 28 */     super(weatherState, openSound, closeSound, properties);
/*    */   }
/*    */   
/*    */   protected boolean isRandomlyTicking(BlockState state) {
/* 33 */     return WeatheringCopper.getNext(state.getBlock()).isPresent();
/*    */   }
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 38 */     if (!((ChestType)state.getValue((Property)ChestBlock.TYPE)).equals(ChestType.RIGHT)) {
/* 38 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/* 38 */       if (blockEntity instanceof ChestBlockEntity) {
/* 38 */         ChestBlockEntity chestBlockEntity = (ChestBlockEntity)blockEntity;
/* 38 */         if (chestBlockEntity.getEntitiesWithContainerOpen().isEmpty())
/* 39 */           changeOverTime(state, level, pos, random); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public WeatheringCopper.WeatherState getAge() {
/* 45 */     return getState();
/*    */   }
/*    */   
/*    */   public boolean isWaxed() {
/* 50 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\WeatheringCopperChestBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */