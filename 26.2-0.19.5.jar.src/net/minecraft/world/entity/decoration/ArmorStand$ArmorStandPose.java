/*     */ package net.minecraft.world.entity.decoration;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.Rotations;
/*     */ 
/*     */ public final class ArmorStandPose extends Record {
/*     */   private final Rotations head;
/*     */   
/*     */   private final Rotations body;
/*     */   
/*     */   private final Rotations leftArm;
/*     */   
/*     */   private final Rotations rightArm;
/*     */   
/*     */   private final Rotations leftLeg;
/*     */   
/*     */   private final Rotations rightLeg;
/*     */   
/*     */   public ArmorStandPose(Rotations head, Rotations body, Rotations leftArm, Rotations rightArm, Rotations leftLeg, Rotations rightLeg) {
/* 704 */     this.head = head;
/* 704 */     this.body = body;
/* 704 */     this.leftArm = leftArm;
/* 704 */     this.rightArm = rightArm;
/* 704 */     this.leftLeg = leftLeg;
/* 704 */     this.rightLeg = rightLeg;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/decoration/ArmorStand$ArmorStandPose;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #704	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/decoration/ArmorStand$ArmorStandPose;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/decoration/ArmorStand$ArmorStandPose;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #704	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/decoration/ArmorStand$ArmorStandPose;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/decoration/ArmorStand$ArmorStandPose;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #704	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/entity/decoration/ArmorStand$ArmorStandPose;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Rotations head() {
/* 704 */     return this.head;
/*     */   }
/*     */   
/*     */   public Rotations body() {
/* 704 */     return this.body;
/*     */   }
/*     */   
/*     */   public Rotations leftArm() {
/* 704 */     return this.leftArm;
/*     */   }
/*     */   
/*     */   public Rotations rightArm() {
/* 704 */     return this.rightArm;
/*     */   }
/*     */   
/*     */   public Rotations leftLeg() {
/* 704 */     return this.leftLeg;
/*     */   }
/*     */   
/*     */   public Rotations rightLeg() {
/* 704 */     return this.rightLeg;
/*     */   }
/*     */   
/* 712 */   public static final ArmorStandPose DEFAULT = new ArmorStandPose(ArmorStand.DEFAULT_HEAD_POSE, ArmorStand.DEFAULT_BODY_POSE, ArmorStand.DEFAULT_LEFT_ARM_POSE, ArmorStand.DEFAULT_RIGHT_ARM_POSE, ArmorStand.DEFAULT_LEFT_LEG_POSE, ArmorStand.DEFAULT_RIGHT_LEG_POSE);
/*     */   
/*     */   public static final Codec<ArmorStandPose> CODEC;
/*     */   
/*     */   static {
/* 721 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Rotations.CODEC.optionalFieldOf("Head", ArmorStand.DEFAULT_HEAD_POSE).forGetter(ArmorStandPose::head), (App)Rotations.CODEC.optionalFieldOf("Body", ArmorStand.DEFAULT_BODY_POSE).forGetter(ArmorStandPose::body), (App)Rotations.CODEC.optionalFieldOf("LeftArm", ArmorStand.DEFAULT_LEFT_ARM_POSE).forGetter(ArmorStandPose::leftArm), (App)Rotations.CODEC.optionalFieldOf("RightArm", ArmorStand.DEFAULT_RIGHT_ARM_POSE).forGetter(ArmorStandPose::rightArm), (App)Rotations.CODEC.optionalFieldOf("LeftLeg", ArmorStand.DEFAULT_LEFT_LEG_POSE).forGetter(ArmorStandPose::leftLeg), (App)Rotations.CODEC.optionalFieldOf("RightLeg", ArmorStand.DEFAULT_RIGHT_LEG_POSE).forGetter(ArmorStandPose::rightLeg)).apply((Applicative)i, ArmorStandPose::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\decoration\ArmorStand$ArmorStandPose.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */