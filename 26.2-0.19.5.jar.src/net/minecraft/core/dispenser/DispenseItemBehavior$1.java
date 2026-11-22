/*    */ package net.minecraft.core.dispenser;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.PostSpawnProcessor;
/*    */ import net.minecraft.world.entity.decoration.ArmorStand;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ class null extends DefaultDispenseItemBehavior {
/*    */   public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 86 */     Direction direction = (Direction)source.state().getValue((Property)DispenserBlock.FACING);
/* 87 */     BlockPos pos = source.pos().relative(direction);
/* 88 */     ServerLevel serverLevel = source.level();
/* 89 */     PostSpawnProcessor<ArmorStand> postSpawnConfig = EntityType.appendDefaultStackConfig(armorStand -> armorStand.setYRot(direction.toYRot()), (Level)serverLevel, dispensed, null);
/* 90 */     ArmorStand armorStand = (ArmorStand)EntityTypes.ARMOR_STAND.spawn(serverLevel, postSpawnConfig, pos, EntitySpawnReason.DISPENSER, false, false);
/* 91 */     if (armorStand != null)
/* 92 */       dispensed.shrink(1); 
/* 94 */     return dispensed;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\DispenseItemBehavior$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */