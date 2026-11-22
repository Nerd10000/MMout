/*     */ package net.minecraft.client.gui.font.providers;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.bytes.ByteList;
/*     */ 
/*     */ final class ByteContents extends Record implements UnihexProvider.LineData {
/*     */   private final byte[] contents;
/*     */   
/*     */   private ByteContents(byte[] contents) {
/* 238 */     this.contents = contents;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$ByteContents;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #238	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$ByteContents;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$ByteContents;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #238	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$ByteContents;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$ByteContents;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #238	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$ByteContents;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public byte[] contents() {
/* 238 */     return this.contents;
/*     */   }
/*     */   
/*     */   public int line(int index) {
/* 241 */     return this.contents[index] << 24;
/*     */   }
/*     */   
/*     */   private static UnihexProvider.LineData read(int line, ByteList input) {
/* 245 */     byte[] content = new byte[16];
/* 246 */     int pos = 0;
/* 247 */     for (int i = 0; i < 16; i++) {
/* 248 */       int n1 = UnihexProvider.decodeHex(line, input, pos++);
/* 249 */       int n0 = UnihexProvider.decodeHex(line, input, pos++);
/* 250 */       byte v = (byte)(n1 << 4 | n0);
/* 251 */       content[i] = v;
/*     */     } 
/* 253 */     return new ByteContents(content);
/*     */   }
/*     */   
/*     */   public int bitWidth() {
/* 258 */     return 8;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\providers\UnihexProvider$ByteContents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */