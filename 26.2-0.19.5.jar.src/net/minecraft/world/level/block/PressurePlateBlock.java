/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class PressurePlateBlock extends BasePressurePlateBlock {
/*    */   public static final MapCodec<PressurePlateBlock> CODEC;
/*    */   
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockSetType.CODEC.fieldOf("block_set_type").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, PressurePlateBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<PressurePlateBlock> codec() {
/* 24 */     return CODEC;
/*    */   }
/*    */   
/* 27 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*    */   
/*    */   protected PressurePlateBlock(BlockSetType type, BlockBehaviour.Properties properties) {
/* 30 */     super(properties, type);
/* 31 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)POWERED, Boolean.valueOf(false)));
/*    */   }
/*    */   
/*    */   protected int getSignalForState(BlockState state) {
/* 36 */     return ((Boolean)state.getValue((Property)POWERED)).booleanValue() ? 15 : 0;
/*    */   }
/*    */   
/*    */   protected BlockState setSignalForState(BlockState state, int signal) {
/* 41 */     return (BlockState)state.setValue((Property)POWERED, Boolean.valueOf((signal > 0)));
/*    */   }
/*    */   
/*    */   protected int getSignalStrength(Level level, BlockPos pos) {
/* 46 */     switch (this.type.pressurePlateSensitivity()) {
/*    */       default:
/* 46 */         throw new MatchException(null, null);
/*    */       case EVERYTHING:
/*    */       
/*    */       case MOBS:
/*    */         break;
/*    */     } 
/* 46 */     Class<LivingEntity> clazz = 
/*    */       
/* 48 */       LivingEntity.class;
/* 50 */     return (getEntityCount(level, TOUCH_AABB.move(pos), (Class)clazz) > 0) ? 15 : 0;
/*    */   }
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 55 */     builder.add(new Property[] { (Property)POWERED });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\PressurePlateBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */