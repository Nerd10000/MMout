/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public final class Sum extends Record implements NumberProvider {
/*    */   private final List<NumberProvider> summands;
/*    */   
/*    */   public static final MapCodec<Sum> MAP_CODEC;
/*    */   
/*    */   public Sum(List<NumberProvider> summands) {
/* 12 */     this.summands = summands;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/number/Sum;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/Sum;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/number/Sum;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/Sum;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/number/Sum;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/Sum;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<NumberProvider> summands() {
/* 12 */     return this.summands;
/*    */   }
/*    */   
/*    */   static {
/* 15 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)NumberProviders.CODEC.listOf().fieldOf("summands").forGetter(Sum::summands)).apply((Applicative)i, Sum::new));
/*    */   }
/*    */   
/*    */   public static Sum sum(NumberProvider... summands) {
/* 20 */     return new Sum(List.of(summands));
/*    */   }
/*    */   
/*    */   public MapCodec<Sum> codec() {
/* 25 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public int getInt(LootContext context) {
/* 30 */     float value = 0.0F;
/* 31 */     for (NumberProvider provider : this.summands)
/* 32 */       value += provider.getFloat(context); 
/* 34 */     return Mth.floor(value);
/*    */   }
/*    */   
/*    */   public float getFloat(LootContext context) {
/* 39 */     float value = 0.0F;
/* 40 */     for (NumberProvider provider : this.summands)
/* 41 */       value += provider.getFloat(context); 
/* 43 */     return value;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 48 */     super.validate(context);
/* 50 */     Validatable.validate(context, "summands", this.summands);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\providers\number\Sum.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */