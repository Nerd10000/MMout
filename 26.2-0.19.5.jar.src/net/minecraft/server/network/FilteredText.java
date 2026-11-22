/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.network.chat.FilterMask;
/*    */ 
/*    */ public final class FilteredText extends Record {
/*    */   private final String raw;
/*    */   
/*    */   private final FilterMask mask;
/*    */   
/*    */   public FilteredText(String raw, FilterMask mask) {
/*  8 */     this.raw = raw;
/*  8 */     this.mask = mask;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/network/FilteredText;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/network/FilteredText;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/network/FilteredText;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/network/FilteredText;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/network/FilteredText;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/network/FilteredText;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String raw() {
/*  8 */     return this.raw;
/*    */   }
/*    */   
/*    */   public FilterMask mask() {
/*  8 */     return this.mask;
/*    */   }
/*    */   
/*  9 */   public static final FilteredText EMPTY = passThrough("");
/*    */   
/*    */   public static FilteredText passThrough(String message) {
/* 12 */     return new FilteredText(message, FilterMask.PASS_THROUGH);
/*    */   }
/*    */   
/*    */   public static FilteredText fullyFiltered(String message) {
/* 16 */     return new FilteredText(message, FilterMask.FULLY_FILTERED);
/*    */   }
/*    */   
/*    */   public String filtered() {
/* 20 */     return this.mask.apply(this.raw);
/*    */   }
/*    */   
/*    */   public String filteredOrEmpty() {
/* 24 */     return Objects.<String>requireNonNullElse(filtered(), "");
/*    */   }
/*    */   
/*    */   public boolean isFiltered() {
/* 28 */     return !this.mask.isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\FilteredText.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */