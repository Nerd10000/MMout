/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import net.minecraft.client.renderer.item.ClientItem;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ final class PendingLoad extends Record {
/*    */   private final Identifier id;
/*    */   
/*    */   private final ClientItem clientItemInfo;
/*    */   
/*    */   private PendingLoad(Identifier id, ClientItem clientItemInfo) {
/* 75 */     this.id = id;
/* 75 */     this.clientItemInfo = clientItemInfo;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/ClientItemInfoLoader$PendingLoad;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #75	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/ClientItemInfoLoader$PendingLoad;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/ClientItemInfoLoader$PendingLoad;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #75	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/ClientItemInfoLoader$PendingLoad;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/ClientItemInfoLoader$PendingLoad;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #75	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/ClientItemInfoLoader$PendingLoad;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier id() {
/* 75 */     return this.id;
/*    */   }
/*    */   
/*    */   public ClientItem clientItemInfo() {
/* 75 */     return this.clientItemInfo;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\ClientItemInfoLoader$PendingLoad.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */