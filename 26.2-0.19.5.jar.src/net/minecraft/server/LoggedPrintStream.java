/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintStream;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class LoggedPrintStream extends PrintStream {
/* 12 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   protected final String name;
/*    */   
/*    */   public LoggedPrintStream(String name, OutputStream out) {
/* 17 */     super(out, false, StandardCharsets.UTF_8);
/* 18 */     this.name = name;
/*    */   }
/*    */   
/*    */   public void println(String string) {
/* 23 */     logLine(string);
/*    */   }
/*    */   
/*    */   public void println(Object object) {
/* 28 */     logLine(String.valueOf(object));
/*    */   }
/*    */   
/*    */   protected void logLine(String out) {
/* 32 */     LOGGER.info("[{}]: {}", this.name, out);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\LoggedPrintStream.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */