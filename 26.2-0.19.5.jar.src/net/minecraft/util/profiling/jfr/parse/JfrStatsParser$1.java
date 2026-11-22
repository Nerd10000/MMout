/*    */ package net.minecraft.util.profiling.jfr.parse;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.UncheckedIOException;
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ import jdk.jfr.consumer.RecordedEvent;
/*    */ import jdk.jfr.consumer.RecordingFile;
/*    */ 
/*    */ class null implements Iterator<RecordedEvent> {
/*    */   public boolean hasNext() {
/* 78 */     return recordingFile.hasMoreEvents();
/*    */   }
/*    */   
/*    */   public RecordedEvent next() {
/* 83 */     if (!hasNext())
/* 84 */       throw new NoSuchElementException(); 
/*    */     try {
/* 87 */       return recordingFile.readEvent();
/* 88 */     } catch (IOException e) {
/* 89 */       throw new UncheckedIOException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\parse\JfrStatsParser$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */