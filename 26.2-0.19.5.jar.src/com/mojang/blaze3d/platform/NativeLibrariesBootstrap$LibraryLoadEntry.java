/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ final class LibraryLoadEntry extends Record {
/*     */   private final String name;
/*     */   
/*     */   private final Runnable loader;
/*     */   
/*     */   private LibraryLoadEntry(String name, Runnable loader) {
/* 389 */     this.name = name;
/* 389 */     this.loader = loader;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/platform/NativeLibrariesBootstrap$LibraryLoadEntry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #389	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/platform/NativeLibrariesBootstrap$LibraryLoadEntry;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/platform/NativeLibrariesBootstrap$LibraryLoadEntry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #389	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/platform/NativeLibrariesBootstrap$LibraryLoadEntry;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/platform/NativeLibrariesBootstrap$LibraryLoadEntry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #389	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/blaze3d/platform/NativeLibrariesBootstrap$LibraryLoadEntry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String name() {
/* 389 */     return this.name;
/*     */   }
/*     */   
/*     */   public Runnable loader() {
/* 389 */     return this.loader;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\NativeLibrariesBootstrap$LibraryLoadEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */