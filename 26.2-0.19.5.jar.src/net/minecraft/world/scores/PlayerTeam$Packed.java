/*     */ package net.minecraft.world.scores;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function11;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ 
/*     */ public final class Packed extends Record {
/*     */   private final String name;
/*     */   
/*     */   private final Optional<Component> displayName;
/*     */   
/*     */   private final Optional<TeamColor> color;
/*     */   
/*     */   private final boolean allowFriendlyFire;
/*     */   
/*     */   private final boolean seeFriendlyInvisibles;
/*     */   
/*     */   private final Component memberNamePrefix;
/*     */   
/*     */   private final Component memberNameSuffix;
/*     */   
/*     */   private final Team.Visibility nameTagVisibility;
/*     */   
/*     */   private final Team.Visibility deathMessageVisibility;
/*     */   
/*     */   private final Team.CollisionRule collisionRule;
/*     */   
/*     */   private final List<String> players;
/*     */   
/*     */   public static final Codec<Packed> CODEC;
/*     */   
/*     */   public Packed(String name, Optional<Component> displayName, Optional<TeamColor> color, boolean allowFriendlyFire, boolean seeFriendlyInvisibles, Component memberNamePrefix, Component memberNameSuffix, Team.Visibility nameTagVisibility, Team.Visibility deathMessageVisibility, Team.CollisionRule collisionRule, List<String> players) {
/* 212 */     this.name = name;
/* 212 */     this.displayName = displayName;
/* 212 */     this.color = color;
/* 212 */     this.allowFriendlyFire = allowFriendlyFire;
/* 212 */     this.seeFriendlyInvisibles = seeFriendlyInvisibles;
/* 212 */     this.memberNamePrefix = memberNamePrefix;
/* 212 */     this.memberNameSuffix = memberNameSuffix;
/* 212 */     this.nameTagVisibility = nameTagVisibility;
/* 212 */     this.deathMessageVisibility = deathMessageVisibility;
/* 212 */     this.collisionRule = collisionRule;
/* 212 */     this.players = players;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/scores/PlayerTeam$Packed;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #212	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/scores/PlayerTeam$Packed;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/scores/PlayerTeam$Packed;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #212	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/scores/PlayerTeam$Packed;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/scores/PlayerTeam$Packed;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #212	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/scores/PlayerTeam$Packed;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String name() {
/* 212 */     return this.name;
/*     */   }
/*     */   
/*     */   public Optional<Component> displayName() {
/* 212 */     return this.displayName;
/*     */   }
/*     */   
/*     */   public Optional<TeamColor> color() {
/* 212 */     return this.color;
/*     */   }
/*     */   
/*     */   public boolean allowFriendlyFire() {
/* 212 */     return this.allowFriendlyFire;
/*     */   }
/*     */   
/*     */   public boolean seeFriendlyInvisibles() {
/* 212 */     return this.seeFriendlyInvisibles;
/*     */   }
/*     */   
/*     */   public Component memberNamePrefix() {
/* 212 */     return this.memberNamePrefix;
/*     */   }
/*     */   
/*     */   public Component memberNameSuffix() {
/* 212 */     return this.memberNameSuffix;
/*     */   }
/*     */   
/*     */   public Team.Visibility nameTagVisibility() {
/* 212 */     return this.nameTagVisibility;
/*     */   }
/*     */   
/*     */   public Team.Visibility deathMessageVisibility() {
/* 212 */     return this.deathMessageVisibility;
/*     */   }
/*     */   
/*     */   public Team.CollisionRule collisionRule() {
/* 212 */     return this.collisionRule;
/*     */   }
/*     */   
/*     */   public List<String> players() {
/* 212 */     return this.players;
/*     */   }
/*     */   
/*     */   static {
/* 225 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("Name").forGetter(Packed::name), (App)ComponentSerialization.CODEC.optionalFieldOf("DisplayName").forGetter(Packed::displayName), (App)TeamColor.CODEC.optionalFieldOf("TeamColor").forGetter(Packed::color), (App)Codec.BOOL.optionalFieldOf("AllowFriendlyFire", Boolean.valueOf(true)).forGetter(Packed::allowFriendlyFire), (App)Codec.BOOL.optionalFieldOf("SeeFriendlyInvisibles", Boolean.valueOf(true)).forGetter(Packed::seeFriendlyInvisibles), (App)ComponentSerialization.CODEC.optionalFieldOf("MemberNamePrefix", CommonComponents.EMPTY).forGetter(Packed::memberNamePrefix), (App)ComponentSerialization.CODEC.optionalFieldOf("MemberNameSuffix", CommonComponents.EMPTY).forGetter(Packed::memberNameSuffix), (App)Team.Visibility.CODEC.optionalFieldOf("NameTagVisibility", Team.Visibility.ALWAYS).forGetter(Packed::nameTagVisibility), (App)Team.Visibility.CODEC.optionalFieldOf("DeathMessageVisibility", Team.Visibility.ALWAYS).forGetter(Packed::deathMessageVisibility), (App)Team.CollisionRule.CODEC.optionalFieldOf("CollisionRule", Team.CollisionRule.ALWAYS).forGetter(Packed::collisionRule), (App)Codec.STRING.listOf().optionalFieldOf("Players", List.of()).forGetter(Packed::players)).apply((Applicative)i, Packed::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\scores\PlayerTeam$Packed.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */