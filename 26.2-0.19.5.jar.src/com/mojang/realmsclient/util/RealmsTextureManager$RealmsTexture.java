/*    */ package com.mojang.realmsclient.util;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class RealmsTexture extends Record {
/*    */   private final String image;
/*    */   
/*    */   private final Identifier textureId;
/*    */   
/*    */   public RealmsTexture(String image, Identifier textureId) {
/* 52 */     this.image = image;
/* 52 */     this.textureId = textureId;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/util/RealmsTextureManager$RealmsTexture;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #52	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/util/RealmsTextureManager$RealmsTexture;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/util/RealmsTextureManager$RealmsTexture;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #52	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/util/RealmsTextureManager$RealmsTexture;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/util/RealmsTextureManager$RealmsTexture;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #52	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/util/RealmsTextureManager$RealmsTexture;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String image() {
/* 52 */     return this.image;
/*    */   }
/*    */   
/*    */   public Identifier textureId() {
/* 52 */     return this.textureId;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclien\\util\RealmsTextureManager$RealmsTexture.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */