/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.renderer.Lightmap;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.server.IntegratedServer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.LightCoordsUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.Leashable;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class EntityRenderer<T extends Entity, S extends EntityRenderState> {
/*     */   private static final float SHADOW_POWER_FALLOFF_Y = 0.5F;
/*     */   
/*     */   private static final float MAX_SHADOW_RADIUS = 32.0F;
/*     */   
/*     */   public static final float NAMETAG_SCALE = 0.025F;
/*     */   
/*     */   protected final EntityRenderDispatcher entityRenderDispatcher;
/*     */   
/*     */   private final Font font;
/*     */   
/*     */   protected float shadowRadius;
/*     */   
/*  44 */   protected float shadowStrength = 1.0F;
/*     */   
/*     */   protected EntityRenderer(EntityRendererProvider.Context context) {
/*  47 */     this.entityRenderDispatcher = context.getEntityRenderDispatcher();
/*  48 */     this.font = context.getFont();
/*     */   }
/*     */   
/*     */   public final int getPackedLightCoords(T entity, float partialTickTime) {
/*  52 */     BlockPos blockPos = BlockPos.containing((Position)entity.getLightProbePosition(partialTickTime));
/*  53 */     return LightCoordsUtil.pack(getBlockLightLevel(entity, blockPos), getSkyLightLevel(entity, blockPos));
/*     */   }
/*     */   
/*     */   protected int getSkyLightLevel(T entity, BlockPos blockPos) {
/*  57 */     return entity.level().getBrightness(LightLayer.SKY, blockPos);
/*     */   }
/*     */   
/*     */   protected int getBlockLightLevel(T entity, BlockPos blockPos) {
/*  61 */     if (entity.isOnFire())
/*  62 */       return 15; 
/*  64 */     return entity.level().getBrightness(LightLayer.BLOCK, blockPos);
/*     */   }
/*     */   
/*     */   public boolean shouldRender(T entity, Frustum culler, double camX, double camY, double camZ) {
/*  68 */     if (!entity.shouldRender(camX, camY, camZ))
/*  69 */       return false; 
/*  71 */     if (!affectedByCulling(entity))
/*  72 */       return true; 
/*  74 */     AABB boundingBox = getBoundingBoxForCulling(entity).inflate(0.5D);
/*  75 */     if (boundingBox.hasNaN() || boundingBox.getSize() == 0.0D)
/*  76 */       boundingBox = new AABB(entity.getX() - 2.0D, entity.getY() - 2.0D, entity.getZ() - 2.0D, entity.getX() + 2.0D, entity.getY() + 2.0D, entity.getZ() + 2.0D); 
/*  78 */     if (culler.isVisible(boundingBox))
/*  79 */       return true; 
/*  82 */     if (entity instanceof Leashable) {
/*  82 */       Leashable leashable = (Leashable)entity;
/*  83 */       Entity leashHolder = leashable.getLeashHolder();
/*  84 */       if (leashHolder != null) {
/*  85 */         AABB leasherBox = this.entityRenderDispatcher.<Entity>getRenderer(leashHolder).getBoundingBoxForCulling(leashHolder);
/*  86 */         return (culler.isVisible(leasherBox) || culler.isVisible(boundingBox.minmax(leasherBox)));
/*     */       } 
/*     */     } 
/*  89 */     return false;
/*     */   }
/*     */   
/*     */   protected AABB getBoundingBoxForCulling(T entity) {
/*  93 */     return entity.getBoundingBox();
/*     */   }
/*     */   
/*     */   protected boolean affectedByCulling(T entity) {
/*  97 */     return true;
/*     */   }
/*     */   
/*     */   public Vec3 getRenderOffset(S state) {
/* 101 */     if (((EntityRenderState)state).passengerOffset != null)
/* 102 */       return ((EntityRenderState)state).passengerOffset; 
/* 104 */     return Vec3.ZERO;
/*     */   }
/*     */   
/*     */   public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 108 */     if (((EntityRenderState)state).leashStates != null)
/* 109 */       for (EntityRenderState.LeashState leashState : ((EntityRenderState)state).leashStates)
/* 110 */         submitNodeCollector.submitLeash(poseStack, leashState);  
/* 114 */     submitNameDisplay(state, poseStack, submitNodeCollector, camera);
/*     */   }
/*     */   
/*     */   protected boolean shouldShowName(T entity, double distanceToCameraSq) {
/* 118 */     return (entity.shouldShowName() || (entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity));
/*     */   }
/*     */   
/*     */   public Font getFont() {
/* 122 */     return this.font;
/*     */   }
/*     */   
/*     */   protected void submitNameDisplay(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 126 */     submitNameDisplay(state, poseStack, submitNodeCollector, camera, 0);
/*     */   }
/*     */   
/*     */   protected final <S extends EntityRenderState> void submitNameDisplay(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, int offset) {
/* 130 */     poseStack.pushPose();
/* 131 */     if (((EntityRenderState)state).scoreText != null) {
/* 132 */       submitNodeCollector.submitNameTag(poseStack, ((EntityRenderState)state).nameTagAttachment, offset, ((EntityRenderState)state).scoreText, !((EntityRenderState)state).isDiscrete, ((EntityRenderState)state).lightCoords, camera);
/* 133 */       Objects.requireNonNull(getFont());
/* 133 */       poseStack.translate(0.0F, 9.0F * 1.15F * 0.025F, 0.0F);
/*     */     } 
/* 135 */     if (((EntityRenderState)state).nameTag != null)
/* 136 */       submitNodeCollector.submitNameTag(poseStack, ((EntityRenderState)state).nameTagAttachment, offset, ((EntityRenderState)state).nameTag, !((EntityRenderState)state).isDiscrete, ((EntityRenderState)state).lightCoords, camera); 
/* 138 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   protected Component getNameTag(T entity) {
/* 142 */     return entity.getDisplayName();
/*     */   }
/*     */   
/*     */   protected float getShadowRadius(S state) {
/* 146 */     return this.shadowRadius;
/*     */   }
/*     */   
/*     */   protected float getShadowStrength(S state) {
/* 150 */     return this.shadowStrength;
/*     */   }
/*     */   
/*     */   public abstract S createRenderState();
/*     */   
/*     */   public final S createRenderState(T entity, float partialTicks) {
/* 156 */     S state = createRenderState();
/* 157 */     extractRenderState(entity, state, partialTicks);
/* 159 */     finalizeRenderState(entity, state);
/* 160 */     return state;
/*     */   }
/*     */   
/*     */   public void extractRenderState(T entity, S state, float partialTicks) {
/*     */     // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: aload_1
/*     */     //   2: invokevirtual getType : ()Lnet/minecraft/world/entity/EntityType;
/*     */     //   5: putfield entityType : Lnet/minecraft/world/entity/EntityType;
/*     */     //   8: aload_2
/*     */     //   9: fload_3
/*     */     //   10: f2d
/*     */     //   11: aload_1
/*     */     //   12: getfield xOld : D
/*     */     //   15: aload_1
/*     */     //   16: invokevirtual getX : ()D
/*     */     //   19: invokestatic lerp : (DDD)D
/*     */     //   22: putfield x : D
/*     */     //   25: aload_2
/*     */     //   26: fload_3
/*     */     //   27: f2d
/*     */     //   28: aload_1
/*     */     //   29: getfield yOld : D
/*     */     //   32: aload_1
/*     */     //   33: invokevirtual getY : ()D
/*     */     //   36: invokestatic lerp : (DDD)D
/*     */     //   39: putfield y : D
/*     */     //   42: aload_2
/*     */     //   43: fload_3
/*     */     //   44: f2d
/*     */     //   45: aload_1
/*     */     //   46: getfield zOld : D
/*     */     //   49: aload_1
/*     */     //   50: invokevirtual getZ : ()D
/*     */     //   53: invokestatic lerp : (DDD)D
/*     */     //   56: putfield z : D
/*     */     //   59: aload_2
/*     */     //   60: aload_1
/*     */     //   61: invokevirtual isInvisible : ()Z
/*     */     //   64: putfield isInvisible : Z
/*     */     //   67: aload_2
/*     */     //   68: aload_1
/*     */     //   69: getfield tickCount : I
/*     */     //   72: i2f
/*     */     //   73: fload_3
/*     */     //   74: fadd
/*     */     //   75: putfield ageInTicks : F
/*     */     //   78: aload_2
/*     */     //   79: aload_1
/*     */     //   80: invokevirtual getBbWidth : ()F
/*     */     //   83: putfield boundingBoxWidth : F
/*     */     //   86: aload_2
/*     */     //   87: aload_1
/*     */     //   88: invokevirtual getBbHeight : ()F
/*     */     //   91: putfield boundingBoxHeight : F
/*     */     //   94: aload_2
/*     */     //   95: aload_1
/*     */     //   96: invokevirtual getEyeHeight : ()F
/*     */     //   99: putfield eyeHeight : F
/*     */     //   102: aload_1
/*     */     //   103: invokevirtual isPassenger : ()Z
/*     */     //   106: ifeq -> 240
/*     */     //   109: aload_1
/*     */     //   110: invokevirtual getVehicle : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   113: astore #6
/*     */     //   115: aload #6
/*     */     //   117: instanceof net/minecraft/world/entity/vehicle/minecart/AbstractMinecart
/*     */     //   120: ifeq -> 240
/*     */     //   123: aload #6
/*     */     //   125: checkcast net/minecraft/world/entity/vehicle/minecart/AbstractMinecart
/*     */     //   128: astore #4
/*     */     //   130: aload #4
/*     */     //   132: invokevirtual getBehavior : ()Lnet/minecraft/world/entity/vehicle/minecart/MinecartBehavior;
/*     */     //   135: astore #6
/*     */     //   137: aload #6
/*     */     //   139: instanceof net/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior
/*     */     //   142: ifeq -> 240
/*     */     //   145: aload #6
/*     */     //   147: checkcast net/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior
/*     */     //   150: astore #5
/*     */     //   152: aload #5
/*     */     //   154: invokevirtual cartHasPosRotLerp : ()Z
/*     */     //   157: ifeq -> 240
/*     */     //   160: fload_3
/*     */     //   161: f2d
/*     */     //   162: aload #4
/*     */     //   164: getfield xOld : D
/*     */     //   167: aload #4
/*     */     //   169: invokevirtual getX : ()D
/*     */     //   172: invokestatic lerp : (DDD)D
/*     */     //   175: dstore #6
/*     */     //   177: fload_3
/*     */     //   178: f2d
/*     */     //   179: aload #4
/*     */     //   181: getfield yOld : D
/*     */     //   184: aload #4
/*     */     //   186: invokevirtual getY : ()D
/*     */     //   189: invokestatic lerp : (DDD)D
/*     */     //   192: dstore #8
/*     */     //   194: fload_3
/*     */     //   195: f2d
/*     */     //   196: aload #4
/*     */     //   198: getfield zOld : D
/*     */     //   201: aload #4
/*     */     //   203: invokevirtual getZ : ()D
/*     */     //   206: invokestatic lerp : (DDD)D
/*     */     //   209: dstore #10
/*     */     //   211: aload_2
/*     */     //   212: aload #5
/*     */     //   214: fload_3
/*     */     //   215: invokevirtual getCartLerpPosition : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   218: new net/minecraft/world/phys/Vec3
/*     */     //   221: dup
/*     */     //   222: dload #6
/*     */     //   224: dload #8
/*     */     //   226: dload #10
/*     */     //   228: invokespecial <init> : (DDD)V
/*     */     //   231: invokevirtual subtract : (Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   234: putfield passengerOffset : Lnet/minecraft/world/phys/Vec3;
/*     */     //   237: goto -> 245
/*     */     //   240: aload_2
/*     */     //   241: aconst_null
/*     */     //   242: putfield passengerOffset : Lnet/minecraft/world/phys/Vec3;
/*     */     //   245: aload_0
/*     */     //   246: aload_1
/*     */     //   247: aload_2
/*     */     //   248: fload_3
/*     */     //   249: invokevirtual extractNameTags : (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;F)V
/*     */     //   252: aload_2
/*     */     //   253: aload_1
/*     */     //   254: invokevirtual isDiscrete : ()Z
/*     */     //   257: putfield isDiscrete : Z
/*     */     //   260: aload_1
/*     */     //   261: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   264: astore #4
/*     */     //   266: aload_1
/*     */     //   267: instanceof net/minecraft/world/entity/Leashable
/*     */     //   270: ifeq -> 748
/*     */     //   273: aload_1
/*     */     //   274: checkcast net/minecraft/world/entity/Leashable
/*     */     //   277: astore #5
/*     */     //   279: aload #5
/*     */     //   281: invokeinterface getLeashHolder : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   286: ifnull -> 748
/*     */     //   289: aload #5
/*     */     //   291: invokeinterface getLeashHolder : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   296: astore #6
/*     */     //   298: aload_1
/*     */     //   299: fload_3
/*     */     //   300: invokevirtual getPreciseBodyRotation : (F)F
/*     */     //   303: ldc_w 0.017453292
/*     */     //   306: fmul
/*     */     //   307: fstore #7
/*     */     //   309: aload #5
/*     */     //   311: fload_3
/*     */     //   312: invokeinterface getLeashOffset : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   317: astore #8
/*     */     //   319: aload_1
/*     */     //   320: fload_3
/*     */     //   321: invokevirtual getEyePosition : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   324: invokestatic containing : (Lnet/minecraft/core/Position;)Lnet/minecraft/core/BlockPos;
/*     */     //   327: astore #9
/*     */     //   329: aload #6
/*     */     //   331: fload_3
/*     */     //   332: invokevirtual getEyePosition : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   335: invokestatic containing : (Lnet/minecraft/core/Position;)Lnet/minecraft/core/BlockPos;
/*     */     //   338: astore #10
/*     */     //   340: aload_0
/*     */     //   341: aload_1
/*     */     //   342: aload #9
/*     */     //   344: invokevirtual getBlockLightLevel : (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;)I
/*     */     //   347: istore #11
/*     */     //   349: aload_0
/*     */     //   350: getfield entityRenderDispatcher : Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;
/*     */     //   353: aload #6
/*     */     //   355: invokevirtual getRenderer : (Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/client/renderer/entity/EntityRenderer;
/*     */     //   358: aload #6
/*     */     //   360: aload #10
/*     */     //   362: invokevirtual getBlockLightLevel : (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;)I
/*     */     //   365: istore #12
/*     */     //   367: aload #4
/*     */     //   369: getstatic net/minecraft/world/level/LightLayer.SKY : Lnet/minecraft/world/level/LightLayer;
/*     */     //   372: aload #9
/*     */     //   374: invokevirtual getBrightness : (Lnet/minecraft/world/level/LightLayer;Lnet/minecraft/core/BlockPos;)I
/*     */     //   377: istore #13
/*     */     //   379: aload #4
/*     */     //   381: getstatic net/minecraft/world/level/LightLayer.SKY : Lnet/minecraft/world/level/LightLayer;
/*     */     //   384: aload #10
/*     */     //   386: invokevirtual getBrightness : (Lnet/minecraft/world/level/LightLayer;Lnet/minecraft/core/BlockPos;)I
/*     */     //   389: istore #14
/*     */     //   391: aload #6
/*     */     //   393: invokevirtual supportQuadLeashAsHolder : ()Z
/*     */     //   396: ifeq -> 413
/*     */     //   399: aload #5
/*     */     //   401: invokeinterface supportQuadLeash : ()Z
/*     */     //   406: ifeq -> 413
/*     */     //   409: iconst_1
/*     */     //   410: goto -> 414
/*     */     //   413: iconst_0
/*     */     //   414: istore #15
/*     */     //   416: iload #15
/*     */     //   418: ifeq -> 425
/*     */     //   421: iconst_4
/*     */     //   422: goto -> 426
/*     */     //   425: iconst_1
/*     */     //   426: istore #16
/*     */     //   428: aload_2
/*     */     //   429: getfield leashStates : Ljava/util/List;
/*     */     //   432: ifnull -> 449
/*     */     //   435: aload_2
/*     */     //   436: getfield leashStates : Ljava/util/List;
/*     */     //   439: invokeinterface size : ()I
/*     */     //   444: iload #16
/*     */     //   446: if_icmpeq -> 495
/*     */     //   449: aload_2
/*     */     //   450: new java/util/ArrayList
/*     */     //   453: dup
/*     */     //   454: iload #16
/*     */     //   456: invokespecial <init> : (I)V
/*     */     //   459: putfield leashStates : Ljava/util/List;
/*     */     //   462: iconst_0
/*     */     //   463: istore #17
/*     */     //   465: iload #17
/*     */     //   467: iload #16
/*     */     //   469: if_icmpge -> 495
/*     */     //   472: aload_2
/*     */     //   473: getfield leashStates : Ljava/util/List;
/*     */     //   476: new net/minecraft/client/renderer/entity/state/EntityRenderState$LeashState
/*     */     //   479: dup
/*     */     //   480: invokespecial <init> : ()V
/*     */     //   483: invokeinterface add : (Ljava/lang/Object;)Z
/*     */     //   488: pop
/*     */     //   489: iinc #17, 1
/*     */     //   492: goto -> 465
/*     */     //   495: iload #15
/*     */     //   497: ifeq -> 660
/*     */     //   500: aload #6
/*     */     //   502: fload_3
/*     */     //   503: invokevirtual getPreciseBodyRotation : (F)F
/*     */     //   506: ldc_w 0.017453292
/*     */     //   509: fmul
/*     */     //   510: fstore #17
/*     */     //   512: aload #6
/*     */     //   514: fload_3
/*     */     //   515: invokevirtual getPosition : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   518: astore #18
/*     */     //   520: aload #5
/*     */     //   522: invokeinterface getQuadLeashOffsets : ()[Lnet/minecraft/world/phys/Vec3;
/*     */     //   527: astore #19
/*     */     //   529: aload #6
/*     */     //   531: invokevirtual getQuadLeashHolderOffsets : ()[Lnet/minecraft/world/phys/Vec3;
/*     */     //   534: astore #20
/*     */     //   536: iconst_0
/*     */     //   537: istore #21
/*     */     //   539: iload #21
/*     */     //   541: iload #16
/*     */     //   543: if_icmpge -> 657
/*     */     //   546: aload_2
/*     */     //   547: getfield leashStates : Ljava/util/List;
/*     */     //   550: iload #21
/*     */     //   552: invokeinterface get : (I)Ljava/lang/Object;
/*     */     //   557: checkcast net/minecraft/client/renderer/entity/state/EntityRenderState$LeashState
/*     */     //   560: astore #22
/*     */     //   562: aload #22
/*     */     //   564: aload #19
/*     */     //   566: iload #21
/*     */     //   568: aaload
/*     */     //   569: fload #7
/*     */     //   571: fneg
/*     */     //   572: invokevirtual yRot : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   575: putfield offset : Lnet/minecraft/world/phys/Vec3;
/*     */     //   578: aload #22
/*     */     //   580: aload_1
/*     */     //   581: fload_3
/*     */     //   582: invokevirtual getPosition : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   585: aload #22
/*     */     //   587: getfield offset : Lnet/minecraft/world/phys/Vec3;
/*     */     //   590: invokevirtual add : (Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   593: putfield start : Lnet/minecraft/world/phys/Vec3;
/*     */     //   596: aload #22
/*     */     //   598: aload #18
/*     */     //   600: aload #20
/*     */     //   602: iload #21
/*     */     //   604: aaload
/*     */     //   605: fload #17
/*     */     //   607: fneg
/*     */     //   608: invokevirtual yRot : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   611: invokevirtual add : (Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   614: putfield end : Lnet/minecraft/world/phys/Vec3;
/*     */     //   617: aload #22
/*     */     //   619: iload #11
/*     */     //   621: putfield startBlockLight : I
/*     */     //   624: aload #22
/*     */     //   626: iload #12
/*     */     //   628: putfield endBlockLight : I
/*     */     //   631: aload #22
/*     */     //   633: iload #13
/*     */     //   635: putfield startSkyLight : I
/*     */     //   638: aload #22
/*     */     //   640: iload #14
/*     */     //   642: putfield endSkyLight : I
/*     */     //   645: aload #22
/*     */     //   647: iconst_0
/*     */     //   648: putfield slack : Z
/*     */     //   651: iinc #21, 1
/*     */     //   654: goto -> 539
/*     */     //   657: goto -> 745
/*     */     //   660: aload #8
/*     */     //   662: fload #7
/*     */     //   664: fneg
/*     */     //   665: invokevirtual yRot : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   668: astore #17
/*     */     //   670: aload_2
/*     */     //   671: getfield leashStates : Ljava/util/List;
/*     */     //   674: invokeinterface getFirst : ()Ljava/lang/Object;
/*     */     //   679: checkcast net/minecraft/client/renderer/entity/state/EntityRenderState$LeashState
/*     */     //   682: astore #18
/*     */     //   684: aload #18
/*     */     //   686: aload #17
/*     */     //   688: putfield offset : Lnet/minecraft/world/phys/Vec3;
/*     */     //   691: aload #18
/*     */     //   693: aload_1
/*     */     //   694: fload_3
/*     */     //   695: invokevirtual getPosition : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   698: aload #17
/*     */     //   700: invokevirtual add : (Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   703: putfield start : Lnet/minecraft/world/phys/Vec3;
/*     */     //   706: aload #18
/*     */     //   708: aload #6
/*     */     //   710: fload_3
/*     */     //   711: invokevirtual getRopeHoldPosition : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   714: putfield end : Lnet/minecraft/world/phys/Vec3;
/*     */     //   717: aload #18
/*     */     //   719: iload #11
/*     */     //   721: putfield startBlockLight : I
/*     */     //   724: aload #18
/*     */     //   726: iload #12
/*     */     //   728: putfield endBlockLight : I
/*     */     //   731: aload #18
/*     */     //   733: iload #13
/*     */     //   735: putfield startSkyLight : I
/*     */     //   738: aload #18
/*     */     //   740: iload #14
/*     */     //   742: putfield endSkyLight : I
/*     */     //   745: goto -> 753
/*     */     //   748: aload_2
/*     */     //   749: aconst_null
/*     */     //   750: putfield leashStates : Ljava/util/List;
/*     */     //   753: aload_2
/*     */     //   754: aload_1
/*     */     //   755: invokevirtual displayFireAnimation : ()Z
/*     */     //   758: putfield displayFireAnimation : Z
/*     */     //   761: invokestatic getInstance : ()Lnet/minecraft/client/Minecraft;
/*     */     //   764: astore #5
/*     */     //   766: aload #5
/*     */     //   768: aload_1
/*     */     //   769: invokevirtual shouldEntityAppearGlowing : (Lnet/minecraft/world/entity/Entity;)Z
/*     */     //   772: istore #6
/*     */     //   774: aload_2
/*     */     //   775: iload #6
/*     */     //   777: ifeq -> 790
/*     */     //   780: aload_1
/*     */     //   781: invokevirtual getTeamColor : ()I
/*     */     //   784: invokestatic opaque : (I)I
/*     */     //   787: goto -> 791
/*     */     //   790: iconst_0
/*     */     //   791: putfield outlineColor : I
/*     */     //   794: aload_2
/*     */     //   795: aload_0
/*     */     //   796: aload_1
/*     */     //   797: fload_3
/*     */     //   798: invokevirtual getPackedLightCoords : (Lnet/minecraft/world/entity/Entity;F)I
/*     */     //   801: putfield lightCoords : I
/*     */     //   804: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #164	-> 0
/*     */     //   #165	-> 8
/*     */     //   #166	-> 25
/*     */     //   #167	-> 42
/*     */     //   #169	-> 59
/*     */     //   #170	-> 67
/*     */     //   #171	-> 78
/*     */     //   #172	-> 86
/*     */     //   #173	-> 94
/*     */     //   #175	-> 102
/*     */     //   #176	-> 109
/*     */     //   #177	-> 130
/*     */     //   #178	-> 154
/*     */     //   #179	-> 160
/*     */     //   #180	-> 177
/*     */     //   #181	-> 194
/*     */     //   #182	-> 211
/*     */     //   #183	-> 237
/*     */     //   #184	-> 240
/*     */     //   #187	-> 245
/*     */     //   #189	-> 252
/*     */     //   #191	-> 260
/*     */     //   #192	-> 266
/*     */     //   #193	-> 289
/*     */     //   #194	-> 298
/*     */     //   #195	-> 309
/*     */     //   #197	-> 319
/*     */     //   #198	-> 329
/*     */     //   #200	-> 340
/*     */     //   #201	-> 349
/*     */     //   #202	-> 367
/*     */     //   #203	-> 379
/*     */     //   #205	-> 391
/*     */     //   #206	-> 416
/*     */     //   #207	-> 428
/*     */     //   #208	-> 449
/*     */     //   #209	-> 462
/*     */     //   #210	-> 472
/*     */     //   #209	-> 489
/*     */     //   #214	-> 495
/*     */     //   #216	-> 500
/*     */     //   #217	-> 512
/*     */     //   #218	-> 520
/*     */     //   #219	-> 529
/*     */     //   #220	-> 536
/*     */     //   #221	-> 546
/*     */     //   #222	-> 562
/*     */     //   #223	-> 578
/*     */     //   #224	-> 596
/*     */     //   #225	-> 617
/*     */     //   #226	-> 624
/*     */     //   #227	-> 631
/*     */     //   #228	-> 638
/*     */     //   #229	-> 645
/*     */     //   #220	-> 651
/*     */     //   #231	-> 657
/*     */     //   #232	-> 660
/*     */     //   #233	-> 670
/*     */     //   #234	-> 684
/*     */     //   #235	-> 691
/*     */     //   #236	-> 706
/*     */     //   #237	-> 717
/*     */     //   #238	-> 724
/*     */     //   #239	-> 731
/*     */     //   #240	-> 738
/*     */     //   #242	-> 745
/*     */     //   #243	-> 748
/*     */     //   #246	-> 753
/*     */     //   #248	-> 761
/*     */     //   #249	-> 766
/*     */     //   #250	-> 774
/*     */     //   #251	-> 794
/*     */     //   #252	-> 804
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   177	60	6	cartLerpX	D
/*     */     //   194	43	8	cartLerpY	D
/*     */     //   211	26	10	cartLerpZ	D
/*     */     //   130	110	4	minecart	Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecart;
/*     */     //   152	88	5	behavior	Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior;
/*     */     //   465	30	17	i	I
/*     */     //   562	89	22	leashState	Lnet/minecraft/client/renderer/entity/state/EntityRenderState$LeashState;
/*     */     //   539	118	21	i	I
/*     */     //   512	145	17	roperYRot	F
/*     */     //   520	137	18	holderPos	Lnet/minecraft/world/phys/Vec3;
/*     */     //   529	128	19	leashableAttachmentPoints	[Lnet/minecraft/world/phys/Vec3;
/*     */     //   536	121	20	roperAttachmentPoints	[Lnet/minecraft/world/phys/Vec3;
/*     */     //   670	75	17	rotatedAttachOffset	Lnet/minecraft/world/phys/Vec3;
/*     */     //   684	61	18	leashState	Lnet/minecraft/client/renderer/entity/state/EntityRenderState$LeashState;
/*     */     //   298	447	6	roper	Lnet/minecraft/world/entity/Entity;
/*     */     //   309	436	7	entityYRot	F
/*     */     //   319	426	8	attachOffset	Lnet/minecraft/world/phys/Vec3;
/*     */     //   329	416	9	entityEyePos	Lnet/minecraft/core/BlockPos;
/*     */     //   340	405	10	roperEyePos	Lnet/minecraft/core/BlockPos;
/*     */     //   349	396	11	startBlockLight	I
/*     */     //   367	378	12	endBlockLight	I
/*     */     //   379	366	13	startSkyLight	I
/*     */     //   391	354	14	endSkyLight	I
/*     */     //   416	329	15	quadConnection	Z
/*     */     //   428	317	16	leashCount	I
/*     */     //   279	469	5	leashable	Lnet/minecraft/world/entity/Leashable;
/*     */     //   0	805	0	this	Lnet/minecraft/client/renderer/entity/EntityRenderer;
/*     */     //   0	805	1	entity	Lnet/minecraft/world/entity/Entity;
/*     */     //   0	805	2	state	Lnet/minecraft/client/renderer/entity/state/EntityRenderState;
/*     */     //   0	805	3	partialTicks	F
/*     */     //   266	539	4	level	Lnet/minecraft/world/level/Level;
/*     */     //   766	39	5	minecraft	Lnet/minecraft/client/Minecraft;
/*     */     //   774	31	6	appearsGlowing	Z
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	805	0	this	Lnet/minecraft/client/renderer/entity/EntityRenderer<TT;TS;>;
/*     */     //   0	805	1	entity	TT;
/*     */     //   0	805	2	state	TS;
/*     */   }
/*     */   
/*     */   protected void extractNameTags(T entity, S state, float partialTicks) {
/* 255 */     extractNameTags(entity, state, partialTicks, 64.0D, 10.0D);
/*     */   }
/*     */   
/*     */   protected final void extractNameTags(T entity, S state, float partialTicks, double nameTagDistance, double belowNameDistance) {
/* 259 */     if (this.entityRenderDispatcher.camera != null) {
/* 260 */       ((EntityRenderState)state).distanceToCameraSq = this.entityRenderDispatcher.distanceToSqr((Entity)entity);
/* 261 */       boolean shouldShowName = (((EntityRenderState)state).distanceToCameraSq < Mth.square(nameTagDistance) && shouldShowName(entity, ((EntityRenderState)state).distanceToCameraSq));
/* 262 */       if (shouldShowName) {
/* 263 */         ((EntityRenderState)state).nameTag = getNameTag(entity);
/* 264 */         ((EntityRenderState)state).nameTagAttachment = entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getYRot(partialTicks));
/*     */       } else {
/* 266 */         ((EntityRenderState)state).nameTag = null;
/*     */       } 
/* 268 */       if (((EntityRenderState)state).distanceToCameraSq < Mth.square(belowNameDistance)) {
/* 269 */         ((EntityRenderState)state).scoreText = entity.belowNameDisplay();
/*     */       } else {
/* 271 */         ((EntityRenderState)state).scoreText = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void finalizeRenderState(T entity, S state) {
/* 277 */     Minecraft minecraft = Minecraft.getInstance();
/* 278 */     Level level = entity.level();
/* 279 */     extractShadow(state, minecraft, level);
/*     */   }
/*     */   
/*     */   private void extractShadow(S state, Minecraft minecraft, Level level) {
/* 283 */     ((EntityRenderState)state).shadowPieces.clear();
/* 284 */     if (((Boolean)minecraft.options.entityShadows().get()).booleanValue() && !((EntityRenderState)state).isInvisible) {
/* 285 */       float shadowRadius = Math.min(getShadowRadius(state), 32.0F);
/* 286 */       ((EntityRenderState)state).shadowRadius = shadowRadius;
/* 288 */       if (shadowRadius > 0.0F) {
/* 289 */         double distSq = ((EntityRenderState)state).distanceToCameraSq;
/* 290 */         float pow = (float)((1.0D - distSq / 256.0D) * getShadowStrength(state));
/* 291 */         if (pow > 0.0F) {
/* 292 */           int x0 = Mth.floor(((EntityRenderState)state).x - shadowRadius);
/* 293 */           int x1 = Mth.floor(((EntityRenderState)state).x + shadowRadius);
/* 294 */           int z0 = Mth.floor(((EntityRenderState)state).z - shadowRadius);
/* 295 */           int z1 = Mth.floor(((EntityRenderState)state).z + shadowRadius);
/* 298 */           float depth = Math.min(pow / 0.5F - 1.0F, shadowRadius);
/* 299 */           int y0 = Mth.floor(((EntityRenderState)state).y - depth);
/* 300 */           int y1 = Mth.floor(((EntityRenderState)state).y);
/* 302 */           BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 303 */           for (int z = z0; z <= z1; z++) {
/* 304 */             for (int x = x0; x <= x1; x++) {
/* 305 */               pos.set(x, 0, z);
/* 306 */               ChunkAccess chunk = level.getChunk((BlockPos)pos);
/* 307 */               for (int y = y0; y <= y1; y++) {
/* 308 */                 pos.setY(y);
/* 309 */                 extractShadowPiece(state, level, pow, pos, chunk);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } else {
/* 316 */       ((EntityRenderState)state).shadowRadius = 0.0F;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void extractShadowPiece(S state, Level level, float pow, BlockPos.MutableBlockPos pos, ChunkAccess chunk) {
/* 321 */     float powerAtDepth = pow - (float)(((EntityRenderState)state).y - pos.getY()) * 0.5F;
/* 322 */     BlockPos belowPos = pos.below();
/* 323 */     BlockState belowState = chunk.getBlockState(belowPos);
/* 324 */     if (belowState.getRenderShape() == RenderShape.INVISIBLE)
/*     */       return; 
/* 328 */     int brightness = level.getMaxLocalRawBrightness((BlockPos)pos);
/* 329 */     if (brightness <= 3)
/*     */       return; 
/* 333 */     if (!belowState.isCollisionShapeFullBlock((BlockGetter)chunk, belowPos))
/*     */       return; 
/* 337 */     VoxelShape belowShape = belowState.getShape((BlockGetter)chunk, belowPos);
/* 338 */     if (belowShape.isEmpty())
/*     */       return; 
/* 343 */     float alpha = Mth.clamp(powerAtDepth * 0.5F * Lightmap.getBrightness(level.dimensionType(), brightness), 0.0F, 1.0F);
/* 344 */     float relativeX = (float)(pos.getX() - ((EntityRenderState)state).x);
/* 345 */     float relativeY = (float)(pos.getY() - ((EntityRenderState)state).y);
/* 346 */     float relativeZ = (float)(pos.getZ() - ((EntityRenderState)state).z);
/* 347 */     ((EntityRenderState)state).shadowPieces.add(new EntityRenderState.ShadowPiece(relativeX, relativeY, relativeZ, belowShape, alpha));
/*     */   }
/*     */   
/*     */   private static Entity getServerSideEntity(Entity entity) {
/* 351 */     IntegratedServer server = Minecraft.getInstance().getSingleplayerServer();
/* 352 */     if (server != null) {
/* 353 */       ServerLevel level = server.getLevel(entity.level().dimension());
/* 354 */       if (level != null)
/* 355 */         return level.getEntity(entity.getId()); 
/*     */     } 
/* 358 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\EntityRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */