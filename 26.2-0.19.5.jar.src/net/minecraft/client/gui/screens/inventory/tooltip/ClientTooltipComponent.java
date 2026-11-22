/*    */ package net.minecraft.client.gui.screens.inventory.tooltip;
/*    */ 
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ import net.minecraft.world.inventory.tooltip.TooltipComponent;
/*    */ 
/*    */ public interface ClientTooltipComponent {
/*    */   static ClientTooltipComponent create(FormattedCharSequence charSequence) {
/* 12 */     return new ClientTextTooltip(charSequence);
/*    */   }
/*    */   
/*    */   static ClientTooltipComponent create(TooltipComponent component) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: dup
/*    */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*    */     //   5: pop
/*    */     //   6: astore_1
/*    */     //   7: iconst_0
/*    */     //   8: istore_2
/*    */     //   9: aload_1
/*    */     //   10: iload_2
/*    */     //   11: <illegal opcode> typeSwitch : (Lnet/minecraft/world/inventory/tooltip/TooltipComponent;I)I
/*    */     //   16: lookupswitch default -> 81, 0 -> 44, 1 -> 63
/*    */     //   44: aload_1
/*    */     //   45: checkcast net/minecraft/world/inventory/tooltip/BundleTooltip
/*    */     //   48: astore_3
/*    */     //   49: new net/minecraft/client/gui/screens/inventory/tooltip/ClientBundleTooltip
/*    */     //   52: dup
/*    */     //   53: aload_3
/*    */     //   54: invokevirtual contents : ()Lnet/minecraft/world/item/component/BundleContents;
/*    */     //   57: invokespecial <init> : (Lnet/minecraft/world/item/component/BundleContents;)V
/*    */     //   60: goto -> 91
/*    */     //   63: aload_1
/*    */     //   64: checkcast net/minecraft/client/gui/screens/inventory/tooltip/ClientActivePlayersTooltip$ActivePlayersTooltip
/*    */     //   67: astore #4
/*    */     //   69: new net/minecraft/client/gui/screens/inventory/tooltip/ClientActivePlayersTooltip
/*    */     //   72: dup
/*    */     //   73: aload #4
/*    */     //   75: invokespecial <init> : (Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientActivePlayersTooltip$ActivePlayersTooltip;)V
/*    */     //   78: goto -> 91
/*    */     //   81: new java/lang/IllegalArgumentException
/*    */     //   84: dup
/*    */     //   85: ldc 'Unknown TooltipComponent'
/*    */     //   87: invokespecial <init> : (Ljava/lang/String;)V
/*    */     //   90: athrow
/*    */     //   91: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     //   #17	-> 44
/*    */     //   #18	-> 63
/*    */     //   #19	-> 81
/*    */     //   #16	-> 91
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   49	14	3	bundleTooltip	Lnet/minecraft/world/inventory/tooltip/BundleTooltip;
/*    */     //   69	12	4	activePlayersTooltip	Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientActivePlayersTooltip$ActivePlayersTooltip;
/*    */     //   0	92	0	component	Lnet/minecraft/world/inventory/tooltip/TooltipComponent;
/*    */   }
/*    */   
/*    */   int getHeight(Font paramFont);
/*    */   
/*    */   int getWidth(Font paramFont);
/*    */   
/*    */   default boolean showTooltipWithItemInHand() {
/* 28 */     return false;
/*    */   }
/*    */   
/*    */   default void extractText(GuiGraphicsExtractor graphics, Font font, int x, int y) {}
/*    */   
/*    */   default void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\tooltip\ClientTooltipComponent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */