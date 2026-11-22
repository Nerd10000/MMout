/*    */ package net.minecraft.util.eventlog;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ class null implements JsonEventLogReader<T> {
/*    */   private volatile long position;
/*    */   
/*    */   public T next() throws IOException {
/*    */     try {
/* 61 */       JsonEventLog.this.channel.position(this.position);
/* 62 */       return (T)reader.next();
/*    */     } finally {
/* 64 */       this.position = JsonEventLog.this.channel.position();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void close() throws IOException {
/* 70 */     JsonEventLog.this.releaseReference();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\eventlog\JsonEventLog$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */