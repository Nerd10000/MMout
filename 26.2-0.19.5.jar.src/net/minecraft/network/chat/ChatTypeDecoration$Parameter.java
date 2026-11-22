/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum Parameter implements StringRepresentable {
/*    */   SENDER, TARGET, CONTENT;
/*    */   
/*    */   private static final IntFunction<Parameter> BY_ID;
/*    */   
/*    */   public static final Codec<Parameter> CODEC;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, Parameter> STREAM_CODEC;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final Selector selector;
/*    */   
/*    */   static {
/* 65 */     SENDER = new Parameter("SENDER", 0, 0, "sender", (content, chatType) -> chatType.name());
/* 66 */     TARGET = new Parameter("TARGET", 1, 1, "target", (content, chatType) -> (Component)chatType.targetName().orElse(CommonComponents.EMPTY));
/* 67 */     CONTENT = new Parameter("CONTENT", 2, 2, "content", (content, chatType) -> content);
/*    */   }
/*    */   
/*    */   static {
/* 69 */     BY_ID = ByIdMap.continuous(p -> p.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 71 */     CODEC = (Codec<Parameter>)StringRepresentable.fromEnum(Parameter::values);
/* 72 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, p -> p.id);
/*    */   }
/*    */   
/*    */   Parameter(int id, String name, Selector selector) {
/* 79 */     this.id = id;
/* 80 */     this.name = name;
/* 81 */     this.selector = selector;
/*    */   }
/*    */   
/*    */   public Component select(Component content, ChatType.Bound chatType) {
/* 85 */     return this.selector.select(content, chatType);
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 90 */     return this.name;
/*    */   }
/*    */   
/*    */   public static interface Selector {
/*    */     Component select(Component param2Component, ChatType.Bound param2Bound);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\ChatTypeDecoration$Parameter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */