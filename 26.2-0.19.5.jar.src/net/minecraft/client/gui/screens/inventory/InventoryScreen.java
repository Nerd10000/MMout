/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.navigation.ScreenPosition;
/*     */ import net.minecraft.client.gui.screens.recipebook.CraftingRecipeBookComponent;
/*     */ import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
/*     */ import net.minecraft.client.renderer.entity.EntityRenderer;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractCraftingMenu;
/*     */ import net.minecraft.world.inventory.InventoryMenu;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class InventoryScreen extends AbstractRecipeBookScreen<InventoryMenu> {
/*     */   private float xMouse;
/*     */   
/*     */   private float yMouse;
/*     */   
/*     */   private boolean buttonClicked;
/*     */   
/*     */   private final EffectsInInventory effects;
/*     */   
/*     */   public InventoryScreen(Player player) {
/*  32 */     super(player.inventoryMenu, (RecipeBookComponent<?>)new CraftingRecipeBookComponent((AbstractCraftingMenu)player.inventoryMenu), player.getInventory(), (Component)Component.translatable("container.crafting"));
/*  33 */     this.titleLabelX = 97;
/*  34 */     this.effects = new EffectsInInventory(this);
/*     */   }
/*     */   
/*     */   public void containerTick() {
/*  39 */     super.containerTick();
/*  41 */     if (this.minecraft.player.hasInfiniteMaterials())
/*  42 */       this.minecraft.gui.setScreen(new CreativeModeInventoryScreen(this.minecraft.player, this.minecraft.player.connection.enabledFeatures(), ((Boolean)this.minecraft.options.operatorItemsTab().get()).booleanValue())); 
/*     */   }
/*     */   
/*     */   protected void init() {
/*  48 */     if (this.minecraft.player.hasInfiniteMaterials()) {
/*  49 */       this.minecraft.gui.setScreen(new CreativeModeInventoryScreen(this.minecraft.player, this.minecraft.player.connection.enabledFeatures(), ((Boolean)this.minecraft.options.operatorItemsTab().get()).booleanValue()));
/*     */       return;
/*     */     } 
/*  52 */     super.init();
/*     */   }
/*     */   
/*     */   protected ScreenPosition getRecipeBookButtonPosition() {
/*  57 */     return new ScreenPosition(this.leftPos + 104, this.height / 2 - 22);
/*     */   }
/*     */   
/*     */   protected void onRecipeBookButtonClick() {
/*  62 */     this.buttonClicked = true;
/*     */   }
/*     */   
/*     */   protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
/*  67 */     graphics.text(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*  72 */     this.effects.extractRenderState(graphics, mouseX, mouseY);
/*  73 */     super.extractRenderState(graphics, mouseX, mouseY, a);
/*  75 */     this.xMouse = mouseX;
/*  76 */     this.yMouse = mouseY;
/*     */   }
/*     */   
/*     */   public boolean showsActiveEffects() {
/*  81 */     return this.effects.canSeeEffects();
/*     */   }
/*     */   
/*     */   protected boolean isBiggerResultSlot() {
/*  86 */     return false;
/*     */   }
/*     */   
/*     */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*  91 */     super.extractBackground(graphics, mouseX, mouseY, a);
/*  92 */     int xo = this.leftPos;
/*  93 */     int yo = this.topPos;
/*  94 */     graphics.blit(RenderPipelines.GUI_TEXTURED, INVENTORY_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/*  96 */     extractEntityInInventoryFollowsMouse(graphics, xo + 26, yo + 8, xo + 75, yo + 78, 30, 0.0625F, this.xMouse, this.yMouse, (LivingEntity)this.minecraft.player);
/*     */   }
/*     */   
/*     */   public static void extractEntityInInventoryFollowsMouse(GuiGraphicsExtractor graphics, int x0, int y0, int x1, int y1, int size, float offsetY, float mouseX, float mouseY, LivingEntity entity) {
/* 100 */     float centerX = (x0 + x1) / 2.0F;
/* 101 */     float centerY = (y0 + y1) / 2.0F;
/* 103 */     float xAngle = (float)Math.atan(((centerX - mouseX) / 40.0F));
/* 104 */     float yAngle = (float)Math.atan(((centerY - mouseY) / 40.0F));
/* 106 */     Quaternionf rotation = (new Quaternionf()).rotateZ(3.1415927F);
/* 107 */     Quaternionf xRotation = (new Quaternionf()).rotateX(yAngle * 20.0F * 0.017453292F);
/* 108 */     rotation.mul((Quaternionfc)xRotation);
/* 110 */     EntityRenderState renderState = extractRenderState(entity);
/* 111 */     if (renderState instanceof LivingEntityRenderState) {
/* 111 */       LivingEntityRenderState livingRenderState = (LivingEntityRenderState)renderState;
/* 112 */       livingRenderState.bodyRot = 180.0F + xAngle * 20.0F;
/* 113 */       livingRenderState.yRot = xAngle * 20.0F;
/* 114 */       if (livingRenderState.pose != Pose.FALL_FLYING) {
/* 115 */         livingRenderState.xRot = -yAngle * 20.0F;
/*     */       } else {
/* 117 */         livingRenderState.xRot = 0.0F;
/*     */       } 
/* 119 */       livingRenderState.boundingBoxWidth /= livingRenderState.scale;
/* 120 */       livingRenderState.boundingBoxHeight /= livingRenderState.scale;
/* 121 */       livingRenderState.scale = 1.0F;
/*     */     } 
/* 124 */     Vector3f translation = new Vector3f(0.0F, renderState.boundingBoxHeight / 2.0F + offsetY, 0.0F);
/* 125 */     graphics.entity(renderState, size, (Vector3fc)translation, (Quaternionfc)rotation, (Quaternionfc)xRotation, x0, y0, x1, y1);
/*     */   }
/*     */   
/*     */   private static EntityRenderState extractRenderState(LivingEntity entity) {
/* 129 */     EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
/* 130 */     EntityRenderer<? super LivingEntity, ?> renderer = entityRenderDispatcher.getRenderer((Entity)entity);
/* 131 */     EntityRenderState renderState = renderer.createRenderState((Entity)entity, 1.0F);
/* 132 */     renderState.shadowPieces.clear();
/* 133 */     renderState.outlineColor = 0;
/* 134 */     return renderState;
/*     */   }
/*     */   
/*     */   public boolean mouseReleased(MouseButtonEvent event) {
/* 139 */     if (this.buttonClicked) {
/* 140 */       this.buttonClicked = false;
/* 141 */       return true;
/*     */     } 
/* 144 */     return super.mouseReleased(event);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\InventoryScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */