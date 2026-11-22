/*      */ package net.minecraft.server;
/*      */ 
/*      */ import net.minecraft.network.chat.Component;
/*      */ 
/*      */ public enum MultiplayerScope {
/* 2149 */   OFF("off"),
/* 2150 */   LAN("lan");
/*      */   
/*      */   private final Component translatable;
/*      */   
/*      */   private final Component tooltip;
/*      */   
/*      */   MultiplayerScope(String key) {
/* 2156 */     this.translatable = (Component)Component.translatable("menu.multiplayerOptions.network." + key);
/* 2157 */     this.tooltip = (Component)Component.translatable("menu.multiplayerOptions.network." + key + ".tooltip");
/*      */   }
/*      */   
/*      */   public Component getDisplayName() {
/* 2161 */     return this.translatable;
/*      */   }
/*      */   
/*      */   public Component getTooltip() {
/* 2165 */     return this.tooltip;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\MinecraftServer$MultiplayerScope.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */