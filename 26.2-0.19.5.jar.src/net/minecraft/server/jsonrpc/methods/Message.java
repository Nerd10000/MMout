/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public final class Message extends Record {
/*    */   private final Optional<String> literal;
/*    */   
/*    */   private final Optional<String> translatable;
/*    */   
/*    */   private final Optional<List<String>> translatableParams;
/*    */   
/*    */   public static final Codec<Message> CODEC;
/*    */   
/*    */   public Message(Optional<String> literal, Optional<String> translatable, Optional<List<String>> translatableParams) {
/* 10 */     this.literal = literal;
/* 10 */     this.translatable = translatable;
/* 10 */     this.translatableParams = translatableParams;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/Message;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/Message;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/Message;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/Message;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/Message;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/Message;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<String> literal() {
/* 10 */     return this.literal;
/*    */   }
/*    */   
/*    */   public Optional<String> translatable() {
/* 10 */     return this.translatable;
/*    */   }
/*    */   
/*    */   public Optional<List<String>> translatableParams() {
/* 10 */     return this.translatableParams;
/*    */   }
/*    */   
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.optionalFieldOf("literal").forGetter(Message::literal), (App)Codec.STRING.optionalFieldOf("translatable").forGetter(Message::translatable), (App)Codec.STRING.listOf().lenientOptionalFieldOf("translatableParams").forGetter(Message::translatableParams)).apply((Applicative)i, Message::new));
/*    */   }
/*    */   
/*    */   public Optional<Component> asComponent() {
/* 19 */     if (this.translatable.isPresent()) {
/* 20 */       String translationKey = this.translatable.get();
/* 21 */       if (this.translatableParams.isPresent()) {
/* 22 */         List<String> translationArgs = this.translatableParams.get();
/* 24 */         return (Optional)Optional.of(Component.translatable(translationKey, translationArgs.toArray()));
/*    */       } 
/* 26 */       return (Optional)Optional.of(Component.translatable(translationKey));
/*    */     } 
/* 29 */     return this.literal.map(Component::literal);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\methods\Message.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */