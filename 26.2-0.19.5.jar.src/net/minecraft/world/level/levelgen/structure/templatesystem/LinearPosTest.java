/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class LinearPosTest extends PosRuleTest {
/*    */   public static final MapCodec<LinearPosTest> CODEC;
/*    */   
/*    */   private final float minChance;
/*    */   
/*    */   private final float maxChance;
/*    */   
/*    */   private final int minDist;
/*    */   
/*    */   private final int maxDist;
/*    */   
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.optionalFieldOf("min_chance", Float.valueOf(0.0F)).forGetter(()), (App)Codec.FLOAT.optionalFieldOf("max_chance", Float.valueOf(0.0F)).forGetter(()), (App)Codec.INT.optionalFieldOf("min_dist", Integer.valueOf(0)).forGetter(()), (App)Codec.INT.optionalFieldOf("max_dist", Integer.valueOf(0)).forGetter(())).apply((Applicative)i, LinearPosTest::new));
/*    */   }
/*    */   
/*    */   public LinearPosTest(float minChance, float maxChance, int minDist, int maxDist) {
/* 24 */     if (minDist >= maxDist)
/* 25 */       throw new IllegalArgumentException("Invalid range: [" + minDist + "," + maxDist + "]"); 
/* 28 */     this.minChance = minChance;
/* 29 */     this.maxChance = maxChance;
/* 30 */     this.minDist = minDist;
/* 31 */     this.maxDist = maxDist;
/*    */   }
/*    */   
/*    */   public boolean test(BlockPos inTemplatePos, BlockPos worldPos, BlockPos worldReference, RandomSource random) {
/* 36 */     int dist = worldPos.distManhattan((Vec3i)worldReference);
/* 38 */     float rnd = random.nextFloat();
/* 39 */     return (rnd <= Mth.clampedLerp(Mth.inverseLerp(dist, this.minDist, this.maxDist), this.minChance, this.maxChance));
/*    */   }
/*    */   
/*    */   protected PosRuleTestType<?> getType() {
/* 44 */     return PosRuleTestType.LINEAR_POS_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\LinearPosTest.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */