/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.Team;
/*     */ import net.minecraft.world.scores.TeamColor;
/*     */ 
/*     */ public class ClientboundSetPlayerTeamPacket implements Packet<ClientGamePacketListener> {
/*  21 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetPlayerTeamPacket> STREAM_CODEC = Packet.codec(ClientboundSetPlayerTeamPacket::write, ClientboundSetPlayerTeamPacket::new);
/*     */   
/*     */   private static final int METHOD_ADD = 0;
/*     */   
/*     */   private static final int METHOD_REMOVE = 1;
/*     */   
/*     */   private static final int METHOD_CHANGE = 2;
/*     */   
/*     */   private static final int METHOD_JOIN = 3;
/*     */   
/*     */   private static final int METHOD_LEAVE = 4;
/*     */   
/*     */   private final int method;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final Collection<String> players;
/*     */   
/*     */   private final Optional<Parameters> parameters;
/*     */   
/*     */   private ClientboundSetPlayerTeamPacket(String name, int method, Optional<Parameters> parameters, Collection<String> players) {
/*  35 */     this.name = name;
/*  36 */     this.method = method;
/*  37 */     this.parameters = parameters;
/*  38 */     this.players = (Collection<String>)ImmutableList.copyOf(players);
/*     */   }
/*     */   
/*     */   public static ClientboundSetPlayerTeamPacket createAddOrModifyPacket(PlayerTeam team, boolean createNew) {
/*  42 */     return new ClientboundSetPlayerTeamPacket(team
/*  43 */         .getName(), 
/*  44 */         createNew ? 0 : 2, 
/*  45 */         Optional.of(new Parameters(team)), 
/*  46 */         createNew ? team.getPlayers() : (Collection<String>)ImmutableList.of());
/*     */   }
/*     */   
/*     */   public static ClientboundSetPlayerTeamPacket createRemovePacket(PlayerTeam team) {
/*  51 */     return new ClientboundSetPlayerTeamPacket(team
/*  52 */         .getName(), 1, 
/*     */         
/*  54 */         Optional.empty(), 
/*  55 */         (Collection<String>)ImmutableList.of());
/*     */   }
/*     */   
/*     */   public static ClientboundSetPlayerTeamPacket createPlayerPacket(PlayerTeam team, String player, Action action) {
/*  60 */     return new ClientboundSetPlayerTeamPacket(team
/*  61 */         .getName(), 
/*  62 */         (action == Action.ADD) ? 3 : 4, 
/*  63 */         Optional.empty(), 
/*  64 */         (Collection<String>)ImmutableList.of(player));
/*     */   }
/*     */   
/*     */   private ClientboundSetPlayerTeamPacket(RegistryFriendlyByteBuf input) {
/*  69 */     this.name = input.readUtf();
/*  70 */     this.method = input.readByte();
/*  72 */     if (shouldHaveParameters(this.method)) {
/*  73 */       this.parameters = Optional.of((Parameters)Parameters.STREAM_CODEC.decode(input));
/*     */     } else {
/*  75 */       this.parameters = Optional.empty();
/*     */     } 
/*  78 */     if (shouldHavePlayerList(this.method)) {
/*  79 */       this.players = input.readList(FriendlyByteBuf::readUtf);
/*     */     } else {
/*  81 */       this.players = (Collection<String>)ImmutableList.of();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void write(RegistryFriendlyByteBuf output) {
/*  86 */     output.writeUtf(this.name);
/*  87 */     output.writeByte(this.method);
/*  89 */     if (shouldHaveParameters(this.method))
/*  90 */       Parameters.STREAM_CODEC.encode(output, this.parameters.<Throwable>orElseThrow(() -> new IllegalStateException("Parameters not present, but method is" + this.method))); 
/*  93 */     if (shouldHavePlayerList(this.method))
/*  94 */       output.writeCollection(this.players, FriendlyByteBuf::writeUtf); 
/*     */   }
/*     */   
/*     */   private static boolean shouldHavePlayerList(int method) {
/*  99 */     return (method == 0 || method == 3 || method == 4);
/*     */   }
/*     */   
/*     */   private static boolean shouldHaveParameters(int method) {
/* 103 */     return (method == 0 || method == 2);
/*     */   }
/*     */   
/*     */   public Action getPlayerAction() {
/* 107 */     switch (this.method) {
/*     */       case 0:
/*     */       case 3:
/*     */       
/*     */       case 4:
/*     */       
/*     */     } 
/* 107 */     return 
/*     */       
/* 110 */       null;
/*     */   }
/*     */   
/*     */   public Action getTeamAction() {
/* 115 */     switch (this.method) {
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */       
/*     */     } 
/* 115 */     return 
/*     */       
/* 118 */       null;
/*     */   }
/*     */   
/*     */   public PacketType<ClientboundSetPlayerTeamPacket> type() {
/* 124 */     return GamePacketTypes.CLIENTBOUND_SET_PLAYER_TEAM;
/*     */   }
/*     */   
/*     */   public void handle(ClientGamePacketListener listener) {
/* 129 */     listener.handleSetPlayerTeamPacket(this);
/*     */   }
/*     */   
/*     */   public String getName() {
/* 133 */     return this.name;
/*     */   }
/*     */   
/*     */   public Collection<String> getPlayers() {
/* 137 */     return this.players;
/*     */   }
/*     */   
/*     */   public Optional<Parameters> getParameters() {
/* 141 */     return this.parameters;
/*     */   }
/*     */   
/*     */   public enum Action {
/* 145 */     ADD, REMOVE;
/*     */   }
/*     */   
/*     */   public static final class Parameters extends Record {
/*     */     private final Component displayName;
/*     */     
/*     */     private final Component playerPrefix;
/*     */     
/*     */     private final Component playerSuffix;
/*     */     
/*     */     private final Team.Visibility nameTagVisibility;
/*     */     
/*     */     private final Team.CollisionRule collisionRule;
/*     */     
/*     */     private final Optional<TeamColor> color;
/*     */     
/*     */     private final byte options;
/*     */     
/*     */     public byte options() {
/* 150 */       return this.options;
/*     */     }
/*     */     
/*     */     public Optional<TeamColor> color() {
/* 150 */       return this.color;
/*     */     }
/*     */     
/*     */     public Team.CollisionRule collisionRule() {
/* 150 */       return this.collisionRule;
/*     */     }
/*     */     
/*     */     public Team.Visibility nameTagVisibility() {
/* 150 */       return this.nameTagVisibility;
/*     */     }
/*     */     
/*     */     public Component playerSuffix() {
/* 150 */       return this.playerSuffix;
/*     */     }
/*     */     
/*     */     public Component playerPrefix() {
/* 150 */       return this.playerPrefix;
/*     */     }
/*     */     
/*     */     public Component displayName() {
/* 150 */       return this.displayName;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket$Parameters;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #150	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket$Parameters;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket$Parameters;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #150	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket$Parameters;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket$Parameters;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #150	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket$Parameters;
/*     */     }
/*     */     
/*     */     public Parameters(Component displayName, Component playerPrefix, Component playerSuffix, Team.Visibility nameTagVisibility, Team.CollisionRule collisionRule, Optional<TeamColor> color, byte options) {
/* 150 */       this.displayName = displayName;
/* 150 */       this.playerPrefix = playerPrefix;
/* 150 */       this.playerSuffix = playerSuffix;
/* 150 */       this.nameTagVisibility = nameTagVisibility;
/* 150 */       this.collisionRule = collisionRule;
/* 150 */       this.color = color;
/* 150 */       this.options = options;
/*     */     }
/*     */     
/* 159 */     public static final StreamCodec<RegistryFriendlyByteBuf, Parameters> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.TRUSTED_STREAM_CODEC, Parameters::displayName, ComponentSerialization.TRUSTED_STREAM_CODEC, Parameters::playerPrefix, ComponentSerialization.TRUSTED_STREAM_CODEC, Parameters::playerSuffix, Team.Visibility.STREAM_CODEC, Parameters::nameTagVisibility, Team.CollisionRule.STREAM_CODEC, Parameters::collisionRule, 
/*     */         
/* 165 */         ByteBufCodecs.optional(TeamColor.STREAM_CODEC), Parameters::color, ByteBufCodecs.BYTE, Parameters::options, Parameters::new);
/*     */     
/*     */     public Parameters(PlayerTeam team) {
/* 171 */       this(team
/* 172 */           .getDisplayName(), team
/* 173 */           .getPlayerPrefix(), team
/* 174 */           .getPlayerSuffix(), team
/* 175 */           .getNameTagVisibility(), team
/* 176 */           .getCollisionRule(), team
/* 177 */           .getColor(), team
/* 178 */           .packOptions());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundSetPlayerTeamPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */