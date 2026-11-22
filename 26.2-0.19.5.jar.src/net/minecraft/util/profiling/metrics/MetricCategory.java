/*    */ package net.minecraft.util.profiling.metrics;
/*    */ 
/*    */ public enum MetricCategory {
/*  4 */   PATH_FINDING("pathfinding"),
/*  5 */   EVENT_LOOPS("event-loops"),
/*  6 */   CONSECUTIVE_EXECUTORS("consecutive-executors"),
/*  7 */   TICK_LOOP("ticking"),
/*  8 */   JVM("jvm"),
/*  9 */   CHUNK_RENDERING("chunk rendering"),
/* 10 */   CHUNK_RENDERING_DISPATCHING("chunk rendering dispatching"),
/* 11 */   CPU("cpu"),
/* 12 */   GPU("gpu");
/*    */   
/*    */   private final String description;
/*    */   
/*    */   MetricCategory(String description) {
/* 18 */     this.description = description;
/*    */   }
/*    */   
/*    */   public String getDescription() {
/* 22 */     return this.description;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\metrics\MetricCategory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */