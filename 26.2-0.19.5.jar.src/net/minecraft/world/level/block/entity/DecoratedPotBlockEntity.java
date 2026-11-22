/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponentPatch;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.RandomizableContainer;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.ItemContainerContents;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.ticks.ContainerSingleItem;
/*     */ 
/*     */ public class DecoratedPotBlockEntity extends BlockEntity implements ContainerSingleItem.BlockContainerSingleItem, RandomizableContainer {
/*     */   public static final String TAG_SHERDS = "sherds";
/*     */   
/*     */   public static final String TAG_ITEM = "item";
/*     */   
/*     */   public static final int EVENT_POT_WOBBLES = 1;
/*     */   
/*     */   public long wobbleStartedAtTick;
/*     */   
/*     */   public WobbleStyle lastWobbleStyle;
/*     */   
/*     */   private PotDecorations decorations;
/*     */   
/*  36 */   private ItemStack item = ItemStack.EMPTY;
/*     */   
/*     */   protected ResourceKey<LootTable> lootTable;
/*     */   
/*     */   protected long lootTableSeed;
/*     */   
/*     */   public DecoratedPotBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  41 */     super(BlockEntityTypes.DECORATED_POT, worldPosition, blockState);
/*  42 */     this.decorations = PotDecorations.EMPTY;
/*     */   }
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  47 */     super.saveAdditional(output);
/*  48 */     if (!this.decorations.equals(PotDecorations.EMPTY))
/*  49 */       output.store("sherds", PotDecorations.CODEC, this.decorations); 
/*  52 */     if (!trySaveLootTable(output) && !this.item.isEmpty())
/*  53 */       output.store("item", ItemStack.CODEC, this.item); 
/*     */   }
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  59 */     super.loadAdditional(input);
/*  60 */     this.decorations = input.read("sherds", PotDecorations.CODEC).orElse(PotDecorations.EMPTY);
/*  62 */     if (!tryLoadLootTable(input)) {
/*  63 */       this.item = input.read("item", ItemStack.CODEC).orElse(ItemStack.EMPTY);
/*     */     } else {
/*  65 */       this.item = ItemStack.EMPTY;
/*     */     } 
/*     */   }
/*     */   
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/*  71 */     return ClientboundBlockEntityDataPacket.create(this);
/*     */   }
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
/*  76 */     return saveCustomOnly(registries);
/*     */   }
/*     */   
/*     */   public Direction getDirection() {
/*  80 */     return (Direction)getBlockState().getValue((Property)BlockStateProperties.HORIZONTAL_FACING);
/*     */   }
/*     */   
/*     */   public PotDecorations getDecorations() {
/*  84 */     return this.decorations;
/*     */   }
/*     */   
/*     */   public static ItemStackTemplate createDecoratedPotTemplate(PotDecorations decorations) {
/*  88 */     return new ItemStackTemplate(Items.DECORATED_POT, 
/*  89 */         DataComponentPatch.builder()
/*  90 */         .set(DataComponents.POT_DECORATIONS, decorations)
/*  91 */         .build());
/*     */   }
/*     */   
/*     */   public static ItemStack createDecoratedPotInstance(PotDecorations decorations) {
/*  96 */     return createDecoratedPotTemplate(decorations).create();
/*     */   }
/*     */   
/*     */   public ResourceKey<LootTable> getLootTable() {
/* 101 */     return this.lootTable;
/*     */   }
/*     */   
/*     */   public void setLootTable(ResourceKey<LootTable> lootTable) {
/* 106 */     this.lootTable = lootTable;
/*     */   }
/*     */   
/*     */   public long getLootTableSeed() {
/* 111 */     return this.lootTableSeed;
/*     */   }
/*     */   
/*     */   public void setLootTableSeed(long lootTableSeed) {
/* 116 */     this.lootTableSeed = lootTableSeed;
/*     */   }
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {
/* 121 */     super.collectImplicitComponents(components);
/* 122 */     components.set(DataComponents.POT_DECORATIONS, this.decorations);
/* 123 */     components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(List.of(this.item)));
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 128 */     super.applyImplicitComponents(components);
/* 129 */     this.decorations = (PotDecorations)components.getOrDefault(DataComponents.POT_DECORATIONS, PotDecorations.EMPTY);
/* 130 */     this.item = ((ItemContainerContents)components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)).copyOne();
/*     */   }
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput output) {
/* 135 */     super.removeComponentsFromTag(output);
/* 136 */     output.discard("sherds");
/* 137 */     output.discard("item");
/*     */   }
/*     */   
/*     */   public ItemStack getTheItem() {
/* 142 */     unpackLootTable(null);
/* 143 */     return this.item;
/*     */   }
/*     */   
/*     */   public ItemStack splitTheItem(int count) {
/* 148 */     unpackLootTable(null);
/* 149 */     ItemStack result = this.item.split(count);
/* 150 */     if (this.item.isEmpty())
/* 151 */       this.item = ItemStack.EMPTY; 
/* 153 */     return result;
/*     */   }
/*     */   
/*     */   public void setTheItem(ItemStack itemStack) {
/* 158 */     unpackLootTable(null);
/* 159 */     this.item = itemStack;
/*     */   }
/*     */   
/*     */   public BlockEntity getContainerBlockEntity() {
/* 164 */     return this;
/*     */   }
/*     */   
/*     */   public void wobble(WobbleStyle wobbleStyle) {
/* 168 */     if (this.level == null || this.level.isClientSide())
/*     */       return; 
/* 171 */     this.level.blockEvent(getBlockPos(), getBlockState().getBlock(), 1, wobbleStyle.ordinal());
/*     */   }
/*     */   
/*     */   public boolean triggerEvent(int event, int data) {
/* 176 */     if (this.level != null && event == 1 && data >= 0 && data < (WobbleStyle.values()).length) {
/* 177 */       this.wobbleStartedAtTick = this.level.getGameTime();
/* 178 */       this.lastWobbleStyle = WobbleStyle.values()[data];
/* 179 */       return true;
/*     */     } 
/* 181 */     return super.triggerEvent(event, data);
/*     */   }
/*     */   
/*     */   public enum WobbleStyle {
/* 185 */     POSITIVE(7),
/* 186 */     NEGATIVE(10);
/*     */     
/*     */     public final int duration;
/*     */     
/*     */     WobbleStyle(int duration) {
/* 191 */       this.duration = duration;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\DecoratedPotBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */