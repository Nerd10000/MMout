/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.saveddata.SavedData;
/*    */ import net.minecraft.world.level.saveddata.SavedDataType;
/*    */ 
/*    */ public class ScoreboardSaveData extends SavedData {
/* 14 */   public static final SavedDataType<ScoreboardSaveData> TYPE = new SavedDataType(
/* 15 */       Identifier.withDefaultNamespace("scoreboard"), ScoreboardSaveData::new, Packed.CODEC
/*    */       
/* 17 */       .xmap(ScoreboardSaveData::new, ScoreboardSaveData::getData), DataFixTypes.SAVED_DATA_SCOREBOARD);
/*    */   
/*    */   private Packed data;
/*    */   
/*    */   private ScoreboardSaveData() {
/* 24 */     this(Packed.EMPTY);
/*    */   }
/*    */   
/*    */   public ScoreboardSaveData(Packed data) {
/* 28 */     this.data = data;
/*    */   }
/*    */   
/*    */   public Packed getData() {
/* 32 */     return this.data;
/*    */   }
/*    */   
/*    */   public void setData(Packed data) {
/* 36 */     if (!data.equals(this.data)) {
/* 37 */       this.data = data;
/* 38 */       setDirty();
/*    */     } 
/*    */   }
/*    */   
/*    */   public static final class Packed extends Record {
/*    */     private final List<Objective.Packed> objectives;
/*    */     
/*    */     private final List<Scoreboard.PackedScore> scores;
/*    */     
/*    */     private final Map<DisplaySlot, String> displaySlots;
/*    */     
/*    */     private final List<PlayerTeam.Packed> teams;
/*    */     
/*    */     public Packed(List<Objective.Packed> objectives, List<Scoreboard.PackedScore> scores, Map<DisplaySlot, String> displaySlots, List<PlayerTeam.Packed> teams) {
/* 42 */       this.objectives = objectives;
/* 42 */       this.scores = scores;
/* 42 */       this.displaySlots = displaySlots;
/* 42 */       this.teams = teams;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/scores/ScoreboardSaveData$Packed;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #42	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/scores/ScoreboardSaveData$Packed;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/scores/ScoreboardSaveData$Packed;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #42	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/scores/ScoreboardSaveData$Packed;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/scores/ScoreboardSaveData$Packed;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #42	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/scores/ScoreboardSaveData$Packed;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public List<Objective.Packed> objectives() {
/* 42 */       return this.objectives;
/*    */     }
/*    */     
/*    */     public List<Scoreboard.PackedScore> scores() {
/* 42 */       return this.scores;
/*    */     }
/*    */     
/*    */     public Map<DisplaySlot, String> displaySlots() {
/* 42 */       return this.displaySlots;
/*    */     }
/*    */     
/*    */     public List<PlayerTeam.Packed> teams() {
/* 42 */       return this.teams;
/*    */     }
/*    */     
/* 43 */     public static final Packed EMPTY = new Packed(List.of(), List.of(), Map.of(), List.of());
/*    */     
/*    */     public static final Codec<Packed> CODEC;
/*    */     
/*    */     static {
/* 45 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Objective.Packed.CODEC.listOf().optionalFieldOf("Objectives", List.of()).forGetter(Packed::objectives), (App)Scoreboard.PackedScore.CODEC.listOf().optionalFieldOf("PlayerScores", List.of()).forGetter(Packed::scores), (App)Codec.unboundedMap((Codec)DisplaySlot.CODEC, (Codec)Codec.STRING).optionalFieldOf("DisplaySlots", Map.of()).forGetter(Packed::displaySlots), (App)PlayerTeam.Packed.CODEC.listOf().optionalFieldOf("Teams", List.of()).forGetter(Packed::teams)).apply((Applicative)i, Packed::new));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\scores\ScoreboardSaveData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */