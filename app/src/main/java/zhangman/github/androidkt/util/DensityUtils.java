package zhangman.github.androidkt.util;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * 常用单位转换的辅助类
 */
public class DensityUtils {
  private static int screenWidth = 0;
  private static int screenHeight = 0;

  private DensityUtils() {
        /* cannot be instantiated */
    throw new UnsupportedOperationException("cannot be instantiated");
  }

  /**
   * dp转px
   */
  public static int dp2px(Context context, float dpVal) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
        context.getResources().getDisplayMetrics());
  }

  /**
   * sp转px
   */
  public static int sp2px(Context context, float spVal) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal,
        context.getResources().getDisplayMetrics());
  }

  /**
   * px转dp
   */
  public static float px2dp(Context context, float pxVal) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (pxVal / scale);
  }

  /**
   * px转sp
   */
  public static float px2sp(Context context, float pxVal) {
    return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
  }

  public static int getScreenHeight(Context c) {
    if (screenHeight == 0) {
      WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
      Display display = wm.getDefaultDisplay();
      Point size = new Point();
      display.getSize(size);
      screenHeight = size.y;
    }

    return screenHeight;
  }

  public static int getScreenWidth(Context c) {
    if (screenWidth == 0) {
      WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
      Display display = wm.getDefaultDisplay();
      Point size = new Point();
      display.getSize(size);
      screenWidth = size.x;
    }

    return screenWidth;
  }

  public static boolean isAndroid5() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
  }
}
