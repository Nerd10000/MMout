/*    */ package com.mojang.blaze3d.platform;
/*    */ 
/*    */ import net.minecraft.client.InactivityFpsLimit;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class FramerateLimitTracker {
/*    */   private static final int OUT_OF_LEVEL_MENU_LIMIT = 60;
/*    */   
/*    */   private static final int ICONIFIED_WINDOW_LIMIT = 10;
/*    */   
/*    */   private static final int AFK_LIMIT = 30;
/*    */   
/*    */   private static final int LONG_AFK_LIMIT = 10;
/*    */   
/*    */   private static final long AFK_THRESHOLD_MS = 60000L;
/*    */   
/*    */   private static final long LONG_AFK_THRESHOLD_MS = 600000L;
/*    */   
/*    */   private final Options options;
/*    */   
/*    */   private final Minecraft minecraft;
/*    */   
/*    */   private int framerateLimit;
/*    */   
/*    */   private long latestInputTime;
/*    */   
/*    */   public enum FramerateThrottleReason {
/* 21 */     NONE, WINDOW_ICONIFIED, LONG_AFK, SHORT_AFK, OUT_OF_LEVEL_MENU;
/*    */   }
/*    */   
/*    */   public FramerateLimitTracker(Options options, Minecraft minecraft) {
/* 29 */     this.options = options;
/* 30 */     this.minecraft = minecraft;
/* 31 */     this.framerateLimit = ((Integer)options.framerateLimit().get()).intValue();
/*    */   }
/*    */   
/*    */   public int getFramerateLimit() {
/* 35 */     switch (getThrottleReason().ordinal()) {
/*    */       default:
/* 35 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */       case 3:
/*    */       
/*    */       case 4:
/*    */         break;
/*    */     } 
/* 35 */     return 
/*    */       
/* 40 */       60;
/*    */   }
/*    */   
/*    */   public FramerateThrottleReason getThrottleReason() {
/* 45 */     InactivityFpsLimit inactivityFpsLimit = (InactivityFpsLimit)this.options.inactivityFpsLimit().get();
/* 46 */     if (this.minecraft.getWindow().isIconified())
/* 47 */       return FramerateThrottleReason.WINDOW_ICONIFIED; 
/* 49 */     if (inactivityFpsLimit == InactivityFpsLimit.AFK) {
/* 50 */       long afkTimeMillis = Util.getMillis() - this.latestInputTime;
/* 51 */       if (afkTimeMillis > 600000L)
/* 52 */         return FramerateThrottleReason.LONG_AFK; 
/* 54 */       if (afkTimeMillis > 60000L)
/* 55 */         return FramerateThrottleReason.SHORT_AFK; 
/*    */     } 
/* 58 */     if (this.minecraft.level == null && (this.minecraft.gui.screen() != null || this.minecraft.gui.overlay() != null))
/* 59 */       return FramerateThrottleReason.OUT_OF_LEVEL_MENU; 
/* 61 */     return FramerateThrottleReason.NONE;
/*    */   }
/*    */   
/*    */   public boolean isHeavilyThrottled() {
/* 66 */     FramerateThrottleReason reason = getThrottleReason();
/* 67 */     return (reason == FramerateThrottleReason.WINDOW_ICONIFIED || reason == FramerateThrottleReason.LONG_AFK);
/*    */   }
/*    */   
/*    */   public void setFramerateLimit(int value) {
/* 71 */     this.framerateLimit = value;
/*    */   }
/*    */   
/*    */   public void onInputReceived() {
/* 75 */     this.latestInputTime = Util.getMillis();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\FramerateLimitTracker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */