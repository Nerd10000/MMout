/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import java.util.OptionalInt;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ final class CacheKey extends Record {
/*    */   private final Component message;
/*    */   
/*    */   private final int maxWidth;
/*    */   
/*    */   private final OptionalInt maxRows;
/*    */   
/*    */   private CacheKey(Component message, int maxWidth, OptionalInt maxRows) {
/* 85 */     this.message = message;
/* 85 */     this.maxWidth = maxWidth;
/* 85 */     this.maxRows = maxRows;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/MultiLineTextWidget$CacheKey;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #85	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/components/MultiLineTextWidget$CacheKey;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/MultiLineTextWidget$CacheKey;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #85	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/components/MultiLineTextWidget$CacheKey;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/MultiLineTextWidget$CacheKey;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #85	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/components/MultiLineTextWidget$CacheKey;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Component message() {
/* 85 */     return this.message;
/*    */   }
/*    */   
/*    */   public int maxWidth() {
/* 85 */     return this.maxWidth;
/*    */   }
/*    */   
/*    */   public OptionalInt maxRows() {
/* 85 */     return this.maxRows;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\MultiLineTextWidget$CacheKey.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */