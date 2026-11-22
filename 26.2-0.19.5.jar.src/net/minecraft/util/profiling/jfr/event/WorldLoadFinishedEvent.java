/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import jdk.jfr.Category;
/*    */ import jdk.jfr.Event;
/*    */ import jdk.jfr.EventType;
/*    */ import jdk.jfr.Label;
/*    */ import jdk.jfr.Name;
/*    */ import jdk.jfr.StackTrace;
/*    */ 
/*    */ @Name("minecraft.LoadWorld")
/*    */ @Label("Create/Load World")
/*    */ @Category({"Minecraft", "World Generation"})
/*    */ @StackTrace(false)
/*    */ public class WorldLoadFinishedEvent extends Event {
/*    */   public static final String EVENT_NAME = "minecraft.LoadWorld";
/*    */   
/* 17 */   public static final EventType TYPE = EventType.getEventType((Class)WorldLoadFinishedEvent.class);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\event\WorldLoadFinishedEvent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */