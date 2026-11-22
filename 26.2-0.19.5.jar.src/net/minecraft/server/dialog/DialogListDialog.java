/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class DialogListDialog extends Record implements ButtonListDialog {
/*    */   private final CommonDialogData common;
/*    */   
/*    */   private final HolderSet<Dialog> dialogs;
/*    */   
/*    */   private final Optional<ActionButton> exitAction;
/*    */   
/*    */   private final int columns;
/*    */   
/*    */   private final int buttonWidth;
/*    */   
/*    */   public static final MapCodec<DialogListDialog> MAP_CODEC;
/*    */   
/*    */   public DialogListDialog(CommonDialogData common, HolderSet<Dialog> dialogs, Optional<ActionButton> exitAction, int columns, int buttonWidth) {
/* 10 */     this.common = common;
/* 10 */     this.dialogs = dialogs;
/* 10 */     this.exitAction = exitAction;
/* 10 */     this.columns = columns;
/* 10 */     this.buttonWidth = buttonWidth;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/DialogListDialog;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/DialogListDialog;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/DialogListDialog;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/DialogListDialog;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/DialogListDialog;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/DialogListDialog;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public CommonDialogData common() {
/* 10 */     return this.common;
/*    */   }
/*    */   
/*    */   public HolderSet<Dialog> dialogs() {
/* 10 */     return this.dialogs;
/*    */   }
/*    */   
/*    */   public Optional<ActionButton> exitAction() {
/* 10 */     return this.exitAction;
/*    */   }
/*    */   
/*    */   public int columns() {
/* 10 */     return this.columns;
/*    */   }
/*    */   
/*    */   public int buttonWidth() {
/* 10 */     return this.buttonWidth;
/*    */   }
/*    */   
/*    */   static {
/* 18 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)CommonDialogData.MAP_CODEC.forGetter(DialogListDialog::common), (App)Dialog.LIST_CODEC.fieldOf("dialogs").forGetter(DialogListDialog::dialogs), (App)ActionButton.CODEC.optionalFieldOf("exit_action").forGetter(DialogListDialog::exitAction), (App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("columns", Integer.valueOf(2)).forGetter(DialogListDialog::columns), (App)WIDTH_CODEC.optionalFieldOf("button_width", Integer.valueOf(150)).forGetter(DialogListDialog::buttonWidth)).apply((Applicative)i, DialogListDialog::new));
/*    */   }
/*    */   
/*    */   public MapCodec<DialogListDialog> codec() {
/* 28 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\DialogListDialog.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */