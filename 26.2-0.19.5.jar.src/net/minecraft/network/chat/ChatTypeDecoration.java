/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.List;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public final class ChatTypeDecoration extends Record {
/*    */   private final String translationKey;
/*    */   
/*    */   private final List<Parameter> parameters;
/*    */   
/*    */   private final Style style;
/*    */   
/*    */   public static final Codec<ChatTypeDecoration> CODEC;
/*    */   
/*    */   public ChatTypeDecoration(String translationKey, List<Parameter> parameters, Style style) {
/* 16 */     this.translationKey = translationKey;
/* 16 */     this.parameters = parameters;
/* 16 */     this.style = style;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ChatTypeDecoration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/ChatTypeDecoration;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ChatTypeDecoration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/ChatTypeDecoration;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ChatTypeDecoration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/ChatTypeDecoration;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String translationKey() {
/* 16 */     return this.translationKey;
/*    */   }
/*    */   
/*    */   public List<Parameter> parameters() {
/* 16 */     return this.parameters;
/*    */   }
/*    */   
/*    */   public Style style() {
/* 16 */     return this.style;
/*    */   }
/*    */   
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("translation_key").forGetter(ChatTypeDecoration::translationKey), (App)Parameter.CODEC.listOf().fieldOf("parameters").forGetter(ChatTypeDecoration::parameters), (App)Style.Serializer.CODEC.optionalFieldOf("style", Style.EMPTY).forGetter(ChatTypeDecoration::style)).apply((Applicative)i, ChatTypeDecoration::new));
/*    */   }
/*    */   
/* 23 */   public static final StreamCodec<RegistryFriendlyByteBuf, ChatTypeDecoration> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, ChatTypeDecoration::translationKey, Parameter.STREAM_CODEC
/*    */       
/* 25 */       .apply(ByteBufCodecs.list()), ChatTypeDecoration::parameters, Style.Serializer.TRUSTED_STREAM_CODEC, ChatTypeDecoration::style, ChatTypeDecoration::new);
/*    */   
/*    */   public static ChatTypeDecoration withSender(String translationKey) {
/* 31 */     return new ChatTypeDecoration(translationKey, List.of(Parameter.SENDER, Parameter.CONTENT), Style.EMPTY);
/*    */   }
/*    */   
/*    */   public static ChatTypeDecoration incomingDirectMessage(String translationKey) {
/* 35 */     Style style = Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(Boolean.valueOf(true));
/* 36 */     return new ChatTypeDecoration(translationKey, List.of(Parameter.SENDER, Parameter.CONTENT), style);
/*    */   }
/*    */   
/*    */   public static ChatTypeDecoration outgoingDirectMessage(String translationKey) {
/* 40 */     Style style = Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(Boolean.valueOf(true));
/* 41 */     return new ChatTypeDecoration(translationKey, List.of(Parameter.TARGET, Parameter.CONTENT), style);
/*    */   }
/*    */   
/*    */   public static ChatTypeDecoration teamMessage(String translationKey) {
/* 45 */     return new ChatTypeDecoration(translationKey, List.of(Parameter.TARGET, Parameter.SENDER, Parameter.CONTENT), Style.EMPTY);
/*    */   }
/*    */   
/*    */   public Component decorate(Component content, ChatType.Bound chatType) {
/* 50 */     Component[] arrayOfComponent = resolveParameters(content, chatType);
/* 52 */     return Component.translatable(this.translationKey, (Object[])arrayOfComponent).withStyle(this.style);
/*    */   }
/*    */   
/*    */   private Component[] resolveParameters(Component content, ChatType.Bound chatType) {
/* 56 */     Component[] resolved = new Component[this.parameters.size()];
/* 57 */     for (int i = 0; i < resolved.length; i++) {
/* 58 */       Parameter parameter = this.parameters.get(i);
/* 59 */       resolved[i] = parameter.select(content, chatType);
/*    */     } 
/* 61 */     return resolved;
/*    */   }
/*    */   
/*    */   public enum Parameter implements StringRepresentable {
/*    */     SENDER, TARGET, CONTENT;
/*    */     
/*    */     private static final IntFunction<Parameter> BY_ID;
/*    */     
/*    */     public static final Codec<Parameter> CODEC;
/*    */     
/*    */     public static final StreamCodec<ByteBuf, Parameter> STREAM_CODEC;
/*    */     
/*    */     private final int id;
/*    */     
/*    */     private final String name;
/*    */     
/*    */     private final Selector selector;
/*    */     
/*    */     static {
/* 65 */       SENDER = new Parameter("SENDER", 0, 0, "sender", (content, chatType) -> chatType.name());
/* 66 */       TARGET = new Parameter("TARGET", 1, 1, "target", (content, chatType) -> (Component)chatType.targetName().orElse(CommonComponents.EMPTY));
/* 67 */       CONTENT = new Parameter("CONTENT", 2, 2, "content", (content, chatType) -> content);
/*    */     }
/*    */     
/*    */     static {
/* 69 */       BY_ID = ByIdMap.continuous(p -> p.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 71 */       CODEC = (Codec<Parameter>)StringRepresentable.fromEnum(Parameter::values);
/* 72 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, p -> p.id);
/*    */     }
/*    */     
/*    */     Parameter(int id, String name, Selector selector) {
/* 79 */       this.id = id;
/* 80 */       this.name = name;
/* 81 */       this.selector = selector;
/*    */     }
/*    */     
/*    */     public Component select(Component content, ChatType.Bound chatType) {
/* 85 */       return this.selector.select(content, chatType);
/*    */     }
/*    */     
/*    */     public String getSerializedName() {
/* 90 */       return this.name;
/*    */     }
/*    */     
/*    */     public static interface Selector {
/*    */       Component select(Component param2Component, ChatType.Bound param2Bound);
/*    */     }
/*    */   }
/*    */   
/*    */   public static interface Selector {
/*    */     Component select(Component param1Component, ChatType.Bound param1Bound);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\ChatTypeDecoration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */