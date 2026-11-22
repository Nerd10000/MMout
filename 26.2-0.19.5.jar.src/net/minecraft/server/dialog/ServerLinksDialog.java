/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class ServerLinksDialog extends Record implements ButtonListDialog {
/*    */   private final CommonDialogData common;
/*    */   
/*    */   private final Optional<ActionButton> exitAction;
/*    */   
/*    */   private final int columns;
/*    */   
/*    */   private final int buttonWidth;
/*    */   
/*    */   public static final MapCodec<ServerLinksDialog> MAP_CODEC;
/*    */   
/*    */   public ServerLinksDialog(CommonDialogData common, Optional<ActionButton> exitAction, int columns, int buttonWidth) {
/*  9 */     this.common = common;
/*  9 */     this.exitAction = exitAction;
/*  9 */     this.columns = columns;
/*  9 */     this.buttonWidth = buttonWidth;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/ServerLinksDialog;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/ServerLinksDialog;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/ServerLinksDialog;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/ServerLinksDialog;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/ServerLinksDialog;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/ServerLinksDialog;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public CommonDialogData common() {
/*  9 */     return this.common;
/*    */   }
/*    */   
/*    */   public Optional<ActionButton> exitAction() {
/*  9 */     return this.exitAction;
/*    */   }
/*    */   
/*    */   public int columns() {
/*  9 */     return this.columns;
/*    */   }
/*    */   
/*    */   public int buttonWidth() {
/*  9 */     return this.buttonWidth;
/*    */   }
/*    */   
/*    */   static {
/* 16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)CommonDialogData.MAP_CODEC.forGetter(ServerLinksDialog::common), (App)ActionButton.CODEC.optionalFieldOf("exit_action").forGetter(ServerLinksDialog::exitAction), (App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("columns", Integer.valueOf(2)).forGetter(ServerLinksDialog::columns), (App)WIDTH_CODEC.optionalFieldOf("button_width", Integer.valueOf(150)).forGetter(ServerLinksDialog::buttonWidth)).apply((Applicative)i, ServerLinksDialog::new));
/*    */   }
/*    */   
/*    */   public MapCodec<ServerLinksDialog> codec() {
/* 25 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\ServerLinksDialog.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */