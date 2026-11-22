/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.component.DataComponentPatch;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.component.FireworkExplosion;
/*     */ import net.minecraft.world.item.component.Fireworks;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class FireworkRocketRecipe extends CustomRecipe {
/*     */   public static final MapCodec<FireworkRocketRecipe> MAP_CODEC;
/*     */   
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, FireworkRocketRecipe> STREAM_CODEC;
/*     */   
/*     */   static {
/*  19 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Ingredient.CODEC.fieldOf("shell").forGetter(()), (App)Ingredient.CODEC.fieldOf("fuel").forGetter(()), (App)Ingredient.CODEC.fieldOf("star").forGetter(()), (App)ItemStackTemplate.CODEC.fieldOf("result").forGetter(())).apply((Applicative)i, FireworkRocketRecipe::new));
/*  26 */     STREAM_CODEC = StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, o -> o.shell, Ingredient.CONTENTS_STREAM_CODEC, o -> o.fuel, Ingredient.CONTENTS_STREAM_CODEC, o -> o.star, ItemStackTemplate.STREAM_CODEC, o -> o.result, FireworkRocketRecipe::new);
/*     */   }
/*     */   
/*  34 */   public static final RecipeSerializer<FireworkRocketRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*     */   
/*     */   private final Ingredient shell;
/*     */   
/*     */   private final Ingredient fuel;
/*     */   
/*     */   private final Ingredient star;
/*     */   
/*     */   private final ItemStackTemplate result;
/*     */   
/*     */   public FireworkRocketRecipe(Ingredient shell, Ingredient fuel, Ingredient star, ItemStackTemplate result) {
/*  43 */     this.shell = shell;
/*  44 */     this.fuel = fuel;
/*  45 */     this.star = star;
/*  46 */     this.result = result;
/*     */   }
/*     */   
/*     */   public boolean matches(CraftingInput input, Level level) {
/*  52 */     if (input.ingredientCount() < 2)
/*  53 */       return false; 
/*  56 */     boolean hasShell = false;
/*  57 */     int fuelCount = 0;
/*  59 */     for (int slot = 0; slot < input.size(); slot++) {
/*  60 */       ItemStack itemStack = input.getItem(slot);
/*  61 */       if (!itemStack.isEmpty())
/*  65 */         if (this.shell.test(itemStack)) {
/*  66 */           if (hasShell)
/*  67 */             return false; 
/*  69 */           hasShell = true;
/*  70 */         } else if (this.fuel.test(itemStack)) {
/*  71 */           fuelCount++;
/*  72 */           if (fuelCount > 3)
/*  73 */             return false; 
/*  75 */         } else if (!this.star.test(itemStack)) {
/*  76 */           return false;
/*     */         }  
/*     */     } 
/*  80 */     return (hasShell && fuelCount >= 1);
/*     */   }
/*     */   
/*     */   public ItemStack assemble(CraftingInput input) {
/*  85 */     List<FireworkExplosion> explosions = new ArrayList<>();
/*  87 */     int fuelCount = 0;
/*  89 */     for (int slot = 0; slot < input.size(); slot++) {
/*  90 */       ItemStack itemStack = input.getItem(slot);
/*  91 */       if (!itemStack.isEmpty())
/*  95 */         if (this.fuel.test(itemStack)) {
/*  96 */           fuelCount++;
/*  97 */         } else if (this.star.test(itemStack)) {
/*  98 */           FireworkExplosion explosion = (FireworkExplosion)itemStack.get(DataComponents.FIREWORK_EXPLOSION);
/*  99 */           if (explosion != null)
/* 100 */             explosions.add(explosion); 
/*     */         }  
/*     */     } 
/* 107 */     DataComponentPatch components = DataComponentPatch.builder().set(DataComponents.FIREWORKS, new Fireworks(fuelCount, explosions)).build();
/* 109 */     return this.result.apply(components);
/*     */   }
/*     */   
/*     */   public RecipeSerializer<FireworkRocketRecipe> getSerializer() {
/* 114 */     return SERIALIZER;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\FireworkRocketRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */