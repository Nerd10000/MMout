/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ final class FoxTexturesByAge extends Record {
/*    */   private final Identifier adult;
/*    */   
/*    */   private final Identifier baby;
/*    */   
/*    */   private FoxTexturesByAge(Identifier adult, Identifier baby) {
/* 86 */     this.adult = adult;
/* 86 */     this.baby = baby;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByAge;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #86	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByAge;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByAge;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #86	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByAge;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByAge;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #86	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByAge;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier adult() {
/* 86 */     return this.adult;
/*    */   }
/*    */   
/*    */   public Identifier baby() {
/* 86 */     return this.baby;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\FoxRenderer$FoxTexturesByAge.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */