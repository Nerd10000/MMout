/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.IdMap;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentInstance;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.EnchantingTableBlock;
/*     */ 
/*     */ public class EnchantmentMenu extends AbstractContainerMenu {
/*  34 */   private static final Identifier EMPTY_SLOT_LAPIS_LAZULI = Identifier.withDefaultNamespace("container/slot/lapis_lazuli");
/*     */   
/*  36 */   private final Container enchantSlots = (Container)new SimpleContainer(2) {
/*     */       public void setChanged() {
/*  39 */         super.setChanged();
/*  40 */         EnchantmentMenu.this.slotsChanged((Container)this);
/*     */       }
/*     */     };
/*     */   
/*     */   private final ContainerLevelAccess access;
/*     */   
/*  45 */   private final RandomSource random = RandomSource.create();
/*     */   
/*  46 */   private final DataSlot enchantmentSeed = DataSlot.standalone();
/*     */   
/*  48 */   public final int[] costs = new int[3];
/*     */   
/*  49 */   public final int[] enchantClue = new int[] { -1, -1, -1 };
/*     */   
/*  50 */   public final int[] levelClue = new int[] { -1, -1, -1 };
/*     */   
/*     */   public EnchantmentMenu(int containerId, Inventory inventory) {
/*  53 */     this(containerId, inventory, ContainerLevelAccess.NULL);
/*     */   }
/*     */   
/*     */   public EnchantmentMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
/*  57 */     super(MenuType.ENCHANTMENT, containerId);
/*  58 */     this.access = access;
/*  59 */     addSlot(new Slot(this, this.enchantSlots, 0, 15, 47) {
/*     */           public int getMaxStackSize() {
/*  62 */             return 1;
/*     */           }
/*     */         });
/*  66 */     addSlot(new Slot(this, this.enchantSlots, 1, 35, 47) {
/*     */           public boolean mayPlace(ItemStack itemStack) {
/*  69 */             return itemStack.is(Items.LAPIS_LAZULI);
/*     */           }
/*     */           
/*     */           public Identifier getNoItemIcon() {
/*  74 */             return EnchantmentMenu.EMPTY_SLOT_LAPIS_LAZULI;
/*     */           }
/*     */         });
/*  78 */     addStandardInventorySlots((Container)inventory, 8, 84);
/*  80 */     addDataSlot(DataSlot.shared(this.costs, 0));
/*  81 */     addDataSlot(DataSlot.shared(this.costs, 1));
/*  82 */     addDataSlot(DataSlot.shared(this.costs, 2));
/*  84 */     addDataSlot(this.enchantmentSeed).set(inventory.player.getEnchantmentSeed());
/*  86 */     addDataSlot(DataSlot.shared(this.enchantClue, 0));
/*  87 */     addDataSlot(DataSlot.shared(this.enchantClue, 1));
/*  88 */     addDataSlot(DataSlot.shared(this.enchantClue, 2));
/*  90 */     addDataSlot(DataSlot.shared(this.levelClue, 0));
/*  91 */     addDataSlot(DataSlot.shared(this.levelClue, 1));
/*  92 */     addDataSlot(DataSlot.shared(this.levelClue, 2));
/*     */   }
/*     */   
/*     */   public void slotsChanged(Container container) {
/*  97 */     if (container == this.enchantSlots) {
/*  98 */       ItemStack itemStack = container.getItem(0);
/* 100 */       if (itemStack.isEmpty() || !itemStack.isEnchantable()) {
/* 101 */         for (int i = 0; i < 3; i++) {
/* 102 */           this.costs[i] = 0;
/* 103 */           this.enchantClue[i] = -1;
/* 104 */           this.levelClue[i] = -1;
/*     */         } 
/*     */       } else {
/* 107 */         this.access.execute((level, pos) -> {
/*     */               IdMap<Holder<Enchantment>> holders = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).asHolderIdMap();
/*     */               int bookcases = 0;
/*     */               for (BlockPos offset : EnchantingTableBlock.BOOKSHELF_OFFSETS) {
/*     */                 if (EnchantingTableBlock.isValidBookShelf(level, pos, offset))
/*     */                   bookcases++; 
/*     */               } 
/*     */               this.random.setSeed(this.enchantmentSeed.get());
/*     */               int i;
/*     */               for (i = 0; i < 3; i++) {
/*     */                 this.costs[i] = EnchantmentHelper.getEnchantmentCost(this.random, i, bookcases, itemStack);
/*     */                 this.enchantClue[i] = -1;
/*     */                 this.levelClue[i] = -1;
/*     */                 if (this.costs[i] < i + 1)
/*     */                   this.costs[i] = 0; 
/*     */               } 
/*     */               for (i = 0; i < 3; i++) {
/*     */                 if (this.costs[i] > 0) {
/*     */                   List<EnchantmentInstance> list = getEnchantmentList(level.registryAccess(), itemStack, i, this.costs[i]);
/*     */                   if (!list.isEmpty()) {
/*     */                     EnchantmentInstance ench = list.get(this.random.nextInt(list.size()));
/*     */                     this.enchantClue[i] = holders.getId(ench.enchantment());
/*     */                     this.levelClue[i] = ench.level();
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */               broadcastChanges();
/*     */             });
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean clickMenuButton(Player player, int buttonId) {
/* 147 */     if (buttonId < 0 || buttonId >= this.costs.length) {
/* 148 */       Util.logAndPauseIfInIde(player.getPlainTextName() + " pressed invalid button id: " + player.getPlainTextName());
/* 149 */       return false;
/*     */     } 
/* 152 */     ItemStack itemStack = this.enchantSlots.getItem(0);
/* 153 */     ItemStack currency = this.enchantSlots.getItem(1);
/* 155 */     int enchantmentCost = buttonId + 1;
/* 157 */     if ((currency.isEmpty() || currency.getCount() < enchantmentCost) && !player.hasInfiniteMaterials())
/* 158 */       return false; 
/* 161 */     if (this.costs[buttonId] > 0 && !itemStack.isEmpty() && ((player.experienceLevel >= enchantmentCost && player.experienceLevel >= this.costs[buttonId]) || player.hasInfiniteMaterials())) {
/* 162 */       this.access.execute((level, pos) -> {
/*     */             ItemStack enchantmentItem = itemStack;
/*     */             List<EnchantmentInstance> newEnchantment = getEnchantmentList(level.registryAccess(), enchantmentItem, buttonId, this.costs[buttonId]);
/*     */             if (!newEnchantment.isEmpty()) {
/*     */               player.onEnchantmentPerformed(enchantmentItem, enchantmentCost);
/*     */               if (enchantmentItem.is(Items.BOOK)) {
/*     */                 enchantmentItem = itemStack.transmuteCopy((ItemLike)Items.ENCHANTED_BOOK);
/*     */                 this.enchantSlots.setItem(0, enchantmentItem);
/*     */               } 
/*     */               for (EnchantmentInstance enchantment : newEnchantment)
/*     */                 enchantmentItem.enchant(enchantment.enchantment(), enchantment.level()); 
/*     */               currency.consume(enchantmentCost, (LivingEntity)player);
/*     */               if (currency.isEmpty())
/*     */                 this.enchantSlots.setItem(1, ItemStack.EMPTY); 
/*     */               player.awardStat(Stats.ENCHANT_ITEM);
/*     */               if (player instanceof ServerPlayer) {
/*     */                 ServerPlayer serverPlayer = (ServerPlayer)player;
/*     */                 CriteriaTriggers.ENCHANTED_ITEM.trigger(serverPlayer, enchantmentItem, enchantmentCost);
/*     */               } 
/*     */               this.enchantSlots.setChanged();
/*     */               this.enchantmentSeed.set(player.getEnchantmentSeed());
/*     */               slotsChanged(this.enchantSlots);
/*     */               level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
/*     */             } 
/*     */           });
/* 194 */       return true;
/*     */     } 
/* 196 */     return false;
/*     */   }
/*     */   
/*     */   private List<EnchantmentInstance> getEnchantmentList(RegistryAccess access, ItemStack itemStack, int slot, int enchantmentCost) {
/* 200 */     this.random.setSeed((this.enchantmentSeed.get() + slot));
/* 202 */     Optional<HolderSet.Named<Enchantment>> tag = access.lookupOrThrow(Registries.ENCHANTMENT).get(EnchantmentTags.IN_ENCHANTING_TABLE);
/* 203 */     if (tag.isEmpty())
/* 204 */       return List.of(); 
/* 206 */     List<EnchantmentInstance> list = EnchantmentHelper.selectEnchantment(this.random, itemStack, enchantmentCost, ((HolderSet.Named)tag.get()).stream());
/* 208 */     if (itemStack.is(Items.BOOK) && list.size() > 1)
/* 210 */       list.remove(this.random.nextInt(list.size())); 
/* 212 */     return list;
/*     */   }
/*     */   
/*     */   public int getGoldCount() {
/* 216 */     ItemStack goldStack = this.enchantSlots.getItem(1);
/* 217 */     if (goldStack.isEmpty())
/* 218 */       return 0; 
/* 220 */     return goldStack.getCount();
/*     */   }
/*     */   
/*     */   public int getEnchantmentSeed() {
/* 224 */     return this.enchantmentSeed.get();
/*     */   }
/*     */   
/*     */   public void removed(Player player) {
/* 229 */     super.removed(player);
/* 230 */     this.access.execute((level, pos) -> clearContainer(player, this.enchantSlots));
/*     */   }
/*     */   
/*     */   public boolean stillValid(Player player) {
/* 235 */     return stillValid(this.access, player, Blocks.ENCHANTING_TABLE);
/*     */   }
/*     */   
/*     */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/* 240 */     ItemStack clicked = ItemStack.EMPTY;
/* 241 */     Slot slot = (Slot)this.slots.get(slotIndex);
/* 242 */     if (slot != null && slot.hasItem()) {
/* 243 */       ItemStack stack = slot.getItem();
/* 244 */       clicked = stack.copy();
/* 246 */       if (slotIndex == 0) {
/* 247 */         if (!moveItemStackTo(stack, 2, 38, true))
/* 248 */           return ItemStack.EMPTY; 
/* 250 */       } else if (slotIndex == 1) {
/* 251 */         if (!moveItemStackTo(stack, 2, 38, true))
/* 252 */           return ItemStack.EMPTY; 
/* 254 */       } else if (stack.is(Items.LAPIS_LAZULI)) {
/* 255 */         if (!moveItemStackTo(stack, 1, 2, true))
/* 256 */           return ItemStack.EMPTY; 
/* 258 */       } else if (!((Slot)this.slots.get(0)).hasItem() && ((Slot)this.slots.get(0)).mayPlace(stack)) {
/* 259 */         ItemStack singleItem = stack.copyWithCount(1);
/* 260 */         stack.shrink(1);
/* 261 */         ((Slot)this.slots.get(0)).setByPlayer(singleItem);
/*     */       } else {
/* 263 */         return ItemStack.EMPTY;
/*     */       } 
/* 265 */       if (stack.isEmpty()) {
/* 266 */         slot.setByPlayer(ItemStack.EMPTY);
/*     */       } else {
/* 268 */         slot.setChanged();
/*     */       } 
/* 270 */       if (stack.getCount() == clicked.getCount())
/* 271 */         return ItemStack.EMPTY; 
/* 273 */       slot.onTake(player, stack);
/*     */     } 
/* 276 */     return clicked;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\EnchantmentMenu.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */