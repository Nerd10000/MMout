/*     */ package net.minecraft.client.gui.screens.recipebook;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ 
/*     */ class OverlayCraftingRecipeButton extends OverlayRecipeComponent.OverlayRecipeButton {
/* 204 */   private static final Identifier ENABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/crafting_overlay");
/*     */   
/* 205 */   private static final Identifier HIGHLIGHTED_ENABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/crafting_overlay_highlighted");
/*     */   
/* 206 */   private static final Identifier DISABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/crafting_overlay_disabled");
/*     */   
/* 207 */   private static final Identifier HIGHLIGHTED_DISABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/crafting_overlay_disabled_highlighted");
/*     */   
/*     */   private static final int GRID_WIDTH = 3;
/*     */   
/*     */   private static final int GRID_HEIGHT = 3;
/*     */   
/*     */   public OverlayCraftingRecipeButton(OverlayRecipeComponent paramOverlayRecipeComponent, int x, int y, RecipeDisplayId id, RecipeDisplay recipe, ContextMap context, boolean isCraftable) {
/* 213 */     super(paramOverlayRecipeComponent, x, y, id, isCraftable, calculateIngredientsPositions(recipe, context));
/*     */   }
/*     */   
/*     */   private static List<OverlayRecipeComponent.OverlayRecipeButton.Pos> calculateIngredientsPositions(RecipeDisplay recipe, ContextMap context) {
/*     */     // Byte code:
/*     */     //   0: new java/util/ArrayList
/*     */     //   3: dup
/*     */     //   4: invokespecial <init> : ()V
/*     */     //   7: astore_2
/*     */     //   8: aload_0
/*     */     //   9: dup
/*     */     //   10: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   13: pop
/*     */     //   14: astore_3
/*     */     //   15: iconst_0
/*     */     //   16: istore #4
/*     */     //   18: aload_3
/*     */     //   19: iload #4
/*     */     //   21: <illegal opcode> typeSwitch : (Lnet/minecraft/world/item/crafting/display/RecipeDisplay;I)I
/*     */     //   26: lookupswitch default -> 175, 0 -> 52, 1 -> 88
/*     */     //   52: aload_3
/*     */     //   53: checkcast net/minecraft/world/item/crafting/display/ShapedCraftingRecipeDisplay
/*     */     //   56: astore #5
/*     */     //   58: iconst_3
/*     */     //   59: iconst_3
/*     */     //   60: aload #5
/*     */     //   62: invokevirtual width : ()I
/*     */     //   65: aload #5
/*     */     //   67: invokevirtual height : ()I
/*     */     //   70: aload #5
/*     */     //   72: invokevirtual ingredients : ()Ljava/util/List;
/*     */     //   75: aload_1
/*     */     //   76: aload_2
/*     */     //   77: <illegal opcode> addItemToSlot : (Lnet/minecraft/util/context/ContextMap;Ljava/util/List;)Lnet/minecraft/recipebook/PlaceRecipeHelper$Output;
/*     */     //   82: invokestatic placeRecipe : (IIIILjava/lang/Iterable;Lnet/minecraft/recipebook/PlaceRecipeHelper$Output;)V
/*     */     //   85: goto -> 175
/*     */     //   88: aload_3
/*     */     //   89: checkcast net/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay
/*     */     //   92: astore #6
/*     */     //   94: aload #6
/*     */     //   96: invokevirtual ingredients : ()Ljava/util/List;
/*     */     //   99: astore #7
/*     */     //   101: iconst_0
/*     */     //   102: istore #8
/*     */     //   104: iload #8
/*     */     //   106: aload #7
/*     */     //   108: invokeinterface size : ()I
/*     */     //   113: if_icmpge -> 172
/*     */     //   116: aload #7
/*     */     //   118: iload #8
/*     */     //   120: invokeinterface get : (I)Ljava/lang/Object;
/*     */     //   125: checkcast net/minecraft/world/item/crafting/display/SlotDisplay
/*     */     //   128: aload_1
/*     */     //   129: invokeinterface resolveForStacks : (Lnet/minecraft/util/context/ContextMap;)Ljava/util/List;
/*     */     //   134: astore #9
/*     */     //   136: aload #9
/*     */     //   138: invokeinterface isEmpty : ()Z
/*     */     //   143: ifne -> 166
/*     */     //   146: aload_2
/*     */     //   147: iload #8
/*     */     //   149: iconst_3
/*     */     //   150: irem
/*     */     //   151: iload #8
/*     */     //   153: iconst_3
/*     */     //   154: idiv
/*     */     //   155: aload #9
/*     */     //   157: invokestatic createGridPos : (IILjava/util/List;)Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;
/*     */     //   160: invokeinterface add : (Ljava/lang/Object;)Z
/*     */     //   165: pop
/*     */     //   166: iinc #8, 1
/*     */     //   169: goto -> 104
/*     */     //   172: goto -> 175
/*     */     //   175: aload_2
/*     */     //   176: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #217	-> 0
/*     */     //   #218	-> 8
/*     */     //   #219	-> 52
/*     */     //   #220	-> 58
/*     */     //   #228	-> 88
/*     */     //   #229	-> 94
/*     */     //   #230	-> 101
/*     */     //   #231	-> 116
/*     */     //   #232	-> 136
/*     */     //   #233	-> 146
/*     */     //   #230	-> 166
/*     */     //   #236	-> 172
/*     */     //   #241	-> 175
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   58	30	5	shaped	Lnet/minecraft/world/item/crafting/display/ShapedCraftingRecipeDisplay;
/*     */     //   136	30	9	items	Ljava/util/List;
/*     */     //   104	68	8	i	I
/*     */     //   101	71	7	ingredients	Ljava/util/List;
/*     */     //   94	81	6	shapeless	Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay;
/*     */     //   0	177	0	recipe	Lnet/minecraft/world/item/crafting/display/RecipeDisplay;
/*     */     //   0	177	1	context	Lnet/minecraft/util/context/ContextMap;
/*     */     //   8	169	2	result	Ljava/util/List;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   136	30	9	items	Ljava/util/List<Lnet/minecraft/world/item/ItemStack;>;
/*     */     //   101	71	7	ingredients	Ljava/util/List<Lnet/minecraft/world/item/crafting/display/SlotDisplay;>;
/*     */     //   8	169	2	result	Ljava/util/List<Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;>;
/*     */   }
/*     */   
/*     */   protected Identifier getSprite(boolean isCraftable) {
/* 246 */     if (isCraftable)
/* 247 */       return isHoveredOrFocused() ? HIGHLIGHTED_ENABLED_SPRITE : ENABLED_SPRITE; 
/* 249 */     return isHoveredOrFocused() ? HIGHLIGHTED_DISABLED_SPRITE : DISABLED_SPRITE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\recipebook\OverlayRecipeComponent$OverlayCraftingRecipeButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */