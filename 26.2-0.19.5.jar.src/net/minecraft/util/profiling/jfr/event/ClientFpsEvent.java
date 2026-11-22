/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import jdk.jfr.Category;
/*    */ import jdk.jfr.Event;
/*    */ import jdk.jfr.EventType;
/*    */ import jdk.jfr.Label;
/*    */ import jdk.jfr.Name;
/*    */ import jdk.jfr.Period;
/*    */ import jdk.jfr.StackTrace;
/*    */ 
/*    */ @Name("minecraft.ClientFps")
/*    */ @Label("Client fps")
/*    */ @Category({"Minecraft", "Ticking"})
/*    */ @StackTrace(false)
/*    */ @Period("1 s")
/*    */ public class ClientFpsEvent extends Event {
/*    */   public static final String EVENT_NAME = "minecraft.ClientFps";
/*    */   
/* 20 */   public static final EventType TYPE = EventType.getEventType((Class)ClientFpsEvent.class);
/*    */   
/*    */   @Name("fps")
/*    */   @Label("Client fps")
/*    */   public final int fps;
/*    */   
/*    */   public ClientFpsEvent(int fps) {
/* 27 */     this.fps = fps;
/*    */   }
/*    */   
/*    */   public static class Fields {
/*    */     public static final String FPS = "fps";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\event\ClientFpsEvent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */