/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ public final class ResultField implements Comparable<ResultField> {
/*    */   public final double percentage;
/*    */   
/*    */   public final double globalPercentage;
/*    */   
/*    */   public final long count;
/*    */   
/*    */   public final String name;
/*    */   
/*    */   public ResultField(String name, double percentage, double globalPercentage, long count) {
/* 10 */     this.name = name;
/* 11 */     this.percentage = percentage;
/* 12 */     this.globalPercentage = globalPercentage;
/* 13 */     this.count = count;
/*    */   }
/*    */   
/*    */   public int compareTo(ResultField resultField) {
/* 18 */     if (resultField.percentage < this.percentage)
/* 19 */       return -1; 
/* 21 */     if (resultField.percentage > this.percentage)
/* 22 */       return 1; 
/* 24 */     return resultField.name.compareTo(this.name);
/*    */   }
/*    */   
/*    */   public int getColor() {
/* 28 */     return (this.name.hashCode() & 0xAAAAAA) + -12303292;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\ResultField.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */