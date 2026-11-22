/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import com.mojang.blaze3d.systems.GpuSurface;
/*    */ import java.util.Locale;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntryFps implements DebugScreenEntry {
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 16 */     Minecraft minecraft = Minecraft.getInstance();
/* 17 */     int framerateLimit = minecraft.getFramerateLimitTracker().getFramerateLimit();
/* 18 */     Optional<GpuSurface.Configuration> surfaceConfiguration = minecraft.windowSurface().currentConfiguration();
/* 20 */     displayer.addPriorityLine(String.format(Locale.ROOT, "%d fps T: %s%s", new Object[] { Integer.valueOf(minecraft.getFps()), 
/* 22 */             (framerateLimit == 260) ? "inf" : Integer.valueOf(framerateLimit), 
/* 23 */             presentModeName(surfaceConfiguration.<GpuSurface.PresentMode>map(GpuSurface.Configuration::presentMode).orElse(null)) }));
/*    */   }
/*    */   
/*    */   public boolean isAllowed(boolean reducedDebugInfo) {
/* 29 */     return true;
/*    */   }
/*    */   
/*    */   private static String presentModeName(GpuSurface.PresentMode mode) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: astore_1
/*    */     //   2: iconst_0
/*    */     //   3: istore_2
/*    */     //   4: aload_1
/*    */     //   5: iload_2
/*    */     //   6: <illegal opcode> enumSwitch : (Lcom/mojang/blaze3d/systems/GpuSurface$PresentMode;I)I
/*    */     //   11: tableswitch default -> 44, -1 -> 74, 0 -> 54, 1 -> 59, 2 -> 64, 3 -> 69
/*    */     //   44: new java/lang/MatchException
/*    */     //   47: dup
/*    */     //   48: aconst_null
/*    */     //   49: aconst_null
/*    */     //   50: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*    */     //   53: athrow
/*    */     //   54: ldc ' (immediate)'
/*    */     //   56: goto -> 76
/*    */     //   59: ldc ' (mailbox)'
/*    */     //   61: goto -> 76
/*    */     //   64: ldc ' (fifo)'
/*    */     //   66: goto -> 76
/*    */     //   69: ldc ' (fifo relaxed)'
/*    */     //   71: goto -> 76
/*    */     //   74: ldc ''
/*    */     //   76: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     //   #34	-> 54
/*    */     //   #35	-> 59
/*    */     //   #36	-> 64
/*    */     //   #37	-> 69
/*    */     //   #38	-> 74
/*    */     //   #33	-> 76
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	77	0	mode	Lcom/mojang/blaze3d/systems/GpuSurface$PresentMode;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryFps.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */