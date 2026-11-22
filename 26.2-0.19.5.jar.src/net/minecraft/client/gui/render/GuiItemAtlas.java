/*     */ package net.minecraft.client.gui.render;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import com.mojang.blaze3d.ProjectionType;
/*     */ import com.mojang.blaze3d.platform.Lighting;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.Projection;
/*     */ import net.minecraft.client.renderer.ProjectionMatrixBuffer;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.SubmitNodeStorage;
/*     */ import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
/*     */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*     */ import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class GuiItemAtlas implements AutoCloseable {
/*     */   private static final int MINIMUM_TEXTURE_SIZE = 512;
/*     */   
/*  29 */   private final SubmitNodeStorage submitNodeStorage = new SubmitNodeStorage();
/*     */   
/*     */   private final FeatureRenderDispatcher featureRenderDispatcher;
/*     */   
/*     */   private final int textureSize;
/*     */   
/*     */   private final int slotTextureSize;
/*     */   
/*     */   private final GpuTexture texture;
/*     */   
/*     */   private final GpuTextureView textureView;
/*     */   
/*     */   private final GpuTexture depthTexture;
/*     */   
/*     */   private final GpuTextureView depthTextureView;
/*     */   
/*     */   private final DynamicAtlasAllocator<Object> allocator;
/*     */   
/*  41 */   private final PoseStack poseStack = new PoseStack();
/*     */   
/*  42 */   private final Projection projection = new Projection();
/*     */   
/*  43 */   private final ProjectionMatrixBuffer projectionMatrixBuffer = new ProjectionMatrixBuffer("items");
/*     */   
/*     */   public GuiItemAtlas(FeatureRenderDispatcher featureRenderDispatcher, int textureSize, int slotTextureSize) {
/*  46 */     this.featureRenderDispatcher = featureRenderDispatcher;
/*  48 */     int storageSize = textureSize / slotTextureSize;
/*  49 */     this.textureSize = textureSize;
/*  50 */     this.slotTextureSize = slotTextureSize;
/*  52 */     GpuDevice device = RenderSystem.getDevice();
/*  53 */     this.texture = device.createTexture("UI items atlas", 13, GpuFormat.RGBA8_UNORM, textureSize, textureSize, 1, 1);
/*  54 */     this.textureView = device.createTextureView(this.texture);
/*  55 */     this.depthTexture = device.createTexture("UI items atlas depth", 9, GpuFormat.D32_FLOAT, textureSize, textureSize, 1, 1);
/*  56 */     this.depthTextureView = device.createTextureView(this.depthTexture);
/*  58 */     this.allocator = new DynamicAtlasAllocator(storageSize, storageSize);
/*  59 */     device.createCommandEncoder().clearColorAndDepthTextures(this.texture, GuiRenderer.CLEAR_COLOR, this.depthTexture, 0.0D);
/*     */   }
/*     */   
/*     */   public static int computeTextureSizeFor(int slotTextureSize, int requiredSlotCount) {
/*  63 */     int preferredSlotCount = requiredSlotCount + requiredSlotCount / 2;
/*  64 */     int atlasSize = Mth.smallestSquareSide(preferredSlotCount);
/*  65 */     return Math.clamp(
/*  66 */         Mth.smallestEncompassingPowerOfTwo(atlasSize * slotTextureSize), 512, 
/*  67 */         RenderSystem.getDevice().getDeviceInfo().limits().maxTextureSizeForFormat(GpuFormat.RGBA8_UNORM));
/*     */   }
/*     */   
/*     */   public void endFrame() {
/*  72 */     this.allocator.endFrame();
/*     */   }
/*     */   
/*     */   public boolean tryPrepareFor(Set<Object> items) {
/*  76 */     if (this.allocator.hasSpaceForAll(items))
/*  77 */       return true; 
/*  80 */     return this.allocator.reclaimSpaceFor(items);
/*     */   }
/*     */   
/*     */   public SlotView getOrUpdate(TrackingItemStackRenderState item) {
/*  84 */     DynamicAtlasAllocator.Slot slot = this.allocator.getOrAllocate(item.getModelIdentity(), item.isAnimated());
/*  85 */     if (slot == null)
/*  86 */       return null; 
/*  88 */     switch (slot.state()) {
/*     */       case EMPTY:
/*  89 */         drawToSlot(slot.x(), slot.y(), false, (ItemStackRenderState)item);
/*     */         break;
/*     */       case STALE:
/*  90 */         drawToSlot(slot.x(), slot.y(), true, (ItemStackRenderState)item);
/*     */         break;
/*     */     } 
/*  94 */     float slotUvSize = this.slotTextureSize / this.textureSize;
/*  95 */     float u0 = slot.x() * slotUvSize;
/*  96 */     float v0 = 1.0F - slot.y() * slotUvSize;
/*  97 */     return new SlotView(this.textureView, u0, v0, u0 + slotUvSize, v0 - slotUvSize);
/*     */   }
/*     */   
/*     */   private void drawToSlot(int slotX, int slotY, boolean clear, ItemStackRenderState item) {
/* 101 */     int left = slotX * this.slotTextureSize;
/* 102 */     int top = slotY * this.slotTextureSize;
/* 103 */     int bottom = top + this.slotTextureSize;
/* 105 */     GpuDevice device = RenderSystem.getDevice();
/* 106 */     if (clear)
/* 107 */       device.createCommandEncoder().clearColorAndDepthTextures(this.texture, GuiRenderer.CLEAR_COLOR, this.depthTexture, 0.0D, left, this.textureSize - bottom, this.slotTextureSize, this.slotTextureSize); 
/* 117 */     this.poseStack.pushPose();
/* 118 */     this.poseStack.translate(left + this.slotTextureSize / 2.0F, top + this.slotTextureSize / 2.0F, 0.0F);
/* 119 */     this.poseStack.scale(this.slotTextureSize, -this.slotTextureSize, this.slotTextureSize);
/* 121 */     RenderSystem.outputColorTextureOverride = this.textureView;
/* 122 */     RenderSystem.outputDepthTextureOverride = this.depthTextureView;
/* 123 */     this.projection.setupOrtho(-1000.0F, 1000.0F, this.textureSize, this.textureSize, true);
/* 124 */     RenderSystem.setProjectionMatrix(this.projectionMatrixBuffer.getBuffer(this.projection), ProjectionType.ORTHOGRAPHIC);
/* 126 */     RenderSystem.enableScissorForRenderTypeDraws(left, this.textureSize - bottom, this.slotTextureSize, this.slotTextureSize);
/* 128 */     Lighting.Entry lighting = item.usesBlockLight() ? Lighting.Entry.ITEMS_3D : Lighting.Entry.ITEMS_FLAT;
/* 129 */     (Minecraft.getInstance()).gameRenderer.lighting().setupFor(lighting);
/* 130 */     item.submit(this.poseStack, (SubmitNodeCollector)this.submitNodeStorage, 15728880, OverlayTexture.NO_OVERLAY, 0);
/* 131 */     this.featureRenderDispatcher.renderAllFeatures(this.submitNodeStorage);
/* 133 */     RenderSystem.disableScissorForRenderTypeDraws();
/* 135 */     RenderSystem.outputColorTextureOverride = null;
/* 136 */     RenderSystem.outputDepthTextureOverride = null;
/* 138 */     this.poseStack.popPose();
/*     */   }
/*     */   
/*     */   public int textureSize() {
/* 142 */     return this.textureSize;
/*     */   }
/*     */   
/*     */   public void close() {
/* 147 */     this.texture.close();
/* 148 */     this.textureView.close();
/* 149 */     this.depthTexture.close();
/* 150 */     this.depthTextureView.close();
/* 151 */     this.projectionMatrixBuffer.close();
/*     */   }
/*     */   
/*     */   public static final class SlotView extends Record {
/*     */     private final GpuTextureView textureView;
/*     */     
/*     */     private final float u0;
/*     */     
/*     */     private final float v0;
/*     */     
/*     */     private final float u1;
/*     */     
/*     */     private final float v1;
/*     */     
/*     */     public SlotView(GpuTextureView textureView, float u0, float v0, float u1, float v1) {
/* 154 */       this.textureView = textureView;
/* 154 */       this.u0 = u0;
/* 154 */       this.v0 = v0;
/* 154 */       this.u1 = u1;
/* 154 */       this.v1 = v1;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/render/GuiItemAtlas$SlotView;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #154	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/render/GuiItemAtlas$SlotView;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/render/GuiItemAtlas$SlotView;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #154	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/render/GuiItemAtlas$SlotView;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/render/GuiItemAtlas$SlotView;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #154	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/render/GuiItemAtlas$SlotView;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public GpuTextureView textureView() {
/* 154 */       return this.textureView;
/*     */     }
/*     */     
/*     */     public float u0() {
/* 154 */       return this.u0;
/*     */     }
/*     */     
/*     */     public float v0() {
/* 154 */       return this.v0;
/*     */     }
/*     */     
/*     */     public float u1() {
/* 154 */       return this.u1;
/*     */     }
/*     */     
/*     */     public float v1() {
/* 154 */       return this.v1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\render\GuiItemAtlas.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */