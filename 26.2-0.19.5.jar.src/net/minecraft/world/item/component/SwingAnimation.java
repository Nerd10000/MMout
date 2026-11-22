/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.SwingAnimationType;
/*    */ 
/*    */ public final class SwingAnimation extends Record {
/*    */   private final SwingAnimationType type;
/*    */   
/*    */   private final int duration;
/*    */   
/*    */   public SwingAnimation(SwingAnimationType type, int duration) {
/* 11 */     this.type = type;
/* 11 */     this.duration = duration;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/SwingAnimation;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/SwingAnimation;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/SwingAnimation;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/SwingAnimation;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/SwingAnimation;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/SwingAnimation;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public SwingAnimationType type() {
/* 11 */     return this.type;
/*    */   }
/*    */   
/*    */   public int duration() {
/* 11 */     return this.duration;
/*    */   }
/*    */   
/* 12 */   public static final SwingAnimation DEFAULT = new SwingAnimation(SwingAnimationType.WHACK, 6);
/*    */   
/*    */   public static final Codec<SwingAnimation> CODEC;
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)SwingAnimationType.CODEC.optionalFieldOf("type", DEFAULT.type).forGetter(SwingAnimation::type), (App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("duration", Integer.valueOf(DEFAULT.duration)).forGetter(SwingAnimation::duration)).apply((Applicative)i, SwingAnimation::new));
/*    */   }
/*    */   
/* 17 */   public static final StreamCodec<ByteBuf, SwingAnimation> STREAM_CODEC = StreamCodec.composite(SwingAnimationType.STREAM_CODEC, SwingAnimation::type, ByteBufCodecs.VAR_INT, SwingAnimation::duration, SwingAnimation::new);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\SwingAnimation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */