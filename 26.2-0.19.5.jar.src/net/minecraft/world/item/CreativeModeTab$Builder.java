/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class Builder {
/*     */   private static final CreativeModeTab.DisplayItemsGenerator EMPTY_GENERATOR = (parameters, output) -> {
/*     */     
/*     */     };
/*     */   
/*     */   private final CreativeModeTab.Row row;
/*     */   
/*     */   private final int column;
/*     */   
/* 141 */   private Component displayName = (Component)Component.empty();
/*     */   
/*     */   private Supplier<ItemStack> iconGenerator = () -> ItemStack.EMPTY;
/*     */   
/* 143 */   private CreativeModeTab.DisplayItemsGenerator displayItemsGenerator = EMPTY_GENERATOR;
/*     */   
/*     */   private boolean canScroll = true;
/*     */   
/*     */   private boolean showTitle = true;
/*     */   
/*     */   private boolean alignedRight = false;
/*     */   
/* 147 */   private CreativeModeTab.Type type = CreativeModeTab.Type.CATEGORY;
/*     */   
/* 148 */   private Identifier backgroundTexture = CreativeModeTab.DEFAULT_BACKGROUND;
/*     */   
/*     */   public Builder(CreativeModeTab.Row row, int column) {
/* 151 */     this.row = row;
/* 152 */     this.column = column;
/*     */   }
/*     */   
/*     */   public Builder title(Component displayName) {
/* 156 */     this.displayName = displayName;
/* 157 */     return this;
/*     */   }
/*     */   
/*     */   public Builder icon(Supplier<ItemStack> iconGenerator) {
/* 161 */     this.iconGenerator = iconGenerator;
/* 162 */     return this;
/*     */   }
/*     */   
/*     */   public Builder displayItems(CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
/* 166 */     this.displayItemsGenerator = displayItemsGenerator;
/* 167 */     return this;
/*     */   }
/*     */   
/*     */   public Builder alignedRight() {
/* 171 */     this.alignedRight = true;
/* 172 */     return this;
/*     */   }
/*     */   
/*     */   public Builder hideTitle() {
/* 176 */     this.showTitle = false;
/* 177 */     return this;
/*     */   }
/*     */   
/*     */   public Builder noScrollBar() {
/* 181 */     this.canScroll = false;
/* 182 */     return this;
/*     */   }
/*     */   
/*     */   protected Builder type(CreativeModeTab.Type type) {
/* 186 */     this.type = type;
/* 187 */     return this;
/*     */   }
/*     */   
/*     */   public Builder backgroundTexture(Identifier backgroundTexture) {
/* 191 */     this.backgroundTexture = backgroundTexture;
/* 192 */     return this;
/*     */   }
/*     */   
/*     */   public CreativeModeTab build() {
/* 196 */     if ((this.type == CreativeModeTab.Type.HOTBAR || this.type == CreativeModeTab.Type.INVENTORY) && this.displayItemsGenerator != EMPTY_GENERATOR)
/* 197 */       throw new IllegalStateException("Special tabs can't have display items"); 
/* 200 */     CreativeModeTab tab = new CreativeModeTab(this.row, this.column, this.type, this.displayName, this.iconGenerator, this.displayItemsGenerator);
/* 201 */     tab.alignedRight = this.alignedRight;
/* 202 */     tab.showTitle = this.showTitle;
/* 203 */     tab.canScroll = this.canScroll;
/* 204 */     tab.backgroundTexture = this.backgroundTexture;
/* 205 */     return tab;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\CreativeModeTab$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */