/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.brigadier.ParseResults;
/*    */ import com.mojang.brigadier.context.CommandContextBuilder;
/*    */ import com.mojang.brigadier.context.ParsedArgument;
/*    */ import com.mojang.brigadier.tree.ArgumentCommandNode;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.ArgumentVisitor;
/*    */ 
/*    */ public final class SignableCommand<S> extends Record {
/*    */   private final List<Argument<S>> arguments;
/*    */   
/*    */   public SignableCommand(List<Argument<S>> arguments) {
/* 14 */     this.arguments = arguments;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/SignableCommand;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/SignableCommand;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/SignableCommand<TS;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/SignableCommand;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/SignableCommand;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/SignableCommand<TS;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/SignableCommand;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/SignableCommand;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/SignableCommand<TS;>;
/*    */   }
/*    */   
/*    */   public List<Argument<S>> arguments() {
/* 14 */     return this.arguments;
/*    */   }
/*    */   
/*    */   public static <S> boolean hasSignableArguments(ParseResults<S> command) {
/* 16 */     return !of(command).arguments().isEmpty();
/*    */   }
/*    */   
/*    */   public static <S> SignableCommand<S> of(ParseResults<S> command) {
/* 20 */     final String commandString = command.getReader().getString();
/* 21 */     final List<Argument<S>> arguments = new ArrayList<>();
/* 23 */     ArgumentVisitor.visitArguments(command, new ArgumentVisitor.Output<S>() {
/*    */           public <T> void accept(CommandContextBuilder<S> context, ArgumentCommandNode<S, T> argument, ParsedArgument<S, T> value) {
/* 26 */             if (value != null && argument.getType() instanceof net.minecraft.commands.arguments.SignedArgument) {
/* 27 */               String stringValue = value.getRange().get(commandString);
/* 28 */               arguments.add(new SignableCommand.Argument<>(argument, stringValue));
/*    */             } 
/*    */           }
/*    */         }true);
/* 32 */     return new SignableCommand<>(arguments);
/*    */   }
/*    */   
/*    */   public Argument<S> getArgument(String name) {
/* 36 */     for (Argument<S> argument : this.arguments) {
/* 37 */       if (name.equals(argument.name()))
/* 38 */         return argument; 
/*    */     } 
/* 41 */     return null;
/*    */   }
/*    */   
/*    */   public static final class Argument<S> extends Record {
/*    */     private final ArgumentCommandNode<S, ?> node;
/*    */     
/*    */     private final String value;
/*    */     
/*    */     public Argument(ArgumentCommandNode<S, ?> node, String value) {
/* 44 */       this.node = node;
/* 44 */       this.value = value;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/SignableCommand$Argument;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/SignableCommand$Argument;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/SignableCommand$Argument<TS;>;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/SignableCommand$Argument;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/SignableCommand$Argument;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/SignableCommand$Argument<TS;>;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/SignableCommand$Argument;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/chat/SignableCommand$Argument;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/network/chat/SignableCommand$Argument<TS;>;
/*    */     }
/*    */     
/*    */     public ArgumentCommandNode<S, ?> node() {
/* 44 */       return this.node;
/*    */     }
/*    */     
/*    */     public String value() {
/* 44 */       return this.value;
/*    */     }
/*    */     
/*    */     public String name() {
/* 46 */       return this.node.getName();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\SignableCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */