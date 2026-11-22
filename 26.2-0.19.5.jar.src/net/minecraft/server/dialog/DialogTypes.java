/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class DialogTypes {
/*    */   public static MapCodec<? extends Dialog> bootstrap(Registry<MapCodec<? extends Dialog>> registry) {
/*  8 */     Registry.register(registry, "notice", NoticeDialog.MAP_CODEC);
/*  9 */     Registry.register(registry, "server_links", ServerLinksDialog.MAP_CODEC);
/* 10 */     Registry.register(registry, "dialog_list", DialogListDialog.MAP_CODEC);
/* 11 */     Registry.register(registry, "multi_action", MultiActionDialog.MAP_CODEC);
/* 12 */     return (MapCodec<? extends Dialog>)Registry.register(registry, "confirmation", ConfirmationDialog.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\DialogTypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */