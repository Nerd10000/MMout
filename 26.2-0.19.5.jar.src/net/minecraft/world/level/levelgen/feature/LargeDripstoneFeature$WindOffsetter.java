/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.FloatProvider;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ final class WindOffsetter {
/*     */   private final int originY;
/*     */   
/*     */   private final Vec3 windSpeed;
/*     */   
/*     */   private final int maxOffset;
/*     */   
/*     */   private WindOffsetter(int originY, RandomSource random, FloatProvider windSpeedRange, int maxOffset) {
/* 217 */     this.originY = originY;
/* 218 */     this.maxOffset = maxOffset;
/* 220 */     float speed = windSpeedRange.sample(random);
/* 222 */     float direction = Mth.randomBetween(random, 0.0F, 3.1415927F);
/* 223 */     this.windSpeed = new Vec3((Mth.cos(direction) * speed), 0.0D, (Mth.sin(direction) * speed));
/*     */   }
/*     */   
/*     */   private WindOffsetter() {
/* 227 */     this.originY = 0;
/* 228 */     this.windSpeed = null;
/* 229 */     this.maxOffset = 0;
/*     */   }
/*     */   
/*     */   private static WindOffsetter noWind() {
/* 233 */     return new WindOffsetter();
/*     */   }
/*     */   
/*     */   private BlockPos offset(BlockPos pos) {
/* 237 */     if (this.windSpeed == null)
/* 238 */       return pos; 
/* 240 */     int dy = this.originY - pos.getY();
/* 241 */     Vec3 totalWindAdjust = this.windSpeed.scale(dy);
/* 242 */     int dx = Mth.clamp(Mth.floor(totalWindAdjust.x), -this.maxOffset, this.maxOffset);
/* 243 */     int dz = Mth.clamp(Mth.floor(totalWindAdjust.z), -this.maxOffset, this.maxOffset);
/* 244 */     return pos.offset(dx, 0, dz);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\LargeDripstoneFeature$WindOffsetter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */