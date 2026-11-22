/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class ExplosionData extends Record {
/*    */   private final int power;
/*    */   
/*    */   private final boolean causesFire;
/*    */   
/*    */   private final int fuse;
/*    */   
/*    */   public static final Codec<ExplosionData> CODEC;
/*    */   
/*    */   public ExplosionData(int power, boolean causesFire, int fuse) {
/* 58 */     this.power = power;
/* 58 */     this.causesFire = causesFire;
/* 58 */     this.fuse = fuse;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/SulfurCubeArchetype$ExplosionData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #58	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$ExplosionData;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/SulfurCubeArchetype$ExplosionData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #58	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$ExplosionData;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/SulfurCubeArchetype$ExplosionData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #58	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$ExplosionData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int power() {
/* 58 */     return this.power;
/*    */   }
/*    */   
/*    */   public boolean causesFire() {
/* 58 */     return this.causesFire;
/*    */   }
/*    */   
/*    */   public int fuse() {
/* 58 */     return this.fuse;
/*    */   }
/*    */   
/*    */   static {
/* 59 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("power").forGetter(ExplosionData::power), (App)Codec.BOOL.fieldOf("causes_fire").forGetter(ExplosionData::causesFire), (App)ExtraCodecs.POSITIVE_INT.fieldOf("fuse").forGetter(ExplosionData::fuse)).apply((Applicative)i, ExplosionData::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\SulfurCubeArchetype$ExplosionData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */