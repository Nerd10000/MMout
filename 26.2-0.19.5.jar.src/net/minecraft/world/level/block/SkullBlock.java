/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class SkullBlock extends AbstractSkullBlock {
/*    */   public static final MapCodec<SkullBlock> CODEC;
/*    */   
/*    */   static {
/* 22 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Type.CODEC.fieldOf("kind").forGetter(AbstractSkullBlock::getType), (App)propertiesCodec()).apply((Applicative)i, SkullBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<? extends SkullBlock> codec() {
/* 29 */     return CODEC;
/*    */   }
/*    */   
/*    */   public static interface Type extends StringRepresentable {
/* 33 */     public static final Map<String, Type> TYPES = (Map<String, Type>)new Object2ObjectArrayMap();
/*    */     
/* 35 */     public static final Codec<Type> CODEC = Codec.stringResolver(StringRepresentable::getSerializedName, TYPES::get);
/*    */     
/*    */     static {
/* 35 */       Objects.requireNonNull(TYPES);
/*    */     }
/*    */   }
/*    */   
/*    */   public enum Types implements Type {
/* 39 */     SKELETON("skeleton"),
/* 40 */     WITHER_SKELETON("wither_skeleton"),
/* 41 */     PLAYER("player"),
/* 42 */     ZOMBIE("zombie"),
/* 43 */     CREEPER("creeper"),
/* 44 */     PIGLIN("piglin"),
/* 45 */     DRAGON("dragon");
/*    */     
/*    */     private final String name;
/*    */     
/*    */     Types(String name) {
/* 51 */       this.name = name;
/* 52 */       TYPES.put(name, this);
/*    */     }
/*    */     
/*    */     public String getSerializedName() {
/* 57 */       return this.name;
/*    */     }
/*    */   }
/*    */   
/* 61 */   public static final int MAX = RotationSegment.getMaxSegmentIndex();
/*    */   
/* 62 */   private static final int ROTATIONS = MAX + 1;
/*    */   
/* 64 */   public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
/*    */   
/* 66 */   private static final VoxelShape SHAPE = Block.column(8.0D, 0.0D, 8.0D);
/*    */   
/* 67 */   private static final VoxelShape SHAPE_PIGLIN = Block.column(10.0D, 0.0D, 8.0D);
/*    */   
/*    */   protected SkullBlock(Type type, BlockBehaviour.Properties properties) {
/* 70 */     super(type, properties);
/* 71 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)ROTATION, Integer.valueOf(0)));
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 76 */     return (getType() == Types.PIGLIN) ? SHAPE_PIGLIN : SHAPE;
/*    */   }
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 81 */     return (BlockState)super.getStateForPlacement(context).setValue((Property)ROTATION, Integer.valueOf(RotationSegment.convertToSegment(context.getRotation())));
/*    */   }
/*    */   
/*    */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 86 */     return (BlockState)state.setValue((Property)ROTATION, Integer.valueOf(rotation.rotate(((Integer)state.getValue((Property)ROTATION)).intValue(), ROTATIONS)));
/*    */   }
/*    */   
/*    */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 91 */     return (BlockState)state.setValue((Property)ROTATION, Integer.valueOf(mirror.mirror(((Integer)state.getValue((Property)ROTATION)).intValue(), ROTATIONS)));
/*    */   }
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 96 */     super.createBlockStateDefinition(builder);
/* 97 */     builder.add(new Property[] { (Property)ROTATION });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SkullBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */