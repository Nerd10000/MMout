/*    */ package net.minecraft.world.item.equipment;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ 
/*    */ public enum ArmorType implements StringRepresentable {
/*  8 */   HELMET(EquipmentSlot.HEAD, 11, "helmet"),
/*  9 */   CHESTPLATE(EquipmentSlot.CHEST, 16, "chestplate"),
/* 10 */   LEGGINGS(EquipmentSlot.LEGS, 15, "leggings"),
/* 11 */   BOOTS(EquipmentSlot.FEET, 13, "boots"),
/* 12 */   BODY(EquipmentSlot.BODY, 16, "body");
/*    */   
/*    */   public static final Codec<ArmorType> CODEC;
/*    */   
/*    */   private final EquipmentSlot slot;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final int unitDurability;
/*    */   
/*    */   static {
/* 15 */     CODEC = StringRepresentable.fromValues(ArmorType::values);
/*    */   }
/*    */   
/*    */   ArmorType(EquipmentSlot slot, int unitDurability, String name) {
/* 22 */     this.slot = slot;
/* 23 */     this.name = name;
/* 24 */     this.unitDurability = unitDurability;
/*    */   }
/*    */   
/*    */   public int getDurability(int multiplier) {
/* 28 */     return this.unitDurability * multiplier;
/*    */   }
/*    */   
/*    */   public EquipmentSlot getSlot() {
/* 32 */     return this.slot;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 36 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 41 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\equipment\ArmorType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */