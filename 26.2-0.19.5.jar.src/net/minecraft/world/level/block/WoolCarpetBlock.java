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
/*    */ public class WoolCarpetBlock extends CarpetBlock {
/*    */   public static final MapCodec<WoolCarpetBlock> CODEC;
/*    */   
/*    */   private final DyeColor color;
/*    */   
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DyeColor.CODEC.fieldOf("color").forGetter(WoolCarpetBlock::getColor), (App)propertiesCodec()).apply((Applicative)i, WoolCarpetBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<WoolCarpetBlock> codec() {
/* 15 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected WoolCarpetBlock(DyeColor color, BlockBehaviour.Properties properties) {
/* 21 */     super(properties);
/* 22 */     this.color = color;
/*    */   }
/*    */   
/*    */   public DyeColor getColor() {
/* 26 */     return this.color;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\WoolCarpetBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */