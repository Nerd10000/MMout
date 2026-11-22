/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.VibrationParticleOption;
/*     */ import net.minecraft.util.LightCoordsUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionf;
/*     */ 
/*     */ public class VibrationSignalParticle extends SingleQuadParticle {
/*     */   private final PositionSource target;
/*     */   
/*     */   private float rot;
/*     */   
/*     */   private float rotO;
/*     */   
/*     */   private float pitch;
/*     */   
/*     */   private float pitchO;
/*     */   
/*     */   private VibrationSignalParticle(ClientLevel level, double x, double y, double z, PositionSource target, int arrivalInTicks, TextureAtlasSprite sprite) {
/*  27 */     super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprite);
/*  29 */     this.quadSize = 0.3F;
/*  31 */     this.target = target;
/*  32 */     this.lifetime = arrivalInTicks;
/*  34 */     Optional<Vec3> position = target.getPosition((Level)level);
/*  35 */     if (position.isPresent()) {
/*  36 */       Vec3 destination = position.get();
/*  37 */       double dx = x - destination.x();
/*  38 */       double dy = y - destination.y();
/*  39 */       double dz = z - destination.z();
/*  40 */       this.rotO = this.rot = (float)Mth.atan2(dx, dz);
/*  41 */       this.pitchO = this.pitch = (float)Mth.atan2(dy, Math.sqrt(dx * dx + dz * dz));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
/*  47 */     float randomSway = Mth.sin(((this.age + partialTickTime - 6.2831855F) * 0.05F)) * 2.0F;
/*  48 */     float lerpedRotation = Mth.lerp(partialTickTime, this.rotO, this.rot);
/*  49 */     float lerpedPitch = Mth.lerp(partialTickTime, this.pitchO, this.pitch) + 1.5707964F;
/*  51 */     Quaternionf rotation = new Quaternionf();
/*  52 */     rotation.rotationY(lerpedRotation).rotateX(-lerpedPitch).rotateY(randomSway);
/*  53 */     extractRotatedQuad(particleTypeRenderState, camera, rotation, partialTickTime);
/*  55 */     rotation.rotationY(-3.1415927F + lerpedRotation).rotateX(lerpedPitch).rotateY(randomSway);
/*  56 */     extractRotatedQuad(particleTypeRenderState, camera, rotation, partialTickTime);
/*     */   }
/*     */   
/*     */   public int getLightCoords(float a) {
/*  61 */     return LightCoordsUtil.withBlock(super.getLightCoords(a), 15);
/*     */   }
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/*  66 */     return SingleQuadParticle.Layer.TRANSLUCENT;
/*     */   }
/*     */   
/*     */   public void tick() {
/*  71 */     this.xo = this.x;
/*  72 */     this.yo = this.y;
/*  73 */     this.zo = this.z;
/*  75 */     if (this.age++ >= this.lifetime) {
/*  76 */       remove();
/*     */       return;
/*     */     } 
/*  80 */     Optional<Vec3> position = this.target.getPosition((Level)this.level);
/*  83 */     if (position.isEmpty()) {
/*  84 */       remove();
/*     */       return;
/*     */     } 
/*  88 */     int ticksRemaining = this.lifetime - this.age;
/*  89 */     double alpha = 1.0D / ticksRemaining;
/*  91 */     Vec3 destination = position.get();
/*  93 */     this.x = Mth.lerp(alpha, this.x, destination.x());
/*  94 */     this.y = Mth.lerp(alpha, this.y, destination.y());
/*  95 */     this.z = Mth.lerp(alpha, this.z, destination.z());
/*  97 */     double dx = this.x - destination.x();
/*  98 */     double dy = this.y - destination.y();
/*  99 */     double dz = this.z - destination.z();
/* 101 */     this.rotO = this.rot;
/* 102 */     this.rot = (float)Mth.atan2(dx, dz);
/* 104 */     this.pitchO = this.pitch;
/* 105 */     this.pitch = (float)Mth.atan2(dy, Math.sqrt(dx * dx + dz * dz));
/*     */   }
/*     */   
/*     */   public static class Provider implements ParticleProvider<VibrationParticleOption> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public Provider(SpriteSet sprite) {
/* 112 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     public Particle createParticle(VibrationParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 117 */       VibrationSignalParticle particle = new VibrationSignalParticle(level, x, y, z, options.getDestination(), options.getArrivalInTicks(), this.sprite.get(random));
/* 118 */       particle.setAlpha(1.0F);
/* 119 */       return particle;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\VibrationSignalParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */