package com.yoursway.web.editing;

public class TextDiff {
	/**
	 * Simple stupid differ
	 */
	public static String getDiff(String first, String second) {
		if (first.equals(second))
			return null;
		if (second.length() > first.length()) {
			// added
			int start = findMCS(first, second, 1);
			int end = findMCS(first, second, -1);
			if (end <= start + second.length() - first.length())
				end = start + second.length() - first.length();
			return "(added  ) [" + start + ":" + end + "] " + second.substring(start, end);
		} else if (second.length() <= first.length()) {
			// removed or changed
			int start = findMCS(second, first, 1);
			int end = findMCS(second, first, -1);
			if (end <= start + first.length() - second.length())
				end = start + first.length() - second.length();
			if (start == end)
				return null;
			return "(removed) [" + start + ":" + end + "] "
					+ first.substring(start, end);
		}
		return null;
	}

	private static int findMCS(String src, String mask, int dir) {
		int start = 0;
		int mstart = 0;
		if (dir != 1) {
			start = src.length() - 1;
			mstart = mask.length() - 1;
		}
		while (start < src.length() && start >= 0 && mstart < mask.length()
				&& mstart >= 0) {
			if (dir == 1) {
				if (src.charAt(start) != mask.charAt(mstart))
					return start;
				start++;
				mstart++;
			} else {
				if (src.charAt(start) != mask.charAt(mstart))
					return start;
				start--;
				mstart--;
			}
		}
		return start;
	}
}
