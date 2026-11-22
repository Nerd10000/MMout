/*   */ package net.minecraft.server.packs;
/*   */ 
/*   */ import net.minecraft.server.packs.repository.Pack;
/*   */ 
/*   */ public final class PackSelectionConfig extends Record {
/*   */   private final boolean required;
/*   */   
/*   */   private final Pack.Position defaultPosition;
/*   */   
/*   */   private final boolean fixedPosition;
/*   */   
/*   */   public PackSelectionConfig(boolean required, Pack.Position defaultPosition, boolean fixedPosition) {
/* 5 */     this.required = required;
/* 5 */     this.defaultPosition = defaultPosition;
/* 5 */     this.fixedPosition = fixedPosition;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/PackSelectionConfig;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/server/packs/PackSelectionConfig;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/PackSelectionConfig;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/server/packs/PackSelectionConfig;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/PackSelectionConfig;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/server/packs/PackSelectionConfig;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public boolean required() {
/* 5 */     return this.required;
/*   */   }
/*   */   
/*   */   public Pack.Position defaultPosition() {
/* 5 */     return this.defaultPosition;
/*   */   }
/*   */   
/*   */   public boolean fixedPosition() {
/* 5 */     return this.fixedPosition;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\PackSelectionConfig.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */