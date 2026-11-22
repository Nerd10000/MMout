/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.UUID;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ 
/*    */ public final class NameAndId extends Record {
/*    */   private final UUID id;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   public static final Codec<NameAndId> CODEC;
/*    */   
/*    */   public NameAndId(UUID id, String name) {
/* 14 */     this.id = id;
/* 14 */     this.name = name;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/players/NameAndId;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/players/NameAndId;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/players/NameAndId;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/players/NameAndId;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/players/NameAndId;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/players/NameAndId;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public UUID id() {
/* 14 */     return this.id;
/*    */   }
/*    */   
/*    */   public String name() {
/* 14 */     return this.name;
/*    */   }
/*    */   
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)UUIDUtil.STRING_CODEC.fieldOf("id").forGetter(NameAndId::id), (App)Codec.STRING.fieldOf("name").forGetter(NameAndId::name)).apply((Applicative)i, NameAndId::new));
/*    */   }
/*    */   
/*    */   public NameAndId(GameProfile profile) {
/* 24 */     this(profile.id(), profile.name());
/*    */   }
/*    */   
/*    */   public NameAndId(com.mojang.authlib.yggdrasil.response.NameAndId profile) {
/* 28 */     this(profile.id(), profile.name());
/*    */   }
/*    */   
/*    */   public static NameAndId fromJson(JsonObject object) {
/*    */     UUID uuid;
/* 34 */     if (!object.has("uuid") || !object.has("name"))
/* 35 */       return null; 
/* 37 */     String uuidString = object.get("uuid").getAsString();
/*    */     try {
/* 40 */       uuid = UUID.fromString(uuidString);
/* 41 */     } catch (Throwable ignored) {
/* 42 */       return null;
/*    */     } 
/* 44 */     return new NameAndId(uuid, object.get("name").getAsString());
/*    */   }
/*    */   
/*    */   public void appendTo(JsonObject output) {
/* 48 */     output.addProperty("uuid", id().toString());
/* 49 */     output.addProperty("name", name());
/*    */   }
/*    */   
/*    */   public static NameAndId createOffline(String name) {
/* 53 */     UUID id = UUIDUtil.createOfflinePlayerUUID(name);
/* 54 */     return new NameAndId(id, name);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\players\NameAndId.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */