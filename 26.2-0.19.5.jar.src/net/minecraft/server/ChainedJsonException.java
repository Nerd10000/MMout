/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class ChainedJsonException extends IOException {
/* 12 */   private final List<Entry> entries = Lists.newArrayList();
/*    */   
/*    */   private final String message;
/*    */   
/*    */   public ChainedJsonException(String message) {
/* 16 */     this.entries.add(new Entry());
/* 17 */     this.message = message;
/*    */   }
/*    */   
/*    */   public ChainedJsonException(String message, Throwable cause) {
/* 21 */     super(cause);
/* 22 */     this.entries.add(new Entry());
/* 23 */     this.message = message;
/*    */   }
/*    */   
/*    */   public void prependJsonKey(String key) {
/* 27 */     ((Entry)this.entries.get(0)).addJsonKey(key);
/*    */   }
/*    */   
/*    */   public void setFilenameAndFlush(String filename) {
/* 31 */     ((Entry)this.entries.get(0)).filename = filename;
/* 32 */     this.entries.add(0, new Entry());
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 37 */     return "Invalid " + String.valueOf(this.entries.get(this.entries.size() - 1)) + ": " + this.message;
/*    */   }
/*    */   
/*    */   public static ChainedJsonException forException(Exception e) {
/* 41 */     if (e instanceof ChainedJsonException) {
/* 41 */       ChainedJsonException chainedJsonException = (ChainedJsonException)e;
/* 42 */       return chainedJsonException;
/*    */     } 
/* 44 */     String message = e.getMessage();
/* 45 */     if (e instanceof java.io.FileNotFoundException)
/* 46 */       message = "File not found"; 
/* 48 */     return new ChainedJsonException(message, e);
/*    */   }
/*    */   
/*    */   public static class Entry {
/*    */     private String filename;
/*    */     
/* 54 */     private final List<String> jsonKeys = Lists.newArrayList();
/*    */     
/*    */     private void addJsonKey(String name) {
/* 60 */       this.jsonKeys.add(0, name);
/*    */     }
/*    */     
/*    */     public String getFilename() {
/* 64 */       return this.filename;
/*    */     }
/*    */     
/*    */     public String getJsonKeys() {
/* 68 */       return StringUtils.join(this.jsonKeys, "->");
/*    */     }
/*    */     
/*    */     public String toString() {
/* 73 */       if (this.filename != null) {
/* 74 */         if (this.jsonKeys.isEmpty())
/* 75 */           return this.filename; 
/* 77 */         return this.filename + " " + this.filename;
/*    */       } 
/* 80 */       if (this.jsonKeys.isEmpty())
/* 81 */         return "(Unknown file)"; 
/* 83 */       return "(Unknown file) " + getJsonKeys();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\ChainedJsonException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */