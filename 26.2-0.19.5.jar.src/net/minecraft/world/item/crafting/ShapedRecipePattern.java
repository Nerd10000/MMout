/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.chars.CharArraySet;
/*     */ import it.unimi.dsi.fastutil.chars.CharSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public final class ShapedRecipePattern {
/*     */   private static final int MAX_SIZE = 3;
/*     */   
/*     */   public static final char EMPTY_SLOT = ' ';
/*     */   
/*     */   public static final MapCodec<ShapedRecipePattern> MAP_CODEC;
/*     */   
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, ShapedRecipePattern> STREAM_CODEC;
/*     */   
/*     */   private final int width;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   private final List<Optional<Ingredient>> ingredients;
/*     */   
/*     */   private final Optional<Data> data;
/*     */   
/*     */   private final int ingredientCount;
/*     */   
/*     */   private final boolean symmetrical;
/*     */   
/*     */   static {
/*  28 */     MAP_CODEC = Data.MAP_CODEC.flatXmap(ShapedRecipePattern::unpack, pattern -> (DataResult)pattern.data.<DataResult>map(DataResult::success).orElseGet(()));
/*  33 */     STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, e -> Integer.valueOf(e.width), ByteBufCodecs.VAR_INT, e -> Integer.valueOf(e.height), Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC
/*     */         
/*  36 */         .apply(ByteBufCodecs.list()), e -> e.ingredients, ShapedRecipePattern::createFromNetwork);
/*     */   }
/*     */   
/*     */   public ShapedRecipePattern(int width, int height, List<Optional<Ingredient>> ingredients, Optional<Data> data) {
/*  49 */     this.width = width;
/*  50 */     this.height = height;
/*  51 */     this.ingredients = ingredients;
/*  52 */     this.data = data;
/*  53 */     this.ingredientCount = (int)ingredients.stream().flatMap(Optional::stream).count();
/*  54 */     this.symmetrical = Util.isSymmetrical(width, height, ingredients);
/*     */   }
/*     */   
/*     */   private static ShapedRecipePattern createFromNetwork(Integer width, Integer height, List<Optional<Ingredient>> ingredients) {
/*  59 */     return new ShapedRecipePattern(width.intValue(), height.intValue(), ingredients, Optional.empty());
/*     */   }
/*     */   
/*     */   public static ShapedRecipePattern of(Map<Character, Ingredient> key, String... pattern) {
/*  63 */     return of(key, List.of(pattern));
/*     */   }
/*     */   
/*     */   public static ShapedRecipePattern of(Map<Character, Ingredient> key, List<String> pattern) {
/*  67 */     Data data = new Data(key, pattern);
/*  68 */     return (ShapedRecipePattern)unpack(data).getOrThrow();
/*     */   }
/*     */   
/*     */   private static DataResult<ShapedRecipePattern> unpack(Data data) {
/*  72 */     String[] shrunkPattern = shrink(data.pattern);
/*  73 */     int width = shrunkPattern[0].length();
/*  74 */     int height = shrunkPattern.length;
/*  75 */     List<Optional<Ingredient>> ingredients = new ArrayList<>(width * height);
/*  76 */     CharArraySet charArraySet = new CharArraySet(data.key.keySet());
/*  78 */     for (String line : shrunkPattern) {
/*  79 */       for (int x = 0; x < line.length(); x++) {
/*     */         Optional<Ingredient> ingredient;
/*  80 */         char symbol = line.charAt(x);
/*  82 */         if (symbol == ' ') {
/*  83 */           ingredient = Optional.empty();
/*     */         } else {
/*  85 */           Ingredient ingredientForSymbol = data.key.get(Character.valueOf(symbol));
/*  86 */           if (ingredientForSymbol == null)
/*  87 */             return DataResult.error(() -> "Pattern references symbol '" + symbol + "' but it's not defined in the key"); 
/*  89 */           ingredient = Optional.of(ingredientForSymbol);
/*     */         } 
/*  91 */         charArraySet.remove(symbol);
/*  92 */         ingredients.add(ingredient);
/*     */       } 
/*     */     } 
/*  96 */     if (!charArraySet.isEmpty())
/*  97 */       return DataResult.error(() -> "Key defines symbols that aren't used in pattern: " + String.valueOf(unusedSymbols)); 
/* 100 */     return DataResult.success(new ShapedRecipePattern(width, height, ingredients, Optional.of(data)));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static String[] shrink(List<String> pattern) {
/* 106 */     int left = Integer.MAX_VALUE;
/* 107 */     int right = 0;
/* 108 */     int top = 0;
/* 109 */     int bottom = 0;
/* 111 */     for (int i = 0; i < pattern.size(); i++) {
/* 112 */       String str = pattern.get(i);
/* 114 */       left = Math.min(left, firstNonEmpty(str));
/* 115 */       int lastNonSpace = lastNonEmpty(str);
/* 116 */       right = Math.max(right, lastNonSpace);
/* 119 */       if (lastNonSpace < 0) {
/* 120 */         if (top == i)
/* 121 */           top++; 
/* 123 */         bottom++;
/*     */       } else {
/* 125 */         bottom = 0;
/*     */       } 
/*     */     } 
/* 129 */     if (pattern.size() == bottom)
/* 130 */       return new String[0]; 
/* 133 */     String[] result = new String[pattern.size() - bottom - top];
/* 134 */     for (int line = 0; line < result.length; line++)
/* 135 */       result[line] = ((String)pattern.get(line + top)).substring(left, right + 1); 
/* 138 */     return result;
/*     */   }
/*     */   
/*     */   private static int firstNonEmpty(String line) {
/* 142 */     int index = 0;
/* 143 */     while (index < line.length() && line.charAt(index) == ' ')
/* 144 */       index++; 
/* 146 */     return index;
/*     */   }
/*     */   
/*     */   private static int lastNonEmpty(String line) {
/* 150 */     int index = line.length() - 1;
/* 151 */     while (index >= 0 && line.charAt(index) == ' ')
/* 152 */       index--; 
/* 154 */     return index;
/*     */   }
/*     */   
/*     */   public boolean matches(CraftingInput input) {
/* 158 */     if (input.ingredientCount() != this.ingredientCount)
/* 159 */       return false; 
/* 161 */     if (input.width() == this.width && input.height() == this.height) {
/* 162 */       if (!this.symmetrical && matches(input, true))
/* 163 */         return true; 
/* 165 */       if (matches(input, false))
/* 166 */         return true; 
/*     */     } 
/* 169 */     return false;
/*     */   }
/*     */   
/*     */   private boolean matches(CraftingInput input, boolean xFlip) {
/* 173 */     for (int y = 0; y < this.height; y++) {
/* 174 */       for (int x = 0; x < this.width; x++) {
/*     */         Optional<Ingredient> expected;
/* 176 */         if (xFlip) {
/* 177 */           expected = this.ingredients.get(this.width - x - 1 + y * this.width);
/*     */         } else {
/* 179 */           expected = this.ingredients.get(x + y * this.width);
/*     */         } 
/* 181 */         ItemStack actual = input.getItem(x, y);
/* 182 */         if (!Ingredient.testOptionalIngredient(expected, actual))
/* 183 */           return false; 
/*     */       } 
/*     */     } 
/* 187 */     return true;
/*     */   }
/*     */   
/*     */   public int width() {
/* 191 */     return this.width;
/*     */   }
/*     */   
/*     */   public int height() {
/* 195 */     return this.height;
/*     */   }
/*     */   
/*     */   public List<Optional<Ingredient>> ingredients() {
/* 199 */     return this.ingredients;
/*     */   }
/*     */   
/*     */   public static final class Data extends Record {
/*     */     private final Map<Character, Ingredient> key;
/*     */     
/*     */     private final List<String> pattern;
/*     */     
/*     */     private static final Codec<List<String>> PATTERN_CODEC;
/*     */     
/*     */     private static final Codec<Character> SYMBOL_CODEC;
/*     */     
/*     */     public static final MapCodec<Data> MAP_CODEC;
/*     */     
/*     */     public Data(Map<Character, Ingredient> key, List<String> pattern) {
/* 202 */       this.key = key;
/* 202 */       this.pattern = pattern;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #202	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #202	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #202	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Map<Character, Ingredient> key() {
/* 202 */       return this.key;
/*     */     }
/*     */     
/*     */     public List<String> pattern() {
/* 202 */       return this.pattern;
/*     */     }
/*     */     
/*     */     static {
/* 203 */       PATTERN_CODEC = Codec.STRING.listOf().comapFlatMap(strings -> {
/*     */             if (strings.size() > 3)
/*     */               return DataResult.error(()); 
/*     */             if (strings.isEmpty())
/*     */               return DataResult.error(()); 
/*     */             int firstLength = ((String)strings.getFirst()).length();
/*     */             for (String line : strings) {
/*     */               if (line.length() > 3)
/*     */                 return DataResult.error(()); 
/*     */               if (firstLength != line.length())
/*     */                 return DataResult.error(()); 
/*     */             } 
/*     */             return DataResult.success(strings);
/* 218 */           }Function.identity());
/* 220 */       SYMBOL_CODEC = Codec.STRING.comapFlatMap(symbol -> (symbol.length() != 1) ? DataResult.error(()) : (" ".equals(symbol) ? DataResult.error(()) : DataResult.success(Character.valueOf(symbol.charAt(0)))), String::valueOf);
/* 230 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.strictUnboundedMap(SYMBOL_CODEC, Ingredient.CODEC).fieldOf("key").forGetter(()), (App)PATTERN_CODEC.fieldOf("pattern").forGetter(())).apply((Applicative)i, Data::new));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\ShapedRecipePattern.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */