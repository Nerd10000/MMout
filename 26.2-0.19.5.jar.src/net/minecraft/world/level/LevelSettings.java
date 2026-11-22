/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.Decoder;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.Difficulty;
/*    */ 
/*    */ public final class LevelSettings extends Record {
/*    */   private final String levelName;
/*    */   
/*    */   private final GameType gameType;
/*    */   
/*    */   private final DifficultySettings difficultySettings;
/*    */   
/*    */   private final boolean allowCommands;
/*    */   
/*    */   private final WorldDataConfiguration dataConfiguration;
/*    */   
/*    */   public LevelSettings(String levelName, GameType gameType, DifficultySettings difficultySettings, boolean allowCommands, WorldDataConfiguration dataConfiguration) {
/*  9 */     this.levelName = levelName;
/*  9 */     this.gameType = gameType;
/*  9 */     this.difficultySettings = difficultySettings;
/*  9 */     this.allowCommands = allowCommands;
/*  9 */     this.dataConfiguration = dataConfiguration;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/LevelSettings;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/LevelSettings;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/LevelSettings;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/LevelSettings;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/LevelSettings;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/LevelSettings;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String levelName() {
/*  9 */     return this.levelName;
/*    */   }
/*    */   
/*    */   public GameType gameType() {
/*  9 */     return this.gameType;
/*    */   }
/*    */   
/*    */   public DifficultySettings difficultySettings() {
/*  9 */     return this.difficultySettings;
/*    */   }
/*    */   
/*    */   public boolean allowCommands() {
/*  9 */     return this.allowCommands;
/*    */   }
/*    */   
/*    */   public WorldDataConfiguration dataConfiguration() {
/*  9 */     return this.dataConfiguration;
/*    */   }
/*    */   
/*    */   public static LevelSettings parse(Dynamic<?> input, WorldDataConfiguration loadConfig) {
/* 17 */     GameType gameType = GameType.byId(input.get("GameType").asInt(0));
/* 18 */     return new LevelSettings(input.get("LevelName").asString(""), gameType, input
/*    */         
/* 20 */         .get("difficulty_settings").read((Decoder)DifficultySettings.CODEC).result().orElse(DifficultySettings.DEFAULT), input
/* 21 */         .get("allowCommands").asBoolean((gameType == GameType.CREATIVE)), loadConfig);
/*    */   }
/*    */   
/*    */   public LevelSettings withGameType(GameType gameType) {
/* 27 */     return new LevelSettings(this.levelName, gameType, this.difficultySettings, this.allowCommands, this.dataConfiguration);
/*    */   }
/*    */   
/*    */   public LevelSettings withAllowCommands(boolean allowCommands) {
/* 31 */     return new LevelSettings(this.levelName, this.gameType, this.difficultySettings, allowCommands, this.dataConfiguration);
/*    */   }
/*    */   
/*    */   public LevelSettings withDifficulty(Difficulty difficulty) {
/* 35 */     return new LevelSettings(this.levelName, this.gameType, new DifficultySettings(difficulty, this.difficultySettings.hardcore(), this.difficultySettings.locked()), this.allowCommands, this.dataConfiguration);
/*    */   }
/*    */   
/*    */   public LevelSettings withDifficultyLock(boolean locked) {
/* 39 */     return new LevelSettings(this.levelName, this.gameType, new DifficultySettings(this.difficultySettings.difficulty(), this.difficultySettings.hardcore(), locked), this.allowCommands, this.dataConfiguration);
/*    */   }
/*    */   
/*    */   public LevelSettings withDataConfiguration(WorldDataConfiguration dataConfiguration) {
/* 43 */     return new LevelSettings(this.levelName, this.gameType, this.difficultySettings, this.allowCommands, dataConfiguration);
/*    */   }
/*    */   
/*    */   public LevelSettings copy() {
/* 47 */     return new LevelSettings(this.levelName, this.gameType, this.difficultySettings, this.allowCommands, this.dataConfiguration);
/*    */   }
/*    */   
/*    */   public static final class DifficultySettings extends Record {
/*    */     private final Difficulty difficulty;
/*    */     
/*    */     private final boolean hardcore;
/*    */     
/*    */     private final boolean locked;
/*    */     
/*    */     public DifficultySettings(Difficulty difficulty, boolean hardcore, boolean locked) {
/* 50 */       this.difficulty = difficulty;
/* 50 */       this.hardcore = hardcore;
/* 50 */       this.locked = locked;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/LevelSettings$DifficultySettings;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #50	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/LevelSettings$DifficultySettings;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/LevelSettings$DifficultySettings;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #50	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/LevelSettings$DifficultySettings;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/LevelSettings$DifficultySettings;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #50	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/LevelSettings$DifficultySettings;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Difficulty difficulty() {
/* 50 */       return this.difficulty;
/*    */     }
/*    */     
/*    */     public boolean hardcore() {
/* 50 */       return this.hardcore;
/*    */     }
/*    */     
/*    */     public boolean locked() {
/* 50 */       return this.locked;
/*    */     }
/*    */     
/* 51 */     public static final DifficultySettings DEFAULT = new DifficultySettings(Difficulty.NORMAL, false, false);
/*    */     
/*    */     public static final Codec<DifficultySettings> CODEC;
/*    */     
/*    */     static {
/* 52 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Difficulty.CODEC.fieldOf("difficulty").forGetter(DifficultySettings::difficulty), (App)Codec.BOOL.fieldOf("hardcore").forGetter(DifficultySettings::hardcore), (App)Codec.BOOL.fieldOf("locked").forGetter(DifficultySettings::locked)).apply((Applicative)i, DifficultySettings::new));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\LevelSettings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */