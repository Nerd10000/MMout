/*      */ package com.mojang.realmsclient;
/*      */ 
/*      */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*      */ import net.minecraft.client.gui.components.Button;
/*      */ import net.minecraft.client.gui.components.SpriteIconButton;
/*      */ import net.minecraft.client.gui.components.WidgetSprites;
/*      */ import net.minecraft.client.renderer.RenderPipelines;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.resources.Identifier;
/*      */ 
/*      */ class NotificationButton extends SpriteIconButton.CenteredIcon {
/* 1228 */   private static final Identifier[] NOTIFICATION_ICONS = new Identifier[] { Identifier.withDefaultNamespace("notification/1"), 
/* 1230 */       Identifier.withDefaultNamespace("notification/2"), 
/* 1231 */       Identifier.withDefaultNamespace("notification/3"), 
/* 1232 */       Identifier.withDefaultNamespace("notification/4"), 
/* 1233 */       Identifier.withDefaultNamespace("notification/5"), 
/* 1234 */       Identifier.withDefaultNamespace("notification/more") };
/*      */   
/*      */   private static final int UNKNOWN_COUNT = 2147483647;
/*      */   
/*      */   private static final int SIZE = 20;
/*      */   
/*      */   private static final int SPRITE_SIZE = 14;
/*      */   
/*      */   private int notificationCount;
/*      */   
/*      */   public NotificationButton(Component title, Identifier texture, Button.OnPress onPress, Component tooltip) {
/* 1245 */     super(20, 20, title, 14, 14, 0, 0, new WidgetSprites(texture), onPress, tooltip, null, false);
/*      */   }
/*      */   
/*      */   private int notificationCount() {
/* 1249 */     return this.notificationCount;
/*      */   }
/*      */   
/*      */   public void setNotificationCount(int notificationCount) {
/* 1253 */     this.notificationCount = notificationCount;
/*      */   }
/*      */   
/*      */   public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 1258 */     super.extractContents(graphics, mouseX, mouseY, a);
/* 1259 */     if (isActive() && this.notificationCount != 0)
/* 1260 */       extractNotificationCounter(graphics); 
/*      */   }
/*      */   
/*      */   private void extractNotificationCounter(GuiGraphicsExtractor graphics) {
/* 1265 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, NOTIFICATION_ICONS[Math.min(this.notificationCount, 6) - 1], getX() + getWidth() - 5, getY() - 3, 8, 8);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\RealmsMainScreen$NotificationButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */