/*     */ package net.minecraft.client.gui.screens.options.controls;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.KeyMapping;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ 
/*     */ public class KeyEntry extends KeyBindsList.Entry {
/*  99 */   private static final Component RESET_BUTTON_TITLE = (Component)Component.translatable("controls.reset");
/*     */   
/*     */   private static final int PADDING = 10;
/*     */   
/*     */   private final KeyMapping key;
/*     */   
/*     */   private final Component name;
/*     */   
/*     */   private final Button changeButton;
/*     */   
/*     */   private final Button resetButton;
/*     */   
/*     */   private boolean hasCollision = false;
/*     */   
/*     */   private KeyEntry(KeyMapping key, Component name) {
/* 109 */     this.key = key;
/* 110 */     this.name = name;
/* 112 */     this
/*     */       
/* 121 */       .changeButton = Button.builder(name, button -> {
/*     */           KeyBindsList.this.keyBindsScreen.selectedKey = key;
/*     */           KeyBindsList.this.resetMappingAndUpdateButtons();
/* 121 */         }).bounds(0, 0, 75, 20).createNarration(defaultNarrationSupplier -> key.isUnbound() ? Component.translatable("narrator.controls.unbound", new Object[] { name }) : Component.translatable("narrator.controls.bound", new Object[] { name, defaultNarrationSupplier.get() })).build();
/* 123 */     this
/*     */       
/* 126 */       .resetButton = Button.builder(RESET_BUTTON_TITLE, button -> {
/*     */           key.setKey(key.getDefaultKey());
/*     */           KeyBindsList.this.resetMappingAndUpdateButtons();
/* 126 */         }).bounds(0, 0, 50, 20).createNarration(defaultNarrationSupplier -> Component.translatable("narrator.controls.reset", new Object[] { name })).build();
/* 127 */     refreshEntry();
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 132 */     int resetButtonX = KeyBindsList.access$200(KeyBindsList.this) - this.resetButton.getWidth() - 10;
/* 133 */     int buttonY = getContentY() - 2;
/* 134 */     this.resetButton.setPosition(resetButtonX, buttonY);
/* 135 */     this.resetButton.extractRenderState(graphics, mouseX, mouseY, a);
/* 137 */     int changeButtonX = resetButtonX - 5 - this.changeButton.getWidth();
/* 138 */     this.changeButton.setPosition(changeButtonX, buttonY);
/* 139 */     this.changeButton.extractRenderState(graphics, mouseX, mouseY, a);
/* 141 */     Objects.requireNonNull((KeyBindsList.access$400(KeyBindsList.this)).font);
/* 141 */     graphics.text((KeyBindsList.access$300(KeyBindsList.this)).font, this.name, getContentX(), getContentYMiddle() - 9 / 2, -1);
/* 143 */     if (this.hasCollision) {
/* 144 */       int stripeWidth = 3;
/* 145 */       int stripeLeft = this.changeButton.getX() - 6;
/* 146 */       graphics.fill(stripeLeft, getContentY() - 1, stripeLeft + 3, getContentBottom(), -256);
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 152 */     return (List<? extends GuiEventListener>)ImmutableList.of(this.changeButton, this.resetButton);
/*     */   }
/*     */   
/*     */   public List<? extends NarratableEntry> narratables() {
/* 157 */     return (List<? extends NarratableEntry>)ImmutableList.of(this.changeButton, this.resetButton);
/*     */   }
/*     */   
/*     */   public void refreshEntry() {
/* 162 */     this.changeButton.setMessage(this.key.getTranslatedKeyMessage());
/* 163 */     this.resetButton.active = !this.key.isDefault();
/* 164 */     this.hasCollision = false;
/* 165 */     MutableComponent tooltip = Component.empty();
/* 167 */     if (!this.key.isUnbound())
/* 168 */       for (KeyMapping otherKey : (KeyBindsList.access$500(KeyBindsList.this)).options.keyMappings) {
/* 169 */         if (otherKey != this.key && this.key.same(otherKey) && (!otherKey.isDefault() || !this.key.isDefault())) {
/* 170 */           if (this.hasCollision)
/* 171 */             tooltip.append(", "); 
/* 173 */           this.hasCollision = true;
/* 174 */           tooltip.append((Component)Component.translatable(otherKey.getName()));
/*     */         } 
/*     */       }  
/* 179 */     if (this.hasCollision) {
/* 180 */       this.changeButton.setMessage((Component)Component.literal("[ ").append((Component)this.changeButton.getMessage().copy().withStyle(ChatFormatting.WHITE)).append(" ]").withStyle(ChatFormatting.YELLOW));
/* 181 */       this.changeButton.setTooltip(Tooltip.create((Component)Component.translatable("controls.keybinds.duplicateKeybinds", new Object[] { tooltip })));
/*     */     } else {
/* 183 */       this.changeButton.setTooltip(null);
/*     */     } 
/* 186 */     if (KeyBindsList.this.keyBindsScreen.selectedKey == this.key)
/* 187 */       this.changeButton.setMessage((Component)Component.literal("> ").append((Component)this.changeButton.getMessage().copy().withStyle(new ChatFormatting[] { ChatFormatting.WHITE, ChatFormatting.UNDERLINE })).append(" <").withStyle(ChatFormatting.YELLOW)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\options\controls\KeyBindsList$KeyEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */