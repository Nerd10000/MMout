/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalInt;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.context.ContextMap;
/*    */ import net.minecraft.world.entity.player.StackedItemContents;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.Ingredient;
/*    */ import net.minecraft.world.item.crafting.RecipeBookCategory;
/*    */ 
/*    */ public final class RecipeDisplayEntry extends Record {
/*    */   private final RecipeDisplayId id;
/*    */   
/*    */   private final RecipeDisplay display;
/*    */   
/*    */   private final OptionalInt group;
/*    */   
/*    */   private final RecipeBookCategory category;
/*    */   
/*    */   private final Optional<List<Ingredient>> craftingRequirements;
/*    */   
/*    */   public RecipeDisplayEntry(RecipeDisplayId id, RecipeDisplay display, OptionalInt group, RecipeBookCategory category, Optional<List<Ingredient>> craftingRequirements) {
/* 22 */     this.id = id;
/* 22 */     this.display = display;
/* 22 */     this.group = group;
/* 22 */     this.category = category;
/* 22 */     this.craftingRequirements = craftingRequirements;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/RecipeDisplayEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/RecipeDisplayEntry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/RecipeDisplayEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/RecipeDisplayEntry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/RecipeDisplayEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/RecipeDisplayEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public RecipeDisplayId id() {
/* 22 */     return this.id;
/*    */   }
/*    */   
/*    */   public RecipeDisplay display() {
/* 22 */     return this.display;
/*    */   }
/*    */   
/*    */   public OptionalInt group() {
/* 22 */     return this.group;
/*    */   }
/*    */   
/*    */   public RecipeBookCategory category() {
/* 22 */     return this.category;
/*    */   }
/*    */   
/*    */   public Optional<List<Ingredient>> craftingRequirements() {
/* 22 */     return this.craftingRequirements;
/*    */   }
/*    */   
/* 29 */   public static final StreamCodec<RegistryFriendlyByteBuf, RecipeDisplayEntry> STREAM_CODEC = StreamCodec.composite(RecipeDisplayId.STREAM_CODEC, RecipeDisplayEntry::id, RecipeDisplay.STREAM_CODEC, RecipeDisplayEntry::display, ByteBufCodecs.OPTIONAL_VAR_INT, RecipeDisplayEntry::group, 
/*    */       
/* 33 */       ByteBufCodecs.registry(Registries.RECIPE_BOOK_CATEGORY), RecipeDisplayEntry::category, Ingredient.CONTENTS_STREAM_CODEC
/* 34 */       .apply(ByteBufCodecs.list()).apply(ByteBufCodecs::optional), RecipeDisplayEntry::craftingRequirements, RecipeDisplayEntry::new);
/*    */   
/*    */   public List<ItemStack> resultItems(ContextMap context) {
/* 39 */     return this.display.result().resolveForStacks(context);
/*    */   }
/*    */   
/*    */   public boolean canCraft(StackedItemContents providedContents) {
/* 43 */     if (this.craftingRequirements.isEmpty())
/* 44 */       return false; 
/* 46 */     return providedContents.canCraft(this.craftingRequirements.get(), null);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\RecipeDisplayEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */