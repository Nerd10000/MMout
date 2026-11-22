/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.Lighting;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import com.mojang.math.Axis;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.renderer.block.BlockStateModelSet;
/*     */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.WindowRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.model.ModelBakery;
/*     */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class ScreenEffectRenderer {
/*  34 */   private static final Identifier UNDERWATER_LOCATION = Identifier.withDefaultNamespace("textures/misc/underwater.png");
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final SpriteGetter sprites;
/*     */   
/*     */   public static final int ITEM_ACTIVATION_ANIMATION_LENGTH = 40;
/*     */   
/*     */   private ItemStack itemActivationItem;
/*     */   
/*     */   private int itemActivationTicks;
/*     */   
/*     */   private float itemActivationOffX;
/*     */   
/*     */   private float itemActivationOffY;
/*     */   
/*     */   public ScreenEffectRenderer(Minecraft minecraft, SpriteGetter sprites) {
/*  47 */     this.minecraft = minecraft;
/*  48 */     this.sprites = sprites;
/*     */   }
/*     */   
/*     */   public void tick() {
/*  52 */     if (this.itemActivationTicks > 0) {
/*  53 */       this.itemActivationTicks--;
/*  54 */       if (this.itemActivationTicks == 0)
/*  55 */         this.itemActivationItem = null; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void submit(boolean isFirstPerson, boolean isSleeping, float partialTicks, SubmitNodeCollector submitNodeCollector, boolean hideGui) {
/*  61 */     PoseStack poseStack = new PoseStack();
/*  62 */     LocalPlayer localPlayer = this.minecraft.player;
/*  63 */     if (isFirstPerson && !isSleeping) {
/*  64 */       BlockState blockState = getViewBlockingState((Player)localPlayer);
/*  65 */       if (blockState != null) {
/*  66 */         BlockStateModelSet blockStateModelSet = this.minecraft.getModelManager().getBlockStateModelSet();
/*  67 */         TextureAtlasSprite sprite = blockStateModelSet.getParticleMaterial(blockState).sprite();
/*  68 */         submitBlockSprite(sprite, poseStack, submitNodeCollector, -15132391);
/*     */       } 
/*  71 */       if (!this.minecraft.player.isSpectator()) {
/*  72 */         if (this.minecraft.player.isEyeInFluid(FluidTags.WATER))
/*  73 */           submitWater(this.minecraft, poseStack, submitNodeCollector); 
/*  76 */         if (this.minecraft.player.isOnFire()) {
/*  77 */           TextureAtlasSprite fireSprite = this.sprites.get(ModelBakery.FIRE_1);
/*  78 */           submitFire(poseStack, submitNodeCollector, fireSprite);
/*     */         } 
/*     */       } 
/*     */     } 
/*  82 */     if (!hideGui)
/*  83 */       renderItemActivationAnimation(poseStack, partialTicks, submitNodeCollector); 
/*     */   }
/*     */   
/*     */   private void renderItemActivationAnimation(PoseStack poseStack, float partialTicks, SubmitNodeCollector submitNodeCollector) {
/*  88 */     if (this.itemActivationItem == null || this.itemActivationTicks <= 0)
/*     */       return; 
/*  92 */     int tick = 40 - this.itemActivationTicks;
/*  93 */     float scale = (tick + partialTicks) / 40.0F;
/*  94 */     float ts = scale * scale;
/*  95 */     float tc = scale * ts;
/*  96 */     float smoothScale = 10.25F * tc * ts - 24.95F * ts * ts + 25.5F * tc - 13.8F * ts + 4.0F * scale;
/*  97 */     float piScale = smoothScale * 3.1415927F;
/*  99 */     WindowRenderState windowState = (this.minecraft.gameRenderer.gameRenderState()).windowRenderState;
/* 101 */     float aspectRatio = windowState.width / windowState.height;
/* 103 */     float offX = this.itemActivationOffX * 0.3F * aspectRatio;
/* 104 */     float offY = this.itemActivationOffY * 0.3F;
/* 106 */     poseStack.pushPose();
/* 107 */     poseStack.translate(offX * Mth.abs(Mth.sin((piScale * 2.0F))), offY * Mth.abs(Mth.sin((piScale * 2.0F))), -10.0F + 9.0F * Mth.sin(piScale));
/* 108 */     float size = 0.8F;
/* 109 */     poseStack.scale(0.8F, 0.8F, 0.8F);
/* 110 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(900.0F * Mth.abs(Mth.sin(piScale))));
/* 111 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(6.0F * Mth.cos((scale * 8.0F))));
/* 112 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(6.0F * Mth.cos((scale * 8.0F))));
/* 114 */     this.minecraft.gameRenderer.lighting().setupFor(Lighting.Entry.ITEMS_3D);
/* 115 */     ItemStackRenderState itemState = new ItemStackRenderState();
/* 116 */     this.minecraft.getItemModelResolver().updateForTopItem(itemState, this.itemActivationItem, ItemDisplayContext.FIXED, (Level)this.minecraft.level, null, 0);
/* 117 */     itemState.submit(poseStack, submitNodeCollector, 15728880, OverlayTexture.NO_OVERLAY, 0);
/* 119 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public void resetItemActivation() {
/* 123 */     this.itemActivationItem = null;
/*     */   }
/*     */   
/*     */   public void displayItemActivation(ItemStack itemStack, RandomSource random) {
/* 127 */     this.itemActivationItem = itemStack;
/* 128 */     this.itemActivationTicks = 40;
/* 129 */     this.itemActivationOffX = random.nextFloat() * 2.0F - 1.0F;
/* 130 */     this.itemActivationOffY = random.nextFloat() * 2.0F - 1.0F;
/*     */   }
/*     */   
/*     */   private static BlockState getViewBlockingState(Player player) {
/* 134 */     if (player.noPhysics)
/* 135 */       return null; 
/* 137 */     BlockPos.MutableBlockPos testPos = new BlockPos.MutableBlockPos();
/* 138 */     for (int i = 0; i < 8; i++) {
/* 139 */       testPos.set(player
/* 140 */           .getX() + ((((i >> 0) % 2) - 0.5F) * player.getBbWidth() * 0.8F), player
/* 141 */           .getEyeY() + ((((i >> 1) % 2) - 0.5F) * 0.1F * player.getScale()), player
/* 142 */           .getZ() + ((((i >> 2) % 2) - 0.5F) * player.getBbWidth() * 0.8F));
/* 144 */       BlockState blockState = player.level().getBlockState((BlockPos)testPos);
/* 145 */       if (blockState.getRenderShape() != RenderShape.INVISIBLE && blockState.isViewBlocking((BlockGetter)player.level(), (BlockPos)testPos))
/* 146 */         return blockState; 
/*     */     } 
/* 149 */     return null;
/*     */   }
/*     */   
/*     */   private static void submitBlockSprite(TextureAtlasSprite sprite, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int color) {
/* 153 */     submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.blockScreenEffect(sprite.atlasLocation()), (pose, builder) -> buildSpriteQuad(builder, pose.pose(), sprite, -1.0F, -1.0F, 1.0F, 1.0F, -0.5F, color));
/*     */   }
/*     */   
/*     */   private static void submitWater(Minecraft minecraft, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
/* 160 */     LocalPlayer player = minecraft.player;
/* 161 */     BlockPos pos = BlockPos.containing((Position)player.getEyePosition());
/* 162 */     float brightness = Lightmap.getBrightness(player.level().dimensionType(), player.level().getMaxLocalRawBrightness(pos));
/* 163 */     int color = ARGB.colorFromFloat(0.1F, brightness, brightness, brightness);
/* 165 */     float u0 = -player.getYRot() / 64.0F;
/* 166 */     float v0 = player.getXRot() / 64.0F;
/* 168 */     submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.blockScreenEffect(UNDERWATER_LOCATION), (pose, builder) -> {
/*     */           float uvSize = 4.0F;
/*     */           buildQuad(builder, pose.pose(), -1.0F, -1.0F, 1.0F, 1.0F, -0.5F, u0 + 4.0F, v0 + 4.0F, u0, v0, color);
/*     */         });
/*     */   }
/*     */   
/*     */   private static void submitFire(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, TextureAtlasSprite sprite) {
/* 183 */     submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.fireScreenEffect(sprite.atlasLocation()), (basePose, builder) -> {
/*     */           Matrix4f pose = new Matrix4f();
/*     */           pose.set((Matrix4fc)basePose.pose());
/*     */           pose.translate(0.24F, -0.3F, 0.0F);
/*     */           pose.rotateY(-0.17453292F);
/*     */           buildFireQuad(sprite, builder, pose);
/*     */           pose.set((Matrix4fc)basePose.pose());
/*     */           pose.translate(-0.24F, -0.3F, 0.0F);
/*     */           pose.rotateY(0.17453292F);
/*     */           buildFireQuad(sprite, builder, pose);
/*     */         });
/*     */   }
/*     */   
/*     */   private static void buildFireQuad(TextureAtlasSprite sprite, VertexConsumer builder, Matrix4f pose) {
/* 198 */     float size = 1.0F;
/* 199 */     buildSpriteQuad(builder, pose, sprite, -0.5F, -0.5F, 0.5F, 0.5F, -0.5F, -436207617);
/*     */   }
/*     */   
/*     */   private static void buildSpriteQuad(VertexConsumer builder, Matrix4f pose, TextureAtlasSprite sprite, float x0, float y0, float x1, float y1, float z, int color) {
/* 209 */     buildQuad(builder, pose, x0, y0, x1, y1, z, sprite.getU1(), sprite.getV1(), sprite.getU0(), sprite.getV0(), color);
/*     */   }
/*     */   
/*     */   private static void buildQuad(VertexConsumer builder, Matrix4f pose, float x0, float y0, float x1, float y1, float z, float u0, float v0, float u1, float v1, int color) {
/* 213 */     builder.addVertex((Matrix4fc)pose, x0, y0, z).setUv(u0, v0).setColor(color);
/* 214 */     builder.addVertex((Matrix4fc)pose, x1, y0, z).setUv(u1, v0).setColor(color);
/* 215 */     builder.addVertex((Matrix4fc)pose, x1, y1, z).setUv(u1, v1).setColor(color);
/* 216 */     builder.addVertex((Matrix4fc)pose, x0, y1, z).setUv(u0, v1).setColor(color);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\ScreenEffectRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */