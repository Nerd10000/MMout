/*     */ package net.minecraft.server.chase;
/*     */ 
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ final class TeleportTarget extends Record {
/*     */   private final ResourceKey<Level> level;
/*     */   
/*     */   private final Vec3 pos;
/*     */   
/*     */   private final Vec2 rot;
/*     */   
/*     */   private TeleportTarget(ResourceKey<Level> level, Vec3 pos, Vec2 rot) {
/* 128 */     this.level = level;
/* 128 */     this.pos = pos;
/* 128 */     this.rot = rot;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/chase/ChaseClient$TeleportTarget;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #128	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/chase/ChaseClient$TeleportTarget;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/chase/ChaseClient$TeleportTarget;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #128	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/chase/ChaseClient$TeleportTarget;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/chase/ChaseClient$TeleportTarget;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #128	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/chase/ChaseClient$TeleportTarget;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ResourceKey<Level> level() {
/* 128 */     return this.level;
/*     */   }
/*     */   
/*     */   public Vec3 pos() {
/* 128 */     return this.pos;
/*     */   }
/*     */   
/*     */   public Vec2 rot() {
/* 128 */     return this.rot;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\chase\ChaseClient$TeleportTarget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */