/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public final class Data extends Record {
/*     */   private final Map<Character, Ingredient> key;
/*     */   
/*     */   private final List<String> pattern;
/*     */   
/*     */   private static final Codec<List<String>> PATTERN_CODEC;
/*     */   
/*     */   private static final Codec<Character> SYMBOL_CODEC;
/*     */   
/*     */   public static final MapCodec<Data> MAP_CODEC;
/*     */   
/*     */   public Data(Map<Character, Ingredient> key, List<String> pattern) {
/* 202 */     this.key = key;
/* 202 */     this.pattern = pattern;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #202	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #202	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #202	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Map<Character, Ingredient> key() {
/* 202 */     return this.key;
/*     */   }
/*     */   
/*     */   public List<String> pattern() {
/* 202 */     return this.pattern;
/*     */   }
/*     */   
/*     */   static {
/* 203 */     PATTERN_CODEC = Codec.STRING.listOf().comapFlatMap(strings -> {
/*     */           if (strings.size() > 3)
/*     */             return DataResult.error(()); 
/*     */           if (strings.isEmpty())
/*     */             return DataResult.error(()); 
/*     */           int firstLength = ((String)strings.getFirst()).length();
/*     */           for (String line : strings) {
/*     */             if (line.length() > 3)
/*     */               return DataResult.error(()); 
/*     */             if (firstLength != line.length())
/*     */               return DataResult.error(()); 
/*     */           } 
/*     */           return DataResult.success(strings);
/* 218 */         }Function.identity());
/* 220 */     SYMBOL_CODEC = Codec.STRING.comapFlatMap(symbol -> (symbol.length() != 1) ? DataResult.error(()) : (" ".equals(symbol) ? DataResult.error(()) : DataResult.success(Character.valueOf(symbol.charAt(0)))), String::valueOf);
/* 230 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.strictUnboundedMap(SYMBOL_CODEC, Ingredient.CODEC).fieldOf("key").forGetter(()), (App)PATTERN_CODEC.fieldOf("pattern").forGetter(())).apply((Applicative)i, Data::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\ShapedRecipePattern$Data.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */