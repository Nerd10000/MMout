/*     */ package net.minecraft.client.gui.components.toasts;
/*     */ 
/*     */ public class SystemToastId {
/* 126 */   public static final SystemToastId NARRATOR_TOGGLE = new SystemToastId();
/*     */   
/* 127 */   public static final SystemToastId WORLD_BACKUP = new SystemToastId();
/*     */   
/* 128 */   public static final SystemToastId PACK_LOAD_FAILURE = new SystemToastId();
/*     */   
/* 129 */   public static final SystemToastId WORLD_ACCESS_FAILURE = new SystemToastId();
/*     */   
/* 130 */   public static final SystemToastId PACK_COPY_FAILURE = new SystemToastId();
/*     */   
/* 131 */   public static final SystemToastId FILE_DROP_FAILURE = new SystemToastId();
/*     */   
/* 132 */   public static final SystemToastId PERIODIC_NOTIFICATION = new SystemToastId();
/*     */   
/* 133 */   public static final SystemToastId LOW_DISK_SPACE = new SystemToastId(10000L);
/*     */   
/* 134 */   public static final SystemToastId CHUNK_LOAD_FAILURE = new SystemToastId();
/*     */   
/* 135 */   public static final SystemToastId CHUNK_SAVE_FAILURE = new SystemToastId();
/*     */   
/* 136 */   public static final SystemToastId UNSECURE_SERVER_WARNING = new SystemToastId(10000L);
/*     */   
/* 137 */   public static final SystemToastId FRIEND_SYSTEM_NOTIFICATION = new SystemToastId();
/*     */   
/*     */   private final long displayTime;
/*     */   
/*     */   public SystemToastId(long displayTime) {
/* 142 */     this.displayTime = displayTime;
/*     */   }
/*     */   
/*     */   public SystemToastId() {
/* 146 */     this(5000L);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\toasts\SystemToast$SystemToastId.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */