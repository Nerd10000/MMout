/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ public final class Default extends Record implements TooltipFlag {
/*    */   private final boolean advanced;
/*    */   
/*    */   private final boolean creative;
/*    */   
/*    */   public Default(boolean advanced, boolean creative) {
/*  4 */     this.advanced = advanced;
/*  4 */     this.creative = creative;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/TooltipFlag$Default;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #4	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/TooltipFlag$Default;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/TooltipFlag$Default;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #4	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/TooltipFlag$Default;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/TooltipFlag$Default;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #4	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/TooltipFlag$Default;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public boolean advanced() {
/*  4 */     return this.advanced;
/*    */   }
/*    */   
/*    */   public boolean creative() {
/*  4 */     return this.creative;
/*    */   }
/*    */   
/*    */   public boolean isAdvanced() {
/*  7 */     return this.advanced;
/*    */   }
/*    */   
/*    */   public boolean isCreative() {
/* 12 */     return this.creative;
/*    */   }
/*    */   
/*    */   public Default asCreative() {
/* 16 */     return new Default(this.advanced, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\TooltipFlag$Default.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */