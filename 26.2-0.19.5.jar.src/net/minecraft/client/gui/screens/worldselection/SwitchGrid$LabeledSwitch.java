/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import java.util.function.BooleanSupplier;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ 
/*     */ final class LabeledSwitch extends Record {
/*     */   private final CycleButton<Boolean> button;
/*     */   
/*     */   private final BooleanSupplier stateSupplier;
/*     */   
/*     */   private final BooleanSupplier isActiveCondition;
/*     */   
/*     */   private LabeledSwitch(CycleButton<Boolean> button, BooleanSupplier stateSupplier, BooleanSupplier isActiveCondition) {
/* 176 */     this.button = button;
/* 176 */     this.stateSupplier = stateSupplier;
/* 176 */     this.isActiveCondition = isActiveCondition;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$LabeledSwitch;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #176	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$LabeledSwitch;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$LabeledSwitch;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #176	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$LabeledSwitch;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$LabeledSwitch;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #176	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$LabeledSwitch;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public CycleButton<Boolean> button() {
/* 176 */     return this.button;
/*     */   }
/*     */   
/*     */   public BooleanSupplier stateSupplier() {
/* 176 */     return this.stateSupplier;
/*     */   }
/*     */   
/*     */   public BooleanSupplier isActiveCondition() {
/* 176 */     return this.isActiveCondition;
/*     */   }
/*     */   
/*     */   public void refreshState() {
/* 178 */     this.button.setValue(Boolean.valueOf(this.stateSupplier.getAsBoolean()));
/* 179 */     if (this.isActiveCondition != null)
/* 180 */       this.button.active = this.isActiveCondition.getAsBoolean(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\SwitchGrid$LabeledSwitch.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */