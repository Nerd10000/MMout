/*    */ package net.minecraft.client.multiplayer;
/*    */ 
/*    */ import net.minecraft.core.particles.ExplosionParticleInfo;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ final class ExplosionInfo extends Record {
/*    */   private final Vec3 center;
/*    */   
/*    */   private final float radius;
/*    */   
/*    */   private final int blockCount;
/*    */   
/*    */   private final WeightedList<ExplosionParticleInfo> blockParticles;
/*    */   
/*    */   private ExplosionInfo(Vec3 center, float radius, int blockCount, WeightedList<ExplosionParticleInfo> blockParticles) {
/* 18 */     this.center = center;
/* 18 */     this.radius = radius;
/* 18 */     this.blockCount = blockCount;
/* 18 */     this.blockParticles = blockParticles;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/ClientExplosionTracker$ExplosionInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientExplosionTracker$ExplosionInfo;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/ClientExplosionTracker$ExplosionInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientExplosionTracker$ExplosionInfo;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/ClientExplosionTracker$ExplosionInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/ClientExplosionTracker$ExplosionInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vec3 center() {
/* 18 */     return this.center;
/*    */   }
/*    */   
/*    */   public float radius() {
/* 18 */     return this.radius;
/*    */   }
/*    */   
/*    */   public int blockCount() {
/* 18 */     return this.blockCount;
/*    */   }
/*    */   
/*    */   public WeightedList<ExplosionParticleInfo> blockParticles() {
/* 18 */     return this.blockParticles;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientExplosionTracker$ExplosionInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */