package net.minecraft.server.dialog;

import com.mojang.serialization.MapCodec;
import java.util.List;

public interface SimpleDialog extends Dialog {
  MapCodec<? extends SimpleDialog> codec();
  
  List<ActionButton> mainActions();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\SimpleDialog.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */