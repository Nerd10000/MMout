/*    */ package net.minecraft.world.level.block.state;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class BlockState extends BlockBehaviour.BlockStateBase {
/* 10 */   public static final Codec<BlockState> CODEC = codec(BuiltInRegistries.BLOCK.byNameCodec(), Block::defaultBlockState, Block::getStateDefinition).stable();
/*    */   
/*    */   public BlockState(Block owner, Property<?>[] propertyKeys, Comparable<?>[] propertyValues) {
/* 13 */     super(owner, propertyKeys, propertyValues);
/*    */   }
/*    */   
/*    */   protected BlockState asState() {
/* 18 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\BlockState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */