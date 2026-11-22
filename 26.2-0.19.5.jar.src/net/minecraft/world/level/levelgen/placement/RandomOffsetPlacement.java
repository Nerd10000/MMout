/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.ConstantInt;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.util.valueproviders.TrapezoidInt;
/*    */ 
/*    */ public class RandomOffsetPlacement extends PlacementModifier {
/*    */   public static final MapCodec<RandomOffsetPlacement> CODEC;
/*    */   
/*    */   private final IntProvider xzSpread;
/*    */   
/*    */   private final IntProvider ySpread;
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)IntProviders.codec(-16, 16).fieldOf("xz_spread").forGetter(()), (App)IntProviders.codec(-16, 16).fieldOf("y_spread").forGetter(())).apply((Applicative)i, RandomOffsetPlacement::new));
/*    */   }
/*    */   
/*    */   public static RandomOffsetPlacement of(IntProvider xzSpread, IntProvider ySpread) {
/* 25 */     return new RandomOffsetPlacement(xzSpread, ySpread);
/*    */   }
/*    */   
/*    */   public static RandomOffsetPlacement ofTriangle(int xzRange, int yRange) {
/* 29 */     return new RandomOffsetPlacement(TrapezoidInt.triangle(xzRange), TrapezoidInt.triangle(yRange));
/*    */   }
/*    */   
/*    */   public static RandomOffsetPlacement vertical(IntProvider ySpread) {
/* 33 */     return new RandomOffsetPlacement((IntProvider)ConstantInt.of(0), ySpread);
/*    */   }
/*    */   
/*    */   public static RandomOffsetPlacement horizontal(IntProvider xzSpread) {
/* 37 */     return new RandomOffsetPlacement(xzSpread, (IntProvider)ConstantInt.of(0));
/*    */   }
/*    */   
/*    */   private RandomOffsetPlacement(IntProvider xzSpread, IntProvider ySpread) {
/* 41 */     this.xzSpread = xzSpread;
/* 42 */     this.ySpread = ySpread;
/*    */   }
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
/* 47 */     int scatterX = origin.getX() + this.xzSpread.sample(random);
/* 48 */     int scatterY = origin.getY() + this.ySpread.sample(random);
/* 49 */     int scatterZ = origin.getZ() + this.xzSpread.sample(random);
/* 50 */     return Stream.of(new BlockPos(scatterX, scatterY, scatterZ));
/*    */   }
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 55 */     return PlacementModifierType.RANDOM_OFFSET;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\placement\RandomOffsetPlacement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */