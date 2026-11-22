/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.Optional;
/*     */ 
/*     */ public final class NativeModuleInfo extends Record {
/*     */   private final String name;
/*     */   
/*     */   private final Optional<NativeModuleLister.NativeModuleVersion> version;
/*     */   
/*     */   public NativeModuleInfo(String name, Optional<NativeModuleLister.NativeModuleVersion> version) {
/* 166 */     this.name = name;
/* 166 */     this.version = version;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/NativeModuleLister$NativeModuleInfo;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #166	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/NativeModuleLister$NativeModuleInfo;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/NativeModuleLister$NativeModuleInfo;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #166	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/NativeModuleLister$NativeModuleInfo;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String name() {
/* 166 */     return this.name;
/*     */   }
/*     */   
/*     */   public Optional<NativeModuleLister.NativeModuleVersion> version() {
/* 166 */     return this.version;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 172 */     return this.version.<String>map(v -> this.name + ":" + this.name).orElse(this.name);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\NativeModuleLister$NativeModuleInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */