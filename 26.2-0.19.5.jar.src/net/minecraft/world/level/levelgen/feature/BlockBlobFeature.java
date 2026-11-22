/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.BlockBlobConfiguration;
/*    */ 
/*    */ public class BlockBlobFeature extends Feature<BlockBlobConfiguration> {
/*    */   public BlockBlobFeature(Codec<BlockBlobConfiguration> codec) {
/* 12 */     super(codec);
/*    */   }
/*    */   
/*    */   public boolean place(FeaturePlaceContext<BlockBlobConfiguration> context) {
/* 17 */     BlockPos origin = context.origin();
/* 18 */     WorldGenLevel level = context.level();
/* 19 */     RandomSource random = context.random();
/* 20 */     BlockBlobConfiguration config = context.config();
/* 21 */     while (origin.getY() > level.getMinY() + 3 && 
/* 22 */       !config.canPlaceOn().test(level, origin.below()))
/* 25 */       origin = origin.below(); 
/* 27 */     if (origin.getY() <= level.getMinY() + 3)
/* 28 */       return false; 
/* 31 */     int c = 0;
/* 32 */     while (c < 3) {
/* 33 */       int xr = random.nextInt(2);
/* 34 */       int yr = random.nextInt(2);
/* 35 */       int zr = random.nextInt(2);
/* 36 */       float tr = (xr + yr + zr) * 0.333F + 0.5F;
/* 38 */       for (BlockPos blockPos : BlockPos.betweenClosed(origin.offset(-xr, -yr, -zr), origin.offset(xr, yr, zr))) {
/* 39 */         if (blockPos.distSqr((Vec3i)origin) <= (tr * tr))
/* 40 */           level.setBlock(blockPos, config.state(), 3); 
/*    */       } 
/* 44 */       origin = origin.offset(-1 + random.nextInt(2), -random.nextInt(2), -1 + random.nextInt(2));
/* 45 */       c++;
/*    */     } 
/* 48 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\BlockBlobFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */