/*    */ package net.minecraft.util.debug;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class Event<T> extends Record {
/*    */   private final DebugSubscription<T> subscription;
/*    */   
/*    */   private final T value;
/*    */   
/*    */   public Event(DebugSubscription<T> subscription, T value) {
/* 68 */     this.subscription = subscription;
/* 68 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugSubscription$Event;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #68	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugSubscription$Event;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugSubscription$Event<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugSubscription$Event;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #68	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugSubscription$Event;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugSubscription$Event<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugSubscription$Event;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #68	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/debug/DebugSubscription$Event;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/util/debug/DebugSubscription$Event<TT;>;
/*    */   }
/*    */   
/*    */   public DebugSubscription<T> subscription() {
/* 68 */     return this.subscription;
/*    */   }
/*    */   
/*    */   public T value() {
/* 68 */     return this.value;
/*    */   }
/*    */   
/* 72 */   public static final StreamCodec<RegistryFriendlyByteBuf, Event<?>> STREAM_CODEC = ByteBufCodecs.registry(Registries.DEBUG_SUBSCRIPTION)
/* 73 */     .dispatch(Event::subscription, Event::streamCodec);
/*    */   
/*    */   private static <T> StreamCodec<? super RegistryFriendlyByteBuf, Event<T>> streamCodec(DebugSubscription<T> subscription) {
/* 76 */     return ((StreamCodec)Objects.<StreamCodec>requireNonNull(subscription.valueStreamCodec)).map(value -> new Event(subscription, value), Event::value);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\debug\DebugSubscription$Event.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */