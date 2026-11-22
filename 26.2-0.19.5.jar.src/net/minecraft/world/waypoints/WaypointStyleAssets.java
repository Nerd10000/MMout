/*    */ package net.minecraft.world.waypoints;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public interface WaypointStyleAssets {
/*  8 */   public static final ResourceKey<? extends Registry<WaypointStyleAsset>> ROOT_ID = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("waypoint_style_asset"));
/*    */   
/* 10 */   public static final ResourceKey<WaypointStyleAsset> DEFAULT = createId("default");
/*    */   
/* 11 */   public static final ResourceKey<WaypointStyleAsset> BOWTIE = createId("bowtie");
/*    */   
/*    */   static ResourceKey<WaypointStyleAsset> createId(String name) {
/* 14 */     return ResourceKey.create(ROOT_ID, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\waypoints\WaypointStyleAssets.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */