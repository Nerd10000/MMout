/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import com.mojang.math.Axis;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.monster.dragon.EnderDragonModel;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.entity.state.EnderDragonRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class EnderDragonRenderer extends EntityRenderer<EnderDragon, EnderDragonRenderState> {
/*  31 */   public static final Identifier CRYSTAL_BEAM_LOCATION = Identifier.withDefaultNamespace("textures/entity/end_crystal/end_crystal_beam.png");
/*     */   
/*  32 */   private static final Identifier DRAGON_EXPLODING_LOCATION = Identifier.withDefaultNamespace("textures/entity/enderdragon/dragon_exploding.png");
/*     */   
/*  33 */   private static final Identifier DRAGON_TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/entity/enderdragon/dragon.png");
/*     */   
/*  34 */   private static final Identifier DRAGON_EYES_LOCATION = Identifier.withDefaultNamespace("textures/entity/enderdragon/dragon_eyes.png");
/*     */   
/*  36 */   private static final RenderType DYING_RENDER_TYPE = RenderTypes.entityCutoutDissolve(DRAGON_TEXTURE_LOCATION, DRAGON_EXPLODING_LOCATION);
/*     */   
/*  37 */   private static final RenderType EYES = RenderTypes.eyes(DRAGON_EYES_LOCATION);
/*     */   
/*  38 */   private static final RenderType BEAM = RenderTypes.endCrystalBeam(CRYSTAL_BEAM_LOCATION);
/*     */   
/*  40 */   private static final float HALF_SQRT_3 = (float)(Math.sqrt(3.0D) / 2.0D);
/*     */   
/*     */   private final EnderDragonModel model;
/*     */   
/*     */   public EnderDragonRenderer(EntityRendererProvider.Context context) {
/*  45 */     super(context);
/*  46 */     this.shadowRadius = 0.5F;
/*  48 */     this.model = new EnderDragonModel(context.bakeLayer(ModelLayers.ENDER_DRAGON));
/*     */   }
/*     */   
/*     */   public void submit(EnderDragonRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*  53 */     poseStack.pushPose();
/*  55 */     float yr = state.getHistoricalPos(7).yRot();
/*  56 */     float rot2 = (float)(state.getHistoricalPos(5).y() - state.getHistoricalPos(10).y());
/*  57 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-yr));
/*  58 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(rot2 * 10.0F));
/*  59 */     poseStack.translate(0.0F, 0.0F, 1.0F);
/*  62 */     poseStack.scale(-1.0F, -1.0F, 1.0F);
/*  65 */     poseStack.translate(0.0F, -1.501F, 0.0F);
/*  67 */     int overlayCoords = OverlayTexture.pack(0.0F, state.hasRedOverlay);
/*  68 */     if (state.deathTime > 0.0F) {
/*  70 */       int color = ARGB.white(1.0F - state.deathTime / 200.0F);
/*  71 */       submitNodeCollector.submitModel((Model)this.model, state, poseStack, DYING_RENDER_TYPE, state.lightCoords, OverlayTexture.NO_OVERLAY, color, null, state.outlineColor, null);
/*     */     } else {
/*  73 */       submitNodeCollector.submitModel((Model)this.model, state, poseStack, DRAGON_TEXTURE_LOCATION, state.lightCoords, overlayCoords, state.outlineColor, null);
/*     */     } 
/*  76 */     submitNodeCollector.submitModel((Model)this.model, state, poseStack, EYES, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
/*  78 */     if (state.deathTime > 0.0F) {
/*  79 */       float deathTime = state.deathTime / 200.0F;
/*  80 */       poseStack.pushPose();
/*  81 */       poseStack.translate(0.0F, -1.0F, -2.0F);
/*  82 */       submitRays(poseStack, deathTime, submitNodeCollector, RenderTypes.dragonRays());
/*  83 */       poseStack.popPose();
/*     */     } 
/*  86 */     poseStack.popPose();
/*  88 */     if (state.beamOffset != null)
/*  89 */       submitCrystalBeams((float)state.beamOffset.x, (float)state.beamOffset.y, (float)state.beamOffset.z, state.ageInTicks, poseStack, submitNodeCollector, state.lightCoords); 
/*  92 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*     */   }
/*     */   
/*     */   private static void submitRays(PoseStack poseStack, float deathTime, SubmitNodeCollector submitNodeCollector, RenderType renderType) {
/*  96 */     submitNodeCollector.submitCustomGeometry(poseStack, renderType, (pose, buffer) -> {
/*     */           float overDrive = Math.min((deathTime > 0.8F) ? ((deathTime - 0.8F) / 0.2F) : 0.0F, 1.0F);
/*     */           int innerColor = ARGB.colorFromFloat(1.0F - overDrive, 1.0F, 1.0F, 1.0F);
/*     */           int outerColor = 16711935;
/*     */           RandomSource random = RandomSource.createThreadLocalInstance(432L);
/*     */           Vector3f origin = new Vector3f();
/*     */           Vector3f outerLeft = new Vector3f();
/*     */           Vector3f outerRight = new Vector3f();
/*     */           Vector3f outerBottom = new Vector3f();
/*     */           Quaternionf rayRotation = new Quaternionf();
/*     */           int rayCount = Mth.floor((deathTime + deathTime * deathTime) / 2.0F * 60.0F);
/*     */           for (int i = 0; i < rayCount; i++) {
/*     */             rayRotation.rotationXYZ(random.nextFloat() * 6.2831855F, random.nextFloat() * 6.2831855F, random.nextFloat() * 6.2831855F).rotateXYZ(random.nextFloat() * 6.2831855F, random.nextFloat() * 6.2831855F, random.nextFloat() * 6.2831855F + deathTime * 1.5707964F);
/*     */             pose.rotate((Quaternionfc)rayRotation);
/*     */             float length = random.nextFloat() * 20.0F + 5.0F + overDrive * 10.0F;
/*     */             float width = random.nextFloat() * 2.0F + 1.0F + overDrive * 2.0F;
/*     */             outerLeft.set(-HALF_SQRT_3 * width, length, -0.5F * width);
/*     */             outerRight.set(HALF_SQRT_3 * width, length, -0.5F * width);
/*     */             outerBottom.set(0.0F, length, width);
/*     */             buffer.addVertex(pose, (Vector3fc)origin).setColor(innerColor);
/*     */             buffer.addVertex(pose, (Vector3fc)outerLeft).setColor(16711935);
/*     */             buffer.addVertex(pose, (Vector3fc)outerRight).setColor(16711935);
/*     */             buffer.addVertex(pose, (Vector3fc)origin).setColor(innerColor);
/*     */             buffer.addVertex(pose, (Vector3fc)outerRight).setColor(16711935);
/*     */             buffer.addVertex(pose, (Vector3fc)outerBottom).setColor(16711935);
/*     */             buffer.addVertex(pose, (Vector3fc)origin).setColor(innerColor);
/*     */             buffer.addVertex(pose, (Vector3fc)outerBottom).setColor(16711935);
/*     */             buffer.addVertex(pose, (Vector3fc)outerLeft).setColor(16711935);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public static void submitCrystalBeams(float deltaX, float deltaY, float deltaZ, float timeInTicks, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
/* 145 */     float horizontalLength = Mth.sqrt(deltaX * deltaX + deltaZ * deltaZ);
/* 146 */     float length = Mth.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
/* 148 */     poseStack.pushPose();
/* 149 */     poseStack.translate(0.0F, 2.0F, 0.0F);
/* 150 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotation((float)-Math.atan2(deltaZ, deltaX) - 1.5707964F));
/* 151 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotation((float)-Math.atan2(horizontalLength, deltaY) - 1.5707964F));
/* 153 */     float v0 = 0.0F - timeInTicks * 0.01F;
/* 154 */     float v1 = length / 32.0F - timeInTicks * 0.01F;
/* 156 */     submitNodeCollector.submitCustomGeometry(poseStack, BEAM, (pose, buffer) -> {
/*     */           int steps = 8;
/*     */           float lastSin = 0.0F;
/*     */           float lastCos = 0.75F;
/*     */           float lastU = 0.0F;
/*     */           for (int i = 1; i <= 8; i++) {
/*     */             float sin = Mth.sin((i * 6.2831855F / 8.0F)) * 0.75F;
/*     */             float cos = Mth.cos((i * 6.2831855F / 8.0F)) * 0.75F;
/*     */             float u = i / 8.0F;
/*     */             buffer.addVertex(pose, lastSin * 0.2F, lastCos * 0.2F, 0.0F).setColor(-16777216).setUv(lastU, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(pose, 0.0F, -1.0F, 0.0F);
/*     */             buffer.addVertex(pose, lastSin, lastCos, length).setColor(-1).setUv(lastU, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(pose, 0.0F, -1.0F, 0.0F);
/*     */             buffer.addVertex(pose, sin, cos, length).setColor(-1).setUv(u, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(pose, 0.0F, -1.0F, 0.0F);
/*     */             buffer.addVertex(pose, sin * 0.2F, cos * 0.2F, 0.0F).setColor(-16777216).setUv(u, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(pose, 0.0F, -1.0F, 0.0F);
/*     */             lastSin = sin;
/*     */             lastCos = cos;
/*     */             lastU = u;
/*     */           } 
/*     */         });
/* 202 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public EnderDragonRenderState createRenderState() {
/* 207 */     return new EnderDragonRenderState();
/*     */   }
/*     */   
/*     */   public void extractRenderState(EnderDragon entity, EnderDragonRenderState state, float partialTicks) {
/* 212 */     super.extractRenderState(entity, state, partialTicks);
/* 213 */     state.flapTime = Mth.lerp(partialTicks, entity.oFlapTime, entity.flapTime);
/* 214 */     state.deathTime = (entity.dragonDeathTime > 0) ? (entity.dragonDeathTime + partialTicks) : 0.0F;
/* 215 */     state.hasRedOverlay = (entity.hurtTime > 0);
/* 217 */     EndCrystal nearestCrystal = entity.nearestCrystal;
/* 218 */     if (nearestCrystal != null) {
/* 220 */       Vec3 crystalPosition = nearestCrystal.getPosition(partialTicks).add(0.0D, EndCrystalRenderer.getY(nearestCrystal.time + partialTicks), 0.0D);
/* 221 */       state.beamOffset = crystalPosition.subtract(entity.getPosition(partialTicks));
/*     */     } else {
/* 223 */       state.beamOffset = null;
/*     */     } 
/* 226 */     DragonPhaseInstance phase = entity.getPhaseManager().getCurrentPhase();
/* 227 */     state.isLandingOrTakingOff = (phase == EnderDragonPhase.LANDING || phase == EnderDragonPhase.TAKEOFF);
/* 228 */     state.isSitting = phase.isSitting();
/* 230 */     BlockPos egg = entity.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(entity.getFightOrigin()));
/* 231 */     state.distanceToEgg = egg.distToCenterSqr((Position)entity.position());
/* 233 */     state.partialTicks = entity.isDeadOrDying() ? 0.0F : partialTicks;
/* 234 */     state.flightHistory.copyFrom(entity.flightHistory);
/*     */   }
/*     */   
/*     */   protected boolean affectedByCulling(EnderDragon entity) {
/* 239 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\EnderDragonRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */