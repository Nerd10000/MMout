/*   */ package net.minecraft.world.entity.animal.golem;
/*   */ 
/*   */ import net.minecraft.resources.Identifier;
/*   */ import net.minecraft.sounds.SoundEvent;
/*   */ 
/*   */ public final class CopperGolemOxidationLevel extends Record {
/*   */   private final SoundEvent spinHeadSound;
/*   */   
/*   */   private final SoundEvent hurtSound;
/*   */   
/*   */   private final SoundEvent deathSound;
/*   */   
/*   */   private final SoundEvent stepSound;
/*   */   
/*   */   private final Identifier texture;
/*   */   
/*   */   private final Identifier eyeTexture;
/*   */   
/*   */   public CopperGolemOxidationLevel(SoundEvent spinHeadSound, SoundEvent hurtSound, SoundEvent deathSound, SoundEvent stepSound, Identifier texture, Identifier eyeTexture) {
/* 6 */     this.spinHeadSound = spinHeadSound;
/* 6 */     this.hurtSound = hurtSound;
/* 6 */     this.deathSound = deathSound;
/* 6 */     this.stepSound = stepSound;
/* 6 */     this.texture = texture;
/* 6 */     this.eyeTexture = eyeTexture;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/golem/CopperGolemOxidationLevel;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/golem/CopperGolemOxidationLevel;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/golem/CopperGolemOxidationLevel;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/golem/CopperGolemOxidationLevel;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/golem/CopperGolemOxidationLevel;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/golem/CopperGolemOxidationLevel;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public SoundEvent spinHeadSound() {
/* 6 */     return this.spinHeadSound;
/*   */   }
/*   */   
/*   */   public SoundEvent hurtSound() {
/* 6 */     return this.hurtSound;
/*   */   }
/*   */   
/*   */   public SoundEvent deathSound() {
/* 6 */     return this.deathSound;
/*   */   }
/*   */   
/*   */   public SoundEvent stepSound() {
/* 6 */     return this.stepSound;
/*   */   }
/*   */   
/*   */   public Identifier texture() {
/* 6 */     return this.texture;
/*   */   }
/*   */   
/*   */   public Identifier eyeTexture() {
/* 6 */     return this.eyeTexture;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\golem\CopperGolemOxidationLevel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */