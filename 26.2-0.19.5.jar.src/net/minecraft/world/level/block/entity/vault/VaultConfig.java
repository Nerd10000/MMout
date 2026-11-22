/*    */ package net.minecraft.world.level.block.entity.vault;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.block.entity.trialspawner.PlayerDetector;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ 
/*    */ public final class VaultConfig extends Record {
/*    */   private final ResourceKey<LootTable> lootTable;
/*    */   
/*    */   private final double activationRange;
/*    */   
/*    */   private final double deactivationRange;
/*    */   
/*    */   private final ItemStack keyItem;
/*    */   
/*    */   private final Optional<ResourceKey<LootTable>> overrideLootTableToDisplay;
/*    */   
/*    */   private final PlayerDetector playerDetector;
/*    */   
/*    */   private final PlayerDetector.EntitySelector entitySelector;
/*    */   
/*    */   public static final String TAG_NAME = "config";
/*    */   
/*    */   public VaultConfig(ResourceKey<LootTable> lootTable, double activationRange, double deactivationRange, ItemStack keyItem, Optional<ResourceKey<LootTable>> overrideLootTableToDisplay, PlayerDetector playerDetector, PlayerDetector.EntitySelector entitySelector) {
/* 16 */     this.lootTable = lootTable;
/* 16 */     this.activationRange = activationRange;
/* 16 */     this.deactivationRange = deactivationRange;
/* 16 */     this.keyItem = keyItem;
/* 16 */     this.overrideLootTableToDisplay = overrideLootTableToDisplay;
/* 16 */     this.playerDetector = playerDetector;
/* 16 */     this.entitySelector = entitySelector;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/vault/VaultConfig;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/vault/VaultConfig;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/vault/VaultConfig;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/vault/VaultConfig;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/vault/VaultConfig;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/block/entity/vault/VaultConfig;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ResourceKey<LootTable> lootTable() {
/* 16 */     return this.lootTable;
/*    */   }
/*    */   
/*    */   public double activationRange() {
/* 16 */     return this.activationRange;
/*    */   }
/*    */   
/*    */   public double deactivationRange() {
/* 16 */     return this.deactivationRange;
/*    */   }
/*    */   
/*    */   public ItemStack keyItem() {
/* 16 */     return this.keyItem;
/*    */   }
/*    */   
/*    */   public Optional<ResourceKey<LootTable>> overrideLootTableToDisplay() {
/* 16 */     return this.overrideLootTableToDisplay;
/*    */   }
/*    */   
/*    */   public PlayerDetector.EntitySelector entitySelector() {
/* 16 */     return this.entitySelector;
/*    */   }
/*    */   
/* 19 */   public static final VaultConfig DEFAULT = new VaultConfig();
/*    */   
/*    */   public static final Codec<VaultConfig> CODEC;
/*    */   
/*    */   static {
/* 27 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)LootTable.KEY_CODEC.lenientOptionalFieldOf("loot_table", DEFAULT.lootTable()).forGetter(VaultConfig::lootTable), (App)Codec.DOUBLE.lenientOptionalFieldOf("activation_range", Double.valueOf(DEFAULT.activationRange())).forGetter(VaultConfig::activationRange), (App)Codec.DOUBLE.lenientOptionalFieldOf("deactivation_range", Double.valueOf(DEFAULT.deactivationRange())).forGetter(VaultConfig::deactivationRange), (App)ItemStack.lenientOptionalFieldOf("key_item").forGetter(VaultConfig::keyItem), (App)LootTable.KEY_CODEC.lenientOptionalFieldOf("override_loot_table_to_display").forGetter(VaultConfig::overrideLootTableToDisplay)).apply((Applicative)i, VaultConfig::new)).validate(VaultConfig::validate);
/*    */   }
/*    */   
/*    */   private VaultConfig() {
/* 30 */     this(BuiltInLootTables.TRIAL_CHAMBERS_REWARD, 4.0D, 4.5D, new ItemStack((ItemLike)Items.TRIAL_KEY), 
/*    */         
/* 35 */         Optional.empty(), PlayerDetector.INCLUDING_CREATIVE_PLAYERS, PlayerDetector.EntitySelector.SELECT_FROM_LEVEL);
/*    */   }
/*    */   
/*    */   public VaultConfig(ResourceKey<LootTable> lootTable, double activationRange, double deactivationRange, ItemStack keyItem, Optional<ResourceKey<LootTable>> overrideDisplayItems) {
/* 41 */     this(lootTable, activationRange, deactivationRange, keyItem, overrideDisplayItems, DEFAULT
/*    */         
/* 47 */         .playerDetector(), DEFAULT
/* 48 */         .entitySelector());
/*    */   }
/*    */   
/*    */   public PlayerDetector playerDetector() {
/* 53 */     return SharedConstants.DEBUG_VAULT_DETECTS_SHEEP_AS_PLAYERS ? PlayerDetector.SHEEP : this.playerDetector;
/*    */   }
/*    */   
/*    */   private DataResult<VaultConfig> validate() {
/* 57 */     if (this.activationRange > this.deactivationRange)
/* 58 */       return DataResult.error(() -> "Activation range must (" + this.activationRange + ") be less or equal to deactivation range (" + this.deactivationRange + ")"); 
/* 60 */     return DataResult.success(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\vault\VaultConfig.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */