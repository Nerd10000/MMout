/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ final class FoxTexturesByState extends Record {
/*    */   private final FoxRenderer.FoxTexturesByAge idle;
/*    */   
/*    */   private final FoxRenderer.FoxTexturesByAge sleeping;
/*    */   
/*    */   private FoxTexturesByState(FoxRenderer.FoxTexturesByAge idle, FoxRenderer.FoxTexturesByAge sleeping) {
/* 88 */     this.idle = idle;
/* 88 */     this.sleeping = sleeping;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #88	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByState;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #88	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByState;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #88	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByState;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public FoxRenderer.FoxTexturesByAge idle() {
/* 88 */     return this.idle;
/*    */   }
/*    */   
/*    */   public FoxRenderer.FoxTexturesByAge sleeping() {
/* 88 */     return this.sleeping;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\FoxRenderer$FoxTexturesByState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */