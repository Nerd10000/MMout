/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.OutputStream;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class DebugLoggedPrintStream extends LoggedPrintStream {
/* 10 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public DebugLoggedPrintStream(String name, OutputStream out) {
/* 13 */     super(name, out);
/*    */   }
/*    */   
/*    */   protected void logLine(String out) {
/* 18 */     StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
/* 19 */     StackTraceElement stackTraceElement = stackTrace[Math.min(3, stackTrace.length)];
/* 20 */     LOGGER.info("[{}]@.({}:{}): {}", new Object[] { this.name, stackTraceElement.getFileName(), Integer.valueOf(stackTraceElement.getLineNumber()), out });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\DebugLoggedPrintStream.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */