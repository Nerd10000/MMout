/*     */ package net.minecraft.client.renderer.item.properties.numeric;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.entity.ItemOwner;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.LodestoneTracker;
/*     */ 
/*     */ public enum CompassTarget implements StringRepresentable {
/* 105 */   NONE("none") {
/*     */     public GlobalPos get(ClientLevel level, ItemStack itemStack, ItemOwner owner) {
/* 108 */       return null;
/*     */     }
/*     */   },
/* 111 */   LODESTONE("lodestone") {
/*     */     public GlobalPos get(ClientLevel level, ItemStack itemStack, ItemOwner owner) {
/* 114 */       LodestoneTracker tracker = (LodestoneTracker)itemStack.get(DataComponents.LODESTONE_TRACKER);
/* 115 */       return (tracker != null) ? tracker.target().orElse(null) : null;
/*     */     }
/*     */   },
/* 118 */   SPAWN("spawn") {
/*     */     public GlobalPos get(ClientLevel level, ItemStack itemStack, ItemOwner owner) {
/* 121 */       return level.getRespawnData().globalPos();
/*     */     }
/*     */   },
/* 124 */   RECOVERY("recovery") {
/*     */     public GlobalPos get(ClientLevel level, ItemStack itemStack, ItemOwner owner) {
/* 127 */       LivingEntity entity = (owner == null) ? null : owner.asLivingEntity();
/* 128 */       Player player = (Player)entity;
/* 128 */       return (entity instanceof Player) ? player.getLastDeathLocation().orElse(null) : null;
/*     */     }
/*     */   };
/*     */   
/*     */   public static final Codec<CompassTarget> CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   static {
/* 133 */     CODEC = (Codec<CompassTarget>)StringRepresentable.fromEnum(CompassTarget::values);
/*     */   }
/*     */   
/*     */   CompassTarget(String name) {
/* 138 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 143 */     return this.name;
/*     */   }
/*     */   
/*     */   public abstract GlobalPos get(ClientLevel paramClientLevel, ItemStack paramItemStack, ItemOwner paramItemOwner);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\numeric\CompassAngleState$CompassTarget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */