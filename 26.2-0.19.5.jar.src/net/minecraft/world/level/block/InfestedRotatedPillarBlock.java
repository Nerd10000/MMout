/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class InfestedRotatedPillarBlock extends InfestedBlock {
/*    */   public static final MapCodec<InfestedRotatedPillarBlock> CODEC;
/*    */   
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("host").forGetter(InfestedBlock::getHostBlock), (App)propertiesCodec()).apply((Applicative)i, InfestedRotatedPillarBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<InfestedRotatedPillarBlock> codec() {
/* 19 */     return CODEC;
/*    */   }
/*    */   
/*    */   public InfestedRotatedPillarBlock(Block hostBlock, BlockBehaviour.Properties properties) {
/* 23 */     super(hostBlock, properties);
/* 24 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)RotatedPillarBlock.AXIS, (Comparable)Direction.Axis.Y));
/*    */   }
/*    */   
/*    */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 29 */     return RotatedPillarBlock.rotatePillar(state, rotation);
/*    */   }
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 34 */     builder.add(new Property[] { (Property)RotatedPillarBlock.AXIS });
/*    */   }
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 39 */     return (BlockState)defaultBlockState().setValue((Property)RotatedPillarBlock.AXIS, (Comparable)context.getClickedFace().getAxis());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\InfestedRotatedPillarBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */