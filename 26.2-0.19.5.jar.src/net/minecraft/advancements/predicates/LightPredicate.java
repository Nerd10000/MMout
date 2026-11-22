/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ 
/*    */ public final class LightPredicate extends Record {
/*    */   private final MinMaxBounds.Ints composite;
/*    */   
/*    */   public static final Codec<LightPredicate> CODEC;
/*    */   
/*    */   public LightPredicate(MinMaxBounds.Ints composite) {
/*  8 */     this.composite = composite;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/LightPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/LightPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/LightPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/LightPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/LightPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/LightPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Ints composite() {
/*  8 */     return this.composite;
/*    */   }
/*    */   
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)MinMaxBounds.Ints.CODEC.optionalFieldOf("light", MinMaxBounds.Ints.ANY).forGetter(LightPredicate::composite)).apply((Applicative)i, LightPredicate::new));
/*    */   }
/*    */   
/*    */   public boolean matches(ServerLevel level, BlockPos pos) {
/* 14 */     if (!level.isLoaded(pos))
/* 15 */       return false; 
/* 17 */     if (!this.composite.matches(level.getMaxLocalRawBrightness(pos)))
/* 18 */       return false; 
/* 20 */     return true;
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 24 */     private MinMaxBounds.Ints composite = MinMaxBounds.Ints.ANY;
/*    */     
/*    */     public static Builder light() {
/* 27 */       return new Builder();
/*    */     }
/*    */     
/*    */     public Builder setComposite(MinMaxBounds.Ints composite) {
/* 31 */       this.composite = composite;
/* 32 */       return this;
/*    */     }
/*    */     
/*    */     public LightPredicate build() {
/* 36 */       return new LightPredicate(this.composite);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\LightPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */