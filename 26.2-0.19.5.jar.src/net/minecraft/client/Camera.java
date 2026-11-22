/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.entity.ClientAvatarState;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.player.AbstractClientPlayer;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.renderer.Projection;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeProbe;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.FogType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.waypoints.TrackedWaypoint;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class Camera implements TrackedWaypoint.Camera {
/*     */   public static final float PROJECTION_Z_NEAR = 0.05F;
/*     */   
/*     */   private static final float DEFAULT_CAMERA_DISTANCE = 4.0F;
/*     */   
/*  46 */   private static final Vector3fc FORWARDS = (Vector3fc)new Vector3f(0.0F, 0.0F, -1.0F);
/*     */   
/*  47 */   private static final Vector3fc UP = (Vector3fc)new Vector3f(0.0F, 1.0F, 0.0F);
/*     */   
/*  48 */   private static final Vector3fc LEFT = (Vector3fc)new Vector3f(-1.0F, 0.0F, 0.0F);
/*     */   
/*     */   public static final float BASE_HUD_FOV = 70.0F;
/*     */   
/*     */   private boolean initialized;
/*     */   
/*     */   private Level level;
/*     */   
/*     */   private Entity entity;
/*     */   
/*  54 */   private Vec3 position = Vec3.ZERO;
/*     */   
/*  55 */   private final BlockPos.MutableBlockPos blockPosition = new BlockPos.MutableBlockPos();
/*     */   
/*  56 */   private final Vector3f forwards = new Vector3f(FORWARDS);
/*     */   
/*  57 */   private final Vector3f panoramicForwards = new Vector3f(FORWARDS);
/*     */   
/*  58 */   private final Vector3f up = new Vector3f(UP);
/*     */   
/*  59 */   private final Vector3f left = new Vector3f(LEFT);
/*     */   
/*     */   private float xRot;
/*     */   
/*     */   private float yRot;
/*     */   
/*  62 */   private final Quaternionf rotation = new Quaternionf();
/*     */   
/*     */   private boolean detached;
/*     */   
/*     */   private float eyeHeight;
/*     */   
/*     */   private float eyeHeightOld;
/*     */   
/*  67 */   private final Projection projection = new Projection();
/*     */   
/*  69 */   private Frustum cullFrustum = new Frustum((Matrix4fc)new Matrix4f(), new Matrix4f());
/*     */   
/*     */   private Frustum capturedFrustum;
/*     */   
/*     */   private boolean captureFrustum;
/*     */   
/*  73 */   private final Matrix4f cachedViewRotMatrix = new Matrix4f();
/*     */   
/*  74 */   private final Matrix4f cachedViewRotProjMatrix = new Matrix4f();
/*     */   
/*  75 */   private long lastProjectionVersion = -1L;
/*     */   
/*  76 */   private int matrixPropertiesDirty = -1;
/*     */   
/*     */   private static final int DIRTY_VIEW_ROT = 1;
/*     */   
/*     */   private static final int DIRTY_VIEW_ROT_PROJ = 2;
/*     */   
/*     */   private float fovModifier;
/*     */   
/*     */   private float oldFovModifier;
/*     */   
/*     */   private float fov;
/*     */   
/*     */   private float hudFov;
/*     */   
/*     */   private float depthFar;
/*     */   
/*     */   private boolean isPanoramicMode;
/*     */   
/*  89 */   private final EnvironmentAttributeProbe attributeProbe = new EnvironmentAttributeProbe();
/*     */   
/*  90 */   private final Minecraft minecraft = Minecraft.getInstance();
/*     */   
/*     */   public void tick() {
/*  93 */     if (this.level != null && this.entity != null) {
/*  94 */       this.eyeHeightOld = this.eyeHeight;
/*  95 */       this.eyeHeight += (this.entity.getEyeHeight() - this.eyeHeight) * 0.5F;
/*  96 */       this.attributeProbe.tick(this.level, this.position);
/*     */     } 
/*  98 */     tickFov();
/*     */   }
/*     */   
/*     */   public void update(DeltaTracker deltaTracker) {
/* 102 */     float renderDistance = (this.minecraft.options.getEffectiveRenderDistance() * 16);
/* 103 */     this.depthFar = Math.max(renderDistance * 4.0F, (((Integer)this.minecraft.options.cloudRange().get()).intValue() * 16));
/* 105 */     LocalPlayer player = this.minecraft.player;
/* 106 */     if (player == null || this.level == null)
/*     */       return; 
/* 109 */     if (this.entity == null)
/* 110 */       setEntity((Entity)player); 
/* 113 */     float partialTicks = getCameraEntityPartialTicks(deltaTracker);
/* 115 */     alignWithEntity(partialTicks);
/* 117 */     this.fov = calculateFov(partialTicks);
/* 118 */     this.hudFov = calculateHudFov(partialTicks);
/* 119 */     prepareCullFrustum((Matrix4fc)getViewRotationMatrix(this.cachedViewRotMatrix), createProjectionMatrixForCulling(), this.position);
/* 121 */     float windowWidth = this.minecraft.getWindow().getWidth();
/* 122 */     float windowHeight = this.minecraft.getWindow().getHeight();
/* 123 */     setupPerspective(0.05F, this.depthFar, this.fov, windowWidth, windowHeight);
/* 125 */     this.initialized = true;
/*     */   }
/*     */   
/*     */   public float getCameraEntityPartialTicks(DeltaTracker deltaTracker) {
/* 129 */     return this.level.tickRateManager().isEntityFrozen(this.entity) ? 1.0F : deltaTracker.getGameTimeDeltaPartialTick(true);
/*     */   }
/*     */   
/*     */   public void extractRenderState(CameraRenderState cameraState, float cameraEntityPartialTicks) {
/* 133 */     cameraState.initialized = isInitialized();
/* 134 */     cameraState.isPanoramicMode = this.isPanoramicMode;
/* 135 */     cameraState.isFrustumCaptured = (this.capturedFrustum != null);
/* 136 */     cameraState.smartCull = this.minecraft.smartCull;
/* 137 */     if (this.minecraft.player.isSpectator() && this.level.getBlockState((BlockPos)this.blockPosition).isSolidRender())
/* 138 */       cameraState.smartCull = false; 
/* 140 */     cameraState.pos = position();
/* 141 */     cameraState.xRot = this.xRot;
/* 142 */     cameraState.yRot = this.yRot;
/* 143 */     cameraState.blockPos = blockPosition();
/* 144 */     cameraState.orientation.set((Quaternionfc)rotation());
/* 145 */     cameraState.cullFrustum.set(this.cullFrustum);
/* 146 */     cameraState.depthFar = this.depthFar;
/* 148 */     this.projection.getMatrix(cameraState.projectionMatrix);
/* 149 */     getViewRotationMatrix(cameraState.viewRotationMatrix);
/* 151 */     Entity entity = this.entity;
/* 151 */     if (entity instanceof LivingEntity) {
/* 151 */       LivingEntity livingEntity = (LivingEntity)entity;
/* 152 */       cameraState.entityRenderState.isLiving = true;
/* 153 */       cameraState.entityRenderState.isSleeping = livingEntity.isSleeping();
/* 154 */       cameraState.entityRenderState.doesMobEffectBlockSky = (livingEntity.hasEffect(MobEffects.BLINDNESS) || livingEntity.hasEffect(MobEffects.DARKNESS));
/* 156 */       cameraState.entityRenderState.isDeadOrDying = livingEntity.isDeadOrDying();
/* 157 */       cameraState.entityRenderState.hurtDir = livingEntity.getHurtDir();
/* 159 */       cameraState.entityRenderState.hurtTime = livingEntity.hurtTime - cameraEntityPartialTicks;
/* 160 */       cameraState.entityRenderState.deathTime = livingEntity.deathTime + cameraEntityPartialTicks;
/* 161 */       cameraState.entityRenderState.hurtDuration = livingEntity.hurtDuration;
/*     */     } else {
/* 163 */       cameraState.entityRenderState.isLiving = false;
/* 164 */       cameraState.entityRenderState.isSleeping = false;
/* 165 */       cameraState.entityRenderState.doesMobEffectBlockSky = false;
/*     */     } 
/* 168 */     entity = this.entity;
/* 168 */     if (entity instanceof AbstractClientPlayer) {
/* 168 */       AbstractClientPlayer player = (AbstractClientPlayer)entity;
/* 169 */       cameraState.entityRenderState.isPlayer = true;
/* 171 */       ClientAvatarState avatarState = player.avatarState();
/* 172 */       cameraState.entityRenderState.backwardsInterpolatedWalkDistance = avatarState.getBackwardsInterpolatedWalkDistance(cameraEntityPartialTicks);
/* 173 */       cameraState.entityRenderState.bob = avatarState.getInterpolatedBob(cameraEntityPartialTicks);
/*     */     } else {
/* 175 */       cameraState.entityRenderState.isPlayer = false;
/*     */     } 
/* 177 */     cameraState.hudFov = this.hudFov;
/*     */   }
/*     */   
/*     */   private void tickFov() {
/*     */     float targetFovModifier;
/* 182 */     Entity entity = this.minecraft.getCameraEntity();
/* 182 */     if (entity instanceof AbstractClientPlayer) {
/* 182 */       AbstractClientPlayer player = (AbstractClientPlayer)entity;
/* 183 */       Options options = this.minecraft.options;
/* 184 */       boolean firstPerson = options.getCameraType().isFirstPerson();
/* 185 */       float effectScale = ((Double)options.fovEffectScale().get()).floatValue();
/* 186 */       targetFovModifier = player.getFieldOfViewModifier(firstPerson, effectScale);
/*     */     } else {
/* 188 */       targetFovModifier = 1.0F;
/*     */     } 
/* 191 */     this.oldFovModifier = this.fovModifier;
/* 192 */     this.fovModifier += (targetFovModifier - this.fovModifier) * 0.5F;
/* 193 */     this.fovModifier = Mth.clamp(this.fovModifier, 0.1F, 1.5F);
/*     */   }
/*     */   
/*     */   private Matrix4f createProjectionMatrixForCulling() {
/* 198 */     float fovForCulling = Math.max(this.fov, ((Integer)this.minecraft.options.fov().get()).intValue());
/* 199 */     Matrix4f projection = new Matrix4f();
/* 200 */     return projection.perspective(fovForCulling * 0.017453292F, this.minecraft.getWindow().getWidth() / this.minecraft.getWindow().getHeight(), 0.05F, this.depthFar, RenderSystem.getDevice().getDeviceInfo().isZZeroToOne());
/*     */   }
/*     */   
/*     */   public Frustum getCullFrustum() {
/* 204 */     return this.cullFrustum;
/*     */   }
/*     */   
/*     */   private void prepareCullFrustum(Matrix4fc modelViewMatrix, Matrix4f projectionMatrixForCulling, Vec3 cameraPos) {
/* 208 */     if (this.capturedFrustum != null && !this.captureFrustum) {
/* 209 */       this.cullFrustum = this.capturedFrustum;
/*     */     } else {
/* 211 */       this.cullFrustum = new Frustum(modelViewMatrix, projectionMatrixForCulling);
/* 212 */       this.cullFrustum.prepare(cameraPos.x(), cameraPos.y(), cameraPos.z());
/*     */     } 
/* 214 */     if (this.captureFrustum) {
/* 215 */       this.capturedFrustum = this.cullFrustum;
/* 216 */       this.captureFrustum = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Frustum getCapturedFrustum() {
/* 221 */     return this.capturedFrustum;
/*     */   }
/*     */   
/*     */   public void captureFrustum() {
/* 225 */     this.captureFrustum = true;
/*     */   }
/*     */   
/*     */   public void killFrustum() {
/* 229 */     this.capturedFrustum = null;
/*     */   }
/*     */   
/*     */   private float calculateFov(float partialTicks) {
/* 233 */     if (this.isPanoramicMode)
/* 234 */       return 90.0F; 
/* 237 */     float fov = ((Integer)this.minecraft.options.fov().get()).intValue() * Mth.lerp(partialTicks, this.oldFovModifier, this.fovModifier);
/* 239 */     return modifyFovBasedOnDeathOrFluid(partialTicks, fov);
/*     */   }
/*     */   
/*     */   private float calculateHudFov(float partialTicks) {
/* 243 */     return modifyFovBasedOnDeathOrFluid(partialTicks, 70.0F);
/*     */   }
/*     */   
/*     */   private float modifyFovBasedOnDeathOrFluid(float partialTicks, float fov) {
/* 247 */     Entity entity = this.entity;
/* 247 */     if (entity instanceof LivingEntity) {
/* 247 */       LivingEntity cameraEntity = (LivingEntity)entity;
/* 247 */       if (cameraEntity.isDeadOrDying()) {
/* 248 */         float duration = Math.min(cameraEntity.deathTime + partialTicks, 20.0F);
/* 249 */         fov /= (1.0F - 500.0F / (duration + 500.0F)) * 2.0F + 1.0F;
/*     */       } 
/*     */     } 
/* 252 */     FogType state = getFluidInCamera();
/* 253 */     if (state == FogType.LAVA || state == FogType.WATER) {
/* 254 */       float effectScale = ((Double)this.minecraft.options.fovEffectScale().get()).floatValue();
/* 255 */       fov *= Mth.lerp(effectScale, 1.0F, 0.85714287F);
/*     */     } 
/* 257 */     return fov;
/*     */   }
/*     */   
/*     */   private void alignWithEntity(float partialTicks) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   4: invokevirtual isPassenger : ()Z
/*     */     //   7: ifeq -> 150
/*     */     //   10: aload_0
/*     */     //   11: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   14: invokevirtual getVehicle : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   17: astore #4
/*     */     //   19: aload #4
/*     */     //   21: instanceof net/minecraft/world/entity/vehicle/minecart/Minecart
/*     */     //   24: ifeq -> 150
/*     */     //   27: aload #4
/*     */     //   29: checkcast net/minecraft/world/entity/vehicle/minecart/Minecart
/*     */     //   32: astore_2
/*     */     //   33: aload_2
/*     */     //   34: invokevirtual getBehavior : ()Lnet/minecraft/world/entity/vehicle/minecart/MinecartBehavior;
/*     */     //   37: astore #4
/*     */     //   39: aload #4
/*     */     //   41: instanceof net/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior
/*     */     //   44: ifeq -> 150
/*     */     //   47: aload #4
/*     */     //   49: checkcast net/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior
/*     */     //   52: astore_3
/*     */     //   53: aload_3
/*     */     //   54: invokevirtual cartHasPosRotLerp : ()Z
/*     */     //   57: ifeq -> 150
/*     */     //   60: aload_2
/*     */     //   61: aload_0
/*     */     //   62: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   65: invokevirtual getPassengerRidingPosition : (Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   68: aload_2
/*     */     //   69: invokevirtual position : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   72: invokevirtual subtract : (Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   75: aload_0
/*     */     //   76: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   79: aload_2
/*     */     //   80: invokevirtual getVehicleAttachmentPoint : (Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   83: invokevirtual subtract : (Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   86: new net/minecraft/world/phys/Vec3
/*     */     //   89: dup
/*     */     //   90: dconst_0
/*     */     //   91: fload_1
/*     */     //   92: aload_0
/*     */     //   93: getfield eyeHeightOld : F
/*     */     //   96: aload_0
/*     */     //   97: getfield eyeHeight : F
/*     */     //   100: invokestatic lerp : (FFF)F
/*     */     //   103: f2d
/*     */     //   104: dconst_0
/*     */     //   105: invokespecial <init> : (DDD)V
/*     */     //   108: invokevirtual add : (Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   111: astore #4
/*     */     //   113: aload_0
/*     */     //   114: aload_0
/*     */     //   115: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   118: fload_1
/*     */     //   119: invokevirtual getViewYRot : (F)F
/*     */     //   122: aload_0
/*     */     //   123: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   126: fload_1
/*     */     //   127: invokevirtual getViewXRot : (F)F
/*     */     //   130: invokevirtual setRotation : (FF)V
/*     */     //   133: aload_0
/*     */     //   134: aload_3
/*     */     //   135: fload_1
/*     */     //   136: invokevirtual getCartLerpPosition : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   139: aload #4
/*     */     //   141: invokevirtual add : (Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   144: invokevirtual setPosition : (Lnet/minecraft/world/phys/Vec3;)V
/*     */     //   147: goto -> 245
/*     */     //   150: aload_0
/*     */     //   151: aload_0
/*     */     //   152: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   155: fload_1
/*     */     //   156: invokevirtual getViewYRot : (F)F
/*     */     //   159: aload_0
/*     */     //   160: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   163: fload_1
/*     */     //   164: invokevirtual getViewXRot : (F)F
/*     */     //   167: invokevirtual setRotation : (FF)V
/*     */     //   170: aload_0
/*     */     //   171: fload_1
/*     */     //   172: f2d
/*     */     //   173: aload_0
/*     */     //   174: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   177: getfield xo : D
/*     */     //   180: aload_0
/*     */     //   181: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   184: invokevirtual getX : ()D
/*     */     //   187: invokestatic lerp : (DDD)D
/*     */     //   190: fload_1
/*     */     //   191: f2d
/*     */     //   192: aload_0
/*     */     //   193: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   196: getfield yo : D
/*     */     //   199: aload_0
/*     */     //   200: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   203: invokevirtual getY : ()D
/*     */     //   206: invokestatic lerp : (DDD)D
/*     */     //   209: fload_1
/*     */     //   210: aload_0
/*     */     //   211: getfield eyeHeightOld : F
/*     */     //   214: aload_0
/*     */     //   215: getfield eyeHeight : F
/*     */     //   218: invokestatic lerp : (FFF)F
/*     */     //   221: f2d
/*     */     //   222: dadd
/*     */     //   223: fload_1
/*     */     //   224: f2d
/*     */     //   225: aload_0
/*     */     //   226: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   229: getfield zo : D
/*     */     //   232: aload_0
/*     */     //   233: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   236: invokevirtual getZ : ()D
/*     */     //   239: invokestatic lerp : (DDD)D
/*     */     //   242: invokevirtual setPosition : (DDD)V
/*     */     //   245: aload_0
/*     */     //   246: aload_0
/*     */     //   247: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   250: getfield options : Lnet/minecraft/client/Options;
/*     */     //   253: invokevirtual getCameraType : ()Lnet/minecraft/client/CameraType;
/*     */     //   256: invokevirtual isFirstPerson : ()Z
/*     */     //   259: ifne -> 266
/*     */     //   262: iconst_1
/*     */     //   263: goto -> 267
/*     */     //   266: iconst_0
/*     */     //   267: putfield detached : Z
/*     */     //   270: aload_0
/*     */     //   271: getfield detached : Z
/*     */     //   274: ifeq -> 435
/*     */     //   277: aload_0
/*     */     //   278: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   281: getfield options : Lnet/minecraft/client/Options;
/*     */     //   284: invokevirtual getCameraType : ()Lnet/minecraft/client/CameraType;
/*     */     //   287: invokevirtual isMirrored : ()Z
/*     */     //   290: ifeq -> 310
/*     */     //   293: aload_0
/*     */     //   294: aload_0
/*     */     //   295: getfield yRot : F
/*     */     //   298: ldc_w 180.0
/*     */     //   301: fadd
/*     */     //   302: aload_0
/*     */     //   303: getfield xRot : F
/*     */     //   306: fneg
/*     */     //   307: invokevirtual setRotation : (FF)V
/*     */     //   310: ldc 4.0
/*     */     //   312: fstore_2
/*     */     //   313: fconst_1
/*     */     //   314: fstore_3
/*     */     //   315: aload_0
/*     */     //   316: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   319: astore #5
/*     */     //   321: aload #5
/*     */     //   323: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   326: ifeq -> 352
/*     */     //   329: aload #5
/*     */     //   331: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   334: astore #4
/*     */     //   336: aload #4
/*     */     //   338: invokevirtual getScale : ()F
/*     */     //   341: fstore_3
/*     */     //   342: aload #4
/*     */     //   344: getstatic net/minecraft/world/entity/ai/attributes/Attributes.CAMERA_DISTANCE : Lnet/minecraft/core/Holder;
/*     */     //   347: invokevirtual getAttributeValue : (Lnet/minecraft/core/Holder;)D
/*     */     //   350: d2f
/*     */     //   351: fstore_2
/*     */     //   352: fload_3
/*     */     //   353: fstore #4
/*     */     //   355: fload_2
/*     */     //   356: fstore #5
/*     */     //   358: aload_0
/*     */     //   359: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   362: invokevirtual isPassenger : ()Z
/*     */     //   365: ifeq -> 410
/*     */     //   368: aload_0
/*     */     //   369: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   372: invokevirtual getVehicle : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   375: astore #7
/*     */     //   377: aload #7
/*     */     //   379: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   382: ifeq -> 410
/*     */     //   385: aload #7
/*     */     //   387: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   390: astore #6
/*     */     //   392: aload #6
/*     */     //   394: invokevirtual getScale : ()F
/*     */     //   397: fstore #4
/*     */     //   399: aload #6
/*     */     //   401: getstatic net/minecraft/world/entity/ai/attributes/Attributes.CAMERA_DISTANCE : Lnet/minecraft/core/Holder;
/*     */     //   404: invokevirtual getAttributeValue : (Lnet/minecraft/core/Holder;)D
/*     */     //   407: d2f
/*     */     //   408: fstore #5
/*     */     //   410: aload_0
/*     */     //   411: aload_0
/*     */     //   412: fload_3
/*     */     //   413: fload_2
/*     */     //   414: fmul
/*     */     //   415: fload #4
/*     */     //   417: fload #5
/*     */     //   419: fmul
/*     */     //   420: invokestatic max : (FF)F
/*     */     //   423: invokevirtual getMaxZoom : (F)F
/*     */     //   426: fneg
/*     */     //   427: fconst_0
/*     */     //   428: fconst_0
/*     */     //   429: invokevirtual move : (FFF)V
/*     */     //   432: goto -> 499
/*     */     //   435: aload_0
/*     */     //   436: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   439: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   442: ifeq -> 499
/*     */     //   445: aload_0
/*     */     //   446: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   449: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   452: invokevirtual isSleeping : ()Z
/*     */     //   455: ifeq -> 499
/*     */     //   458: aload_0
/*     */     //   459: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   462: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   465: invokevirtual getBedOrientation : ()Lnet/minecraft/core/Direction;
/*     */     //   468: astore_2
/*     */     //   469: aload_0
/*     */     //   470: aload_2
/*     */     //   471: ifnull -> 485
/*     */     //   474: aload_2
/*     */     //   475: invokevirtual toYRot : ()F
/*     */     //   478: ldc_w 180.0
/*     */     //   481: fsub
/*     */     //   482: goto -> 486
/*     */     //   485: fconst_0
/*     */     //   486: fconst_0
/*     */     //   487: invokevirtual setRotation : (FF)V
/*     */     //   490: aload_0
/*     */     //   491: fconst_0
/*     */     //   492: ldc_w 0.15
/*     */     //   495: fconst_0
/*     */     //   496: invokevirtual move : (FFF)V
/*     */     //   499: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #261	-> 0
/*     */     //   #262	-> 33
/*     */     //   #263	-> 54
/*     */     //   #266	-> 60
/*     */     //   #267	-> 69
/*     */     //   #268	-> 80
/*     */     //   #269	-> 100
/*     */     //   #271	-> 113
/*     */     //   #272	-> 133
/*     */     //   #273	-> 147
/*     */     //   #274	-> 150
/*     */     //   #275	-> 170
/*     */     //   #277	-> 245
/*     */     //   #278	-> 270
/*     */     //   #279	-> 277
/*     */     //   #280	-> 293
/*     */     //   #282	-> 310
/*     */     //   #283	-> 313
/*     */     //   #284	-> 315
/*     */     //   #285	-> 336
/*     */     //   #286	-> 342
/*     */     //   #288	-> 352
/*     */     //   #289	-> 355
/*     */     //   #290	-> 358
/*     */     //   #291	-> 392
/*     */     //   #292	-> 399
/*     */     //   #294	-> 410
/*     */     //   #295	-> 432
/*     */     //   #296	-> 458
/*     */     //   #297	-> 469
/*     */     //   #298	-> 490
/*     */     //   #300	-> 499
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   113	34	4	positionOffset	Lnet/minecraft/world/phys/Vec3;
/*     */     //   33	117	2	minecart	Lnet/minecraft/world/entity/vehicle/minecart/Minecart;
/*     */     //   53	97	3	behavior	Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior;
/*     */     //   336	16	4	living	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   392	18	6	mount	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   313	119	2	cameraDistance	F
/*     */     //   315	117	3	cameraScale	F
/*     */     //   355	77	4	mountScale	F
/*     */     //   358	74	5	mountDistance	F
/*     */     //   469	30	2	bedOrientation	Lnet/minecraft/core/Direction;
/*     */     //   0	500	0	this	Lnet/minecraft/client/Camera;
/*     */     //   0	500	1	partialTicks	F
/*     */   }
/*     */   
/*     */   private float getMaxZoom(float cameraDist) {
/* 303 */     float jitterScale = 0.1F;
/* 304 */     for (int i = 0; i < 8; i++) {
/* 305 */       float offsetX = ((i & 0x1) * 2 - 1);
/* 306 */       float offsetY = ((i >> 1 & 0x1) * 2 - 1);
/* 307 */       float offsetZ = ((i >> 2 & 0x1) * 2 - 1);
/* 308 */       Vec3 from = this.position.add((offsetX * 0.1F), (offsetY * 0.1F), (offsetZ * 0.1F));
/* 309 */       Vec3 to = from.add((new Vec3((Vector3fc)this.forwards)).scale(-cameraDist));
/* 310 */       BlockHitResult blockHitResult = this.level.clip(new ClipContext(from, to, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, this.entity));
/* 311 */       if (blockHitResult.getType() != HitResult.Type.MISS) {
/* 312 */         float distSq = (float)blockHitResult.getLocation().distanceToSqr(this.position);
/* 313 */         if (distSq < Mth.square(cameraDist))
/* 314 */           cameraDist = Mth.sqrt(distSq); 
/*     */       } 
/*     */     } 
/* 318 */     return cameraDist;
/*     */   }
/*     */   
/*     */   public boolean isPanoramicMode() {
/* 322 */     return this.isPanoramicMode;
/*     */   }
/*     */   
/*     */   public float getFov() {
/* 326 */     return this.fov;
/*     */   }
/*     */   
/*     */   private void setupPerspective(float zNear, float zFar, float fov, float width, float height) {
/* 330 */     this.projection.setupPerspective(zNear, zFar, fov, width, height);
/*     */   }
/*     */   
/*     */   private void setupOrtho(float zNear, float zFar, float width, float height, boolean invertY) {
/* 334 */     this.projection.setupOrtho(zNear, zFar, width, height, invertY);
/*     */   }
/*     */   
/*     */   protected void move(float forwards, float up, float right) {
/* 338 */     Vector3f offset = (new Vector3f(right, up, -forwards)).rotate((Quaternionfc)this.rotation);
/* 339 */     setPosition(new Vec3(this.position.x + offset.x, this.position.y + offset.y, this.position.z + offset.z));
/*     */   }
/*     */   
/*     */   protected void setRotation(float yRot, float xRot) {
/* 343 */     this.xRot = xRot;
/* 344 */     this.yRot = yRot;
/* 346 */     this.rotation.rotationYXZ(3.1415927F - yRot * 0.017453292F, -xRot * 0.017453292F, 0.0F);
/* 352 */     FORWARDS.rotate((Quaternionfc)this.rotation, this.forwards);
/* 353 */     UP.rotate((Quaternionfc)this.rotation, this.up);
/* 354 */     LEFT.rotate((Quaternionfc)this.rotation, this.left);
/* 356 */     this.matrixPropertiesDirty |= 0x3;
/*     */   }
/*     */   
/*     */   protected void setPosition(double x, double y, double z) {
/* 360 */     setPosition(new Vec3(x, y, z));
/*     */   }
/*     */   
/*     */   protected void setPosition(Vec3 position) {
/* 364 */     this.position = position;
/* 365 */     this.blockPosition.set(position.x, position.y, position.z);
/*     */   }
/*     */   
/*     */   public Vec3 position() {
/* 370 */     return this.position;
/*     */   }
/*     */   
/*     */   public BlockPos blockPosition() {
/* 374 */     return (BlockPos)this.blockPosition;
/*     */   }
/*     */   
/*     */   public float xRot() {
/* 378 */     return this.xRot;
/*     */   }
/*     */   
/*     */   public float yRot() {
/* 382 */     return this.yRot;
/*     */   }
/*     */   
/*     */   public float yaw() {
/* 387 */     return Mth.wrapDegrees(yRot());
/*     */   }
/*     */   
/*     */   public Quaternionf rotation() {
/* 391 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public Matrix4f getViewRotationMatrix(Matrix4f dest) {
/* 395 */     if ((this.matrixPropertiesDirty & 0x1) != 0) {
/* 396 */       Quaternionf inverseRotation = rotation().conjugate(new Quaternionf());
/* 397 */       this.cachedViewRotMatrix.rotation((Quaternionfc)inverseRotation);
/* 398 */       this.matrixPropertiesDirty &= 0xFFFFFFFE;
/*     */     } 
/* 400 */     return dest.set((Matrix4fc)this.cachedViewRotMatrix);
/*     */   }
/*     */   
/*     */   public Matrix4f getViewRotationProjectionMatrix(Matrix4f dest) {
/* 404 */     long projectionVersion = this.projection.getMatrixVersion();
/* 405 */     if ((this.matrixPropertiesDirty & 0x2) != 0 || this.lastProjectionVersion != this.projection.getMatrixVersion()) {
/* 406 */       getViewRotationMatrix(this.cachedViewRotMatrix);
/* 407 */       this.projection.getMatrix(this.cachedViewRotProjMatrix);
/* 408 */       this.cachedViewRotProjMatrix.mul((Matrix4fc)this.cachedViewRotMatrix);
/* 409 */       this.matrixPropertiesDirty &= 0xFFFFFFFD;
/* 410 */       this.lastProjectionVersion = projectionVersion;
/*     */     } 
/* 412 */     return dest.set((Matrix4fc)this.cachedViewRotProjMatrix);
/*     */   }
/*     */   
/*     */   public Entity entity() {
/* 416 */     return this.entity;
/*     */   }
/*     */   
/*     */   public void setEntity(Entity entity) {
/* 420 */     this.entity = entity;
/*     */   }
/*     */   
/*     */   public boolean isInitialized() {
/* 424 */     return this.initialized;
/*     */   }
/*     */   
/*     */   public boolean isDetached() {
/* 428 */     return this.detached;
/*     */   }
/*     */   
/*     */   public EnvironmentAttributeProbe attributeProbe() {
/* 432 */     return this.attributeProbe;
/*     */   }
/*     */   
/*     */   public NearPlane getNearPlane(float fov) {
/* 436 */     double aspectRatio = this.projection.width() / this.projection.height();
/* 437 */     double planeHeight = Math.tan((fov * 0.017453292F) / 2.0D) * this.projection.zNear();
/* 438 */     double planeWidth = planeHeight * aspectRatio;
/* 440 */     Vec3 forwardsVec3 = (new Vec3((Vector3fc)this.forwards)).scale(this.projection.zNear());
/* 441 */     Vec3 leftVec3 = (new Vec3((Vector3fc)this.left)).scale(planeWidth);
/* 442 */     Vec3 upVec3 = (new Vec3((Vector3fc)this.up)).scale(planeHeight);
/* 444 */     return new NearPlane(forwardsVec3, leftVec3, upVec3);
/*     */   }
/*     */   
/*     */   public FogType getFluidInCamera() {
/* 448 */     if (!this.initialized)
/* 449 */       return FogType.NONE; 
/* 452 */     FluidState fluidState1 = this.level.getFluidState((BlockPos)this.blockPosition);
/* 453 */     if (fluidState1.is(FluidTags.WATER) && 
/* 454 */       this.position.y < (this.blockPosition.getY() + fluidState1.getHeight((BlockGetter)this.level, (BlockPos)this.blockPosition)))
/* 455 */       return FogType.WATER; 
/* 459 */     NearPlane plane = getNearPlane(((Integer)this.minecraft.options.fov().get()).intValue());
/* 462 */     List<Vec3> points = Arrays.asList(new Vec3[] { plane.forward, plane.getTopLeft(), plane.getTopRight(), plane.getBottomLeft(), plane.getBottomRight() });
/* 463 */     for (Vec3 point : points) {
/* 464 */       Vec3 offsetPos = this.position.add(point);
/* 465 */       BlockPos checkPos = BlockPos.containing((Position)offsetPos);
/* 466 */       FluidState fluidState = this.level.getFluidState(checkPos);
/* 467 */       if (fluidState.is(FluidTags.LAVA)) {
/* 468 */         if (offsetPos.y <= (fluidState.getHeight((BlockGetter)this.level, checkPos) + checkPos.getY()))
/* 469 */           return FogType.LAVA; 
/*     */         continue;
/*     */       } 
/* 472 */       BlockState state = this.level.getBlockState(checkPos);
/* 473 */       if (state.is(Blocks.POWDER_SNOW))
/* 474 */         return FogType.POWDER_SNOW; 
/*     */     } 
/* 479 */     return FogType.NONE;
/*     */   }
/*     */   
/*     */   public Vector3fc forwardVector() {
/* 483 */     return (Vector3fc)this.forwards;
/*     */   }
/*     */   
/*     */   public Vector3fc panoramicForwards() {
/* 487 */     return (Vector3fc)this.panoramicForwards;
/*     */   }
/*     */   
/*     */   public Vector3fc upVector() {
/* 491 */     return (Vector3fc)this.up;
/*     */   }
/*     */   
/*     */   public Vector3fc leftVector() {
/* 495 */     return (Vector3fc)this.left;
/*     */   }
/*     */   
/*     */   public void reset() {
/* 499 */     this.level = null;
/* 500 */     this.entity = null;
/* 501 */     this.attributeProbe.reset();
/* 502 */     this.initialized = false;
/*     */   }
/*     */   
/*     */   public void setLevel(ClientLevel level) {
/* 506 */     this.level = (Level)level;
/*     */   }
/*     */   
/*     */   public void enablePanoramicMode() {
/* 510 */     this.isPanoramicMode = true;
/* 511 */     this.panoramicForwards.set((Vector3fc)this.forwards);
/*     */   }
/*     */   
/*     */   public void disablePanoramicMode() {
/* 515 */     this.isPanoramicMode = false;
/*     */   }
/*     */   
/*     */   public static class NearPlane {
/*     */     private final Vec3 forward;
/*     */     
/*     */     private final Vec3 left;
/*     */     
/*     */     private final Vec3 up;
/*     */     
/*     */     private NearPlane(Vec3 forward, Vec3 left, Vec3 up) {
/* 524 */       this.forward = forward;
/* 525 */       this.left = left;
/* 526 */       this.up = up;
/*     */     }
/*     */     
/*     */     public Vec3 getTopLeft() {
/* 530 */       return this.forward.add(this.up).add(this.left);
/*     */     }
/*     */     
/*     */     public Vec3 getTopRight() {
/* 534 */       return this.forward.add(this.up).subtract(this.left);
/*     */     }
/*     */     
/*     */     public Vec3 getBottomLeft() {
/* 538 */       return this.forward.subtract(this.up).add(this.left);
/*     */     }
/*     */     
/*     */     public Vec3 getBottomRight() {
/* 542 */       return this.forward.subtract(this.up).subtract(this.left);
/*     */     }
/*     */     
/*     */     public Vec3 getPointOnPlane(float x, float y) {
/* 546 */       return this.forward.add(this.up.scale(y)).subtract(this.left.scale(x));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\Camera.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */