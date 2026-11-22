/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public class CreativeModeTab {
/*  15 */   private static final Identifier DEFAULT_BACKGROUND = createTextureLocation("items");
/*     */   
/*     */   private final Component displayName;
/*     */   
/*  17 */   private Identifier backgroundTexture = DEFAULT_BACKGROUND;
/*     */   
/*     */   private boolean canScroll = true;
/*     */   
/*     */   private boolean showTitle = true;
/*     */   
/*     */   private boolean alignedRight = false;
/*     */   
/*     */   private final Row row;
/*     */   
/*     */   private final int column;
/*     */   
/*     */   private final Type type;
/*     */   
/*     */   private ItemStack iconItemStack;
/*     */   
/*  25 */   private Collection<ItemStack> displayItems = ItemStackLinkedSet.createTypeAndComponentsSet();
/*     */   
/*  26 */   private Set<ItemStack> displayItemsSearchTab = ItemStackLinkedSet.createTypeAndComponentsSet();
/*     */   
/*     */   private final Supplier<ItemStack> iconGenerator;
/*     */   
/*     */   private final DisplayItemsGenerator displayItemsGenerator;
/*     */   
/*     */   private CreativeModeTab(Row row, int column, Type type, Component displayName, Supplier<ItemStack> iconGenerator, DisplayItemsGenerator displayItemsGenerator) {
/*  31 */     this.row = row;
/*  32 */     this.column = column;
/*  33 */     this.displayName = displayName;
/*  34 */     this.iconGenerator = iconGenerator;
/*  35 */     this.displayItemsGenerator = displayItemsGenerator;
/*  36 */     this.type = type;
/*     */   }
/*     */   
/*     */   public static Identifier createTextureLocation(String name) {
/*  40 */     return Identifier.withDefaultNamespace("textures/gui/container/creative_inventory/tab_" + name + ".png");
/*     */   }
/*     */   
/*     */   public static Builder builder(Row row, int column) {
/*  44 */     return new Builder(row, column);
/*     */   }
/*     */   
/*     */   public Component getDisplayName() {
/*  48 */     return this.displayName;
/*     */   }
/*     */   
/*     */   public ItemStack getIconItem() {
/*  52 */     if (this.iconItemStack == null)
/*  53 */       this.iconItemStack = this.iconGenerator.get(); 
/*  55 */     return this.iconItemStack;
/*     */   }
/*     */   
/*     */   public Identifier getBackgroundTexture() {
/*  59 */     return this.backgroundTexture;
/*     */   }
/*     */   
/*     */   public boolean showTitle() {
/*  63 */     return this.showTitle;
/*     */   }
/*     */   
/*     */   public boolean canScroll() {
/*  67 */     return this.canScroll;
/*     */   }
/*     */   
/*     */   public int column() {
/*  71 */     return this.column;
/*     */   }
/*     */   
/*     */   public Row row() {
/*  75 */     return this.row;
/*     */   }
/*     */   
/*     */   public boolean hasAnyItems() {
/*  79 */     return !this.displayItems.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean shouldDisplay() {
/*  83 */     return (this.type != Type.CATEGORY || hasAnyItems());
/*     */   }
/*     */   
/*     */   public boolean isAlignedRight() {
/*  87 */     return this.alignedRight;
/*     */   }
/*     */   
/*     */   public Type getType() {
/*  91 */     return this.type;
/*     */   }
/*     */   
/*     */   public void buildContents(ItemDisplayParameters parameters) {
/*  95 */     ItemDisplayBuilder displayList = new ItemDisplayBuilder(this, parameters.enabledFeatures);
/*  96 */     this.displayItemsGenerator.accept(parameters, displayList);
/*  97 */     this.displayItems = displayList.tabContents;
/*  98 */     this.displayItemsSearchTab = displayList.searchTabContents;
/*     */   }
/*     */   
/*     */   public Collection<ItemStack> getDisplayItems() {
/* 102 */     return this.displayItems;
/*     */   }
/*     */   
/*     */   public Collection<ItemStack> getSearchTabDisplayItems() {
/* 106 */     return this.displayItemsSearchTab;
/*     */   }
/*     */   
/*     */   public boolean contains(ItemStack stack) {
/* 110 */     return this.displayItemsSearchTab.contains(stack);
/*     */   }
/*     */   
/*     */   public static final class ItemDisplayParameters extends Record {
/*     */     private final FeatureFlagSet enabledFeatures;
/*     */     
/*     */     private final boolean hasPermissions;
/*     */     
/*     */     private final HolderLookup.Provider holders;
/*     */     
/*     */     public ItemDisplayParameters(FeatureFlagSet enabledFeatures, boolean hasPermissions, HolderLookup.Provider holders) {
/* 113 */       this.enabledFeatures = enabledFeatures;
/* 113 */       this.hasPermissions = hasPermissions;
/* 113 */       this.holders = holders;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #113	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #113	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #113	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public FeatureFlagSet enabledFeatures() {
/* 113 */       return this.enabledFeatures;
/*     */     }
/*     */     
/*     */     public boolean hasPermissions() {
/* 113 */       return this.hasPermissions;
/*     */     }
/*     */     
/*     */     public HolderLookup.Provider holders() {
/* 113 */       return this.holders;
/*     */     }
/*     */     
/*     */     public boolean needsUpdate(FeatureFlagSet enabledFeatures, boolean hasPermissions, HolderLookup.Provider holders) {
/* 115 */       return (!this.enabledFeatures.equals(enabledFeatures) || this.hasPermissions != hasPermissions || this.holders != holders);
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Type {
/* 125 */     CATEGORY, INVENTORY, HOTBAR, SEARCH;
/*     */   }
/*     */   
/*     */   public enum Row {
/* 132 */     TOP, BOTTOM;
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private static final CreativeModeTab.DisplayItemsGenerator EMPTY_GENERATOR = (parameters, output) -> {
/*     */       
/*     */       };
/*     */     
/*     */     private final CreativeModeTab.Row row;
/*     */     
/*     */     private final int column;
/*     */     
/* 141 */     private Component displayName = (Component)Component.empty();
/*     */     
/*     */     private Supplier<ItemStack> iconGenerator = () -> ItemStack.EMPTY;
/*     */     
/* 143 */     private CreativeModeTab.DisplayItemsGenerator displayItemsGenerator = EMPTY_GENERATOR;
/*     */     
/*     */     private boolean canScroll = true;
/*     */     
/*     */     private boolean showTitle = true;
/*     */     
/*     */     private boolean alignedRight = false;
/*     */     
/* 147 */     private CreativeModeTab.Type type = CreativeModeTab.Type.CATEGORY;
/*     */     
/* 148 */     private Identifier backgroundTexture = CreativeModeTab.DEFAULT_BACKGROUND;
/*     */     
/*     */     public Builder(CreativeModeTab.Row row, int column) {
/* 151 */       this.row = row;
/* 152 */       this.column = column;
/*     */     }
/*     */     
/*     */     public Builder title(Component displayName) {
/* 156 */       this.displayName = displayName;
/* 157 */       return this;
/*     */     }
/*     */     
/*     */     public Builder icon(Supplier<ItemStack> iconGenerator) {
/* 161 */       this.iconGenerator = iconGenerator;
/* 162 */       return this;
/*     */     }
/*     */     
/*     */     public Builder displayItems(CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
/* 166 */       this.displayItemsGenerator = displayItemsGenerator;
/* 167 */       return this;
/*     */     }
/*     */     
/*     */     public Builder alignedRight() {
/* 171 */       this.alignedRight = true;
/* 172 */       return this;
/*     */     }
/*     */     
/*     */     public Builder hideTitle() {
/* 176 */       this.showTitle = false;
/* 177 */       return this;
/*     */     }
/*     */     
/*     */     public Builder noScrollBar() {
/* 181 */       this.canScroll = false;
/* 182 */       return this;
/*     */     }
/*     */     
/*     */     protected Builder type(CreativeModeTab.Type type) {
/* 186 */       this.type = type;
/* 187 */       return this;
/*     */     }
/*     */     
/*     */     public Builder backgroundTexture(Identifier backgroundTexture) {
/* 191 */       this.backgroundTexture = backgroundTexture;
/* 192 */       return this;
/*     */     }
/*     */     
/*     */     public CreativeModeTab build() {
/* 196 */       if ((this.type == CreativeModeTab.Type.HOTBAR || this.type == CreativeModeTab.Type.INVENTORY) && this.displayItemsGenerator != EMPTY_GENERATOR)
/* 197 */         throw new IllegalStateException("Special tabs can't have display items"); 
/* 200 */       CreativeModeTab tab = new CreativeModeTab(this.row, this.column, this.type, this.displayName, this.iconGenerator, this.displayItemsGenerator);
/* 201 */       tab.alignedRight = this.alignedRight;
/* 202 */       tab.showTitle = this.showTitle;
/* 203 */       tab.canScroll = this.canScroll;
/* 204 */       tab.backgroundTexture = this.backgroundTexture;
/* 205 */       return tab;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ItemDisplayBuilder implements Output {
/* 210 */     public final Collection<ItemStack> tabContents = ItemStackLinkedSet.createTypeAndComponentsSet();
/*     */     
/* 211 */     public final Set<ItemStack> searchTabContents = ItemStackLinkedSet.createTypeAndComponentsSet();
/*     */     
/*     */     private final CreativeModeTab tab;
/*     */     
/*     */     private final FeatureFlagSet featureFlagSet;
/*     */     
/*     */     public ItemDisplayBuilder(CreativeModeTab tab, FeatureFlagSet featureFlagSet) {
/* 216 */       this.tab = tab;
/* 217 */       this.featureFlagSet = featureFlagSet;
/*     */     }
/*     */     
/*     */     public void accept(ItemStack stack, CreativeModeTab.TabVisibility tabVisibility) {
/* 222 */       if (stack.getCount() != 1)
/* 223 */         throw new IllegalArgumentException("Stack size must be exactly 1"); 
/* 227 */       boolean foundDuplicateStack = (this.tabContents.contains(stack) && tabVisibility != CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
/* 229 */       if (foundDuplicateStack)
/* 230 */         throw new IllegalStateException("Accidentally adding the same item stack twice " + stack
/* 231 */             .getDisplayName().getString() + " to a Creative Mode Tab: " + this.tab
/*     */             
/* 233 */             .getDisplayName().getString()); 
/* 236 */       if (stack.getItem().isEnabled(this.featureFlagSet))
/* 237 */         switch (tabVisibility.ordinal()) {
/*     */           case 0:
/* 239 */             this.tabContents.add(stack);
/* 240 */             this.searchTabContents.add(stack);
/*     */             break;
/*     */           case 1:
/* 242 */             this.tabContents.add(stack);
/*     */             break;
/*     */           case 2:
/* 243 */             this.searchTabContents.add(stack);
/*     */             break;
/*     */         }  
/*     */     }
/*     */   }
/*     */   
/*     */   protected enum TabVisibility {
/* 250 */     PARENT_AND_SEARCH_TABS, PARENT_TAB_ONLY, SEARCH_TAB_ONLY;
/*     */   }
/*     */   
/*     */   protected static interface Output {
/*     */     default void accept(ItemStack stack) {
/* 259 */       accept(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
/*     */     }
/*     */     
/*     */     default void accept(ItemLike item, CreativeModeTab.TabVisibility tabVisibility) {
/* 263 */       accept(new ItemStack(item), tabVisibility);
/*     */     }
/*     */     
/*     */     default void accept(ItemLike item) {
/* 267 */       accept(new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
/*     */     }
/*     */     
/*     */     default void acceptAll(Collection<ItemStack> stacks, CreativeModeTab.TabVisibility tabVisibility) {
/* 271 */       stacks.forEach(stack -> accept(stack, tabVisibility));
/*     */     }
/*     */     
/*     */     default void acceptAll(Collection<ItemStack> stacks) {
/* 275 */       acceptAll(stacks, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
/*     */     }
/*     */     
/*     */     void accept(ItemStack param1ItemStack, CreativeModeTab.TabVisibility param1TabVisibility);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface DisplayItemsGenerator {
/*     */     void accept(CreativeModeTab.ItemDisplayParameters param1ItemDisplayParameters, CreativeModeTab.Output param1Output);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\CreativeModeTab.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */