/*     */ package net.minecraft.client.quickplay;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.RealmsMainScreen;
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.dto.RealmsServerList;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
/*     */ import com.mojang.realmsclient.util.task.GetServerDetailsTask;
/*     */ import com.mojang.realmsclient.util.task.LongRunningTask;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.screens.ConnectScreen;
/*     */ import net.minecraft.client.gui.screens.DisconnectedScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.TitleScreen;
/*     */ import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
/*     */ import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
/*     */ import net.minecraft.client.main.GameConfig;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.client.multiplayer.ServerList;
/*     */ import net.minecraft.client.multiplayer.resolver.ServerAddress;
/*     */ import net.minecraft.client.resources.language.I18n;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.LevelSummary;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class QuickPlay {
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  36 */   public static final Component ERROR_TITLE = (Component)Component.translatable("quickplay.error.title");
/*     */   
/*  37 */   private static final Component INVALID_IDENTIFIER = (Component)Component.translatable("quickplay.error.invalid_identifier");
/*     */   
/*  38 */   private static final Component REALM_CONNECT = (Component)Component.translatable("quickplay.error.realm_connect");
/*     */   
/*  39 */   private static final Component REALM_PERMISSION = (Component)Component.translatable("quickplay.error.realm_permission");
/*     */   
/*  41 */   private static final Component TO_TITLE = (Component)Component.translatable("gui.toTitle");
/*     */   
/*  42 */   private static final Component TO_WORLD_LIST = (Component)Component.translatable("gui.toWorld");
/*     */   
/*  43 */   private static final Component TO_REALMS_LIST = (Component)Component.translatable("gui.toRealms");
/*     */   
/*     */   public static void connect(Minecraft minecraft, GameConfig.QuickPlayVariant quickPlayVariant, RealmsClient realmsClient) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokeinterface isEnabled : ()Z
/*     */     //   6: ifne -> 34
/*     */     //   9: getstatic net/minecraft/client/quickplay/QuickPlay.LOGGER : Lorg/slf4j/Logger;
/*     */     //   12: ldc 'Quick play disabled'
/*     */     //   14: invokeinterface error : (Ljava/lang/String;)V
/*     */     //   19: aload_0
/*     */     //   20: getfield gui : Lnet/minecraft/client/gui/Gui;
/*     */     //   23: new net/minecraft/client/gui/screens/TitleScreen
/*     */     //   26: dup
/*     */     //   27: invokespecial <init> : ()V
/*     */     //   30: invokevirtual setScreen : (Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   33: return
/*     */     //   34: aload_1
/*     */     //   35: dup
/*     */     //   36: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   39: pop
/*     */     //   40: astore_3
/*     */     //   41: iconst_0
/*     */     //   42: istore #4
/*     */     //   44: aload_3
/*     */     //   45: iload #4
/*     */     //   47: <illegal opcode> typeSwitch : (Lnet/minecraft/client/main/GameConfig$QuickPlayVariant;I)I
/*     */     //   52: tableswitch default -> 84, 0 -> 94, 1 -> 112, 2 -> 131, 3 -> 170
/*     */     //   84: new java/lang/MatchException
/*     */     //   87: dup
/*     */     //   88: aconst_null
/*     */     //   89: aconst_null
/*     */     //   90: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   93: athrow
/*     */     //   94: aload_3
/*     */     //   95: checkcast net/minecraft/client/main/GameConfig$QuickPlayMultiplayerData
/*     */     //   98: astore #5
/*     */     //   100: aload_0
/*     */     //   101: aload #5
/*     */     //   103: invokevirtual serverAddress : ()Ljava/lang/String;
/*     */     //   106: invokestatic joinMultiplayerWorld : (Lnet/minecraft/client/Minecraft;Ljava/lang/String;)V
/*     */     //   109: goto -> 200
/*     */     //   112: aload_3
/*     */     //   113: checkcast net/minecraft/client/main/GameConfig$QuickPlayRealmsData
/*     */     //   116: astore #6
/*     */     //   118: aload_0
/*     */     //   119: aload_2
/*     */     //   120: aload #6
/*     */     //   122: invokevirtual realmId : ()Ljava/lang/String;
/*     */     //   125: invokestatic joinRealmsWorld : (Lnet/minecraft/client/Minecraft;Lcom/mojang/realmsclient/client/RealmsClient;Ljava/lang/String;)V
/*     */     //   128: goto -> 200
/*     */     //   131: aload_3
/*     */     //   132: checkcast net/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData
/*     */     //   135: astore #7
/*     */     //   137: aload #7
/*     */     //   139: invokevirtual worldId : ()Ljava/lang/String;
/*     */     //   142: astore #8
/*     */     //   144: aload #8
/*     */     //   146: invokestatic isBlank : (Ljava/lang/String;)Z
/*     */     //   149: ifeq -> 161
/*     */     //   152: aload_0
/*     */     //   153: invokevirtual getLevelSource : ()Lnet/minecraft/world/level/storage/LevelStorageSource;
/*     */     //   156: invokestatic getLatestSingleplayerWorld : (Lnet/minecraft/world/level/storage/LevelStorageSource;)Ljava/lang/String;
/*     */     //   159: astore #8
/*     */     //   161: aload_0
/*     */     //   162: aload #8
/*     */     //   164: invokestatic joinSingleplayerWorld : (Lnet/minecraft/client/Minecraft;Ljava/lang/String;)V
/*     */     //   167: goto -> 200
/*     */     //   170: aload_3
/*     */     //   171: checkcast net/minecraft/client/main/GameConfig$QuickPlayDisabled
/*     */     //   174: astore #8
/*     */     //   176: getstatic net/minecraft/client/quickplay/QuickPlay.LOGGER : Lorg/slf4j/Logger;
/*     */     //   179: ldc 'Quick play disabled'
/*     */     //   181: invokeinterface error : (Ljava/lang/String;)V
/*     */     //   186: aload_0
/*     */     //   187: getfield gui : Lnet/minecraft/client/gui/Gui;
/*     */     //   190: new net/minecraft/client/gui/screens/TitleScreen
/*     */     //   193: dup
/*     */     //   194: invokespecial <init> : ()V
/*     */     //   197: invokevirtual setScreen : (Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   200: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #46	-> 0
/*     */     //   #47	-> 9
/*     */     //   #48	-> 19
/*     */     //   #49	-> 33
/*     */     //   #52	-> 34
/*     */     //   #53	-> 94
/*     */     //   #54	-> 112
/*     */     //   #55	-> 131
/*     */     //   #56	-> 137
/*     */     //   #57	-> 144
/*     */     //   #58	-> 152
/*     */     //   #60	-> 161
/*     */     //   #61	-> 167
/*     */     //   #62	-> 170
/*     */     //   #63	-> 176
/*     */     //   #64	-> 186
/*     */     //   #67	-> 200
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   100	12	5	multiplayerData	Lnet/minecraft/client/main/GameConfig$QuickPlayMultiplayerData;
/*     */     //   118	13	6	realmsData	Lnet/minecraft/client/main/GameConfig$QuickPlayRealmsData;
/*     */     //   144	23	8	worldId	Ljava/lang/String;
/*     */     //   137	33	7	singlePlayerData	Lnet/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData;
/*     */     //   176	24	8	disabled	Lnet/minecraft/client/main/GameConfig$QuickPlayDisabled;
/*     */     //   0	201	0	minecraft	Lnet/minecraft/client/Minecraft;
/*     */     //   0	201	1	quickPlayVariant	Lnet/minecraft/client/main/GameConfig$QuickPlayVariant;
/*     */     //   0	201	2	realmsClient	Lcom/mojang/realmsclient/client/RealmsClient;
/*     */   }
/*     */   
/*     */   private static String getLatestSingleplayerWorld(LevelStorageSource levelSource) {
/*     */     try {
/*  71 */       List<LevelSummary> levels = levelSource.loadLevelSummaries(levelSource.findLevelCandidates()).get();
/*  72 */       if (levels.isEmpty()) {
/*  73 */         LOGGER.warn("no latest singleplayer world found");
/*  74 */         return null;
/*     */       } 
/*  76 */       return ((LevelSummary)levels.getFirst()).getLevelId();
/*  77 */     } catch (InterruptedException|java.util.concurrent.ExecutionException e) {
/*  78 */       LOGGER.error("failed to load singleplayer world summaries", e);
/*  79 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void joinSingleplayerWorld(Minecraft minecraft, String identifier) {
/*  84 */     if (StringUtil.isBlank(identifier) || !minecraft.getLevelSource().levelExists(identifier)) {
/*  85 */       SelectWorldScreen selectWorldScreen = new SelectWorldScreen((Screen)new TitleScreen());
/*  86 */       minecraft.gui.setScreen((Screen)new DisconnectedScreen((Screen)selectWorldScreen, ERROR_TITLE, INVALID_IDENTIFIER, TO_WORLD_LIST));
/*     */       return;
/*     */     } 
/*  89 */     minecraft.createWorldOpenFlows().openWorld(identifier, () -> minecraft.gui.setScreen((Screen)new TitleScreen()));
/*     */   }
/*     */   
/*     */   private static void joinMultiplayerWorld(Minecraft minecraft, String serverAddressString) {
/*  93 */     ServerList servers = new ServerList(minecraft);
/*  94 */     servers.load();
/*  95 */     ServerData serverData = servers.get(serverAddressString);
/*  96 */     if (serverData == null) {
/*  97 */       serverData = new ServerData(I18n.get("selectServer.defaultName", new Object[0]), serverAddressString, ServerData.Type.OTHER);
/*  98 */       servers.add(serverData, true);
/*  99 */       servers.save();
/*     */     } 
/* 102 */     ServerAddress serverAddress = ServerAddress.parseString(serverAddressString);
/* 103 */     ConnectScreen.startConnecting((Screen)new JoinMultiplayerScreen((Screen)new TitleScreen()), minecraft, serverAddress, serverData, true, null);
/*     */   }
/*     */   
/*     */   private static void joinRealmsWorld(Minecraft minecraft, RealmsClient realmsClient, String identifier) {
/*     */     long realmId;
/*     */     RealmsServerList realmsServerList;
/*     */     try {
/* 110 */       realmId = Long.parseLong(identifier);
/* 111 */       realmsServerList = realmsClient.listRealms();
/* 112 */     } catch (NumberFormatException e) {
/* 113 */       RealmsMainScreen realmsMainScreen = new RealmsMainScreen((Screen)new TitleScreen());
/* 114 */       minecraft.gui.setScreen((Screen)new DisconnectedScreen((Screen)realmsMainScreen, ERROR_TITLE, INVALID_IDENTIFIER, TO_REALMS_LIST));
/*     */       return;
/* 116 */     } catch (RealmsServiceException e) {
/* 117 */       TitleScreen titleScreen1 = new TitleScreen();
/* 118 */       minecraft.gui.setScreen((Screen)new DisconnectedScreen((Screen)titleScreen1, ERROR_TITLE, REALM_CONNECT, TO_TITLE));
/*     */       return;
/*     */     } 
/* 121 */     RealmsServer server = realmsServerList.servers().stream().filter(realmsServer -> (realmsServer.id == realmId)).findFirst().orElse(null);
/* 122 */     if (server == null) {
/* 123 */       RealmsMainScreen realmsMainScreen = new RealmsMainScreen((Screen)new TitleScreen());
/* 124 */       minecraft.gui.setScreen((Screen)new DisconnectedScreen((Screen)realmsMainScreen, ERROR_TITLE, REALM_PERMISSION, TO_REALMS_LIST));
/*     */       return;
/*     */     } 
/* 128 */     TitleScreen titleScreen = new TitleScreen();
/* 129 */     minecraft.gui.setScreen((Screen)new RealmsLongRunningMcoTaskScreen((Screen)titleScreen, new LongRunningTask[] { (LongRunningTask)new GetServerDetailsTask((Screen)titleScreen, server) }));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\quickplay\QuickPlay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */