/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import java.net.URI;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public final class Entry extends Record {
/*    */   private final Either<ServerLinks.KnownLinkType, Component> type;
/*    */   
/*    */   private final URI link;
/*    */   
/*    */   public Entry(Either<ServerLinks.KnownLinkType, Component> type, URI link) {
/* 49 */     this.type = type;
/* 49 */     this.link = link;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/ServerLinks$Entry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #49	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/ServerLinks$Entry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/ServerLinks$Entry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #49	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/ServerLinks$Entry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/ServerLinks$Entry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #49	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/ServerLinks$Entry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Either<ServerLinks.KnownLinkType, Component> type() {
/* 49 */     return this.type;
/*    */   }
/*    */   
/*    */   public URI link() {
/* 49 */     return this.link;
/*    */   }
/*    */   
/*    */   public static Entry knownType(ServerLinks.KnownLinkType type, URI link) {
/* 54 */     return new Entry(Either.left(type), link);
/*    */   }
/*    */   
/*    */   public static Entry custom(Component displayName, URI link) {
/* 58 */     return new Entry(Either.right(displayName), link);
/*    */   }
/*    */   
/*    */   public Component displayName() {
/* 62 */     return (Component)this.type.map(ServerLinks.KnownLinkType::displayName, r -> r);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\ServerLinks$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */