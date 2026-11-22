/*    */ package net.minecraft.server.dialog.action;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ 
/*    */ public final class CommandTemplate extends Record implements Action {
/*    */   private final ParsedTemplate template;
/*    */   
/*    */   public static final MapCodec<CommandTemplate> MAP_CODEC;
/*    */   
/*    */   public CommandTemplate(ParsedTemplate template) {
/* 10 */     this.template = template;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/action/CommandTemplate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/action/CommandTemplate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/action/CommandTemplate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/action/CommandTemplate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/action/CommandTemplate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/action/CommandTemplate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ParsedTemplate template() {
/* 10 */     return this.template;
/*    */   }
/*    */   
/*    */   static {
/* 13 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ParsedTemplate.CODEC.fieldOf("template").forGetter(CommandTemplate::template)).apply((Applicative)i, CommandTemplate::new));
/*    */   }
/*    */   
/*    */   public MapCodec<CommandTemplate> codec() {
/* 19 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public Optional<ClickEvent> createAction(Map<String, Action.ValueGetter> parameters) {
/* 24 */     String command = this.template.instantiate(Action.ValueGetter.getAsTemplateSubstitutions(parameters));
/* 25 */     return (Optional)Optional.of(new ClickEvent.RunCommand(command));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\action\CommandTemplate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */