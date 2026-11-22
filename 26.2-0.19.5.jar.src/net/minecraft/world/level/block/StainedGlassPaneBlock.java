/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class StainedGlassPaneBlock extends IronBarsBlock implements BeaconBeamBlock {
/*    */   public static final MapCodec<StainedGlassPaneBlock> CODEC;
/*    */   
/*    */   private final DyeColor color;
/*    */   
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DyeColor.CODEC.fieldOf("color").forGetter(StainedGlassPaneBlock::getColor), (App)propertiesCodec()).apply((Applicative)i, StainedGlassPaneBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<StainedGlassPaneBlock> codec() {
/* 15 */     return CODEC;
/*    */   }
/*    */   
/*    */   public StainedGlassPaneBlock(DyeColor color, BlockBehaviour.Properties properties) {
/* 21 */     super(properties);
/* 22 */     this.color = color;
/* 23 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, Boolean.valueOf(false))).setValue((Property)EAST, Boolean.valueOf(false))).setValue((Property)SOUTH, Boolean.valueOf(false))).setValue((Property)WEST, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*    */   }
/*    */   
/*    */   public DyeColor getColor() {
/* 28 */     return this.color;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\StainedGlassPaneBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */