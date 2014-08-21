package com.xgr.wonderful.utils;

public class StringUtils {
	/**
	 * 检验邮箱格式是否正确
	 * @param target
	 * @return
	 */
	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}
}
