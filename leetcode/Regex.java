import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Implement regular expression matching with support for '.' and '*'.
public class Regex {
    // beats 50.80%
    public boolean isMatch(String s, String p) {
        if (s == null || p == null) return false;
        if (p.isEmpty()) return s.isEmpty();

        Matcher matcher = new Matcher(p);
        return matcher.match(s);
    }

    static class Token {
        char ch;
        boolean many;
        boolean any;
        Token(char c, boolean many) {
            ch = c;
            this.many = many;
            any = (c == '.');
        }

        int[] match(String s, int start) {
            if (many && any) return new int[] {start, s.length() + 1};

            if (start >= s.length()) {
                return many ? new int[] {start, start} : null;
            }

            char c = s.charAt(start);
            if (ch != c) {
                if (many) return new int[] {start, start};
                if (any) return new int[] {start + 1, start + 1};

                return null;
            }
            // ch == c
            if (!many) return new int[] {start + 1, start + 1};
            int end = start + 1;
            for (; end < s.length(); end++) {
                if (s.charAt(end) != c) break;
            }
            return new int[] {start, end};
        }
    }

    static class Matcher {
        Token[] tokens;
        Matcher(String p) {
            List<Token> tokenList = new ArrayList<>();
            int len = p.length();
            for (int i = 0; i < len; i++) {
                char c = p.charAt(i);
                if (c != '*') {  // * is either looked ahead or just ignored
                    tokenList.add(
                        new Token(c, (i + 1 < len) && p.charAt(i + 1) == '*'));
                }
            }
            // if keep tokens as list instead of array, "beat ratio" will
            // drop to 43.24%
            tokens = tokenList.toArray(new Token[0]);
        }

        public boolean match(String s) {
            return match(s, 0, 0);
        }

        boolean match(String s, int offset, int tokenStart) {
            int len = s.length();
            if (tokenStart == tokens.length) return offset == len;

            int[] matchRange = tokens[tokenStart].match(s, offset);
            if (matchRange == null) return false;

            ++tokenStart;
            for (int i = matchRange[1]; i >= matchRange[0]; i--) {
                if (i <= len) {
                    if (match(s, i, tokenStart)) return true;
                } else if (tokenStart >= tokens.length) {
                    return true;
                }
            }
            return false;
        }
    }

    // http://articles.leetcode.com/regular-expression-matching/
    // beats 2.28%
    public boolean isMatch2(String s, String p) {
        if (p.isEmpty()) return s.isEmpty();

        char c = p.charAt(0);
        if (p.length() == 1 || p.charAt(1) != '*') {
            if (s.length() == 0 || c != '.' && c != s.charAt(0)) return false;
            return isMatch2(s.substring(1), p.substring(1));
        }

        // p.charAt(1) == '*'
        while (s.length() > 0 && (c == '.' || c == s.charAt(0))) {
            if (isMatch2(s, p.substring(2))) return true;
            s = s.substring(1);
        }
        return isMatch2(s, p.substring(2));
    }

    // http://www.jiuzhang.com/solutions/regular-expression-matching/
    // beats 26.44%
    public boolean isMatch3(String s, String p) {
        if (p.isEmpty()) return s.isEmpty();

        if (p.length() == 1 || p.charAt(1) != '*') {
            if (!matchFirst(p, s)) return false;

            return isMatch3(s.substring(1), p.substring(1));
        }

        if (!matchFirst(p, s)) return isMatch3(s, p.substring(2));
        // fork: 1. consume the character and reuse the same pattern
        //       2. keep the character, and skip '*' pattern
        // Here is also an opportunity to use DP
        return isMatch3(s.substring(1), p) || isMatch3(s, p.substring(2));
    }

    private boolean matchFirst(String p, String s){
        if (s.isEmpty()) return false;
        return p.charAt(0) == '.' || p.charAt(0) == s.charAt(0);
    }

    void test(String s, String p, boolean expected) {
        assertEquals(expected, isMatch(s, p));
        assertEquals(expected, isMatch2(s, p));
        assertEquals(expected, isMatch3(s, p));
    }

    @Test
    public void test1() {
        test("", "", true);
        test("aa","a", false);
        test("aa","aa", true);
        test("aaa","aa", false);
        test("aa", "a*", true);
        test("aa", ".*", true);
        test("ab", ".*", true);
        test("aab", "c*a*b", true);
        test("aab", "c*a*b.", false);
        test("aabd", "c*a*b.", true);
        test("babcd", "b.*", true);
        test("babcd", "a.*", false);
        test("abcd", ".*d", true);
        test("babcd", "b.*d", true);
        test("aa", "b*a", false);
        test("aaa", "ab*a", false);
        test("", "b*", true);
        test("a", "ab*", true);
        test("a", "ab*c*", true);
        test("abcd", "a.*d", true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Regex");
    }
}