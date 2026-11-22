/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.world.Nameable;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.AbstractBannerBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class BannerBlockEntity extends BlockEntity implements Nameable {
/*     */   public static final int MAX_PATTERNS = 6;
/*     */   
/*     */   private static final String TAG_PATTERNS = "patterns";
/*     */   
/*  25 */   private static final Component DEFAULT_NAME = (Component)Component.translatable("block.minecraft.banner");
/*     */   
/*     */   private Component name;
/*     */   
/*     */   private final DyeColor baseColor;
/*     */   
/*  29 */   private BannerPatternLayers patterns = BannerPatternLayers.EMPTY;
/*     */   
/*     */   public BannerBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  32 */     this(worldPosition, blockState, ((AbstractBannerBlock)blockState.getBlock()).getColor());
/*     */   }
/*     */   
/*     */   public BannerBlockEntity(BlockPos worldPosition, BlockState blockState, DyeColor color) {
/*  36 */     super(BlockEntityTypes.BANNER, worldPosition, blockState);
/*  37 */     this.baseColor = color;
/*     */   }
/*     */   
/*     */   public Component getName() {
/*  42 */     if (this.name != null)
/*  43 */       return this.name; 
/*  45 */     return DEFAULT_NAME;
/*     */   }
/*     */   
/*     */   public Component getCustomName() {
/*  50 */     return this.name;
/*     */   }
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  55 */     super.saveAdditional(output);
/*  57 */     if (!this.patterns.equals(BannerPatternLayers.EMPTY))
/*  58 */       output.store("patterns", BannerPatternLayers.CODEC, this.patterns); 
/*  61 */     output.storeNullable("CustomName", ComponentSerialization.CODEC, this.name);
/*     */   }
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  66 */     super.loadAdditional(input);
/*  68 */     this.name = parseCustomNameSafe(input, "CustomName");
/*  70 */     this.patterns = input.read("patterns", BannerPatternLayers.CODEC).orElse(BannerPatternLayers.EMPTY);
/*     */   }
/*     */   
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/*  75 */     return ClientboundBlockEntityDataPacket.create(this);
/*     */   }
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
/*  81 */     return saveWithoutMetadata(registries);
/*     */   }
/*     */   
/*     */   public BannerPatternLayers getPatterns() {
/*  85 */     return this.patterns;
/*     */   }
/*     */   
/*     */   public ItemStack getItem() {
/*  89 */     ItemStack itemStack = new ItemStack((ItemLike)getBlockState().getBlock());
/*  90 */     itemStack.applyComponents(collectComponents());
/*  91 */     return itemStack;
/*     */   }
/*     */   
/*     */   public DyeColor getBaseColor() {
/*  95 */     return this.baseColor;
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 100 */     super.applyImplicitComponents(components);
/* 101 */     this.patterns = (BannerPatternLayers)components.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
/* 102 */     this.name = (Component)components.get(DataComponents.CUSTOM_NAME);
/*     */   }
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {
/* 107 */     super.collectImplicitComponents(components);
/* 108 */     components.set(DataComponents.BANNER_PATTERNS, this.patterns);
/* 109 */     components.set(DataComponents.CUSTOM_NAME, this.name);
/*     */   }
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput output) {
/* 114 */     output.discard("patterns");
/* 115 */     output.discard("CustomName");
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BannerBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */