/*     */ package net.minecraft.world.level.timers;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.HashBasedTable;
/*     */ import com.google.common.collect.Table;
/*     */ import com.google.common.primitives.UnsignedLong;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ 
/*     */ public class TimerQueue<T> extends SavedData {
/*     */   public static final class Event<T> extends Record {
/*     */     private final long triggerTime;
/*     */     
/*     */     private final UnsignedLong sequentialId;
/*     */     
/*     */     private final String id;
/*     */     
/*     */     private final TimerCallback<T> callback;
/*     */     
/*     */     public Event(long triggerTime, UnsignedLong sequentialId, String id, TimerCallback<T> callback) {
/*  24 */       this.triggerTime = triggerTime;
/*  24 */       this.sequentialId = sequentialId;
/*  24 */       this.id = id;
/*  24 */       this.callback = callback;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/timers/TimerQueue$Event;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #24	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/timers/TimerQueue$Event;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #24	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/timers/TimerQueue$Event;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #24	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event<TT;>;
/*     */     }
/*     */     
/*     */     public long triggerTime() {
/*  24 */       return this.triggerTime;
/*     */     }
/*     */     
/*     */     public UnsignedLong sequentialId() {
/*  24 */       return this.sequentialId;
/*     */     }
/*     */     
/*     */     public String id() {
/*  24 */       return this.id;
/*     */     }
/*     */     
/*     */     public TimerCallback<T> callback() {
/*  24 */       return this.callback;
/*     */     }
/*     */     
/*     */     public static final class Packed<T> extends Record {
/*     */       private final long triggerTime;
/*     */       
/*     */       private final String id;
/*     */       
/*     */       private final TimerCallback<T> callback;
/*     */       
/*     */       public Packed(long triggerTime, String id, TimerCallback<T> callback) {
/*  30 */         this.triggerTime = triggerTime;
/*  30 */         this.id = id;
/*  30 */         this.callback = callback;
/*     */       }
/*     */       
/*     */       public final String toString() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #30	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed;
/*     */         // Local variable type table:
/*     */         //   start	length	slot	name	signature
/*     */         //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed<TT;>;
/*     */       }
/*     */       
/*     */       public final int hashCode() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #30	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed;
/*     */         // Local variable type table:
/*     */         //   start	length	slot	name	signature
/*     */         //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed<TT;>;
/*     */       }
/*     */       
/*     */       public final boolean equals(Object o) {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #30	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */         // Local variable type table:
/*     */         //   start	length	slot	name	signature
/*     */         //   0	8	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed<TT;>;
/*     */       }
/*     */       
/*     */       public long triggerTime() {
/*  30 */         return this.triggerTime;
/*     */       }
/*     */       
/*     */       public String id() {
/*  30 */         return this.id;
/*     */       }
/*     */       
/*     */       public TimerCallback<T> callback() {
/*  30 */         return this.callback;
/*     */       }
/*     */       
/*     */       public static <T> Codec<Packed<T>> codec(Codec<TimerCallback<T>> callbackCodec) {
/*  36 */         return RecordCodecBuilder.create(i -> i.group((App)Codec.LONG.fieldOf("trigger_time").forGetter(Packed::triggerTime), (App)Codec.STRING.fieldOf("id").forGetter(Packed::id), (App)callbackCodec.fieldOf("callback").forGetter(Packed::callback)).apply((Applicative)i, Packed::new));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Packed<T> extends Record {
/*     */     private final long triggerTime;
/*     */     
/*     */     private final String id;
/*     */     
/*     */     private final TimerCallback<T> callback;
/*     */     
/*     */     public Packed(long triggerTime, String id, TimerCallback<T> callback) {
/*     */       this.triggerTime = triggerTime;
/*     */       this.id = id;
/*     */       this.callback = callback;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #30	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #30	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #30	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Event$Packed<TT;>;
/*     */     }
/*     */     
/*     */     public long triggerTime() {
/*     */       return this.triggerTime;
/*     */     }
/*     */     
/*     */     public String id() {
/*     */       return this.id;
/*     */     }
/*     */     
/*     */     public TimerCallback<T> callback() {
/*     */       return this.callback;
/*     */     }
/*     */     
/*     */     public static <T> Codec<Packed<T>> codec(Codec<TimerCallback<T>> callbackCodec) {
/*  36 */       return RecordCodecBuilder.create(i -> i.group((App)Codec.LONG.fieldOf("trigger_time").forGetter(Packed::triggerTime), (App)Codec.STRING.fieldOf("id").forGetter(Packed::id), (App)callbackCodec.fieldOf("callback").forGetter(Packed::callback)).apply((Applicative)i, Packed::new));
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Packed<T> extends Record {
/*     */     private final List<TimerQueue.Event.Packed<T>> events;
/*     */     
/*     */     public Packed(List<TimerQueue.Event.Packed<T>> events) {
/*  45 */       this.events = events;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/timers/TimerQueue$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Packed;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Packed<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/timers/TimerQueue$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Packed;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Packed<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/timers/TimerQueue$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Packed;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/timers/TimerQueue$Packed<TT;>;
/*     */     }
/*     */     
/*     */     public List<TimerQueue.Event.Packed<T>> events() {
/*  45 */       return this.events;
/*     */     }
/*     */     
/*     */     public static <T> Codec<Packed<T>> codec(Codec<TimerCallback<T>> callbackCodec) {
/*  49 */       return RecordCodecBuilder.create(i -> i.group((App)TimerQueue.Event.Packed.<T>codec(callbackCodec).listOf().fieldOf("events").forGetter(Packed::events)).apply((Applicative)i, Packed::new));
/*     */     }
/*     */   }
/*     */   
/*  55 */   public static final Codec<TimerQueue<MinecraftServer>> CODEC = codec(TimerCallbacks.SERVER_CALLBACKS);
/*     */   
/*  57 */   public static final SavedDataType<TimerQueue<MinecraftServer>> TYPE = new SavedDataType(
/*  58 */       Identifier.withDefaultNamespace("scheduled_events"), TimerQueue::new, CODEC, DataFixTypes.SAVED_DATA_SCHEDULED_EVENTS);
/*     */   
/*     */   @VisibleForTesting
/*     */   protected static <T> Codec<TimerQueue<T>> codec(TimerCallbacks<T> callbacks) {
/*  66 */     return Packed.<T>codec(callbacks.codec()).xmap(TimerQueue::new, TimerQueue::pack);
/*     */   }
/*     */   
/*     */   private static <T> Comparator<Event<T>> createComparator() {
/*  70 */     return Comparator.comparingLong(l -> l.triggerTime).thenComparing(l -> l.sequentialId);
/*     */   }
/*     */   
/*  73 */   private final Queue<Event<T>> queue = new PriorityQueue<>((Comparator)createComparator());
/*     */   
/*  75 */   private UnsignedLong sequentialId = UnsignedLong.ZERO;
/*     */   
/*  77 */   private final Table<String, Long, Event<T>> events = (Table<String, Long, Event<T>>)HashBasedTable.create();
/*     */   
/*     */   public TimerQueue(Packed<T> packedEvents) {
/*  80 */     this();
/*  81 */     this.queue.clear();
/*  82 */     this.events.clear();
/*  83 */     this.sequentialId = UnsignedLong.ZERO;
/*  85 */     packedEvents.events.forEach(event -> schedule(event.id, event.triggerTime, event.callback));
/*     */   }
/*     */   
/*     */   public void tick(T context, long currentTick) {
/*     */     while (true) {
/*  93 */       Event<T> event = this.queue.peek();
/*  94 */       if (event == null || event.triggerTime > currentTick)
/*     */         break; 
/*  98 */       this.queue.remove();
/*  99 */       this.events.remove(event.id, Long.valueOf(currentTick));
/* 100 */       setDirty();
/* 102 */       event.callback.handle(context, this, currentTick);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void schedule(String id, long time, TimerCallback<T> callback) {
/* 107 */     if (this.events.contains(id, Long.valueOf(time)))
/*     */       return; 
/* 110 */     this.sequentialId = this.sequentialId.plus(UnsignedLong.ONE);
/* 111 */     Event<T> newEvent = new Event<>(time, this.sequentialId, id, callback);
/* 112 */     this.events.put(id, Long.valueOf(time), newEvent);
/* 113 */     this.queue.add(newEvent);
/* 114 */     setDirty();
/*     */   }
/*     */   
/*     */   public int remove(String id) {
/* 118 */     Collection<Event<T>> eventsToRemove = this.events.row(id).values();
/* 119 */     Objects.requireNonNull(this.queue);
/* 119 */     eventsToRemove.forEach(this.queue::remove);
/* 120 */     int size = eventsToRemove.size();
/* 121 */     eventsToRemove.clear();
/* 122 */     setDirty();
/* 123 */     return size;
/*     */   }
/*     */   
/*     */   public Set<String> getEventsIds() {
/* 127 */     return Collections.unmodifiableSet(this.events.rowKeySet());
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   protected Packed<T> pack() {
/* 132 */     return new Packed<>(this.queue.stream()
/* 133 */         .sorted(createComparator())
/* 134 */         .map(event -> new Event.Packed(event.triggerTime, event.id, event.callback))
/* 135 */         .toList());
/*     */   }
/*     */   
/*     */   public TimerQueue() {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\timers\TimerQueue.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */