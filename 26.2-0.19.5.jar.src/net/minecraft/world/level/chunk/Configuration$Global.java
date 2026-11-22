/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public final class Global extends Record implements Configuration {
/*    */   private final int bitsInMemory;
/*    */   
/*    */   private final int bitsInStorage;
/*    */   
/*    */   public Global(int bitsInMemory, int bitsInStorage) {
/* 43 */     this.bitsInMemory = bitsInMemory;
/* 43 */     this.bitsInStorage = bitsInStorage;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/Configuration$Global;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #43	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/Configuration$Global;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/Configuration$Global;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #43	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/Configuration$Global;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/Configuration$Global;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #43	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/chunk/Configuration$Global;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int bitsInMemory() {
/* 43 */     return this.bitsInMemory;
/*    */   }
/*    */   
/*    */   public int bitsInStorage() {
/* 43 */     return this.bitsInStorage;
/*    */   }
/*    */   
/*    */   public boolean alwaysRepack() {
/* 50 */     return true;
/*    */   }
/*    */   
/*    */   public <T> Palette<T> createPalette(Strategy<T> strategy, List<T> paletteEntries) {
/* 55 */     return strategy.globalPalette();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\Configuration$Global.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */