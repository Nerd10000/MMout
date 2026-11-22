/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public final class BinomialDistributionGenerator extends Record implements NumberProvider {
/*    */   private final NumberProvider n;
/*    */   
/*    */   private final NumberProvider p;
/*    */   
/*    */   public static final MapCodec<BinomialDistributionGenerator> MAP_CODEC;
/*    */   
/*    */   public BinomialDistributionGenerator(NumberProvider n, NumberProvider p) {
/* 10 */     this.n = n;
/* 10 */     this.p = p;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/number/BinomialDistributionGenerator;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/BinomialDistributionGenerator;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/number/BinomialDistributionGenerator;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/BinomialDistributionGenerator;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/number/BinomialDistributionGenerator;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/BinomialDistributionGenerator;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public NumberProvider n() {
/* 10 */     return this.n;
/*    */   }
/*    */   
/*    */   public NumberProvider p() {
/* 10 */     return this.p;
/*    */   }
/*    */   
/*    */   static {
/* 14 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)NumberProviders.CODEC.fieldOf("n").forGetter(BinomialDistributionGenerator::n), (App)NumberProviders.CODEC.fieldOf("p").forGetter(BinomialDistributionGenerator::p)).apply((Applicative)i, BinomialDistributionGenerator::new));
/*    */   }
/*    */   
/*    */   public MapCodec<BinomialDistributionGenerator> codec() {
/* 21 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public int getInt(LootContext context) {
/* 28 */     int n = this.n.getInt(context);
/* 29 */     float p = this.p.getFloat(context);
/* 30 */     RandomSource random = context.getRandom();
/* 31 */     int result = 0;
/* 32 */     for (int i = 0; i < n; i++) {
/* 33 */       if (random.nextFloat() < p)
/* 34 */         result++; 
/*    */     } 
/* 38 */     return result;
/*    */   }
/*    */   
/*    */   public float getFloat(LootContext context) {
/* 43 */     return getInt(context);
/*    */   }
/*    */   
/*    */   public static BinomialDistributionGenerator binomial(int n, float p) {
/* 47 */     return new BinomialDistributionGenerator(ConstantValue.exactly(n), ConstantValue.exactly(p));
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 52 */     super.validate(context);
/* 54 */     Validatable.validate(context, "n", (Validatable)this.n);
/* 55 */     Validatable.validate(context, "p", (Validatable)this.p);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\providers\number\BinomialDistributionGenerator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */