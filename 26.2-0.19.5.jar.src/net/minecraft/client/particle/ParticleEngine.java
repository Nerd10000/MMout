/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Queues;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Queue;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.client.renderer.state.level.ParticlesRenderState;
/*     */ import net.minecraft.core.particles.ParticleLimit;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ public class ParticleEngine {
/*  25 */   private static final List<ParticleRenderType> RENDER_ORDER = List.of(ParticleRenderType.SINGLE_QUADS, ParticleRenderType.ITEM_PICKUP, ParticleRenderType.ELDER_GUARDIANS);
/*     */   
/*     */   protected ClientLevel level;
/*     */   
/*  32 */   private final Map<ParticleRenderType, ParticleGroup<?>> particles = Maps.newIdentityHashMap();
/*     */   
/*  33 */   private final Queue<TrackingEmitter> trackingEmitters = Queues.newArrayDeque();
/*     */   
/*  35 */   private final Queue<Particle> particlesToAdd = Queues.newArrayDeque();
/*     */   
/*  37 */   private final Object2IntOpenHashMap<ParticleLimit> trackedParticleCounts = new Object2IntOpenHashMap();
/*     */   
/*     */   private final ParticleResources resourceManager;
/*     */   
/*  40 */   private final RandomSource random = RandomSource.create();
/*     */   
/*     */   public ParticleEngine(ClientLevel level, ParticleResources resourceManager) {
/*  43 */     this.level = level;
/*  44 */     this.resourceManager = resourceManager;
/*     */   }
/*     */   
/*     */   public void createTrackingEmitter(Entity entity, ParticleOptions particle) {
/*  48 */     this.trackingEmitters.add(new TrackingEmitter(this.level, entity, particle));
/*     */   }
/*     */   
/*     */   public void createTrackingEmitter(Entity entity, ParticleOptions particle, int lifeTime) {
/*  52 */     this.trackingEmitters.add(new TrackingEmitter(this.level, entity, particle, lifeTime));
/*     */   }
/*     */   
/*     */   public Particle createParticle(ParticleOptions options, double x, double y, double z, double xa, double ya, double za) {
/*  56 */     Particle particle = makeParticle(options, x, y, z, xa, ya, za);
/*  57 */     if (particle != null) {
/*  58 */       add(particle);
/*  59 */       return particle;
/*     */     } 
/*  61 */     return null;
/*     */   }
/*     */   
/*     */   private <T extends ParticleOptions> Particle makeParticle(T options, double x, double y, double z, double xa, double ya, double za) {
/*  66 */     ParticleProvider<T> provider = (ParticleProvider<T>)this.resourceManager.getProviders().get(BuiltInRegistries.PARTICLE_TYPE.getId(options.getType()));
/*  67 */     if (provider == null)
/*  68 */       return null; 
/*  70 */     return provider.createParticle(options, this.level, x, y, z, xa, ya, za, this.random);
/*     */   }
/*     */   
/*     */   public void add(Particle p) {
/*  74 */     Optional<ParticleLimit> limit = p.getParticleLimit();
/*  75 */     if (limit.isPresent()) {
/*  76 */       if (hasSpaceInParticleLimit(limit.get())) {
/*  77 */         this.particlesToAdd.add(p);
/*  78 */         updateCount(limit.get(), 1);
/*     */       } 
/*     */     } else {
/*  81 */       this.particlesToAdd.add(p);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tick() {
/*  86 */     this.particles.forEach((type, group) -> {
/*     */           Profiler.get().push(type.name());
/*     */           group.tickParticles();
/*     */           Profiler.get().pop();
/*     */         });
/*  92 */     if (!this.trackingEmitters.isEmpty()) {
/*  93 */       List<TrackingEmitter> removed = Lists.newArrayList();
/*  94 */       for (TrackingEmitter emitter : this.trackingEmitters) {
/*  95 */         emitter.tick();
/*  96 */         if (!emitter.isAlive())
/*  97 */           removed.add(emitter); 
/*     */       } 
/* 100 */       this.trackingEmitters.removeAll(removed);
/*     */     } 
/* 103 */     if (!this.particlesToAdd.isEmpty()) {
/*     */       Particle particle;
/* 105 */       while ((particle = this.particlesToAdd.poll()) != null) {
/* 106 */         if (!((ParticleGroup)this.particles.computeIfAbsent(particle.getGroup(), this::createParticleGroup)).add(particle))
/* 107 */           particle.getParticleLimit().ifPresent(options -> updateCount(options, -1)); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private ParticleGroup<?> createParticleGroup(ParticleRenderType type) {
/* 114 */     if (type == ParticleRenderType.ITEM_PICKUP)
/* 115 */       return new ItemPickupParticleGroup(this); 
/* 117 */     if (type == ParticleRenderType.ELDER_GUARDIANS)
/* 118 */       return new ElderGuardianParticleGroup(this); 
/* 120 */     if (type == ParticleRenderType.NO_RENDER)
/* 121 */       return new NoRenderParticleGroup(this); 
/* 123 */     return new QuadParticleGroup(this, type);
/*     */   }
/*     */   
/*     */   protected void updateCount(ParticleLimit limit, int change) {
/* 127 */     this.trackedParticleCounts.addTo(limit, change);
/*     */   }
/*     */   
/*     */   public void extract(ParticlesRenderState particlesRenderState, Frustum frustum, Camera camera, float partialTickTime) {
/* 131 */     for (ParticleRenderType particleType : RENDER_ORDER) {
/* 132 */       ParticleGroup<?> particles = this.particles.get(particleType);
/* 133 */       if (particles != null && !particles.isEmpty())
/* 134 */         particlesRenderState.add(particles.extractRenderState(frustum, camera, partialTickTime)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setLevel(ClientLevel level) {
/* 140 */     this.level = level;
/* 141 */     clearParticles();
/* 142 */     this.trackingEmitters.clear();
/*     */   }
/*     */   
/*     */   public String countParticles() {
/* 146 */     StringBuilder builder = new StringBuilder();
/* 147 */     int total = 0;
/* 148 */     for (Map.Entry<ParticleRenderType, ParticleGroup<?>> group : this.particles.entrySet()) {
/* 149 */       builder.append(((ParticleRenderType)group.getKey()).shorthand()).append(" ");
/* 150 */       int size = ((ParticleGroup)group.getValue()).size();
/* 151 */       builder.append(size).append(" ");
/* 152 */       total += size;
/*     */     } 
/* 154 */     builder.append("T ");
/* 155 */     builder.append(total);
/* 156 */     return builder.toString();
/*     */   }
/*     */   
/*     */   private boolean hasSpaceInParticleLimit(ParticleLimit limit) {
/* 160 */     return (this.trackedParticleCounts.getInt(limit) < limit.limit());
/*     */   }
/*     */   
/*     */   public void clearParticles() {
/* 164 */     this.particles.clear();
/* 165 */     this.particlesToAdd.clear();
/* 166 */     this.trackingEmitters.clear();
/* 167 */     this.trackedParticleCounts.clear();
/*     */   }
/*     */   
/*     */   public RandomSource getRandom() {
/* 171 */     return this.random;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\ParticleEngine.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */