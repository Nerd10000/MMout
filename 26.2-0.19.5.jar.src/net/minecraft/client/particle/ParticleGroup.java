/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import java.util.ArrayDeque;
/*    */ import java.util.Iterator;
/*    */ import java.util.Objects;
/*    */ import java.util.Queue;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.ReportedException;
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
/*    */ import net.minecraft.core.particles.ParticleLimit;
/*    */ 
/*    */ public abstract class ParticleGroup<P extends Particle> {
/*    */   private static final int MAX_PARTICLES = 16384;
/*    */   
/*    */   private static final int RESERVOIR_SIZE = 4096;
/*    */   
/*    */   private static final int RESERVOIR_START = 12288;
/*    */   
/*    */   protected final ParticleEngine engine;
/*    */   
/* 20 */   protected final Queue<P> particles = new ArrayDeque<>(16384);
/*    */   
/*    */   public ParticleGroup(ParticleEngine engine) {
/* 23 */     this.engine = engine;
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 27 */     return this.particles.isEmpty();
/*    */   }
/*    */   
/*    */   public void tickParticles() {
/* 31 */     if (!this.particles.isEmpty())
/* 32 */       for (Iterator<P> iterator = this.particles.iterator(); iterator.hasNext(); ) {
/* 33 */         Particle particle = (Particle)iterator.next();
/* 34 */         tickParticle(particle);
/* 36 */         if (!particle.isAlive()) {
/* 37 */           particle.getParticleLimit().ifPresent(options -> this.engine.updateCount(options, -1));
/* 38 */           iterator.remove();
/*    */         } 
/*    */       }  
/*    */   }
/*    */   
/*    */   private void tickParticle(Particle particle) {
/*    */     try {
/* 46 */       particle.tick();
/* 47 */     } catch (Throwable t) {
/* 48 */       CrashReport report = CrashReport.forThrowable(t, "Ticking Particle");
/* 49 */       CrashReportCategory category = report.addCategory("Particle being ticked");
/* 50 */       Objects.requireNonNull(particle);
/* 50 */       category.setDetail("Particle", particle::toString);
/* 51 */       Objects.requireNonNull(particle.getGroup());
/* 51 */       category.setDetail("Particle Type", particle.getGroup()::toString);
/* 53 */       throw new ReportedException(report);
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean add(Particle particle) {
/* 58 */     int currentSize = this.particles.size();
/* 59 */     if (currentSize >= 16384)
/* 60 */       return false; 
/* 62 */     if (currentSize >= 12288) {
/* 63 */       float freeSpace = (16384 - currentSize) / 4096.0F;
/* 64 */       if (this.engine.getRandom().nextFloat() >= freeSpace * freeSpace)
/* 65 */         return false; 
/*    */     } 
/* 68 */     this.particles.add((P)particle);
/* 69 */     return true;
/*    */   }
/*    */   
/*    */   public int size() {
/* 73 */     return this.particles.size();
/*    */   }
/*    */   
/*    */   public abstract ParticleGroupRenderState extractRenderState(Frustum paramFrustum, Camera paramCamera, float paramFloat);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\ParticleGroup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */