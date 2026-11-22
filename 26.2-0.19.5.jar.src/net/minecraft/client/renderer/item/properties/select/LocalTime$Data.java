/*    */ package net.minecraft.client.renderer.item.properties.select;
/*    */ 
/*    */ import com.ibm.icu.util.TimeZone;
/*    */ import java.util.Optional;
/*    */ 
/*    */ final class Data extends Record {
/*    */   private final String format;
/*    */   
/*    */   private final String localeId;
/*    */   
/*    */   private final Optional<TimeZone> timeZone;
/*    */   
/*    */   private Data(String format, String localeId, Optional<TimeZone> timeZone) {
/* 40 */     this.format = format;
/* 40 */     this.localeId = localeId;
/* 40 */     this.timeZone = timeZone;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/select/LocalTime$Data;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #40	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/LocalTime$Data;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/select/LocalTime$Data;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #40	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/LocalTime$Data;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/select/LocalTime$Data;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #40	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/select/LocalTime$Data;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String format() {
/* 40 */     return this.format;
/*    */   }
/*    */   
/*    */   public String localeId() {
/* 40 */     return this.localeId;
/*    */   }
/*    */   
/*    */   public Optional<TimeZone> timeZone() {
/* 40 */     return this.timeZone;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\select\LocalTime$Data.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */