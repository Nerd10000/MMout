/*     */ package net.minecraft.stats;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ 
/*     */ public final class Packed extends Record {
/*     */   private final RecipeBookSettings settings;
/*     */   
/*     */   private final List<ResourceKey<Recipe<?>>> known;
/*     */   
/*     */   private final List<ResourceKey<Recipe<?>>> highlight;
/*     */   
/*     */   public static final Codec<Packed> CODEC;
/*     */   
/*     */   public Packed(RecipeBookSettings settings, List<ResourceKey<Recipe<?>>> known, List<ResourceKey<Recipe<?>>> highlight) {
/* 154 */     this.settings = settings;
/* 154 */     this.known = known;
/* 154 */     this.highlight = highlight;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/stats/ServerRecipeBook$Packed;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #154	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/stats/ServerRecipeBook$Packed;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/stats/ServerRecipeBook$Packed;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #154	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/stats/ServerRecipeBook$Packed;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/stats/ServerRecipeBook$Packed;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #154	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/stats/ServerRecipeBook$Packed;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public RecipeBookSettings settings() {
/* 154 */     return this.settings;
/*     */   }
/*     */   
/*     */   public List<ResourceKey<Recipe<?>>> known() {
/* 154 */     return this.known;
/*     */   }
/*     */   
/*     */   public List<ResourceKey<Recipe<?>>> highlight() {
/* 154 */     return this.highlight;
/*     */   }
/*     */   
/*     */   static {
/* 159 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)RecipeBookSettings.MAP_CODEC.forGetter(Packed::settings), (App)Recipe.KEY_CODEC.listOf().fieldOf("recipes").forGetter(Packed::known), (App)Recipe.KEY_CODEC.listOf().fieldOf("toBeDisplayed").forGetter(Packed::highlight)).apply((Applicative)i, Packed::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\stats\ServerRecipeBook$Packed.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */