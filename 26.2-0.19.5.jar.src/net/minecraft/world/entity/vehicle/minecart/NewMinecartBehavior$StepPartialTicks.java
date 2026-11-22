/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ 
/*     */ final class StepPartialTicks extends Record {
/*     */   private final float partialTicksInStep;
/*     */   
/*     */   private final NewMinecartBehavior.MinecartStep currentStep;
/*     */   
/*     */   private final NewMinecartBehavior.MinecartStep previousStep;
/*     */   
/*     */   private StepPartialTicks(float partialTicksInStep, NewMinecartBehavior.MinecartStep currentStep, NewMinecartBehavior.MinecartStep previousStep) {
/* 159 */     this.partialTicksInStep = partialTicksInStep;
/* 159 */     this.currentStep = currentStep;
/* 159 */     this.previousStep = previousStep;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$StepPartialTicks;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #159	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$StepPartialTicks;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$StepPartialTicks;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #159	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$StepPartialTicks;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$StepPartialTicks;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #159	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$StepPartialTicks;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public float partialTicksInStep() {
/* 159 */     return this.partialTicksInStep;
/*     */   }
/*     */   
/*     */   public NewMinecartBehavior.MinecartStep currentStep() {
/* 159 */     return this.currentStep;
/*     */   }
/*     */   
/*     */   public NewMinecartBehavior.MinecartStep previousStep() {
/* 159 */     return this.previousStep;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\vehicle\minecart\NewMinecartBehavior$StepPartialTicks.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */