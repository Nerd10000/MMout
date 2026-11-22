/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.primitives.Longs;
/*     */ import it.unimi.dsi.fastutil.bytes.ByteArrays;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ 
/*     */ public final class SaltSignaturePair extends Record {
/*     */   private final long salt;
/*     */   
/*     */   private final byte[] signature;
/*     */   
/*     */   public SaltSignaturePair(long salt, byte[] signature) {
/* 215 */     this.salt = salt;
/* 215 */     this.signature = signature;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/Crypt$SaltSignaturePair;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #215	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/Crypt$SaltSignaturePair;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/Crypt$SaltSignaturePair;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #215	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/Crypt$SaltSignaturePair;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/Crypt$SaltSignaturePair;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #215	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/Crypt$SaltSignaturePair;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public long salt() {
/* 215 */     return this.salt;
/*     */   }
/*     */   
/*     */   public byte[] signature() {
/* 215 */     return this.signature;
/*     */   }
/*     */   
/* 216 */   public static final SaltSignaturePair EMPTY = new SaltSignaturePair(0L, ByteArrays.EMPTY_ARRAY);
/*     */   
/*     */   public SaltSignaturePair(FriendlyByteBuf input) {
/* 219 */     this(input.readLong(), input.readByteArray());
/*     */   }
/*     */   
/*     */   public boolean isValid() {
/* 223 */     return (this.signature.length > 0);
/*     */   }
/*     */   
/*     */   public static void write(FriendlyByteBuf output, SaltSignaturePair saltSignaturePair) {
/* 227 */     output.writeLong(saltSignaturePair.salt);
/* 228 */     output.writeByteArray(saltSignaturePair.signature);
/*     */   }
/*     */   
/*     */   public byte[] saltAsBytes() {
/* 232 */     return Longs.toByteArray(this.salt);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\Crypt$SaltSignaturePair.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */