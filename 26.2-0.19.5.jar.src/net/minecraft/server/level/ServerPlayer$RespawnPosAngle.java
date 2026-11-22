/*      */ package net.minecraft.server.level;
/*      */ 
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ final class RespawnPosAngle extends Record {
/*      */   private final Vec3 position;
/*      */   
/*      */   private final float yaw;
/*      */   
/*      */   private final float pitch;
/*      */   
/*      */   private RespawnPosAngle(Vec3 position, float yaw, float pitch) {
/* 1113 */     this.position = position;
/* 1113 */     this.yaw = yaw;
/* 1113 */     this.pitch = pitch;
/*      */   }
/*      */   
/*      */   public final String toString() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ServerPlayer$RespawnPosAngle;)Ljava/lang/String;
/*      */     //   6: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1113	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/server/level/ServerPlayer$RespawnPosAngle;
/*      */   }
/*      */   
/*      */   public final int hashCode() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ServerPlayer$RespawnPosAngle;)I
/*      */     //   6: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1113	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/server/level/ServerPlayer$RespawnPosAngle;
/*      */   }
/*      */   
/*      */   public final boolean equals(Object o) {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_1
/*      */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ServerPlayer$RespawnPosAngle;Ljava/lang/Object;)Z
/*      */     //   7: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1113	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	8	0	this	Lnet/minecraft/server/level/ServerPlayer$RespawnPosAngle;
/*      */     //   0	8	1	o	Ljava/lang/Object;
/*      */   }
/*      */   
/*      */   public Vec3 position() {
/* 1113 */     return this.position;
/*      */   }
/*      */   
/*      */   public float yaw() {
/* 1113 */     return this.yaw;
/*      */   }
/*      */   
/*      */   public float pitch() {
/* 1113 */     return this.pitch;
/*      */   }
/*      */   
/*      */   public static RespawnPosAngle of(Vec3 position, BlockPos lookAtBlockPos, float pitch) {
/* 1115 */     return new RespawnPosAngle(position, calculateLookAtYaw(position, lookAtBlockPos), pitch);
/*      */   }
/*      */   
/*      */   private static float calculateLookAtYaw(Vec3 position, BlockPos lookAtBlockPos) {
/* 1119 */     Vec3 lookDirection = Vec3.atBottomCenterOf((Vec3i)lookAtBlockPos).subtract(position).normalize();
/* 1120 */     return (float)Mth.wrapDegrees(Mth.atan2(lookDirection.z, lookDirection.x) * 57.2957763671875D - 90.0D);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ServerPlayer$RespawnPosAngle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */