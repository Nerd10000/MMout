/*     */ package net.minecraft.client.gui.font.providers;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.bytes.ByteList;
/*     */ 
/*     */ final class ShortContents extends Record implements UnihexProvider.LineData {
/*     */   private final short[] contents;
/*     */   
/*     */   private ShortContents(short[] contents) {
/* 262 */     this.contents = contents;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$ShortContents;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #262	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$ShortContents;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$ShortContents;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #262	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$ShortContents;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$ShortContents;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #262	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$ShortContents;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public short[] contents() {
/* 262 */     return this.contents;
/*     */   }
/*     */   
/*     */   public int line(int index) {
/* 265 */     return this.contents[index] << 16;
/*     */   }
/*     */   
/*     */   private static UnihexProvider.LineData read(int line, ByteList input) {
/* 269 */     short[] content = new short[16];
/* 270 */     int pos = 0;
/* 271 */     for (int i = 0; i < 16; i++) {
/* 272 */       int n3 = UnihexProvider.decodeHex(line, input, pos++);
/* 273 */       int n2 = UnihexProvider.decodeHex(line, input, pos++);
/* 274 */       int n1 = UnihexProvider.decodeHex(line, input, pos++);
/* 275 */       int n0 = UnihexProvider.decodeHex(line, input, pos++);
/* 276 */       short v = (short)(n3 << 12 | n2 << 8 | n1 << 4 | n0);
/* 277 */       content[i] = v;
/*     */     } 
/* 279 */     return new ShortContents(content);
/*     */   }
/*     */   
/*     */   public int bitWidth() {
/* 284 */     return 16;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\providers\UnihexProvider$ShortContents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */