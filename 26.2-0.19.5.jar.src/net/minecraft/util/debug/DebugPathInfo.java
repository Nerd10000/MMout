/*    */ package net.minecraft.util.debug;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ 
/*    */ public final class DebugPathInfo extends Record {
/*    */   private final Path path;
/*    */   
/*    */   private final float maxNodeDistance;
/*    */   
/*    */   public DebugPathInfo(Path path, float maxNodeDistance) {
/*  8 */     this.path = path;
/*  8 */     this.maxNodeDistance = maxNodeDistance;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugPathInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugPathInfo;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugPathInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugPathInfo;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugPathInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/debug/DebugPathInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Path path() {
/*  8 */     return this.path;
/*    */   }
/*    */   
/*    */   public float maxNodeDistance() {
/*  8 */     return this.maxNodeDistance;
/*    */   }
/*    */   
/* 12 */   public static final StreamCodec<FriendlyByteBuf, DebugPathInfo> STREAM_CODEC = StreamCodec.composite(Path.STREAM_CODEC, DebugPathInfo::path, ByteBufCodecs.FLOAT, DebugPathInfo::maxNodeDistance, DebugPathInfo::new);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\debug\DebugPathInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */