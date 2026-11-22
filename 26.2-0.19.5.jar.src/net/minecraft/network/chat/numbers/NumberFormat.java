package net.minecraft.network.chat.numbers;

import net.minecraft.network.chat.MutableComponent;

public interface NumberFormat {
  MutableComponent format(int paramInt);
  
  NumberFormatType<? extends NumberFormat> type();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\numbers\NumberFormat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */