/*    */ package net.minecraft.world.level.timers;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class TimerCallbacks<C> {
/* 13 */   public static final TimerCallbacks<MinecraftServer> SERVER_CALLBACKS = (new TimerCallbacks())
/* 14 */     .register(Identifier.withDefaultNamespace("function"), (MapCodec)FunctionCallback.CODEC)
/* 15 */     .register(Identifier.withDefaultNamespace("function_tag"), (MapCodec)FunctionTagCallback.CODEC);
/*    */   
/* 17 */   private final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends TimerCallback<C>>> idMapper = new ExtraCodecs.LateBoundIdMapper();
/*    */   
/*    */   private final Codec<TimerCallback<C>> codec;
/*    */   
/*    */   @VisibleForTesting
/*    */   public TimerCallbacks() {
/* 23 */     this.codec = this.idMapper.codec(Identifier.CODEC).dispatch("type", TimerCallback::codec, Function.identity());
/*    */   }
/*    */   
/*    */   public TimerCallbacks<C> register(Identifier id, MapCodec<? extends TimerCallback<C>> codec) {
/* 27 */     this.idMapper.put(id, codec);
/* 28 */     return this;
/*    */   }
/*    */   
/*    */   public Codec<TimerCallback<C>> codec() {
/* 32 */     return this.codec;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\timers\TimerCallbacks.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */