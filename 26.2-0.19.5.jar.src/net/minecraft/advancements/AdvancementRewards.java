/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.commands.CacheableFunction;
/*     */ import net.minecraft.commands.functions.CommandFunction;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ 
/*     */ public final class AdvancementRewards extends Record {
/*     */   private final int experience;
/*     */   
/*     */   private final List<ResourceKey<LootTable>> loot;
/*     */   
/*     */   private final List<ResourceKey<Recipe<?>>> recipes;
/*     */   
/*     */   private final Optional<CacheableFunction> function;
/*     */   
/*     */   public static final Codec<AdvancementRewards> CODEC;
/*     */   
/*     */   public AdvancementRewards(int experience, List<ResourceKey<LootTable>> loot, List<ResourceKey<Recipe<?>>> recipes, Optional<CacheableFunction> function) {
/*  26 */     this.experience = experience;
/*  26 */     this.loot = loot;
/*  26 */     this.recipes = recipes;
/*  26 */     this.function = function;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/AdvancementRewards;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #26	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/AdvancementRewards;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/AdvancementRewards;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #26	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/AdvancementRewards;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/AdvancementRewards;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #26	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/AdvancementRewards;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int experience() {
/*  26 */     return this.experience;
/*     */   }
/*     */   
/*     */   public List<ResourceKey<LootTable>> loot() {
/*  26 */     return this.loot;
/*     */   }
/*     */   
/*     */   public List<ResourceKey<Recipe<?>>> recipes() {
/*  26 */     return this.recipes;
/*     */   }
/*     */   
/*     */   public Optional<CacheableFunction> function() {
/*  26 */     return this.function;
/*     */   }
/*     */   
/*     */   static {
/*  32 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.optionalFieldOf("experience", Integer.valueOf(0)).forGetter(AdvancementRewards::experience), (App)LootTable.KEY_CODEC.listOf().optionalFieldOf("loot", List.of()).forGetter(AdvancementRewards::loot), (App)Recipe.KEY_CODEC.listOf().optionalFieldOf("recipes", List.of()).forGetter(AdvancementRewards::recipes), (App)CacheableFunction.CODEC.optionalFieldOf("function").forGetter(AdvancementRewards::function)).apply((Applicative)i, AdvancementRewards::new));
/*     */   }
/*     */   
/*  39 */   public static final AdvancementRewards EMPTY = new AdvancementRewards(0, List.of(), List.of(), Optional.empty());
/*     */   
/*     */   public void grant(ServerPlayer player) {
/*  42 */     player.giveExperiencePoints(this.experience);
/*  43 */     ServerLevel level = player.level();
/*  44 */     MinecraftServer server = level.getServer();
/*  49 */     LootParams params = (new LootParams.Builder(level)).withParameter(LootContextParams.THIS_ENTITY, player).withParameter(LootContextParams.ORIGIN, player.position()).create(LootContextParamSets.ADVANCEMENT_REWARD);
/*  51 */     boolean changes = false;
/*  52 */     for (ResourceKey<LootTable> lootTable : this.loot) {
/*  53 */       for (ObjectListIterator<ItemStack> objectListIterator = server.reloadableRegistries().getLootTable(lootTable).getRandomItems(params).iterator(); objectListIterator.hasNext(); ) {
/*  53 */         ItemStack itemStack = objectListIterator.next();
/*  54 */         if (player.addItem(itemStack)) {
/*  55 */           level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
/*  56 */           changes = true;
/*     */           continue;
/*     */         } 
/*  58 */         ItemEntity drop = player.drop(itemStack, false);
/*  59 */         if (drop != null) {
/*  60 */           drop.setNoPickUpDelay();
/*  61 */           drop.setTarget(player.getUUID());
/*     */         } 
/*     */       } 
/*     */     } 
/*  66 */     if (changes)
/*  67 */       player.containerMenu.broadcastChanges(); 
/*  69 */     if (!this.recipes.isEmpty())
/*  70 */       player.awardRecipesByKey(this.recipes); 
/*  72 */     this.function.flatMap(function -> function.get(server.getFunctions()))
/*  73 */       .ifPresent(function -> server.getFunctions().execute(function, player.createCommandSourceStack().withSuppressedOutput().withPermission((PermissionSet)LevelBasedPermissionSet.GAMEMASTER)));
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private int experience;
/*     */     
/*  78 */     private final ImmutableList.Builder<ResourceKey<LootTable>> loot = ImmutableList.builder();
/*     */     
/*  79 */     private final ImmutableList.Builder<ResourceKey<Recipe<?>>> recipes = ImmutableList.builder();
/*     */     
/*  80 */     private Optional<Identifier> function = Optional.empty();
/*     */     
/*     */     public static Builder experience(int amount) {
/*  83 */       return (new Builder()).addExperience(amount);
/*     */     }
/*     */     
/*     */     public Builder addExperience(int amount) {
/*  87 */       this.experience += amount;
/*  88 */       return this;
/*     */     }
/*     */     
/*     */     public static Builder loot(ResourceKey<LootTable> id) {
/*  92 */       return (new Builder()).addLootTable(id);
/*     */     }
/*     */     
/*     */     public Builder addLootTable(ResourceKey<LootTable> id) {
/*  96 */       this.loot.add(id);
/*  97 */       return this;
/*     */     }
/*     */     
/*     */     public static Builder recipe(ResourceKey<Recipe<?>> id) {
/* 101 */       return (new Builder()).addRecipe(id);
/*     */     }
/*     */     
/*     */     public Builder addRecipe(ResourceKey<Recipe<?>> id) {
/* 105 */       this.recipes.add(id);
/* 106 */       return this;
/*     */     }
/*     */     
/*     */     public static Builder function(Identifier id) {
/* 110 */       return (new Builder()).runs(id);
/*     */     }
/*     */     
/*     */     public Builder runs(Identifier function) {
/* 114 */       this.function = Optional.of(function);
/* 115 */       return this;
/*     */     }
/*     */     
/*     */     public AdvancementRewards build() {
/* 119 */       return new AdvancementRewards(this.experience, (List<ResourceKey<LootTable>>)this.loot.build(), (List<ResourceKey<Recipe<?>>>)this.recipes.build(), this.function.map(CacheableFunction::new));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\AdvancementRewards.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */