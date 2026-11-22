/*    */ package net.minecraft.server;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.net.URI;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public enum KnownLinkType {
/* 67 */   BUG_REPORT(0, "report_bug"),
/* 68 */   COMMUNITY_GUIDELINES(1, "community_guidelines"),
/* 69 */   SUPPORT(2, "support"),
/* 70 */   STATUS(3, "status"),
/* 71 */   FEEDBACK(4, "feedback"),
/* 72 */   COMMUNITY(5, "community"),
/* 73 */   WEBSITE(6, "website"),
/* 74 */   FORUMS(7, "forums"),
/* 75 */   NEWS(8, "news"),
/* 76 */   ANNOUNCEMENTS(9, "announcements");
/*    */   
/*    */   private static final IntFunction<KnownLinkType> BY_ID;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, KnownLinkType> STREAM_CODEC;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 79 */     BY_ID = ByIdMap.continuous(e -> e.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 81 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, e -> e.id);
/*    */   }
/*    */   
/*    */   KnownLinkType(int id, String name) {
/* 87 */     this.id = id;
/* 88 */     this.name = name;
/*    */   }
/*    */   
/*    */   private Component displayName() {
/* 92 */     return (Component)Component.translatable("known_server_link." + this.name);
/*    */   }
/*    */   
/*    */   public ServerLinks.Entry create(URI link) {
/* 96 */     return ServerLinks.Entry.knownType(this, link);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\ServerLinks$KnownLinkType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */