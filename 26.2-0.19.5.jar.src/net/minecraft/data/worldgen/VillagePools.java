/*    */ package net.minecraft.data.worldgen;
/*    */ 
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ public class VillagePools {
/*    */   public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
/*  7 */     PlainVillagePools.bootstrap(context);
/*  8 */     SnowyVillagePools.bootstrap(context);
/*  9 */     SavannaVillagePools.bootstrap(context);
/* 10 */     DesertVillagePools.bootstrap(context);
/* 11 */     TaigaVillagePools.bootstrap(context);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\worldgen\VillagePools.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */