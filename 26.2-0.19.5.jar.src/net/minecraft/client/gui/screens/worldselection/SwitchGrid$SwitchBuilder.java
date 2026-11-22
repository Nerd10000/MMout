/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ 
/*     */ public class SwitchBuilder {
/*     */   private final Component label;
/*     */   
/*     */   private final BooleanSupplier stateSupplier;
/*     */   
/*     */   private final Consumer<Boolean> onClicked;
/*     */   
/*     */   private Component info;
/*     */   
/*     */   private BooleanSupplier isActiveCondition;
/*     */   
/*     */   private final int buttonWidth;
/*     */   
/*     */   private SwitchBuilder(Component label, BooleanSupplier stateSupplier, Consumer<Boolean> onClicked, int buttonWidth) {
/* 114 */     this.label = label;
/* 115 */     this.stateSupplier = stateSupplier;
/* 116 */     this.onClicked = onClicked;
/* 117 */     this.buttonWidth = buttonWidth;
/*     */   }
/*     */   
/*     */   public SwitchBuilder withIsActiveCondition(BooleanSupplier isActiveCondition) {
/* 121 */     this.isActiveCondition = isActiveCondition;
/* 122 */     return this;
/*     */   }
/*     */   
/*     */   public SwitchBuilder withInfo(Component info) {
/* 126 */     this.info = info;
/* 127 */     return this;
/*     */   }
/*     */   
/*     */   private SwitchGrid.LabeledSwitch build(SwitchGrid.Builder switchGridBuilder, GridLayout gridLayout, int startColumn) {
/* 131 */     switchGridBuilder.increaseRow();
/* 132 */     StringWidget labelWidget = new StringWidget(this.label, (Minecraft.getInstance()).font);
/* 133 */     gridLayout.addChild((LayoutElement)labelWidget, switchGridBuilder.rowCount, startColumn, gridLayout.newCellSettings().align(0.0F, 0.5F).paddingLeft(switchGridBuilder.paddingLeft));
/* 135 */     Optional<SwitchGrid.InfoUnderneathSettings> infoUnderneath = switchGridBuilder.infoUnderneath;
/* 137 */     CycleButton.Builder<Boolean> buttonBuilder = CycleButton.onOffBuilder(this.stateSupplier.getAsBoolean());
/* 138 */     buttonBuilder.displayOnlyValue();
/* 140 */     boolean hasTooltip = (this.info != null && infoUnderneath.isEmpty());
/* 141 */     if (hasTooltip) {
/* 142 */       Tooltip tooltip = Tooltip.create(this.info);
/* 143 */       buttonBuilder.withTooltip(value -> tooltip);
/*     */     } 
/* 146 */     if (this.info != null && !hasTooltip) {
/* 147 */       buttonBuilder.withCustomNarration(button -> CommonComponents.joinForNarration(new Component[] { this.label, (Component)button.createDefaultNarrationMessage(), this.info }));
/*     */     } else {
/* 149 */       buttonBuilder.withCustomNarration(button -> CommonComponents.joinForNarration(new Component[] { this.label, (Component)button.createDefaultNarrationMessage() }));
/*     */     } 
/* 152 */     CycleButton<Boolean> button = buttonBuilder.create(0, 0, this.buttonWidth, 20, (Component)Component.empty(), (b, value) -> this.onClicked.accept(value));
/* 153 */     if (this.isActiveCondition != null)
/* 154 */       button.active = this.isActiveCondition.getAsBoolean(); 
/* 156 */     gridLayout.addChild((LayoutElement)button, switchGridBuilder.rowCount, startColumn + 1, gridLayout.newCellSettings().alignHorizontallyRight());
/* 158 */     if (this.info != null)
/* 159 */       infoUnderneath.ifPresent(infoUnderneathSettings -> {
/*     */             MutableComponent mutableComponent = this.info.copy().withStyle(ChatFormatting.GRAY);
/*     */             Font font = (Minecraft.getInstance()).font;
/*     */             MultiLineTextWidget infoWidget = new MultiLineTextWidget((Component)mutableComponent, font);
/*     */             infoWidget.setMaxWidth(switchGridBuilder.width - switchGridBuilder.paddingLeft - this.buttonWidth);
/*     */             infoWidget.setMaxRows(infoUnderneathSettings.maxInfoRows());
/*     */             switchGridBuilder.increaseRow();
/*     */             Objects.requireNonNull(font);
/*     */             int extraBottomPadding = infoUnderneathSettings.alwaysMaxHeight ? (9 * infoUnderneathSettings.maxInfoRows - infoWidget.getHeight()) : 0;
/*     */             gridLayout.addChild((LayoutElement)infoWidget, switchGridBuilder.rowCount, startColumn, gridLayout.newCellSettings().paddingTop(-switchGridBuilder.rowSpacing).paddingBottom(extraBottomPadding));
/*     */           }); 
/* 172 */     return new SwitchGrid.LabeledSwitch(button, this.stateSupplier, this.isActiveCondition);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\SwitchGrid$SwitchBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */