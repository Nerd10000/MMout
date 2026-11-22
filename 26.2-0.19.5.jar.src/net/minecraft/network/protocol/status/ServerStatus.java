/*    */ package net.minecraft.network.protocol.status;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Base64;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.WorldVersion;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ 
/*    */ public final class ServerStatus extends Record {
/*    */   private final Component description;
/*    */   
/*    */   private final Optional<Players> players;
/*    */   
/*    */   private final Optional<Version> version;
/*    */   
/*    */   private final Optional<Favicon> favicon;
/*    */   
/*    */   private final boolean enforcesSecureChat;
/*    */   
/*    */   public static final Codec<ServerStatus> CODEC;
/*    */   
/*    */   public ServerStatus(Component description, Optional<Players> players, Optional<Version> version, Optional<Favicon> favicon, boolean enforcesSecureChat) {
/* 18 */     this.description = description;
/* 18 */     this.players = players;
/* 18 */     this.version = version;
/* 18 */     this.favicon = favicon;
/* 18 */     this.enforcesSecureChat = enforcesSecureChat;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/status/ServerStatus;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/status/ServerStatus;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/status/ServerStatus;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/status/ServerStatus;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Component description() {
/* 18 */     return this.description;
/*    */   }
/*    */   
/*    */   public Optional<Players> players() {
/* 18 */     return this.players;
/*    */   }
/*    */   
/*    */   public Optional<Version> version() {
/* 18 */     return this.version;
/*    */   }
/*    */   
/*    */   public Optional<Favicon> favicon() {
/* 18 */     return this.favicon;
/*    */   }
/*    */   
/*    */   public boolean enforcesSecureChat() {
/* 18 */     return this.enforcesSecureChat;
/*    */   }
/*    */   
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ComponentSerialization.CODEC.lenientOptionalFieldOf("description", CommonComponents.EMPTY).forGetter(ServerStatus::description), (App)Players.CODEC.lenientOptionalFieldOf("players").forGetter(ServerStatus::players), (App)Version.CODEC.lenientOptionalFieldOf("version").forGetter(ServerStatus::version), (App)Favicon.CODEC.lenientOptionalFieldOf("favicon").forGetter(ServerStatus::favicon), (App)Codec.BOOL.lenientOptionalFieldOf("enforcesSecureChat", Boolean.valueOf(false)).forGetter(ServerStatus::enforcesSecureChat)).apply((Applicative)i, ServerStatus::new));
/*    */   }
/*    */   
/*    */   public static final class Players extends Record {
/*    */     private final int max;
/*    */     
/*    */     private final int online;
/*    */     
/*    */     private final List<NameAndId> sample;
/*    */     
/*    */     public static final Codec<Players> CODEC;
/*    */     
/*    */     public Players(int max, int online, List<NameAndId> sample) {
/* 28 */       this.max = max;
/* 28 */       this.online = online;
/* 28 */       this.sample = sample;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/status/ServerStatus$Players;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Players;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/status/ServerStatus$Players;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Players;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/status/ServerStatus$Players;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Players;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public int max() {
/* 28 */       return this.max;
/*    */     }
/*    */     
/*    */     public int online() {
/* 28 */       return this.online;
/*    */     }
/*    */     
/*    */     public List<NameAndId> sample() {
/* 28 */       return this.sample;
/*    */     }
/*    */     
/*    */     static {
/* 29 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("max").forGetter(Players::max), (App)Codec.INT.fieldOf("online").forGetter(Players::online), (App)NameAndId.CODEC.listOf().lenientOptionalFieldOf("sample", List.of()).forGetter(Players::sample)).apply((Applicative)i, Players::new));
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Version extends Record {
/*    */     private final String name;
/*    */     
/*    */     private final int protocol;
/*    */     
/*    */     public static final Codec<Version> CODEC;
/*    */     
/*    */     public Version(String name, int protocol) {
/* 36 */       this.name = name;
/* 36 */       this.protocol = protocol;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/status/ServerStatus$Version;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Version;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/status/ServerStatus$Version;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Version;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/status/ServerStatus$Version;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Version;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public String name() {
/* 36 */       return this.name;
/*    */     }
/*    */     
/*    */     public int protocol() {
/* 36 */       return this.protocol;
/*    */     }
/*    */     
/*    */     static {
/* 37 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("name").forGetter(Version::name), (App)Codec.INT.fieldOf("protocol").forGetter(Version::protocol)).apply((Applicative)i, Version::new));
/*    */     }
/*    */     
/*    */     public static Version current() {
/* 43 */       WorldVersion version = SharedConstants.getCurrentVersion();
/* 44 */       return new Version(version.name(), version.protocolVersion());
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Favicon extends Record {
/*    */     private final byte[] iconBytes;
/*    */     
/*    */     private static final String PREFIX = "data:image/png;base64,";
/*    */     
/*    */     public static final Codec<Favicon> CODEC;
/*    */     
/*    */     public Favicon(byte[] iconBytes) {
/* 48 */       this.iconBytes = iconBytes;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/status/ServerStatus$Favicon;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Favicon;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/status/ServerStatus$Favicon;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Favicon;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/status/ServerStatus$Favicon;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Favicon;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public byte[] iconBytes() {
/* 48 */       return this.iconBytes;
/*    */     }
/*    */     
/*    */     static {
/* 51 */       CODEC = Codec.STRING.comapFlatMap(string -> {
/*    */             if (!string.startsWith("data:image/png;base64,"))
/*    */               return DataResult.error(()); 
/*    */             try {
/*    */               String base64 = string.substring("data:image/png;base64,".length()).replaceAll("\n", "");
/*    */               byte[] iconBytes = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
/*    */               return DataResult.success(new Favicon(iconBytes));
/* 61 */             } catch (IllegalArgumentException e) {
/*    */               return DataResult.error(());
/*    */             } 
/*    */           }favicon -> "data:image/png;base64," + new String(Base64.getEncoder().encode(favicon.iconBytes), StandardCharsets.UTF_8));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\status\ServerStatus.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */