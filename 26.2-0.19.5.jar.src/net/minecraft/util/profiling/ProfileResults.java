/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.List;
/*    */ 
/*    */ public interface ProfileResults {
/*    */   public static final char PATH_SEPARATOR = '\036';
/*    */   
/*    */   List<ResultField> getTimes(String paramString);
/*    */   
/*    */   boolean saveResults(Path paramPath);
/*    */   
/*    */   long getStartTimeNano();
/*    */   
/*    */   int getStartTimeTicks();
/*    */   
/*    */   long getEndTimeNano();
/*    */   
/*    */   int getEndTimeTicks();
/*    */   
/*    */   default long getNanoDuration() {
/* 22 */     return getEndTimeNano() - getStartTimeNano();
/*    */   }
/*    */   
/*    */   default int getTickDuration() {
/* 26 */     return getEndTimeTicks() - getStartTimeTicks();
/*    */   }
/*    */   
/*    */   String getProfilerResults();
/*    */   
/*    */   static String demanglePath(String path) {
/* 32 */     return path.replace('\036', '.');
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\ProfileResults.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */