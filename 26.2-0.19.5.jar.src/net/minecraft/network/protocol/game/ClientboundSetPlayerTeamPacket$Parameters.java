/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.Team;
/*     */ import net.minecraft.world.scores.TeamColor;
/*     */ 
/*     */ public final class Parameters extends Record {
/*     */   private final Component displayName;
/*     */   
/*     */   private final Component playerPrefix;
/*     */   
/*     */   private final Component playerSuffix;
/*     */   
/*     */   private final Team.Visibility nameTagVisibility;
/*     */   
/*     */   private final Team.CollisionRule collisionRule;
/*     */   
/*     */   private final Optional<TeamColor> color;
/*     */   
/*     */   private final byte options;
/*     */   
/*     */   public byte options() {
/* 150 */     return this.options;
/*     */   }
/*     */   
/*     */   public Optional<TeamColor> color() {
/* 150 */     return this.color;
/*     */   }
/*     */   
/*     */   public Team.CollisionRule collisionRule() {
/* 150 */     return this.collisionRule;
/*     */   }
/*     */   
/*     */   public Team.Visibility nameTagVisibility() {
/* 150 */     return this.nameTagVisibility;
/*     */   }
/*     */   
/*     */   public Component playerSuffix() {
/* 150 */     return this.playerSuffix;
/*     */   }
/*     */   
/*     */   public Component playerPrefix() {
/* 150 */     return this.playerPrefix;
/*     */   }
/*     */   
/*     */   public Component displayName() {
/* 150 */     return this.displayName;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket$Parameters;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #150	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket$Parameters;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket$Parameters;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #150	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket$Parameters;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket$Parameters;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #150	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket$Parameters;
/*     */   }
/*     */   
/*     */   public Parameters(Component displayName, Component playerPrefix, Component playerSuffix, Team.Visibility nameTagVisibility, Team.CollisionRule collisionRule, Optional<TeamColor> color, byte options) {
/* 150 */     this.displayName = displayName;
/* 150 */     this.playerPrefix = playerPrefix;
/* 150 */     this.playerSuffix = playerSuffix;
/* 150 */     this.nameTagVisibility = nameTagVisibility;
/* 150 */     this.collisionRule = collisionRule;
/* 150 */     this.color = color;
/* 150 */     this.options = options;
/*     */   }
/*     */   
/* 159 */   public static final StreamCodec<RegistryFriendlyByteBuf, Parameters> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.TRUSTED_STREAM_CODEC, Parameters::displayName, ComponentSerialization.TRUSTED_STREAM_CODEC, Parameters::playerPrefix, ComponentSerialization.TRUSTED_STREAM_CODEC, Parameters::playerSuffix, Team.Visibility.STREAM_CODEC, Parameters::nameTagVisibility, Team.CollisionRule.STREAM_CODEC, Parameters::collisionRule, 
/*     */       
/* 165 */       ByteBufCodecs.optional(TeamColor.STREAM_CODEC), Parameters::color, ByteBufCodecs.BYTE, Parameters::options, Parameters::new);
/*     */   
/*     */   public Parameters(PlayerTeam team) {
/* 171 */     this(team
/* 172 */         .getDisplayName(), team
/* 173 */         .getPlayerPrefix(), team
/* 174 */         .getPlayerSuffix(), team
/* 175 */         .getNameTagVisibility(), team
/* 176 */         .getCollisionRule(), team
/* 177 */         .getColor(), team
/* 178 */         .packOptions());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundSetPlayerTeamPacket$Parameters.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */