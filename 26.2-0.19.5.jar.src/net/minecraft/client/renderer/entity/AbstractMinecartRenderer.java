/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*     */ import net.minecraft.client.model.object.cart.MinecartModel;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.block.BlockModelRenderState;
/*     */ import net.minecraft.client.renderer.block.BlockModelResolver;
/*     */ import net.minecraft.client.renderer.block.model.BlockDisplayContext;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.MinecartRenderState;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*     */ import net.minecraft.world.entity.vehicle.minecart.MinecartBehavior;
/*     */ import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
/*     */ import net.minecraft.world.entity.vehicle.minecart.OldMinecartBehavior;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public abstract class AbstractMinecartRenderer<T extends AbstractMinecart, S extends MinecartRenderState> extends EntityRenderer<T, S> {
/*  25 */   private static final Identifier MINECART_LOCATION = Identifier.withDefaultNamespace("textures/entity/minecart/minecart.png");
/*     */   
/*     */   private static final float DISPLAY_BLOCK_SCALE = 0.75F;
/*     */   
/*  28 */   public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
/*     */   
/*     */   protected final MinecartModel model;
/*     */   
/*     */   private final BlockModelResolver blockModelResolver;
/*     */   
/*     */   public AbstractMinecartRenderer(EntityRendererProvider.Context context, ModelLayerLocation model) {
/*  34 */     super(context);
/*  35 */     this.shadowRadius = 0.7F;
/*  36 */     this.model = new MinecartModel(context.bakeLayer(model));
/*  37 */     this.blockModelResolver = context.getBlockModelResolver();
/*     */   }
/*     */   
/*     */   public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*  42 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*  44 */     poseStack.pushPose();
/*  46 */     long seed = ((MinecartRenderState)state).offsetSeed;
/*  48 */     float offsetX = (((float)(seed >> 16L & 0x7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
/*  49 */     float offsetY = (((float)(seed >> 20L & 0x7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
/*  50 */     float offsetZ = (((float)(seed >> 24L & 0x7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
/*  52 */     poseStack.translate(offsetX, offsetY, offsetZ);
/*  54 */     if (((MinecartRenderState)state).isNewRender) {
/*  55 */       newRender(state, poseStack);
/*     */     } else {
/*  57 */       oldRender(state, poseStack);
/*     */     } 
/*  60 */     float hurt = ((MinecartRenderState)state).hurtTime;
/*  61 */     if (hurt > 0.0F)
/*  62 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(Mth.sin(hurt) * hurt * ((MinecartRenderState)state).damageTime / 10.0F * ((MinecartRenderState)state).hurtDir)); 
/*  65 */     BlockModelRenderState displayBlockModel = ((MinecartRenderState)state).displayBlockModel;
/*  66 */     if (!displayBlockModel.isEmpty()) {
/*  67 */       poseStack.pushPose();
/*  69 */       poseStack.scale(0.75F, 0.75F, 0.75F);
/*  70 */       poseStack.translate(-0.5F, (((MinecartRenderState)state).displayOffset - 8) / 16.0F, 0.5F);
/*  71 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(90.0F));
/*  72 */       submitMinecartContents(state, displayBlockModel, poseStack, submitNodeCollector, ((MinecartRenderState)state).lightCoords);
/*  74 */       poseStack.popPose();
/*     */     } 
/*  77 */     poseStack.scale(-1.0F, -1.0F, 1.0F);
/*  78 */     submitNodeCollector.submitModel((Model)this.model, state, poseStack, MINECART_LOCATION, ((MinecartRenderState)state).lightCoords, OverlayTexture.NO_OVERLAY, ((MinecartRenderState)state).outlineColor, null);
/*  79 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private static <S extends MinecartRenderState> void newRender(S state, PoseStack poseStack) {
/*  83 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(((MinecartRenderState)state).yRot));
/*  84 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(-((MinecartRenderState)state).xRot));
/*  86 */     poseStack.translate(0.0F, 0.375F, 0.0F);
/*     */   }
/*     */   
/*     */   private static <S extends MinecartRenderState> void oldRender(S state, PoseStack poseStack) {
/*  90 */     double entityX = ((MinecartRenderState)state).x;
/*  91 */     double entityY = ((MinecartRenderState)state).y;
/*  92 */     double entityZ = ((MinecartRenderState)state).z;
/*  94 */     float xRot = ((MinecartRenderState)state).xRot;
/*  95 */     float rotation = ((MinecartRenderState)state).yRot;
/*  97 */     if (((MinecartRenderState)state).posOnRail != null && ((MinecartRenderState)state).frontPos != null && ((MinecartRenderState)state).backPos != null) {
/*  98 */       Vec3 frontPos = ((MinecartRenderState)state).frontPos;
/*  99 */       Vec3 backPos = ((MinecartRenderState)state).backPos;
/* 100 */       poseStack.translate(((MinecartRenderState)state).posOnRail.x - entityX, (frontPos.y + backPos.y) / 2.0D - entityY, ((MinecartRenderState)state).posOnRail.z - entityZ);
/* 102 */       Vec3 direction = backPos.add(-frontPos.x, -frontPos.y, -frontPos.z);
/* 103 */       if (direction.length() != 0.0D) {
/* 104 */         direction = direction.normalize();
/* 105 */         rotation = (float)(Math.atan2(direction.z, direction.x) * 180.0D / Math.PI);
/* 106 */         xRot = (float)(Math.atan(direction.y) * 73.0D);
/*     */       } 
/*     */     } 
/* 109 */     poseStack.translate(0.0F, 0.375F, 0.0F);
/* 111 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F - rotation));
/* 112 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(-xRot));
/*     */   }
/*     */   
/*     */   public void extractRenderState(T entity, S state, float partialTicks) {
/* 117 */     super.extractRenderState(entity, state, partialTicks);
/* 119 */     MinecartBehavior minecartBehavior = entity.getBehavior();
/* 119 */     if (minecartBehavior instanceof NewMinecartBehavior) {
/* 119 */       NewMinecartBehavior behavior = (NewMinecartBehavior)minecartBehavior;
/* 120 */       newExtractState(entity, behavior, state, partialTicks);
/* 121 */       ((MinecartRenderState)state).isNewRender = true;
/*     */     } else {
/* 122 */       minecartBehavior = entity.getBehavior();
/* 122 */       if (minecartBehavior instanceof OldMinecartBehavior) {
/* 122 */         OldMinecartBehavior behavior = (OldMinecartBehavior)minecartBehavior;
/* 123 */         oldExtractState(entity, behavior, state, partialTicks);
/* 124 */         ((MinecartRenderState)state).isNewRender = false;
/*     */       } 
/*     */     } 
/* 127 */     long seed = entity.getId() * 493286711L;
/* 128 */     ((MinecartRenderState)state).offsetSeed = seed * seed * 4392167121L + seed * 98761L;
/* 130 */     ((MinecartRenderState)state).hurtTime = entity.getHurtTime() - partialTicks;
/* 131 */     ((MinecartRenderState)state).hurtDir = entity.getHurtDir();
/* 132 */     ((MinecartRenderState)state).damageTime = Math.max(entity.getDamage() - partialTicks, 0.0F);
/* 134 */     ((MinecartRenderState)state).displayOffset = entity.getDisplayOffset();
/* 135 */     this.blockModelResolver.update(((MinecartRenderState)state).displayBlockModel, entity.getDisplayBlockState(), BLOCK_DISPLAY_CONTEXT);
/*     */   }
/*     */   
/*     */   private static <T extends AbstractMinecart, S extends MinecartRenderState> void newExtractState(T entity, NewMinecartBehavior behavior, S state, float partialTicks) {
/* 139 */     if (behavior.cartHasPosRotLerp()) {
/* 140 */       ((MinecartRenderState)state).renderPos = behavior.getCartLerpPosition(partialTicks);
/* 141 */       ((MinecartRenderState)state).xRot = behavior.getCartLerpXRot(partialTicks);
/* 142 */       ((MinecartRenderState)state).yRot = behavior.getCartLerpYRot(partialTicks);
/*     */     } else {
/* 144 */       ((MinecartRenderState)state).renderPos = null;
/* 145 */       ((MinecartRenderState)state).xRot = entity.getXRot();
/* 146 */       ((MinecartRenderState)state).yRot = entity.getYRot();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <T extends AbstractMinecart, S extends MinecartRenderState> void oldExtractState(T entity, OldMinecartBehavior behavior, S state, float partialTicks) {
/* 151 */     float HALF_LENGTH = 0.3F;
/* 152 */     ((MinecartRenderState)state).xRot = entity.getXRot(partialTicks);
/* 153 */     ((MinecartRenderState)state).yRot = entity.getYRot(partialTicks);
/* 155 */     double entityX = ((MinecartRenderState)state).x;
/* 156 */     double entityY = ((MinecartRenderState)state).y;
/* 157 */     double entityZ = ((MinecartRenderState)state).z;
/* 158 */     Vec3 pos = behavior.getPos(entityX, entityY, entityZ);
/* 159 */     if (pos != null) {
/* 160 */       ((MinecartRenderState)state).posOnRail = pos;
/* 161 */       Vec3 p0 = behavior.getPosOffs(entityX, entityY, entityZ, 0.30000001192092896D);
/* 162 */       Vec3 p1 = behavior.getPosOffs(entityX, entityY, entityZ, -0.30000001192092896D);
/* 163 */       ((MinecartRenderState)state).frontPos = Objects.<Vec3>requireNonNullElse(p0, pos);
/* 164 */       ((MinecartRenderState)state).backPos = Objects.<Vec3>requireNonNullElse(p1, pos);
/*     */     } else {
/* 166 */       ((MinecartRenderState)state).posOnRail = null;
/* 167 */       ((MinecartRenderState)state).frontPos = null;
/* 168 */       ((MinecartRenderState)state).backPos = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void submitMinecartContents(S state, BlockModelRenderState blockModel, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
/* 173 */     blockModel.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, ((MinecartRenderState)state).outlineColor);
/*     */   }
/*     */   
/*     */   protected AABB getBoundingBoxForCulling(T entity) {
/* 178 */     AABB aabb = super.getBoundingBoxForCulling(entity);
/* 179 */     if (!entity.getDisplayBlockState().isAir())
/* 180 */       return aabb.expandTowards(0.0D, (entity.getDisplayOffset() * 0.75F / 16.0F), 0.0D); 
/* 182 */     return aabb;
/*     */   }
/*     */   
/*     */   public Vec3 getRenderOffset(S state) {
/* 187 */     Vec3 offset = super.getRenderOffset(state);
/* 188 */     if (((MinecartRenderState)state).isNewRender && ((MinecartRenderState)state).renderPos != null)
/* 189 */       return offset.add(((MinecartRenderState)state).renderPos.x - ((MinecartRenderState)state).x, ((MinecartRenderState)state).renderPos.y - ((MinecartRenderState)state).y, ((MinecartRenderState)state).renderPos.z - ((MinecartRenderState)state).z); 
/* 191 */     return offset;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\AbstractMinecartRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */