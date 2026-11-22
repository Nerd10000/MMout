/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.server.dialog.action.Action;
/*    */ 
/*    */ public final class NoticeDialog extends Record implements SimpleDialog {
/*    */   private final CommonDialogData common;
/*    */   
/*    */   private final ActionButton action;
/*    */   
/*    */   public NoticeDialog(CommonDialogData common, ActionButton action) {
/* 11 */     this.common = common;
/* 11 */     this.action = action;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/NoticeDialog;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/NoticeDialog;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/NoticeDialog;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/NoticeDialog;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/NoticeDialog;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/NoticeDialog;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public CommonDialogData common() {
/* 11 */     return this.common;
/*    */   }
/*    */   
/*    */   public ActionButton action() {
/* 11 */     return this.action;
/*    */   }
/*    */   
/* 15 */   public static final ActionButton DEFAULT_ACTION = new ActionButton(new CommonButtonData(CommonComponents.GUI_OK, 150), 
/*    */       
/* 20 */       Optional.empty());
/*    */   
/*    */   public static final MapCodec<NoticeDialog> MAP_CODEC;
/*    */   
/*    */   static {
/* 23 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)CommonDialogData.MAP_CODEC.forGetter(NoticeDialog::common), (App)ActionButton.CODEC.optionalFieldOf("action", DEFAULT_ACTION).forGetter(NoticeDialog::action)).apply((Applicative)i, NoticeDialog::new));
/*    */   }
/*    */   
/*    */   public MapCodec<NoticeDialog> codec() {
/* 30 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public Optional<Action> onCancel() {
/* 35 */     return this.action.action();
/*    */   }
/*    */   
/*    */   public List<ActionButton> mainActions() {
/* 40 */     return List.of(this.action);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\NoticeDialog.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */