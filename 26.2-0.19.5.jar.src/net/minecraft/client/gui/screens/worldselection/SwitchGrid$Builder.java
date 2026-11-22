/*    */ package net.minecraft.client.gui.screens.worldselection;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BooleanSupplier;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.gui.layouts.GridLayout;
/*    */ import net.minecraft.client.gui.layouts.Layout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.SpacerElement;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class Builder {
/*    */   private final int width;
/*    */   
/* 31 */   private final List<SwitchGrid.SwitchBuilder> switchBuilders = new ArrayList<>();
/*    */   
/*    */   private int paddingLeft;
/*    */   
/* 33 */   private int rowSpacing = 4;
/*    */   
/*    */   private int rowCount;
/*    */   
/* 35 */   private Optional<SwitchGrid.InfoUnderneathSettings> infoUnderneath = Optional.empty();
/*    */   
/*    */   public Builder(int width) {
/* 38 */     this.width = width;
/*    */   }
/*    */   
/*    */   private void increaseRow() {
/* 42 */     this.rowCount++;
/*    */   }
/*    */   
/*    */   public SwitchGrid.SwitchBuilder addSwitch(Component label, BooleanSupplier stateSupplier, Consumer<Boolean> onClicked) {
/* 46 */     SwitchGrid.SwitchBuilder switchBuilder = new SwitchGrid.SwitchBuilder(label, stateSupplier, onClicked, 44);
/* 47 */     this.switchBuilders.add(switchBuilder);
/* 48 */     return switchBuilder;
/*    */   }
/*    */   
/*    */   public Builder withPaddingLeft(int paddingLeft) {
/* 52 */     this.paddingLeft = paddingLeft;
/* 53 */     return this;
/*    */   }
/*    */   
/*    */   public Builder withRowSpacing(int rowSpacing) {
/* 57 */     this.rowSpacing = rowSpacing;
/* 58 */     return this;
/*    */   }
/*    */   
/*    */   public SwitchGrid build() {
/* 62 */     GridLayout switchGrid = (new GridLayout()).rowSpacing(this.rowSpacing);
/* 64 */     switchGrid.addChild((LayoutElement)SpacerElement.width(this.width - 44), 0, 0);
/* 65 */     switchGrid.addChild((LayoutElement)SpacerElement.width(44), 0, 1);
/* 67 */     List<SwitchGrid.LabeledSwitch> switches = new ArrayList<>();
/* 68 */     this.rowCount = 0;
/* 69 */     for (SwitchGrid.SwitchBuilder switchBuilder : this.switchBuilders)
/* 70 */       switches.add(switchBuilder.build(this, switchGrid, 0)); 
/* 73 */     switchGrid.arrangeElements();
/* 74 */     SwitchGrid result = new SwitchGrid(switches, (Layout)switchGrid);
/* 75 */     result.refreshStates();
/* 76 */     return result;
/*    */   }
/*    */   
/*    */   public Builder withInfoUnderneath(int maxRows, boolean alwaysMaxHeight) {
/* 80 */     this.infoUnderneath = Optional.of(new SwitchGrid.InfoUnderneathSettings(maxRows, alwaysMaxHeight));
/* 81 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\SwitchGrid$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */