/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.renderer.item.ClientItem;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class LoadedClientInfos extends Record {
/*    */   private final Map<Identifier, ClientItem> contents;
/*    */   
/*    */   public LoadedClientInfos(Map<Identifier, ClientItem> contents) {
/* 78 */     this.contents = contents;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/ClientItemInfoLoader$LoadedClientInfos;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/ClientItemInfoLoader$LoadedClientInfos;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/ClientItemInfoLoader$LoadedClientInfos;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/ClientItemInfoLoader$LoadedClientInfos;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/ClientItemInfoLoader$LoadedClientInfos;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/ClientItemInfoLoader$LoadedClientInfos;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<Identifier, ClientItem> contents() {
/* 78 */     return this.contents;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\ClientItemInfoLoader$LoadedClientInfos.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */