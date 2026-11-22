/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.functions.CommandFunction;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.ServerFunctionManager;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.util.Unit;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public final class Functions extends Record implements TestEnvironmentDefinition<Unit> {
/*     */   private final Optional<Identifier> setupFunction;
/*     */   
/*     */   private final Optional<Identifier> teardownFunction;
/*     */   
/*     */   public Functions(Optional<Identifier> setupFunction, Optional<Identifier> teardownFunction) {
/* 199 */     this.setupFunction = setupFunction;
/* 199 */     this.teardownFunction = teardownFunction;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #199	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #199	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #199	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Optional<Identifier> setupFunction() {
/* 199 */     return this.setupFunction;
/*     */   }
/*     */   
/*     */   public Optional<Identifier> teardownFunction() {
/* 199 */     return this.teardownFunction;
/*     */   }
/*     */   
/* 200 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final MapCodec<Functions> CODEC;
/*     */   
/*     */   static {
/* 202 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.optionalFieldOf("setup").forGetter(Functions::setupFunction), (App)Identifier.CODEC.optionalFieldOf("teardown").forGetter(Functions::teardownFunction)).apply((Applicative)i, Functions::new));
/*     */   }
/*     */   
/*     */   public Unit setup(ServerLevel level) {
/* 209 */     this.setupFunction.ifPresent(p -> run(level, p));
/* 210 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public void teardown(ServerLevel level, Unit saveData) {
/* 215 */     this.teardownFunction.ifPresent(p -> run(level, p));
/*     */   }
/*     */   
/*     */   private static void run(ServerLevel level, Identifier functionId) {
/* 219 */     MinecraftServer server = level.getServer();
/* 220 */     ServerFunctionManager functions = server.getFunctions();
/* 221 */     Optional<CommandFunction<CommandSourceStack>> function = functions.get(functionId);
/* 222 */     if (function.isPresent()) {
/* 226 */       CommandSourceStack source = server.createCommandSourceStack().withPermission((PermissionSet)LevelBasedPermissionSet.GAMEMASTER).withSuppressedOutput().withLevel(level);
/* 227 */       functions.execute(function.get(), source);
/*     */     } else {
/* 229 */       LOGGER.error("Test Batch failed for non-existent function {}", functionId);
/*     */     } 
/*     */   }
/*     */   
/*     */   public MapCodec<Functions> codec() {
/* 235 */     return CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\TestEnvironmentDefinition$Functions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */