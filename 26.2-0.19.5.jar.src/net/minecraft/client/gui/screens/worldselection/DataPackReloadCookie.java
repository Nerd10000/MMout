/*   */ package net.minecraft.client.gui.screens.worldselection;
/*   */ 
/*   */ import net.minecraft.world.level.WorldDataConfiguration;
/*   */ import net.minecraft.world.level.levelgen.WorldGenSettings;
/*   */ 
/*   */ public final class DataPackReloadCookie extends Record {
/*   */   private final WorldGenSettings worldGenSettings;
/*   */   
/*   */   private final WorldDataConfiguration dataConfiguration;
/*   */   
/*   */   public DataPackReloadCookie(WorldGenSettings worldGenSettings, WorldDataConfiguration dataConfiguration) {
/* 6 */     this.worldGenSettings = worldGenSettings;
/* 6 */     this.dataConfiguration = dataConfiguration;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/worldselection/DataPackReloadCookie;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/DataPackReloadCookie;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/worldselection/DataPackReloadCookie;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/DataPackReloadCookie;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/worldselection/DataPackReloadCookie;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/client/gui/screens/worldselection/DataPackReloadCookie;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public WorldGenSettings worldGenSettings() {
/* 6 */     return this.worldGenSettings;
/*   */   }
/*   */   
/*   */   public WorldDataConfiguration dataConfiguration() {
/* 6 */     return this.dataConfiguration;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\DataPackReloadCookie.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */