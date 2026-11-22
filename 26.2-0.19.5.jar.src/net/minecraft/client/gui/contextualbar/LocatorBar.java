/*    */ package net.minecraft.client.gui.contextualbar;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import net.minecraft.client.DeltaTracker;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.client.resources.WaypointStyle;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.TickRateManager;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.waypoints.PartialTickSupplier;
/*    */ import net.minecraft.world.waypoints.TrackedWaypoint;
/*    */ import net.minecraft.world.waypoints.Waypoint;
/*    */ 
/*    */ public class LocatorBar implements ContextualBar {
/* 19 */   private static final Identifier LOCATOR_BAR_BACKGROUND = Identifier.withDefaultNamespace("hud/locator_bar_background");
/*    */   
/* 20 */   private static final Identifier LOCATOR_BAR_ARROW_UP = Identifier.withDefaultNamespace("hud/locator_bar_arrow_up");
/*    */   
/* 21 */   private static final Identifier LOCATOR_BAR_ARROW_DOWN = Identifier.withDefaultNamespace("hud/locator_bar_arrow_down");
/*    */   
/*    */   private static final int DOT_SIZE = 9;
/*    */   
/*    */   private static final int VISIBLE_DEGREE_RANGE = 60;
/*    */   
/*    */   private static final int ARROW_WIDTH = 7;
/*    */   
/*    */   private static final int ARROW_HEIGHT = 5;
/*    */   
/*    */   private static final int ARROW_LEFT = 1;
/*    */   
/*    */   private static final int ARROW_PADDING = 1;
/*    */   
/*    */   private final Minecraft minecraft;
/*    */   
/*    */   public LocatorBar(Minecraft minecraft) {
/* 33 */     this.minecraft = minecraft;
/*    */   }
/*    */   
/*    */   public void extractBackground(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
/* 38 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, LOCATOR_BAR_BACKGROUND, left(this.minecraft.getWindow()), top(this.minecraft.getWindow()), 182, 5);
/*    */   }
/*    */   
/*    */   public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
/* 43 */     int top = top(this.minecraft.getWindow());
/* 45 */     Entity cameraEntity = this.minecraft.getCameraEntity();
/* 46 */     if (cameraEntity == null)
/*    */       return; 
/* 49 */     Level level = cameraEntity.level();
/* 50 */     TickRateManager tickRateManager = level.tickRateManager();
/* 51 */     PartialTickSupplier partialTickSupplier = entity -> deltaTracker.getGameTimeDeltaPartialTick(!tickRateManager.isEntityFrozen(entity));
/* 53 */     this.minecraft.player.connection.getWaypointManager().forEachWaypoint(cameraEntity, waypoint -> {
/*    */           if (((Boolean)waypoint.id().left().map(()).orElse(Boolean.valueOf(false))).booleanValue())
/*    */             return; 
/*    */           double angle = waypoint.yawAngleToCamera(level, (TrackedWaypoint.Camera)this.minecraft.gameRenderer.mainCamera(), partialTickSupplier);
/*    */           if (angle <= -60.0D || angle > 60.0D)
/*    */             return; 
/*    */           int screenMiddle = Mth.ceil((graphics.guiWidth() - 9) / 2.0F);
/*    */           Waypoint.Icon icon = waypoint.icon();
/*    */           WaypointStyle style = this.minecraft.gui.hud.getWaypointStyles().get(icon.style);
/*    */           float distance = Mth.sqrt((float)waypoint.distanceSquared(cameraEntity));
/*    */           Identifier sprite = style.sprite(distance);
/*    */           int color = ((Integer)icon.color.orElseGet(())).intValue();
/*    */           int dotPosition = Mth.floor(angle * 173.0D / 2.0D / 60.0D);
/*    */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, screenMiddle + dotPosition, top - 2, 9, 9, color);
/*    */           TrackedWaypoint.PitchDirection pitchDirection = waypoint.pitchDirectionToCamera(level, (TrackedWaypoint.Projector)this.minecraft.gameRenderer, partialTickSupplier);
/*    */           if (pitchDirection != TrackedWaypoint.PitchDirection.NONE) {
/*    */             int arrowTop;
/*    */             Identifier arrowSprite;
/*    */             if (pitchDirection == TrackedWaypoint.PitchDirection.DOWN) {
/*    */               arrowTop = 6;
/*    */               arrowSprite = LOCATOR_BAR_ARROW_DOWN;
/*    */             } else {
/*    */               arrowTop = -6;
/*    */               arrowSprite = LOCATOR_BAR_ARROW_UP;
/*    */             } 
/*    */             graphics.blitSprite(RenderPipelines.GUI_TEXTURED, arrowSprite, screenMiddle + dotPosition + 1, top + arrowTop, 7, 5);
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\contextualbar\LocatorBar.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */