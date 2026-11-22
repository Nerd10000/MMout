/*    */ package net.minecraft.client.multiplayer;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.particles.ExplosionParticleInfo;
/*    */ import net.minecraft.server.level.ParticleStatus;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.util.random.WeightedRandom;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ClientExplosionTracker {
/*    */   private static final int MAX_PARTICLES_PER_TICK = 512;
/*    */   
/*    */   private static final class ExplosionInfo extends Record {
/*    */     private final Vec3 center;
/*    */     
/*    */     private final float radius;
/*    */     
/*    */     private final int blockCount;
/*    */     
/*    */     private final WeightedList<ExplosionParticleInfo> blockParticles;
/*    */     
/*    */     private ExplosionInfo(Vec3 center, float radius, int blockCount, WeightedList<ExplosionParticleInfo> blockParticles) {
/* 18 */       this.center = center;
/* 18 */       this.radius = radius;
/* 18 */       this.blockCount = blockCount;
/* 18 */       this.blockParticles = blockParticles;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/ClientExplosionTracker$ExplosionInfo;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientExplosionTracker$ExplosionInfo;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/ClientExplosionTracker$ExplosionInfo;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientExplosionTracker$ExplosionInfo;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/ClientExplosionTracker$ExplosionInfo;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/ClientExplosionTracker$ExplosionInfo;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Vec3 center() {
/* 18 */       return this.center;
/*    */     }
/*    */     
/*    */     public float radius() {
/* 18 */       return this.radius;
/*    */     }
/*    */     
/*    */     public int blockCount() {
/* 18 */       return this.blockCount;
/*    */     }
/*    */     
/*    */     public WeightedList<ExplosionParticleInfo> blockParticles() {
/* 18 */       return this.blockParticles;
/*    */     }
/*    */   }
/*    */   
/* 20 */   private final List<ExplosionInfo> explosions = new ArrayList<>();
/*    */   
/*    */   public void track(Vec3 center, float radius, int blockCount, WeightedList<ExplosionParticleInfo> blockParticles) {
/* 23 */     if (!blockParticles.isEmpty())
/* 24 */       this.explosions.add(new ExplosionInfo(center, radius, blockCount, blockParticles)); 
/*    */   }
/*    */   
/*    */   public void tick(ClientLevel level) {
/* 29 */     if ((Minecraft.getInstance()).options.particles().get() != ParticleStatus.ALL) {
/* 30 */       this.explosions.clear();
/*    */       return;
/*    */     } 
/* 34 */     int totalBlocks = WeightedRandom.getTotalWeight(this.explosions, ExplosionInfo::blockCount);
/* 35 */     int totalParticles = Math.min(totalBlocks, 512);
/* 36 */     for (int i = 0; i < totalParticles; i++)
/* 37 */       WeightedRandom.getRandomItem(level.getRandom(), this.explosions, totalBlocks, ExplosionInfo::blockCount).ifPresent(info -> addParticle(level, info)); 
/* 40 */     this.explosions.clear();
/*    */   }
/*    */   
/*    */   private void addParticle(ClientLevel level, ExplosionInfo explosion) {
/* 44 */     RandomSource random = level.getRandom();
/* 45 */     Vec3 center = explosion.center();
/* 47 */     Vec3 directionFromCenter = (new Vec3((random.nextFloat() * 2.0F - 1.0F), (random.nextFloat() * 2.0F - 1.0F), (random.nextFloat() * 2.0F - 1.0F))).normalize();
/* 48 */     float radius = (float)Math.cbrt(random.nextFloat()) * explosion.radius();
/* 49 */     Vec3 localPos = directionFromCenter.scale(radius);
/* 50 */     Vec3 pos = center.add(localPos);
/* 52 */     if (!level.getBlockState(BlockPos.containing((Position)pos)).isAir())
/*    */       return; 
/* 56 */     float speed = 0.5F / (radius / explosion.radius() + 0.1F) * random.nextFloat() * random.nextFloat() + 0.3F;
/* 58 */     ExplosionParticleInfo info = (ExplosionParticleInfo)explosion.blockParticles.getRandomOrThrow(random);
/* 59 */     Vec3 particlePos = center.add(localPos.scale(info.scaling()));
/* 60 */     Vec3 particleVelocity = directionFromCenter.scale((speed * info.speed()));
/* 61 */     level.addParticle(info.particle(), particlePos.x(), particlePos.y(), particlePos.z(), particleVelocity.x(), particleVelocity.y(), particleVelocity.z());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientExplosionTracker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */