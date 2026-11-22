/*    */ package net.minecraft.client.gui.screens.options;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.components.CycleButton;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.player.PlayerModelPart;
/*    */ 
/*    */ public class SkinCustomizationScreen extends OptionsSubScreen {
/* 14 */   private static final Component TITLE = (Component)Component.translatable("options.skinCustomisation.title");
/*    */   
/*    */   public SkinCustomizationScreen(Screen lastScreen, Options options) {
/* 17 */     super(lastScreen, options, TITLE);
/*    */   }
/*    */   
/*    */   protected void addOptions() {
/* 22 */     List<AbstractWidget> widgets = new ArrayList<>();
/* 23 */     for (PlayerModelPart part : PlayerModelPart.values())
/* 24 */       widgets.add(CycleButton.onOffBuilder(this.options.isModelPartEnabled(part)).create(part.getName(), (button, value) -> this.options.setModelPart(part, value.booleanValue()))); 
/* 28 */     widgets.add(this.options.mainHand().createButton(this.options));
/* 29 */     this.list.addSmall(widgets);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\options\SkinCustomizationScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */