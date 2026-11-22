/*     */ package net.minecraft.world.scores;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function11;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ 
/*     */ public class PlayerTeam extends Team {
/*     */   private static final byte BIT_FRIENDLY_FIRE = 1;
/*     */   
/*     */   private static final byte BIT_SEE_INVISIBLES = 2;
/*     */   
/*     */   private final Scoreboard scoreboard;
/*     */   
/*     */   private final String name;
/*     */   
/*  31 */   private final Set<String> players = Sets.newHashSet();
/*     */   
/*     */   private Component displayName;
/*     */   
/*  33 */   private Component playerPrefix = CommonComponents.EMPTY;
/*     */   
/*  34 */   private Component playerSuffix = CommonComponents.EMPTY;
/*     */   
/*     */   private boolean allowFriendlyFire = true;
/*     */   
/*     */   private boolean seeFriendlyInvisibles = true;
/*     */   
/*  37 */   private Team.Visibility nameTagVisibility = Team.Visibility.ALWAYS;
/*     */   
/*  38 */   private Team.Visibility deathMessageVisibility = Team.Visibility.ALWAYS;
/*     */   
/*  39 */   private Optional<TeamColor> color = Optional.empty();
/*     */   
/*  40 */   private Team.CollisionRule collisionRule = Team.CollisionRule.ALWAYS;
/*     */   
/*     */   private final Style displayNameStyle;
/*     */   
/*     */   public PlayerTeam(Scoreboard scoreboard, String name) {
/*  44 */     this.scoreboard = scoreboard;
/*  45 */     this.name = name;
/*  46 */     this.displayName = (Component)Component.literal(name);
/*  48 */     this
/*     */       
/*  50 */       .displayNameStyle = Style.EMPTY.withInsertion(name).withHoverEvent((HoverEvent)new HoverEvent.ShowText((Component)Component.literal(name)));
/*     */   }
/*     */   
/*     */   public Packed pack() {
/*  54 */     return new Packed(this.name, 
/*     */         
/*  56 */         Optional.of(this.displayName), this.color, this.allowFriendlyFire, this.seeFriendlyInvisibles, this.playerPrefix, this.playerSuffix, this.nameTagVisibility, this.deathMessageVisibility, this.collisionRule, 
/*     */         
/*  65 */         List.copyOf(this.players));
/*     */   }
/*     */   
/*     */   public Scoreboard getScoreboard() {
/*  70 */     return this.scoreboard;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  75 */     return this.name;
/*     */   }
/*     */   
/*     */   public Component getDisplayName() {
/*  79 */     return this.displayName;
/*     */   }
/*     */   
/*     */   private MutableComponent applyColor(MutableComponent result) {
/*  83 */     this.color.ifPresent(teamColor -> result.withColor(teamColor.textColor()));
/*  84 */     return result;
/*     */   }
/*     */   
/*     */   public MutableComponent getFormattedDisplayName() {
/*  88 */     return applyColor(ComponentUtils.wrapInSquareBrackets((Component)this.displayName.copy().withStyle(this.displayNameStyle)));
/*     */   }
/*     */   
/*     */   public void setDisplayName(Component displayName) {
/*  92 */     if (displayName == null)
/*  93 */       throw new IllegalArgumentException("Name cannot be null"); 
/*  95 */     this.displayName = displayName;
/*  96 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public void setPlayerPrefix(Component playerPrefix) {
/* 100 */     this.playerPrefix = (playerPrefix == null) ? CommonComponents.EMPTY : playerPrefix;
/* 101 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public Component getPlayerPrefix() {
/* 105 */     return this.playerPrefix;
/*     */   }
/*     */   
/*     */   public void setPlayerSuffix(Component playerSuffix) {
/* 109 */     this.playerSuffix = (playerSuffix == null) ? CommonComponents.EMPTY : playerSuffix;
/* 110 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public Component getPlayerSuffix() {
/* 114 */     return this.playerSuffix;
/*     */   }
/*     */   
/*     */   public Collection<String> getPlayers() {
/* 119 */     return this.players;
/*     */   }
/*     */   
/*     */   public MutableComponent getFormattedName(Component teamMemberName) {
/* 124 */     return applyColor(Component.empty().append(this.playerPrefix).append(teamMemberName).append(this.playerSuffix));
/*     */   }
/*     */   
/*     */   public static MutableComponent formatNameForTeam(Team team, Component name) {
/* 128 */     if (team == null)
/* 129 */       return name.copy(); 
/* 131 */     return team.getFormattedName(name);
/*     */   }
/*     */   
/*     */   public boolean isAllowFriendlyFire() {
/* 136 */     return this.allowFriendlyFire;
/*     */   }
/*     */   
/*     */   public void setAllowFriendlyFire(boolean allowFriendlyFire) {
/* 140 */     this.allowFriendlyFire = allowFriendlyFire;
/* 141 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public boolean canSeeFriendlyInvisibles() {
/* 146 */     return this.seeFriendlyInvisibles;
/*     */   }
/*     */   
/*     */   public void setSeeFriendlyInvisibles(boolean seeFriendlyInvisibles) {
/* 150 */     this.seeFriendlyInvisibles = seeFriendlyInvisibles;
/* 151 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public Team.Visibility getNameTagVisibility() {
/* 156 */     return this.nameTagVisibility;
/*     */   }
/*     */   
/*     */   public Team.Visibility getDeathMessageVisibility() {
/* 161 */     return this.deathMessageVisibility;
/*     */   }
/*     */   
/*     */   public void setNameTagVisibility(Team.Visibility visibility) {
/* 165 */     this.nameTagVisibility = visibility;
/* 166 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public void setDeathMessageVisibility(Team.Visibility visibility) {
/* 170 */     this.deathMessageVisibility = visibility;
/* 171 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public Team.CollisionRule getCollisionRule() {
/* 176 */     return this.collisionRule;
/*     */   }
/*     */   
/*     */   public void setCollisionRule(Team.CollisionRule collisionRule) {
/* 180 */     this.collisionRule = collisionRule;
/* 181 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public byte packOptions() {
/* 185 */     byte result = 0;
/* 187 */     if (isAllowFriendlyFire())
/* 188 */       result = (byte)(result | 0x1); 
/* 190 */     if (canSeeFriendlyInvisibles())
/* 191 */       result = (byte)(result | 0x2); 
/* 194 */     return result;
/*     */   }
/*     */   
/*     */   public void unpackOptions(byte options) {
/* 198 */     setAllowFriendlyFire(((options & 0x1) != 0));
/* 199 */     setSeeFriendlyInvisibles(((options & 0x2) != 0));
/*     */   }
/*     */   
/*     */   public void setColor(Optional<TeamColor> color) {
/* 203 */     this.color = color;
/* 204 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public Optional<TeamColor> getColor() {
/* 209 */     return this.color;
/*     */   }
/*     */   
/*     */   public static final class Packed extends Record {
/*     */     private final String name;
/*     */     
/*     */     private final Optional<Component> displayName;
/*     */     
/*     */     private final Optional<TeamColor> color;
/*     */     
/*     */     private final boolean allowFriendlyFire;
/*     */     
/*     */     private final boolean seeFriendlyInvisibles;
/*     */     
/*     */     private final Component memberNamePrefix;
/*     */     
/*     */     private final Component memberNameSuffix;
/*     */     
/*     */     private final Team.Visibility nameTagVisibility;
/*     */     
/*     */     private final Team.Visibility deathMessageVisibility;
/*     */     
/*     */     private final Team.CollisionRule collisionRule;
/*     */     
/*     */     private final List<String> players;
/*     */     
/*     */     public static final Codec<Packed> CODEC;
/*     */     
/*     */     public Packed(String name, Optional<Component> displayName, Optional<TeamColor> color, boolean allowFriendlyFire, boolean seeFriendlyInvisibles, Component memberNamePrefix, Component memberNameSuffix, Team.Visibility nameTagVisibility, Team.Visibility deathMessageVisibility, Team.CollisionRule collisionRule, List<String> players) {
/* 212 */       this.name = name;
/* 212 */       this.displayName = displayName;
/* 212 */       this.color = color;
/* 212 */       this.allowFriendlyFire = allowFriendlyFire;
/* 212 */       this.seeFriendlyInvisibles = seeFriendlyInvisibles;
/* 212 */       this.memberNamePrefix = memberNamePrefix;
/* 212 */       this.memberNameSuffix = memberNameSuffix;
/* 212 */       this.nameTagVisibility = nameTagVisibility;
/* 212 */       this.deathMessageVisibility = deathMessageVisibility;
/* 212 */       this.collisionRule = collisionRule;
/* 212 */       this.players = players;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/scores/PlayerTeam$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #212	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/scores/PlayerTeam$Packed;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/scores/PlayerTeam$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #212	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/scores/PlayerTeam$Packed;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/scores/PlayerTeam$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #212	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/scores/PlayerTeam$Packed;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String name() {
/* 212 */       return this.name;
/*     */     }
/*     */     
/*     */     public Optional<Component> displayName() {
/* 212 */       return this.displayName;
/*     */     }
/*     */     
/*     */     public Optional<TeamColor> color() {
/* 212 */       return this.color;
/*     */     }
/*     */     
/*     */     public boolean allowFriendlyFire() {
/* 212 */       return this.allowFriendlyFire;
/*     */     }
/*     */     
/*     */     public boolean seeFriendlyInvisibles() {
/* 212 */       return this.seeFriendlyInvisibles;
/*     */     }
/*     */     
/*     */     public Component memberNamePrefix() {
/* 212 */       return this.memberNamePrefix;
/*     */     }
/*     */     
/*     */     public Component memberNameSuffix() {
/* 212 */       return this.memberNameSuffix;
/*     */     }
/*     */     
/*     */     public Team.Visibility nameTagVisibility() {
/* 212 */       return this.nameTagVisibility;
/*     */     }
/*     */     
/*     */     public Team.Visibility deathMessageVisibility() {
/* 212 */       return this.deathMessageVisibility;
/*     */     }
/*     */     
/*     */     public Team.CollisionRule collisionRule() {
/* 212 */       return this.collisionRule;
/*     */     }
/*     */     
/*     */     public List<String> players() {
/* 212 */       return this.players;
/*     */     }
/*     */     
/*     */     static {
/* 225 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("Name").forGetter(Packed::name), (App)ComponentSerialization.CODEC.optionalFieldOf("DisplayName").forGetter(Packed::displayName), (App)TeamColor.CODEC.optionalFieldOf("TeamColor").forGetter(Packed::color), (App)Codec.BOOL.optionalFieldOf("AllowFriendlyFire", Boolean.valueOf(true)).forGetter(Packed::allowFriendlyFire), (App)Codec.BOOL.optionalFieldOf("SeeFriendlyInvisibles", Boolean.valueOf(true)).forGetter(Packed::seeFriendlyInvisibles), (App)ComponentSerialization.CODEC.optionalFieldOf("MemberNamePrefix", CommonComponents.EMPTY).forGetter(Packed::memberNamePrefix), (App)ComponentSerialization.CODEC.optionalFieldOf("MemberNameSuffix", CommonComponents.EMPTY).forGetter(Packed::memberNameSuffix), (App)Team.Visibility.CODEC.optionalFieldOf("NameTagVisibility", Team.Visibility.ALWAYS).forGetter(Packed::nameTagVisibility), (App)Team.Visibility.CODEC.optionalFieldOf("DeathMessageVisibility", Team.Visibility.ALWAYS).forGetter(Packed::deathMessageVisibility), (App)Team.CollisionRule.CODEC.optionalFieldOf("CollisionRule", Team.CollisionRule.ALWAYS).forGetter(Packed::collisionRule), (App)Codec.STRING.listOf().optionalFieldOf("Players", List.of()).forGetter(Packed::players)).apply((Applicative)i, Packed::new));
/*     */     }
/*     */   }
/*     */   
/*     */   @Retention(RetentionPolicy.CLASS)
/*     */   @Target({ElementType.TYPE_USE})
/*     */   public static @interface OptionFlags {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\scores\PlayerTeam.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */