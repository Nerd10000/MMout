/*     */ package net.minecraft.client.gui.screens.options;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.gui.components.LoadingDotsWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.worldselection.AbstractGameRulesScreen;
/*     */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundSetGameRulePacket;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ import net.minecraft.world.level.gamerules.GameRuleMap;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ public class InWorldGameRulesScreen extends AbstractGameRulesScreen implements HasGamemasterPermissionReaction {
/*  26 */   private static final Component PENDING_TEXT = (Component)Component.translatable("editGamerule.inGame.downloadingGamerules");
/*     */   
/*  27 */   private final GameRuleMap initialValues = GameRuleMap.of();
/*     */   
/*  28 */   private final List<GameRule<?>> serverProvidedRules = new ArrayList<>();
/*     */   
/*     */   private final ClientPacketListener connection;
/*     */   
/*     */   private final Screen lastScreen;
/*     */   
/*     */   private LoadingDotsWidget loadingDotsWidget;
/*     */   
/*     */   private boolean receivedServerValues = false;
/*     */   
/*     */   public InWorldGameRulesScreen(ClientPacketListener connection, Consumer<Optional<GameRules>> exitCallback, Screen lastScreen) {
/*  36 */     super(new GameRules(connection.enabledFeatures()), exitCallback);
/*  37 */     this.connection = connection;
/*  38 */     this.lastScreen = lastScreen;
/*     */   }
/*     */   
/*     */   protected void initContent() {
/*  43 */     this.loadingDotsWidget = new LoadingDotsWidget(this.font, PENDING_TEXT);
/*  44 */     this.layout.addToContents((LayoutElement)this.loadingDotsWidget);
/*  45 */     this.connection.send((Packet)new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.REQUEST_GAMERULE_VALUES));
/*     */   }
/*     */   
/*     */   protected void onDone() {
/*  52 */     List<ServerboundSetGameRulePacket.Entry> changedEntries = new ArrayList<>();
/*  53 */     this.initialValues.keySet().forEach(rule -> collectChangedGameRule(rule, changedEntries));
/*  54 */     if (!changedEntries.isEmpty())
/*  55 */       this.connection.send((Packet)new ServerboundSetGameRulePacket(changedEntries)); 
/*  57 */     closeAndApplyChanges();
/*     */   }
/*     */   
/*     */   private <T> void collectChangedGameRule(GameRule<T> rule, List<ServerboundSetGameRulePacket.Entry> entries) {
/*  61 */     if (hasGameRuleChanged(rule)) {
/*  62 */       T currentValue = (T)this.gameRules.get(rule);
/*  63 */       BuiltInRegistries.GAME_RULE.getResourceKey(rule).ifPresent(key -> entries.add(new ServerboundSetGameRulePacket.Entry(key, rule.serialize(currentValue))));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onClose() {
/*  72 */     if (hasPendingChanges()) {
/*  73 */       this.minecraft.gui.setScreen((Screen)new ConfirmScreen(confirmed -> {
/*     */               if (confirmed) {
/*     */                 closeAndDiscardChanges();
/*     */               } else {
/*     */                 this.minecraft.gui.setScreen((Screen)this);
/*     */               } 
/*  81 */             }(Component)Component.translatable("editGamerule.inGame.discardChanges.title"), 
/*  82 */             (Component)Component.translatable("editGamerule.inGame.discardChanges.message")));
/*     */     } else {
/*  85 */       closeAndDiscardChanges();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean hasPendingChanges() {
/*  90 */     return this.initialValues.keySet().stream().anyMatch(this::hasGameRuleChanged);
/*     */   }
/*     */   
/*     */   private <T> boolean hasGameRuleChanged(GameRule<T> rule) {
/*  94 */     return !this.gameRules.get(rule).equals(this.initialValues.get(rule));
/*     */   }
/*     */   
/*     */   public void onGameRuleValuesUpdated(Map<ResourceKey<GameRule<?>>, String> values) {
/*  98 */     if (this.receivedServerValues)
/*     */       return; 
/* 103 */     this.receivedServerValues = true;
/* 104 */     values.forEach((key, valueStr) -> {
/*     */           GameRule<?> rule = (GameRule)BuiltInRegistries.GAME_RULE.getValue(key);
/*     */           if (rule != null) {
/*     */             this.serverProvidedRules.add(rule);
/*     */             initializeGameRuleValue(rule, valueStr);
/*     */           } 
/*     */         });
/* 112 */     if (this.loadingDotsWidget != null)
/* 113 */       removeWidget((GuiEventListener)this.loadingDotsWidget); 
/* 116 */     GameRules serverGameRules = new GameRules(this.serverProvidedRules);
/* 117 */     this.ruleList = (AbstractGameRulesScreen.RuleList)this.layout.addToContents((LayoutElement)new AbstractGameRulesScreen.RuleList(this, serverGameRules));
/* 118 */     addRenderableWidget((GuiEventListener)this.ruleList);
/* 120 */     repositionElements();
/*     */   }
/*     */   
/*     */   private <T> void initializeGameRuleValue(GameRule<T> rule, String valueStr) {
/* 124 */     rule.deserialize(valueStr).result().ifPresent(value -> {
/*     */           this.initialValues.set(rule, value);
/*     */           this.gameRules.set(rule, value, null);
/*     */         });
/*     */   }
/*     */   
/*     */   public void onGamemasterPermissionChanged(boolean hasGamemasterPermission) {
/* 132 */     if (!hasGamemasterPermission) {
/* 133 */       this.minecraft.gui.setScreen(this.lastScreen);
/* 134 */       Screen screen = this.minecraft.gui.screen();
/* 134 */       if (screen instanceof HasGamemasterPermissionReaction) {
/* 134 */         HasGamemasterPermissionReaction hasGamemasterPermissionReaction = (HasGamemasterPermissionReaction)screen;
/* 135 */         hasGamemasterPermissionReaction.onGamemasterPermissionChanged(hasGamemasterPermission);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\options\InWorldGameRulesScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */