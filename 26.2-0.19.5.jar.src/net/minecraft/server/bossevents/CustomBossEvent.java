/*     */ package net.minecraft.server.bossevents;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function10;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerBossEvent;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.BossEvent;
/*     */ 
/*     */ public class CustomBossEvent extends ServerBossEvent {
/*     */   private static final int DEFAULT_MAX = 100;
/*     */   
/*     */   private final Identifier customId;
/*     */   
/*  24 */   private final Set<UUID> players = Sets.newHashSet();
/*     */   
/*     */   private int value;
/*     */   
/*  26 */   private int max = 100;
/*     */   
/*     */   private final Runnable dirtyCallback;
/*     */   
/*     */   public CustomBossEvent(UUID id, Identifier customId, Component name, Runnable dirtyCallback) {
/*  30 */     super(id, name, BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);
/*  31 */     this.dirtyCallback = dirtyCallback;
/*  32 */     this.customId = customId;
/*  33 */     setProgress(0.0F);
/*     */   }
/*     */   
/*     */   public Identifier customId() {
/*  37 */     return this.customId;
/*     */   }
/*     */   
/*     */   public void addPlayer(ServerPlayer player) {
/*  42 */     super.addPlayer(player);
/*  43 */     if (this.players.add(player.getUUID()))
/*  44 */       setDirty(); 
/*     */   }
/*     */   
/*     */   public void removePlayer(ServerPlayer player) {
/*  50 */     super.removePlayer(player);
/*  51 */     if (this.players.remove(player.getUUID()))
/*  52 */       setDirty(); 
/*     */   }
/*     */   
/*     */   public void removeAllPlayers() {
/*  58 */     super.removeAllPlayers();
/*  59 */     if (!this.players.isEmpty()) {
/*  60 */       this.players.clear();
/*  61 */       setDirty();
/*     */     } 
/*     */   }
/*     */   
/*     */   public int value() {
/*  66 */     return this.value;
/*     */   }
/*     */   
/*     */   public int max() {
/*  70 */     return this.max;
/*     */   }
/*     */   
/*     */   public void setValue(int value) {
/*  74 */     this.value = value;
/*  75 */     setProgress(Mth.clamp(value / this.max, 0.0F, 1.0F));
/*  76 */     setDirty();
/*     */   }
/*     */   
/*     */   public void setMax(int max) {
/*  80 */     this.max = max;
/*  81 */     setProgress(Mth.clamp(this.value / max, 0.0F, 1.0F));
/*  82 */     setDirty();
/*     */   }
/*     */   
/*     */   public final Component getDisplayName() {
/*  86 */     return (Component)ComponentUtils.wrapInSquareBrackets(getName()).withStyle(s -> s.withColor(getColor().getFormatting()).withHoverEvent((HoverEvent)new HoverEvent.ShowText((Component)Component.literal(customId().toString()))).withInsertion(customId().toString()));
/*     */   }
/*     */   
/*     */   public boolean setPlayers(Collection<ServerPlayer> players) {
/*  94 */     Set<UUID> toRemove = Sets.newHashSet();
/*  95 */     Set<ServerPlayer> toAdd = Sets.newHashSet();
/*  97 */     for (UUID uuid : this.players) {
/*  98 */       boolean found = false;
/*  99 */       for (ServerPlayer player : players) {
/* 100 */         if (player.getUUID().equals(uuid)) {
/* 101 */           found = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 105 */       if (!found)
/* 106 */         toRemove.add(uuid); 
/*     */     } 
/* 110 */     for (ServerPlayer player : players) {
/* 111 */       boolean found = false;
/* 112 */       for (UUID uuid : this.players) {
/* 113 */         if (player.getUUID().equals(uuid)) {
/* 114 */           found = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 118 */       if (!found)
/* 119 */         toAdd.add(player); 
/*     */     } 
/* 123 */     for (UUID uuid : toRemove) {
/* 124 */       for (ServerPlayer player : getPlayers()) {
/* 125 */         if (player.getUUID().equals(uuid)) {
/* 126 */           removePlayer(player);
/*     */           break;
/*     */         } 
/*     */       } 
/* 130 */       this.players.remove(uuid);
/*     */     } 
/* 133 */     for (ServerPlayer player : toAdd)
/* 134 */       addPlayer(player); 
/* 137 */     boolean playersChanged = (!toRemove.isEmpty() || !toAdd.isEmpty());
/* 138 */     if (playersChanged)
/* 139 */       setDirty(); 
/* 141 */     return playersChanged;
/*     */   }
/*     */   
/*     */   public static CustomBossEvent load(UUID id, Identifier customId, Packed packed, Runnable setDirty) {
/* 145 */     CustomBossEvent event = new CustomBossEvent(id, customId, packed.name, setDirty);
/* 146 */     event.setVisible(packed.visible);
/* 147 */     event.setValue(packed.value);
/* 148 */     event.setMax(packed.max);
/* 149 */     event.setColor(packed.color);
/* 150 */     event.setOverlay(packed.overlay);
/* 151 */     event.setDarkenScreen(packed.darkenScreen);
/* 152 */     event.setPlayBossMusic(packed.playBossMusic);
/* 153 */     event.setCreateWorldFog(packed.createWorldFog);
/* 154 */     event.players.addAll(packed.players);
/* 155 */     return event;
/*     */   }
/*     */   
/*     */   public Packed pack() {
/* 159 */     return new Packed(getName(), isVisible(), value(), max(), getColor(), getOverlay(), shouldDarkenScreen(), shouldPlayBossMusic(), shouldCreateWorldFog(), Set.copyOf(this.players));
/*     */   }
/*     */   
/*     */   public void onPlayerConnect(ServerPlayer player) {
/* 163 */     if (this.players.contains(player.getUUID()))
/* 164 */       addPlayer(player); 
/*     */   }
/*     */   
/*     */   public void onPlayerDisconnect(ServerPlayer player) {
/* 169 */     super.removePlayer(player);
/*     */   }
/*     */   
/*     */   public void setDirty() {
/* 174 */     this.dirtyCallback.run();
/*     */   }
/*     */   
/*     */   public static final class Packed extends Record {
/*     */     private final Component name;
/*     */     
/*     */     private final boolean visible;
/*     */     
/*     */     private final int value;
/*     */     
/*     */     private final int max;
/*     */     
/*     */     private final BossEvent.BossBarColor color;
/*     */     
/*     */     private final BossEvent.BossBarOverlay overlay;
/*     */     
/*     */     private final boolean darkenScreen;
/*     */     
/*     */     private final boolean playBossMusic;
/*     */     
/*     */     private final boolean createWorldFog;
/*     */     
/*     */     private final Set<UUID> players;
/*     */     
/*     */     public static final Codec<Packed> CODEC;
/*     */     
/*     */     public Packed(Component name, boolean visible, int value, int max, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay, boolean darkenScreen, boolean playBossMusic, boolean createWorldFog, Set<UUID> players) {
/* 177 */       this.name = name;
/* 177 */       this.visible = visible;
/* 177 */       this.value = value;
/* 177 */       this.max = max;
/* 177 */       this.color = color;
/* 177 */       this.overlay = overlay;
/* 177 */       this.darkenScreen = darkenScreen;
/* 177 */       this.playBossMusic = playBossMusic;
/* 177 */       this.createWorldFog = createWorldFog;
/* 177 */       this.players = players;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/bossevents/CustomBossEvent$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #177	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/bossevents/CustomBossEvent$Packed;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/bossevents/CustomBossEvent$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #177	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/bossevents/CustomBossEvent$Packed;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/bossevents/CustomBossEvent$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #177	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/bossevents/CustomBossEvent$Packed;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Component name() {
/* 177 */       return this.name;
/*     */     }
/*     */     
/*     */     public boolean visible() {
/* 177 */       return this.visible;
/*     */     }
/*     */     
/*     */     public int value() {
/* 177 */       return this.value;
/*     */     }
/*     */     
/*     */     public int max() {
/* 177 */       return this.max;
/*     */     }
/*     */     
/*     */     public BossEvent.BossBarColor color() {
/* 177 */       return this.color;
/*     */     }
/*     */     
/*     */     public BossEvent.BossBarOverlay overlay() {
/* 177 */       return this.overlay;
/*     */     }
/*     */     
/*     */     public boolean darkenScreen() {
/* 177 */       return this.darkenScreen;
/*     */     }
/*     */     
/*     */     public boolean playBossMusic() {
/* 177 */       return this.playBossMusic;
/*     */     }
/*     */     
/*     */     public boolean createWorldFog() {
/* 177 */       return this.createWorldFog;
/*     */     }
/*     */     
/*     */     public Set<UUID> players() {
/* 177 */       return this.players;
/*     */     }
/*     */     
/*     */     static {
/* 189 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)ComponentSerialization.CODEC.fieldOf("Name").forGetter(Packed::name), (App)Codec.BOOL.optionalFieldOf("Visible", Boolean.valueOf(false)).forGetter(Packed::visible), (App)Codec.INT.optionalFieldOf("Value", Integer.valueOf(0)).forGetter(Packed::value), (App)Codec.INT.optionalFieldOf("Max", Integer.valueOf(100)).forGetter(Packed::max), (App)BossEvent.BossBarColor.CODEC.optionalFieldOf("Color", BossEvent.BossBarColor.WHITE).forGetter(Packed::color), (App)BossEvent.BossBarOverlay.CODEC.optionalFieldOf("Overlay", BossEvent.BossBarOverlay.PROGRESS).forGetter(Packed::overlay), (App)Codec.BOOL.optionalFieldOf("DarkenScreen", Boolean.valueOf(false)).forGetter(Packed::darkenScreen), (App)Codec.BOOL.optionalFieldOf("PlayBossMusic", Boolean.valueOf(false)).forGetter(Packed::playBossMusic), (App)Codec.BOOL.optionalFieldOf("CreateWorldFog", Boolean.valueOf(false)).forGetter(Packed::createWorldFog), (App)UUIDUtil.CODEC_SET.optionalFieldOf("Players", Set.of()).forGetter(Packed::players)).apply((Applicative)i, Packed::new));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\bossevents\CustomBossEvent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */