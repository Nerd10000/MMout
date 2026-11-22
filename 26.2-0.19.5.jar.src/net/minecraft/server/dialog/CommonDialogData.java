/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function7;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.server.dialog.body.DialogBody;
/*    */ 
/*    */ public final class CommonDialogData extends Record {
/*    */   private final Component title;
/*    */   
/*    */   private final Optional<Component> externalTitle;
/*    */   
/*    */   private final boolean canCloseWithEscape;
/*    */   
/*    */   private final boolean pause;
/*    */   
/*    */   private final DialogAction afterAction;
/*    */   
/*    */   private final List<DialogBody> body;
/*    */   
/*    */   private final List<Input> inputs;
/*    */   
/*    */   public static final MapCodec<CommonDialogData> MAP_CODEC;
/*    */   
/*    */   public CommonDialogData(Component title, Optional<Component> externalTitle, boolean canCloseWithEscape, boolean pause, DialogAction afterAction, List<DialogBody> body, List<Input> inputs) {
/* 14 */     this.title = title;
/* 14 */     this.externalTitle = externalTitle;
/* 14 */     this.canCloseWithEscape = canCloseWithEscape;
/* 14 */     this.pause = pause;
/* 14 */     this.afterAction = afterAction;
/* 14 */     this.body = body;
/* 14 */     this.inputs = inputs;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/CommonDialogData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/CommonDialogData;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/CommonDialogData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/CommonDialogData;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/CommonDialogData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/CommonDialogData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Component title() {
/* 14 */     return this.title;
/*    */   }
/*    */   
/*    */   public Optional<Component> externalTitle() {
/* 14 */     return this.externalTitle;
/*    */   }
/*    */   
/*    */   public boolean canCloseWithEscape() {
/* 14 */     return this.canCloseWithEscape;
/*    */   }
/*    */   
/*    */   public boolean pause() {
/* 14 */     return this.pause;
/*    */   }
/*    */   
/*    */   public DialogAction afterAction() {
/* 14 */     return this.afterAction;
/*    */   }
/*    */   
/*    */   public List<DialogBody> body() {
/* 14 */     return this.body;
/*    */   }
/*    */   
/*    */   public List<Input> inputs() {
/* 14 */     return this.inputs;
/*    */   }
/*    */   
/*    */   static {
/* 31 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ComponentSerialization.CODEC.fieldOf("title").forGetter(CommonDialogData::title), (App)ComponentSerialization.CODEC.optionalFieldOf("external_title").forGetter(CommonDialogData::externalTitle), (App)Codec.BOOL.optionalFieldOf("can_close_with_escape", Boolean.valueOf(true)).forGetter(CommonDialogData::canCloseWithEscape), (App)Codec.BOOL.optionalFieldOf("pause", Boolean.valueOf(true)).forGetter(CommonDialogData::pause), (App)DialogAction.CODEC.optionalFieldOf("after_action", DialogAction.CLOSE).forGetter(CommonDialogData::afterAction), (App)DialogBody.COMPACT_LIST_CODEC.optionalFieldOf("body", List.of()).forGetter(CommonDialogData::body), (App)Input.CODEC.listOf().optionalFieldOf("inputs", List.of()).forGetter(CommonDialogData::inputs)).apply((Applicative)i, CommonDialogData::new)).validate(data -> 
/* 32 */         (data.pause && !data.afterAction.willUnpause()) ? DataResult.error(()) : DataResult.success(data));
/*    */   }
/*    */   
/*    */   public Component computeExternalTitle() {
/* 40 */     return this.externalTitle.orElse(this.title);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\CommonDialogData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */