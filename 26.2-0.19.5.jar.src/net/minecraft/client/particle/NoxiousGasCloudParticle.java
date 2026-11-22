/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.PotentSulfurBlockEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class NoxiousGasCloudParticle extends NoRenderParticle {
/*    */   private static final int PARTICLE_TICKS = 2;
/*    */   
/*    */   protected NoxiousGasCloudParticle(ClientLevel level, double x, double y, double z) {
/* 17 */     super(level, x, y, z);
/* 18 */     this.lifetime = 20;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 23 */     super.tick();
/* 24 */     if (this.age % 2 != 0)
/*    */       return; 
/* 28 */     BlockPos sourceBlock = BlockPos.containing(this.x, this.y, this.z);
/* 29 */     Vec3 particlePos = pickRandomParticleSpawnPoint((Level)this.level, sourceBlock);
/* 30 */     if (PotentSulfurBlockEntity.canBeReachedByNoxiousGas((Level)this.level, sourceBlock, particlePos))
/* 31 */       spawnNoxiousGasParticle((Level)this.level, particlePos); 
/*    */   }
/*    */   
/*    */   private static Vec3 pickRandomParticleSpawnPoint(Level level, BlockPos centerBlock) {
/* 36 */     RandomSource random = level.getRandom();
/* 38 */     Vec3 horizontalDirection = (new Vec3((random.nextFloat() - 0.5F), 0.0D, (random.nextFloat() - 0.5F))).normalize();
/* 39 */     float distance = random.nextFloat() * 3.0F;
/* 41 */     return Vec3.atCenterOf((Vec3i)centerBlock).add(horizontalDirection.scale(distance)).subtract(0.0D, 0.25D, 0.0D);
/*    */   }
/*    */   
/*    */   private static void spawnNoxiousGasParticle(Level level, Vec3 pos) {
/* 45 */     level.addAlwaysVisibleParticle((ParticleOptions)ParticleTypes.NOXIOUS_GAS, pos.x, pos.y, pos.z, 0.0D, 0.0D, 0.0D);
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 51 */       return new NoxiousGasCloudParticle(level, x, y, z);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\NoxiousGasCloudParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */