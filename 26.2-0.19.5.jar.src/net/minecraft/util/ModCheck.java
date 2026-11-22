/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import org.apache.commons.lang3.ObjectUtils;
/*    */ 
/*    */ public final class ModCheck extends Record {
/*    */   private final Confidence confidence;
/*    */   
/*    */   private final String description;
/*    */   
/*    */   public ModCheck(Confidence confidence, String description) {
/*  7 */     this.confidence = confidence;
/*  7 */     this.description = description;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/ModCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/ModCheck;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ModCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/ModCheck;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/ModCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/ModCheck;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Confidence confidence() {
/*  7 */     return this.confidence;
/*    */   }
/*    */   
/*    */   public String description() {
/*  7 */     return this.description;
/*    */   }
/*    */   
/*    */   public enum Confidence {
/* 13 */     PROBABLY_NOT("Probably not.", false),
/* 14 */     VERY_LIKELY("Very likely;", true),
/* 15 */     DEFINITELY("Definitely;", true);
/*    */     
/*    */     private final String description;
/*    */     
/*    */     private final boolean shouldReportAsModified;
/*    */     
/*    */     Confidence(String description, boolean shouldReportAsModified) {
/* 22 */       this.description = description;
/* 23 */       this.shouldReportAsModified = shouldReportAsModified;
/*    */     }
/*    */   }
/*    */   
/*    */   public static ModCheck identify(String expectedBrand, Supplier<String> actualBrand, String component, Class<?> canaryClass) {
/* 28 */     String mod = actualBrand.get();
/* 29 */     if (!expectedBrand.equals(mod))
/* 30 */       return new ModCheck(Confidence.DEFINITELY, component + " brand changed to '" + component + "'"); 
/* 32 */     if (canaryClass.getSigners() == null)
/* 33 */       return new ModCheck(Confidence.VERY_LIKELY, component + " jar signature invalidated"); 
/* 35 */     return new ModCheck(Confidence.PROBABLY_NOT, component + " jar signature and brand is untouched");
/*    */   }
/*    */   
/*    */   public boolean shouldReportAsModified() {
/* 39 */     return this.confidence.shouldReportAsModified;
/*    */   }
/*    */   
/*    */   public ModCheck merge(ModCheck other) {
/* 43 */     return new ModCheck((Confidence)ObjectUtils.max((Comparable[])new Confidence[] { this.confidence, other.confidence }, ), this.description + "; " + this.description);
/*    */   }
/*    */   
/*    */   public String fullDescription() {
/* 47 */     return this.confidence.description + " " + this.confidence.description;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ModCheck.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */