/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class ItemDisplay extends Display {
/*     */   private static final String TAG_ITEM = "item";
/*     */   
/*     */   private static final String TAG_ITEM_DISPLAY = "item_display";
/*     */   
/* 511 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK_ID = SynchedEntityData.defineId(ItemDisplay.class, EntityDataSerializers.ITEM_STACK);
/*     */   
/* 513 */   private static final EntityDataAccessor<Byte> DATA_ITEM_DISPLAY_ID = SynchedEntityData.defineId(ItemDisplay.class, EntityDataSerializers.BYTE);
/*     */   
/* 515 */   private final SlotAccess slot = SlotAccess.of(this::getItemStack, this::setItemStack);
/*     */   
/*     */   private ItemRenderState itemRenderState;
/*     */   
/*     */   public ItemDisplay(EntityType<?> type, Level level) {
/* 520 */     super(type, level);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 525 */     super.defineSynchedData(entityData);
/* 526 */     entityData.define(DATA_ITEM_STACK_ID, ItemStack.EMPTY);
/* 527 */     entityData.define(DATA_ITEM_DISPLAY_ID, Byte.valueOf(ItemDisplayContext.NONE.getId()));
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 532 */     super.onSyncedDataUpdated(accessor);
/* 534 */     if (DATA_ITEM_STACK_ID.equals(accessor) || DATA_ITEM_DISPLAY_ID.equals(accessor))
/* 535 */       this.updateRenderState = true; 
/*     */   }
/*     */   
/*     */   private ItemStack getItemStack() {
/* 540 */     return (ItemStack)this.entityData.get(DATA_ITEM_STACK_ID);
/*     */   }
/*     */   
/*     */   private void setItemStack(ItemStack item) {
/* 544 */     this.entityData.set(DATA_ITEM_STACK_ID, item);
/*     */   }
/*     */   
/*     */   private void setItemTransform(ItemDisplayContext transform) {
/* 548 */     this.entityData.set(DATA_ITEM_DISPLAY_ID, Byte.valueOf(transform.getId()));
/*     */   }
/*     */   
/*     */   private ItemDisplayContext getItemTransform() {
/* 552 */     return ItemDisplayContext.BY_ID.apply(((Byte)this.entityData.get(DATA_ITEM_DISPLAY_ID)).byteValue());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 557 */     super.readAdditionalSaveData(input);
/* 558 */     setItemStack(input.read("item", ItemStack.CODEC).orElse(ItemStack.EMPTY));
/* 559 */     setItemTransform(input.read("item_display", ItemDisplayContext.CODEC).orElse(ItemDisplayContext.NONE));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 564 */     super.addAdditionalSaveData(output);
/* 565 */     ItemStack itemStack = getItemStack();
/* 566 */     if (!itemStack.isEmpty())
/* 567 */       output.store("item", ItemStack.CODEC, itemStack); 
/* 569 */     output.store("item_display", ItemDisplayContext.CODEC, getItemTransform());
/*     */   }
/*     */   
/*     */   public SlotAccess getSlot(int slot) {
/* 574 */     if (slot == 0)
/* 575 */       return this.slot; 
/* 577 */     return null;
/*     */   }
/*     */   
/*     */   public ItemRenderState itemRenderState() {
/* 581 */     return this.itemRenderState;
/*     */   }
/*     */   
/*     */   protected void updateRenderSubState(boolean shouldInterpolate, float progress) {
/* 586 */     this.itemRenderState = new ItemRenderState(getItemStack(), getItemTransform());
/*     */   }
/*     */   
/*     */   public static final class ItemRenderState extends Record {
/*     */     private final ItemStack itemStack;
/*     */     
/*     */     private final ItemDisplayContext itemTransform;
/*     */     
/*     */     public ItemRenderState(ItemStack itemStack, ItemDisplayContext itemTransform) {
/* 589 */       this.itemStack = itemStack;
/* 589 */       this.itemTransform = itemTransform;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #589	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #589	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #589	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ItemStack itemStack() {
/* 589 */       return this.itemStack;
/*     */     }
/*     */     
/*     */     public ItemDisplayContext itemTransform() {
/* 589 */       return this.itemTransform;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Display$ItemDisplay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */