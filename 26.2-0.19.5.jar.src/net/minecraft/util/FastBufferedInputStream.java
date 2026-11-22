/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class FastBufferedInputStream extends InputStream {
/*    */   private static final int DEFAULT_BUFFER_SIZE = 8192;
/*    */   
/*    */   private final InputStream in;
/*    */   
/*    */   private final byte[] buffer;
/*    */   
/*    */   private int limit;
/*    */   
/*    */   private int position;
/*    */   
/*    */   public FastBufferedInputStream(InputStream in) {
/* 19 */     this(in, 8192);
/*    */   }
/*    */   
/*    */   public FastBufferedInputStream(InputStream in, int bufferSize) {
/* 23 */     this.in = in;
/* 24 */     this.buffer = new byte[bufferSize];
/*    */   }
/*    */   
/*    */   public int read() throws IOException {
/* 29 */     if (this.position >= this.limit) {
/* 30 */       fill();
/* 31 */       if (this.position >= this.limit)
/* 32 */         return -1; 
/*    */     } 
/* 35 */     return Byte.toUnsignedInt(this.buffer[this.position++]);
/*    */   }
/*    */   
/*    */   public int read(byte[] output, int offset, int length) throws IOException {
/* 40 */     int availableInBuffer = bytesInBuffer();
/* 41 */     if (availableInBuffer <= 0) {
/* 42 */       if (length >= this.buffer.length)
/* 43 */         return this.in.read(output, offset, length); 
/* 45 */       fill();
/* 46 */       availableInBuffer = bytesInBuffer();
/* 47 */       if (availableInBuffer <= 0)
/* 48 */         return -1; 
/*    */     } 
/* 51 */     if (length > availableInBuffer)
/* 52 */       length = availableInBuffer; 
/* 54 */     System.arraycopy(this.buffer, this.position, output, offset, length);
/* 55 */     this.position += length;
/* 56 */     return length;
/*    */   }
/*    */   
/*    */   public long skip(long count) throws IOException {
/* 61 */     if (count <= 0L)
/* 62 */       return 0L; 
/* 64 */     long availableInBuffer = bytesInBuffer();
/* 65 */     if (availableInBuffer <= 0L)
/* 66 */       return this.in.skip(count); 
/* 68 */     if (count > availableInBuffer)
/* 69 */       count = availableInBuffer; 
/* 71 */     this.position = (int)(this.position + count);
/* 72 */     return count;
/*    */   }
/*    */   
/*    */   public int available() throws IOException {
/* 77 */     return bytesInBuffer() + this.in.available();
/*    */   }
/*    */   
/*    */   public void close() throws IOException {
/* 82 */     this.in.close();
/*    */   }
/*    */   
/*    */   private int bytesInBuffer() {
/* 86 */     return this.limit - this.position;
/*    */   }
/*    */   
/*    */   private void fill() throws IOException {
/* 90 */     this.limit = 0;
/* 91 */     this.position = 0;
/* 92 */     int actuallyRead = this.in.read(this.buffer, 0, this.buffer.length);
/* 93 */     if (actuallyRead > 0)
/* 94 */       this.limit = actuallyRead; 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\FastBufferedInputStream.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */