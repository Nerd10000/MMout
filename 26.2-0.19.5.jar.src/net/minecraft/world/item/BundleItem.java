/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ClickAction;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.inventory.tooltip.TooltipComponent;
/*     */ import net.minecraft.world.item.component.BundleContents;
/*     */ import net.minecraft.world.item.component.TooltipDisplay;
/*     */ import net.minecraft.world.level.Level;
/*     */ import org.apache.commons.lang3.math.Fraction;
/*     */ 
/*     */ public class BundleItem extends Item {
/*     */   public static final int MAX_SHOWN_GRID_ITEMS_X = 4;
/*     */   
/*     */   public static final int MAX_SHOWN_GRID_ITEMS_Y = 3;
/*     */   
/*     */   public static final int MAX_SHOWN_GRID_ITEMS = 12;
/*     */   
/*     */   public static final int OVERFLOWING_MAX_SHOWN_GRID_ITEMS = 11;
/*     */   
/*  36 */   private static final int FULL_BAR_COLOR = ARGB.colorFromFloat(1.0F, 1.0F, 0.33F, 0.33F);
/*     */   
/*  37 */   private static final int BAR_COLOR = ARGB.colorFromFloat(1.0F, 0.44F, 0.53F, 1.0F);
/*     */   
/*     */   private static final int TICKS_AFTER_FIRST_THROW = 10;
/*     */   
/*     */   private static final int TICKS_BETWEEN_THROWS = 2;
/*     */   
/*     */   private static final int TICKS_MAX_THROW_DURATION = 200;
/*     */   
/*     */   public BundleItem(Item.Properties properties) {
/*  43 */     super(properties);
/*     */   }
/*     */   
/*     */   private static Fraction getWeightSafe(BundleContents contents) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual weight : ()Lcom/mojang/serialization/DataResult;
/*     */     //   4: dup
/*     */     //   5: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   8: pop
/*     */     //   9: astore_1
/*     */     //   10: iconst_0
/*     */     //   11: istore_2
/*     */     //   12: aload_1
/*     */     //   13: iload_2
/*     */     //   14: <illegal opcode> typeSwitch : (Lcom/mojang/serialization/DataResult;I)I
/*     */     //   19: lookupswitch default -> 44, 0 -> 54, 1 -> 69
/*     */     //   44: new java/lang/MatchException
/*     */     //   47: dup
/*     */     //   48: aconst_null
/*     */     //   49: aconst_null
/*     */     //   50: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   53: athrow
/*     */     //   54: aload_1
/*     */     //   55: checkcast com/mojang/serialization/DataResult$Success
/*     */     //   58: astore_3
/*     */     //   59: aload_3
/*     */     //   60: invokevirtual value : ()Ljava/lang/Object;
/*     */     //   63: checkcast org/apache/commons/lang3/math/Fraction
/*     */     //   66: goto -> 78
/*     */     //   69: aload_1
/*     */     //   70: checkcast com/mojang/serialization/DataResult$Error
/*     */     //   73: astore #4
/*     */     //   75: getstatic org/apache/commons/lang3/math/Fraction.ONE : Lorg/apache/commons/lang3/math/Fraction;
/*     */     //   78: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #47	-> 0
/*     */     //   #48	-> 54
/*     */     //   #49	-> 69
/*     */     //   #47	-> 78
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   59	10	3	success	Lcom/mojang/serialization/DataResult$Success;
/*     */     //   75	3	4	error	Lcom/mojang/serialization/DataResult$Error;
/*     */     //   0	79	0	contents	Lnet/minecraft/world/item/component/BundleContents;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   59	10	3	success	Lcom/mojang/serialization/DataResult$Success<Lorg/apache/commons/lang3/math/Fraction;>;
/*     */     //   75	3	4	error	Lcom/mojang/serialization/DataResult$Error<*>;
/*     */   }
/*     */   
/*     */   public static float getFullnessDisplay(ItemStack itemStack) {
/*  54 */     BundleContents contents = (BundleContents)itemStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
/*  55 */     return getWeightSafe(contents).floatValue();
/*     */   }
/*     */   
/*     */   public boolean overrideStackedOnOther(ItemStack self, Slot slot, ClickAction clickAction, Player player) {
/*  60 */     BundleContents initialContents = (BundleContents)self.get(DataComponents.BUNDLE_CONTENTS);
/*  61 */     if (initialContents == null)
/*  62 */       return false; 
/*  65 */     ItemStack other = slot.getItem();
/*  66 */     BundleContents.Mutable contents = new BundleContents.Mutable(initialContents);
/*  69 */     if (clickAction == ClickAction.PRIMARY && !other.isEmpty()) {
/*  70 */       if (contents.tryTransfer(slot, player) > 0) {
/*  71 */         playInsertSound((Entity)player);
/*     */       } else {
/*  73 */         playInsertFailSound((Entity)player);
/*     */       } 
/*  75 */       self.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
/*  76 */       broadcastChangesOnContainerMenu(player);
/*  77 */       return true;
/*     */     } 
/*  81 */     if (clickAction == ClickAction.SECONDARY && other.isEmpty()) {
/*  82 */       ItemStack itemStack = contents.removeOne();
/*  83 */       if (itemStack != null) {
/*  84 */         ItemStack remainder = slot.safeInsert(itemStack);
/*  85 */         if (remainder.getCount() > 0) {
/*  86 */           contents.tryInsert(remainder);
/*     */         } else {
/*  88 */           playRemoveOneSound((Entity)player);
/*     */         } 
/*     */       } 
/*  91 */       self.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
/*  92 */       broadcastChangesOnContainerMenu(player);
/*  93 */       return true;
/*     */     } 
/*  98 */     return false;
/*     */   }
/*     */   
/*     */   public boolean overrideOtherStackedOnMe(ItemStack self, ItemStack other, Slot slot, ClickAction clickAction, Player player, SlotAccess carriedItem) {
/* 104 */     if (clickAction == ClickAction.PRIMARY && other.isEmpty()) {
/* 105 */       toggleSelectedItem(self, -1);
/* 106 */       return false;
/*     */     } 
/* 108 */     BundleContents initialContents = (BundleContents)self.get(DataComponents.BUNDLE_CONTENTS);
/* 109 */     if (initialContents == null)
/* 110 */       return false; 
/* 112 */     BundleContents.Mutable contents = new BundleContents.Mutable(initialContents);
/* 115 */     if (clickAction == ClickAction.PRIMARY && !other.isEmpty()) {
/* 116 */       if (slot.allowModification(player) && contents.tryInsert(other) > 0) {
/* 117 */         playInsertSound((Entity)player);
/*     */       } else {
/* 119 */         playInsertFailSound((Entity)player);
/*     */       } 
/* 121 */       self.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
/* 122 */       broadcastChangesOnContainerMenu(player);
/* 123 */       return true;
/*     */     } 
/* 127 */     if (clickAction == ClickAction.SECONDARY && other.isEmpty()) {
/* 128 */       if (slot.allowModification(player)) {
/* 129 */         ItemStack removed = contents.removeOne();
/* 130 */         if (removed != null) {
/* 131 */           playRemoveOneSound((Entity)player);
/* 132 */           carriedItem.set(removed);
/*     */         } 
/*     */       } 
/* 135 */       self.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
/* 136 */       broadcastChangesOnContainerMenu(player);
/* 137 */       return true;
/*     */     } 
/* 141 */     toggleSelectedItem(self, -1);
/* 142 */     return false;
/*     */   }
/*     */   
/*     */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 147 */     player.startUsingItem(hand);
/* 148 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private void dropContent(Level level, Player player, ItemStack itemStack) {
/* 152 */     if (dropContent(itemStack, player)) {
/* 153 */       playDropContentsSound(level, (Entity)player);
/* 154 */       player.awardStat(Stats.ITEM_USED.get(this));
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isBarVisible(ItemStack stack) {
/* 160 */     BundleContents contents = (BundleContents)stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
/* 161 */     return (getWeightSafe(contents).compareTo(Fraction.ZERO) > 0);
/*     */   }
/*     */   
/*     */   public int getBarWidth(ItemStack stack) {
/* 166 */     BundleContents contents = (BundleContents)stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
/* 168 */     return Math.min(1 + Mth.mulAndTruncate(getWeightSafe(contents), 12), 13);
/*     */   }
/*     */   
/*     */   public int getBarColor(ItemStack stack) {
/* 173 */     BundleContents contents = (BundleContents)stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
/* 174 */     return (getWeightSafe(contents).compareTo(Fraction.ONE) >= 0) ? FULL_BAR_COLOR : BAR_COLOR;
/*     */   }
/*     */   
/*     */   public static void toggleSelectedItem(ItemStack stack, int selectedItem) {
/* 178 */     BundleContents initialContents = (BundleContents)stack.get(DataComponents.BUNDLE_CONTENTS);
/* 179 */     if (initialContents == null)
/*     */       return; 
/* 183 */     BundleContents.Mutable contents = new BundleContents.Mutable(initialContents);
/* 184 */     contents.toggleSelectedItem(selectedItem);
/* 185 */     stack.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
/*     */   }
/*     */   
/*     */   public static int getSelectedItemIndex(ItemStack stack) {
/* 189 */     return ((BundleContents)stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)).getSelectedItemIndex();
/*     */   }
/*     */   
/*     */   public static ItemStackTemplate getSelectedItem(ItemStack stack) {
/* 193 */     return ((BundleContents)stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)).getSelectedItem();
/*     */   }
/*     */   
/*     */   public static int getNumberOfItemsToShow(ItemStack stack) {
/* 197 */     BundleContents contents = (BundleContents)stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
/* 198 */     return contents.getNumberOfItemsToShow();
/*     */   }
/*     */   
/*     */   private boolean dropContent(ItemStack bundle, Player player) {
/* 202 */     BundleContents contents = (BundleContents)bundle.get(DataComponents.BUNDLE_CONTENTS);
/* 203 */     if (contents == null || contents.isEmpty())
/* 204 */       return false; 
/* 207 */     Optional<ItemStack> itemStack = removeOneItemFromBundle(bundle, player, contents);
/* 208 */     if (itemStack.isPresent()) {
/* 209 */       player.drop(itemStack.get(), true);
/* 210 */       return true;
/*     */     } 
/* 212 */     return false;
/*     */   }
/*     */   
/*     */   private static Optional<ItemStack> removeOneItemFromBundle(ItemStack self, Player player, BundleContents initialContents) {
/* 216 */     BundleContents.Mutable contents = new BundleContents.Mutable(initialContents);
/* 217 */     ItemStack removed = contents.removeOne();
/* 218 */     if (removed != null) {
/* 219 */       playRemoveOneSound((Entity)player);
/* 220 */       self.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
/* 221 */       return Optional.of(removed);
/*     */     } 
/* 223 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
/* 228 */     if (livingEntity instanceof Player) {
/* 228 */       Player player = (Player)livingEntity;
/* 229 */       int useDuration = getUseDuration(itemStack, livingEntity);
/* 230 */       boolean isFirstTick = (ticksRemaining == useDuration);
/* 231 */       if (isFirstTick || (ticksRemaining < useDuration - 10 && ticksRemaining % 2 == 0))
/* 232 */         dropContent(level, player, itemStack); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getUseDuration(ItemStack itemStack, LivingEntity entity) {
/* 239 */     return 200;
/*     */   }
/*     */   
/*     */   public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
/* 244 */     return ItemUseAnimation.BUNDLE;
/*     */   }
/*     */   
/*     */   public Optional<TooltipComponent> getTooltipImage(ItemStack bundle) {
/* 249 */     TooltipDisplay display = (TooltipDisplay)bundle.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
/* 250 */     if (!display.shows(DataComponents.BUNDLE_CONTENTS))
/* 251 */       return Optional.empty(); 
/* 253 */     return Optional.<BundleContents>ofNullable((BundleContents)bundle.get(DataComponents.BUNDLE_CONTENTS)).map(net.minecraft.world.inventory.tooltip.BundleTooltip::new);
/*     */   }
/*     */   
/*     */   public void onDestroyed(ItemEntity entity) {
/* 258 */     BundleContents contents = (BundleContents)entity.getItem().get(DataComponents.BUNDLE_CONTENTS);
/* 259 */     if (contents == null)
/*     */       return; 
/* 262 */     entity.getItem().set(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
/* 263 */     ItemUtils.onContainerDestroyed(entity, contents.itemCopyStream());
/*     */   }
/*     */   
/*     */   private static void playRemoveOneSound(Entity entity) {
/* 267 */     entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
/*     */   }
/*     */   
/*     */   private static void playInsertSound(Entity entity) {
/* 271 */     entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
/*     */   }
/*     */   
/*     */   private static void playInsertFailSound(Entity entity) {
/* 275 */     entity.playSound(SoundEvents.BUNDLE_INSERT_FAIL, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   private static void playDropContentsSound(Level level, Entity entity) {
/* 279 */     level.playSound(null, entity.blockPosition(), SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.PLAYERS, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
/*     */   }
/*     */   
/*     */   private void broadcastChangesOnContainerMenu(Player player) {
/* 283 */     AbstractContainerMenu containerMenu = player.containerMenu;
/* 284 */     if (containerMenu != null)
/* 285 */       containerMenu.slotsChanged((Container)player.getInventory()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\BundleItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */