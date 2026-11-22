/*     */ package net.minecraft.client.gui.screens.debug;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntries;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntry;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntryStatus;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class OptionEntry extends DebugOptionsScreen.AbstractOptionEntry {
/*     */   private static final int BUTTON_WIDTH = 60;
/*     */   
/*     */   private final Identifier location;
/*     */   
/* 228 */   protected final List<AbstractWidget> children = Lists.newArrayList();
/*     */   
/*     */   private final CycleButton<Boolean> always;
/*     */   
/*     */   private final CycleButton<Boolean> overlay;
/*     */   
/*     */   private final CycleButton<Boolean> never;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final boolean isAllowed;
/*     */   
/*     */   public OptionEntry(Identifier location) {
/* 236 */     this.location = location;
/* 237 */     DebugScreenEntry entry = DebugScreenEntries.getEntry(location);
/* 238 */     this.isAllowed = (entry != null && entry.isAllowed(DebugOptionsScreen.access$100(paramDebugOptionsScreen).showOnlyReducedInfo()));
/* 239 */     String name = location.getPath();
/* 241 */     if (this.isAllowed) {
/* 242 */       this.name = name;
/*     */     } else {
/* 244 */       this.name = String.valueOf(ChatFormatting.ITALIC) + String.valueOf(ChatFormatting.ITALIC);
/*     */     } 
/* 247 */     this
/*     */       
/* 250 */       .always = CycleButton.booleanBuilder((Component)DebugOptionsScreen.ENABLED_TEXT.copy().withColor(-2142128), (Component)DebugOptionsScreen.ENABLED_TEXT.copy().withColor(-4539718), false).displayOnlyValue().withCustomNarration(this::narrateButton).create(10, 5, 60, 16, (Component)Component.literal(name), (button, newValue) -> setValue(location, DebugScreenEntryStatus.ALWAYS_ON));
/* 251 */     this
/*     */       
/* 254 */       .overlay = CycleButton.booleanBuilder((Component)DebugOptionsScreen.IN_OVERLAY_TEXT.copy().withColor(-171), (Component)DebugOptionsScreen.IN_OVERLAY_TEXT.copy().withColor(-4539718), false).displayOnlyValue().withCustomNarration(this::narrateButton).create(10, 5, 60, 16, (Component)Component.literal(name), (button, newValue) -> setValue(location, DebugScreenEntryStatus.IN_OVERLAY));
/* 255 */     this
/*     */       
/* 258 */       .never = CycleButton.booleanBuilder((Component)DebugOptionsScreen.DISABLED_TEXT.copy().withColor(-1), (Component)DebugOptionsScreen.DISABLED_TEXT.copy().withColor(-4539718), false).displayOnlyValue().withCustomNarration(this::narrateButton).create(10, 5, 60, 16, (Component)Component.literal(name), (button, newValue) -> setValue(location, DebugScreenEntryStatus.NEVER));
/* 259 */     this.children.add(this.never);
/* 260 */     this.children.add(this.overlay);
/* 261 */     this.children.add(this.always);
/* 263 */     refreshEntry();
/*     */   }
/*     */   
/*     */   private MutableComponent narrateButton(CycleButton<Boolean> booleanCycleButton) {
/* 267 */     DebugScreenEntryStatus status = (DebugOptionsScreen.access$200(DebugOptionsScreen.this)).debugEntries.getStatus(this.location);
/* 268 */     MutableComponent current = Component.translatable("debug.entry.currently." + status.getSerializedName(), new Object[] { this.name });
/* 269 */     return CommonComponents.optionNameValue((Component)current, booleanCycleButton.getMessage());
/*     */   }
/*     */   
/*     */   private void setValue(Identifier location, DebugScreenEntryStatus never) {
/* 273 */     (DebugOptionsScreen.access$300(DebugOptionsScreen.this)).debugEntries.setStatus(location, never);
/* 274 */     for (Button profileButton : DebugOptionsScreen.this.profileButtons)
/* 275 */       profileButton.active = true; 
/* 277 */     refreshEntry();
/*     */   }
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 282 */     return (List)this.children;
/*     */   }
/*     */   
/*     */   public List<? extends NarratableEntry> narratables() {
/* 287 */     return (List)this.children;
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 292 */     int x = getContentX();
/* 293 */     int y = getContentY();
/* 294 */     graphics.text((DebugOptionsScreen.access$400(DebugOptionsScreen.this)).font, this.name, x, y + 5, this.isAllowed ? -1 : -8355712);
/* 296 */     int buttonsStartX = x + getContentWidth() - this.never.getWidth() - this.overlay.getWidth() - this.always.getWidth();
/* 298 */     if (!this.isAllowed && hovered && mouseX < buttonsStartX)
/* 299 */       graphics.setTooltipForNextFrame(DebugOptionsScreen.NOT_ALLOWED_TOOLTIP, mouseX, mouseY); 
/* 302 */     this.never.setX(buttonsStartX);
/* 303 */     this.overlay.setX(this.never.getX() + this.never.getWidth());
/* 304 */     this.always.setX(this.overlay.getX() + this.overlay.getWidth());
/* 306 */     this.always.setY(y);
/* 307 */     this.overlay.setY(y);
/* 308 */     this.never.setY(y);
/* 310 */     this.always.extractRenderState(graphics, mouseX, mouseY, a);
/* 311 */     this.overlay.extractRenderState(graphics, mouseX, mouseY, a);
/* 312 */     this.never.extractRenderState(graphics, mouseX, mouseY, a);
/*     */   }
/*     */   
/*     */   public void refreshEntry() {
/* 317 */     DebugScreenEntryStatus status = (DebugOptionsScreen.access$500(DebugOptionsScreen.this)).debugEntries.getStatus(this.location);
/* 318 */     this.always.setValue(Boolean.valueOf((status == DebugScreenEntryStatus.ALWAYS_ON)));
/* 319 */     this.overlay.setValue(Boolean.valueOf((status == DebugScreenEntryStatus.IN_OVERLAY)));
/* 320 */     this.never.setValue(Boolean.valueOf((status == DebugScreenEntryStatus.NEVER)));
/* 321 */     this.always.active = !((Boolean)this.always.getValue()).booleanValue();
/* 322 */     this.overlay.active = !((Boolean)this.overlay.getValue()).booleanValue();
/* 323 */     this.never.active = !((Boolean)this.never.getValue()).booleanValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\debug\DebugOptionsScreen$OptionEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */