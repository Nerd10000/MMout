/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.LayeredRegistryAccess;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.component.DataComponentInitializers;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.server.packs.resources.PreparableReloadListener;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.server.packs.resources.SimpleReloadInstance;
/*    */ import net.minecraft.server.permissions.PermissionSet;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ import net.minecraft.world.item.crafting.RecipeManager;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class ReloadableServerResources {
/* 25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 26 */   private static final CompletableFuture<Unit> DATA_RELOAD_INITIAL_TASK = CompletableFuture.completedFuture(Unit.INSTANCE);
/*    */   
/*    */   private final ReloadableServerRegistries.Holder fullRegistryHolder;
/*    */   
/*    */   private final Commands commands;
/*    */   
/*    */   private final RecipeManager recipes;
/*    */   
/*    */   private final ServerAdvancementManager advancements;
/*    */   
/*    */   private final ServerFunctionLibrary functionLibrary;
/*    */   
/*    */   private final List<Registry.PendingTags<?>> postponedTags;
/*    */   
/*    */   private final List<DataComponentInitializers.PendingComponents<?>> newComponents;
/*    */   
/*    */   private ReloadableServerResources(LayeredRegistryAccess<RegistryLayer> fullLayers, HolderLookup.Provider loadingContext, FeatureFlagSet enabledFeatures, Commands.CommandSelection commandSelection, List<Registry.PendingTags<?>> postponedTags, PermissionSet functionCompilationPermissions, List<DataComponentInitializers.PendingComponents<?>> newComponents) {
/* 38 */     this.fullRegistryHolder = new ReloadableServerRegistries.Holder((HolderLookup.Provider)fullLayers.compositeAccess());
/* 40 */     this.postponedTags = postponedTags;
/* 41 */     this.newComponents = newComponents;
/* 43 */     this.recipes = new RecipeManager(loadingContext);
/* 44 */     this.commands = new Commands(commandSelection, CommandBuildContext.simple(loadingContext, enabledFeatures));
/* 45 */     this.advancements = new ServerAdvancementManager(loadingContext);
/* 46 */     this.functionLibrary = new ServerFunctionLibrary(functionCompilationPermissions, this.commands.getDispatcher());
/*    */   }
/*    */   
/*    */   public ServerFunctionLibrary getFunctionLibrary() {
/* 50 */     return this.functionLibrary;
/*    */   }
/*    */   
/*    */   public ReloadableServerRegistries.Holder fullRegistries() {
/* 54 */     return this.fullRegistryHolder;
/*    */   }
/*    */   
/*    */   public RecipeManager getRecipeManager() {
/* 58 */     return this.recipes;
/*    */   }
/*    */   
/*    */   public Commands getCommands() {
/* 62 */     return this.commands;
/*    */   }
/*    */   
/*    */   public ServerAdvancementManager getAdvancements() {
/* 66 */     return this.advancements;
/*    */   }
/*    */   
/*    */   public List<PreparableReloadListener> listeners() {
/* 70 */     return (List)List.of(this.recipes, this.functionLibrary, this.advancements);
/*    */   }
/*    */   
/*    */   public static CompletableFuture<ReloadableServerResources> loadResources(ResourceManager resourceManager, LayeredRegistryAccess<RegistryLayer> contextLayers, List<Registry.PendingTags<?>> updatedContextTags, FeatureFlagSet enabledFeatures, Commands.CommandSelection commandSelection, PermissionSet functionCompilationPermissions, Executor backgroundExecutor, Executor mainThreadExecutor) {
/* 83 */     return ReloadableServerRegistries.reload(contextLayers, updatedContextTags, resourceManager, backgroundExecutor)
/* 84 */       .thenCompose(fullRegistries -> CompletableFuture.supplyAsync((), backgroundExecutor).thenCompose(()));
/*    */   }
/*    */   
/*    */   public void updateComponentsAndStaticRegistryTags() {
/* 95 */     this.postponedTags.forEach(Registry.PendingTags::apply);
/* 96 */     this.newComponents.forEach(DataComponentInitializers.PendingComponents::apply);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\ReloadableServerResources.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */