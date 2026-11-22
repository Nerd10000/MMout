/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class StainedGlassBlock extends TransparentBlock implements BeaconBeamBlock {
/*    */   public static final MapCodec<StainedGlassBlock> CODEC;
/*    */   
/*    */   private final DyeColor color;
/*    */   
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DyeColor.CODEC.fieldOf("color").forGetter(StainedGlassBlock::getColor), (App)propertiesCodec()).apply((Applicative)i, StainedGlassBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<StainedGlassBlock> codec() {
/* 15 */     return CODEC;
/*    */   }
/*    */   
/*    */   public StainedGlassBlock(DyeColor color, BlockBehaviour.Properties properties) {
/* 20 */     super(properties);
/* 21 */     this.color = color;
/*    */   }
/*    */   
/*    */   public DyeColor getColor() {
/* 26 */     return this.color;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\StainedGlassBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */