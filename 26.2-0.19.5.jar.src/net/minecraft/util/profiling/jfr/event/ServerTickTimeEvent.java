/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import jdk.jfr.Category;
/*    */ import jdk.jfr.Event;
/*    */ import jdk.jfr.EventType;
/*    */ import jdk.jfr.Label;
/*    */ import jdk.jfr.Name;
/*    */ import jdk.jfr.Period;
/*    */ import jdk.jfr.StackTrace;
/*    */ import jdk.jfr.Timespan;
/*    */ 
/*    */ @Name("minecraft.ServerTickTime")
/*    */ @Label("Server Tick Time")
/*    */ @Category({"Minecraft", "Ticking"})
/*    */ @StackTrace(false)
/*    */ @Period("1 s")
/*    */ public class ServerTickTimeEvent extends Event {
/*    */   public static final String EVENT_NAME = "minecraft.ServerTickTime";
/*    */   
/* 21 */   public static final EventType TYPE = EventType.getEventType((Class)ServerTickTimeEvent.class);
/*    */   
/*    */   @Name("averageTickDuration")
/*    */   @Label("Average Server Tick Duration")
/*    */   @Timespan
/*    */   public final long averageTickDurationNanos;
/*    */   
/*    */   public ServerTickTimeEvent(float averageTickTimeMs) {
/* 30 */     this.averageTickDurationNanos = (long)(1000000.0F * averageTickTimeMs);
/*    */   }
/*    */   
/*    */   public static class Fields {
/*    */     public static final String AVERAGE_TICK_DURATION = "averageTickDuration";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\event\ServerTickTimeEvent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */