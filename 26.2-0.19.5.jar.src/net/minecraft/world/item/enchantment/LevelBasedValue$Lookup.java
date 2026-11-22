/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ 
/*    */ public final class Lookup extends Record implements LevelBasedValue {
/*    */   private final List<Float> values;
/*    */   
/*    */   private final LevelBasedValue fallback;
/*    */   
/*    */   public static final MapCodec<Lookup> CODEC;
/*    */   
/*    */   public Lookup(List<Float> values, LevelBasedValue fallback) {
/* 70 */     this.values = values;
/* 70 */     this.fallback = fallback;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<Float> values() {
/* 70 */     return this.values;
/*    */   }
/*    */   
/*    */   public LevelBasedValue fallback() {
/* 70 */     return this.fallback;
/*    */   }
/*    */   
/*    */   static {
/* 71 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.listOf().fieldOf("values").forGetter(Lookup::values), (App)LevelBasedValue.CODEC.fieldOf("fallback").forGetter(Lookup::fallback)).apply((Applicative)i, Lookup::new));
/*    */   }
/*    */   
/*    */   public float calculate(int level) {
/* 78 */     return (level <= this.values.size()) ? ((Float)this.values.get(level - 1)).floatValue() : this.fallback.calculate(level);
/*    */   }
/*    */   
/*    */   public MapCodec<Lookup> codec() {
/* 83 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\LevelBasedValue$Lookup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */