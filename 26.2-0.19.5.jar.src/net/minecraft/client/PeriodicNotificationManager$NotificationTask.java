/*     */ package net.minecraft.client;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.TimerTask;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ class NotificationTask extends TimerTask {
/* 134 */   private final Minecraft minecraft = Minecraft.getInstance();
/*     */   
/*     */   private final List<PeriodicNotificationManager.Notification> notifications;
/*     */   
/*     */   private final long period;
/*     */   
/*     */   private final AtomicLong elapsed;
/*     */   
/*     */   public NotificationTask(List<PeriodicNotificationManager.Notification> notifications, long elapsed, long period) {
/* 142 */     this.notifications = notifications;
/* 143 */     this.period = period;
/* 144 */     this.elapsed = new AtomicLong(elapsed);
/*     */   }
/*     */   
/*     */   public NotificationTask reset(List<PeriodicNotificationManager.Notification> notifications, long period) {
/* 148 */     cancel();
/* 150 */     return new NotificationTask(notifications, this.elapsed.get(), period);
/*     */   }
/*     */   
/*     */   public void run() {
/* 155 */     long currentMinute = this.elapsed.getAndAdd(this.period);
/* 156 */     long nextMinute = this.elapsed.get();
/* 158 */     for (PeriodicNotificationManager.Notification notification : this.notifications) {
/* 159 */       if (currentMinute < notification.delay)
/*     */         continue; 
/* 163 */       long elapsedPeriods = currentMinute / notification.period;
/* 164 */       long currentPeriods = nextMinute / notification.period;
/* 166 */       if (elapsedPeriods != currentPeriods) {
/* 167 */         this.minecraft.execute(() -> SystemToast.add((Minecraft.getInstance()).gui.toastManager(), SystemToast.SystemToastId.PERIODIC_NOTIFICATION, (Component)Component.translatable(notification.title, new Object[] { Long.valueOf(elapsedPeriods) }), (Component)Component.translatable(notification.message, new Object[] { Long.valueOf(elapsedPeriods) })));
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\PeriodicNotificationManager$NotificationTask.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */