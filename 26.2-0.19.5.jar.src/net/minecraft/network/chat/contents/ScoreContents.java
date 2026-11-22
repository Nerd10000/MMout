/*    */ package net.minecraft.network.chat.contents;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentContents;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.ResolutionContext;
/*    */ import net.minecraft.network.chat.numbers.NumberFormat;
/*    */ import net.minecraft.network.chat.numbers.StyledFormat;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.ServerScoreboard;
/*    */ import net.minecraft.util.CompilableString;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.scores.Objective;
/*    */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*    */ import net.minecraft.world.scores.ScoreHolder;
/*    */ 
/*    */ public final class ScoreContents extends Record implements ComponentContents {
/*    */   private final Either<CompilableString<EntitySelector>, String> name;
/*    */   
/*    */   private final String objective;
/*    */   
/*    */   public static final MapCodec<ScoreContents> INNER_CODEC;
/*    */   
/*    */   public ScoreContents(Either<CompilableString<EntitySelector>, String> name, String objective) {
/* 27 */     this.name = name;
/* 27 */     this.objective = objective;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/ScoreContents;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/ScoreContents;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/ScoreContents;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/ScoreContents;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Either<CompilableString<EntitySelector>, String> name() {
/* 27 */     return this.name;
/*    */   }
/*    */   
/*    */   public String objective() {
/* 27 */     return this.objective;
/*    */   }
/*    */   
/*    */   static {
/* 28 */     INNER_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.either(EntitySelector.COMPILABLE_CODEC, (Codec)Codec.STRING).fieldOf("name").forGetter(ScoreContents::name), (App)Codec.STRING.fieldOf("objective").forGetter(ScoreContents::objective)).apply((Applicative)i, ScoreContents::new));
/*    */   }
/*    */   
/* 33 */   public static final MapCodec<ScoreContents> MAP_CODEC = INNER_CODEC.fieldOf("score");
/*    */   
/*    */   public MapCodec<ScoreContents> codec() {
/* 37 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   private ScoreHolder findTargetName(CommandSourceStack source) throws CommandSyntaxException {
/* 41 */     Optional<CompilableString<EntitySelector>> selector = this.name.left();
/* 42 */     if (selector.isPresent()) {
/* 43 */       List<? extends Entity> entities = ((EntitySelector)((CompilableString)selector.get()).compiled()).findEntities(source);
/* 44 */       if (!entities.isEmpty()) {
/* 45 */         if (entities.size() != 1)
/* 46 */           throw EntityArgument.ERROR_NOT_SINGLE_ENTITY.create(); 
/* 48 */         return (ScoreHolder)entities.getFirst();
/*    */       } 
/* 50 */       return ScoreHolder.forNameOnly(((CompilableString)selector.get()).source());
/*    */     } 
/* 52 */     return ScoreHolder.forNameOnly(this.name.right().orElseThrow());
/*    */   }
/*    */   
/*    */   private MutableComponent getScore(ScoreHolder name, CommandSourceStack source) {
/* 56 */     MinecraftServer server = source.getServer();
/* 57 */     if (server != null) {
/* 58 */       ServerScoreboard serverScoreboard = server.getScoreboard();
/* 59 */       Objective objective = serverScoreboard.getObjective(this.objective);
/* 61 */       if (objective != null) {
/* 62 */         ReadOnlyScoreInfo scoreInfo = serverScoreboard.getPlayerScoreInfo(name, objective);
/* 63 */         if (scoreInfo != null)
/* 64 */           return scoreInfo.formatValue(objective.numberFormatOrDefault((NumberFormat)StyledFormat.NO_STYLE)); 
/*    */       } 
/*    */     } 
/* 68 */     return Component.empty();
/*    */   }
/*    */   
/*    */   public MutableComponent resolve(ResolutionContext context, int recursionDepth) throws CommandSyntaxException {
/* 73 */     CommandSourceStack source = context.source();
/* 74 */     if (source == null)
/* 75 */       return Component.empty(); 
/* 78 */     ScoreHolder scoreHolder = findTargetName(source);
/* 79 */     Entity entity = context.defaultScoreboardEntity();
/* 80 */     ScoreHolder scoreName = (entity != null && scoreHolder.equals(ScoreHolder.WILDCARD)) ? (ScoreHolder)entity : scoreHolder;
/* 81 */     return getScore(scoreName, source);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 86 */     return "score{name='" + String.valueOf(this.name) + "', objective='" + this.objective + "'}";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\contents\ScoreContents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */