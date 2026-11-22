/*    */ package net.minecraft.world.entity.ai.memory;
/*    */ 
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ 
/*    */ public class MemorySlot<T> {
/*    */   private static final long NEVER_EXPIRE = 9223372036854775807L;
/*    */   
/*    */   private T value;
/*    */   
/*    */   private long timeToLive;
/*    */   
/*    */   private MemorySlot(T value, long timeToLive) {
/* 14 */     this.value = value;
/* 15 */     this.timeToLive = timeToLive;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 19 */     if (hasValue() && canExpire())
/* 20 */       if (hasExpired()) {
/* 21 */         clear();
/*    */       } else {
/* 23 */         this.timeToLive--;
/*    */       }  
/*    */   }
/*    */   
/*    */   public static <T> MemorySlot<T> create() {
/* 29 */     return new MemorySlot<>(null, Long.MAX_VALUE);
/*    */   }
/*    */   
/*    */   public void set(T value, long timeToLive) {
/* 33 */     this.value = value;
/* 34 */     this.timeToLive = timeToLive;
/*    */   }
/*    */   
/*    */   public void set(T value) {
/* 38 */     set(value, Long.MAX_VALUE);
/*    */   }
/*    */   
/*    */   public void clear() {
/* 42 */     this.value = null;
/* 43 */     this.timeToLive = Long.MAX_VALUE;
/*    */   }
/*    */   
/*    */   public boolean hasValue() {
/* 47 */     return (this.value != null);
/*    */   }
/*    */   
/*    */   public T value() {
/* 51 */     return this.value;
/*    */   }
/*    */   
/*    */   public boolean canExpire() {
/* 55 */     return (this.timeToLive != Long.MAX_VALUE);
/*    */   }
/*    */   
/*    */   public boolean hasExpired() {
/* 59 */     return (this.timeToLive <= 0L);
/*    */   }
/*    */   
/*    */   public long timeToLive() {
/* 63 */     return this.timeToLive;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 68 */     if (this.value == null)
/* 69 */       return "<empty>"; 
/* 71 */     return String.valueOf(this.value) + String.valueOf(this.value);
/*    */   }
/*    */   
/*    */   public void visit(MemoryModuleType<T> type, Brain.Visitor visitor) {
/* 75 */     if (this.value != null) {
/* 76 */       if (canExpire()) {
/* 77 */         visitor.accept(type, this.value, this.timeToLive);
/*    */       } else {
/* 79 */         visitor.accept(type, this.value);
/*    */       } 
/*    */     } else {
/* 82 */       visitor.acceptEmpty(type);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\memory\MemorySlot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */