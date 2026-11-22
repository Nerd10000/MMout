/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class Wrench extends Record {
/*     */   private final Vec3 force;
/*     */   
/*     */   private final double torque;
/*     */   
/*     */   public Wrench(Vec3 force, double torque) {
/* 429 */     this.force = force;
/* 429 */     this.torque = torque;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Leashable$Wrench;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #429	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/Leashable$Wrench;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Leashable$Wrench;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #429	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/Leashable$Wrench;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Leashable$Wrench;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #429	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/entity/Leashable$Wrench;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Vec3 force() {
/* 429 */     return this.force;
/*     */   }
/*     */   
/*     */   public double torque() {
/* 429 */     return this.torque;
/*     */   }
/*     */   
/* 433 */   public static final Wrench ZERO = new Wrench(Vec3.ZERO, 0.0D);
/*     */   
/*     */   public static double torqueFromForce(Vec3 leverArm, Vec3 force) {
/* 437 */     return leverArm.z * force.x - leverArm.x * force.z;
/*     */   }
/*     */   
/*     */   public static Wrench accumulate(List<Wrench> wrenches) {
/* 441 */     if (wrenches.isEmpty())
/* 442 */       return ZERO; 
/* 444 */     double x = 0.0D;
/* 445 */     double y = 0.0D;
/* 446 */     double z = 0.0D;
/* 447 */     double t = 0.0D;
/* 448 */     for (Wrench wrench : wrenches) {
/* 449 */       Vec3 force = wrench.force;
/* 450 */       x += force.x;
/* 451 */       y += force.y;
/* 452 */       z += force.z;
/* 453 */       t += wrench.torque;
/*     */     } 
/* 455 */     return new Wrench(new Vec3(x, y, z), t);
/*     */   }
/*     */   
/*     */   public Wrench scale(double scale) {
/* 459 */     return new Wrench(this.force.scale(scale), this.torque * scale);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Leashable$Wrench.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */