/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.dialog.Dialog;
/*    */ 
/*    */ public final class ShowDialog extends Record implements ClickEvent {
/*    */   private final Holder<Dialog> dialog;
/*    */   
/*    */   public static final MapCodec<ShowDialog> CODEC;
/*    */   
/*    */   public ShowDialog(Holder<Dialog> dialog) {
/* 82 */     this.dialog = dialog;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$ShowDialog;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #82	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$ShowDialog;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$ShowDialog;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #82	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$ShowDialog;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$ShowDialog;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #82	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$ShowDialog;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<Dialog> dialog() {
/* 82 */     return this.dialog;
/*    */   }
/*    */   
/*    */   static {
/* 83 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Dialog.CODEC.fieldOf("dialog").forGetter(ShowDialog::dialog)).apply((Applicative)i, ShowDialog::new));
/*    */   }
/*    */   
/*    */   public ClickEvent.Action action() {
/* 89 */     return ClickEvent.Action.SHOW_DIALOG;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\ClickEvent$ShowDialog.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */