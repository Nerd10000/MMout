/*    */ package net.minecraft.world.level.timers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.functions.CommandFunction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.ServerFunctionManager;
/*    */ 
/*    */ public final class FunctionCallback extends Record implements TimerCallback<MinecraftServer> {
/*    */   private final Identifier functionId;
/*    */   
/*    */   public static final MapCodec<FunctionCallback> CODEC;
/*    */   
/*    */   public FunctionCallback(Identifier functionId) {
/*  9 */     this.functionId = functionId;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/timers/FunctionCallback;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/timers/FunctionCallback;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/timers/FunctionCallback;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/timers/FunctionCallback;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/timers/FunctionCallback;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/timers/FunctionCallback;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier functionId() {
/*  9 */     return this.functionId;
/*    */   }
/*    */   
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("id").forGetter(FunctionCallback::functionId)).apply((Applicative)i, FunctionCallback::new));
/*    */   }
/*    */   
/*    */   public void handle(MinecraftServer server, TimerQueue<MinecraftServer> queue, long time) {
/* 16 */     ServerFunctionManager functionManager = server.getFunctions();
/* 17 */     functionManager.get(this.functionId).ifPresent(function -> functionManager.execute(function, functionManager.getGameLoopSender()));
/*    */   }
/*    */   
/*    */   public MapCodec<FunctionCallback> codec() {
/* 22 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\timers\FunctionCallback.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */