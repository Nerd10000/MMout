/*    */ package net.minecraft.realms;
/*    */ 
/*    */ import com.google.common.util.concurrent.RateLimiter;
/*    */ import java.time.Duration;
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ import net.minecraft.client.GameNarrator;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class RepeatedNarrator {
/*    */   private final float permitsPerSecond;
/*    */   
/* 13 */   private final AtomicReference<Params> params = new AtomicReference<>();
/*    */   
/*    */   public RepeatedNarrator(Duration repeatDelay) {
/* 16 */     this.permitsPerSecond = 1000.0F / (float)repeatDelay.toMillis();
/*    */   }
/*    */   
/*    */   public void narrate(GameNarrator narrator, Component narration) {
/* 20 */     Params params = this.params.updateAndGet(existing -> 
/* 21 */         (existing == null || !narration.equals(existing.narration)) ? new Params(narration, RateLimiter.create(this.permitsPerSecond)) : existing);
/* 26 */     if (params.rateLimiter.tryAcquire(1))
/* 27 */       narrator.saySystemNow(narration); 
/*    */   }
/*    */   
/*    */   private static final class Params extends Record {
/*    */     private final Component narration;
/*    */     
/*    */     private final RateLimiter rateLimiter;
/*    */     
/*    */     private Params(Component narration, RateLimiter rateLimiter) {
/* 31 */       this.narration = narration;
/* 31 */       this.rateLimiter = rateLimiter;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/realms/RepeatedNarrator$Params;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/realms/RepeatedNarrator$Params;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/realms/RepeatedNarrator$Params;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/realms/RepeatedNarrator$Params;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/realms/RepeatedNarrator$Params;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/realms/RepeatedNarrator$Params;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Component narration() {
/* 31 */       return this.narration;
/*    */     }
/*    */     
/*    */     public RateLimiter rateLimiter() {
/* 31 */       return this.rateLimiter;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\realms\RepeatedNarrator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */