/*     */ package net.minecraft.util;
/*     */ 
/*     */ public final class NativeModuleVersion extends Record {
/*     */   private final String description;
/*     */   
/*     */   private final String version;
/*     */   
/*     */   private final String company;
/*     */   
/*     */   public NativeModuleVersion(String description, String version, String company) {
/* 154 */     this.description = description;
/* 154 */     this.version = version;
/* 154 */     this.company = company;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/NativeModuleLister$NativeModuleVersion;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #154	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/NativeModuleLister$NativeModuleVersion;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/NativeModuleLister$NativeModuleVersion;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #154	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/NativeModuleLister$NativeModuleVersion;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String description() {
/* 154 */     return this.description;
/*     */   }
/*     */   
/*     */   public String version() {
/* 154 */     return this.version;
/*     */   }
/*     */   
/*     */   public String company() {
/* 154 */     return this.company;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 162 */     return this.description + ":" + this.description + ":" + this.version;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\NativeModuleLister$NativeModuleVersion.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */