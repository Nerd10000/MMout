/*    */ package net.minecraft.world.level.timers;
/*    */ 
/*    */ import com.google.common.primitives.UnsignedLong;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class Event<T> extends Record {
/*    */   private final long triggerTime;
/*    */   
/*    */   private final UnsignedLong sequentialId;
/*    */   
/*    */   private final String id;
/*    */   
/*    */   private final TimerCallback<T> callback;
/*    */   
/*    */   public Event(long triggerTime, UnsignedLong sequentialId, String id, TimerCallback<T> callback) {
/* 24 */     this.triggerTime = triggerTime;
/* 24 */     this.sequentialId = sequentialId;
/* 24 */     this.id = id;
/* 24 */     this.callback = callback;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/timers/TimerQueue$Event;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/timers/TimerQueue$Event;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/timers/TimerQueue$Event;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event<TT;>;
/*    */   }
/*    */   
/*    */   public long triggerTime() {
/* 24 */     return this.triggerTime;
/*    */   }
/*    */   
/*    */   public UnsignedLong sequentialId() {
/* 24 */     return this.sequentialId;
/*    */   }
/*    */   
/*    */   public String id() {
/* 24 */     return this.id;
/*    */   }
/*    */   
/*    */   public TimerCallback<T> callback() {
/* 24 */     return this.callback;
/*    */   }
/*    */   
/*    */   public static final class Packed<T> extends Record {
/*    */     private final long triggerTime;
/*    */     
/*    */     private final String id;
/*    */     
/*    */     private final TimerCallback<T> callback;
/*    */     
/*    */     public Packed(long triggerTime, String id, TimerCallback<T> callback) {
/* 30 */       this.triggerTime = triggerTime;
/* 30 */       this.id = id;
/* 30 */       this.callback = callback;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed<TT;>;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed<TT;>;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed<TT;>;
/*    */     }
/*    */     
/*    */     public long triggerTime() {
/* 30 */       return this.triggerTime;
/*    */     }
/*    */     
/*    */     public String id() {
/* 30 */       return this.id;
/*    */     }
/*    */     
/*    */     public TimerCallback<T> callback() {
/* 30 */       return this.callback;
/*    */     }
/*    */     
/*    */     public static <T> Codec<Packed<T>> codec(Codec<TimerCallback<T>> callbackCodec) {
/* 36 */       return RecordCodecBuilder.create(i -> i.group((App)Codec.LONG.fieldOf("trigger_time").forGetter(Packed::triggerTime), (App)Codec.STRING.fieldOf("id").forGetter(Packed::id), (App)callbackCodec.fieldOf("callback").forGetter(Packed::callback)).apply((Applicative)i, Packed::new));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\timers\TimerQueue$Event.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */