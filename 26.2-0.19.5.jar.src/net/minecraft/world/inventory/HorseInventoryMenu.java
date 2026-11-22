/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.tags.EntityTypeTags;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ 
/*    */ public class HorseInventoryMenu extends AbstractMountInventoryMenu {
/* 12 */   private static final Identifier SADDLE_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/saddle");
/*    */   
/* 13 */   private static final Identifier LLAMA_ARMOR_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/llama_armor");
/*    */   
/* 14 */   private static final Identifier ARMOR_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/horse_armor");
/*    */   
/*    */   public HorseInventoryMenu(int containerId, Inventory playerInventory, Container horseInventory, final AbstractHorse horse, int inventoryColumns) {
/* 17 */     super(containerId, playerInventory, horseInventory, (LivingEntity)horse);
/* 19 */     Container saddleContainer = horse.createEquipmentSlotContainer(EquipmentSlot.SADDLE);
/* 20 */     addSlot(new ArmorSlot(this, saddleContainer, (LivingEntity)horse, EquipmentSlot.SADDLE, 0, 8, 18, SADDLE_SLOT_SPRITE) {
/*    */           public boolean isActive() {
/* 23 */             return (horse.canUseSlot(EquipmentSlot.SADDLE) && horse.is(EntityTypeTags.CAN_EQUIP_SADDLE));
/*    */           }
/*    */         });
/* 27 */     final boolean isLlama = horse instanceof net.minecraft.world.entity.animal.equine.Llama;
/* 28 */     Identifier armorSprite = isLlama ? LLAMA_ARMOR_SLOT_SPRITE : ARMOR_SLOT_SPRITE;
/* 29 */     Container armorContainer = horse.createEquipmentSlotContainer(EquipmentSlot.BODY);
/* 30 */     addSlot(new ArmorSlot(this, armorContainer, (LivingEntity)horse, EquipmentSlot.BODY, 0, 8, 36, armorSprite) {
/*    */           public boolean isActive() {
/* 33 */             return (horse.canUseSlot(EquipmentSlot.BODY) && (horse.is(EntityTypeTags.CAN_WEAR_HORSE_ARMOR) || isLlama));
/*    */           }
/*    */         });
/* 37 */     if (inventoryColumns > 0)
/* 38 */       for (int y = 0; y < 3; y++) {
/* 39 */         for (int x = 0; x < inventoryColumns; x++)
/* 40 */           addSlot(new Slot(horseInventory, x + y * inventoryColumns, 80 + x * 18, 18 + y * 18)); 
/*    */       }  
/* 45 */     addStandardInventorySlots((Container)playerInventory, 8, 84);
/*    */   }
/*    */   
/*    */   protected boolean hasInventoryChanged(Container container) {
/* 50 */     return ((AbstractHorse)this.mount).hasInventoryChanged(container);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\HorseInventoryMenu.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */