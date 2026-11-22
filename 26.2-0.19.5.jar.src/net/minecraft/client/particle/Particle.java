/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleLimit;
/*     */ import net.minecraft.util.LightCoordsUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.BlockAndLightGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ 
/*     */ public abstract class Particle {
/*  18 */   private static final AABB INITIAL_AABB = new AABB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
/*     */   
/*  19 */   private static final double MAXIMUM_COLLISION_VELOCITY_SQUARED = Mth.square(100.0D);
/*     */   
/*     */   protected final ClientLevel level;
/*     */   
/*     */   protected double xo;
/*     */   
/*     */   protected double yo;
/*     */   
/*     */   protected double zo;
/*     */   
/*     */   protected double x;
/*     */   
/*     */   protected double y;
/*     */   
/*     */   protected double z;
/*     */   
/*     */   protected double xd;
/*     */   
/*     */   protected double yd;
/*     */   
/*     */   protected double zd;
/*     */   
/*  31 */   private AABB bb = INITIAL_AABB;
/*     */   
/*     */   protected boolean onGround;
/*     */   
/*     */   protected boolean hasPhysics = true;
/*     */   
/*     */   private boolean stoppedByCollision;
/*     */   
/*     */   protected boolean removed;
/*     */   
/*  38 */   protected float bbWidth = 0.6F;
/*     */   
/*  39 */   protected float bbHeight = 1.8F;
/*     */   
/*  41 */   protected final RandomSource random = RandomSource.create();
/*     */   
/*     */   protected int age;
/*     */   
/*     */   protected int lifetime;
/*     */   
/*     */   protected float gravity;
/*     */   
/*  46 */   protected float friction = 0.98F;
/*     */   
/*     */   protected boolean speedUpWhenYMotionIsBlocked = false;
/*     */   
/*     */   protected Particle(ClientLevel level, double x, double y, double z) {
/*  50 */     this.level = level;
/*  52 */     setSize(0.2F, 0.2F);
/*  53 */     setPos(x, y, z);
/*  54 */     this.xo = x;
/*  55 */     this.yo = y;
/*  56 */     this.zo = z;
/*  58 */     this.lifetime = (int)(4.0F / (this.random.nextFloat() * 0.9F + 0.1F));
/*     */   }
/*     */   
/*     */   public Particle(ClientLevel level, double x, double y, double z, double xa, double ya, double za) {
/*  62 */     this(level, x, y, z);
/*  64 */     this.xd = xa + ((this.random.nextFloat() * 2.0F - 1.0F) * 0.4F);
/*  65 */     this.yd = ya + ((this.random.nextFloat() * 2.0F - 1.0F) * 0.4F);
/*  66 */     this.zd = za + ((this.random.nextFloat() * 2.0F - 1.0F) * 0.4F);
/*  67 */     double speed = ((this.random.nextFloat() + this.random.nextFloat() + 1.0F) * 0.15F);
/*  69 */     double dd = Math.sqrt(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd);
/*  70 */     this.xd = this.xd / dd * speed * 0.4000000059604645D;
/*  71 */     this.yd = this.yd / dd * speed * 0.4000000059604645D + 0.10000000149011612D;
/*  72 */     this.zd = this.zd / dd * speed * 0.4000000059604645D;
/*     */   }
/*     */   
/*     */   public Particle setPower(float power) {
/*  76 */     this.xd *= power;
/*  77 */     this.yd = (this.yd - 0.10000000149011612D) * power + 0.10000000149011612D;
/*  78 */     this.zd *= power;
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public void setParticleSpeed(double xd, double yd, double zd) {
/*  83 */     this.xd = xd;
/*  84 */     this.yd = yd;
/*  85 */     this.zd = zd;
/*     */   }
/*     */   
/*     */   public Particle scale(float scale) {
/*  89 */     setSize(0.2F * scale, 0.2F * scale);
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public void setLifetime(int lifetime) {
/*  94 */     this.lifetime = lifetime;
/*     */   }
/*     */   
/*     */   public int getLifetime() {
/*  98 */     return this.lifetime;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 102 */     this.xo = this.x;
/* 103 */     this.yo = this.y;
/* 104 */     this.zo = this.z;
/* 106 */     if (this.age++ >= this.lifetime) {
/* 107 */       remove();
/*     */       return;
/*     */     } 
/* 111 */     this.yd -= 0.04D * this.gravity;
/* 112 */     move(this.xd, this.yd, this.zd);
/* 113 */     if (this.speedUpWhenYMotionIsBlocked && this.y == this.yo) {
/* 114 */       this.xd *= 1.1D;
/* 115 */       this.zd *= 1.1D;
/*     */     } 
/* 117 */     this.xd *= this.friction;
/* 118 */     this.yd *= this.friction;
/* 119 */     this.zd *= this.friction;
/* 121 */     if (this.onGround) {
/* 122 */       this.xd *= 0.699999988079071D;
/* 123 */       this.zd *= 0.699999988079071D;
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract ParticleRenderType getGroup();
/*     */   
/*     */   public String toString() {
/* 131 */     return getClass().getSimpleName() + ", Pos (" + getClass().getSimpleName() + "," + this.x + "," + this.y + "), Age " + this.z;
/*     */   }
/*     */   
/*     */   public void remove() {
/* 135 */     this.removed = true;
/*     */   }
/*     */   
/*     */   protected void setSize(float w, float h) {
/* 139 */     if (w != this.bbWidth || h != this.bbHeight) {
/* 140 */       this.bbWidth = w;
/* 141 */       this.bbHeight = h;
/* 142 */       AABB aabb = getBoundingBox();
/* 143 */       double newMinX = (aabb.minX + aabb.maxX - w) / 2.0D;
/* 144 */       double newMinZ = (aabb.minZ + aabb.maxZ - w) / 2.0D;
/* 145 */       setBoundingBox(new AABB(newMinX, aabb.minY, newMinZ, newMinX + this.bbWidth, aabb.minY + this.bbHeight, newMinZ + this.bbWidth));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setPos(double x, double y, double z) {
/* 150 */     this.x = x;
/* 151 */     this.y = y;
/* 152 */     this.z = z;
/* 153 */     float w = this.bbWidth / 2.0F;
/* 154 */     float h = this.bbHeight;
/* 155 */     setBoundingBox(new AABB(x - w, y, z - w, x + w, y + h, z + w));
/*     */   }
/*     */   
/*     */   public void move(double xa, double ya, double za) {
/* 159 */     if (this.stoppedByCollision)
/*     */       return; 
/* 163 */     double originalXa = xa;
/* 164 */     double originalYa = ya;
/* 165 */     double originalZa = za;
/* 167 */     if (this.hasPhysics && (xa != 0.0D || ya != 0.0D || za != 0.0D) && xa * xa + ya * ya + za * za < MAXIMUM_COLLISION_VELOCITY_SQUARED) {
/* 168 */       Vec3 movement = Entity.collideBoundingBox(CollisionContext.positionContext(this.y), new Vec3(xa, ya, za), getBoundingBox(), (Level)this.level, List.of());
/* 169 */       xa = movement.x;
/* 170 */       ya = movement.y;
/* 171 */       za = movement.z;
/*     */     } 
/* 174 */     if (xa != 0.0D || ya != 0.0D || za != 0.0D) {
/* 175 */       setBoundingBox(getBoundingBox().move(xa, ya, za));
/* 176 */       setLocationFromBoundingbox();
/*     */     } 
/* 179 */     if (Math.abs(originalYa) >= 9.999999747378752E-6D && Math.abs(ya) < 9.999999747378752E-6D)
/* 180 */       this.stoppedByCollision = true; 
/* 183 */     this.onGround = (originalYa != ya && originalYa < 0.0D);
/* 185 */     if (originalXa != xa)
/* 186 */       this.xd = 0.0D; 
/* 188 */     if (originalZa != za)
/* 189 */       this.zd = 0.0D; 
/*     */   }
/*     */   
/*     */   protected void setLocationFromBoundingbox() {
/* 194 */     AABB aabb = getBoundingBox();
/* 195 */     this.x = (aabb.minX + aabb.maxX) / 2.0D;
/* 196 */     this.y = aabb.minY;
/* 197 */     this.z = (aabb.minZ + aabb.maxZ) / 2.0D;
/*     */   }
/*     */   
/*     */   protected int getLightCoords(float a) {
/* 201 */     BlockPos pos = BlockPos.containing(this.x, this.y, this.z);
/* 202 */     if (this.level.hasChunkAt(pos))
/* 203 */       return LightCoordsUtil.getLightCoords((BlockAndLightGetter)this.level, pos); 
/* 205 */     return 15728640;
/*     */   }
/*     */   
/*     */   public boolean isAlive() {
/* 209 */     return !this.removed;
/*     */   }
/*     */   
/*     */   public AABB getBoundingBox() {
/* 213 */     return this.bb;
/*     */   }
/*     */   
/*     */   public void setBoundingBox(AABB bb) {
/* 217 */     this.bb = bb;
/*     */   }
/*     */   
/*     */   public Optional<ParticleLimit> getParticleLimit() {
/* 221 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static final class LifetimeAlpha extends Record {
/*     */     private final float startAlpha;
/*     */     
/*     */     private final float endAlpha;
/*     */     
/*     */     private final float startAtNormalizedAge;
/*     */     
/*     */     private final float endAtNormalizedAge;
/*     */     
/*     */     public LifetimeAlpha(float startAlpha, float endAlpha, float startAtNormalizedAge, float endAtNormalizedAge) {
/* 224 */       this.startAlpha = startAlpha;
/* 224 */       this.endAlpha = endAlpha;
/* 224 */       this.startAtNormalizedAge = startAtNormalizedAge;
/* 224 */       this.endAtNormalizedAge = endAtNormalizedAge;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/particle/Particle$LifetimeAlpha;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #224	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/particle/Particle$LifetimeAlpha;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/particle/Particle$LifetimeAlpha;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #224	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/particle/Particle$LifetimeAlpha;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/particle/Particle$LifetimeAlpha;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #224	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/particle/Particle$LifetimeAlpha;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public float startAlpha() {
/* 224 */       return this.startAlpha;
/*     */     }
/*     */     
/*     */     public float endAlpha() {
/* 224 */       return this.endAlpha;
/*     */     }
/*     */     
/*     */     public float startAtNormalizedAge() {
/* 224 */       return this.startAtNormalizedAge;
/*     */     }
/*     */     
/*     */     public float endAtNormalizedAge() {
/* 224 */       return this.endAtNormalizedAge;
/*     */     }
/*     */     
/* 225 */     public static final LifetimeAlpha ALWAYS_OPAQUE = new LifetimeAlpha(1.0F, 1.0F, 0.0F, 1.0F);
/*     */     
/*     */     public boolean isOpaque() {
/* 228 */       return (this.startAlpha >= 1.0F && this.endAlpha >= 1.0F);
/*     */     }
/*     */     
/*     */     public float currentAlphaForAge(int age, int lifetime, float partialTickTime) {
/* 232 */       if (Mth.equal(this.startAlpha, this.endAlpha))
/* 233 */         return this.startAlpha; 
/* 236 */       float timeNormalized = Mth.inverseLerp((age + partialTickTime) / lifetime, this.startAtNormalizedAge, this.endAtNormalizedAge);
/* 237 */       return Mth.clampedLerp(timeNormalized, this.startAlpha, this.endAlpha);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\Particle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */