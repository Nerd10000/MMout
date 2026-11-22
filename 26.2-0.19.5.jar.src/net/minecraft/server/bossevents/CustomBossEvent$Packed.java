/*     */ package net.minecraft.server.bossevents;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function10;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.world.BossEvent;
/*     */ 
/*     */ public final class Packed extends Record {
/*     */   private final Component name;
/*     */   
/*     */   private final boolean visible;
/*     */   
/*     */   private final int value;
/*     */   
/*     */   private final int max;
/*     */   
/*     */   private final BossEvent.BossBarColor color;
/*     */   
/*     */   private final BossEvent.BossBarOverlay overlay;
/*     */   
/*     */   private final boolean darkenScreen;
/*     */   
/*     */   private final boolean playBossMusic;
/*     */   
/*     */   private final boolean createWorldFog;
/*     */   
/*     */   private final Set<UUID> players;
/*     */   
/*     */   public static final Codec<Packed> CODEC;
/*     */   
/*     */   public Packed(Component name, boolean visible, int value, int max, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay, boolean darkenScreen, boolean playBossMusic, boolean createWorldFog, Set<UUID> players) {
/* 177 */     this.name = name;
/* 177 */     this.visible = visible;
/* 177 */     this.value = value;
/* 177 */     this.max = max;
/* 177 */     this.color = color;
/* 177 */     this.overlay = overlay;
/* 177 */     this.darkenScreen = darkenScreen;
/* 177 */     this.playBossMusic = playBossMusic;
/* 177 */     this.createWorldFog = createWorldFog;
/* 177 */     this.players = players;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/bossevents/CustomBossEvent$Packed;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #177	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/bossevents/CustomBossEvent$Packed;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/bossevents/CustomBossEvent$Packed;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #177	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/bossevents/CustomBossEvent$Packed;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/bossevents/CustomBossEvent$Packed;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #177	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/bossevents/CustomBossEvent$Packed;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Component name() {
/* 177 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean visible() {
/* 177 */     return this.visible;
/*     */   }
/*     */   
/*     */   public int value() {
/* 177 */     return this.value;
/*     */   }
/*     */   
/*     */   public int max() {
/* 177 */     return this.max;
/*     */   }
/*     */   
/*     */   public BossEvent.BossBarColor color() {
/* 177 */     return this.color;
/*     */   }
/*     */   
/*     */   public BossEvent.BossBarOverlay overlay() {
/* 177 */     return this.overlay;
/*     */   }
/*     */   
/*     */   public boolean darkenScreen() {
/* 177 */     return this.darkenScreen;
/*     */   }
/*     */   
/*     */   public boolean playBossMusic() {
/* 177 */     return this.playBossMusic;
/*     */   }
/*     */   
/*     */   public boolean createWorldFog() {
/* 177 */     return this.createWorldFog;
/*     */   }
/*     */   
/*     */   public Set<UUID> players() {
/* 177 */     return this.players;
/*     */   }
/*     */   
/*     */   static {
/* 189 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ComponentSerialization.CODEC.fieldOf("Name").forGetter(Packed::name), (App)Codec.BOOL.optionalFieldOf("Visible", Boolean.valueOf(false)).forGetter(Packed::visible), (App)Codec.INT.optionalFieldOf("Value", Integer.valueOf(0)).forGetter(Packed::value), (App)Codec.INT.optionalFieldOf("Max", Integer.valueOf(100)).forGetter(Packed::max), (App)BossEvent.BossBarColor.CODEC.optionalFieldOf("Color", BossEvent.BossBarColor.WHITE).forGetter(Packed::color), (App)BossEvent.BossBarOverlay.CODEC.optionalFieldOf("Overlay", BossEvent.BossBarOverlay.PROGRESS).forGetter(Packed::overlay), (App)Codec.BOOL.optionalFieldOf("DarkenScreen", Boolean.valueOf(false)).forGetter(Packed::darkenScreen), (App)Codec.BOOL.optionalFieldOf("PlayBossMusic", Boolean.valueOf(false)).forGetter(Packed::playBossMusic), (App)Codec.BOOL.optionalFieldOf("CreateWorldFog", Boolean.valueOf(false)).forGetter(Packed::createWorldFog), (App)UUIDUtil.CODEC_SET.optionalFieldOf("Players", Set.of()).forGetter(Packed::players)).apply((Applicative)i, Packed::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\bossevents\CustomBossEvent$Packed.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */