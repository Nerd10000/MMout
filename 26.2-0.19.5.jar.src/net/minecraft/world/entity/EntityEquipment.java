/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.EnumMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class EntityEquipment {
/*    */   public static final Codec<EntityEquipment> CODEC;
/*    */   
/*    */   private final EnumMap<EquipmentSlot, ItemStack> items;
/*    */   
/*    */   static {
/* 11 */     CODEC = Codec.unboundedMap((Codec)EquipmentSlot.CODEC, ItemStack.CODEC).xmap(items -> {
/*    */           EnumMap<EquipmentSlot, ItemStack> map = new EnumMap<>(EquipmentSlot.class);
/*    */           map.putAll(items);
/*    */           return new EntityEquipment(map);
/*    */         }equipment -> {
/*    */           Map<EquipmentSlot, ItemStack> items = new EnumMap<>(equipment.items);
/*    */           items.values().removeIf(ItemStack::isEmpty);
/*    */           return items;
/*    */         });
/*    */   }
/*    */   
/*    */   private EntityEquipment(EnumMap<EquipmentSlot, ItemStack> items) {
/* 27 */     this.items = items;
/*    */   }
/*    */   
/*    */   public EntityEquipment() {
/* 31 */     this(new EnumMap<>(EquipmentSlot.class));
/*    */   }
/*    */   
/*    */   public ItemStack set(EquipmentSlot slot, ItemStack itemStack) {
/* 35 */     return Objects.<ItemStack>requireNonNullElse(this.items.put(slot, itemStack), ItemStack.EMPTY);
/*    */   }
/*    */   
/*    */   public ItemStack get(EquipmentSlot slot) {
/* 39 */     return this.items.getOrDefault(slot, ItemStack.EMPTY);
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 43 */     for (ItemStack item : this.items.values()) {
/* 44 */       if (!item.isEmpty())
/* 45 */         return false; 
/*    */     } 
/* 48 */     return true;
/*    */   }
/*    */   
/*    */   public void tick(Entity owner) {
/* 52 */     for (Map.Entry<EquipmentSlot, ItemStack> entry : this.items.entrySet()) {
/* 53 */       ItemStack item = entry.getValue();
/* 54 */       if (!item.isEmpty())
/* 55 */         item.inventoryTick(owner.level(), owner, entry.getKey()); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setAll(EntityEquipment equipment) {
/* 61 */     this.items.clear();
/* 62 */     this.items.putAll(equipment.items);
/*    */   }
/*    */   
/*    */   public void dropAll(LivingEntity dropper) {
/* 66 */     for (ItemStack item : this.items.values())
/* 67 */       dropper.drop(item, true, false); 
/* 69 */     clear();
/*    */   }
/*    */   
/*    */   public void clear() {
/* 73 */     this.items.replaceAll((s, v) -> ItemStack.EMPTY);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\EntityEquipment.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */