/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ 
/*    */ public final class KnockbackModifiers extends Record {
/*    */   private final float horizontalPower;
/*    */   
/*    */   private final float verticalPower;
/*    */   
/*    */   public static final Codec<KnockbackModifiers> CODEC;
/*    */   
/*    */   public KnockbackModifiers(float horizontalPower, float verticalPower) {
/* 74 */     this.horizontalPower = horizontalPower;
/* 74 */     this.verticalPower = verticalPower;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/SulfurCubeArchetype$KnockbackModifiers;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #74	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$KnockbackModifiers;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/SulfurCubeArchetype$KnockbackModifiers;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #74	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$KnockbackModifiers;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/SulfurCubeArchetype$KnockbackModifiers;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #74	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$KnockbackModifiers;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float horizontalPower() {
/* 74 */     return this.horizontalPower;
/*    */   }
/*    */   
/*    */   public float verticalPower() {
/* 74 */     return this.verticalPower;
/*    */   }
/*    */   
/*    */   static {
/* 75 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.FLOAT.fieldOf("horizontal_power").forGetter(KnockbackModifiers::horizontalPower), (App)Codec.FLOAT.fieldOf("vertical_power").forGetter(KnockbackModifiers::verticalPower)).apply((Applicative)i, KnockbackModifiers::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\SulfurCubeArchetype$KnockbackModifiers.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */