/*     */ package net.minecraft.client;
/*     */ 
/*     */ public final class Notification extends Record {
/*     */   private final long delay;
/*     */   
/*     */   private final long period;
/*     */   
/*     */   private final String title;
/*     */   
/*     */   private final String message;
/*     */   
/*     */   public String message() {
/* 124 */     return this.message;
/*     */   }
/*     */   
/*     */   public String title() {
/* 124 */     return this.title;
/*     */   }
/*     */   
/*     */   public long period() {
/* 124 */     return this.period;
/*     */   }
/*     */   
/*     */   public long delay() {
/* 124 */     return this.delay;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/PeriodicNotificationManager$Notification;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #124	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/PeriodicNotificationManager$Notification;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/PeriodicNotificationManager$Notification;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #124	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/PeriodicNotificationManager$Notification;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/PeriodicNotificationManager$Notification;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #124	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/PeriodicNotificationManager$Notification;
/*     */   }
/*     */   
/*     */   public Notification(long delay, long period, String title, String message) {
/* 126 */     this.delay = (delay != 0L) ? delay : period;
/* 127 */     this.period = period;
/* 128 */     this.title = title;
/* 129 */     this.message = message;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\PeriodicNotificationManager$Notification.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */