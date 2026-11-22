/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class BedRule extends Record {
/*    */   private final Rule canSleep;
/*    */   
/*    */   private final Rule canSetSpawn;
/*    */   
/*    */   private final boolean explodes;
/*    */   
/*    */   private final Optional<Component> errorMessage;
/*    */   
/*    */   public BedRule(Rule canSleep, Rule canSetSpawn, boolean explodes, Optional<Component> errorMessage) {
/* 13 */     this.canSleep = canSleep;
/* 13 */     this.canSetSpawn = canSetSpawn;
/* 13 */     this.explodes = explodes;
/* 13 */     this.errorMessage = errorMessage;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/BedRule;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/BedRule;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/BedRule;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/BedRule;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/BedRule;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/attribute/BedRule;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Rule canSleep() {
/* 13 */     return this.canSleep;
/*    */   }
/*    */   
/*    */   public Rule canSetSpawn() {
/* 13 */     return this.canSetSpawn;
/*    */   }
/*    */   
/*    */   public boolean explodes() {
/* 13 */     return this.explodes;
/*    */   }
/*    */   
/*    */   public Optional<Component> errorMessage() {
/* 13 */     return this.errorMessage;
/*    */   }
/*    */   
/* 19 */   public static final BedRule CAN_SLEEP_WHEN_DARK = new BedRule(Rule.WHEN_DARK, Rule.ALWAYS, false, 
/*    */       
/* 23 */       (Optional)Optional.of(Component.translatable("block.minecraft.bed.no_sleep")));
/*    */   
/* 25 */   public static final BedRule EXPLODES = new BedRule(Rule.NEVER, Rule.NEVER, true, Optional.empty());
/*    */   
/*    */   public static final Codec<BedRule> CODEC;
/*    */   
/*    */   static {
/* 27 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Rule.CODEC.fieldOf("can_sleep").forGetter(BedRule::canSleep), (App)Rule.CODEC.fieldOf("can_set_spawn").forGetter(BedRule::canSetSpawn), (App)Codec.BOOL.optionalFieldOf("explodes", Boolean.valueOf(false)).forGetter(BedRule::explodes), (App)ComponentSerialization.CODEC.optionalFieldOf("error_message").forGetter(BedRule::errorMessage)).apply((Applicative)i, BedRule::new));
/*    */   }
/*    */   
/*    */   public boolean canSleep(Level level) {
/* 35 */     return this.canSleep.test(level);
/*    */   }
/*    */   
/*    */   public boolean canSetSpawn(Level level) {
/* 39 */     return this.canSetSpawn.test(level);
/*    */   }
/*    */   
/*    */   public Player.BedSleepingProblem asProblem() {
/* 43 */     return new Player.BedSleepingProblem(this.errorMessage.orElse(null));
/*    */   }
/*    */   
/*    */   public enum Rule implements StringRepresentable {
/* 47 */     ALWAYS("always"),
/* 50 */     WHEN_DARK("when_dark"),
/* 51 */     NEVER("never");
/*    */     
/* 54 */     public static final Codec<Rule> CODEC = (Codec<Rule>)StringRepresentable.fromEnum(Rule::values);
/*    */     
/*    */     private final String name;
/*    */     
/*    */     static {
/*    */     
/*    */     }
/*    */     
/*    */     Rule(String name) {
/* 59 */       this.name = name;
/*    */     }
/*    */     
/*    */     public boolean test(Level level) {
/* 63 */       switch (ordinal()) {
/*    */         default:
/* 63 */           throw new MatchException(null, null);
/*    */         case 0:
/*    */         
/*    */         case 1:
/*    */         
/*    */         case 2:
/*    */           break;
/*    */       } 
/* 63 */       return false;
/*    */     }
/*    */     
/*    */     public String getSerializedName() {
/* 72 */       return this.name;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\BedRule.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */