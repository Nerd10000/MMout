/*    */ package net.minecraft.util.debug;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class Update<T> extends Record {
/*    */   private final DebugSubscription<T> subscription;
/*    */   
/*    */   private final Optional<T> value;
/*    */   
/*    */   public Update(DebugSubscription<T> subscription, Optional<T> value) {
/* 55 */     this.subscription = subscription;
/* 55 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugSubscription$Update;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #55	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugSubscription$Update;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugSubscription$Update<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugSubscription$Update;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #55	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugSubscription$Update;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugSubscription$Update<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugSubscription$Update;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #55	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/debug/DebugSubscription$Update;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/util/debug/DebugSubscription$Update<TT;>;
/*    */   }
/*    */   
/*    */   public DebugSubscription<T> subscription() {
/* 55 */     return this.subscription;
/*    */   }
/*    */   
/*    */   public Optional<T> value() {
/* 55 */     return this.value;
/*    */   }
/*    */   
/* 59 */   public static final StreamCodec<RegistryFriendlyByteBuf, Update<?>> STREAM_CODEC = ByteBufCodecs.registry(Registries.DEBUG_SUBSCRIPTION)
/* 60 */     .dispatch(Update::subscription, Update::streamCodec);
/*    */   
/*    */   private static <T> StreamCodec<? super RegistryFriendlyByteBuf, Update<T>> streamCodec(DebugSubscription<T> subscription) {
/* 63 */     return ByteBufCodecs.optional(Objects.<StreamCodec>requireNonNull(subscription.valueStreamCodec))
/* 64 */       .map(value -> new Update(subscription, value), Update::value);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\debug\DebugSubscription$Update.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */