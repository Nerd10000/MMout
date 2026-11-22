/*     */ package net.minecraft.client.gui.font.providers;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.bytes.ByteList;
/*     */ 
/*     */ final class IntContents extends Record implements UnihexProvider.LineData {
/*     */   private final int[] contents;
/*     */   
/*     */   private final int bitWidth;
/*     */   
/*     */   private static final int SIZE_24 = 24;
/*     */   
/*     */   private IntContents(int[] contents, int bitWidth) {
/* 288 */     this.contents = contents;
/* 288 */     this.bitWidth = bitWidth;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$IntContents;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #288	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$IntContents;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$IntContents;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #288	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$IntContents;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$IntContents;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #288	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$IntContents;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int[] contents() {
/* 288 */     return this.contents;
/*     */   }
/*     */   
/*     */   public int bitWidth() {
/* 288 */     return this.bitWidth;
/*     */   }
/*     */   
/*     */   public int line(int index) {
/* 293 */     return this.contents[index];
/*     */   }
/*     */   
/*     */   private static UnihexProvider.LineData read24(int line, ByteList input) {
/* 297 */     int[] content = new int[16];
/* 298 */     int mask = 0;
/* 299 */     int pos = 0;
/* 300 */     for (int i = 0; i < 16; i++) {
/* 301 */       int n5 = UnihexProvider.decodeHex(line, input, pos++);
/* 302 */       int n4 = UnihexProvider.decodeHex(line, input, pos++);
/* 303 */       int n3 = UnihexProvider.decodeHex(line, input, pos++);
/* 304 */       int n2 = UnihexProvider.decodeHex(line, input, pos++);
/* 305 */       int n1 = UnihexProvider.decodeHex(line, input, pos++);
/* 306 */       int n0 = UnihexProvider.decodeHex(line, input, pos++);
/* 307 */       int v = n5 << 20 | n4 << 16 | n3 << 12 | n2 << 8 | n1 << 4 | n0;
/* 308 */       content[i] = v << 8;
/* 309 */       mask |= v;
/*     */     } 
/* 311 */     return new IntContents(content, 24);
/*     */   }
/*     */   
/*     */   public static UnihexProvider.LineData read32(int line, ByteList input) {
/* 315 */     int[] content = new int[16];
/* 316 */     int mask = 0;
/* 317 */     int pos = 0;
/* 318 */     for (int i = 0; i < 16; i++) {
/* 319 */       int n7 = UnihexProvider.decodeHex(line, input, pos++);
/* 320 */       int n6 = UnihexProvider.decodeHex(line, input, pos++);
/* 321 */       int n5 = UnihexProvider.decodeHex(line, input, pos++);
/* 322 */       int n4 = UnihexProvider.decodeHex(line, input, pos++);
/* 323 */       int n3 = UnihexProvider.decodeHex(line, input, pos++);
/* 324 */       int n2 = UnihexProvider.decodeHex(line, input, pos++);
/* 325 */       int n1 = UnihexProvider.decodeHex(line, input, pos++);
/* 326 */       int n0 = UnihexProvider.decodeHex(line, input, pos++);
/* 327 */       int v = n7 << 28 | n6 << 24 | n5 << 20 | n4 << 16 | n3 << 12 | n2 << 8 | n1 << 4 | n0;
/* 328 */       content[i] = v;
/* 329 */       mask |= v;
/*     */     } 
/* 331 */     return new IntContents(content, 32);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\providers\UnihexProvider$IntContents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */