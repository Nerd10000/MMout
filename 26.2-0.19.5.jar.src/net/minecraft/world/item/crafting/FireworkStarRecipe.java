/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.util.Map;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.component.FireworkExplosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class FireworkStarRecipe extends CustomRecipe {
/*     */   public static final MapCodec<FireworkStarRecipe> MAP_CODEC;
/*     */   
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, FireworkStarRecipe> STREAM_CODEC;
/*     */   
/*     */   static {
/*  24 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.simpleMap(FireworkExplosion.Shape.CODEC, Ingredient.CODEC, StringRepresentable.keys((StringRepresentable[])FireworkExplosion.Shape.values())).fieldOf("shapes").forGetter(()), (App)Ingredient.CODEC.fieldOf("trail").forGetter(()), (App)Ingredient.CODEC.fieldOf("twinkle").forGetter(()), (App)Ingredient.CODEC.fieldOf("fuel").forGetter(()), (App)Ingredient.CODEC.fieldOf("dye").forGetter(()), (App)ItemStackTemplate.CODEC.fieldOf("result").forGetter(())).apply((Applicative)i, FireworkStarRecipe::new));
/*  37 */     STREAM_CODEC = StreamCodec.composite(
/*  38 */         ByteBufCodecs.map(java.util.HashMap::new, FireworkExplosion.Shape.STREAM_CODEC, Ingredient.CONTENTS_STREAM_CODEC), o -> o.shapes, Ingredient.CONTENTS_STREAM_CODEC, o -> o.trail, Ingredient.CONTENTS_STREAM_CODEC, o -> o.twinkle, Ingredient.CONTENTS_STREAM_CODEC, o -> o.fuel, Ingredient.CONTENTS_STREAM_CODEC, o -> o.dye, ItemStackTemplate.STREAM_CODEC, o -> o.result, FireworkStarRecipe::new);
/*     */   }
/*     */   
/*  51 */   public static final RecipeSerializer<FireworkStarRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*     */   
/*     */   private final Map<FireworkExplosion.Shape, Ingredient> shapes;
/*     */   
/*     */   private final Ingredient trail;
/*     */   
/*     */   private final Ingredient twinkle;
/*     */   
/*     */   private final Ingredient fuel;
/*     */   
/*     */   private final Ingredient dye;
/*     */   
/*     */   private final ItemStackTemplate result;
/*     */   
/*     */   public FireworkStarRecipe(Map<FireworkExplosion.Shape, Ingredient> shapes, Ingredient trail, Ingredient twinkle, Ingredient fuel, Ingredient dye, ItemStackTemplate result) {
/*  63 */     this.shapes = shapes;
/*  64 */     this.trail = trail;
/*  65 */     this.twinkle = twinkle;
/*  66 */     this.fuel = fuel;
/*  67 */     this.dye = dye;
/*  68 */     this.result = result;
/*     */   }
/*     */   
/*     */   private FireworkExplosion.Shape findShape(ItemStack itemStack) {
/*  72 */     for (Map.Entry<FireworkExplosion.Shape, Ingredient> e : this.shapes.entrySet()) {
/*  73 */       if (((Ingredient)e.getValue()).test(itemStack))
/*  74 */         return e.getKey(); 
/*     */     } 
/*  77 */     return null;
/*     */   }
/*     */   
/*     */   public boolean matches(CraftingInput input, Level level) {
/*  83 */     if (input.ingredientCount() < 2)
/*  84 */       return false; 
/*  87 */     boolean hasFuel = false;
/*  88 */     boolean hasDye = false;
/*  89 */     boolean hasShape = false;
/*  90 */     boolean hasTrail = false;
/*  91 */     boolean hasTwinkle = false;
/*  93 */     for (int slot = 0; slot < input.size(); slot++) {
/*  94 */       ItemStack itemStack = input.getItem(slot);
/*  95 */       if (!itemStack.isEmpty())
/*  99 */         if (this.twinkle.test(itemStack)) {
/* 100 */           if (hasTwinkle)
/* 101 */             return false; 
/* 103 */           hasTwinkle = true;
/* 104 */         } else if (this.trail.test(itemStack)) {
/* 105 */           if (hasTrail)
/* 106 */             return false; 
/* 108 */           hasTrail = true;
/* 109 */         } else if (this.fuel.test(itemStack)) {
/* 110 */           if (hasFuel)
/* 111 */             return false; 
/* 113 */           hasFuel = true;
/* 114 */         } else if (this.dye.test(itemStack) && itemStack.has(DataComponents.DYE)) {
/* 115 */           hasDye = true;
/*     */         } else {
/* 117 */           FireworkExplosion.Shape shape = findShape(itemStack);
/* 118 */           if (shape != null) {
/* 119 */             if (hasShape)
/* 120 */               return false; 
/* 122 */             hasShape = true;
/*     */           } else {
/* 124 */             return false;
/*     */           } 
/*     */         }  
/*     */     } 
/* 129 */     return (hasFuel && hasDye);
/*     */   }
/*     */   
/*     */   public ItemStack assemble(CraftingInput input) {
/* 134 */     FireworkExplosion.Shape shape = FireworkExplosion.Shape.SMALL_BALL;
/* 135 */     boolean hasTwinkle = false;
/* 136 */     boolean hasTrail = false;
/* 137 */     IntArrayList intArrayList = new IntArrayList();
/* 139 */     for (int slot = 0; slot < input.size(); slot++) {
/* 140 */       ItemStack itemStack = input.getItem(slot);
/* 141 */       if (!itemStack.isEmpty()) {
/* 145 */         FireworkExplosion.Shape maybeShape = findShape(itemStack);
/* 146 */         if (maybeShape != null) {
/* 147 */           shape = maybeShape;
/* 148 */         } else if (this.twinkle.test(itemStack)) {
/* 149 */           hasTwinkle = true;
/* 150 */         } else if (this.trail.test(itemStack)) {
/* 151 */           hasTrail = true;
/* 152 */         } else if (this.dye.test(itemStack)) {
/* 153 */           DyeColor dye = (DyeColor)itemStack.getOrDefault(DataComponents.DYE, DyeColor.WHITE);
/* 154 */           intArrayList.add(dye.getFireworkColor());
/*     */         } 
/*     */       } 
/*     */     } 
/* 158 */     ItemStack star = this.result.create();
/* 159 */     star.set(DataComponents.FIREWORK_EXPLOSION, new FireworkExplosion(shape, (IntList)intArrayList, IntList.of(), hasTrail, hasTwinkle));
/* 160 */     return star;
/*     */   }
/*     */   
/*     */   public RecipeSerializer<FireworkStarRecipe> getSerializer() {
/* 165 */     return SERIALIZER;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\FireworkStarRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */