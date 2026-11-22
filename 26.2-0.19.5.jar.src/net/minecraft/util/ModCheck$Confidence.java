/*    */ package net.minecraft.util;
/*    */ 
/*    */ public enum Confidence {
/* 13 */   PROBABLY_NOT("Probably not.", false),
/* 14 */   VERY_LIKELY("Very likely;", true),
/* 15 */   DEFINITELY("Definitely;", true);
/*    */   
/*    */   private final String description;
/*    */   
/*    */   private final boolean shouldReportAsModified;
/*    */   
/*    */   Confidence(String description, boolean shouldReportAsModified) {
/* 22 */     this.description = description;
/* 23 */     this.shouldReportAsModified = shouldReportAsModified;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ModCheck$Confidence.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */