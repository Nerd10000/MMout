/*    */ package net.minecraft.world.level.levelgen.flat;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.dimension.DimensionType;
/*    */ 
/*    */ public class FlatLayerInfo {
/*    */   public static final Codec<FlatLayerInfo> CODEC;
/*    */   
/*    */   private final Holder<Block> block;
/*    */   
/*    */   private final int height;
/*    */   
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.intRange(0, DimensionType.Y_SIZE).fieldOf("height").forGetter(FlatLayerInfo::getHeight), (App)BuiltInRegistries.BLOCK.holderByNameCodec().fieldOf("block").forGetter(())).apply((Applicative)i, FlatLayerInfo::new));
/*    */   }
/*    */   
/*    */   public FlatLayerInfo(int height, Block block) {
/* 21 */     this(height, (Holder<Block>)block.builtInRegistryHolder());
/*    */   }
/*    */   
/*    */   public FlatLayerInfo(int height, Holder<Block> block) {
/* 25 */     this.height = height;
/* 26 */     this.block = block;
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 30 */     return this.height;
/*    */   }
/*    */   
/*    */   public BlockState getBlockState() {
/* 34 */     return ((Block)this.block.value()).defaultBlockState();
/*    */   }
/*    */   
/*    */   public FlatLayerInfo heightLimited(int maxHeight) {
/* 38 */     if (this.height > maxHeight)
/* 39 */       return new FlatLayerInfo(maxHeight, this.block); 
/* 41 */     return this;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 46 */     return ((this.height != 1) ? ("" + this.height + "*") : "") + ((this.height != 1) ? ("" + this.height + "*") : "");
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\flat\FlatLayerInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */