/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class Entry {
/*    */   private String filename;
/*    */   
/* 54 */   private final List<String> jsonKeys = Lists.newArrayList();
/*    */   
/*    */   private void addJsonKey(String name) {
/* 60 */     this.jsonKeys.add(0, name);
/*    */   }
/*    */   
/*    */   public String getFilename() {
/* 64 */     return this.filename;
/*    */   }
/*    */   
/*    */   public String getJsonKeys() {
/* 68 */     return StringUtils.join(this.jsonKeys, "->");
/*    */   }
/*    */   
/*    */   public String toString() {
/* 73 */     if (this.filename != null) {
/* 74 */       if (this.jsonKeys.isEmpty())
/* 75 */         return this.filename; 
/* 77 */       return this.filename + " " + this.filename;
/*    */     } 
/* 80 */     if (this.jsonKeys.isEmpty())
/* 81 */       return "(Unknown file)"; 
/* 83 */     return "(Unknown file) " + getJsonKeys();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\ChainedJsonException$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */