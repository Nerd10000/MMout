/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class SuggestCommand extends Record implements ClickEvent {
/*    */   private final String command;
/*    */   
/*    */   public static final MapCodec<SuggestCommand> CODEC;
/*    */   
/*    */   public SuggestCommand(String command) {
/* 71 */     this.command = command;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$SuggestCommand;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #71	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$SuggestCommand;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$SuggestCommand;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #71	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$SuggestCommand;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$SuggestCommand;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #71	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$SuggestCommand;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String command() {
/* 71 */     return this.command;
/*    */   }
/*    */   
/*    */   static {
/* 72 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.CHAT_STRING.fieldOf("command").forGetter(SuggestCommand::command)).apply((Applicative)i, SuggestCommand::new));
/*    */   }
/*    */   
/*    */   public ClickEvent.Action action() {
/* 78 */     return ClickEvent.Action.SUGGEST_COMMAND;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\ClickEvent$SuggestCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */