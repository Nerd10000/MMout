/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class AxisAlignedLinearPosTest extends PosRuleTest {
/*    */   public static final MapCodec<AxisAlignedLinearPosTest> CODEC;
/*    */   
/*    */   private final float minChance;
/*    */   
/*    */   private final float maxChance;
/*    */   
/*    */   private final int minDist;
/*    */   
/*    */   private final int maxDist;
/*    */   
/*    */   private final Direction.Axis axis;
/*    */   
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.optionalFieldOf("min_chance", Float.valueOf(0.0F)).forGetter(()), (App)Codec.FLOAT.optionalFieldOf("max_chance", Float.valueOf(0.0F)).forGetter(()), (App)Codec.INT.optionalFieldOf("min_dist", Integer.valueOf(0)).forGetter(()), (App)Codec.INT.optionalFieldOf("max_dist", Integer.valueOf(0)).forGetter(()), (App)Direction.Axis.CODEC.optionalFieldOf("axis", Direction.Axis.Y).forGetter(())).apply((Applicative)i, AxisAlignedLinearPosTest::new));
/*    */   }
/*    */   
/*    */   public AxisAlignedLinearPosTest(float minChance, float maxChance, int minDist, int maxDist, Direction.Axis axis) {
/* 27 */     if (minDist >= maxDist)
/* 28 */       throw new IllegalArgumentException("Invalid range: [" + minDist + "," + maxDist + "]"); 
/* 30 */     this.minChance = minChance;
/* 31 */     this.maxChance = maxChance;
/* 32 */     this.minDist = minDist;
/* 33 */     this.maxDist = maxDist;
/* 34 */     this.axis = axis;
/*    */   }
/*    */   
/*    */   public boolean test(BlockPos inTemplatePos, BlockPos worldPos, BlockPos worldReference, RandomSource random) {
/* 39 */     Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, this.axis);
/* 40 */     float xd = Math.abs((worldPos.getX() - worldReference.getX()) * direction.getStepX());
/* 41 */     float yd = Math.abs((worldPos.getY() - worldReference.getY()) * direction.getStepY());
/* 42 */     float zd = Math.abs((worldPos.getZ() - worldReference.getZ()) * direction.getStepZ());
/* 43 */     int dist = (int)(xd + yd + zd);
/* 45 */     float rnd = random.nextFloat();
/* 46 */     return (rnd <= Mth.clampedLerp(Mth.inverseLerp(dist, this.minDist, this.maxDist), this.minChance, this.maxChance));
/*    */   }
/*    */   
/*    */   protected PosRuleTestType<?> getType() {
/* 51 */     return PosRuleTestType.AXIS_ALIGNED_LINEAR_POS_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\AxisAlignedLinearPosTest.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */