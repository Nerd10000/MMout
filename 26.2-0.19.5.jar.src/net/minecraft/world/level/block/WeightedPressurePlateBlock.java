/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class WeightedPressurePlateBlock extends BasePressurePlateBlock {
/*    */   public static final MapCodec<WeightedPressurePlateBlock> CODEC;
/*    */   
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.intRange(1, 1024).fieldOf("max_weight").forGetter(()), (App)BlockSetType.CODEC.fieldOf("block_set_type").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, WeightedPressurePlateBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<WeightedPressurePlateBlock> codec() {
/* 27 */     return CODEC;
/*    */   }
/*    */   
/* 30 */   public static final IntegerProperty POWER = BlockStateProperties.POWER;
/*    */   
/*    */   private final int maxWeight;
/*    */   
/*    */   protected WeightedPressurePlateBlock(int maxWeight, BlockSetType type, BlockBehaviour.Properties properties) {
/* 35 */     super(properties, type);
/* 36 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)POWER, Integer.valueOf(0)));
/* 37 */     this.maxWeight = maxWeight;
/*    */   }
/*    */   
/*    */   protected int getSignalStrength(Level level, BlockPos pos) {
/* 43 */     int count = Math.min(getEntityCount(level, TOUCH_AABB.move(pos), Entity.class), this.maxWeight);
/* 44 */     if (count > 0) {
/* 45 */       float percent = Math.min(this.maxWeight, count) / this.maxWeight;
/* 46 */       return Mth.ceil(percent * 15.0F);
/*    */     } 
/* 49 */     return 0;
/*    */   }
/*    */   
/*    */   protected int getSignalForState(BlockState state) {
/* 54 */     return ((Integer)state.getValue((Property)POWER)).intValue();
/*    */   }
/*    */   
/*    */   protected BlockState setSignalForState(BlockState state, int signal) {
/* 59 */     return (BlockState)state.setValue((Property)POWER, Integer.valueOf(signal));
/*    */   }
/*    */   
/*    */   protected int getPressedTime() {
/* 64 */     return 10;
/*    */   }
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 69 */     builder.add(new Property[] { (Property)POWER });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\WeightedPressurePlateBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */