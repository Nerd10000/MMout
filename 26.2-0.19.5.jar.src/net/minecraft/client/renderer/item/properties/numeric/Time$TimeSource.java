/*    */ package net.minecraft.client.renderer.item.properties.numeric;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.MoonPhase;
/*    */ 
/*    */ public enum TimeSource implements StringRepresentable {
/* 48 */   RANDOM("random") {
/*    */     public float get(ClientLevel level, ItemStack itemStack, ItemOwner owner, RandomSource random) {
/* 51 */       return random.nextFloat();
/*    */     }
/*    */   },
/* 54 */   DAYTIME("daytime") {
/*    */     public float get(ClientLevel level, ItemStack itemStack, ItemOwner owner, RandomSource random) {
/* 57 */       return ((Float)level.environmentAttributes().getValue(EnvironmentAttributes.SUN_ANGLE, owner.position())).floatValue() / 360.0F;
/*    */     }
/*    */   },
/* 60 */   MOON_PHASE("moon_phase") {
/*    */     public float get(ClientLevel level, ItemStack itemStack, ItemOwner owner, RandomSource random) {
/* 63 */       return ((MoonPhase)level.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, owner.position())).index() / MoonPhase.COUNT;
/*    */     }
/*    */   };
/*    */   
/*    */   public static final Codec<TimeSource> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 68 */     CODEC = (Codec<TimeSource>)StringRepresentable.fromEnum(TimeSource::values);
/*    */   }
/*    */   
/*    */   TimeSource(String name) {
/* 73 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 78 */     return this.name;
/*    */   }
/*    */   
/*    */   public abstract float get(ClientLevel paramClientLevel, ItemStack paramItemStack, ItemOwner paramItemOwner, RandomSource paramRandomSource);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\numeric\Time$TimeSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */