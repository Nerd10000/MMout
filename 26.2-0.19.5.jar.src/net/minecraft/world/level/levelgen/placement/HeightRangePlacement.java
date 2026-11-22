/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.TrapezoidHeight;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
/*    */ 
/*    */ public class HeightRangePlacement extends PlacementModifier {
/*    */   public static final MapCodec<HeightRangePlacement> CODEC;
/*    */   
/*    */   private final HeightProvider height;
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)HeightProvider.CODEC.fieldOf("height").forGetter(())).apply((Applicative)i, HeightRangePlacement::new));
/*    */   }
/*    */   
/*    */   private HeightRangePlacement(HeightProvider height) {
/* 23 */     this.height = height;
/*    */   }
/*    */   
/*    */   public static HeightRangePlacement of(HeightProvider height) {
/* 27 */     return new HeightRangePlacement(height);
/*    */   }
/*    */   
/*    */   public static HeightRangePlacement uniform(VerticalAnchor minInclusive, VerticalAnchor maxInclusive) {
/* 31 */     return of((HeightProvider)UniformHeight.of(minInclusive, maxInclusive));
/*    */   }
/*    */   
/*    */   public static HeightRangePlacement triangle(VerticalAnchor minInclusive, VerticalAnchor maxInclusive) {
/* 35 */     return of((HeightProvider)TrapezoidHeight.of(minInclusive, maxInclusive));
/*    */   }
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
/* 40 */     return Stream.of(origin.atY(this.height.sample(random, context)));
/*    */   }
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 45 */     return PlacementModifierType.HEIGHT_RANGE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\placement\HeightRangePlacement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */