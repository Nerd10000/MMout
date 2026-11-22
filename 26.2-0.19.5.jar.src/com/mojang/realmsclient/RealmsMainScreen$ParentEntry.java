/*      */ package com.mojang.realmsclient;
/*      */ 
/*      */ import com.mojang.realmsclient.dto.RealmsServer;
/*      */ import com.mojang.realmsclient.util.RealmsUtil;
/*      */ import java.util.Objects;
/*      */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*      */ import net.minecraft.client.gui.components.Tooltip;
/*      */ import net.minecraft.client.gui.components.WidgetTooltipHolder;
/*      */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*      */ import net.minecraft.network.chat.Component;
/*      */ 
/*      */ class ParentEntry extends RealmsMainScreen.Entry {
/*      */   private final RealmsServer server;
/*      */   
/* 1053 */   private final WidgetTooltipHolder tooltip = new WidgetTooltipHolder();
/*      */   
/*      */   public ParentEntry(RealmsMainScreen paramRealmsMainScreen, RealmsServer server) {
/* 1055 */     super(paramRealmsMainScreen);
/* 1056 */     this.server = server;
/* 1057 */     if (!server.expired)
/* 1058 */       this.tooltip.set(Tooltip.create((Component)Component.translatable("mco.snapshot.parent.tooltip"))); 
/*      */   }
/*      */   
/*      */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 1064 */     extractStatusLights(this.server, graphics, getContentRight(), getContentY(), mouseX, mouseY);
/* 1065 */     RealmsUtil.extractPlayerFace(graphics, getContentX(), getContentY(), 32, this.server.ownerUUID);
/* 1066 */     extractFirstLine(graphics, getContentY(), getContentX(), getContentWidth(), -8355712, this.server);
/* 1067 */     extractSecondLine(graphics, getContentY(), getContentX(), getContentWidth(), this.server);
/* 1068 */     extractThirdLine(graphics, getContentY(), getContentX(), this.server);
/* 1069 */     this.tooltip.refreshTooltipForNextRenderPass(graphics, mouseX, mouseY, hovered, isFocused(), new ScreenRectangle(getContentX(), getContentY(), getContentWidth(), getContentHeight()));
/*      */   }
/*      */   
/*      */   public Component getNarration() {
/* 1074 */     return (Component)Component.literal(Objects.<String>requireNonNullElse(this.server.name, "unknown server"));
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\RealmsMainScreen$ParentEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */