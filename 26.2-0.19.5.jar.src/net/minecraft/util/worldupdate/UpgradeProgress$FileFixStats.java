/*    */ package net.minecraft.util.worldupdate;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class FileFixStats {
/* 66 */   private final AtomicInteger finishedOperations = new AtomicInteger();
/*    */   
/* 67 */   private final AtomicInteger totalOperations = new AtomicInteger();
/*    */   
/*    */   public int finishedOperations() {
/* 70 */     return this.finishedOperations.get();
/*    */   }
/*    */   
/*    */   public int totalOperations() {
/* 74 */     return this.totalOperations.get();
/*    */   }
/*    */   
/*    */   public void reset() {
/* 78 */     this.finishedOperations.set(0);
/* 79 */     this.totalOperations.set(0);
/*    */   }
/*    */   
/*    */   public float getProgress() {
/* 83 */     if (totalOperations() == 0)
/* 84 */       return 0.0F; 
/* 86 */     return Mth.clamp(finishedOperations() / totalOperations(), 0.0F, 1.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\worldupdate\UpgradeProgress$FileFixStats.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */