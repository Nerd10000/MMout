/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.dialog.action.Action;
/*    */ 
/*    */ public final class ConfirmationDialog extends Record implements SimpleDialog {
/*    */   private final CommonDialogData common;
/*    */   
/*    */   private final ActionButton yesButton;
/*    */   
/*    */   private final ActionButton noButton;
/*    */   
/*    */   public static final MapCodec<ConfirmationDialog> MAP_CODEC;
/*    */   
/*    */   public ConfirmationDialog(CommonDialogData common, ActionButton yesButton, ActionButton noButton) {
/* 10 */     this.common = common;
/* 10 */     this.yesButton = yesButton;
/* 10 */     this.noButton = noButton;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/ConfirmationDialog;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/ConfirmationDialog;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/ConfirmationDialog;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/ConfirmationDialog;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/ConfirmationDialog;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/ConfirmationDialog;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public CommonDialogData common() {
/* 10 */     return this.common;
/*    */   }
/*    */   
/*    */   public ActionButton yesButton() {
/* 10 */     return this.yesButton;
/*    */   }
/*    */   
/*    */   public ActionButton noButton() {
/* 10 */     return this.noButton;
/*    */   }
/*    */   
/*    */   static {
/* 15 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)CommonDialogData.MAP_CODEC.forGetter(ConfirmationDialog::common), (App)ActionButton.CODEC.fieldOf("yes").forGetter(ConfirmationDialog::yesButton), (App)ActionButton.CODEC.fieldOf("no").forGetter(ConfirmationDialog::noButton)).apply((Applicative)i, ConfirmationDialog::new));
/*    */   }
/*    */   
/*    */   public MapCodec<ConfirmationDialog> codec() {
/* 23 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public Optional<Action> onCancel() {
/* 28 */     return this.noButton.action();
/*    */   }
/*    */   
/*    */   public List<ActionButton> mainActions() {
/* 33 */     return List.of(this.yesButton, this.noButton);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\ConfirmationDialog.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */