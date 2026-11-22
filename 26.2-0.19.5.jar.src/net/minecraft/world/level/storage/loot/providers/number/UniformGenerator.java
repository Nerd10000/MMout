/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public final class UniformGenerator extends Record implements NumberProvider {
/*    */   private final NumberProvider min;
/*    */   
/*    */   private final NumberProvider max;
/*    */   
/*    */   public static final MapCodec<UniformGenerator> MAP_CODEC;
/*    */   
/*    */   public UniformGenerator(NumberProvider min, NumberProvider max) {
/* 10 */     this.min = min;
/* 10 */     this.max = max;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/number/UniformGenerator;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/UniformGenerator;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/number/UniformGenerator;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/UniformGenerator;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/number/UniformGenerator;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/UniformGenerator;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public NumberProvider min() {
/* 10 */     return this.min;
/*    */   }
/*    */   
/*    */   public NumberProvider max() {
/* 10 */     return this.max;
/*    */   }
/*    */   
/*    */   static {
/* 14 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)NumberProviders.CODEC.fieldOf("min").forGetter(UniformGenerator::min), (App)NumberProviders.CODEC.fieldOf("max").forGetter(UniformGenerator::max)).apply((Applicative)i, UniformGenerator::new));
/*    */   }
/*    */   
/*    */   public MapCodec<UniformGenerator> codec() {
/* 21 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public static UniformGenerator between(float min, float max) {
/* 25 */     return new UniformGenerator(ConstantValue.exactly(min), ConstantValue.exactly(max));
/*    */   }
/*    */   
/*    */   public int getInt(LootContext context) {
/* 30 */     return Mth.nextInt(context.getRandom(), this.min.getInt(context), this.max.getInt(context));
/*    */   }
/*    */   
/*    */   public float getFloat(LootContext context) {
/* 35 */     return Mth.nextFloat(context.getRandom(), this.min.getFloat(context), this.max.getFloat(context));
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 40 */     super.validate(context);
/* 42 */     Validatable.validate(context, "min", (Validatable)this.min);
/* 43 */     Validatable.validate(context, "max", (Validatable)this.max);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\providers\number\UniformGenerator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */