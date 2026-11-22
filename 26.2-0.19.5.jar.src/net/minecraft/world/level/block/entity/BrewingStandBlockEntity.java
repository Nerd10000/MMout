/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.WorldlyContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.BrewingStandMenu;
/*     */ import net.minecraft.world.inventory.ContainerData;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.PotionBrewing;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BrewingStandBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class BrewingStandBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
/*     */   private static final int INGREDIENT_SLOT = 3;
/*     */   
/*     */   private static final int FUEL_SLOT = 4;
/*     */   
/*  35 */   private static final int[] SLOTS_FOR_UP = new int[] { 3 };
/*     */   
/*  38 */   private static final int[] SLOTS_FOR_DOWN = new int[] { 0, 1, 2, 3 };
/*     */   
/*  41 */   private static final int[] SLOTS_FOR_SIDES = new int[] { 0, 1, 2, 4 };
/*     */   
/*     */   public static final int FUEL_USES = 20;
/*     */   
/*     */   public static final int DATA_BREW_TIME = 0;
/*     */   
/*     */   public static final int DATA_FUEL_USES = 1;
/*     */   
/*     */   public static final int NUM_DATA_VALUES = 2;
/*     */   
/*     */   private static final short DEFAULT_BREW_TIME = 0;
/*     */   
/*     */   private static final byte DEFAULT_FUEL = 0;
/*     */   
/*  54 */   private static final Component DEFAULT_NAME = (Component)Component.translatable("container.brewing");
/*     */   
/*  56 */   private NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
/*     */   
/*     */   private int brewTime;
/*     */   
/*     */   private boolean[] lastPotionCount;
/*     */   
/*     */   private Item ingredient;
/*     */   
/*     */   private int fuel;
/*     */   
/*  63 */   protected final ContainerData dataAccess = new ContainerData() {
/*     */       public int get(int dataId) {
/*  66 */         switch (dataId) {
/*     */           case 0:
/*     */           
/*     */           case 1:
/*     */           
/*     */         } 
/*  66 */         return 
/*     */           
/*  69 */           0;
/*     */       }
/*     */       
/*     */       public void set(int dataId, int value) {
/*  75 */         switch (dataId) {
/*     */           case 0:
/*  76 */             BrewingStandBlockEntity.this.brewTime = value;
/*     */             break;
/*     */           case 1:
/*  77 */             BrewingStandBlockEntity.this.fuel = value;
/*     */             break;
/*     */         } 
/*     */       }
/*     */       
/*     */       public int getCount() {
/*  83 */         return 2;
/*     */       }
/*     */     };
/*     */   
/*     */   public BrewingStandBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  88 */     super(BlockEntityTypes.BREWING_STAND, worldPosition, blockState);
/*     */   }
/*     */   
/*     */   protected Component getDefaultName() {
/*  93 */     return DEFAULT_NAME;
/*     */   }
/*     */   
/*     */   public int getContainerSize() {
/*  98 */     return this.items.size();
/*     */   }
/*     */   
/*     */   protected NonNullList<ItemStack> getItems() {
/* 103 */     return this.items;
/*     */   }
/*     */   
/*     */   protected void setItems(NonNullList<ItemStack> items) {
/* 108 */     this.items = items;
/*     */   }
/*     */   
/*     */   public static void serverTick(Level level, BlockPos pos, BlockState selfState, BrewingStandBlockEntity entity) {
/* 112 */     ItemStack fuel = (ItemStack)entity.items.get(4);
/* 113 */     if (entity.fuel <= 0 && fuel.is(ItemTags.BREWING_FUEL)) {
/* 114 */       entity.fuel = 20;
/* 115 */       fuel.shrink(1);
/* 116 */       setChanged(level, pos, selfState);
/*     */     } 
/* 119 */     boolean brewable = isBrewable(level.potionBrewing(), entity.items);
/* 120 */     boolean isBrewing = (entity.brewTime > 0);
/* 121 */     ItemStack ingredient = (ItemStack)entity.items.get(3);
/* 122 */     if (isBrewing) {
/* 123 */       entity.brewTime--;
/* 125 */       boolean isDoneBrewing = (entity.brewTime == 0);
/* 126 */       if (isDoneBrewing && brewable) {
/* 128 */         doBrew(level, pos, entity.items);
/* 129 */       } else if (!brewable || !ingredient.is(entity.ingredient)) {
/* 130 */         entity.brewTime = 0;
/*     */       } 
/* 132 */       setChanged(level, pos, selfState);
/* 133 */     } else if (brewable && entity.fuel > 0) {
/* 134 */       entity.fuel--;
/* 135 */       entity.brewTime = 400;
/* 136 */       entity.ingredient = ingredient.getItem();
/* 137 */       setChanged(level, pos, selfState);
/*     */     } 
/* 140 */     boolean[] newCount = entity.getPotionBits();
/* 141 */     if (!Arrays.equals(newCount, entity.lastPotionCount)) {
/* 142 */       entity.lastPotionCount = newCount;
/* 143 */       BlockState state = selfState;
/* 144 */       if (!(state.getBlock() instanceof BrewingStandBlock))
/*     */         return; 
/* 147 */       for (int i = 0; i < BrewingStandBlock.HAS_BOTTLE.length; i++)
/* 148 */         state = (BlockState)state.setValue((Property)BrewingStandBlock.HAS_BOTTLE[i], Boolean.valueOf(newCount[i])); 
/* 150 */       level.setBlock(pos, state, 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean[] getPotionBits() {
/* 156 */     boolean[] result = new boolean[3];
/* 157 */     for (int potion = 0; potion < 3; potion++) {
/* 158 */       if (!((ItemStack)this.items.get(potion)).isEmpty())
/* 159 */         result[potion] = true; 
/*     */     } 
/* 162 */     return result;
/*     */   }
/*     */   
/*     */   private static boolean isBrewable(PotionBrewing potionBrewing, NonNullList<ItemStack> items) {
/* 166 */     ItemStack ingredient = (ItemStack)items.get(3);
/* 167 */     if (ingredient.isEmpty())
/* 168 */       return false; 
/* 171 */     if (!potionBrewing.isIngredient(ingredient))
/* 172 */       return false; 
/* 175 */     for (int dest = 0; dest < 3; dest++) {
/* 176 */       ItemStack itemStack = (ItemStack)items.get(dest);
/* 177 */       if (!itemStack.isEmpty())
/* 181 */         if (potionBrewing.hasMix(itemStack, ingredient))
/* 182 */           return true;  
/*     */     } 
/* 185 */     return false;
/*     */   }
/*     */   
/*     */   private static void doBrew(Level level, BlockPos pos, NonNullList<ItemStack> items) {
/* 189 */     ItemStack ingredient = (ItemStack)items.get(3);
/* 190 */     PotionBrewing potionBrewing = level.potionBrewing();
/* 192 */     for (int dest = 0; dest < 3; dest++)
/* 193 */       items.set(dest, potionBrewing.mix(ingredient, (ItemStack)items.get(dest))); 
/* 196 */     ingredient.shrink(1);
/* 197 */     ItemStackTemplate remainder = ingredient.getItem().getCraftingRemainder();
/* 198 */     if (remainder != null)
/* 199 */       if (ingredient.isEmpty()) {
/* 200 */         ingredient = remainder.create();
/*     */       } else {
/* 202 */         Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), remainder.create());
/*     */       }  
/* 206 */     items.set(3, ingredient);
/* 208 */     level.levelEvent(1035, pos, 0);
/*     */   }
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 213 */     super.loadAdditional(input);
/* 215 */     this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/* 216 */     ContainerHelper.loadAllItems(input, this.items);
/* 218 */     this.brewTime = input.getShortOr("BrewTime", (short)0);
/* 219 */     if (this.brewTime > 0)
/* 220 */       this.ingredient = ((ItemStack)this.items.get(3)).getItem(); 
/* 222 */     this.fuel = input.getByteOr("Fuel", (byte)0);
/*     */   }
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 227 */     super.saveAdditional(output);
/* 229 */     output.putShort("BrewTime", (short)this.brewTime);
/* 230 */     ContainerHelper.saveAllItems(output, this.items);
/* 232 */     output.putByte("Fuel", (byte)this.fuel);
/*     */   }
/*     */   
/*     */   public boolean canPlaceItem(int slot, ItemStack itemStack) {
/* 237 */     if (slot == 3) {
/* 238 */       PotionBrewing potionBrewing = (this.level != null) ? this.level.potionBrewing() : PotionBrewing.EMPTY;
/* 239 */       return potionBrewing.isIngredient(itemStack);
/*     */     } 
/* 242 */     if (slot == 4)
/* 243 */       return itemStack.is(ItemTags.BREWING_FUEL); 
/* 246 */     return ((itemStack.is(Items.POTION) || itemStack.is(Items.SPLASH_POTION) || itemStack.is(Items.LINGERING_POTION) || itemStack.is(Items.GLASS_BOTTLE)) && getItem(slot).isEmpty());
/*     */   }
/*     */   
/*     */   public int[] getSlotsForFace(Direction direction) {
/* 251 */     if (direction == Direction.UP)
/* 252 */       return SLOTS_FOR_UP; 
/* 254 */     if (direction == Direction.DOWN)
/* 255 */       return SLOTS_FOR_DOWN; 
/* 257 */     return SLOTS_FOR_SIDES;
/*     */   }
/*     */   
/*     */   public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
/* 262 */     return canPlaceItem(slot, itemStack);
/*     */   }
/*     */   
/*     */   public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
/* 267 */     if (slot == 3)
/* 268 */       return itemStack.is(Items.GLASS_BOTTLE); 
/* 270 */     return true;
/*     */   }
/*     */   
/*     */   protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
/* 275 */     return (AbstractContainerMenu)new BrewingStandMenu(containerId, inventory, this, this.dataAccess);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BrewingStandBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */