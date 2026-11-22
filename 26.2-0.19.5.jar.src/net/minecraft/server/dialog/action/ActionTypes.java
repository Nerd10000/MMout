/*    */ package net.minecraft.server.dialog.action;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ActionTypes {
/*    */   public static MapCodec<? extends Action> bootstrap(Registry<MapCodec<? extends Action>> registry) {
/*  9 */     StaticAction.WRAPPED_CODECS.forEach((action, codec) -> Registry.register(registry, Identifier.withDefaultNamespace(action.getSerializedName()), codec));
/* 12 */     Registry.register(registry, Identifier.withDefaultNamespace("dynamic/run_command"), CommandTemplate.MAP_CODEC);
/* 13 */     return (MapCodec<? extends Action>)Registry.register(registry, Identifier.withDefaultNamespace("dynamic/custom"), CustomAll.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\action\ActionTypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */