/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public abstract class SingleQuadParticle extends Particle {
/*     */   protected float quadSize;
/*     */   
/*     */   public static interface FacingCameraMode {
/*     */     public static final FacingCameraMode LOOKAT_XYZ;
/*     */     
/*     */     public static final FacingCameraMode LOOKAT_Y;
/*     */     
/*     */     static {
/*  19 */       LOOKAT_XYZ = ((target, camera, partialTickTime) -> target.set((Quaternionfc)camera.rotation()));
/*  21 */       LOOKAT_Y = ((target, camera, partialTickTime) -> target.set(0.0F, (camera.rotation()).y, 0.0F, (camera.rotation()).w));
/*     */     }
/*     */     
/*     */     void setRotation(Quaternionf param1Quaternionf, Camera param1Camera, float param1Float);
/*     */   }
/*     */   
/*  27 */   protected float rCol = 1.0F;
/*     */   
/*  28 */   protected float gCol = 1.0F;
/*     */   
/*  29 */   protected float bCol = 1.0F;
/*     */   
/*  30 */   protected float alpha = 1.0F;
/*     */   
/*     */   protected float roll;
/*     */   
/*     */   protected float oRoll;
/*     */   
/*     */   protected TextureAtlasSprite sprite;
/*     */   
/*     */   protected SingleQuadParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
/*  36 */     super(level, x, y, z);
/*  37 */     this.sprite = sprite;
/*  38 */     this.quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F;
/*     */   }
/*     */   
/*     */   protected SingleQuadParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, TextureAtlasSprite sprite) {
/*  42 */     super(level, x, y, z, xa, ya, za);
/*  43 */     this.sprite = sprite;
/*  44 */     this.quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F;
/*     */   }
/*     */   
/*     */   public FacingCameraMode getFacingCameraMode() {
/*  48 */     return FacingCameraMode.LOOKAT_XYZ;
/*     */   }
/*     */   
/*     */   public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
/*  52 */     Quaternionf rotation = new Quaternionf();
/*  53 */     getFacingCameraMode().setRotation(rotation, camera, partialTickTime);
/*  54 */     if (this.roll != 0.0F)
/*  55 */       rotation.rotateZ(Mth.lerp(partialTickTime, this.oRoll, this.roll)); 
/*  58 */     extractRotatedQuad(particleTypeRenderState, camera, rotation, partialTickTime);
/*     */   }
/*     */   
/*     */   protected void extractRotatedQuad(QuadParticleRenderState particleTypeRenderState, Camera camera, Quaternionf rotation, float partialTickTime) {
/*  62 */     Vec3 pos = camera.position();
/*  64 */     float x = (float)(Mth.lerp(partialTickTime, this.xo, this.x) - pos.x());
/*  65 */     float y = (float)(Mth.lerp(partialTickTime, this.yo, this.y) - pos.y());
/*  66 */     float z = (float)(Mth.lerp(partialTickTime, this.zo, this.z) - pos.z());
/*  68 */     extractRotatedQuad(particleTypeRenderState, rotation, x, y, z, partialTickTime);
/*     */   }
/*     */   
/*     */   protected void extractRotatedQuad(QuadParticleRenderState particleTypeRenderState, Quaternionf rotation, float x, float y, float z, float partialTickTime) {
/*  72 */     particleTypeRenderState.add(getLayer(), x, y, z, rotation.x, rotation.y, rotation.z, rotation.w, getQuadSize(partialTickTime), getU0(), getU1(), getV0(), getV1(), ARGB.colorFromFloat(this.alpha, this.rCol, this.gCol, this.bCol), getLightCoords(partialTickTime));
/*     */   }
/*     */   
/*     */   public float getQuadSize(float a) {
/*  76 */     return this.quadSize;
/*     */   }
/*     */   
/*     */   public Particle scale(float scale) {
/*  81 */     this.quadSize *= scale;
/*  82 */     return super.scale(scale);
/*     */   }
/*     */   
/*     */   public ParticleRenderType getGroup() {
/*  87 */     return ParticleRenderType.SINGLE_QUADS;
/*     */   }
/*     */   
/*     */   public void setSpriteFromAge(SpriteSet sprites) {
/*  91 */     if (!this.removed)
/*  92 */       setSprite(sprites.get(this.age, this.lifetime)); 
/*     */   }
/*     */   
/*     */   protected void setSprite(TextureAtlasSprite icon) {
/*  97 */     this.sprite = icon;
/*     */   }
/*     */   
/*     */   protected float getU0() {
/* 101 */     return this.sprite.getU0();
/*     */   }
/*     */   
/*     */   protected float getU1() {
/* 105 */     return this.sprite.getU1();
/*     */   }
/*     */   
/*     */   protected float getV0() {
/* 109 */     return this.sprite.getV0();
/*     */   }
/*     */   
/*     */   protected float getV1() {
/* 113 */     return this.sprite.getV1();
/*     */   }
/*     */   
/*     */   protected abstract Layer getLayer();
/*     */   
/*     */   public void setColor(float r, float g, float b) {
/* 119 */     this.rCol = r;
/* 120 */     this.gCol = g;
/* 121 */     this.bCol = b;
/*     */   }
/*     */   
/*     */   protected void setAlpha(float alpha) {
/* 125 */     this.alpha = alpha;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 130 */     return getClass().getSimpleName() + ", Pos (" + getClass().getSimpleName() + "," + this.x + "," + this.y + "), RGBA (" + this.z + "," + this.rCol + "," + this.gCol + "," + this.bCol + "), Age " + this.alpha;
/*     */   }
/*     */   
/*     */   public static final class Layer extends Record {
/*     */     private final boolean translucent;
/*     */     
/*     */     private final Identifier textureAtlasLocation;
/*     */     
/*     */     private final RenderPipeline pipeline;
/*     */     
/*     */     public Layer(boolean translucent, Identifier textureAtlasLocation, RenderPipeline pipeline) {
/* 133 */       this.translucent = translucent;
/* 133 */       this.textureAtlasLocation = textureAtlasLocation;
/* 133 */       this.pipeline = pipeline;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/particle/SingleQuadParticle$Layer;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #133	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/particle/SingleQuadParticle$Layer;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/particle/SingleQuadParticle$Layer;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #133	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/particle/SingleQuadParticle$Layer;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/particle/SingleQuadParticle$Layer;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #133	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/particle/SingleQuadParticle$Layer;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public boolean translucent() {
/* 133 */       return this.translucent;
/*     */     }
/*     */     
/*     */     public Identifier textureAtlasLocation() {
/* 133 */       return this.textureAtlasLocation;
/*     */     }
/*     */     
/*     */     public RenderPipeline pipeline() {
/* 133 */       return this.pipeline;
/*     */     }
/*     */     
/* 134 */     public static final Layer OPAQUE_TERRAIN = new Layer(false, TextureAtlas.LOCATION_BLOCKS, RenderPipelines.OPAQUE_PARTICLE);
/*     */     
/* 135 */     public static final Layer TRANSLUCENT_TERRAIN = new Layer(true, TextureAtlas.LOCATION_BLOCKS, RenderPipelines.TRANSLUCENT_PARTICLE);
/*     */     
/* 136 */     public static final Layer OPAQUE_ITEMS = new Layer(false, TextureAtlas.LOCATION_ITEMS, RenderPipelines.OPAQUE_PARTICLE);
/*     */     
/* 137 */     public static final Layer TRANSLUCENT_ITEMS = new Layer(true, TextureAtlas.LOCATION_ITEMS, RenderPipelines.TRANSLUCENT_PARTICLE);
/*     */     
/* 138 */     public static final Layer OPAQUE = new Layer(false, TextureAtlas.LOCATION_PARTICLES, RenderPipelines.OPAQUE_PARTICLE);
/*     */     
/* 139 */     public static final Layer TRANSLUCENT = new Layer(true, TextureAtlas.LOCATION_PARTICLES, RenderPipelines.TRANSLUCENT_PARTICLE);
/*     */     
/*     */     public static Layer bySprite(TextureAtlasSprite sprite) {
/* 142 */       boolean translucent = sprite.transparency().hasTranslucent();
/* 143 */       if (sprite.atlasLocation().equals(TextureAtlas.LOCATION_BLOCKS))
/* 144 */         return translucent ? TRANSLUCENT_TERRAIN : OPAQUE_TERRAIN; 
/* 145 */       if (sprite.atlasLocation().equals(TextureAtlas.LOCATION_ITEMS))
/* 146 */         return translucent ? TRANSLUCENT_ITEMS : OPAQUE_ITEMS; 
/* 148 */       return translucent ? TRANSLUCENT : OPAQUE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\SingleQuadParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */