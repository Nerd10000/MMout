/*    */ package net.minecraft.server.dialog.body;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public interface DialogBody {
/*    */   public static final Codec<DialogBody> DIALOG_BODY_CODEC;
/*    */   
/*    */   MapCodec<? extends DialogBody> mapCodec();
/*    */   
/*    */   static {
/* 12 */     DIALOG_BODY_CODEC = BuiltInRegistries.DIALOG_BODY_TYPE.byNameCodec().dispatch(DialogBody::mapCodec, c -> c);
/*    */   }
/*    */   
/* 14 */   public static final Codec<List<DialogBody>> COMPACT_LIST_CODEC = ExtraCodecs.compactListCodec(DIALOG_BODY_CODEC);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\body\DialogBody.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */