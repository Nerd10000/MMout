/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.mojang.blaze3d.Blaze3D;
/*     */ import com.mojang.blaze3d.platform.ClipboardManager;
/*     */ import com.mojang.blaze3d.platform.InputConstants;
/*     */ import com.mojang.blaze3d.platform.TextureUtil;
/*     */ import com.mojang.blaze3d.platform.Window;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntries;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.debug.DebugOptionsScreen;
/*     */ import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
/*     */ import net.minecraft.client.input.CharacterEvent;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.PreeditEvent;
/*     */ import net.minecraft.client.renderer.fog.FogRenderer;
/*     */ import net.minecraft.commands.arguments.blocks.BlockStateParser;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundChangeGameModePacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.commands.GameModeCommand;
/*     */ import net.minecraft.server.commands.VersionCommand;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.NativeModuleLister;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.FeatureCountTracker;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class KeyboardHandler {
/*  66 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final int DEBUG_CRASH_TIME = 10000;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*  70 */   private final ClipboardManager clipboardManager = new ClipboardManager();
/*     */   
/*     */   private long debugCrashKeyTime;
/*     */   
/*     */   private long debugCrashKeyReportedTime;
/*     */   
/*     */   private long debugCrashKeyReportedCount;
/*     */   
/*     */   private boolean usedDebugKeyAsModifier;
/*     */   
/*     */   private PreeditEvent lastPreeditEvent;
/*     */   
/*     */   public KeyboardHandler(Minecraft minecraft) {
/*  76 */     this.debugCrashKeyTime = -1L;
/*  77 */     this.debugCrashKeyReportedTime = -1L;
/*  78 */     this.debugCrashKeyReportedCount = -1L;
/*     */     this.minecraft = minecraft;
/*     */   }
/*     */   
/*     */   private boolean handleChunkDebugKeys(KeyEvent event) {
/*     */     boolean chunkSectionPaths;
/*     */     boolean renderOctree;
/*     */     boolean fogEnabled;
/*     */     boolean sectionVisibility;
/*  84 */     switch (event.key()) {
/*     */       case 69:
/*  89 */         chunkSectionPaths = this.minecraft.debugEntries.toggleStatus(DebugScreenEntries.CHUNK_SECTION_PATHS);
/*  90 */         debugFeedback("SectionPath: " + (chunkSectionPaths ? "shown" : "hidden"));
/*     */         return !(this.minecraft.player == null);
/*     */       case 76:
/*  94 */         this.minecraft.smartCull = !this.minecraft.smartCull;
/*  95 */         debugFeedbackEnabledStatus("SmartCull: ", this.minecraft.smartCull);
/*     */       case 79:
/* 102 */         renderOctree = this.minecraft.debugEntries.toggleStatus(DebugScreenEntries.CHUNK_SECTION_OCTREE);
/* 103 */         debugFeedbackEnabledStatus("Frustum culling Octree: ", renderOctree);
/*     */         return !(this.minecraft.player == null);
/*     */       case 70:
/* 107 */         fogEnabled = FogRenderer.toggleFog();
/* 108 */         debugFeedbackEnabledStatus("Fog: ", fogEnabled);
/*     */       case 85:
/* 112 */         if (event.hasShiftDown()) {
/* 113 */           this.minecraft.gameRenderer.mainCamera().killFrustum();
/* 114 */           debugFeedback("Killed frustum");
/*     */         } else {
/* 116 */           this.minecraft.gameRenderer.mainCamera().captureFrustum();
/* 117 */           debugFeedback("Captured frustum");
/*     */         } 
/*     */       case 86:
/* 125 */         sectionVisibility = this.minecraft.debugEntries.toggleStatus(DebugScreenEntries.CHUNK_SECTION_VISIBILITY);
/* 126 */         debugFeedbackEnabledStatus("SectionVisibility: ", sectionVisibility);
/*     */         return !(this.minecraft.player == null);
/*     */       case 87:
/* 130 */         this.minecraft.wireframe = !this.minecraft.wireframe;
/* 131 */         debugFeedbackEnabledStatus("WireFrame: ", this.minecraft.wireframe);
/*     */     } 
/*     */     return false;
/*     */   }
/*     */   
/*     */   private void debugFeedbackEnabledStatus(String prefix, boolean isEnabled) {
/* 139 */     debugFeedback(prefix + prefix);
/*     */   }
/*     */   
/*     */   private static Component decorateDebugComponent(ChatFormatting formatting, Component component) {
/* 143 */     return (Component)Component.empty().append((Component)Component.translatable("debug.prefix").withStyle(new ChatFormatting[] { formatting, ChatFormatting.BOLD })).append(CommonComponents.SPACE).append(component);
/*     */   }
/*     */   
/*     */   private void debugWarningComponent(Component component) {
/* 147 */     this.minecraft.showDebugChat(decorateDebugComponent(ChatFormatting.RED, component));
/*     */   }
/*     */   
/*     */   private void debugFeedbackComponent(Component component) {
/* 151 */     this.minecraft.showDebugChat(decorateDebugComponent(ChatFormatting.YELLOW, component));
/*     */   }
/*     */   
/*     */   private void debugFeedbackTranslated(String pattern) {
/* 155 */     debugFeedbackComponent((Component)Component.translatable(pattern));
/*     */   }
/*     */   
/*     */   private void debugFeedback(String message) {
/* 159 */     debugFeedbackComponent((Component)Component.literal(message));
/*     */   }
/*     */   
/*     */   private boolean handleDebugKeys(KeyEvent event) {
/* 163 */     if (this.debugCrashKeyTime > 0L && this.debugCrashKeyTime < Util.getMillis() - 100L)
/* 164 */       return true; 
/* 167 */     if (SharedConstants.DEBUG_HOTKEYS && handleChunkDebugKeys(event))
/* 168 */       return true; 
/* 171 */     if (SharedConstants.DEBUG_FEATURE_COUNT)
/* 172 */       switch (event.key()) {
/*     */         case 82:
/* 174 */           FeatureCountTracker.clearCounts();
/* 175 */           return true;
/*     */         case 76:
/* 178 */           FeatureCountTracker.logCounts();
/* 179 */           return true;
/*     */       }  
/* 184 */     Options options = this.minecraft.options;
/* 185 */     boolean debugAction = false;
/* 186 */     if (options.keyDebugReloadChunk.matches(event)) {
/* 187 */       this.minecraft.levelExtractor.allChanged();
/* 188 */       debugFeedbackTranslated("debug.reload_chunks.message");
/* 189 */       debugAction = true;
/*     */     } 
/* 192 */     if (options.keyDebugShowHitboxes.matches(event) && 
/* 193 */       this.minecraft.player != null && !this.minecraft.player.isReducedDebugInfo()) {
/* 194 */       boolean renderHitBoxes = this.minecraft.debugEntries.toggleStatus(DebugScreenEntries.ENTITY_HITBOXES);
/* 195 */       debugFeedbackTranslated(renderHitBoxes ? "debug.show_hitboxes.on" : "debug.show_hitboxes.off");
/* 196 */       debugAction = true;
/*     */     } 
/* 200 */     if (options.keyDebugClearChat.matches(event)) {
/* 201 */       this.minecraft.gui.hud.getChat().clearMessages(false);
/* 202 */       debugAction = true;
/*     */     } 
/* 205 */     if (options.keyDebugShowChunkBorders.matches(event) && 
/* 206 */       this.minecraft.player != null && !this.minecraft.player.isReducedDebugInfo()) {
/* 207 */       boolean displayChunkborder = this.minecraft.debugEntries.toggleStatus(DebugScreenEntries.CHUNK_BORDERS);
/* 208 */       debugFeedbackTranslated(displayChunkborder ? "debug.chunk_boundaries.on" : "debug.chunk_boundaries.off");
/* 209 */       debugAction = true;
/*     */     } 
/* 213 */     if (options.keyDebugShowAdvancedTooltips.matches(event)) {
/* 214 */       options.advancedItemTooltips = !options.advancedItemTooltips;
/* 215 */       debugFeedbackTranslated(options.advancedItemTooltips ? "debug.advanced_tooltips.on" : "debug.advanced_tooltips.off");
/* 216 */       options.save();
/* 217 */       debugAction = true;
/*     */     } 
/* 220 */     if (options.keyDebugCopyRecreateCommand.matches(event)) {
/* 221 */       if (this.minecraft.player != null && !this.minecraft.player.isReducedDebugInfo())
/* 222 */         copyRecreateCommand(this.minecraft.player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER), !event.hasShiftDown()); 
/* 224 */       debugAction = true;
/*     */     } 
/* 227 */     if (options.keyDebugSpectate.matches(event)) {
/* 228 */       if (this.minecraft.player == null || !GameModeCommand.PERMISSION_CHECK.check(this.minecraft.player.permissions())) {
/* 229 */         debugFeedbackTranslated("debug.creative_spectator.error");
/* 230 */       } else if (!this.minecraft.player.isSpectator()) {
/* 231 */         this.minecraft.player.connection.send((Packet)new ServerboundChangeGameModePacket(GameType.SPECTATOR));
/*     */       } else {
/* 233 */         GameType newGameType = (GameType)MoreObjects.firstNonNull(this.minecraft.gameMode.getPreviousPlayerMode(), GameType.CREATIVE);
/* 234 */         this.minecraft.player.connection.send((Packet)new ServerboundChangeGameModePacket(newGameType));
/*     */       } 
/* 236 */       debugAction = true;
/*     */     } 
/* 239 */     if (options.keyDebugSwitchGameMode.matches(event) && this.minecraft.level != null && this.minecraft.gui.screen() == null) {
/* 240 */       if (this.minecraft.canSwitchGameMode() && GameModeCommand.PERMISSION_CHECK.check(this.minecraft.player.permissions())) {
/* 241 */         this.minecraft.gui.setScreen((Screen)new GameModeSwitcherScreen());
/*     */       } else {
/* 243 */         debugFeedbackTranslated("debug.gamemodes.error");
/*     */       } 
/* 245 */       debugAction = true;
/*     */     } 
/* 248 */     if (options.keyDebugDebugOptions.matches(event)) {
/* 249 */       if (this.minecraft.gui.screen() instanceof DebugOptionsScreen) {
/* 250 */         this.minecraft.gui.screen().onClose();
/* 251 */       } else if (this.minecraft.canInterruptScreen()) {
/* 252 */         if (this.minecraft.gui.screen() != null)
/* 253 */           this.minecraft.gui.screen().onClose(); 
/* 255 */         this.minecraft.gui.setScreen((Screen)new DebugOptionsScreen());
/*     */       } 
/* 257 */       debugAction = true;
/*     */     } 
/* 260 */     if (options.keyDebugFocusPause.matches(event)) {
/* 261 */       options.pauseOnLostFocus = !options.pauseOnLostFocus;
/* 262 */       options.save();
/* 263 */       debugFeedbackTranslated(options.pauseOnLostFocus ? "debug.pause_focus.on" : "debug.pause_focus.off");
/* 264 */       debugAction = true;
/*     */     } 
/* 267 */     if (options.keyDebugDumpDynamicTextures.matches(event)) {
/* 268 */       Path gameDirectory = this.minecraft.gameDirectory.toPath().toAbsolutePath();
/* 269 */       Path debugTexturePath = TextureUtil.getDebugTexturePath(gameDirectory);
/* 270 */       this.minecraft.getTextureManager().dumpAllSheets(debugTexturePath);
/* 274 */       MutableComponent mutableComponent = Component.literal(gameDirectory.relativize(debugTexturePath).toString()).withStyle(ChatFormatting.UNDERLINE).withStyle(s -> s.withClickEvent((ClickEvent)new ClickEvent.OpenFile(debugTexturePath)));
/* 276 */       debugFeedbackComponent((Component)Component.translatable("debug.dump_dynamic_textures", new Object[] { mutableComponent }));
/* 277 */       debugAction = true;
/*     */     } 
/* 280 */     if (options.keyDebugReloadResourcePacks.matches(event)) {
/* 281 */       debugFeedbackTranslated("debug.reload_resourcepacks.message");
/* 282 */       this.minecraft.reloadResourcePacks();
/* 283 */       debugAction = true;
/*     */     } 
/* 286 */     if (options.keyDebugProfiling.matches(event)) {
/* 287 */       if (this.minecraft.debugClientMetricsStart(this::debugFeedbackComponent))
/* 288 */         debugFeedbackComponent((Component)Component.translatable("debug.profiling.start", new Object[] { Integer.valueOf(10), options.keyDebugModifier.getTranslatedKeyMessage(), options.keyDebugProfiling.getTranslatedKeyMessage() })); 
/* 290 */       debugAction = true;
/*     */     } 
/* 293 */     if (options.keyDebugCopyLocation.matches(event) && 
/* 294 */       this.minecraft.player != null && !this.minecraft.player.isReducedDebugInfo()) {
/* 295 */       debugFeedbackTranslated("debug.copy_location.message");
/* 296 */       setClipboard(String.format(Locale.ROOT, "/execute in %s run tp @s %.2f %.2f %.2f %.2f %.2f", new Object[] { this.minecraft.player.level().dimension().identifier(), Double.valueOf(this.minecraft.player.getX()), Double.valueOf(this.minecraft.player.getY()), Double.valueOf(this.minecraft.player.getZ()), Float.valueOf(this.minecraft.player.getYRot()), Float.valueOf(this.minecraft.player.getXRot()) }));
/* 297 */       debugAction = true;
/*     */     } 
/* 301 */     if (options.keyDebugDumpVersion.matches(event)) {
/* 302 */       debugFeedbackTranslated("debug.version.header");
/* 303 */       Objects.requireNonNull(this.minecraft);
/* 303 */       VersionCommand.dumpVersion(this.minecraft::showDebugChat);
/* 304 */       debugAction = true;
/*     */     } 
/* 307 */     if (options.keyDebugPofilingChart.matches(event)) {
/* 308 */       this.minecraft.getDebugOverlay().toggleProfilerChart();
/* 309 */       debugAction = true;
/*     */     } 
/* 312 */     if (options.keyDebugFpsCharts.matches(event)) {
/* 313 */       this.minecraft.getDebugOverlay().toggleFpsCharts();
/* 314 */       debugAction = true;
/*     */     } 
/* 317 */     if (options.keyDebugNetworkCharts.matches(event)) {
/* 318 */       this.minecraft.getDebugOverlay().toggleNetworkCharts();
/* 319 */       debugAction = true;
/*     */     } 
/* 322 */     if (options.keyDebugLightmapTexture.matches(event)) {
/* 323 */       this.minecraft.getDebugOverlay().toggleLightmapTexture();
/* 324 */       debugAction = true;
/*     */     } 
/* 327 */     return debugAction;
/*     */   }
/*     */   
/*     */   private void copyRecreateCommand(boolean addNbt, boolean pullFromServer) {
/*     */     BlockPos blockPos;
/*     */     Entity entity;
/*     */     Level level;
/*     */     Identifier id;
/*     */     BlockState state;
/* 331 */     HitResult hitResult = this.minecraft.hitResult;
/* 332 */     if (hitResult == null)
/*     */       return; 
/* 336 */     switch (hitResult.getType()) {
/*     */       case BLOCK:
/* 338 */         blockPos = ((BlockHitResult)hitResult).getBlockPos();
/* 339 */         level = this.minecraft.player.level();
/* 340 */         state = level.getBlockState(blockPos);
/* 342 */         if (addNbt) {
/* 343 */           if (pullFromServer) {
/* 344 */             this.minecraft.player.connection.getDebugQueryHandler().queryBlockEntityTag(blockPos, tag -> {
/*     */                   copyCreateBlockCommand(state, blockPos, tag);
/*     */                   debugFeedbackTranslated("debug.inspect.server.block");
/*     */                 });
/*     */             break;
/*     */           } 
/* 349 */           BlockEntity blockEntity = level.getBlockEntity(blockPos);
/* 350 */           CompoundTag tag = (blockEntity != null) ? blockEntity.saveWithoutMetadata((HolderLookup.Provider)level.registryAccess()) : null;
/* 351 */           copyCreateBlockCommand(state, blockPos, tag);
/* 352 */           debugFeedbackTranslated("debug.inspect.client.block");
/*     */           break;
/*     */         } 
/* 355 */         copyCreateBlockCommand(state, blockPos, null);
/* 356 */         debugFeedbackTranslated("debug.inspect.client.block");
/*     */         break;
/*     */       case ENTITY:
/* 360 */         entity = ((EntityHitResult)hitResult).getEntity();
/* 361 */         id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
/* 362 */         if (addNbt) {
/* 363 */           if (pullFromServer) {
/* 364 */             this.minecraft.player.connection.getDebugQueryHandler().queryEntityTag(entity.getId(), tag -> {
/*     */                   copyCreateEntityCommand(id, entity.position(), tag);
/*     */                   debugFeedbackTranslated("debug.inspect.server.entity");
/*     */                 });
/*     */             break;
/*     */           } 
/* 369 */           ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(entity.problemPath(), LOGGER);
/*     */           try {
/* 370 */             TagValueOutput output = TagValueOutput.createWithContext((ProblemReporter)reporter, (HolderLookup.Provider)entity.registryAccess());
/* 371 */             entity.saveWithoutId((ValueOutput)output);
/* 372 */             copyCreateEntityCommand(id, entity.position(), output.buildResult());
/* 373 */             reporter.close();
/*     */           } catch (Throwable throwable) {
/*     */             try {
/*     */               reporter.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             } 
/*     */             throw throwable;
/*     */           } 
/* 374 */           debugFeedbackTranslated("debug.inspect.client.entity");
/*     */           break;
/*     */         } 
/* 377 */         copyCreateEntityCommand(id, entity.position(), null);
/* 378 */         debugFeedbackTranslated("debug.inspect.client.entity");
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void copyCreateBlockCommand(BlockState state, BlockPos blockPos, CompoundTag entityTag) {
/* 387 */     StringBuilder description = new StringBuilder(BlockStateParser.serialize(state));
/* 388 */     if (entityTag != null)
/* 389 */       description.append(entityTag); 
/* 391 */     String command = String.format(Locale.ROOT, "/setblock %d %d %d %s", new Object[] { Integer.valueOf(blockPos.getX()), Integer.valueOf(blockPos.getY()), Integer.valueOf(blockPos.getZ()), description });
/* 392 */     setClipboard(command);
/*     */   }
/*     */   
/*     */   private void copyCreateEntityCommand(Identifier id, Vec3 pos, CompoundTag entityTag) {
/*     */     String command;
/* 397 */     if (entityTag != null) {
/* 398 */       entityTag.remove("UUID");
/* 399 */       entityTag.remove("Pos");
/* 400 */       String snbt = NbtUtils.toPrettyComponent((Tag)entityTag).getString();
/* 401 */       command = String.format(Locale.ROOT, "/summon %s %.2f %.2f %.2f %s", new Object[] { id, Double.valueOf(pos.x), Double.valueOf(pos.y), Double.valueOf(pos.z), snbt });
/*     */     } else {
/* 403 */       command = String.format(Locale.ROOT, "/summon %s %.2f %.2f %.2f", new Object[] { id, Double.valueOf(pos.x), Double.valueOf(pos.y), Double.valueOf(pos.z) });
/*     */     } 
/* 405 */     setClipboard(command);
/*     */   }
/*     */   
/*     */   private void keyPress(long handle, int action, KeyEvent event) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   4: invokevirtual getWindow : ()Lcom/mojang/blaze3d/platform/Window;
/*     */     //   7: astore #5
/*     */     //   9: lload_1
/*     */     //   10: aload #5
/*     */     //   12: invokevirtual handle : ()J
/*     */     //   15: lcmp
/*     */     //   16: ifeq -> 20
/*     */     //   19: return
/*     */     //   20: aload_0
/*     */     //   21: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   24: invokevirtual getFramerateLimitTracker : ()Lcom/mojang/blaze3d/platform/FramerateLimitTracker;
/*     */     //   27: invokevirtual onInputReceived : ()V
/*     */     //   30: aload_0
/*     */     //   31: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   34: getfield options : Lnet/minecraft/client/Options;
/*     */     //   37: astore #6
/*     */     //   39: aload #6
/*     */     //   41: getfield keyDebugModifier : Lnet/minecraft/client/KeyMapping;
/*     */     //   44: getfield key : Lcom/mojang/blaze3d/platform/InputConstants$Key;
/*     */     //   47: invokevirtual getValue : ()I
/*     */     //   50: aload #6
/*     */     //   52: getfield keyDebugOverlay : Lnet/minecraft/client/KeyMapping;
/*     */     //   55: getfield key : Lcom/mojang/blaze3d/platform/InputConstants$Key;
/*     */     //   58: invokevirtual getValue : ()I
/*     */     //   61: if_icmpne -> 68
/*     */     //   64: iconst_1
/*     */     //   65: goto -> 69
/*     */     //   68: iconst_0
/*     */     //   69: istore #7
/*     */     //   71: aload #6
/*     */     //   73: getfield keyDebugModifier : Lnet/minecraft/client/KeyMapping;
/*     */     //   76: invokevirtual isDown : ()Z
/*     */     //   79: istore #8
/*     */     //   81: aload #6
/*     */     //   83: getfield keyDebugCrash : Lnet/minecraft/client/KeyMapping;
/*     */     //   86: invokevirtual isUnbound : ()Z
/*     */     //   89: ifne -> 120
/*     */     //   92: aload_0
/*     */     //   93: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   96: invokevirtual getWindow : ()Lcom/mojang/blaze3d/platform/Window;
/*     */     //   99: aload #6
/*     */     //   101: getfield keyDebugCrash : Lnet/minecraft/client/KeyMapping;
/*     */     //   104: getfield key : Lcom/mojang/blaze3d/platform/InputConstants$Key;
/*     */     //   107: invokevirtual getValue : ()I
/*     */     //   110: invokestatic isKeyDown : (Lcom/mojang/blaze3d/platform/Window;I)Z
/*     */     //   113: ifeq -> 120
/*     */     //   116: iconst_1
/*     */     //   117: goto -> 121
/*     */     //   120: iconst_0
/*     */     //   121: istore #9
/*     */     //   123: aload_0
/*     */     //   124: getfield debugCrashKeyTime : J
/*     */     //   127: lconst_0
/*     */     //   128: lcmp
/*     */     //   129: ifle -> 152
/*     */     //   132: iload #9
/*     */     //   134: ifeq -> 142
/*     */     //   137: iload #8
/*     */     //   139: ifne -> 187
/*     */     //   142: aload_0
/*     */     //   143: ldc2_w -1
/*     */     //   146: putfield debugCrashKeyTime : J
/*     */     //   149: goto -> 187
/*     */     //   152: iload #9
/*     */     //   154: ifeq -> 187
/*     */     //   157: iload #8
/*     */     //   159: ifeq -> 187
/*     */     //   162: aload_0
/*     */     //   163: iload #7
/*     */     //   165: putfield usedDebugKeyAsModifier : Z
/*     */     //   168: aload_0
/*     */     //   169: invokestatic getMillis : ()J
/*     */     //   172: putfield debugCrashKeyTime : J
/*     */     //   175: aload_0
/*     */     //   176: invokestatic getMillis : ()J
/*     */     //   179: putfield debugCrashKeyReportedTime : J
/*     */     //   182: aload_0
/*     */     //   183: lconst_0
/*     */     //   184: putfield debugCrashKeyReportedCount : J
/*     */     //   187: aload_0
/*     */     //   188: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   191: getfield gui : Lnet/minecraft/client/gui/Gui;
/*     */     //   194: invokevirtual screen : ()Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   197: astore #10
/*     */     //   199: aload #10
/*     */     //   201: ifnull -> 279
/*     */     //   204: aload #4
/*     */     //   206: invokevirtual key : ()I
/*     */     //   209: tableswitch default -> 279, 258 -> 269, 259 -> 279, 260 -> 279, 261 -> 279, 262 -> 256, 263 -> 256, 264 -> 256, 265 -> 256
/*     */     //   256: aload_0
/*     */     //   257: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   260: getstatic net/minecraft/client/InputType.KEYBOARD_ARROW : Lnet/minecraft/client/InputType;
/*     */     //   263: invokevirtual setLastInputType : (Lnet/minecraft/client/InputType;)V
/*     */     //   266: goto -> 279
/*     */     //   269: aload_0
/*     */     //   270: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   273: getstatic net/minecraft/client/InputType.KEYBOARD_TAB : Lnet/minecraft/client/InputType;
/*     */     //   276: invokevirtual setLastInputType : (Lnet/minecraft/client/InputType;)V
/*     */     //   279: iload_3
/*     */     //   280: iconst_1
/*     */     //   281: if_icmpne -> 336
/*     */     //   284: aload #10
/*     */     //   286: instanceof net/minecraft/client/gui/screens/options/controls/KeyBindsScreen
/*     */     //   289: ifeq -> 315
/*     */     //   292: aload #10
/*     */     //   294: checkcast net/minecraft/client/gui/screens/options/controls/KeyBindsScreen
/*     */     //   297: astore #11
/*     */     //   299: aload #11
/*     */     //   301: getfield lastKeySelection : J
/*     */     //   304: invokestatic getMillis : ()J
/*     */     //   307: ldc2_w 20
/*     */     //   310: lsub
/*     */     //   311: lcmp
/*     */     //   312: ifgt -> 336
/*     */     //   315: aload_0
/*     */     //   316: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   319: aload #4
/*     */     //   321: invokestatic getKey : (Lnet/minecraft/client/input/KeyEvent;)Lcom/mojang/blaze3d/platform/InputConstants$Key;
/*     */     //   324: aload #4
/*     */     //   326: invokevirtual hasControlDownWithQuirk : ()Z
/*     */     //   329: invokevirtual handleGlobalKeyPress : (Lcom/mojang/blaze3d/platform/InputConstants$Key;Z)Z
/*     */     //   332: ifeq -> 336
/*     */     //   335: return
/*     */     //   336: iload_3
/*     */     //   337: ifeq -> 504
/*     */     //   340: aload #10
/*     */     //   342: ifnull -> 370
/*     */     //   345: aload #10
/*     */     //   347: invokevirtual getFocused : ()Lnet/minecraft/client/gui/components/events/GuiEventListener;
/*     */     //   350: instanceof net/minecraft/client/gui/components/EditBox
/*     */     //   353: ifeq -> 370
/*     */     //   356: aload #10
/*     */     //   358: invokevirtual getFocused : ()Lnet/minecraft/client/gui/components/events/GuiEventListener;
/*     */     //   361: checkcast net/minecraft/client/gui/components/EditBox
/*     */     //   364: invokevirtual canConsumeInput : ()Z
/*     */     //   367: ifne -> 374
/*     */     //   370: iconst_1
/*     */     //   371: goto -> 375
/*     */     //   374: iconst_0
/*     */     //   375: istore #11
/*     */     //   377: iload #11
/*     */     //   379: ifeq -> 504
/*     */     //   382: aload #4
/*     */     //   384: invokevirtual hasControlDownWithQuirk : ()Z
/*     */     //   387: ifeq -> 495
/*     */     //   390: aload #4
/*     */     //   392: invokevirtual key : ()I
/*     */     //   395: bipush #66
/*     */     //   397: if_icmpne -> 495
/*     */     //   400: aload_0
/*     */     //   401: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   404: invokevirtual getNarrator : ()Lnet/minecraft/client/GameNarrator;
/*     */     //   407: invokevirtual isActive : ()Z
/*     */     //   410: ifeq -> 495
/*     */     //   413: aload #6
/*     */     //   415: invokevirtual narratorHotkey : ()Lnet/minecraft/client/OptionInstance;
/*     */     //   418: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   421: checkcast java/lang/Boolean
/*     */     //   424: invokevirtual booleanValue : ()Z
/*     */     //   427: ifeq -> 495
/*     */     //   430: aload #6
/*     */     //   432: invokevirtual narrator : ()Lnet/minecraft/client/OptionInstance;
/*     */     //   435: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   438: getstatic net/minecraft/client/NarratorStatus.OFF : Lnet/minecraft/client/NarratorStatus;
/*     */     //   441: if_acmpne -> 448
/*     */     //   444: iconst_1
/*     */     //   445: goto -> 449
/*     */     //   448: iconst_0
/*     */     //   449: istore #12
/*     */     //   451: aload #6
/*     */     //   453: invokevirtual narrator : ()Lnet/minecraft/client/OptionInstance;
/*     */     //   456: aload #6
/*     */     //   458: invokevirtual narrator : ()Lnet/minecraft/client/OptionInstance;
/*     */     //   461: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   464: checkcast net/minecraft/client/NarratorStatus
/*     */     //   467: invokevirtual getId : ()I
/*     */     //   470: iconst_1
/*     */     //   471: iadd
/*     */     //   472: invokestatic byId : (I)Lnet/minecraft/client/NarratorStatus;
/*     */     //   475: invokevirtual set : (Ljava/lang/Object;)V
/*     */     //   478: aload #6
/*     */     //   480: invokevirtual save : ()V
/*     */     //   483: aload #10
/*     */     //   485: ifnull -> 495
/*     */     //   488: aload #10
/*     */     //   490: iload #12
/*     */     //   492: invokevirtual updateNarratorStatus : (Z)V
/*     */     //   495: aload_0
/*     */     //   496: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   499: getfield player : Lnet/minecraft/client/player/LocalPlayer;
/*     */     //   502: astore #12
/*     */     //   504: aload #10
/*     */     //   506: ifnull -> 687
/*     */     //   509: iload_3
/*     */     //   510: iconst_1
/*     */     //   511: if_icmpeq -> 519
/*     */     //   514: iload_3
/*     */     //   515: iconst_2
/*     */     //   516: if_icmpne -> 561
/*     */     //   519: aload #10
/*     */     //   521: invokevirtual afterKeyboardAction : ()V
/*     */     //   524: aload #10
/*     */     //   526: aload #4
/*     */     //   528: invokevirtual keyPressed : (Lnet/minecraft/client/input/KeyEvent;)Z
/*     */     //   531: ifeq -> 594
/*     */     //   534: aload_0
/*     */     //   535: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   538: getfield gui : Lnet/minecraft/client/gui/Gui;
/*     */     //   541: invokevirtual screen : ()Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   544: ifnonnull -> 560
/*     */     //   547: aload #4
/*     */     //   549: invokestatic getKey : (Lnet/minecraft/client/input/KeyEvent;)Lcom/mojang/blaze3d/platform/InputConstants$Key;
/*     */     //   552: astore #11
/*     */     //   554: aload #11
/*     */     //   556: iconst_0
/*     */     //   557: invokestatic set : (Lcom/mojang/blaze3d/platform/InputConstants$Key;Z)V
/*     */     //   560: return
/*     */     //   561: iload_3
/*     */     //   562: ifne -> 594
/*     */     //   565: aload #10
/*     */     //   567: aload #4
/*     */     //   569: invokevirtual keyReleased : (Lnet/minecraft/client/input/KeyEvent;)Z
/*     */     //   572: ifeq -> 594
/*     */     //   575: aload #6
/*     */     //   577: getfield keyDebugModifier : Lnet/minecraft/client/KeyMapping;
/*     */     //   580: aload #4
/*     */     //   582: invokevirtual matches : (Lnet/minecraft/client/input/KeyEvent;)Z
/*     */     //   585: ifeq -> 593
/*     */     //   588: aload_0
/*     */     //   589: iconst_0
/*     */     //   590: putfield usedDebugKeyAsModifier : Z
/*     */     //   593: return
/*     */     //   594: goto -> 687
/*     */     //   597: astore #11
/*     */     //   599: aload #11
/*     */     //   601: ldc_w 'keyPressed event handler'
/*     */     //   604: invokestatic forThrowable : (Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/CrashReport;
/*     */     //   607: astore #12
/*     */     //   609: aload #10
/*     */     //   611: aload #12
/*     */     //   613: invokevirtual fillCrashDetails : (Lnet/minecraft/CrashReport;)V
/*     */     //   616: aload #12
/*     */     //   618: ldc_w 'Key'
/*     */     //   621: invokevirtual addCategory : (Ljava/lang/String;)Lnet/minecraft/CrashReportCategory;
/*     */     //   624: astore #13
/*     */     //   626: aload #13
/*     */     //   628: ldc_w 'Key'
/*     */     //   631: aload #4
/*     */     //   633: invokevirtual key : ()I
/*     */     //   636: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */     //   639: invokevirtual setDetail : (Ljava/lang/String;Ljava/lang/Object;)Lnet/minecraft/CrashReportCategory;
/*     */     //   642: pop
/*     */     //   643: aload #13
/*     */     //   645: ldc_w 'Scancode'
/*     */     //   648: aload #4
/*     */     //   650: invokevirtual scancode : ()I
/*     */     //   653: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */     //   656: invokevirtual setDetail : (Ljava/lang/String;Ljava/lang/Object;)Lnet/minecraft/CrashReportCategory;
/*     */     //   659: pop
/*     */     //   660: aload #13
/*     */     //   662: ldc_w 'Mods'
/*     */     //   665: aload #4
/*     */     //   667: invokevirtual modifiers : ()I
/*     */     //   670: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */     //   673: invokevirtual setDetail : (Ljava/lang/String;Ljava/lang/Object;)Lnet/minecraft/CrashReportCategory;
/*     */     //   676: pop
/*     */     //   677: new net/minecraft/ReportedException
/*     */     //   680: dup
/*     */     //   681: aload #12
/*     */     //   683: invokespecial <init> : (Lnet/minecraft/CrashReport;)V
/*     */     //   686: athrow
/*     */     //   687: aload #4
/*     */     //   689: invokestatic getKey : (Lnet/minecraft/client/input/KeyEvent;)Lcom/mojang/blaze3d/platform/InputConstants$Key;
/*     */     //   692: astore #11
/*     */     //   694: aload_0
/*     */     //   695: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   698: getfield gui : Lnet/minecraft/client/gui/Gui;
/*     */     //   701: invokevirtual screen : ()Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   704: ifnonnull -> 711
/*     */     //   707: iconst_1
/*     */     //   708: goto -> 712
/*     */     //   711: iconst_0
/*     */     //   712: istore #12
/*     */     //   714: iload #12
/*     */     //   716: ifne -> 770
/*     */     //   719: aload_0
/*     */     //   720: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   723: getfield gui : Lnet/minecraft/client/gui/Gui;
/*     */     //   726: invokevirtual screen : ()Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   729: astore #15
/*     */     //   731: aload #15
/*     */     //   733: instanceof net/minecraft/client/gui/screens/PauseScreen
/*     */     //   736: ifeq -> 754
/*     */     //   739: aload #15
/*     */     //   741: checkcast net/minecraft/client/gui/screens/PauseScreen
/*     */     //   744: astore #14
/*     */     //   746: aload #14
/*     */     //   748: invokevirtual showsPauseMenu : ()Z
/*     */     //   751: ifeq -> 770
/*     */     //   754: aload_0
/*     */     //   755: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   758: getfield gui : Lnet/minecraft/client/gui/Gui;
/*     */     //   761: invokevirtual screen : ()Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   764: instanceof net/minecraft/client/gui/screens/debug/GameModeSwitcherScreen
/*     */     //   767: ifeq -> 774
/*     */     //   770: iconst_1
/*     */     //   771: goto -> 775
/*     */     //   774: iconst_0
/*     */     //   775: istore #13
/*     */     //   777: iload #7
/*     */     //   779: ifeq -> 827
/*     */     //   782: aload #6
/*     */     //   784: getfield keyDebugModifier : Lnet/minecraft/client/KeyMapping;
/*     */     //   787: aload #4
/*     */     //   789: invokevirtual matches : (Lnet/minecraft/client/input/KeyEvent;)Z
/*     */     //   792: ifeq -> 827
/*     */     //   795: iload_3
/*     */     //   796: ifne -> 827
/*     */     //   799: aload_0
/*     */     //   800: getfield usedDebugKeyAsModifier : Z
/*     */     //   803: ifeq -> 814
/*     */     //   806: aload_0
/*     */     //   807: iconst_0
/*     */     //   808: putfield usedDebugKeyAsModifier : Z
/*     */     //   811: goto -> 860
/*     */     //   814: aload_0
/*     */     //   815: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   818: getfield debugEntries : Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList;
/*     */     //   821: invokevirtual toggleDebugOverlay : ()V
/*     */     //   824: goto -> 860
/*     */     //   827: iload #7
/*     */     //   829: ifne -> 860
/*     */     //   832: aload #6
/*     */     //   834: getfield keyDebugOverlay : Lnet/minecraft/client/KeyMapping;
/*     */     //   837: aload #4
/*     */     //   839: invokevirtual matches : (Lnet/minecraft/client/input/KeyEvent;)Z
/*     */     //   842: ifeq -> 860
/*     */     //   845: iload_3
/*     */     //   846: iconst_1
/*     */     //   847: if_icmpne -> 860
/*     */     //   850: aload_0
/*     */     //   851: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   854: getfield debugEntries : Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList;
/*     */     //   857: invokevirtual toggleDebugOverlay : ()V
/*     */     //   860: iload_3
/*     */     //   861: ifne -> 871
/*     */     //   864: aload #11
/*     */     //   866: iconst_0
/*     */     //   867: invokestatic set : (Lcom/mojang/blaze3d/platform/InputConstants$Key;Z)V
/*     */     //   870: return
/*     */     //   871: iconst_0
/*     */     //   872: istore #14
/*     */     //   874: iload #13
/*     */     //   876: ifeq -> 903
/*     */     //   879: aload #4
/*     */     //   881: invokevirtual isEscape : ()Z
/*     */     //   884: ifeq -> 903
/*     */     //   887: aload_0
/*     */     //   888: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   891: iload #8
/*     */     //   893: invokevirtual pauseGame : (Z)V
/*     */     //   896: iload #8
/*     */     //   898: istore #14
/*     */     //   900: goto -> 963
/*     */     //   903: iload #8
/*     */     //   905: ifeq -> 963
/*     */     //   908: aload_0
/*     */     //   909: aload #4
/*     */     //   911: invokevirtual handleDebugKeys : (Lnet/minecraft/client/input/KeyEvent;)Z
/*     */     //   914: istore #14
/*     */     //   916: iload #14
/*     */     //   918: ifeq -> 963
/*     */     //   921: aload #10
/*     */     //   923: instanceof net/minecraft/client/gui/screens/debug/DebugOptionsScreen
/*     */     //   926: ifeq -> 963
/*     */     //   929: aload #10
/*     */     //   931: checkcast net/minecraft/client/gui/screens/debug/DebugOptionsScreen
/*     */     //   934: astore #15
/*     */     //   936: aload #15
/*     */     //   938: invokevirtual getOptionList : ()Lnet/minecraft/client/gui/screens/debug/DebugOptionsScreen$OptionList;
/*     */     //   941: astore #16
/*     */     //   943: aload #16
/*     */     //   945: ifnull -> 963
/*     */     //   948: aload #16
/*     */     //   950: invokevirtual children : ()Ljava/util/List;
/*     */     //   953: <illegal opcode> accept : ()Ljava/util/function/Consumer;
/*     */     //   958: invokeinterface forEach : (Ljava/util/function/Consumer;)V
/*     */     //   963: iload #7
/*     */     //   965: ifeq -> 979
/*     */     //   968: aload_0
/*     */     //   969: dup
/*     */     //   970: getfield usedDebugKeyAsModifier : Z
/*     */     //   973: iload #14
/*     */     //   975: ior
/*     */     //   976: putfield usedDebugKeyAsModifier : Z
/*     */     //   979: aload_0
/*     */     //   980: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   983: invokevirtual getDebugOverlay : ()Lnet/minecraft/client/gui/components/DebugScreenOverlay;
/*     */     //   986: invokevirtual showProfilerChart : ()Z
/*     */     //   989: ifeq -> 1025
/*     */     //   992: iload #8
/*     */     //   994: ifne -> 1025
/*     */     //   997: aload #4
/*     */     //   999: invokevirtual getDigit : ()I
/*     */     //   1002: istore #15
/*     */     //   1004: iload #15
/*     */     //   1006: iconst_m1
/*     */     //   1007: if_icmpeq -> 1025
/*     */     //   1010: aload_0
/*     */     //   1011: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   1014: invokevirtual getDebugOverlay : ()Lnet/minecraft/client/gui/components/DebugScreenOverlay;
/*     */     //   1017: invokevirtual getProfilerPieChart : ()Lnet/minecraft/client/gui/components/debugchart/ProfilerPieChart;
/*     */     //   1020: iload #15
/*     */     //   1022: invokevirtual profilerPieChartKeyPress : (I)V
/*     */     //   1025: iload #12
/*     */     //   1027: ifeq -> 1058
/*     */     //   1030: iload #14
/*     */     //   1032: ifeq -> 1044
/*     */     //   1035: aload #11
/*     */     //   1037: iconst_0
/*     */     //   1038: invokestatic set : (Lcom/mojang/blaze3d/platform/InputConstants$Key;Z)V
/*     */     //   1041: goto -> 1089
/*     */     //   1044: aload #11
/*     */     //   1046: iconst_1
/*     */     //   1047: invokestatic set : (Lcom/mojang/blaze3d/platform/InputConstants$Key;Z)V
/*     */     //   1050: aload #11
/*     */     //   1052: invokestatic click : (Lcom/mojang/blaze3d/platform/InputConstants$Key;)V
/*     */     //   1055: goto -> 1089
/*     */     //   1058: aload #11
/*     */     //   1060: aload #6
/*     */     //   1062: getfield keyDebugModifier : Lnet/minecraft/client/KeyMapping;
/*     */     //   1065: getfield key : Lcom/mojang/blaze3d/platform/InputConstants$Key;
/*     */     //   1068: if_acmpne -> 1089
/*     */     //   1071: aload #6
/*     */     //   1073: getfield keyDebugModifier : Lnet/minecraft/client/KeyMapping;
/*     */     //   1076: iload #14
/*     */     //   1078: ifne -> 1085
/*     */     //   1081: iconst_1
/*     */     //   1082: goto -> 1086
/*     */     //   1085: iconst_0
/*     */     //   1086: invokevirtual setDown : (Z)V
/*     */     //   1089: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #409	-> 0
/*     */     //   #410	-> 9
/*     */     //   #411	-> 19
/*     */     //   #414	-> 20
/*     */     //   #417	-> 30
/*     */     //   #418	-> 39
/*     */     //   #419	-> 71
/*     */     //   #420	-> 81
/*     */     //   #421	-> 123
/*     */     //   #422	-> 132
/*     */     //   #423	-> 142
/*     */     //   #425	-> 152
/*     */     //   #426	-> 162
/*     */     //   #427	-> 168
/*     */     //   #428	-> 175
/*     */     //   #429	-> 182
/*     */     //   #436	-> 187
/*     */     //   #438	-> 199
/*     */     //   #439	-> 204
/*     */     //   #441	-> 256
/*     */     //   #442	-> 269
/*     */     //   #446	-> 279
/*     */     //   #447	-> 284
/*     */     //   #448	-> 315
/*     */     //   #449	-> 335
/*     */     //   #454	-> 336
/*     */     //   #455	-> 340
/*     */     //   #456	-> 377
/*     */     //   #458	-> 382
/*     */     //   #459	-> 430
/*     */     //   #460	-> 451
/*     */     //   #461	-> 478
/*     */     //   #462	-> 483
/*     */     //   #463	-> 488
/*     */     //   #467	-> 495
/*     */     //   #475	-> 504
/*     */     //   #478	-> 509
/*     */     //   #479	-> 519
/*     */     //   #480	-> 524
/*     */     //   #483	-> 534
/*     */     //   #484	-> 547
/*     */     //   #485	-> 554
/*     */     //   #487	-> 560
/*     */     //   #489	-> 561
/*     */     //   #490	-> 565
/*     */     //   #491	-> 575
/*     */     //   #492	-> 588
/*     */     //   #494	-> 593
/*     */     //   #505	-> 594
/*     */     //   #497	-> 597
/*     */     //   #498	-> 599
/*     */     //   #499	-> 609
/*     */     //   #500	-> 616
/*     */     //   #501	-> 626
/*     */     //   #502	-> 643
/*     */     //   #503	-> 660
/*     */     //   #504	-> 677
/*     */     //   #509	-> 687
/*     */     //   #511	-> 694
/*     */     //   #514	-> 714
/*     */     //   #513	-> 719
/*     */     //   #514	-> 761
/*     */     //   #516	-> 777
/*     */     //   #517	-> 789
/*     */     //   #521	-> 799
/*     */     //   #522	-> 806
/*     */     //   #524	-> 814
/*     */     //   #526	-> 827
/*     */     //   #527	-> 839
/*     */     //   #530	-> 850
/*     */     //   #533	-> 860
/*     */     //   #534	-> 864
/*     */     //   #535	-> 870
/*     */     //   #538	-> 871
/*     */     //   #539	-> 874
/*     */     //   #540	-> 887
/*     */     //   #541	-> 896
/*     */     //   #542	-> 903
/*     */     //   #543	-> 908
/*     */     //   #544	-> 916
/*     */     //   #545	-> 921
/*     */     //   #546	-> 936
/*     */     //   #547	-> 943
/*     */     //   #548	-> 948
/*     */     //   #554	-> 963
/*     */     //   #555	-> 968
/*     */     //   #558	-> 979
/*     */     //   #559	-> 997
/*     */     //   #560	-> 1004
/*     */     //   #561	-> 1010
/*     */     //   #565	-> 1025
/*     */     //   #566	-> 1030
/*     */     //   #567	-> 1035
/*     */     //   #569	-> 1044
/*     */     //   #570	-> 1050
/*     */     //   #572	-> 1058
/*     */     //   #574	-> 1071
/*     */     //   #576	-> 1089
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   299	16	11	keyBindsScreen	Lnet/minecraft/client/gui/screens/options/controls/KeyBindsScreen;
/*     */     //   451	44	12	wasDisabled	Z
/*     */     //   377	127	11	hasNoEditboxFocused	Z
/*     */     //   554	6	11	key	Lcom/mojang/blaze3d/platform/InputConstants$Key;
/*     */     //   609	78	12	report	Lnet/minecraft/CrashReport;
/*     */     //   626	61	13	keyDetails	Lnet/minecraft/CrashReportCategory;
/*     */     //   599	88	11	t	Ljava/lang/Throwable;
/*     */     //   746	8	14	pauseScreen	Lnet/minecraft/client/gui/screens/PauseScreen;
/*     */     //   943	20	16	optionList	Lnet/minecraft/client/gui/screens/debug/DebugOptionsScreen$OptionList;
/*     */     //   936	27	15	debugOptionsScreen	Lnet/minecraft/client/gui/screens/debug/DebugOptionsScreen;
/*     */     //   1004	21	15	digit	I
/*     */     //   0	1090	0	this	Lnet/minecraft/client/KeyboardHandler;
/*     */     //   0	1090	1	handle	J
/*     */     //   0	1090	3	action	I
/*     */     //   0	1090	4	event	Lnet/minecraft/client/input/KeyEvent;
/*     */     //   9	1081	5	window	Lcom/mojang/blaze3d/platform/Window;
/*     */     //   39	1051	6	options	Lnet/minecraft/client/Options;
/*     */     //   71	1019	7	modifierAndOverlayIsSame	Z
/*     */     //   81	1009	8	debugModifierDown	Z
/*     */     //   123	967	9	debugCrash	Z
/*     */     //   199	891	10	screen	Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   694	396	11	key	Lcom/mojang/blaze3d/platform/InputConstants$Key;
/*     */     //   714	376	12	handlesGameInput	Z
/*     */     //   777	313	13	handlesGlobalInput	Z
/*     */     //   874	216	14	didDebugAction	Z
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   509	560	597	java/lang/Throwable
/*     */     //   561	593	597	java/lang/Throwable
/*     */   }
/*     */   
/*     */   private void charTyped(long handle, CharacterEvent event) {
/* 579 */     if (handle != this.minecraft.getWindow().handle())
/*     */       return; 
/* 582 */     Screen screen = this.minecraft.gui.screen();
/* 583 */     if (screen == null || this.minecraft.gui.overlay() != null)
/*     */       return; 
/*     */     try {
/* 587 */       screen.charTyped(event);
/* 588 */     } catch (Throwable t) {
/* 589 */       CrashReport report = CrashReport.forThrowable(t, "charTyped event handler");
/* 590 */       screen.fillCrashDetails(report);
/* 591 */       CrashReportCategory keyDetails = report.addCategory("Key");
/* 592 */       keyDetails.setDetail("Codepoint", Integer.valueOf(event.codepoint()));
/* 593 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void preeditCallback(long handle, PreeditEvent event) {
/* 598 */     if (handle != this.minecraft.getWindow().handle())
/*     */       return; 
/* 602 */     this.lastPreeditEvent = event;
/* 603 */     Screen screen = this.minecraft.gui.screen();
/* 604 */     if (screen == null || this.minecraft.gui.overlay() != null)
/*     */       return; 
/* 607 */     submitPreeditEvent((GuiEventListener)screen, event);
/*     */   }
/*     */   
/*     */   public void resubmitLastPreeditEvent(GuiEventListener screen) {
/* 611 */     submitPreeditEvent(screen, this.lastPreeditEvent);
/*     */   }
/*     */   
/*     */   public static void submitPreeditEvent(GuiEventListener element, PreeditEvent event) {
/*     */     try {
/* 616 */       element.preeditUpdated(event);
/* 617 */     } catch (Throwable t) {
/* 618 */       CrashReport report = CrashReport.forThrowable(t, "IME pre-edit event handler");
/* 619 */       if (element instanceof Screen) {
/* 619 */         Screen screen = (Screen)element;
/* 620 */         screen.fillCrashDetails(report);
/*     */       } 
/* 622 */       CrashReportCategory keyDetails = report.addCategory("Event");
/* 623 */       keyDetails.setDetail("Contents", () -> String.valueOf(event));
/* 624 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setup(Window window) {
/* 629 */     InputConstants.setupKeyboardCallbacks(window, (window1, keysym, scancode, action, mods) -> {
/*     */           KeyEvent event = new KeyEvent(keysym, scancode, mods);
/*     */           this.minecraft.execute(());
/*     */         }(window1, codepoint) -> {
/*     */           CharacterEvent event = new CharacterEvent(codepoint);
/*     */           this.minecraft.execute(());
/*     */         }(window1, preeditSize, preeditPtr, blockCount, blockSizesPtr, focusedBlock, caret) -> {
/*     */           PreeditEvent event = PreeditEvent.createFromCallback(preeditSize, preeditPtr, blockCount, blockSizesPtr, focusedBlock, caret);
/*     */           this.minecraft.execute(());
/*     */         }window1 -> this.minecraft.textInputManager().notifyIMEChanged());
/*     */   }
/*     */   
/*     */   public String getClipboard() {
/* 647 */     return this.clipboardManager.getClipboard(this.minecraft.getWindow(), (error, description) -> {
/*     */           if (error != 65545)
/*     */             this.minecraft.getWindow().defaultErrorCallback(error, description); 
/*     */         });
/*     */   }
/*     */   
/*     */   public void setClipboard(String clipboard) {
/* 655 */     if (!clipboard.isEmpty())
/* 656 */       this.clipboardManager.setClipboard(this.minecraft.getWindow(), clipboard); 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 661 */     if (this.debugCrashKeyTime > 0L) {
/* 662 */       long now = Util.getMillis();
/* 663 */       long remainingTime = 10000L - now - this.debugCrashKeyTime;
/* 664 */       long reportedTime = now - this.debugCrashKeyReportedTime;
/* 665 */       if (remainingTime < 0L) {
/* 666 */         if (this.minecraft.hasControlDown())
/* 667 */           Blaze3D.youJustLostTheGame(); 
/* 669 */         String message = "Manually triggered debug crash";
/* 670 */         CrashReport report = new CrashReport("Manually triggered debug crash", new Throwable("Manually triggered debug crash"));
/* 671 */         CrashReportCategory manualCrashDetails = report.addCategory("Manual crash details");
/* 672 */         NativeModuleLister.addCrashSection(manualCrashDetails);
/* 673 */         throw new ReportedException(report);
/*     */       } 
/* 675 */       if (reportedTime >= 1000L) {
/* 676 */         if (this.debugCrashKeyReportedCount == 0L) {
/* 677 */           debugFeedbackComponent((Component)Component.translatable("debug.crash.message", new Object[] { this.minecraft.options.keyDebugModifier
/* 678 */                   .getTranslatedKeyMessage(), this.minecraft.options.keyDebugCrash
/* 679 */                   .getTranslatedKeyMessage() }));
/*     */         } else {
/* 682 */           debugWarningComponent((Component)Component.translatable("debug.crash.warning", new Object[] { Integer.valueOf(Mth.ceil((float)remainingTime / 1000.0F)) }));
/*     */         } 
/* 684 */         this.debugCrashKeyReportedTime = now;
/* 685 */         this.debugCrashKeyReportedCount++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\KeyboardHandler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */