/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ import net.minecraft.client.gui.layouts.Layout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.SpacerElement;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ 
/*     */ class SwitchGrid {
/*     */   private static final int DEFAULT_SWITCH_BUTTON_WIDTH = 44;
/*     */   
/*     */   private final List<LabeledSwitch> switches;
/*     */   
/*     */   private final Layout layout;
/*     */   
/*     */   public static class Builder {
/*     */     private final int width;
/*     */     
/*  31 */     private final List<SwitchGrid.SwitchBuilder> switchBuilders = new ArrayList<>();
/*     */     
/*     */     private int paddingLeft;
/*     */     
/*  33 */     private int rowSpacing = 4;
/*     */     
/*     */     private int rowCount;
/*     */     
/*  35 */     private Optional<SwitchGrid.InfoUnderneathSettings> infoUnderneath = Optional.empty();
/*     */     
/*     */     public Builder(int width) {
/*  38 */       this.width = width;
/*     */     }
/*     */     
/*     */     private void increaseRow() {
/*  42 */       this.rowCount++;
/*     */     }
/*     */     
/*     */     public SwitchGrid.SwitchBuilder addSwitch(Component label, BooleanSupplier stateSupplier, Consumer<Boolean> onClicked) {
/*  46 */       SwitchGrid.SwitchBuilder switchBuilder = new SwitchGrid.SwitchBuilder(label, stateSupplier, onClicked, 44);
/*  47 */       this.switchBuilders.add(switchBuilder);
/*  48 */       return switchBuilder;
/*     */     }
/*     */     
/*     */     public Builder withPaddingLeft(int paddingLeft) {
/*  52 */       this.paddingLeft = paddingLeft;
/*  53 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withRowSpacing(int rowSpacing) {
/*  57 */       this.rowSpacing = rowSpacing;
/*  58 */       return this;
/*     */     }
/*     */     
/*     */     public SwitchGrid build() {
/*  62 */       GridLayout switchGrid = (new GridLayout()).rowSpacing(this.rowSpacing);
/*  64 */       switchGrid.addChild((LayoutElement)SpacerElement.width(this.width - 44), 0, 0);
/*  65 */       switchGrid.addChild((LayoutElement)SpacerElement.width(44), 0, 1);
/*  67 */       List<SwitchGrid.LabeledSwitch> switches = new ArrayList<>();
/*  68 */       this.rowCount = 0;
/*  69 */       for (SwitchGrid.SwitchBuilder switchBuilder : this.switchBuilders)
/*  70 */         switches.add(switchBuilder.build(this, switchGrid, 0)); 
/*  73 */       switchGrid.arrangeElements();
/*  74 */       SwitchGrid result = new SwitchGrid(switches, (Layout)switchGrid);
/*  75 */       result.refreshStates();
/*  76 */       return result;
/*     */     }
/*     */     
/*     */     public Builder withInfoUnderneath(int maxRows, boolean alwaysMaxHeight) {
/*  80 */       this.infoUnderneath = Optional.of(new SwitchGrid.InfoUnderneathSettings(maxRows, alwaysMaxHeight));
/*  81 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   private SwitchGrid(List<LabeledSwitch> switches, Layout layout) {
/*  89 */     this.switches = switches;
/*  90 */     this.layout = layout;
/*     */   }
/*     */   
/*     */   public Layout layout() {
/*  94 */     return this.layout;
/*     */   }
/*     */   
/*     */   public void refreshStates() {
/*  98 */     this.switches.forEach(LabeledSwitch::refreshState);
/*     */   }
/*     */   
/*     */   public static Builder builder(int width) {
/* 102 */     return new Builder(width);
/*     */   }
/*     */   
/*     */   public static class SwitchBuilder {
/*     */     private final Component label;
/*     */     
/*     */     private final BooleanSupplier stateSupplier;
/*     */     
/*     */     private final Consumer<Boolean> onClicked;
/*     */     
/*     */     private Component info;
/*     */     
/*     */     private BooleanSupplier isActiveCondition;
/*     */     
/*     */     private final int buttonWidth;
/*     */     
/*     */     private SwitchBuilder(Component label, BooleanSupplier stateSupplier, Consumer<Boolean> onClicked, int buttonWidth) {
/* 114 */       this.label = label;
/* 115 */       this.stateSupplier = stateSupplier;
/* 116 */       this.onClicked = onClicked;
/* 117 */       this.buttonWidth = buttonWidth;
/*     */     }
/*     */     
/*     */     public SwitchBuilder withIsActiveCondition(BooleanSupplier isActiveCondition) {
/* 121 */       this.isActiveCondition = isActiveCondition;
/* 122 */       return this;
/*     */     }
/*     */     
/*     */     public SwitchBuilder withInfo(Component info) {
/* 126 */       this.info = info;
/* 127 */       return this;
/*     */     }
/*     */     
/*     */     private SwitchGrid.LabeledSwitch build(SwitchGrid.Builder switchGridBuilder, GridLayout gridLayout, int startColumn) {
/* 131 */       switchGridBuilder.increaseRow();
/* 132 */       StringWidget labelWidget = new StringWidget(this.label, (Minecraft.getInstance()).font);
/* 133 */       gridLayout.addChild((LayoutElement)labelWidget, switchGridBuilder.rowCount, startColumn, gridLayout.newCellSettings().align(0.0F, 0.5F).paddingLeft(switchGridBuilder.paddingLeft));
/* 135 */       Optional<SwitchGrid.InfoUnderneathSettings> infoUnderneath = switchGridBuilder.infoUnderneath;
/* 137 */       CycleButton.Builder<Boolean> buttonBuilder = CycleButton.onOffBuilder(this.stateSupplier.getAsBoolean());
/* 138 */       buttonBuilder.displayOnlyValue();
/* 140 */       boolean hasTooltip = (this.info != null && infoUnderneath.isEmpty());
/* 141 */       if (hasTooltip) {
/* 142 */         Tooltip tooltip = Tooltip.create(this.info);
/* 143 */         buttonBuilder.withTooltip(value -> tooltip);
/*     */       } 
/* 146 */       if (this.info != null && !hasTooltip) {
/* 147 */         buttonBuilder.withCustomNarration(button -> CommonComponents.joinForNarration(new Component[] { this.label, (Component)button.createDefaultNarrationMessage(), this.info }));
/*     */       } else {
/* 149 */         buttonBuilder.withCustomNarration(button -> CommonComponents.joinForNarration(new Component[] { this.label, (Component)button.createDefaultNarrationMessage() }));
/*     */       } 
/* 152 */       CycleButton<Boolean> button = buttonBuilder.create(0, 0, this.buttonWidth, 20, (Component)Component.empty(), (b, value) -> this.onClicked.accept(value));
/* 153 */       if (this.isActiveCondition != null)
/* 154 */         button.active = this.isActiveCondition.getAsBoolean(); 
/* 156 */       gridLayout.addChild((LayoutElement)button, switchGridBuilder.rowCount, startColumn + 1, gridLayout.newCellSettings().alignHorizontallyRight());
/* 158 */       if (this.info != null)
/* 159 */         infoUnderneath.ifPresent(infoUnderneathSettings -> {
/*     */               MutableComponent mutableComponent = this.info.copy().withStyle(ChatFormatting.GRAY);
/*     */               Font font = (Minecraft.getInstance()).font;
/*     */               MultiLineTextWidget infoWidget = new MultiLineTextWidget((Component)mutableComponent, font);
/*     */               infoWidget.setMaxWidth(switchGridBuilder.width - switchGridBuilder.paddingLeft - this.buttonWidth);
/*     */               infoWidget.setMaxRows(infoUnderneathSettings.maxInfoRows());
/*     */               switchGridBuilder.increaseRow();
/*     */               Objects.requireNonNull(font);
/*     */               int extraBottomPadding = infoUnderneathSettings.alwaysMaxHeight ? (9 * infoUnderneathSettings.maxInfoRows - infoWidget.getHeight()) : 0;
/*     */               gridLayout.addChild((LayoutElement)infoWidget, switchGridBuilder.rowCount, startColumn, gridLayout.newCellSettings().paddingTop(-switchGridBuilder.rowSpacing).paddingBottom(extraBottomPadding));
/*     */             }); 
/* 172 */       return new SwitchGrid.LabeledSwitch(button, this.stateSupplier, this.isActiveCondition);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class LabeledSwitch extends Record {
/*     */     private final CycleButton<Boolean> button;
/*     */     
/*     */     private final BooleanSupplier stateSupplier;
/*     */     
/*     */     private final BooleanSupplier isActiveCondition;
/*     */     
/*     */     private LabeledSwitch(CycleButton<Boolean> button, BooleanSupplier stateSupplier, BooleanSupplier isActiveCondition) {
/* 176 */       this.button = button;
/* 176 */       this.stateSupplier = stateSupplier;
/* 176 */       this.isActiveCondition = isActiveCondition;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$LabeledSwitch;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #176	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$LabeledSwitch;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$LabeledSwitch;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #176	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$LabeledSwitch;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$LabeledSwitch;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #176	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$LabeledSwitch;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public CycleButton<Boolean> button() {
/* 176 */       return this.button;
/*     */     }
/*     */     
/*     */     public BooleanSupplier stateSupplier() {
/* 176 */       return this.stateSupplier;
/*     */     }
/*     */     
/*     */     public BooleanSupplier isActiveCondition() {
/* 176 */       return this.isActiveCondition;
/*     */     }
/*     */     
/*     */     public void refreshState() {
/* 178 */       this.button.setValue(Boolean.valueOf(this.stateSupplier.getAsBoolean()));
/* 179 */       if (this.isActiveCondition != null)
/* 180 */         this.button.active = this.isActiveCondition.getAsBoolean(); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class InfoUnderneathSettings extends Record {
/*     */     private final int maxInfoRows;
/*     */     
/*     */     private final boolean alwaysMaxHeight;
/*     */     
/*     */     private InfoUnderneathSettings(int maxInfoRows, boolean alwaysMaxHeight) {
/* 185 */       this.maxInfoRows = maxInfoRows;
/* 185 */       this.alwaysMaxHeight = alwaysMaxHeight;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$InfoUnderneathSettings;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #185	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$InfoUnderneathSettings;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$InfoUnderneathSettings;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #185	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$InfoUnderneathSettings;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$InfoUnderneathSettings;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #185	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$InfoUnderneathSettings;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int maxInfoRows() {
/* 185 */       return this.maxInfoRows;
/*     */     }
/*     */     
/*     */     public boolean alwaysMaxHeight() {
/* 185 */       return this.alwaysMaxHeight;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\SwitchGrid.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */